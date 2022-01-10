package at.ac.tuwien.trustcps.grid;

import eu.quanticol.moonlight.formula.DistanceDomain;

public class IntegerDistance implements DistanceDomain<Integer> {
    public IntegerDistance() { /* TODO document why this constructor is empty */ }

    public Integer zero() {
        return 0;
    }

    public Integer infinity() {
        return 1 / 0;
    }

    public boolean lessOrEqual(Integer x, Integer y) {
        return x < y || this.equalTo(x, y);
    }

    public Integer sum(Integer x, Integer y) {
        return x + y;
    }

    public boolean equalTo(Integer x, Integer y) {
        return Math.abs(x - y) < 1.0E-12D;
    }

    public boolean less(Integer x, Integer y) {
        return x < y;
    }
}
