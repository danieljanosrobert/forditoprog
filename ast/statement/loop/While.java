package ast.statement.loop;

import ast.Expression;
import ast.Script;
import ast.Statement;
import ast.exception.BreakException;
import ast.exception.ContinueException;
import ast.generator.TabulableInterface;
import ast.statement.Sequence;

import static ast.generator.Generate.tabulateStatements;

public class While extends Statement implements TabulableInterface {
    private Expression condition;
    private Sequence sequence;

    public While(Script script, Expression condition, Sequence sequence) {
        super(script);
        this.condition = condition;
        this.sequence = sequence;
    }

    public Sequence getSequence() {
        return sequence;
    }

    @Override
    public void execute() {
        while (condition.evaluate(script).getIntegerValue() == 1) {
            try {
                for (Statement statement : sequence.getStatements()) {
                    script.newStackFrame();
                    statement.execute();
                    script.freeStackFrame();
                }
            } catch (BreakException be) {
                script.freeStackFrame();
                break;
            } catch (ContinueException ce) {
                script.freeStackFrame();
                continue;
            }
        }
    }

    public String getHeader() {
        return "while (" +
                condition.toString() +
                ") {\n";
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getHeader());
        sequence.getStatements()
                .forEach(statement -> stringBuilder.append(tabulateStatements(statement)));
        stringBuilder.append("}\n");
        return stringBuilder.toString();
    }
}
