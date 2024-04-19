package org.contractlib.parser;

%%

%public
%abstract
%class Scanner
%function shift
%type Token

%eofval{
    return eof();
%eofval}

%line
%column

%{
    final String yytext(int start, int end) {
        int length = zzMarkedPos-zzStartRead;
        return new String( zzBuffer, zzStartRead + start, length - start + end );
    }

    int line()   { return yyline; }
    int column() { return yycolumn; }

    abstract Token eof();

    abstract Token lparen();
    abstract Token rparen();

    abstract Token identifier(String text);
    abstract Token keyword(String text);

    abstract Token string(String text);
    abstract Token real(String text);
    abstract Token integer(String text);
    abstract Token hexadecimal(String text);
    abstract Token binary(String text);

    abstract Token unexpected(String text);
%}

nl = \r|\n|\r\n
ws = {nl} | [ \t\f]

// printable = [\u0020-\u007E] | [\u0080-\uFFFF]
digit   = [0-9]
letter  = [a-zA-Z]
extra   = [~!@$%&*_+=<>.?/] | "-" | "^"

num     = [1-9]{digit}* | 0
dec     = {num} "\." 0* {num}
hex     = "0x" [0-9a-fA-F]+
bin     = "#b" [01]+

symbol0 = {letter} | {extra}
symbol1 = {letter} | {extra} | {digit}
symbol  = {symbol0} {symbol1}*

quoted  = \| ~ \|

kw      = ":" {symbol}

%%

<YYINITIAL> {

{ws}+ {}
";" .* {nl} {}

"("         { return lparen();   }
")"         { return rparen();   }

\" ~ \"
            { return string(yytext(+1,-1)); }

{dec}       { return real(yytext()); }
{num}       { return integer(yytext()); }
{hex}       { return hexadecimal(yytext(+2,0)); }
{bin}       { return binary(yytext(+2,0)); }

{symbol}    { return identifier(yytext()); }
{quoted}    { return identifier(yytext(+1,-1)); }
{kw}        { return keyword(yytext(+1,0));  }

[^]         { unexpected(yytext()); }

}
