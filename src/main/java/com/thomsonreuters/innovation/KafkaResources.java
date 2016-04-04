/*     / \____  _    _  ____   ______  / \ ____  __    _ _____
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  / /  _  \   Javaslang
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/  \__/  /   Copyright 2014-now Daniel Dietrich
 * /___/\_/  \_/\____/\_/  \_/\__\/__/___\_/  \_//  \__/_____/    Licensed under the Apache License, Version 2.0
 */
package com.thomsonreuters.innovation;

import javaslang.collection.Stream;
import javaslang.concurrent.Future;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Properties;
import java.util.concurrent.ExecutorService;

import static javaslang.concurrent.Future.fromJavaFuture;

class KafkaResources {

    private KafkaResources() {
        throw new AssertionError();
    }

    /**
     * Creates a basic kafka producer
     * @return the producer
     */
    static Producer<String, String> kafkaProducer() {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        return new KafkaProducer<>(props);
    }

    /**
     *
     * @param groupId
     * @return
     */
    static Consumer<String, String> kafkaConsumer(String groupId) {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("group.id", groupId);
        props.put("auto.offset.reset", "earliest");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        return new KafkaConsumer<>(props);
    }

    /**
     * Map a stream of producer records to a stream of futures of sending the records.
     *
     * @param service service to schedule threads in
     * @param producer kafka record sender
     * @param records the stream of records to send
     * @return the mapped stream
     */
    static <K, V> Stream<Future<RecordMetadata>> send(ExecutorService service, Producer<K, V> producer,
                                                      Stream<ProducerRecord<K, V>> records) {
        return records.map(record -> fromJavaFuture(service, producer.send(record)));
    }

    /**
     * Create an infinite stream over a consumer poll.
     *
     * @param consumer the consumer to use for polling
     * @return the stream of consumer records derived from the poll.
     */
    static <K, V> Stream<ConsumerRecord<K, V>> poll(Consumer<K, V> consumer, int timeoutMillis) {
        return Stream
            .continually(() -> consumer.poll(timeoutMillis))
            .flatMap(Stream::ofAll);
    }
}
