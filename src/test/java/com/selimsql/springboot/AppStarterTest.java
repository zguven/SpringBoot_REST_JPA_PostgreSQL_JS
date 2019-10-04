package com.selimsql.springboot;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class AppStarterTest {
	private static final String APP_NAME = AppStarterTest.class.getSimpleName();

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	public void contextLoads() {
		System.out.println(APP_NAME + ".contextLoads");
	}

	@Test
	public void getMainPage() throws Exception {
		String urlBase = "http://localhost:" + port + "/";
		System.out.println(APP_NAME + ".contextLoads.urlBase: " + urlBase);
		ResponseEntity<String> response = restTemplate.getForEntity(urlBase, String.class);
		HttpStatus httpStatus = response.getStatusCode();
		System.out.println(".response.getStatusCode: " + httpStatus.name());
		assertEquals(HttpStatus.OK, httpStatus);
	}
}
