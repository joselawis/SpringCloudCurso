package com.lawis.springcloud.app.gateway.msvc_gateway_server;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(TestSecurityConfig.class)
class MsvcGatewayServerApplicationTests {

	@Test
	void contextLoads() {
	}

}
