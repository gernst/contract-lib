package org.contractlib.factory;

import org.contractlib.util.Pair;

import java.util.List;

public interface Terms<Term, Type> {
    Term literal(Object value);

    Term identifier(String name);

    Term old(Term argument);

    Term application(String function, List<Term> arguments);

    Term binder(String binder, List<Pair<String, Type>> formals, Term body);

    Terms<Term, Type> extended(List<Pair<String, Type>> formals);
}