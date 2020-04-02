package helper;

public enum DateTimeUnit {
    DAY(1), WEEK(7), TWOWEEKS(14), MONTH(30), QUARTER(90), SIXMONTHS(180), YEAR(360);
    private int value;
    private DateTimeUnit(int i) {
        this.value = i;
    }
    public int getValue() {
        return this.value;
    }
}
