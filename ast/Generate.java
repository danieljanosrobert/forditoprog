package ast;

import ast.statement.conditional.Case;
import ast.statement.conditional.If;
import ast.statement.conditional.Switch;

import static java.util.Objects.nonNull;

public class Generate {
    public static String tabulateStatements(Statement statement) {
        return tabulateStatements(statement, 1);
    }

    public static String tabulateStatements(Statement statement, int level) {
        String tabs = "\t".repeat(level);
        StringBuilder stringBuilder = new StringBuilder();
        if (statement instanceof TabulableInterface) {
            tabulateTabulableInterface(statement, level, tabs, stringBuilder);
        } else if (statement.getClass() == Switch.class) {
            tabulateSwitchCaseStatement((Switch) statement, level, tabs, stringBuilder);
        } else if (statement.getClass() == If.class) {
            tabulateIfStatement((If) statement, level, tabs, stringBuilder);
        } else {
            stringBuilder.append(tabs)
                    .append(statement.toString());
        }
        return stringBuilder.toString();
    }

    private static void tabulateIfStatement(If statement, int level, String tabs, StringBuilder stringBuilder) {
        stringBuilder.append(tabs)
                .append(statement.getHeader());
        statement.getTrueBranch().getStatements()
                .forEach(stat -> stringBuilder.append(tabulateStatements(stat, level + 1)));
        stringBuilder.append(nonNull(statement.getFalseBranch()) ? (tabs + "} ") : (tabs + "}\n"));
        tabulateElseBranch(statement, level, tabs, stringBuilder);
    }

    private static void tabulateElseBranch(If statement, int level, String tabs, StringBuilder stringBuilder) {
        if (nonNull(statement.getFalseBranch())) {
            stringBuilder.append("else {\n");
            statement.getFalseBranch().getStatements()
                    .forEach(stat -> stringBuilder.append(tabulateStatements(stat, level + 1)));
            stringBuilder.append(tabs)
                    .append("}\n");
        }
    }

    private static void tabulateSwitchCaseStatement(Switch statement, int level, String tabs,
                                                    StringBuilder stringBuilder) {
        stringBuilder.append(tabs)
                .append(statement.getHeader());
        statement.getCaseStatements()
                .forEach(stat -> stringBuilder.append(tabulateStatements(stat, level + 1)));
        if (nonNull(statement.getDefaultStatement())) {
            stringBuilder.append("\t")
                    .append(tabs)
                    .append("default:\n")
                    .append("\t\t")
                    .append(tabs)
                    .append(statement.getDefaultStatement().toString());
        }
        stringBuilder.append(tabs)
                .append("}\n");
    }

    private static void tabulateTabulableInterface(Statement statement, int level, String tabs,
                                                   StringBuilder stringBuilder) {
        stringBuilder.append(tabs)
                .append(((TabulableInterface) statement).getHeader());
        ((TabulableInterface) statement).getSequence().getStatements()
                .forEach(stat -> stringBuilder.append(tabulateStatements(stat, level + 1)));
        if (statement.getClass() != Case.class) {
            stringBuilder.append(tabs)
                    .append("}\n");
        }
    }
}
