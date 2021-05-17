package ast.statement;

import ast.Expression;
import ast.Script;
import ast.Statement;

import static java.util.Objects.nonNull;

public class Print extends Statement {
    private Expression expression;

    public Print(Script script, Expression expression) {
        super(script);
        this.expression = expression;
    }

    public Print(Script script) {
        super(script);
    }

    @Override
    public void execute() {
        if (nonNull(this.expression)) {
            System.out.println(expression.evaluate(script));
        } else {
            System.out.println();
        }
    }

    @Override
    public String toString() {
        return "print(" + (nonNull(expression) ? expression.toString() : "") + ");\n";
    }
}
