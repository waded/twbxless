package com.rationalagents.twbxless;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * A set of tests showing how twbxless works end-to-end. If these don't work, we don't have anything.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {"HYPEREXEC=lib/hyper", "URLPREFIX=classpath:"})
public class EndToEndTest {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	public void canGetFilenames() {
		assertEquals("filenames\r\nData/TableauTemp/TEMP_0kf7uk81qi1qyf18sg86d1m8pl9s.hyper\r\n",
			restTemplate.getForObject("http://localhost:" + port + "/filenames?url=classpath:animal-observations.twbx",
				String.class));
	}

	@Test
	public void canGetData() {
		assertThat(
			restTemplate.getForObject("http://localhost:" + port + "/data?url=classpath:animal-observations.twbx"
				+ "&filename=Data/TableauTemp/TEMP_0kf7uk81qi1qyf18sg86d1m8pl9s.hyper", String.class))
			.contains("Date,Animal Observed,Animal,Leg Count\r\n")
			.contains("2020-05-15,Frog,Frog,4\r\n");
	}

	@Test
	public void issue1CsvEncoding() throws IOException {
		assertEquals(
			restTemplate.getForObject("http://localhost:" + port + "/data?url=classpath:tricky.twbx"
				+ "&filename=.hyper", String.class),
				FileUtils.readToEnd(getClass().getClassLoader().getResourceAsStream("tricky.csv")));
	}
}
