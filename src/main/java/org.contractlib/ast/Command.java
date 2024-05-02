package org.contractlib.ast;

import org.contractlib.util.Pair;
import org.contractlib.factory.Mode;

import java.util.List;

public sealed interface Command {
	record Assert(Term formula) implements Command {
	}

	record DeclareSort(String name, Integer arity) implements Command {
	}

	record DefineSort(String name, List<String> params, Type body) implements Command {
	}

	record DeclareDatatypes(List<Pair<String, Integer>> arities, List<Datatype> datatypes)
			implements Command {
	}

	record DeclareFun(String name, List<String> params, List<Type> arguments, Type result) implements Command {
	}

	record DefineFun(String name, List<String> params, List<Pair<String, Type>> arguments, Type result, Term body)
			implements Command {
	}

	record DeclareProc(String name, List<String> params, List<Pair<String, Pair<Mode, Type>>> arguments,
			List<Pair<Term, Term>> contracts) implements Command {
	}
}