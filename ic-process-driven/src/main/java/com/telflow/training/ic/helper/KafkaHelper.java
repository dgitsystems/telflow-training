package com.telflow.training.ic.helper;

import com.inomial.secore.kafka.KafkaMessage;
import com.inomial.secore.kafka.MessageProducer;
import com.telflow.factory.configuration.management.ConsulManager;
import com.telflow.training.ic.ConsulKeys;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * for common methods using kafka
 */
public final class KafkaHelper {

    private static final transient Logger LOG = LoggerFactory.getLogger(KafkaHelper.class);

    private static final String CORRELATION_ID = "correlationId";

    private static MessageProducer producer;

    private KafkaHelper() {

    }

    /**
     * Initialise the kafka helper
     */
    public static void init() {
        resetProducer();
        ConsulManager.addRegisteredObject("kafkaHelperMessageProducer", KafkaHelper::resetProducer);
    }

    private static void resetProducer() {
        if (producer != null) {
            producer.close();
        }
        Map<String, Object> cp = new HashMap<>();
        cp.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, ConsulManager.getEnvKey(ConsulKeys.KAFKA_ENDPOINT));
        producer = new MessageProducer(cp);
    }

    /**
     * Sends a message to the configured kafka outbox topic
     * @param messageBody the messageBody
     * @param headers the additional headers to add
     */
    public static void send(String messageBody, Map<String, String> headers) {
        LOG.trace("Sending outbound message.  Headers: {}, Body: {}", headers, messageBody);
        producer.send(new KafkaMessage(ConsulManager.getAppKey(ConsulKeys.RESULT_TOPIC),
            headers.get(CORRELATION_ID), messageBody) {
            @Override
            public ProducerRecord<Object, String> produce() {
                // KafkaMessage predefined headers are prepended with "inomial", so … do it here for now
                ProducerRecord<Object, String> pr = super.produce();
                headers.forEach((key, value) -> {
                    LOG.info("KEY : {}", key);
                    LOG.info("VAL : {}", value);
                    Header correlation = new RecordHeader(key, value.getBytes());
                    pr.headers().add(correlation);
                });
                return pr;
            }
        });
    }
}
