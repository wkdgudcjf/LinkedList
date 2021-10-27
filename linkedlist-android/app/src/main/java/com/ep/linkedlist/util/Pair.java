package com.ep.linkedlist.util;

public class Pair<A, B> {
    private A first;
    private B second;

    public Pair(A first, B second) {
        super();
        this.first = first;
        this.second = second;
    }

    public boolean equals(Object rhs) {
        if (rhs instanceof Pair) {
            Pair rhsPair = (Pair)rhs;

            return (
                (this.first == rhsPair.first ||
                (this.first != null && rhsPair.first != null && this.first.equals(rhsPair.first))) &&
                (this.second == rhsPair.second ||
                (this.first != null && rhsPair.first != null && this.first.equals(rhsPair.first)))
            );
        }

        return false;
    }

    public A getFirst() { return first; }

    public B getSecond() { return second; }

    public String toString() {
        return "(" + first + ", " + second + ")";
    }
}