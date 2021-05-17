package ast.expression;

import ast.Expression;
import ast.Script;
import ast.Value;

import java.time.Instant;

public class Time extends Expression {

    public Time() {
    }

    @Override
    public Value evaluate(Script script) {
        return new Value((int) (Instant.now().getEpochSecond()));
    }

    @Override
    public String toString() {
        return "TIME";
    }
}
