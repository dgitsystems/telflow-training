package com.telflow.csv.address.provider;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.telflow.csv.address.transform.Transformer;

import biz.dgit.schemas.telflow.cim.v3.NewZealandAddress;

/**
 * Class to schedule address importer job
 *
 */
public class Importer {
    
    private static final transient Logger LOG = LoggerFactory.getLogger(Importer.class);
    
    private static final String FILE_LOCATION = "/opt/telflow/csv-address-provider/resources/input.csv";
    
    private Transformer transformer;
    
    /**
     * @param fileReader fileReader
     * @param transformer transformer
     */
    public Importer(Transformer transformer) {
        this.transformer = transformer;
    }

    public List<NewZealandAddress> run() {
        try {
            List<String> data = readCsvFile();
            List<NewZealandAddress> telflowAddresses = transformer.buildTelflowAddressJsonFormat(data);
            
            return telflowAddresses;
        } catch (ParseException | IOException e) {
            LOG.error("Unable to process Address Import : {}", e.getMessage());
            return Collections.EMPTY_LIST;
        }
    }
    
    /**
     * @return list of address String separated by comma
     * @throws IOException IOException
     * @throws FileNotFoundException FileNotFoundException
     */
    public List<String> readCsvFile() throws FileNotFoundException, IOException {
        List<String> values = new ArrayList<String>();
        
        LOG.info("FILE LOCATION : {}", FILE_LOCATION);

        values = IOUtils.readLines(new FileInputStream(
                FILE_LOCATION), "UTF-8");
        values.remove(0);
        Set<String> valueSet = new HashSet<String>(values);
        values.clear();
        values.addAll(valueSet);
        
        LOG.info("Found {} address entries in the file", values.size());

        return values;
    }

}
