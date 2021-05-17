package ast.expression;

import ast.Expression;
import ast.Script;
import ast.Value;

public class Const extends Expression {
    private String literal;
    private Value value;

    public Const(String literal) {
        this.literal = literal;
        if (literal.contains(".")) {
            this.value = new Value(Double.parseDouble(literal));
        } else {
            this.value = new Value(Integer.parseInt(literal));
        }
    }

    @Override
    public Value evaluate(Script script) {
        return this.value;
    }

    @Override
    public String toString() {
        return this.literal;
    }
}
