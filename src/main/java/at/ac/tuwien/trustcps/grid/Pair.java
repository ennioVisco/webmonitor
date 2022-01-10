package at.ac.tuwien.trustcps.grid;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class Pair implements Comparable<Pair> {
    private final int first;
    private final int second;

    public Pair(int first, int second) {
        this.first = first;
        this.second = second;
    }

    public int getFirst() {
        return this.first;
    }

    public int getSecond() {
        return this.second;
    }

    public String toString() {
        return "<" + this.first + " , " + this.second + ">";
    }

    @Override
    public int compareTo(@NotNull Pair o) {
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pair)) return false;
        Pair pair = (Pair) o;
        return first == pair.first && second == pair.second;
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }
}

