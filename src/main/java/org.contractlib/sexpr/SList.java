package org.contractlib.sexpr;

import org.contractlib.parser.Token;

import java.util.List;

public record SList(List<SExpr> arguments) implements SExpr {
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("(");

		boolean first = true;
		for (SExpr argument : arguments) {
			if (first) {
				first = false;
			} else {
				builder.append(" ");
			}

			builder.append(argument);
		}

		builder.append(")");
		
        return builder.toString();
	}
}