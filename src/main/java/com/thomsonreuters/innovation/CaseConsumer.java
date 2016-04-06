package com.thomsonreuters.innovation;

import com.fasterxml.jackson.databind.ObjectMapper;
import javaslang.collection.Stream;
import javaslang.control.Try;
import org.apache.commons.cli.*;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Collections;
import java.util.Properties;

/**
 * A consumer which posts the un-marshalled messages from a queue to the supplied endpoint.
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

        RestOperations restOperations = new RestTemplate();
        ObjectMapper objectMapper = new ObjectMapper();

        String topic = (String) properties.get("topic");
        String sink = (String) properties.get("sink");
        Class<?> aClass = Class.forName((String) properties.get("payloadClass"));

        try (Consumer<String, String> consumer = new KafkaConsumer<>(properties)) {
            consumer.subscribe(Collections.singletonList(topic));
            Stream
                .continually(() -> consumer.poll(100))
                .flatMap(Stream::ofAll)
                .forEach(record ->
                    Try.of(() -> objectMapper.readValue(record.value(), aClass))
                       .onSuccess(aCase -> restOperations.postForObject(sink, aCase, aClass))
                       .onFailure(Throwable::printStackTrace)
                );
        }
    }
}
