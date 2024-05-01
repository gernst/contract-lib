package org.contractlib;

import java.util.List;

import java.io.Reader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.contractlib.ast.*;
import org.contractlib.parser.Parser;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ParserTests {
	@Test
	void canScanAll() throws IOException {
		File path = new File("src/test/examples");
		File[] files = path.listFiles();

		for (File file : files) {
			if (file.getName().endsWith(".smt2")) {
				// canScan(file);
				canParse(file);
			}
		}
	}

	void canScan(File file) throws IOException {
		System.out.println("Scanning: " + file.getAbsolutePath());
		Reader reader = new FileReader(file);
		Parser parser = new Parser(reader);
		parser.sexprs();
	}

	void canParse(File file) throws IOException {
		System.out.println("Parsing: " + file.getAbsolutePath());
		Reader reader = new FileReader(file);
		Parser parser = new Parser(reader);
		Factory ast = new Factory();

		List<Command> result = parser.script(ast);
	}
}