/*     / \____  _    _  ____   ______  / \ ____  __    _ _____
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  / /  _  \   Javaslang
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/  \__/  /   Copyright 2014-now Daniel Dietrich
 * /___/\_/  \_/\____/\_/  \_/\__\/__/___\_/  \_//  \__/_____/    Licensed under the Apache License, Version 2.0
 */
package com.thomsonreuters.innovation;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.HttpClientConfig;
import io.searchbox.core.Index;
import javaslang.Tuple2;
import javaslang.control.Try;
import org.apache.kafka.clients.consumer.Consumer;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.thomsonreuters.innovation.KafkaResources.kafkaConsumer;
import static com.thomsonreuters.innovation.KafkaResources.poll;

/**
 * Very simple consumer of a topic 'mytopic'.  Creates an infinite stream over a poll operation and then outputs all
 * of the messages returned.
 */
public class SimpleConsumerExample {
    public static void main(String[] args) {

        JestClientFactory factory = new JestClientFactory();
        factory.setHttpClientConfig(
            new HttpClientConfig.Builder("http://localhost:9200")
                .multiThreaded(true)
                .build());

        JestClient client = factory.getObject();

        String groupId = "elastic-search";
        try (Consumer<String, String> consumer = kafkaConsumer("postgres")) {
            consumer.subscribe(Collections.singletonList("mytopic"));
            poll(consumer, 100)
                .map(record -> new Tuple2<>(record.key(), record.value()))
                .forEach(record -> index(record._1() + record._2(), client));
        }
    }

    private static void index(String message, JestClient client) {
        Map<String, String> source = new LinkedHashMap<>();
        source.put("message", message);

        Index index = new Index.Builder(source).index("messages").type("message").build();
        Try.run(() -> client.execute(index))
            .onFailure(Throwable::printStackTrace);
    }
}
