package com.imatia.statemachine.config;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class StateMachineApplicationTests {

	@Test
	void contextLoad() {
		Assertions.assertThat(Boolean.TRUE).isTrue();
	}

}
