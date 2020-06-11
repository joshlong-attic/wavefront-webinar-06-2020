package com.example.service2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@SpringBootApplication
@EnableBinding(Source.class)
public class Service2Application {

    public static void main(String[] args) {
        SpringApplication.run(Service2Application.class, args);
    }

}

@RestController
class AnotherGreetingsRestController {

    private final MessageChannel channel;

    AnotherGreetingsRestController(Source source) {
        this.channel = source.output();
    }

    @GetMapping("/hello-again/{name}")
    Map<String, String> helloAgain(@PathVariable String name) {
        var stringMessage = "Hello  " + name + "!";
        var msg = MessageBuilder.withPayload(stringMessage).build();
        this.channel.send(msg);
        return Collections.singletonMap("greeting", stringMessage);
    }

}