package org.contractlib.ast;

import java.util.List;

public sealed interface Type {
	public static final Type BOOL = Mono("Bool");
	public static final Type INT = Mono("Int");
	public static final Type REAL = Mono("Real");

	public static Sort Mono(String name) {
		return new Sort(name, List.of());
	}

	record Param(String name) implements Type {
	}

	record Sort(String name, List<Type> arguments) implements Type {
	}
}