package com.example.service1;

import brave.sampler.Sampler;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@SpringBootApplication
public class Service1Application {

    private final Counter counter;

    Service1Application(MeterRegistry registry) {
        this.counter = Counter
                .builder("greetings-counter")
                .tags("region", "test") // optional
                .register(registry);
    }

    @Bean
    WebClient webClient(WebClient.Builder builder) {
        return builder.build();
    }

    @Bean
    Sampler defaultSampler() {
        return Sampler.ALWAYS_SAMPLE;
    }

    @Bean
    RouterFunction<ServerResponse> http(WebClient httpClient) {
        return route()
                .GET("/hello/{name}", r -> {
                    this.counter.increment();
                    String name = r.pathVariable("name");
                    Flux<String> stringFlux = httpClient
                            .get()
                            .uri("http://localhost:8090/hello-again/{name}", name)
                            .retrieve()
                            .bodyToFlux(String.class);
                    return ServerResponse.ok().body(stringFlux, String.class);
                })
                .build();
    }

    public static void main(String[] args) {
        SpringApplication.run(Service1Application.class, args);
    }
}
