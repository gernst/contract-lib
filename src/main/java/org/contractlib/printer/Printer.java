package org.contractlib.printer;

import java.io.PrintStream;

public interface Printer<A> {
    void print(A a, PrintStream out);
}