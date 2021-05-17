package ast.expression;

import ast.Expression;
import ast.Script;
import ast.Value;

public class Variable extends Expression {
    private String variableName;

    public Variable(String variableName) {
        this.variableName = variableName;
    }

    public String getVariableName() {
        return variableName;
    }

    @Override
    public Value evaluate(Script script) {
        return script.getVariable(this.variableName);
    }

    @Override
    public String toString() {
        return variableName;
    }
}
