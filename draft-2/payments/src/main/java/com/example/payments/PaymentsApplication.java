package com.example.payments;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;

import java.util.Collections;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@SpringBootApplication
@EnableBinding(Source.class)
public class PaymentsApplication {

    public PaymentsApplication(ObjectMapper objectMapper, MeterRegistry meterRegistry) {
        this.objectMapper = objectMapper;
        this.meterRegistry = meterRegistry;
        this.counter = Counter
                .builder("payments")
                .tag("region", "us-west")
                .register(this.meterRegistry);
    }

    @Bean
    WebClient webClient(WebClient.Builder builder) {
        return builder.build();
    }

    private final ObjectMapper objectMapper;
    private final Counter counter;
    private final MeterRegistry meterRegistry;


    private String json(Object o) {
        try {
            return this.objectMapper.writeValueAsString(o);
        } catch (Exception e) {
            // don't care
        }
        return null;
    }


    @Bean
    RouterFunction<ServerResponse> http(WebClient http, Source kafka) {
        return route()
                .GET("/payments", request -> {
                    // launch a call to fulfillment and to customer-service
                    String orderId = request.queryParam("orderId").get();
                    // custom metric

                    this.counter.increment(Math.random() * 1000);

                    // HTTP
                    Flux<String> launchFulfillment = http
                            .get()
                            .uri("http://localhost:8090/fulfillment/{orderId}", orderId).retrieve()
                            .bodyToFlux(String.class);


                    // Kafka
                    String payload = json(Collections.singletonMap("orderId", orderId));
                    kafka.output().send(MessageBuilder.withPayload(payload).build());

                    return ServerResponse.ok().body(launchFulfillment, String.class);
                })
                .build();
    }

    public static void main(String[] args) {
        SpringApplication.run(PaymentsApplication.class, args);
    }

}
