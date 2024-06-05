package org.contractlib.factory;

import org.contractlib.util.Pair;

import java.util.List;

public interface Abstractions<Type, Abstraction> {
    Types<Type> types(List<String> params);

    Abstraction abstraction(List<String> params, List<Pair<String, List<Pair<String, List<Type>>>>> constructors);
}