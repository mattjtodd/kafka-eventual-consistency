/*     / \____  _    _  ____   ______  / \ ____  __    _ _____
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  / /  _  \   Javaslang
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/  \__/  /   Copyright 2014-now Daniel Dietrich
 * /___/\_/  \_/\____/\_/  \_/\__\/__/___\_/  \_//  \__/_____/    Licensed under the Apache License, Version 2.0
 */
package com.thomsonreuters.innovation;

import com.fasterxml.jackson.databind.ObjectMapper;
import javaslang.control.Try;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

import static com.thomsonreuters.innovation.KafkaResources.kafkaConsumer;
import static com.thomsonreuters.innovation.KafkaResources.poll;

/**
 * Very simple consumer of a topic 'mytopic'.  Creates an infinite stream over a poll operation and then outputs all
 * of the messages returned.
 */
public class SimpleConsumerExample {
    public static void main(String[] args) {

        RestOperations operations = new RestTemplate();
        ObjectMapper objectMapper = new ObjectMapper();

        try (Consumer<String, String> consumer = kafkaConsumer("elastic-search-1")) {
            consumer.subscribe(Collections.singletonList("NewTest"));
            poll(consumer, 100)
                .forEach(record -> index(record, operations, objectMapper));
        }
    }

    private static void index(ConsumerRecord<String, String> record, RestOperations restOperations, ObjectMapper objectMapper) {
        System.out.println("Handling..................");
        Try.of(() -> objectMapper.readValue(record.value(), JpaCase.class))
            .onSuccess(jpaCase -> restOperations.postForObject("http://localhost:8080/elastic-cases", jpaCase, JpaCase.class));

    }
}
