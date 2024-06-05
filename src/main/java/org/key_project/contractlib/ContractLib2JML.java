package org.key_project.contractlib;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.misc.Interval;
import org.antlr.v4.runtime.tree.RuleNode;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.contractlib.antlr4parser.ContractLIBBaseVisitor;
import org.contractlib.antlr4parser.ContractLIBLexer;
import org.contractlib.antlr4parser.ContractLIBParser;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ContractLib2JML extends ContractLIBBaseVisitor<Void> {
    final CharStream input;
    String interfaceName = "";
    String template = """
        interface %s {
        %s
        %s
        }
        """;

    List<String> contracts = new ArrayList<>();
    List<String> ghostFields = new ArrayList<>();

    Map<String, String> sorts = Map.of(
        "Int", "int",
        "Bool", "boolean");

    public ContractLib2JML(CharStream input) {
        this.input = input;
    }

    public static void main(String[] args) throws IOException {
        Path path = Paths.get("src/test/contractlib/examples/examples.smt2");
        System.out.println("Parsing:  " + path);

        CharStream charStream = CharStreams.fromPath(path);
        ContractLIBLexer lexer = new ContractLIBLexer(charStream);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        ContractLIBParser parser = new ContractLIBParser(tokens);

        ContractLIBParser.ScriptContext ctx = parser.script();
        ContractLib2JML converter = new ContractLib2JML(charStream);
        converter.visit(ctx);
        String test = converter.template;

        test = String.format(test,
            converter.interfaceName,
            String.join(System.lineSeparator(), converter.ghostFields),
            String.join(System.lineSeparator(), converter.contracts));

        System.out.println(test);
    }

    @Override
    public Void visitCmd_defineContract(ContractLIBParser.Cmd_defineContractContext ctx) {

        if (interfaceName.isBlank())
            throw new RuntimeException("Please declare an abstraction first and specify the name of the datatype!");

        String methodName = ctx.symbol().getText();
        System.out.println("Found declaration of " + methodName);

        // remove prefix from method name
        if (!methodName.startsWith(interfaceName)) {
            throw new RuntimeException("At the moment, only a single abstraction with a set of contracts is allowed!");
        }
        methodName = methodName.substring(interfaceName.length() + 1);

        String returnSort = "";

        String params = ""; //ctx.formal_param().stream().map(RuleContext::getText).collect(Collectors.joining());
        for (var param : ctx.formal()) {
            var paramMode = param.argument_mode().getText();
            var paramName = param.symbol().getText();
            var paramSort = convertSort(param.sort().getText());
            if (paramMode.equals("in")) {
                params += paramSort + " " + paramName + ", ";
            } else {    // "in" or "inout"
                if (returnSort.isEmpty()) {
                    // TODO: how to deal with multiple out/inout parameters?
                    returnSort = paramSort;
                }
                if (paramMode.equals("inout")) {
                    params += paramSort + " " + paramName + ", ";

                }
            }

        }
        if (!params.isEmpty()) {
            // remove trailing ", "
            params = params.substring(0, params.length() - 2);
        }

        //if (ctx.formal_param().size() != 1)
        //    throw new RuntimeException("At the moment, \"define-contract\" only supports a single contract!");

        if (ctx.contract(0).term().size() != 2)
            throw new RuntimeException("Context must have pre and post (and nothing else)!");


        String pre = ctx.contract(0).term(0).getText();
        String post = ctx.contract(0).term(1).getText();

        String contract = """
            /*@ normal_behavior
              @  requires %s;
              @  ensures %s;
              @*/
            %s %s(%s);""";

        contract = String.format(contract, pre, post, returnSort, methodName, params).indent(4);
        contracts.add(contract);
        return null;
    }

    private String convertSort(String sort) {
        return sorts.getOrDefault(sort, sort);
    }

    private String convertExpression(String term) {
        // TODO: convert expression
        return term;
    }

    @Override
    public Void visitTerminal(TerminalNode node) {
        //int a = ctx.start.getStartIndex();
        //int b = ctx.stop.getStopIndex();
        //Interval interval = new Interval(a,b);
        Interval interval = node.getSourceInterval();
        input.getText(interval);
        //return node.getText();
        return null;
    }

    @Override
    public Void visitChildren(RuleNode node) {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < node.getChildCount(); i++) {
            s.append(visit(node.getChild(i)));
        }
        //return s.toString();
        return null;
    }
}
