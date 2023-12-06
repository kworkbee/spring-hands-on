package me.g1tommy.webflux.controller;

import java.time.Duration;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

@WebFluxTest(SSEController.class)
class SSEControllerTests {

	@Autowired
	WebTestClient webTestClient;

	@Test
	void testSSEUsingServerSentEvent() {
		webTestClient.get()
				.uri("/sse/using-server-sent-event")
				.exchange()
				.expectStatus().isOk()
				.expectHeader().contentTypeCompatibleWith(MediaType.TEXT_EVENT_STREAM)
				.expectBodyList(String.class)
				.hasSize(2)
				.contains("1st Response", "2nd Response");
	}

	@Test
	void testSSEUsingFlux() {
		webTestClient = webTestClient.mutate()
						.responseTimeout(Duration.ofSeconds(11L))
						.build();

		webTestClient.get()
				.uri("/sse/using-flux")
				.exchange()
				.expectStatus().isOk()
				.expectHeader().contentTypeCompatibleWith(MediaType.TEXT_EVENT_STREAM)
				.expectBodyList(String.class)
				.hasSize(11)
				.contains("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "Complete");
	}
}