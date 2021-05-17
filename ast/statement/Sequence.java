package ast.statement;

import ast.Script;
import ast.Statement;

import java.util.ArrayList;
import java.util.List;

public class Sequence extends Statement {
    private List<Statement> statements = new ArrayList<>();

    public Sequence(Script script) {
        super(script);
    }

    public void addStatement(Statement statement) {
        this.statements.add(statement);
    }

    public List<Statement> getStatements() {
        return this.statements;
    }

    @Override
    public void execute() {
        statements.forEach(s -> s.execute());
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        statements.forEach(statement -> str.append(statement.toString()));
        return str.toString();
    }
}
