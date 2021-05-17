package ast;

public abstract class Statement {
    protected Script script;

    protected Statement(Script script) {
        this.script = script;
    }

    public abstract void execute();

}
