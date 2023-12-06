package me.g1tommy.projectreactor.operators;

import me.g1tommy.projectreactor.operators.domain.Node;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

class FluxExpandTest {

	@Test
	@DisplayName("Evaluate each source by BFS method")
	void expandTest() {
		Node root = getSampleNodeGraph();
		Flux<Integer> expandFlux = Flux.just(root)
				.expand(v -> Flux.fromIterable(v.getChildren()))
				.map(Node::getNumber);

		expandFlux
				.as(StepVerifier::create)
				.expectNext(1)
				.expectNext(2, 3, 4)
				.expectNext(21, 31, 41)
				.verifyComplete();
	}

	@Test
	@DisplayName("Evaluate each source by DFS method")
	void expandDeepTest() {
		Node root = getSampleNodeGraph();
		Flux<Integer> expandDeepFlux = Flux.just(root)
				.expandDeep(v -> Flux.fromIterable(v.getChildren()))
				.map(Node::getNumber);

		expandDeepFlux
				.as(StepVerifier::create)
				.expectNext(1)
				.expectNext(2, 21)
				.expectNext(3, 31)
				.expectNext(4, 41)
				.verifyComplete();
	}

	private Node getSampleNodeGraph() {
		/**
		 * The graph looks like below:
		 *     1
		 *   2 3 4
		 * 21 31 41
		 */
		return new Node(1,
				new Node(2, new Node(21)),
				new Node(3, new Node(31)),
				new Node(4, new Node(41))
		);
	}
}
