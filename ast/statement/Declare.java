package ast.statement;

import ast.Expression;
import ast.Script;
import ast.Statement;

import java.util.Arrays;

import static java.util.Objects.nonNull;

public class Declare extends Statement {
    private DeclareType type;
    private String variableName;
    private Expression expression;

    public Declare(Script script, String type, String variableName) {
        super(script);
        this.type = DeclareType.getDeclarationType(type);
        this.variableName = variableName;
    }

    public Declare(Script script, String type, String variableName, Expression expression) {
        super(script);
        this.type = DeclareType.getDeclarationType(type);
        this.variableName = variableName;
        this.expression = expression;
    }

    @Override
    public void execute() {
        switch (this.type) {
            case INT:
                script.addVariable(int.class, this.variableName);
                if (nonNull(this.expression)) {
                    new Assign(script, this.variableName, this.expression).execute();
                }
                break;
            case DOUBLE:
                script.addVariable(double.class, this.variableName);
                if (nonNull(this.expression)) {
                    new Assign(script, this.variableName, this.expression, true).execute();
                }
                break;
        }
    }

    @Override
    public String toString() {
        return type.getType()
                + " " + variableName
                + (nonNull(expression) ? " = " + expression.toString() : "")
                + ";\n";
    }

    private enum DeclareType {
        INT("int"), DOUBLE("double");
        private String type;

        DeclareType(String type) {
            this.type = type;
        }

        public static Declare.DeclareType getDeclarationType(String type) {
            return Arrays.stream(DeclareType.values())
                    .filter(_type -> _type.getType().equals(type))
                    .findAny().orElse(null);
        }

        public String getType() {
            return type;
        }
    }
}
