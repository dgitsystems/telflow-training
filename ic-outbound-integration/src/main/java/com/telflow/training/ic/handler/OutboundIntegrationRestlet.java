package com.telflow.training.ic.handler;

import java.io.IOException;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.telflow.factory.common.exception.ClientTransactionException;
import com.telflow.factory.common.exception.ServerTransactionException;

/**
 * Restlet for TMF 641 notification endpoint
 */
@Path("training/v1")
public class OutboundIntegrationRestlet {
    
    private static final transient Logger LOG = LoggerFactory.getLogger(OutboundIntegrationRestlet.class);

    private static final int INTERNAL_SERVER_ERROR = 500;
    
    /**
     * Endpoint that receives Task name update request
     * @param taskRequest the taskRequest

     * @return response
     */
    @POST
    @Path("/task")
    @Consumes("application/json")
    @Produces("application/xml")
    public String taskUpdate(String taskRequest) {
        try {
            LOG.info("Received Task name update request: {}",  taskRequest);
            return OutboundIntegrationHandler.handleTaskNameUpdate(taskRequest);
        } catch (ServerTransactionException | ClientTransactionException | IOException e) {
            LOG.error("Unable to process Task update Request : {}", e.getMessage());
            return e.getMessage();
        }
    }
    
    /**
     * Endpoint that receives order name update request
     * @param orderRequest the orderRequest

     * @return response
     */
    @POST
    @Path("/order")
    @Consumes("application/json")
    @Produces("application/xml")
    public String orderUpdate(String orderRequest) {
        try {
            LOG.info("Received Order update request: {}",  orderRequest);
            return OutboundIntegrationHandler.handleOrderNameUpdate(orderRequest);
        } catch (ServerTransactionException | ClientTransactionException | IOException e) {
            LOG.error("Unable to process Order update Request : {}", e.getMessage());
            return e.getMessage();
        }
    }

}
