package org.contractlib.parser;

import org.contractlib.antlr4parser.ContractLIBBaseVisitor;
import org.contractlib.antlr4parser.ContractLIBParser;
import org.contractlib.ast.*;
import org.contractlib.factory.Mode;
import org.contractlib.factory.Types;
import org.contractlib.util.Pair;
import org.jetbrains.annotations.NotNull;

import org.contractlib.factory.*;

import java.util.ArrayList;
import java.util.List;

public class ContractLibANTLRParser extends ContractLIBBaseVisitor<Void> {

    private final Commands<Term, Type, Datatype, Command> factory = new Factory();
    private final List<Command> commands = new ArrayList<>();

    public List<Command> getCommands() {
        return commands;
    }

    @Override
    public Void visitCmd_declareAbstractions(ContractLIBParser.Cmd_declareAbstractionsContext ctx) {

        List<Pair<String, Integer>> arities = new ArrayList<>();
        for (var a : ctx.sort_dec()) {
            String name = convertSymbol(a.symbol());
            Integer arity = Integer.parseInt(a.numeral().Numeral().getText());
            arities.add(new Pair<>(name, arity));
        }

        List<String> params = new ArrayList<>();
        for (var p : ctx.sort_dec()) {
            params.add(convertSymbol(p.symbol()));
            //System.out.println("Found datatype declaration: " + p.getText());
        }
        Types<Type> context = factory.types(params);

        List<Abstraction> abstractions = new ArrayList<>();
        for (var d : ctx.datatype_dec()) {
            Abstraction abstr = convertAbstraction(d, params, context);
            abstractions.add(abstr);
            //System.out.println("Found declaration of abstraction " + abstr);
        }
        commands.add(factory.declareAbstractions(arities, abstractions));

        return null;

    }

    private @NotNull Abstraction convertAbstraction(ContractLIBParser.Datatype_decContext ctx,
                                                    List<String> params,
                                                    Types<Type> context) {
        List<Pair<String, List<Pair<String, List<Type>>>>> constrs = new ArrayList<>();
        for (var ctr : ctx.constructor_dec()) {
            constrs.add(convertConstructor(ctr, context));
        }
        return new Abstraction(params, constrs);
    }

    private Pair<String, List<Pair<String, List<Type>>>> convertConstructor(ContractLIBParser.Constructor_decContext constr,
                                                                            Types<Type> params) {
        String ctrName = convertSymbol(constr.symbol());
        List<Pair<String, List<Type>>> selectors = new ArrayList<>();
        for (var selector : constr.selector_dec()) {
            String name = convertSymbol(selector.symbol());
            Type t = convertSort(selector.sort(), params);

            Pair<String, List<Type>> sel = new Pair<>(name, List.of(t));
            selectors.add(sel);
        }
        return new Pair<>(ctrName, selectors);
    }

    private <Type> Type convertSort(ContractLIBParser.SortContext sort, Types<Type> params) {
        String identifier = sort.identifier().getText();

        // TODO: repair parametric sorts
        /*List<Type> args = new ArrayList<>();
        if (sort.sort() != null) {
            // parametric sort
            for (var arg : sort.sort()) {
                args.add(convertSort(arg, params));
            }
        }
        return new Type.Sort(identifier, args);*/
        return params.identifier(identifier);
    }


    public String convertSymbol(ContractLIBParser.SymbolContext ctx) {
        if (ctx.quotedSymbol() != null) {
            return ctx.quotedSymbol().QuotedSymbol().getSymbol().getText();
        } else {
            return ctx.simpleSymbol().getText();
        }
    }

    @Override
    public Void visitCmd_defineContract(ContractLIBParser.Cmd_defineContractContext ctx) {

        String name = convertSymbol(ctx.symbol());
        //System.out.println("Found contract for " + name);

        List<Pair<String, Pair<Mode, Type>>> formalWithMode = new ArrayList<>();

        Types<Type> context = factory.types(List.of());
        for (var f : ctx.formal()) {
            formalWithMode.add(convertFormal(f, context));
        }
        List<Pair<String, Type>> formal = formalWithMode.stream()
            .map(x -> new Pair<>(x.first(), x.second().second())).toList();

        List<Pair<Term, Term>> contracts = new ArrayList<>();
        for (var c : ctx.contract()) {
            contracts.add(convertContract(c, context, formal));
        }

        commands.add(factory.defineContract(name, formalWithMode, contracts));
        return null;
    }

    private Pair<Term, Term> convertContract(ContractLIBParser.ContractContext ctx,
                                             Types<Type> context,
                                             List<Pair<String, Type>> vars) {
        Terms<Term, Type> scope = factory.terms(vars);
        Term pre = convertTerm(ctx.term(0), scope, context);
        Term post = convertTerm(ctx.term(1), scope, context);

        return new Pair<>(pre, post);
    }

    private <Term, Type> Term convertTerm(ContractLIBParser.TermContext ctx,
                             Terms<Term, Type> scope,
                             Types<Type> vars) {
        if (ctx.GRW_Exists() != null) {
            List<Pair<String, Type>> boundVars = new ArrayList<>();
            for (ContractLIBParser.Sorted_varContext bv : ctx.sorted_var()) {
                boundVars.add(convertSortedVar(bv, vars));
            }
            Terms<Term, Type> ext = scope.extended(boundVars);
            return scope.binder("exists", boundVars, convertTerm(ctx.term(0), ext, vars));

        } else if (ctx.GRW_Forall() != null) {
            List<Pair<String, Type>> boundVars = new ArrayList<>();
            for (ContractLIBParser.Sorted_varContext bv : ctx.sorted_var()) {
                boundVars.add(convertSortedVar(bv, vars));
            }
            Terms<Term, Type> ext = scope.extended(boundVars);
            return scope.binder("forall", boundVars, convertTerm(ctx.term(0), ext, vars));

        } else if (ctx.GRW_Old() != null) {
            return scope.old(convertTerm(ctx.term(0), scope, vars));

        } else if (ctx.spec_constant() != null) {
            return scope.literal(ctx.spec_constant().getText());

        } else if (ctx.qual_identifer() != null && ctx.term() != null) {
            String ident = ctx.qual_identifer().getText();
            List<Term> args = new ArrayList<>();
            for (var t : ctx.term()) {
                args.add(convertTerm(t, scope, vars));
            }
            if (args.isEmpty()) {
                return scope.identifier(ident);
            }
            return scope.application(ident, args);

        } else if (ctx.qual_identifer() != null) {
            String ident = ctx.qual_identifer().getText();
            return scope.identifier(ident);

        } else {
            // TODO: support other types of terms
        }
        return null;
    }

    private <Type> Pair<String, Type> convertSortedVar(ContractLIBParser.Sorted_varContext ctx,
                                                Types<Type> context) {
        String name = convertSymbol(ctx.symbol());
        Type type = convertSort(ctx.sort(), context);
        return new Pair<>(name, type);
    }

    private Pair<String, Pair<Mode, Type>> convertFormal(ContractLIBParser.FormalContext ctx,
                                                         Types<Type> context) {
        String name = convertSymbol(ctx.symbol());
        Mode mode = convertMode(ctx.argument_mode());
        Type type = convertSort(ctx.sort(), context);
        return new Pair<>(name, new Pair<>(mode, type));
    }

    private Mode convertMode(ContractLIBParser.Argument_modeContext ctx) {
        return switch (ctx.getText()) {
            case "in" -> Mode.IN;
            case "out" -> Mode.OUT;
            default -> Mode.INOUT;
        };
    }
}
