package com.telflow.training.ic;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.inomial.secore.kafka.MessageConsumer;
import com.inomial.secore.scope.Scope;
import com.telflow.factory.common.exception.InitialisationException;
import com.telflow.factory.configuration.management.ConsulManager;
import com.telflow.training.ic.handler.NotificationHandler;

/**
 * Main class for Process Driven IC
 *
 */
public class Main {
    
    private static final transient Logger LOG = LoggerFactory.getLogger(Main.class);
    
    private static final String APP_NAME = "ic-process-driven";
    
    private static MessageConsumer MESSAGE_CONSUMER;

    /**
     * @param args
     */
    public static void main(String[] args) {
        LOG.info("Starting Up IC");
        try {
            initConsul();
        } catch (InitialisationException ie) {
            LOG.error("Failed to initialise application, exiting.", ie);
            return;
        }
        LOG.info("Notification Driven IC is now initalised");
        setupIntegrationCartridge();
    }
    
    private static void initConsul() {
        try {
            String consulEndpoint = System.getenv("CONSUL_SERVER");
            LOG.info("Consul Endpoint: {}", consulEndpoint);
            URL url = new URL(consulEndpoint);
            Map<String, String> defaults = getDefaults();
            ConsulManager.init(url, APP_NAME, defaults);
            ConsulManager.addRegisteredObject("integrationCartridge", Main::setupIntegrationCartridge);
        } catch (MalformedURLException mue) {
            LOG.error("Failed to initialise Consul: ", mue);
            throw new InitialisationException("Failed to initialise Consul", mue);
        }
    }
    
    private static void setupIntegrationCartridge() {
        // Kafka configuration
        Map<String, Object> cp = new HashMap<>();
        cp.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
            ConsulManager.getEnvKey(ConsulKeys.KAFKA_ENDPOINT));
        cp.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");

        if (MESSAGE_CONSUMER != null) {
            MESSAGE_CONSUMER.shutdown();
        }
        MESSAGE_CONSUMER = new MessageConsumer(cp);

        NotificationHandler listener = new NotificationHandler();
        MESSAGE_CONSUMER.addMessageHandler(ConsulManager.getAppKey(
            ConsulKeys.INBOX_TOPIC), listener, Scope.NONE);
        MESSAGE_CONSUMER.start(APP_NAME);

        LOG.trace("Initialised a consumer for IC: ID: {}, Name: {}",
            MESSAGE_CONSUMER.getId(), MESSAGE_CONSUMER.getName());
    }
    
    private static Map<String, String> getDefaults() {
        ConsulManager.setAppName(Constants.APP_NAME);
        Map<String, String> defaultValues = new HashMap<>();
        defaultValues.put(ConsulManager.buildAppKey(ConsulKeys.INBOX_TOPIC), "telflow.process.inbox.training");
        defaultValues.put(ConsulManager.buildAppKey(ConsulKeys.RESULT_TOPIC), "telflow.inbox.process.asyncmessage");
        return defaultValues;
    }

}
