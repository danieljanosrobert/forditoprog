lexer grammar ScriptLexer;

options {
    language = Java;
}

LF          : '\n';
WS          : [ \t\r]+ -> skip;
QUE         : '?';
COL         : ':';
LPAR        : '(';
RPAR        : ')';
LBRAC       : '{';
RBRAC       : '}';
COMMA       : ',';
SEM_COL     : ';';

OP_ADDSUB   : '+' | '-';
OP_MULDIV   : '*' | '/';
OP_ABS      : 'ABS';
OP_ASSIGN   : '=';
OP_EQ       : '==';
OP_REL      : '<' | '>';
OP_OR       : '||';
OP_AND      : '&&';
OP_NOT      : '!';
OP_PRINT    : 'print';
OP_TIME     : 'TIME';

KW_INT      : 'int';
KW_DOUBLE   : 'double';
KW_FOR      : 'for';
KW_WHILE    : 'while';
KW_IF       : 'if';
KW_ELSE     : 'else';
KW_SWITCH   : 'switch';
KW_CASE     : 'case';
KW_DEFAULT  : 'default';
KW_BREAK    : 'break';
KW_CONT     : 'continue';

NUMBER      : [0-9]+('.'[0-9]+)?;
VARIABLE    : [A-Za-z][A-Za-z_0-9]*;
