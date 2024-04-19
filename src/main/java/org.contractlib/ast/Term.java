package org.contractlib.ast;

import java.util.List;

public sealed interface Term {
	static final Term TRUE = Constant("true");
	static final Term FALSE = Constant("false");

	static Application Constant(String name) {
		return new Application(name, List.of());
	}

	record Literal(Object value) implements Term {
	}

	record Variable(String name, Type type) implements Term {
	}

	record Old(Term argument) implements Term {
	}

	record Application(String function, List<Term> arguments) implements Term {
	}

	record Binder(String binder, List<Variable> formals, Term body) implements Term {

	}
}