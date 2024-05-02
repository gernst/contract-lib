package org.contractlib.sexpr;

public record Keyword(String name) implements Atom {
    public String toString() {
        return ":" + name;
    }
}