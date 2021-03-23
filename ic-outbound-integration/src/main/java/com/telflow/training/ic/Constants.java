package com.telflow.training.ic;

/**
 * Constants used across IC. Kept here to centralise.
 */
public final class Constants {

    /**
     * Name of this IC. Used for consul manager setup.
     * Anything that uses consul manager app keys will need this
     */
    public static final String APP_NAME = "ic-outbound-integration";

    /**
     * The outbound header name for correlation id that process expects
     */
    public static final String CORRELATION_ID = "correlationId";

    /**
     * The outbound header name for business interaction id that process expects
     */
    public static final String BUSINESS_INTERACTION_ID = "businessInteractionId";

    /**
     * The header name of the command header on inbound kafka request
     */
    public static final String CMD = "command";

    /**
     * The header name of the correlationId header on inbound kafka request
     */
    public static final String CORRELATION = "correlation";

    private Constants() {

    }
}
