parser grammar ScriptParser;

options {
    language = Java;
    tokenVocab = ScriptLexer;
}

@header {
    import java.util.ArrayList;
    import java.util.List;
}

@members {
    public static void main(String[] args) throws Exception {
        ScriptLexer lex = new ScriptLexer(CharStreams.fromFileName(args[0]));
        CommonTokenStream tokens = new CommonTokenStream(lex);
        ScriptParser parser = new ScriptParser(tokens);
        parser.start(args.length > 1 && "--generate".equals(args[1]));
    }
}

start [boolean genSrc ]
    @init {
        ast.Script script = new ast.Script();
    }
    @after { 
        if (genSrc) {
            System.out.println(script);
        } else {
            script.execute();
        }
    }
    : sequence[script] { script.addStatements($sequence.node); }
    ;

sequence [ast.Script script] returns [ ast.statement.Sequence node ]
    : { $node = new ast.statement.Sequence(script); } (LF* statement[script] LF*
        { $node.addStatement($statement.node); })+
    ;


statement [ast.Script script] returns [ ast.Statement node ]
    : assign[script] SEM_COL { $node = $assign.node; }
    | expression SEM_COL { $node = new ast.statement.ExpressionStatement($script, $expression.node); }
    | { ast.statement.Sequence false_case = null; }
        KW_IF LPAR logical_expression RPAR LF* LBRAC LF* trueS=sequence[script] LF* RBRAC
        (LF* KW_ELSE LF* LBRAC LF* falseS=sequence[script] LF* RBRAC { false_case = $falseS.node; })?
            { $node = new ast.statement.conditional.If($script, $logical_expression.node, $trueS.node, false_case); }
    | KW_WHILE LPAR logical_expression RPAR LF* LBRAC LF* sequence[script] LF* RBRAC
        { $node = new ast.statement.loop.While($script, $logical_expression.node, $sequence.node); }
    | KW_FOR LPAR var=assign[script] SEM_COL logical_expression SEM_COL up=assign[script] RPAR LF* LBRAC LF*
        sequence[script] LF* RBRAC{ $node = new ast.statement.loop.For($script, $var.node, $logical_expression.node, $up.node, $sequence.node); }
    | KW_FOR LPAR variable SEM_COL logical_expression SEM_COL assign[script] RPAR LF* LBRAC LF* sequence[script] LF* RBRAC
        { $node = new ast.statement.loop.For($script, $variable.node, $logical_expression.node, $assign.node, $sequence.node); }
    | {
        List<ast.Statement> caseList = new ArrayList<>();
        ast.Statement defaultSeq = null;
      }
      KW_SWITCH LPAR switch_expression=expression RPAR LF* LBRAC LF*
        (KW_CASE case_expression=expression COL LF* case_sequence=sequence[script] LF* {
            caseList.add(new ast.statement.conditional.Case($script,  $switch_expression.node, $case_expression.node, $case_sequence.node));
        })*
        (KW_DEFAULT COL LF* default_sequence=sequence[script] LF* {defaultSeq = $default_sequence.node; })? RBRAC
            { $node = new ast.statement.conditional.Switch($script, $switch_expression.node, caseList, defaultSeq); }
    | declare[script] SEM_COL { $node = $declare.node; }
    | KW_BREAK SEM_COL{ $node = new ast.statement.Break($script);}
    | KW_CONT SEM_COL{ $node = new ast.statement.Continue($script); }
    | OP_PRINT LPAR expression RPAR SEM_COL { $node = new ast.statement.Print($script, $expression.node); }
    | OP_PRINT LPAR RPAR SEM_COL {$node = new ast.statement.Print($script); }
    ;

declare [ast.Script script] returns [ ast.Statement node ]
    : type=(KW_INT|KW_DOUBLE) first_var=declare_variable[$type.text, script] { $node = $first_var.node; }
    ;

declare_variable [ String type, ast.Script script ] returns [ ast.Statement node ]
    : VARIABLE { $node = new ast.statement.Declare($script, $type, $VARIABLE.text); }
    | VARIABLE OP_ASSIGN expression
        { $node = new ast.statement.Declare($script, $type, $VARIABLE.text, $expression.node); }
    ;

assign [ast.Script script] returns [ ast.Statement node ]
    : VARIABLE OP_ASSIGN expression { $node = new ast.statement.Assign($script, $VARIABLE.text, $expression.node); }
    ;

expression returns [ ast.Expression node ]
    : logical_expression { $node = $logical_expression.node; }
    | num_expression { $node = $num_expression.node; }
    | logical_expression LF* QUE LF* trueE=expression LF* COL LF* falseE=expression
        {$node = new ast.expression.Ternary($logical_expression.node, $trueE.node, $falseE.node); }
    ;

logical_expression returns [ ast.Expression node ]
    : first_op=logical_tag {$node = $first_op.node; } (OP_OR next_op=logical_tag {
        $node = new ast.expression.Binary($OP_OR.text, $node, $next_op.node);
    })*
    ;

logical_tag returns [ ast.Expression node ]
    : first_op=logical_fct {$node = $first_op.node; } (OP_AND next_op=logical_fct {
        $node = new ast.expression.Binary($OP_AND.text, $node, $next_op.node);
    })*
    ;

logical_fct returns [ ast.Expression node ]
    : left=num_expression op=(OP_EQ|OP_REL) right=num_expression
        { $node = new ast.expression.Binary($op.text, $left.node, $right.node); }
    | OP_NOT logical_fct { $node = new ast.expression.Unary($OP_NOT.text, $logical_fct.node); }
    | LPAR logical_fct RPAR { $node = new ast.expression.Parens($logical_fct.node); }
    | variable { $node = $variable.node; }
    | num_expression { $node = $num_expression.node; }
    ;

num_expression returns [ ast.Expression node ]
    : first_op=addop { $node = $first_op.node; } (OP_ADDSUB next_op=addop {
        $node = new ast.expression.Binary($OP_ADDSUB.text, $node, $next_op.node);
    })*
    ;

addop returns [ ast.Expression node ]
    : first_op=num_mul { $node = $first_op.node; } (OP_MULDIV next_op=addop {
        $node = new ast.expression.Binary($OP_MULDIV.text, $node, $next_op.node);
    })?
    ;

num_mul returns [ ast.Expression node ]
    : NUMBER { $node = new ast.expression.Const($NUMBER.text); }
    | LPAR num_expression RPAR { $node = new ast.expression.Parens($num_expression.node); }
    | OP_ADDSUB num_mul { $node = new ast.expression.Unary($OP_ADDSUB.text, $num_mul.node); }
    | OP_ABS LPAR num_mul RPAR { $node = new ast.expression.Unary($OP_ABS.text, $num_mul.node); }
    | variable { $node = $variable.node; }
    | OP_TIME { $node = new ast.expression.Time(); }
    ;

variable returns [ ast.Expression node ]
    : VARIABLE { $node = new ast.expression.Variable($VARIABLE.text); }
    ;
