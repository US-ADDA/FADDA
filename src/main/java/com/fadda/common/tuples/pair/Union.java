package com.fadda.common.tuples.pair;

public record Union<A, B>(A a, B b) {

    public static <A, B> Union<A, B> ofA(A a) {
        return new Union<>(a, null);
    }

    public static <A, B> Union<A, B> ofB(B b) {
        return new Union<>(null, b);
    }

    public Boolean isA() {
        return this.b() == null;
    }

    public Boolean isB() {
        return this.a() == null;
    }

    @Override
    public String toString() {
        return Boolean.TRUE.equals(this.isA()) ? this.a().toString() : this.b().toString();
    }
}
