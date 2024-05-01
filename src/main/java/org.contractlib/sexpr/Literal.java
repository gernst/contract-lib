package org.contractlib.sexpr;

public record Literal(Object value) implements Atom {
	public String toString() {
		return value.toString();
	}
}