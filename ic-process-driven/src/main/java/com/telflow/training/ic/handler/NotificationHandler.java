package com.telflow.training.ic.handler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inomial.secore.kafka.KafkaHeaderMapper;
import com.inomial.secore.kafka.MessageHandler;
import com.telflow.factory.common.RequestContext;
import com.telflow.factory.common.exception.StopProcessingException;
import com.telflow.training.ic.Constants;
import com.telflow.training.ic.helper.KafkaHelper;

/**
 * Class to handle received notifications
 *
 */
public class NotificationHandler implements MessageHandler {
    
    private static final transient Logger LOG = LoggerFactory.getLogger(NotificationHandler.class);
    
    private static final String RESPONSE_CODE = "responseCode";

    private static final String CORRELATION_HEADER = "correlation";
    
    private static ObjectMapper objectMapper;


    public NotificationHandler() {
        objectMapper = new ObjectMapper();
        KafkaHelper.init();
    }

    @Override
    public void handleMessage(ConsumerRecord<String, String> consumerRecord) {
        KafkaHeaderMapper mapper = new KafkaHeaderMapper(consumerRecord.headers());
        UUID correlation = mapper.getUuidHeader(Constants.CORRELATION);
        try (MDC.MDCCloseable tcidx = MDC.putCloseable(Constants.CORRELATION_ID, correlation.toString())) {
            LOG.info("Kafka Message Received - Starting processing");

            LOG.debug("Attempting to build request context from ConsumerRecord from kafka");
            RequestContext rc = new RequestContext();

            UUID correlationId = mapper.getUuidHeader("correlation");
            rc.setCorrelationId(correlationId.toString());

            String message = consumerRecord.value();
            JsonNode notificationJson = objectMapper.readTree(message);
            
            String telflowId = notificationJson.get("businessInteractionId").toString();
            String transactionId = notificationJson.get("correlationId").toString();
            LOG.info("Kafka Message Received - TELFLOW ID : {} ", telflowId);
            
            rc.setTelflowId(telflowId);
            
            rc.setOriginalMessage(message);
            rc.setCurrentMessage(message);

            try (MDC.MDCCloseable actionx =
                     MDC.putCloseable(Constants.CMD, rc.getContextProperties().get(Constants.CMD))) {
                processMessage(rc, transactionId);
            }
        } catch (StopProcessingException | IOException spex) {
            LOG.info("Stopping processing: {}", spex.getLocalizedMessage());
        }
    }
    
    
    private void processMessage(RequestContext request, String transactionId) {
        LOG.info("Processing message : {}", request.getCorrelationId());
        setResponsePayload(request, "200", transactionId);
        LOG.info("Sending response to Process:\n{}", request.getCurrentMessage());
        Map<String, String> headers = new HashMap<>();
        headers.put(CORRELATION_HEADER, request.getCorrelationId());
        headers.put(Constants.BUSINESS_INTERACTION_ID, request.getTelflowId());
        KafkaHelper.send(request.getCurrentMessage(), headers);
        return;
    }
    
    private void setResponsePayload(RequestContext requestContext, String status, String transactionId) {
        String response = "{\"responseCode\":\"" + status + "\", "
                + "\"businessInteractionId\":" + requestContext.getTelflowId() + ", "
                        + "\"transactionId\":" + transactionId + ", \"event\":{\"response\":\"SOMERESPONSE\"}}";
        
        requestContext.setCurrentMessage(response);
    }
    
    

}
