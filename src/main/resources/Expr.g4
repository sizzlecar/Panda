grammar Expr;
programmer:	(expr NEWLINE)* ;
expr:	expr (MULTI|DIV) expr
    |	expr (ADD|SUB) expr
    |	INT
    |	'(' expr ')'
    ;
NEWLINE : [\r\n]+ ;
INT     : [0-9]+ ;
ADD     : '+' ;
SUB     : '-' ;
MULTI   : '*' ;
DIV     : '/' ;
