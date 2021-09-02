package net.cho.api.quiz.service;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
@RequiredArgsConstructor
@Service
public class KafkaSender {
    private final KafkaTemplate<String, String> kafkaTemplate;

    String kafkaTopic = "kafka-topic";
    public void send(String data){
        kafkaTemplate.send(kafkaTopic,data);

    }
}
