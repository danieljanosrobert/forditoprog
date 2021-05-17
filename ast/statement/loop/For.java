package ast.statement.loop;

import ast.Expression;
import ast.Script;
import ast.Statement;
import ast.exception.BreakException;
import ast.exception.ContinueException;
import ast.expression.Variable;
import ast.generator.TabulableInterface;
import ast.statement.Assign;
import ast.statement.Sequence;

import static ast.generator.Generate.tabulateStatements;
import static java.util.Objects.nonNull;

public class For extends Statement implements TabulableInterface {
    private Statement init;
    private Variable variable;
    private Expression condition;
    private Statement update;
    private Sequence sequence;

    public For(Script script, Statement init, Expression condition, Statement update, Sequence sequence) {
        super(script);
        this.init = init;
        this.condition = condition;
        this.update = update;
        this.sequence = sequence;
    }

    public For(Script script, Expression variable, Expression condition, Statement update, Sequence sequence) {
        super(script);
        this.variable = (Variable) variable;
        this.condition = condition;
        this.update = update;
        this.sequence = sequence;
    }

    public Sequence getSequence() {
        return sequence;
    }

    @Override
    public void execute() {
        if (nonNull(this.init)) {
            this.init.execute();
        }
        while (this.condition.evaluate(script).getIntegerValue() == 1) {
            try {
                for (Statement statement : sequence.getStatements()) {
                    script.newStackFrame();
                    statement.execute();
                    script.freeStackFrame();
                }
                this.update.execute();
            } catch (BreakException be) {
                script.freeStackFrame();
                break;
            } catch (ContinueException ce) {
                this.update.execute();
                script.freeStackFrame();
                continue;
            }
        }
    }

    public String getHeader() {
        return "for (" +
                (nonNull(init) ? ((Assign) init).toForString() : variable.getVariableName()) +
                "; " +
                condition.toString() +
                "; " +
                ((Assign) update).toForString() +
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
