/*     / \____  _    _  ____   ______  / \ ____  __    _ _____
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  / /  _  \   Javaslang
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/  \__/  /   Copyright 2014-now Daniel Dietrich
 * /___/\_/  \_/\____/\_/  \_/\__\/__/___\_/  \_//  \__/_____/    Licensed under the Apache License, Version 2.0
 */
package com.thomsonreuters.innovation;

import javaslang.collection.Stream;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.Collections;

import static com.thomsonreuters.innovation.KafkaResources.kafkaConsumer;

/**
 * Very simple consumer of a topic 'mytopic'.  Creates an infinite stream over a poll operation and then outputs all
 * of the messages returned.
 */
public class SimpleConsumerExample {
    public static void main(String[] args) {
        System.out.println("Received the following messages");

        try (Consumer<String, String> consumer = kafkaConsumer()) {
            consumer.subscribe(Collections.singletonList("mytopic"));
            Stream
                .continually(() -> consumer.poll(100))
                .flatMap(Stream::ofAll)
                .map(ConsumerRecord::value)
                .forEach(record -> System.out.println("Received a message: " + record));
        }
    }
}
