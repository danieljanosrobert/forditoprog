package ast.statement.conditional;

import ast.Expression;
import ast.Script;
import ast.Statement;
import ast.Value;
import ast.generator.TabulableInterface;
import ast.statement.Sequence;

import static ast.generator.Generate.tabulateStatements;

public class Case extends Statement implements TabulableInterface {
    private Expression switchExpression;
    private Expression caseExpression;
    private Sequence sequence;

    public Case(Script script, Expression switchExpression, Expression caseExpression, Sequence sequence) {
        super(script);
        this.switchExpression = switchExpression;
        this.caseExpression = caseExpression;
        this.sequence = sequence;
    }

    public Sequence getSequence() {
        return this.sequence;
    }

    public boolean testStatement() {
        Value switchExpr = switchExpression.evaluate(script);
        Value caseExpr = caseExpression.evaluate(script);
        return (switchExpr.isFraction() ? switchExpr.getDoubleValue() : switchExpr.getIntegerValue())
                == (caseExpr.isFraction() ? caseExpr.getDoubleValue() : caseExpr.getIntegerValue());
    }

    @Override
    public void execute() {
        sequence.execute();
    }

    public String getHeader() {
        return "case " + caseExpression + ":\n";
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\t")
                .append(getHeader());
        sequence.getStatements().forEach(statement -> stringBuilder.append(tabulateStatements(statement, 2)));
        return stringBuilder.toString();
    }
}
