package org.contractlib.sexpr;

import org.contractlib.parser.Token;

import java.util.List;

public record SList(List<SExpr> arguments) implements SExpr {

}