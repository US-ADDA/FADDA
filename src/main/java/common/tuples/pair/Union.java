package main.java.common.tuples.pair;

public class Union<A, B> {

    private final A a;
    private final B b;

    public Union(A a, B b) {
        this.a = a;
        this.b = b;
    }

    public A a() {
        return a;
    }

    public B b() {
        return b;
    }

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
