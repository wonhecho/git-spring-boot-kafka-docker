package net.cho.api.config;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class KafkaConsumers {
    @KafkaListener(topics = "sample", groupId = "myGroup")
    public void consume ( String message) throws IOException {
        System.out.println("Consumer Message Test : " + message);
    }
}
