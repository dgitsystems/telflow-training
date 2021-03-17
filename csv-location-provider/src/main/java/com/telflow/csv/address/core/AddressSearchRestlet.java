package com.telflow.csv.address.core;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.telflow.factory.common.exception.ClientTransactionException;
import com.telflow.factory.common.exception.ServerTransactionException;

/**
 * Restlet for Address Provider endpoint. Qualify will hit this restlet
 */
@Path("api/v1")
public class AddressSearchRestlet {

    private static final transient Logger LOG = LoggerFactory.getLogger(AddressSearchRestlet.class);

    /**
     * Endpoint that receives address search requests
     * @param addressSearchRequest the addressSearchRequest

     * @return response
     */
    @POST
    @Path("/address")
    @Consumes("application/xml")
    @Produces("application/xml")
    public String addressSearchRequest(String addressSearchRequest) {
        try {
            LOG.info("Received address search request: {}", addressSearchRequest);
            return AddressSearchHandler.handleAddressSearchRequest(addressSearchRequest);
        } catch (ServerTransactionException | ClientTransactionException e) {
            LOG.error("Unable to process address search Request : {}", e.getMessage());
            return e.getMessage();
        }
    }
}
