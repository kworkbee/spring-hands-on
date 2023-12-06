package me.g1tommy.webflux.controller;

import java.time.Duration;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sse")
public class SSEController {

	@GetMapping("/using-server-sent-event")
	public Flux<ServerSentEvent<String>> sseUsingServerSentEvent() {
		return Flux.concat(
				Mono.just("1st Response")
						.map(data -> ServerSentEvent.builder(data).build()),
				Mono.just("2nd Response")
						.delayElement(Duration.ofSeconds(1L))
						.map(data -> ServerSentEvent.builder(data).build())
		);
	}

	@GetMapping(value = "/using-flux", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<String> sseUsingFlux() {
		return Flux.concat(
				Flux.zip(
								Flux.interval(Duration.ofSeconds(1L)),
								Flux.range(1, 10)
										.map(String::valueOf)
										.repeat()
						)
						.map(Tuple2::getT2)
						.takeUntil(str -> Integer.parseInt(str) >= 10),
				Mono.just("Complete")
		);
	}
}
