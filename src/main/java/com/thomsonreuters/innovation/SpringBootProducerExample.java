package com.thomsonreuters.innovation;

import javaslang.collection.Stream;
import javaslang.concurrent.Future;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.thomsonreuters.innovation.KafkaResources.kafkaProducer;
import static javaslang.concurrent.Future.fromJavaFuture;

/**
 * Producer using Spring Boot
 */
@Controller
@EnableAutoConfiguration
@ComponentScan("com.thomsonreuters.innovation")
@Import({SimpleProducerExample.class, SimpleConsumerExample.class})
public class SpringBootProducerExample {

    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public String home(Model model) {
        CaseBean caseBean = new CaseBean();
        model.addAttribute("caseBean", caseBean);
        return "home";
    }

    @RequestMapping(value="/home", method=RequestMethod.POST)
    public String produceMessage(@ModelAttribute CaseBean caseBean, Model model) {
        String caseId = caseBean.getCaseId();

        ExecutorService executorService = Executors.newCachedThreadPool();
        try (Producer<String, String> producer = kafkaProducer()) {
            Future
                    .sequence(executorService, Stream
                            .range(0, 1)
                            .map(value -> new ProducerRecord<>("mytopic", "key ", caseId))
                            .map(record -> fromJavaFuture(executorService, producer.send(record))))
                    .onFailure(Throwable::printStackTrace)
                    .onSuccess(sequence -> System.out.println("Completed Successfully, sent: " + sequence.size()))
                    .await();
        }
        finally {
            executorService.shutdown();
        }
        model.addAttribute("caseBean", caseBean);
        return "outcome";
    }

    @RequestMapping("/message/case/{caseId}")
    @ResponseBody
    public void produceMessage(@PathVariable String caseId) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        try (Producer<String, String> producer = kafkaProducer()) {
            Future
                    .sequence(executorService, Stream
                            .range(0, 1)
                            .map(value -> new ProducerRecord<>("mytopic", "key ", caseId))
                            .map(record -> fromJavaFuture(executorService, producer.send(record))))
                    .onFailure(Throwable::printStackTrace)
                    .onSuccess(sequence -> System.out.println("Completed Successfully, sent: " + sequence.size()))
                    .await();
        }
        finally {
            executorService.shutdown();
        }
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(SpringBootProducerExample.class, args);
    }
}
