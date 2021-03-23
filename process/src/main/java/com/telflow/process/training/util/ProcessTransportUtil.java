package com.telflow.process.training.util;

import org.activiti.engine.delegate.BpmnError;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.telflow.process.training.kafka.KafkaMessagePublisher;
import com.telflow.process.transports.TransportMessage;
import com.telflow.process.transports.registry.TransportRegistry;
import com.telflow.process.utils.ProcessConstants;
import com.telflow.process.utils.TelflowXMLProcessor;

import biz.dgit.schemas.telflow.cim.v3.Fault;
import biz.dgit.schemas.telflow.cim.v3.OperationRequest;
import biz.dgit.schemas.telflow.cim.v3.OperationResponse;
import biz.dgit.schemas.telflow.cim.v3.PartyRole;
import biz.dgit.schemas.telflow.cim.v3.PartyRolePartyRoleType;
import net.sf.json.JSONObject;

/**
 * @author ymadiana
 *
 */
public class ProcessTransportUtil {
    
    private static final transient Logger LOG = LoggerFactory.getLogger(ProcessTransportUtil.class);
    
    private static final String ENRICH_ELEMENTS = "enrichElements";
    
    private static final String ERROR_MESSAGE = "Failed to %s: %s";

    private ProcessTransportUtil() {
        
    }

    public static PartyRole generateProcessUser() {
        return new PartyRole().withType(PartyRolePartyRoleType.SYSTEM);
    }
    
    /**
     * Send Request to Telflow Fabric
     * @param <T> object
     * @param request Request as Telflow CIM object
     * @param fabricAction Request action
     * @param aClass Response class 
     * @param enrichElements Request enrich elements
     * @return Response CIM Object
     */
    public static <T> T sendRequestToTelflow(OperationRequest request, String fabricAction, Class<T> aClass, 
            String enrichElements) {
        enrichElements = StringUtils.isEmpty(ENRICH_ELEMENTS) ? "PartyRole" : enrichElements;
        String out = TelflowXMLProcessor.getInstance().marshal(request);
        TransportMessage msg = TransportMessage.newInstance().
            setDestination(ProcessConstants.FABRIC_DESTINATION).
            setAction(fabricAction).
            setHeader(ENRICH_ELEMENTS, enrichElements).
            setMessage(out);
        try {
            String response = TransportRegistry.request(msg);
            return TelflowXMLProcessor.getInstance().unmarshal(response, aClass);
        } catch (Exception x) {
            LOG.warn("Failed to " + fabricAction, x);
            throw new BpmnError(String.format(ERROR_MESSAGE, fabricAction, x.getMessage()));
        }
    }

    /**
     * Process Telflow response to check if it has exception or not.
     * When response is null has exception {@code BpmnError} is thrown
     * @param <T> object
     * @param response Telflow CIM object
     * @param fabricAction request action that gave this response
     */
    public static <T extends OperationResponse> void processTelflowResponse(T response, String fabricAction) {
        if (response == null) {
            throw new BpmnError(String.format("Failed to %s.", fabricAction));
        }
        Fault ex = response.getException();
        if (ex != null && !"warning".equalsIgnoreCase(ex.getSeverity())) {
            throw new BpmnError(String.format(ERROR_MESSAGE, fabricAction, ex.getPublicMessage()));
        }
    }

    /**
     * Send a message to Telflow System Bus
     * @param command request
     * @param queue Queue to send the request to
     * @param message Message to be sent
     * @param errorCode BPMN Error Code
     */
    public static void sendMessageToSystemBus(String queue, String command, JSONObject message, String errorCode) {
        try {
            KafkaMessagePublisher.getInstance().publish(queue, command, "", message);
        } catch (Exception x) {
            LOG.warn("Failed to send " + command, x);
            throw new BpmnError(errorCode, String.format("Failed to send %s: %s", command, x.getMessage()));
        }
    }

}
