package ast;

import static java.util.Objects.isNull;

public class Value {
    private Double doubleValue;
    private Integer intValue;
    private boolean fraction;

    public static int getBooleanOfValue(Value value) {
        if (value.isFraction() || value.getIntegerValue() < 0 || value.getIntegerValue() > 1) {
            throw new RuntimeException(value + " can not be evaluated as a boolean.");
        }
        return value.getIntegerValue();
    }

    public static int getNegatedBooleanOfValue(Value value) {
        if (value.isFraction() || value.getIntegerValue() < 0 || value.getIntegerValue() > 1) {
            throw new RuntimeException(value + " can not be evaluated as a boolean.");
        }
        return Math.abs(value.getIntegerValue() - 1);
    }

    public Value(double value) {
        this.doubleValue = value;
        this.intValue = null;
        this.fraction = true;
    }

    public Value(int value) {
        this.doubleValue = null;
        this.intValue = value;
        this.fraction = false;
    }

    public Integer getIntegerValue() {
        if (this.fraction) {
            throw new RuntimeException("Not an integer!");
        }
        return intValue;
    }

    public Double getDoubleValue() {
        if (!this.fraction) {
            throw new RuntimeException("Not a double!");
        }
        return doubleValue;
    }

    public void setDoubleValue(Double val) {
        this.doubleValue = val;
    }

    public void setIntegerValue(Integer val) {
        this.intValue = val;
    }

    public boolean isFraction() {
        return fraction;
    }

    public void setFraction(boolean val) {
        this.fraction = val;
    }

    @Override
    public String toString() {
        if (this.isFraction() && isNull(this.doubleValue) || !this.isFraction() && isNull(this.intValue)) {
            return null;
        }
        return this.isFraction() ? Double.toString(this.doubleValue) : Integer.toString(this.intValue);
    }
}
