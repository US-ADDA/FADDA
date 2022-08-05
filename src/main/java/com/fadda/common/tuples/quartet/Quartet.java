package com.fadda.common.tuples.quartet;

public record Quartet<A, B, C, D>(A first, B second, C third, D fourth) {


    public static <A, B, C, D> Quartet<A, B, C, D> of(A first, B second, C third, D fourth) {
        return new Quartet<>(first, second, third, fourth);
    }


    @Override
    public String toString() {
        return String.format("(%s,%s,%s,%s)", first, second, third, fourth);
    }
}
