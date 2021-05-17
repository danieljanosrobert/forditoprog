package ast.expression;

import ast.Expression;
import ast.Script;
import ast.Value;

public class Parens extends Expression {
    private final Expression expression;

    public Parens(Expression expression) {
        this.expression = expression;
    }

    @Override
    public Value evaluate(Script script) {
        return expression.evaluate(script);
    }

    @Override
    public String toString() {
        return "(" + expression.toString() + ")";
    }
}
