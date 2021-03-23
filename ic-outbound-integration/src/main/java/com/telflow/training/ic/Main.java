package com.telflow.training.ic;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Sets;
import com.inomial.secore.http.HttpServer;
import com.telflow.factory.common.FabricHelper;
import com.telflow.factory.common.exception.InitialisationException;
import com.telflow.factory.configuration.management.ConsulManager;
import com.telflow.training.ic.handler.OutboundIntegrationHandler;
import com.telflow.training.ic.handler.OutboundIntegrationRestlet;

/**
 * Main class for Outbound Integration IC
 *
 */
public class Main {
    
    private static final transient Logger LOG = LoggerFactory.getLogger(Main.class);
    
    private static final String APP_NAME = "ic-outbound-integration";
    
    private static FabricHelper fabricHelper;

    /**
     * @param args
     */
    public static void main(String[] args) {
        LOG.info("Starting Up IC");
        try {
            initConsul();
            initFabricHelper();
            initOutboundIntegrationHandler();
        } catch (InitialisationException ie) {
            LOG.error("Failed to initialise application, exiting.", ie);
            return;
        }
        LOG.info("Outbound IC is now initalised");
    }
    
    private static void initConsul() {
        try {
            String consulEndpoint = System.getenv("CONSUL_SERVER");
            LOG.info("Consul Endpoint: {}", consulEndpoint);
            URL url = new URL(consulEndpoint);
            Map<String, String> defaults = getDefaults();
            ConsulManager.init(url, APP_NAME, defaults);
        } catch (MalformedURLException mue) {
            LOG.error("Failed to initialise Consul: ", mue);
            throw new InitialisationException("Failed to initialise Consul", mue);
        }
    }
    
    private static void initFabricHelper() {
        fabricHelper = new FabricHelper(
                String.format("%s://%s/api/",
                        ConsulManager.getAppKey(ConsulKeys.FABRIC_PROTOCOL),
                        ConsulManager.getEnvKey(ConsulKeys.FABRIC_ENDPOINT)),
                ConsulManager.getEnvKey(ConsulKeys.FABRIC_USER),
                ConsulManager.getEnvKey(ConsulKeys.FABRIC_PASSWORD)
        );
    }
    
    private static void initOutboundIntegrationHandler() {
        try {
            System.out.println("Starting http server on port 8080");
            OutboundIntegrationHandler.init(fabricHelper);
            HttpServer.addResourceClass(OutboundIntegrationRestlet.class);
            HttpServer.start(Sets.newHashSet(), Sets.newHashSet());
        } catch (Exception e) {
            throw new InitialisationException(e.getMessage());
        }
    }
    
    private static Map<String, String> getDefaults() {
        ConsulManager.setAppName(Constants.APP_NAME);
        Map<String, String> defaultValues = new HashMap<>();
        defaultValues.put(ConsulManager.buildAppKey(ConsulKeys.FABRIC_PROTOCOL), "http");
        defaultValues.put(ConsulManager.buildEnvKey(ConsulKeys.FABRIC_ENDPOINT), "esb:9797");
        defaultValues.put(ConsulManager.buildEnvKey(ConsulKeys.FABRIC_USER), "amq_user");
        defaultValues.put(ConsulManager.buildEnvKey(ConsulKeys.FABRIC_PASSWORD), "");
        return defaultValues;
    }

}
