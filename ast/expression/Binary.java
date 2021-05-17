package ast.expression;

import ast.Expression;
import ast.Script;
import ast.Value;

import java.util.Arrays;

public class Binary extends Expression {

    public static final double EPS = 1e-10;

    private BinaryOperator op;
    private Expression left;
    private Expression right;

    public Binary(String op, Expression left, Expression right) {
        this.op = BinaryOperator.getBinaryOperator(op);
        this.left = left;
        this.right = right;
    }

    @Override
    public Value evaluate(Script script) {
        Value lValue = this.left.evaluate(script);
        Value rValue = this.right.evaluate(script);
        double lAbstractValue = lValue.isFraction() ? lValue.getDoubleValue() : lValue.getIntegerValue();
        double rAbstractValue = rValue.isFraction() ? rValue.getDoubleValue() : rValue.getIntegerValue();
        return switch (this.op) {
            case ADD -> lValue.isFraction() || rValue.isFraction()
                    ? new Value(lAbstractValue + rAbstractValue)
                    : new Value((int) (lAbstractValue + rAbstractValue));
            case SUB -> lValue.isFraction() || rValue.isFraction()
                    ? new Value(lAbstractValue - rAbstractValue)
                    : new Value((int) (lAbstractValue - rAbstractValue));
            case MUL -> lValue.isFraction() || rValue.isFraction()
                    ? new Value(lAbstractValue * rAbstractValue)
                    : new Value((int) (lAbstractValue * rAbstractValue));
            case DIV -> lValue.isFraction() || rValue.isFraction()
                    ? new Value(lAbstractValue / rAbstractValue)
                    : new Value((int) (lAbstractValue / rAbstractValue));
            case LT -> new Value(((lAbstractValue < rAbstractValue) && checkEpsilon(lValue, rValue)) ? 1 : 0);
            case GT -> new Value(((lAbstractValue > rAbstractValue) && checkEpsilon(lValue, rValue)) ? 1 : 0);
            case EQ -> new Value((Math.abs(lAbstractValue - rAbstractValue) < EPS) ? 1 : 0);
            case OR -> new Value((Value.getBooleanOfValue(lValue) + Value.getBooleanOfValue(rValue) >= 1) ? 1 : 0);
            case AND -> new Value((Value.getBooleanOfValue(lValue) + Value.getBooleanOfValue(rValue) == 2) ? 1 : 0);
            default -> throw new IllegalArgumentException();
        };
    }

    private boolean checkEpsilon(Value lValue, Value rValue) {
        return Math.abs((lValue.isFraction() ? lValue.getDoubleValue() : lValue.getIntegerValue())
                - (rValue.isFraction() ? rValue.getDoubleValue() : rValue.getIntegerValue())) >= EPS;
    }

    @Override
    public String toString() {
        if (left == null) System.out.println("left side is <NULL>");
        if (right == null) System.out.println("right side is null<NULL>");
        return left.toString() + " " + op.getText() + " " + right.toString();
    }

    private enum BinaryOperator {
        ADD("+"), SUB("-"), MUL("*"), DIV("/"), LT("<"), GT(">"), EQ("=="),
        OR("||"), AND("&&");

        private String text;

        BinaryOperator(String text) {
            this.text = text;
        }

        public static Binary.BinaryOperator getBinaryOperator(String op) {
            return Arrays.stream(Binary.BinaryOperator.values())
                    .filter(_op -> _op.getText().equals(op))
                    .findAny().orElse(null);
        }

        public String getText() {
            return text;
        }
    }
}