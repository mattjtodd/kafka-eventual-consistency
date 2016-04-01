/*     / \____  _    _  ____   ______  / \ ____  __    _ _____
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  / /  _  \   Javaslang
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/  \__/  /   Copyright 2014-now Daniel Dietrich
 * /___/\_/  \_/\____/\_/  \_/\__\/__/___\_/  \_//  \__/_____/    Licensed under the Apache License, Version 2.0
 */
package com.thomsonreuters.innovation;

import javaslang.Tuple2;
import javaslang.collection.HashMap;
import javaslang.collection.Map;
import javaslang.collection.Stream;
import javaslang.control.Try;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.IntegerDeserializer;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.*;
import java.util.stream.IntStream;

public class App {

    private static final Map<String, Object> PRODUCER_PROPERTIES = HashMap.of(
        "bootstrap.servers", "localhost:9092",
        "acks", "all",
        "retries", 0,
        "batch.size", 16384,
        "linger.ms", 1,
        "buffer.memory", 33554432,
        "key.serializer", "org.apache.kafka.common.serialization.StringSerializer",
        "value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

    private static final Map<String, Object> CONSUMER_PROPERTIES = HashMap.of(
        "bootstrap.servers", "localhost:9092",
        "enable.auto.commit", "true",
        "group.id", "default",
        "acks", "all",
        "retries", 0,
        "batch.size", 16384,
        "linger.ms", 1,
        "buffer.memory", 33554432,
        "key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer",
        "value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

    public static void main(String[] args) {

//        StringReader stringReader = new StringReader("line1\nline2\nline3\nline4");
//        BufferedReader reader = new BufferedReader(stringReader);
//
//        Stream
//            .ofAll(reader.lines()::iterator)
//            .forEach(System.out::println);

        CompletableFuture.runAsync(() -> {
            IntStream.range(1, 100)
                     .peek(value -> Try.run(() -> Thread.sleep(5000)))
                     .forEach(value -> produce());

        });
//        produce();
        consume();
    }

    private static void produce() {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        KafkaProducer<String, String> producer = new KafkaProducer<>(props);
        for (int i = 0; i < 100; i++) {
            ProducerRecord<String, String> record = new ProducerRecord<>("mytopic", "value-" + i);
            producer.send(record);
        }

        producer.close();    }

    private static void consume() {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("group.id", "mygroup");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Arrays.asList("mytopic"));

        boolean running = true;
        while (running) {
            ConsumerRecords<String, String> records = consumer.poll(100);
            System.out.println("Poll....");
            for (ConsumerRecord<String, String> record : records) {
                System.out.println(record.value());
            }
        }

        consumer.close();    }

    private static Future<RecordMetadata> send(Producer<Void, String> producer, String message) {
        return producer.send(new ProducerRecord<>("names", message));
    }

    private static Producer<Void, String> kafkaProducer() {
        return new KafkaProducer<>(PRODUCER_PROPERTIES.toJavaMap());
    }

    private static Consumer<Void, String> kafkaConsumer() {
        return new KafkaConsumer<>(CONSUMER_PROPERTIES.toJavaMap());
    }
}
