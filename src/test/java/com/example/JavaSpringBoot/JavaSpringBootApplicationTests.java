package com.example.JavaSpringBoot;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource("/test.properties")
class JavaSpringBootApplicationTests {

	@Test
	void contextLoads() {
	}

}
