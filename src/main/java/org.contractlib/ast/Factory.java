package org.contractlib.ast;

import org.contractlib.util.Pair;
import org.contractlib.factory.Mode;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;

public class Factory implements org.contractlib.factory.Commands<Term, Type, Datatype, Command> {
    public Types types(List<String> params) {
        Types empty = new Types(Map.of());
        return empty.extend(params);
    }

    public Command declareSort(String name, Integer arity) {
        return new Command.DeclareSort(name, arity);
    }

    public Command defineSort(String name, List<String> params, Type body) {
        return new Command.DefineSort(name, params, body);
    }

    public Datatypes datatypes(List<Pair<String, Integer>> arities) {
        return new Datatypes();
    }

    public Command declareDatatypes(List<Pair<String, Integer>> arities, List<Datatype> datatypes) {
        return new Command.DeclareDatatypes(arities, datatypes);
    }

    public Terms terms(List<Pair<String, Type>> variables) {
        Terms empty = new Terms(Map.of());
        return empty.extended(variables);
    }

    public Command declareFun(String name, List<String> params, List<Type> arguments, Type result) {
        return new Command.DeclareFun(name, params, arguments, result);
    }

    public Command defineFun(String name, List<String> params, List<Pair<String, Type>> arguments, Type result,
            Term body) {
        return new Command.DefineFun(name, params, arguments, result, body);
    }

    public Command declareProc(String name, List<String> params, List<Pair<String, Pair<Mode, Type>>> arguments,
            List<Pair<Term, Term>> contracts) {
        return new Command.DeclareProc(name, params, arguments, contracts);
    }

    public Command assertion(Term term) {
        return new Command.Assert(term);
    }

    class Datatypes implements org.contractlib.factory.Datatypes<Type, Datatype> {
        public Types types(List<String> params) {
            Types empty = new Types(Map.of());
            return empty.extend(params);
        }

        public Datatype datatype(List<String> params, List<Pair<String, List<Pair<String, List<Type>>>>> constrs) {
            return new Datatype(params, constrs);
        }
    }

    class Types implements org.contractlib.factory.Types<Type> {
        final Map<String, Type.Param> context;

        public Types(Map<String, Type.Param> context) {
            this.context = context;
        }

        public Type identifier(String name) {
            if (context.containsKey(name)) {
                return context.get(name);
            } else {
                return sort(name, List.of());
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

    class Terms implements org.contractlib.factory.Terms<Term, Type> {
        final Map<String, Term.Variable> scope;

        public Terms(Map<String, Term.Variable> scope) {
            this.scope = scope;
        }

        public Term literal(Object value) {
            return new Term.Literal(value);
        }

        public Term identifier(String name) {
            if (scope.containsKey(name)) {
                return scope.get(name);
            } else {
                return application(name, List.of());
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