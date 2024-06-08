package org.contractlib.factory;

import java.util.List;

public interface Types<TYPE> {
    TYPE identifier(String name);

    TYPE sort(String name, List<TYPE> arguments);

    Types<TYPE> extend(List<String> params);
}