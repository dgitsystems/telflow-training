package com.telflow.process.training.listeners;

import org.activiti.engine.delegate.DelegateExecution;

import com.telflow.process.listeners.TelflowProcessListener;
import com.telflow.process.training.util.ProcessTransportUtil;

import net.sf.json.JSONObject;

/**
 * @author macbookpro2
 *
 */
public class SendExternalMessage extends TelflowProcessListener {
    
    @Override
    public void doExecute(DelegateExecution execution) throws Exception {
        String kafkaTopic = (String) execution.getVariable("kafkaTopic");
        String message = (String) execution.getVariable("message");
        String correlationId = (String) execution.getVariable("correlationId");
        String businessInteractionId = (String) execution.getVariable("businessInteractionId");
        
        LOG.info("SENDING MESSAGE TO KAFKA : ...");
     // Send message to System bus
        ProcessTransportUtil.sendMessageToSystemBus(kafkaTopic, 
                "sendMessage", buildMessage(correlationId, message, businessInteractionId), "processError");
        
        LOG.info("MESSAGE SENT : ...");
    }
    
    private JSONObject buildMessage(String correlationId, String message, String businessInteractionId) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("businessInteractionId", businessInteractionId);
        jsonObject.put("correlationId", correlationId);
        jsonObject.put("message", message);
        
        return jsonObject;
    }

}
