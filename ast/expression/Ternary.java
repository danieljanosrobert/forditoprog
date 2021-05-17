package ast.expression;

import ast.Expression;
import ast.Script;
import ast.Value;

public class Ternary extends Expression {
    private Expression condition;
    private Expression trueExpression;
    private Expression falseExpression;

    public Ternary(Expression condition, Expression trueExpression, Expression falseExpression) {
        this.condition = condition;
        this.trueExpression = trueExpression;
        this.falseExpression = falseExpression;
    }

    @Override
    public Value evaluate(Script script) {
        return Value.getBooleanOfValue(condition.evaluate(script)) == 1
                ? trueExpression.evaluate(script)
                : falseExpression.evaluate(script);
    }

    @Override
    public String toString() {
        return condition.toString() + " ? " + trueExpression.toString() + " : " + falseExpression.toString();
    }
}
