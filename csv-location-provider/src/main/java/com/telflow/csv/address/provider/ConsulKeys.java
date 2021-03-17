package com.telflow.csv.address.provider;

/**
 *  Consul keys for Address Provider
 *
 */
public final class ConsulKeys {
    
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
     * File Location
     */
    public static final String FILE_LOCATION = "/address/fileImportLocation";
    

    private ConsulKeys() {
        
    }
    
}
