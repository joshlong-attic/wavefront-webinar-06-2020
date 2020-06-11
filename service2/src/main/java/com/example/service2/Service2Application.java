package com.example.service2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.util.Collections;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@SpringBootApplication
@EnableBinding(Source.class)
public class Service2Application {

    public static void main(String[] args) {
        SpringApplication.run(Service2Application.class, args);
    }

    @Bean
    RouterFunction<ServerResponse> routes(Source source) {
        return route()
                .GET("/hello-again/{name}", r -> {
                    var name = r.pathVariable("name") ;
                    var stringMessage = "Hello  " + name + "!";
                    var msg = MessageBuilder.withPayload(stringMessage).build();
                    source.output().send(msg);
                    return ServerResponse.ok().bodyValue(Collections.singletonMap("greeting", stringMessage));
                })
                .build();

    }
}
