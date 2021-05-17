package ast.statement;

import ast.Script;
import ast.Statement;
import ast.exception.ContinueException;

public class Continue extends Statement {
    public Continue(Script script) {
        super(script);
    }

    @Override
    public void execute() {
        throw new ContinueException();
    }

    @Override
    public String toString() {
        return "continue;\n";
    }
}
