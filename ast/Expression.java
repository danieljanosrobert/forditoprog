package ast;

public abstract class Expression {
    public abstract Value evaluate(Script script);
}
