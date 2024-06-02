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

Comment
    : Semicolon ~[\r\n]* -> skip
    ;

ParOpen
    : '('
    ;

ParClose
    : ')'
    ;

Semicolon
    : ';'
    ;

String
    : '"' (PrintableCharNoDquote | WhiteSpaceChar)+ '"'
    ;

QuotedSymbol
    : '|' (PrintableCharNoBackslash | WhiteSpaceChar)+ '|'
    ;

// Predefined Symbols

PS_Not
    : 'not'
    ;

PS_Bool
    : 'Bool'
    ;

PS_False
    : 'false'
    ;

PS_True
    : 'true'
    ;

// Command names

CMD_Assert
    : 'assert'
    ;

CMD_DeclareConst
    : 'declare-const'
    ;

CMD_DeclareAbstractions
    : 'declare-abstractions'
    ;

CMD_DeclareDatatype
    : 'declare-datatype'
    ;

CMD_DeclareDatatypes
    : 'declare-datatypes'
    ;

CMD_DeclareFun
    : 'declare-fun'
    ;

CMD_DeclareSort
    : 'declare-sort'
    ;

CMD_DefineContract
    : 'define-contract'
    ;

CMD_DefineFun
    : 'define-fun'
    ;

CMD_DefineFunRec
    : 'define-fun-rec'
    ;

CMD_DefineFunsRec
    : 'define-funs-rec'
    ;

CMD_DefineSort
    : 'define-sort'
    ;

// General reserved words

GRW_Exclamation
    : '!'
    ;

GRW_Underscore
    : '_'
    ;

GRW_As
    : 'as'
    ;

GRW_Binary
    : 'BINARY'
    ;

GRW_Decimal
    : 'DECIMAL'
    ;

GRW_Exists
    : 'exists'
    ;

GRW_Hexadecimal
    : 'HEXADECIMAL'
    ;

GRW_Forall
    : 'forall'
    ;

GRW_Let
    : 'let'
    ;

GRW_Match
    : 'match'
    ;

GRW_Numeral
    : 'NUMERAL'
    ;

GRW_Par
    : 'par'
    ;

GRW_Old
    : 'old'
    ;

GRW_String
    : 'string'
    ;

Numeral
    : '0'
    | [1-9] Digit*
    ;

Binary
    : BinaryDigit+
    ;

HexDecimal
    : '#x' HexDigit HexDigit HexDigit HexDigit
    ;

Decimal
    : Numeral '.' '0'* Numeral
    ;

fragment HexDigit
    : '0' .. '9'
    | 'a' .. 'f'
    | 'A' .. 'F'
    ;

Colon
    : ':'
    ;

fragment Digit
    : [0-9]
    ;

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

fragment BinaryDigit
    : [01]
    ;

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

RS_Model //  for model responses
    : 'model'
    ;

UndefinedSymbol
    : Sym (Digit | Sym)*
    ;

// HandwrittenParser Rules Start

// Starting rule(s)

start_
    : script EOF
    ;

simpleSymbol
    : predefSymbol
    | UndefinedSymbol
    ;

quotedSymbol
    : QuotedSymbol
    ;

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

numeral
    : Numeral
    ;

decimal
    : Decimal
    ;

hexadecimal
    : HexDecimal
    ;

binary
    : Binary
    ;

string
    : String
    ;

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
    | ParOpen s_expr* ParClose
    ;

// Identifiers

index
    : numeral
    | symbol
    ;

identifier
    : symbol
    | ParOpen GRW_Underscore symbol index+ ParClose
    ;

// Attributes

attribute_value
    : spec_constant
    | symbol
    | ParOpen s_expr* ParClose
    ;

attribute
    : keyword
    | keyword attribute_value
    ;

// Sorts

sort
    : identifier
    | ParOpen identifier sort+ ParClose
    ;

// Terms and Formulas

qual_identifer
    : identifier
    | ParOpen GRW_As identifier sort ParClose
    ;

var_binding
    : ParOpen symbol term ParClose
    ;

sorted_var
    : ParOpen symbol sort ParClose
    ;

pattern
    : symbol
    | ParOpen symbol symbol+ ParClose
    ;

match_case
    : ParOpen pattern term ParClose
    ;

term
    : spec_constant
    | qual_identifer
    | ParOpen qual_identifer term+ ParClose
    | ParOpen GRW_Let ParOpen var_binding+ ParClose term ParClose
    | ParOpen GRW_Forall ParOpen sorted_var+ ParClose term ParClose
    | ParOpen GRW_Exists ParOpen sorted_var+ ParClose term ParClose
    | ParOpen GRW_Match term ParOpen match_case+ ParClose ParClose
    | ParOpen GRW_Exclamation term attribute+ ParClose
    | ParOpen GRW_Old term ParClose
    ;

// Scripts

sort_dec
    : ParOpen symbol numeral ParClose
    ;

selector_dec
    : ParOpen symbol sort ParClose
    ;

constructor_dec
    : ParOpen symbol selector_dec* ParClose
    ;

datatype_dec
    : ParOpen constructor_dec+ ParClose
    | ParOpen GRW_Par ParOpen symbol+ ParClose ParOpen constructor_dec+ ParClose ParClose
    ;

function_dec
    : ParOpen symbol ParOpen sorted_var* ParClose sort ParClose
    ;

function_def
    : symbol ParOpen sorted_var* ParClose sort term
    ;

script
    : command*
    ;

cmd_assert
    : CMD_Assert term
    ;

cmd_declareAbstractions
    // cardinalitiees for sort_dec and datatype_dec have to be n+1
    : CMD_DeclareAbstractions ParOpen sort_dec+ ParClose ParOpen datatype_dec+ ParClose
    ;

cmd_declareConst
    : CMD_DeclareConst symbol sort
    ;

cmd_declareDatatype
    : CMD_DeclareDatatype symbol datatype_dec
    ;

cmd_declareDatatypes
    // cardinalitiees for sort_dec and datatype_dec have to be n+1
    : CMD_DeclareDatatypes ParOpen sort_dec+ ParClose ParOpen datatype_dec+ ParClose
    ;

cmd_declareFun
    : CMD_DeclareFun symbol ParOpen sort* ParClose sort
    ;

cmd_declareSort
    : CMD_DeclareSort symbol numeral
    ;

cmd_defineContract
    : CMD_DefineContract symbol ParOpen formal_param+ ParClose ParOpen contract+ ParClose
    ;

cmd_defineFun
    : CMD_DefineFun function_def
    ;

cmd_defineFunRec
    : CMD_DefineFunRec function_def
    ;

cmd_defineFunsRec
    // cardinalitiees for function_dec and term have to be n+1
    : CMD_DefineFunsRec ParOpen function_dec+ ParClose ParOpen term+ ParClose
    ;

cmd_defineSort
    : CMD_DefineSort symbol ParOpen symbol* ParClose sort
    ;

command
    : ParOpen cmd_assert ParClose
    | ParOpen cmd_declareAbstractions ParClose
    | ParOpen cmd_declareConst ParClose
    | ParOpen cmd_declareDatatype ParClose
    | ParOpen cmd_declareDatatypes ParClose
    | ParOpen cmd_declareFun ParClose
    | ParOpen cmd_declareSort ParClose
    | ParOpen cmd_defineContract ParClose
    | ParOpen cmd_defineFun ParClose
    | ParOpen cmd_defineFunRec ParClose
    | ParOpen cmd_defineFunsRec ParClose
    | ParOpen cmd_defineSort ParClose
    ;

formal_param
    : ParOpen symbol ParOpen param_mode sort ParClose ParClose
    ;

contract
    : ParOpen term term ParClose
    ;

param_mode
    : 'in'
    | 'out'
    | 'inout'
    ;

// HandwrittenParser Rules End

WS
    : [ \t\r\n]+ -> skip
    ;
