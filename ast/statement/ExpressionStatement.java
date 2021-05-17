package ast.statement;

import ast.Expression;
import ast.Script;
import ast.Statement;

public class ExpressionStatement extends Statement {
    private Expression expression;

    public ExpressionStatement(Script script, Expression expression) {
        super(script);
        this.expression = expression;
    }

    @Override
    public void execute() {
        expression.evaluate(script);
    }

    @Override
    public String toString() {
        return expression.toString() + ";\n";
    }
}
