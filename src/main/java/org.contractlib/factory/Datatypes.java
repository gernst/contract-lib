package org.contractlib.factory;

import org.contractlib.util.Pair;

import java.util.List;

public interface Datatypes<Type, Datatype> {
	Types<Type> types(List<String> params);

	Datatype datatype(List<String> params, List<Pair<String, List<Pair<String, List<Type>>>>> constructors);
}