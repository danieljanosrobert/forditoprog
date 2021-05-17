package ast.expression;

import ast.Expression;
import ast.Script;
import ast.Value;

import java.util.Arrays;

public class Unary extends Expression {
    private UnaryOperator op;
    private Expression expression;

    public Unary(String op, Expression expression) {
        this.op = UnaryOperator.getUnaryOperator(op);
        this.expression = expression;
    }

    public Value evaluate(Script script) {
        Value value = this.expression.evaluate(script);
        return switch (this.op) {
            case PLUS -> value;
            case NEG -> value.isFraction()
                    ? new Value(-value.getDoubleValue())
                    : new Value((-value.getIntegerValue()));
            case NOT -> new Value(Value.getNegatedBooleanOfValue(value));
            case ABS -> value.isFraction()
                    ? new Value(Math.abs(value.getDoubleValue()))
                    : new Value(Math.abs(value.getIntegerValue()));
        };
    }

    @Override
    public String toString() {
        if (expression == null) System.out.println("expression is <NULL>");
        return op.getOp()
                + (op == UnaryOperator.ABS ? "(" : "")
                + expression.toString()
                + (op == UnaryOperator.ABS ? ")" : "");
    }

    private enum UnaryOperator {
        PLUS("+"), NEG("-"), ABS("ABS"), NOT("!");
        private String op;

        UnaryOperator(String op) {
            this.op = op;
        }

        public static UnaryOperator getUnaryOperator(String op) {
            return Arrays.stream(UnaryOperator.values())
                    .filter(_op -> _op.getOp().equals(op))
                    .findAny().orElse(null);
        }

        public String getOp() {
            return op;
        }
    }
}
