package org.contractlib.factory;

import org.contractlib.util.Pair;

import java.util.List;

public interface Datatypes<Type, Datatype> {
	Datatype datatype(String name, List<String> params, List<Pair<String, List<Pair<String, List<Type>>>>> constrs);
}