package me.g1tommy.projectreactor.operators.domain;

import java.util.List;

import lombok.Getter;

@Getter
public class Node {

	private final int number;
	private final List<Node> children;

	public Node(int number, Node... children) {
		this.number = number;
		this.children = List.of(children);
	}
}
