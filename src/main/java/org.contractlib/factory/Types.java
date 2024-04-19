package org.contractlib.factory;

import org.contractlib.util.Pair;

import java.util.List;

public interface Types<Type> {
    Type identifier(String name);

    Type sort(String name, List<Type> arguments);

    Types<Type> extend(List<String> params);
}