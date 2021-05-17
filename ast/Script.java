package ast;

import ast.statement.Sequence;

import java.util.*;

public class Script {

    private Map<String, Value> globals = new HashMap<>();
    private Stack<Map<String, Value>> locals = new Stack<>();
    private List<Statement> statements = new ArrayList<>();

    public void addStatements(Sequence sequence) {
        statements = sequence.getStatements();
    }

    public void addVariable(Class<?> cls, String variableName) {
        Map<String, Value> table = globals;
        if (!locals.empty()) {
            table = locals.peek();
        }
        if (table.containsKey(variableName)) {
            throw new RuntimeException("Variable already exists!");
        }
        if (cls == int.class) {
            table.put(variableName, new Value(0));
        } else if (cls == double.class) {
            table.put(variableName, new Value(0.0));
        } else {
            throw new RuntimeException("Type is not supported!");
        }
    }

    public Value getVariable(String variableName) {
        if (!locals.empty() && locals.peek().containsKey(variableName)) {
            return locals.peek().get(variableName);
        }
        if (!globals.containsKey(variableName)) {
            throw new RuntimeException("Variable " + variableName + " is not declared.");
        }
        return globals.get(variableName);
    }

    public void setVariable(String variableName, Value value) {
        if (!locals.empty() && locals.peek().containsKey(variableName)) {
            assignVariable(variableName, value, locals.peek());
            return;
        }
        if (!globals.containsKey(variableName)) {
            throw new RuntimeException("No variable " + variableName);
        }
        assignVariable(variableName, value, globals);
    }

    private void assignVariable(String variableName, Value value, Map<String, Value> table) {
        Value tableVariable = table.get(variableName);
        if (tableVariable.isFraction() == value.isFraction()) {
            table.put(variableName, value);
        } else if (tableVariable.isFraction()) {
            table.put(variableName, new Value((double) value.getIntegerValue()));
        } else {
            System.err.println("assigned a double value (" + value.getDoubleValue() + ") to " + variableName +
                    ", which was an integer. " + variableName + "'s value is cut to " +
                    (int) value.getDoubleValue().doubleValue());
            table.put(variableName, new Value((int) value.getDoubleValue().doubleValue()));
        }
    }

    public void newStackFrame() {
        locals.push(new HashMap<>());
    }

    public void freeStackFrame() {
        if (locals.empty()) {
            throw new RuntimeException("Return to void");
        }
        locals.pop();
    }

    public void execute() {
        for (Statement statement : statements) {
            statement.execute();
        }
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(" |----------start of script----------| \n");
        statements.forEach(statement -> stringBuilder.append(statement.toString()));
        stringBuilder.append(" |-----------end of script-----------| \n");
        return stringBuilder.toString();
    }

}