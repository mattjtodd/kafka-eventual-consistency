/*     / \____  _    _  ____   ______  / \ ____  __    _ _____
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  / /  _  \   Javaslang
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/  \__/  /   Copyright 2014-now Daniel Dietrich
 * /___/\_/  \_/\____/\_/  \_/\__\/__/___\_/  \_//  \__/_____/    Licensed under the Apache License, Version 2.0
 */
package com.thomsonreuters.innovation;

import com.fasterxml.jackson.databind.ObjectMapper;
import javaslang.control.Try;
import org.apache.commons.cli.*;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Collections;
import java.util.Properties;

import static com.thomsonreuters.innovation.KafkaResources.kafkaConsumer;
import static com.thomsonreuters.innovation.KafkaResources.poll;

/**
 * Very simple consumer of a topic 'mytopic'.  Creates an infinite stream over a poll operation and then outputs all
 * of the messages returned.
 */
public class CaseConsumer {

    private static final String PROPERTIES = "properties";

    public static void main(String[] args) throws ParseException, IOException, ClassNotFoundException {

        Options options = new Options();
        options.addOption(Option.builder(PROPERTIES).required().hasArg().type(String.class).build());

        CommandLine commandLine = new DefaultParser().parse(options, args);

        String propertiesPath = commandLine.getOptionValue(PROPERTIES);
        Properties properties = new Properties();
        properties.load(CaseConsumer.class.getClassLoader().getResourceAsStream(propertiesPath));

        RestOperations operations = new RestTemplate();
        ObjectMapper objectMapper = new ObjectMapper();

        String topic = (String) properties.get("topic");
        String groupId = (String) properties.get("groupId");
        String sink = (String) properties.get("sink");
        String klassName = (String) properties.get("payloadClass");
        Class<?> klass = Class.forName(klassName);

        try (Consumer<String, String> consumer = kafkaConsumer(groupId)) {
            consumer.subscribe(Collections.singletonList(topic));
            poll(consumer, 100)
                .forEach(record -> index(record, operations, objectMapper, sink, klass));
        }
    }

    private static void index(ConsumerRecord<String, String> record, RestOperations restOperations, ObjectMapper objectMapper,
                              String url, Class<?> klass) {
        Try.of(() -> objectMapper.readValue(record.value(), klass))
            .onSuccess(kase -> restOperations.postForObject(url, kase, klass))
            .onFailure(Throwable::printStackTrace);

    }
}
