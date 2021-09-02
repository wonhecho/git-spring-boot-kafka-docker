package net.cho.api.config;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class KafkaProducers {
    private static final String TOPIC = "sample";
    private final KafkaTemplate<String,String> kafkaTemplate;

    public void SendMessage(String message){
        System.out.println(" Producer Message " + message);


    }
}
