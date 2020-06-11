package com.example.service3;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@TestPropertySource(properties = "management.metrics.export.wavefront.enabled=false")
@SpringBootTest
class Service3ApplicationTests {

	@Test
	void contextLoads() {
	}

}
