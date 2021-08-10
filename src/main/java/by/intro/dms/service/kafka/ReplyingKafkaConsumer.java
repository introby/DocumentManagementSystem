package by.intro.dms.service.kafka;

import by.intro.dms.model.KafkaTestModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ReplyingKafkaConsumer {

    private static final Logger LOG = LoggerFactory.getLogger(ReplyingKafkaConsumer.class);

    @KafkaListener(id = "Request", topics = "${kafka.topic.request-topic}")
    @SendTo("${kafka.topic.requestreply-topic}")
    public KafkaTestModel listen(KafkaTestModel request) {

        int sum = request.getFirstNumber() + request.getSecondNumber();
        request.setAdditionalProperty("sum", sum);
        return request;
    }

    @KafkaListener(id = "Reply", topics = "${kafka.topic.requestreply-topic}")
    public void result(KafkaTestModel request) {
        Map<String, Object> additionalProperties = request.getAdditionalProperties();
        int sum = (int) additionalProperties.get("sum");
        LOG.error("received sum ='{}'", sum);
    }
}
