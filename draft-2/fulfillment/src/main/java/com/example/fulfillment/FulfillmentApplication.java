package com.example.fulfillment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@SpringBootApplication
public class FulfillmentApplication {

    @Bean
    RouterFunction<ServerResponse> http() {
        return route()
                .GET("/fulfillment/{fid}", r -> {
                    System.out.println("order-id: " + r.pathVariable("fid"));
                    return ServerResponse.ok().bodyValue(true);
                })
                .build();
    }

    public static void main(String[] args) {
        SpringApplication.run(FulfillmentApplication.class, args);
    }

}
