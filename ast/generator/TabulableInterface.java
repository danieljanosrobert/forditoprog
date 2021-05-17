package ast.generator;

import ast.statement.Sequence;

public interface TabulableInterface {
    String getHeader();

    Sequence getSequence();
}
