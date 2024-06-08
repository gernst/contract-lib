package org.contractlib.printer;

import org.contractlib.sexpr.*;

import java.util.List;
import java.util.ArrayList;
import java.io.PrintStream;

public class SExprPrinter implements Printer<SExpr> {
	public static final int SHIFT = 2;

	record Line(int indent, String contents) {
		public int length() {
			return indent + contents.length();
		}

		public Line shift() {
			return new Line(indent + SHIFT, contents);
		}

		public Line prepend(String that) {
			return new Line(indent, that + contents);
		}

		public Line shiftAppend(String that) {
			return new Line(indent + SHIFT, contents + that);
		}
	}

	public void print(SExpr expr, PrintStream out) {
		List<Line> lines = format(expr);
		for (Line line : lines) {
			for (int i = 0; i < line.indent(); i++)
				out.append(" ");
			out.append(line.contents());
			out.append("\n");
		}
	}

	List<Line> format(SExpr expr) {
        switch (expr) {
        case Atom atom: {
            Line line = new Line(0, atom.toString());
            return List.of(line);
        }

        case SList list: {
            List<Line> lines = new ArrayList<>();
            for (SExpr argument : list.arguments()) {
                lines.addAll(format(argument));
            }

            int sum = 0;
            for (Line line : lines)
                sum += line.length();

            int max = 0;
            for (Line line : lines)
                if (line.length() > max)
                    max = line.length();

            if (lines.size() >= 2 && (max > 20 || sum >= 80)) {
                int n = lines.size();

                for (int i = 0; i < n; i++) {
                    Line line = lines.get(i);

                    if (i == 0) {
                        lines.set(i, line.prepend("("));
                    } else if (i == n - 1) {
                        lines.set(i, line.shiftAppend(")"));
                    } else {
                        lines.set(i, line.shift());
                    }
                }

                return lines;
            } else {
                StringBuilder result = new StringBuilder();
                boolean first = true;

                result.append("(");

                for (Line line : lines) {
                    if (first) {
                        first = false;
                    } else {
                        result.append(" ");
                    }

                    result.append(line.contents());
                }

                result.append(")");

                Line line = new Line(0, result.toString());
                return List.of(line);
            }
        }

        default:
            throw new RuntimeException("unknown sexpr: " + expr);
        }
    }
}
