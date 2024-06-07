package org.key_project.contractlib;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.IntegerLiteralExpr;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.jml.clauses.*;
import com.github.javaparser.ast.stmt.Behavior;
import com.github.javaparser.ast.type.*;
import org.contractlib.ast.Command;
import org.contractlib.ast.Term;
import org.contractlib.factory.Mode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ContractLib2JMLAST {
    List<JmlContract> jmlContracts = new ArrayList<>();

    Map<String, Type> typeMap = Map.of(
        "Int", PrimitiveType.intType(),
        "Bool", PrimitiveType.booleanType());

    void convert(List<Command> commands) {
        for (Command c : commands) {
            switch (c) {
            case Command.DefineContract contr: {
                List<Parameter> args = new ArrayList<>();
                for (var a : contr.formal().stream().filter(x -> x.second().first() != Mode.OUT).toList()) {
                    var argName = a.first();
                    Type argType = typeMap.getOrDefault(argName, new ClassOrInterfaceType(argName));   // TODO: default?
                    Parameter arg = new Parameter(argType, argName);
                    args.add(arg);
                }
                List<JmlClause> clauses = new ArrayList<>();
                for (var contract : contr.contracts()) {

                    // TODO
                    //JmlClause clause = new JmlSimpleExprClause();
                }
                NodeList<JmlClause> nodeList = new NodeList<>(clauses);
                var jmlContract = new JmlContract(ContractType.METHOD, Behavior.NORMAL,
                    new SimpleName(""), NodeList.nodeList(), nodeList, NodeList.nodeList());
                jmlContracts.add(jmlContract);
                break;
            }
            case Command.DeclareAbstractions abs: {

            }
            break;
            default:
            }
        }
    }

    Expression convertTerm(Term term) {
        /*
        switch (term) {
        case Term.Literal lit:
            return new IntegerLiteralExpr(lit.value().toString());      // TODO: literals of other types
        case Term.Variable:
            return new Var
        case Term.Old:
            new JmlOldClause()
        case Term.Application:
        case Term.Binder:
        }*/
        return null;
    }
}
