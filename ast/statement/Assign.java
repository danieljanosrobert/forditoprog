package ast.statement;

import ast.Expression;
import ast.Script;
import ast.Statement;

public class Assign extends Statement {
    private String variableName;
    private Expression expression;
    private boolean isDouble;

    public Assign(Script script, String variableName, Expression expression) {
        super(script);
        this.variableName = variableName;
        this.expression = expression;
    }

    public Assign(Script script, String variableName, Expression expression, boolean isDouble) {
        super(script);
        this.variableName = variableName;
        this.expression = expression;
        this.isDouble = isDouble;
    }

    @Override
    public void execute() {
        script.setVariable(variableName, expression.evaluate(script));
    }

    @Override
    public String toString() {
        return variableName + " = " + expression.toString() + ";\n";
    }

    public String toForString() {
        return variableName + " = " + expression.toString();
    }
}
