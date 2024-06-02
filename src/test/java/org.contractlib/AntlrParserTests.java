package org.contractlib;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.contractlib.antlr4parser.ContractLIBLexer;
import org.contractlib.antlr4parser.ContractLIBParser;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

class AntlrParserTests {
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
                canParse(file);
            }
        }
    }

    void canParse(File file) throws IOException {
        System.out.println("Parsing:  " + file.getPath());

        CharStream charStream = CharStreams.fromPath(file.toPath());
        ContractLIBLexer lexer = new ContractLIBLexer(charStream);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        ContractLIBParser parser = new ContractLIBParser(tokens);

        ContractLIBParser.ScriptContext s = parser.script();
    }
}
