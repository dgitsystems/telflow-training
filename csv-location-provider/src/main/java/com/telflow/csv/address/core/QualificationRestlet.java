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
 * Restlet for Qualification Provider endpoint. Qualify will hit this restlet
 */
@Path("api/v1")
public class QualificationRestlet {

    private static final transient Logger LOG = LoggerFactory.getLogger(QualificationRestlet.class);

    /**
     * Endpoint that receives availability search requests
     * @param availabilityRequest the availabilityRequest

     * @return response
     */
    @POST
    @Path("/availability")
    @Consumes("application/xml")
    @Produces("application/xml")
    public String availabilityRequest(String availabilityRequest) {
        try {
            LOG.info("Received Service Qualification request: {}",  availabilityRequest);
            return QualificationHandler.handleQualificationRequest(availabilityRequest);
        } catch (ServerTransactionException | ClientTransactionException e) {
            LOG.error("Unable to process Service Qualification Request : {}", e.getMessage());
            return e.getMessage();
        }
    }
}
