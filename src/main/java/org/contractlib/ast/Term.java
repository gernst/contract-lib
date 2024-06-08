package org.contractlib.ast;

import java.util.List;
import java.util.ArrayList;

public sealed interface Term {
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