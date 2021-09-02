package net.cho.api.config;

import lombok.RequiredArgsConstructor;
import net.cho.api.config.KafkaProducers;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/kafka")
public class KafkaController {
    private final KafkaProducers producers;

    @GetMapping
    public String hello(){
        return "Hello Kafka";
    }

    @PostMapping
    public String sendMessage(@RequestParam("message") String message){
        System.out.println("######## producer 진입 #############");
        this.producers.SendMessage(message);
        return "Message sent to Kafka Successfully";
    }

}
