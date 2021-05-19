package ast.statement.conditional;

import ast.Expression;
import ast.Script;
import ast.Statement;
import ast.Value;
import ast.statement.Sequence;

import static ast.Generate.tabulateStatements;
import static java.util.Objects.nonNull;

public class If extends Statement {
    private Expression condition;
    private Sequence trueBranch;
    private Sequence falseBranch;

    public If(Script script, Expression condition, Sequence trueBranch, Sequence falseBranch) {
        super(script);
        this.condition = condition;
        this.trueBranch = trueBranch;
        this.falseBranch = falseBranch;
    }

    public Sequence getTrueBranch() {
        return trueBranch;
    }

    public Sequence getFalseBranch() {
        return falseBranch;
    }

    @Override
    public void execute() {
        if (Value.getBooleanOfValue(condition.evaluate(script)) == 1) {
            script.newStackFrame();
            trueBranch.execute();
            script.freeStackFrame();
        } else if (nonNull(falseBranch)) {
            script.newStackFrame();
            falseBranch.execute();
            script.freeStackFrame();
        }
    }

    public String getHeader() {
        return "if (" + condition.toString() + ") {\n";
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getHeader());
        trueBranch.getStatements().forEach(statement -> stringBuilder.append(tabulateStatements(statement)));
        stringBuilder.append(nonNull(falseBranch) ? "} " : "}\n");
        if (nonNull(falseBranch)) {
            stringBuilder.append(" else {\n");
            falseBranch.getStatements().forEach(statement -> stringBuilder.append(tabulateStatements(statement)));
            stringBuilder.append("}\n");
        }
        return stringBuilder.toString();
    }
}
