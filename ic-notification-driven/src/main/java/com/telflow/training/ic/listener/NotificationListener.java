package com.telflow.training.ic.listener;

import java.io.IOException;

import javax.xml.bind.JAXBException;

import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.inomial.secore.kafka.MessageHandler;
import com.telflow.cim.camel.CimTypeConverterException;
import com.telflow.cim.converter.CimConverter;
import com.telflow.cim.converter.impl.CimConverterImpl;
import com.telflow.training.ic.NotificationProcessor;

import biz.dgit.schemas.telflow.cim.v3.BusinessInteractionNotification;

/**
 * Listen for Kafka message and send them to the processor
 * @author Sandeep Vasani
 */
public class NotificationListener implements MessageHandler {

    private static final transient Logger LOG = LoggerFactory.getLogger(NotificationListener.class);

    private static CimConverter CIM_CONVERTER;

    private NotificationProcessor processor;
    
    static {
        try {
            CIM_CONVERTER = new CimConverterImpl(); 
        } catch (JAXBException e) {
            LOG.error("Failed to initialised CimConverter: ", e);
        }
    }

    public NotificationListener(String filePath) {
        this.processor = new NotificationProcessor(CIM_CONVERTER, filePath);
    }

    @Override
    public void handleMessage(ConsumerRecord<String, String> record) {
        String message = record.value();

        if (StringUtils.isEmpty(message)) {
            return;
        }

        try {
            BusinessInteractionNotification notification = CIM_CONVERTER.unmarshal(message,
                    BusinessInteractionNotification.class);
            LOG.info("Received kafka notification: headers: {} body: {}", record.headers(), message);
            this.processor.process(notification);
        } catch (CimTypeConverterException e) {
            LOG.trace("Ignoring {} notifiation as not BusinessInteractionNotification.", message);
            return;
        } catch (IOException e) {
            LOG.error("Failed to generate quote: ", e);
        }
    }


}
