package com.telflow.training.ic.handler;

import java.io.IOException;

import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.telflow.cim.converter.CimConverter;
import com.telflow.cim.converter.impl.CimConverterImpl;
import com.telflow.factory.common.FabricHelper;

import biz.dgit.schemas.telflow.cim.v3.BusinessInteraction;
import biz.dgit.schemas.telflow.cim.v3.ManageBusinessInteractionRequest;
import biz.dgit.schemas.telflow.cim.v3.ManageTaskRequest;
import biz.dgit.schemas.telflow.cim.v3.Party;
import biz.dgit.schemas.telflow.cim.v3.PartyRole;
import biz.dgit.schemas.telflow.cim.v3.PartyRolePartyRoleType;
import biz.dgit.schemas.telflow.cim.v3.PartyUser;
import biz.dgit.schemas.telflow.cim.v3.PartyUsers;
import biz.dgit.schemas.telflow.cim.v3.Task;

/**
 * @author macbookpro2
 *
 */
public class OutboundIntegrationHandler {
    
    private static final transient Logger LOG = LoggerFactory.getLogger(OutboundIntegrationRestlet.class);
    
    private static FabricHelper fabricHelper;
    
    private static ObjectMapper mapper;
    
    private static CimConverter CIM_CONVERTER;
    
    static {
        try {
            CIM_CONVERTER = new CimConverterImpl(); 
        } catch (JAXBException e) {
            LOG.error("Failed to initialised CimConverter: ", e);
        }
    }

    private OutboundIntegrationHandler() {
        
    }
    
    /**
     * Init the Outbound integration Handler
     * @param fh A fabric helper
     */
    public static void init(FabricHelper fh) {
        fabricHelper = fh;
        mapper = new ObjectMapper();
    }
    
    public static String handleTaskNameUpdate(String taskRequest) throws IOException {
        JsonNode taskJson = mapper.readTree(taskRequest);
        
        String taskId = taskJson.get("id").asText();
        String taskName = taskJson.get("name").asText();
        
        ManageTaskRequest req = new ManageTaskRequest();
        req.withTask(new Task().withID(taskId).withName(taskName))
            .withUser(new PartyRole().withID("PTR000000000042").withType(PartyRolePartyRoleType.PROVIDER_ADMINISTRATOR)
                    .withParty(new Party().withUsers(new PartyUsers()
                            .withPartyUser(new PartyUser().withUserID("ymadiana")))));
        
        String response = fabricHelper.post(CIM_CONVERTER.marshal(req), "UpdateTask", MediaType.APPLICATION_XML);
        
        return response;
    }
    
    public static String handleOrderNameUpdate(String orderRequest) throws IOException {
        JsonNode orderJson = mapper.readTree(orderRequest);
        
        String orderId = orderJson.get("id").asText();
        String orderName = orderJson.get("name").asText();
        
        ManageBusinessInteractionRequest req = new ManageBusinessInteractionRequest()
                .withBusinessInteraction(new BusinessInteraction().withID(orderId)
                        .withName(orderName))
                .withUser(new PartyRole().withType(PartyRolePartyRoleType.SYSTEM));
        
        String response = fabricHelper.post(CIM_CONVERTER.marshal(req), "UpdateBusinessInteraction", MediaType.APPLICATION_XML);
        
        return response;
    }

}
