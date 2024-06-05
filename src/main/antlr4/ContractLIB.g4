/**
 * SMT-LIB (v2.6) grammar
 *
 * Grammar is baesd on the following specification:
 * http://smtlib.cs.uiowa.edu/papers/smt-lib-reference-v2.6-r2017-07-18.pdf
 *
 * The MIT License (MIT)
 *
 * Copyright (c) 2017 Julian Thome <julian.thome.de@gmail.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do
 * so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 **/

// $antlr-format alignTrailingComments true, columnLimit 150, minEmptyLines 1, maxEmptyLinesToKeep 1, reflowComments false, useTab false
// $antlr-format allowShortRulesOnASingleLine false, allowShortBlocksOnASingleLine true, alignSemicolons hanging, alignColons hanging

grammar ContractLIB;

// Lexer Rules Start

Comment : Semicolon ~[\r\n]* -> skip;
LPAR : '(';
RPAR : ')';
Semicolon : ';';
String : '"' (PrintableCharNoDquote | WhiteSpaceChar)+ '"';
QuotedSymbol : '|' (PrintableCharNoBackslash | WhiteSpaceChar)+ '|';

// Predefined Symbols
PS_Not : 'not';
PS_Bool : 'Bool';
PS_False : 'false';
PS_True : 'true';

// Command names
CMD_Assert : 'assert';
CMD_DeclareConst : 'declare-const';
CMD_DeclareAbstraction : 'declare-abstraction';
CMD_DeclareAbstractions : 'declare-abstractions';
CMD_DeclareDatatype : 'declare-datatype';
CMD_DeclareDatatypes : 'declare-datatypes';
CMD_DeclareFun : 'declare-fun';
CMD_DeclareSort : 'declare-sort';
CMD_DefineContract : 'define-contract';
CMD_DefineFun : 'define-fun';
CMD_DefineFunRec : 'define-fun-rec';
CMD_DefineFunsRec : 'define-funs-rec';
CMD_DefineSort : 'define-sort';

// General reserved words
GRW_Exclamation : '!';
GRW_Underscore : '_';
GRW_As : 'as';
GRW_Binary : 'BINARY';
GRW_Decimal : 'DECIMAL';
GRW_Exists : 'exists';
GRW_Hexadecimal : 'HEXADECIMAL';
GRW_Forall : 'forall';
GRW_Let : 'let';
GRW_Match : 'match';
GRW_Numeral : 'NUMERAL';
GRW_Par : 'par';
GRW_Old : 'old';
GRW_String : 'string';
Numeral : '0'
    | [1-9] Digit*
    ;
Binary : BinaryDigit+;
HexDecimal : '#x' HexDigit HexDigit HexDigit HexDigit;
Decimal : Numeral '.' '0'* Numeral;

fragment HexDigit
    : '0' .. '9'
    | 'a' .. 'f'
    | 'A' .. 'F'
    ;

Colon : ':';

fragment Digit : [0-9];

fragment Sym
    : 'a' ..'z'
    | 'A' .. 'Z'
    | '+'
    | '='
    | '/'
    | '*'
    | '%'
    | '?'
    | '!'
    | '$'
    | '-'
    | '_'
    | '~'
    | '&'
    | '^'
    | '<'
    | '>'
    | '@'
    | '.'
    ;

fragment BinaryDigit : [01];

fragment PrintableChar
    : '\u0020' .. '\u007E'
    | '\u0080' .. '\uffff'
    | EscapedSpace
    ;

fragment PrintableCharNoDquote
    : '\u0020' .. '\u0021'
    | '\u0023' .. '\u007E'
    | '\u0080' .. '\uffff'
    | EscapedSpace
    ;

fragment PrintableCharNoBackslash
    : '\u0020' .. '\u005B'
    | '\u005D' .. '\u007B'
    | '\u007D' .. '\u007E'
    | '\u0080' .. '\uffff'
    | EscapedSpace
    ;

fragment EscapedSpace
    : '""'
    ;

fragment WhiteSpaceChar
    : '\u0009'
    | '\u000A'
    | '\u000D'
    | '\u0020'
    ;

// Lexer Rules End

// Predefined Keywords

UndefinedSymbol : Sym (Digit | Sym)*;

// Parser Rules Start

// Starting rule(s)

start_ : script EOF;

simpleSymbol
    : predefSymbol
    | UndefinedSymbol
    ;

quotedSymbol : QuotedSymbol;

predefSymbol
    : PS_Not
    | PS_Bool
    | PS_False
    | PS_True
    ;

symbol
    : simpleSymbol
    | quotedSymbol
    ;

numeral : Numeral;
decimal : Decimal;
hexadecimal : HexDecimal;
binary : Binary;
string : String;

keyword
    : //predefKeyword
    // |
    Colon simpleSymbol
    ;

// S-expression

spec_constant
    : numeral
    | decimal
    | hexadecimal
    | binary
    | string
    ;

s_expr
    : spec_constant
    | symbol
    | keyword
    | LPAR s_expr* RPAR
    ;

// Identifiers

index
    : numeral
    | symbol
    ;

identifier
    : symbol
    | LPAR GRW_Underscore symbol index+ RPAR
    ;

// Attributes

attribute_value
    : spec_constant
    | symbol
    | LPAR s_expr* RPAR
    ;

attribute
    : keyword
    | keyword attribute_value
    ;

// Sorts

sort
    : identifier
    | LPAR identifier sort+ RPAR
    ;

// Terms and Formulas

qual_identifer
    : identifier
    | LPAR GRW_As identifier sort RPAR
    ;

var_binding : LPAR symbol term RPAR;

sorted_var : LPAR symbol sort RPAR;

pattern
    : symbol
    | LPAR symbol symbol+ RPAR
    ;

match_case : LPAR pattern term RPAR;

term
    : spec_constant
    | qual_identifer
    | LPAR qual_identifer term+ RPAR
    | LPAR GRW_Let LPAR var_binding+ RPAR term RPAR
    | LPAR GRW_Forall LPAR sorted_var+ RPAR term RPAR
    | LPAR GRW_Exists LPAR sorted_var+ RPAR term RPAR
    | LPAR GRW_Match term LPAR match_case+ RPAR RPAR
    | LPAR GRW_Exclamation term attribute+ RPAR
    | LPAR GRW_Old term RPAR
    ;

// Scripts

sort_dec : LPAR symbol numeral RPAR;

selector_dec : LPAR symbol sort RPAR;

constructor_dec : LPAR symbol selector_dec* RPAR;

datatype_dec
    : LPAR constructor_dec+ RPAR
    | LPAR GRW_Par LPAR symbol+ RPAR LPAR constructor_dec+ RPAR RPAR
    ;

function_dec : LPAR symbol LPAR sorted_var* RPAR sort RPAR;

function_def : symbol LPAR sorted_var* RPAR sort term;

script : command*;

cmd_assert : CMD_Assert term;

// cardinalitiees for sort_dec and datatype_dec have to be n+1
cmd_declareAbstraction : CMD_DeclareAbstraction symbol datatype_dec+;

// cardinalitiees for sort_dec and datatype_dec have to be n+1
cmd_declareAbstractions : CMD_DeclareAbstractions LPAR sort_dec+ RPAR LPAR datatype_dec+ RPAR;

cmd_declareConst : CMD_DeclareConst symbol sort;

cmd_declareDatatype : CMD_DeclareDatatype symbol datatype_dec;

// cardinalitiees for sort_dec and datatype_dec have to be n+1
cmd_declareDatatypes : CMD_DeclareDatatypes LPAR sort_dec+ RPAR LPAR datatype_dec+ RPAR;

cmd_declareFun : CMD_DeclareFun symbol LPAR sort* RPAR sort;

cmd_declareSort : CMD_DeclareSort symbol numeral;

cmd_defineContract : CMD_DefineContract symbol LPAR formal+ RPAR LPAR contract+ RPAR;

cmd_defineFun : CMD_DefineFun function_def;

cmd_defineFunRec : CMD_DefineFunRec function_def;

// cardinalitiees for function_dec and term have to be n+1
cmd_defineFunsRec : CMD_DefineFunsRec LPAR function_dec+ RPAR LPAR term+ RPAR;

cmd_defineSort : CMD_DefineSort symbol LPAR symbol* RPAR sort;

command
    : LPAR cmd_assert RPAR
    | LPAR cmd_declareAbstraction RPAR
    | LPAR cmd_declareAbstractions RPAR
    | LPAR cmd_declareConst RPAR
    | LPAR cmd_declareDatatype RPAR
    | LPAR cmd_declareDatatypes RPAR
    | LPAR cmd_declareFun RPAR
    | LPAR cmd_declareSort RPAR
    | LPAR cmd_defineContract RPAR
    | LPAR cmd_defineFun RPAR
    | LPAR cmd_defineFunRec RPAR
    | LPAR cmd_defineFunsRec RPAR
    | LPAR cmd_defineSort RPAR
    ;

formal : LPAR symbol LPAR argument_mode sort RPAR RPAR;

contract : LPAR term term RPAR;

argument_mode
    : 'in'
    | 'out'
    | 'inout'
    ;

// Parser Rules End

WS : [ \t\r\n]+ -> skip;
