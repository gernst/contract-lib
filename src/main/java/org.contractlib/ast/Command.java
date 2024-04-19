package org.contractlib.ast;

public sealed interface Command {
    record Assert(Term formula) implements Command {}
}