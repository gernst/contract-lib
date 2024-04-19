package org.contractlib.ast;

import org.contractlib.util.Pair;
import org.contractlib.factory.Mode;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class Factory implements org.contractlib.factory.Commands<Term, Type, Datatype, Command> {
	public Types types(List<String> params) {
		return Types.EMPTY.extend(params);
	}

	public Command declareSort(String name, Integer arity) {
		return null;
	}

	public Command defineSort(String name, List<String> params, Type body) {
		return null;
	}

	public Datatypes datatypes(List<Pair<String, Integer>> arities) {
		return null;
	}

	public Command declareDatatypes(List<Pair<String, Integer>> arities, List<Pair<String, Datatype>> datatypes) {
		return null;
	}

	public Terms terms(List<Pair<String, Type>> variables) {
		return Terms.EMPTY.extended(variables);
	}

	public Command declareFun(String name, List<String> params, List<Type> arguments, Type result) {
		return null;
	}

	public Command defineFun(String name, List<String> params, List<Pair<String, Type>> arguments, Type result,
			Term body) {
		return null;
	}

	public Command declareProc(String name, List<String> params, List<Pair<String, Pair<Mode, Type>>> arguments,
			List<Pair<Term, Term>> contracts) {
		return null;
	}

	public Command assertion(Term term) {
		return new Command.Assert(term);
	}

	record Datatypes() implements org.contractlib.factory.Datatypes<Type, Datatype> {
		public Datatype datatype(String name, List<String> params,
				List<Pair<String, List<Pair<String, List<Type>>>>> constrs) {
			return null;
		}
	}

	record Types(Map<String, Type.Param> context) implements org.contractlib.factory.Types<Type> {
		public static final Types EMPTY = new Types(Map.of());

		public Type identifier(String name) {
			switch (name) {
			case "Bool":
				return Type.BOOL;
			case "Int":
				return Type.INT;
			case "Real":
				return Type.REAL;
			default:
				// TODO: honor sorts without parameters here!
				return context.get(name);
			}
		}

		public Type sort(String name, List<Type> arguments) {
			return new Type.Sort(name, arguments);
		}

		public Types extend(List<String> params) {
			Map<String, Type.Param> context_ = new HashMap<>();

			context_.putAll(context);

			for (String name : params) {
				Type.Param param = new Type.Param(name);
				context_.put(name, param);
			}

			return new Types(context_);
		}
	}

	record Terms(Map<String, Term.Variable> scope) implements org.contractlib.factory.Terms<Term, Type> {
		public static final Terms EMPTY = new Terms(Map.of());

		public Term literal(Object value) {
			return new Term.Literal(value);
		}

		public Term identifier(String name) {
			switch (name) {
			case "true":
				return Term.TRUE;

			case "false":
				return Term.FALSE;

			default:
                // TODO: honor constants/nullary functions here
				return scope.get(name);
			}
		}

		public Term old(Term argument) {
			return new Term.Old(argument);
		}

		public Term application(String function, List<Term> arguments) {
			return new Term.Application(function, arguments);
		}

		public Term binder(String binder, List<Pair<String, Type>> formals, Term body) {
			List<Term.Variable> variables = new ArrayList<>();

			for (Pair<String, Type> formal : formals) {
				String name = formal.first();
				variables.add(scope.get(name));
			}

			return new Term.Binder(binder, variables, body);
		}

		public Terms extended(List<Pair<String, Type>> formals) {
			Map<String, Term.Variable> scope_ = new HashMap<>();

			scope_.putAll(scope);

			for (Pair<String, Type> formal : formals) {
				String name = formal.first();
				Type type = formal.second();

				Term.Variable variable = new Term.Variable(name, type);
				scope_.put(name, variable);
			}

			return new Terms(scope_);
		}
	}
}