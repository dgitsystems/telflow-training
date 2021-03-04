package com.telflow.training.ic;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.telflow.cim.converter.CimConverter;

import biz.dgit.schemas.telflow.cim.v3.BusinessInteractionNotification;

/**
 * Class to process BI Notification
 *
 */
public class NotificationProcessor {
    
    private static final transient Logger LOG = LoggerFactory.getLogger(NotificationProcessor.class);
    
    private CimConverter cimConverter;
    
    private String filePath;

    public NotificationProcessor(CimConverter cimConverter, String filePath) {
        this.cimConverter = cimConverter;
        this.filePath = filePath;
    }
    
    /**
     * Process Business Interaction Notification and write it into file
     * @param notification BI Notification
     * @throws IOException IOException
     */
    public void process(BusinessInteractionNotification notification) throws IOException {
        String biNotification = this.cimConverter.marshal(notification);
        
        try {
            File file = new File(this.filePath);
            file.createNewFile();
            
            BufferedWriter writer = new BufferedWriter(new FileWriter(file, false));
            writer.write(biNotification);
            
            writer.close();
        } catch (IOException e) {
            LOG.error("Unable to Write into log file : {}", e.getMessage());
        }
        
    }

}
