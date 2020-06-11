package com.example.customersatisfaction;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;

@EnableBinding(Sink.class)
@SpringBootApplication
public class CustomerSatisfactionApplication {

    @StreamListener(Sink.INPUT)
    public void consume(String msg) {
        System.out.println("incoming: " + msg);
    }

    public static void main(String[] args) {
        SpringApplication.run(CustomerSatisfactionApplication.class, args);
    }
}
