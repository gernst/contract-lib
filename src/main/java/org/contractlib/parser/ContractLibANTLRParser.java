package org.contractlib.parser;

import org.contractlib.antlr4parser.ContractLIBBaseVisitor;
import org.contractlib.antlr4parser.ContractLIBParser;
import org.contractlib.factory.Mode;
import org.contractlib.factory.Types;
import org.contractlib.util.Pair;
import org.jetbrains.annotations.NotNull;

import org.contractlib.factory.*;

import java.util.ArrayList;
import java.util.List;

public class ContractLibANTLRParser<TERM, TYPE, ABS, DT, COMMAND> extends ContractLIBBaseVisitor<Void> {

    private final Commands<TERM, TYPE, ABS, DT, COMMAND> factory;
    private final List<COMMAND> commands = new ArrayList<>();

    public ContractLibANTLRParser(Commands<TERM, TYPE, ABS, DT, COMMAND> factory) {
        this.factory = factory;
    }

    public List<COMMAND> getCommands() {
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
        Types<TYPE> context = factory.types(params);

        List<ABS> abstractions = new ArrayList<>();
        for (var d : ctx.datatype_dec()) {
            ABS abstr = convertAbstraction(d, params, context, arities);
            abstractions.add(abstr);
            //System.out.println("Found declaration of abstraction " + abstr);
        }
        commands.add(factory.declareAbstractions(arities, abstractions));

        return null;

    }

    private @NotNull ABS convertAbstraction(ContractLIBParser.Datatype_decContext ctx,
                                            List<String> params,
                                            Types<TYPE> context,
                                            List<Pair<String, Integer>> arities) {
        List<Pair<String, List<Pair<String, List<TYPE>>>>> constrs = new ArrayList<>();
        for (var ctr : ctx.constructor_dec()) {
            constrs.add(convertConstructor(ctr, context));
        }
        Abstractions<TYPE, ABS> abs = factory.abstractions(arities);
        return abs.abstraction(params, constrs);
    }

    private Pair<String, List<Pair<String, List<TYPE>>>> convertConstructor(ContractLIBParser.Constructor_decContext constr,
                                                                            Types<TYPE> params) {
        String ctrName = convertSymbol(constr.symbol());
        List<Pair<String, List<TYPE>>> selectors = new ArrayList<>();
        for (var selector : constr.selector_dec()) {
            String name = convertSymbol(selector.symbol());
            TYPE t = convertSort(selector.sort(), params);

            Pair<String, List<TYPE>> sel = new Pair<>(name, List.of(t));
            selectors.add(sel);
        }
        return new Pair<>(ctrName, selectors);
    }

    private TYPE convertSort(ContractLIBParser.SortContext sort, Types<TYPE> params) {
        String identifier = sort.identifier().getText();

        // TODO: repair parametric sorts
        /*List<TYPE> args = new ArrayList<>();
        if (sort.sort() != null) {
            // parametric sort
            for (var arg : sort.sort()) {
                args.add(convertSort(arg, params));
            }
        }
        return new TYPE.Sort(identifier, args);*/
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

        List<Pair<String, Pair<Mode, TYPE>>> formalWithMode = new ArrayList<>();

        Types<TYPE> context = factory.types(List.of());
        for (var f : ctx.formal()) {
            formalWithMode.add(convertFormal(f, context));
        }
        List<Pair<String, TYPE>> formal = formalWithMode.stream()
            .map(x -> new Pair<>(x.first(), x.second().second())).toList();

        List<Pair<TERM, TERM>> contracts = new ArrayList<>();
        for (var c : ctx.contract()) {
            contracts.add(convertContract(c, context, formal));
        }

        commands.add(factory.defineContract(name, formalWithMode, contracts));
        return null;
    }

    private Pair<TERM, TERM> convertContract(ContractLIBParser.ContractContext ctx,
                                             Types<TYPE> context,
                                             List<Pair<String, TYPE>> vars) {
        Terms<TERM, TYPE> scope = factory.terms(vars);
        TERM pre = convertTerm(ctx.term(0), scope, context);
        TERM post = convertTerm(ctx.term(1), scope, context);

        return new Pair<>(pre, post);
    }

    private TERM convertTerm(ContractLIBParser.TermContext ctx,
                             Terms<TERM, TYPE> scope,
                             Types<TYPE> vars) {
        if (ctx.GRW_Exists() != null) {
            List<Pair<String, TYPE>> boundVars = new ArrayList<>();
            for (ContractLIBParser.Sorted_varContext bv : ctx.sorted_var()) {
                boundVars.add(convertSortedVar(bv, vars));
            }
            Terms<TERM, TYPE> ext = scope.extended(boundVars);
            return scope.binder("exists", boundVars, convertTerm(ctx.term(0), ext, vars));

        } else if (ctx.GRW_Forall() != null) {
            List<Pair<String, TYPE>> boundVars = new ArrayList<>();
            for (ContractLIBParser.Sorted_varContext bv : ctx.sorted_var()) {
                boundVars.add(convertSortedVar(bv, vars));
            }
            Terms<TERM, TYPE> ext = scope.extended(boundVars);
            return scope.binder("forall", boundVars, convertTerm(ctx.term(0), ext, vars));

        } else if (ctx.GRW_Old() != null) {
            return scope.old(convertTerm(ctx.term(0), scope, vars));

        } else if (ctx.spec_constant() != null) {
            return scope.literal(ctx.spec_constant().getText());

        } else if (ctx.qual_identifer() != null && ctx.term() != null) {
            String ident = ctx.qual_identifer().getText();
            List<TERM> args = new ArrayList<>();
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

    private Pair<String, TYPE> convertSortedVar(ContractLIBParser.Sorted_varContext ctx,
                                                Types<TYPE> context) {
        String name = convertSymbol(ctx.symbol());
        TYPE type = convertSort(ctx.sort(), context);
        return new Pair<>(name, type);
    }

    private Pair<String, Pair<Mode, TYPE>> convertFormal(ContractLIBParser.FormalContext ctx,
                                                         Types<TYPE> context) {
        String name = convertSymbol(ctx.symbol());
        Mode mode = convertMode(ctx.argument_mode());
        TYPE type = convertSort(ctx.sort(), context);
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
