package com.telflow.csv.address.provider;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.inomial.secore.http.HttpServer;
import com.telflow.cim.converter.CimConverter;
import com.telflow.cim.converter.impl.CimConverterImpl;
import com.telflow.csv.address.core.AddressSearchHandler;
import com.telflow.csv.address.core.AddressSearchRestlet;
import com.telflow.csv.address.core.QualificationClient;
import com.telflow.csv.address.core.QualificationHandler;
import com.telflow.csv.address.core.QualificationRestlet;
import com.telflow.csv.address.transform.Transformer;
import com.telflow.factory.common.exception.InitialisationException;
import com.telflow.factory.configuration.management.ConsulManager;

import biz.dgit.schemas.telflow.cim.v3.NewZealandAddress;

/**
 * Main class for CSV Address Provider
 *
 */
public class Main {

private static final transient Logger LOG = LoggerFactory.getLogger(Main.class);
    
    private static CimConverter converter;
    
    private static Transformer transformer;
    
    private Main() {
        // Do nothing.
    }

    public static void main(String[] args) throws InterruptedException {
        try {
            initConsul();
            converter = new CimConverterImpl();
            
            transformer = new Transformer();
            
            Importer importer = new Importer(transformer);
            
            List<NewZealandAddress> addresses = importer.run();
            
            initHttpServer(addresses);
            
            Thread.sleep(86400000);
        } catch (InitialisationException | JAXBException ie) {
            LOG.error("Failed to initialise application, exiting", ie);
        }
    }
    
    private static void initConsul() {
        try {
            String consulEndpoint = System.getenv("CONSUL_SERVER");
            LOG.info("Consul Endpoint: {}", consulEndpoint);
            URL url = new URL(consulEndpoint);
            Map<String, String> defaults = getDefaults();
            ConsulManager.init(url, "csv-provider", defaults);
        } catch (MalformedURLException mue) {
            LOG.error("Failed to initialise Consul", mue);
            throw new InitialisationException("Failed To initialise Consul", mue);
        }
    }

    private static void initHttpServer(List<NewZealandAddress> addresses) {
        try {
            AddressSearchHandler.init(addresses, converter);
            QualificationClient qualClient = new QualificationClient();
            
            QualificationHandler.init(qualClient, converter);
            LOG.info("Starting http server on port 8989");
            HttpServer.addResourceClass(AddressSearchRestlet.class);
            HttpServer.addResourceClass(QualificationRestlet.class);
            HttpServer.start(Collections.EMPTY_SET, Collections.EMPTY_SET);
        } catch (Exception e) {
            throw new InitialisationException(e.getMessage());
        }
    }
    
    private static Map<String, String> getDefaults() {
        ConsulManager.setAppName("training-provider");
        Map<String, String> defaultValues = new HashMap<>();
        setFabricDefault(defaultValues);

        return defaultValues;
    }

    private static void setFabricDefault(Map<String, String> defaultValues) {
        defaultValues.put(ConsulManager.buildAppKey(ConsulKeys.FABRIC_PROTOCOL), "http");
        defaultValues.put(ConsulManager.buildEnvKey(ConsulKeys.FABRIC_ENDPOINT), "esb:9797");
        defaultValues.put(ConsulManager.buildEnvKey(ConsulKeys.FABRIC_USER), "amq_user");
        defaultValues.put(ConsulManager.buildEnvKey(ConsulKeys.FABRIC_PASSWORD), "");
        defaultValues.put(ConsulManager.buildAppKey(ConsulKeys.FILE_LOCATION), "/opt/telflow/csv-address-provider/imports/input.csv");
    }

}
