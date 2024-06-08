package org.contractlib;

import java.util.List;

import java.io.Reader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.contractlib.sexpr.SExpr;
import org.contractlib.ast.*;
import org.contractlib.parser.Parser;
import org.contractlib.printer.Printer;
import org.contractlib.printer.SExprPrinter;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ParserTests {
	@Test
	void builtin() throws IOException {
		canParseAll(new File("src/test/contractlib/builtin"));
	}

	@Test
	void examples() throws IOException {
		canParseAll(new File("src/test/contractlib/examples"));
	}

	@Test
	void regression() throws IOException {
		canParseAll(new File("src/test/contractlib/regression"));
	}

	void canParseAll(File path) throws IOException {
		File[] files = path.listFiles();

		for (File file : files) {
			if (file.getName().endsWith(".smt2")) {
				canScan(file);
				canParse(file);
			}
		}
	}

	void canScan(File file) throws IOException {
		System.out.println("Scanning: " + file.getPath());
		Reader reader = new FileReader(file);
		Parser parser = new Parser(reader);

		List<SExpr> result = parser.sexprs();

		Printer<SExpr> printer = new SExprPrinter();
		for (SExpr expr : result) {
			printer.print(expr, System.out);
			System.out.println();
		}
	}

	void canParse(File file) throws IOException {
		System.out.println("Parsing:  " + file.getPath());
		Reader reader = new FileReader(file);
		Parser parser = new Parser(reader);
		Factory ast = new Factory();

		List<Command> result = parser.script(ast);
	}
}