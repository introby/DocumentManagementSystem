package by.intro.dms.controller;

import by.intro.dms.model.KafkaTestModel;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping(value = "/api/v1/kafka")
public class KafkaTestController {

    private final ReplyingKafkaTemplate<String, Object, Object> replyingKafkaTemplate;

    @Value("${kafka.topic.request-topic}")
    String requestTopic;

    @Value("${kafka.topic.requestreply-topic}")
    String requestReplyTopic;

    public KafkaTestController(ReplyingKafkaTemplate<String, Object, Object> replyingKafkaTemplate) {
        this.replyingKafkaTemplate = replyingKafkaTemplate;
    }

    @PostMapping(value = "/sum", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Object sum(@RequestBody KafkaTestModel request) throws InterruptedException, ExecutionException {
        ProducerRecord<String, Object> record = new ProducerRecord<>(requestTopic, UUID.randomUUID().toString(), request);
        RequestReplyFuture<String, Object, Object> future =
                replyingKafkaTemplate.sendAndReceive(record);
        ConsumerRecord<String, Object> response = future.get();
        return response.value();
    }
}
