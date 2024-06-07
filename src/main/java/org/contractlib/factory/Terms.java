package org.contractlib.factory;

import org.contractlib.util.Pair;

import java.util.List;

/**
 * An abstract factory for creating terms.
 * @param <TERM>
 * @param <TYPE>
 */
public interface Terms<TERM, TYPE> {
    TERM literal(Object value);

    TERM identifier(String name);

    TERM old(TERM argument);

    TERM application(String function, List<TERM> arguments);

    TERM binder(String binder, List<Pair<String, TYPE>> formals, TERM body);

    Terms<TERM, TYPE> extended(List<Pair<String, TYPE>> formals);
}