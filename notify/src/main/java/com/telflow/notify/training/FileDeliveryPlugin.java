/*
 * Copyright (c) 2010-2017 DGIT Systems Pty. Ltd. All Rights Reserved.
 *
 * This program and the accompanying materials are the property of DGIT
 * Systems Pty. Ltd.
 *
 * You may obtain a copy of the Licence at http://www.dgit.biz/licence
 */

package com.telflow.notify.training;

import biz.dgit.schemas.telflow.cim.v3.OperationNotification;
import com.telflow.notify.core.interfaces.NotifyDeliveryPlugin;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Write notifications to a file in a directory.
 *
 * @author laurie
 */
public class FileDeliveryPlugin implements NotifyDeliveryPlugin {

    private static final transient Logger LOG = LoggerFactory.getLogger(FileDeliveryPlugin.class);

    /**
     * If found in parameters, will be used as the file name to write the notification to.
     */
    private static final String KEY_FILE_NAME = "fileName";

    private final File baseDirectory;

    private final AtomicLong sequence;

    public FileDeliveryPlugin(File baseDirectory) {
        this.baseDirectory = baseDirectory.getAbsoluteFile();
        this.sequence = new AtomicLong();
    }

    @Override
    public void deliverNotification(OperationNotification notification, HashMap<String, byte[]> parameters) {
        writeNotificationParameters(parameters);
    }

    /**
     * Write the notification parameters to a file.
     *
     * @param parameters to be written
     * @return the File they were written to
     */
    public File writeNotificationParameters(HashMap<String, byte[]> parameters) {
        if (!this.baseDirectory.isDirectory() && !this.baseDirectory.mkdirs()) {
            throw new RuntimeException(new FileNotFoundException(this.baseDirectory.getAbsolutePath()));
        }
        String filename = String.format("training-notification-%s-%s.txt",
            new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date()), this.sequence.getAndIncrement());
        if (parameters.containsKey(KEY_FILE_NAME)) {
            filename = new String(parameters.get(KEY_FILE_NAME), StandardCharsets.UTF_8);
        }
        File out = new File(this.baseDirectory, filename);
        LOG.info("Writing notification to {}.", out);
        try (FileOutputStream os = new FileOutputStream(out)) {
            parameters.entrySet().stream().forEachOrdered(x -> write(os, x.getKey(), x.getValue()));
        } catch (IOException x) {
            throw new RuntimeException(out.getAbsolutePath(), x);
        }
        return out;
    }

    private static void write(FileOutputStream os, String key, byte[] value) {
        try {
            IOUtils.write(key, os, StandardCharsets.UTF_8);
            IOUtils.write(": ", os, StandardCharsets.UTF_8);
            IOUtils.write(value, os);
            IOUtils.write("\n", os, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
