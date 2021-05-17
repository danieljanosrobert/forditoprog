package ast.statement.conditional;

import ast.Expression;
import ast.Script;
import ast.Statement;
import ast.exception.BreakException;
import ast.exception.ContinueException;

import java.util.List;
import java.util.stream.Collectors;

import static ast.generator.Generate.tabulateStatements;
import static java.util.Objects.nonNull;

public class Switch extends Statement {
    private Expression switchExpression;
    private List<Case> caseStatements;
    private Statement defaultStatement;
    private boolean run;

    public Switch(Script script, Expression switchExpression, List<Statement> caseStatements,
                  Statement defaultStatement) {
        super(script);
        this.switchExpression = switchExpression;
        this.caseStatements = caseStatements.stream()
                .map(statement -> (Case) statement)
                .collect(Collectors.toList());
        this.defaultStatement = defaultStatement;
    }

    public List<Case> getCaseStatements() {
        return caseStatements;
    }

    public Statement getDefaultStatement() {
        return defaultStatement;
    }

    @Override
    public void execute() {
        this.run = false;
        for (Case statement : caseStatements) {
            if (this.run || statement.testStatement()) {
                this.run = true;
            }
            if (run) {
                try {
                    statement.execute();
                } catch (BreakException cb) {
                    break;
                } catch (ContinueException ce) {
                    throw new ContinueException();
                }
            }
        }
        if (!run && nonNull(defaultStatement)) {
            defaultStatement.execute();
        }
    }

    public String getHeader() {
        return "switch ("
                + switchExpression.toString()
                + ") {\n";
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getHeader());
        caseStatements.forEach(statement -> stringBuilder.append(tabulateStatements(statement)));
        stringBuilder.append("}\n");
        return stringBuilder.toString();
    }
}
