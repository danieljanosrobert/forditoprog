package ast.statement;

import ast.Script;
import ast.Statement;
import ast.BreakException;

public class Break extends Statement {

    public Break(Script script) {
        super(script);
    }

    @Override
    public void execute() {
        throw new BreakException();
    }

    @Override
    public String toString() {
        return "break;\n";
    }
}
