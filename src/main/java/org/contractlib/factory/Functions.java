package org.contractlib.factory;

import org.contractlib.util.Pair;

import java.util.List;

public interface Functions<TYPE, FUNCTION> {

    FUNCTION funDec(String name, List<String> params, List<Pair<String, TYPE>> arguments, TYPE result);
}
