package org.contractlib.sexpr;

public record Identifier(String name) implements Atom {
    public String toString() {
        return name;
    }
}