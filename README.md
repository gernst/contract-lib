# contract-lib
Contract-LIB proposal and tool-chain

This repository provides a tool-chain for Contract-LIB. It contains:
* An ANTLR4 grammar based on the SMT-LIB grammar (version 2.6), which supports all commands `declare-*` and `define-*`
  as well as `assert` from SMT-LIB, and additionally the new commands `declare-abstractions` and `define-contract`.
* Interfaces of factories to create the abstract syntax tree (AST) nodes for Contract-LIB (found in the package
  `org.contractlib.factory`).
* A parser `org.contractlib.parser.ContractLibANTLRParser` based on the ANTLR grammar which has to be instantiated with the concrete factories and which produces the
  AST.
* Basic default implementations of these interfaces in the package `org.contractlib.ast`, which support tasks such as type resolution.

The idea is that for tool developers who want to import Contract-LIB into their tools, the following steps have to be
done: First, they need to implement the factory interfaces. This gives the freedom to only implement what is of interest
for the tool. For example, one tool might only need the abstraction and datatype declarations, but not the assertions.
Second, code needs to be written to traverse the \ContractLIB AST and convert it into the tool specific data structures.
With these preparation steps, the provided parser can be used to convert from Contract-LIB text into the AST and then
further into the tool specific structures.
