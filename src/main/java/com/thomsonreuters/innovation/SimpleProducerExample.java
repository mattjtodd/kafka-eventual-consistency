/*     / \____  _    _  ____   ______  / \ ____  __    _ _____
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  / /  _  \   Javaslang
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/  \__/  /   Copyright 2014-now Daniel Dietrich
 * /___/\_/  \_/\____/\_/  \_/\__\/__/___\_/  \_//  \__/_____/    Licensed under the Apache License, Version 2.0
 */
package com.thomsonreuters.innovation;

import javaslang.collection.Stream;
import javaslang.concurrent.Future;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.thomsonreuters.innovation.KafkaResources.kafkaProducer;
import static javaslang.concurrent.Future.fromJavaFuture;

/**
 * Provides a fully asynchronous way of sending messages and outputting the resultant RecordMetadata.  Javaslang's future
 * provides the ability to handle the Future which is returned by the Producer's send message asynchronously.
 */
public class SimpleProducerExample {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        try (Producer<String, String> producer = kafkaProducer()) {
            Future
                .sequence(executorService, Stream
                    .range(0, 10)
                    .map(value -> UUID.randomUUID().toString() + " count " + value)
                    .map(value -> new ProducerRecord<String, String>("mytopic", value))
                    .map(record -> fromJavaFuture(executorService, producer.send(record))))
                .onFailure(Throwable::printStackTrace)
                .onSuccess(sequence -> System.out.println("Completed Successfully, sent: " + sequence.size()))
                .await();
        }
        finally {
            executorService.shutdown();
        }
    }
}

