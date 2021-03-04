package com.telflow.training.ic;

/**
 * Consul Keys used by Integration Cartridge
 * 
 * @author Sandeep Vasani
 */
public class ConsulKeys {

    /**
     * the inbox topic
     */
    public static final String INBOX_TOPIC = "/inbox";

    /**
     * The protocol for fabric to use
     */
    public static final String FABRIC_PROTOCOL = "/fabricProtocol";

    /**
     * The endpoint for fabric. Used for fabricHelper to contact fabric for enrichments
     */
    public static final String FABRIC_ENDPOINT = "/fabric/esbEndpoint";

    /**
     * fabric user
     */
    public static final String FABRIC_USER = "/fabric/esbHttpUser";

    /**
     * fabric password
     */
    public static final String FABRIC_PASSWORD = "/fabric/secure/esbHttpPassword";

    /**
     * The endpoint for kafka. Used by anything that requires kafka access
     */
    public static final String KAFKA_ENDPOINT = "/kafka/kafkaEndpoint";
    
    /**
     * Path to file to save BI Notification
     */
    public static final String FILE_PATH = "/filePath";

    private ConsulKeys() {
        // do nothing
    }
}
