package com.fadda.common.tuples.triplet;

public record Triplet<A, B, C>(A first, B second, C third) {


    public static <A, B, C> Triplet<A, B, C> of(A first, B second, C third) {
        return new Triplet<>(first, second, third);
    }


    @Override
    public String toString() {
        return String.format("(%s,%s,%s)", first, second, third);
    }

}
