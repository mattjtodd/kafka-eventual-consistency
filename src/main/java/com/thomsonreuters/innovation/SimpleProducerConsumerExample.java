/*     / \____  _    _  ____   ______  / \ ____  __    _ _____
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  / /  _  \   Javaslang
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/  \__/  /   Copyright 2014-now Daniel Dietrich
 * /___/\_/  \_/\____/\_/  \_/\__\/__/___\_/  \_//  \__/_____/    Licensed under the Apache License, Version 2.0
 */
package com.thomsonreuters.innovation;

import javaslang.collection.Stream;
import javaslang.control.Try;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Collections;
import java.util.Properties;
import java.util.UUID;

public class SimpleProducerConsumerExample {

    public static void main(String[] args) {

        try (Producer<String, String> producer = kafkaProducer()) {
            int count = Stream.range(0, 10)
                .map(value -> UUID.randomUUID().toString() + " count " + value)
                .map(value -> new ProducerRecord<String, String>("mytopic", value))
                .map(producer::send)
                .map(future -> Try.of(future::get))
                .count(Try::isSuccess);

            System.out.println("Sent the following number of messages: " + count);
        }

        System.out.println("Received the following messages");

        try (Consumer<String, String> consumer = kafkaConsumer()) {
            consumer.subscribe(Collections.singletonList("mytopic"));
            Stream
                .continually(() -> consumer.poll(100))
                .flatMap(Stream::ofAll)
                .map(ConsumerRecord::value)
                .take(10)
                .forEach(System.out::println);

        }
    }

    private static Producer<String, String> kafkaProducer() {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        return new KafkaProducer<>(props);
    }

    private static Consumer<String, String> kafkaConsumer() {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("group.id", "mygroup");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        return new KafkaConsumer<>(props);
    }
}
