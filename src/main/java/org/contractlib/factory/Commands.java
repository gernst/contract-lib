package org.contractlib.factory;

import org.contractlib.ast.Abstraction;
import org.contractlib.util.Pair;

import java.util.List;

public interface Commands<Term, Type, Datatype, Command> {
    Types<Type> types(List<String> params);

    Command declareSort(String name, Integer arity);

    Command defineSort(String name, List<String> params, Type body);

    Command declareAbstractions(List<Pair<String, Integer>> arities, List<Abstraction> abstractions);

    Datatypes<Type, Datatype> datatypes(List<Pair<String, Integer>> arities);

    Abstractions<Type, Abstraction> abstractions(List<Pair<String, Integer>> arities);

    Command declareDatatypes(List<Pair<String, Integer>> arities, List<Datatype> datatypes);

    Terms<Term, Type> terms(List<Pair<String, Type>> variables);

    Command declareFun(String name, List<String> params, List<Type> arguments, Type result);

    Command defineFun(String name, List<String> params, List<Pair<String, Type>> arguments, Type result, Term body);

    Command defineContract(String name, List<Pair<String, Pair<Mode, Type>>> arguments,
                           List<Pair<Term, Term>> contracts);

    Command assertion(Term term);
}