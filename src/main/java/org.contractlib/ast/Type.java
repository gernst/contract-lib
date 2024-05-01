package org.contractlib.ast;

import java.util.List;

public sealed interface Type {
	record Param(String name) implements Type {
	}

	record Sort(String name, List<Type> arguments) implements Type {
	}
}