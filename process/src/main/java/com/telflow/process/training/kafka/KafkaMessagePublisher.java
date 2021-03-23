package com.telflow.process.training.kafka;

import com.inomial.secore.kafka.KafkaHeaderMapper;
import com.inomial.secore.kafka.KafkaMessage;
import com.inomial.secore.kafka.MessageProducer;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * A helper class to be able to send a notification to an arbitrary topic.
 * Singleton, will cache a producer for each topic.
 */
public class KafkaMessagePublisher {

    private static KafkaMessagePublisher INSTANCE;

    private static final transient Logger LOG = LoggerFactory.getLogger(KafkaMessagePublisher.class);
    
    private String kafkaEndpoint;

    private Map<String, MessageProducer> producerMap;

    // Singleton Stuff
    private KafkaMessagePublisher(String endpoint) {
        this.producerMap = new HashMap<>();
        this.kafkaEndpoint = endpoint;
    }

    /**
     * Create a new KarafMessagePublisher instance with a set ID
     * @param endpoint The Kafka endpoint.
     * @return The newly created instance.
     */
    public synchronized static KafkaMessagePublisher setup(String endpoint) {
        if (INSTANCE != null) {
            throw new RuntimeException("KafkaMessagePublisher already set");
        }

        INSTANCE = new KafkaMessagePublisher(endpoint);
        return INSTANCE;
    }

    /**
     * Safely close the consumers
     */
    public synchronized static void teardown() {
        if (INSTANCE == null) {
            throw new RuntimeException("KafkaMessagePublisher not set, cannot teardown");
        }

        INSTANCE.teardownConsumers();
    }

    /**
     * Retrieve the instance of the kafka message publisher
     * @return The instance of the KafkaMessagePublisher
     */
    public static KafkaMessagePublisher getInstance() {
        if (INSTANCE == null) {
            throw new RuntimeException("KafkaMessagePublisher not set");
        }

        return INSTANCE;
    }

    // End Singleton stuff

    /**
     * Publish a new request to a given topic.
     * @param topic The topic to publish to.
     * @param command The command header.
     * @param replyTo The reply-to header
     * @param message The message to publish.
     */
    public void publish(String topic, String command, String replyTo, JSONObject message) {
        LOG.trace("Publishing a message to {} with the command {}", topic, command);

        MessageProducer producer = null;

        synchronized (this.producerMap) {
            if (!this.producerMap.containsKey(topic)) {
                this.producerMap.put(topic, new MessageProducer(createProducerProperties()));
            }

            producer = this.producerMap.get(topic);
        }
        LOG.trace("Producer selected");
        producer.send(new KafkaMessage(topic, null, message.toString()) {

            @Override
            public ProducerRecord<Object, String> produce() {
                LOG.trace("Building headers for message");
                // KafkaMessage predefined headers are prepended with "inomial", so … do it here for now
                ProducerRecord<Object, String> pr = super.produce();
                KafkaHeaderMapper mapper = new KafkaHeaderMapper(pr.headers());

                mapper.setUuidHeader("correlation", UUID.randomUUID()); // Currently randomise
                mapper.setUuidHeader("uuid", UUID.randomUUID());
                mapper.setStringHeader("messageHeader", "0");
                mapper.setStringHeader("command", command);
                if (StringUtils.isNotBlank(replyTo)) {
                    mapper.setStringHeader("reply-to", replyTo);
                }

                return pr;
            }

        });
        LOG.trace("Message Sent.");
    }

    private void teardownConsumers() {
        for (MessageProducer currentProducer : this.producerMap.values()) {
            currentProducer.close();
        }
    }

    /**
     * @return configuration for the kafka producer
     */
    private Map<String, Object> createProducerProperties() {
        Map<String, Object> pp = new HashMap<>();
        pp.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, this.kafkaEndpoint);
        pp.put(ProducerConfig.CLIENT_ID_CONFIG, "telflow-process-training");
        pp.put(ProducerConfig.LINGER_MS_CONFIG, 1);
        pp.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
        return pp;
    }
}