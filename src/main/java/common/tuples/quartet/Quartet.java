package main.java.common.tuples.quartet;

public class Quartet<A, B, C, D> {
    private final A first;
    private final B second;
    private final C third;
    private final D fourth;

    public Quartet(A first, B second, C third, D fourth) {
        this.first = first;
        this.second = second;
        this.third = third;
        this.fourth = fourth;
    }

    public A first() {
        return first;
    }

    public B second() {
        return second;
    }

    public C third() {
        return third;
    }

    public D fourth() {
        return fourth;
    }

    public static <A, B, C, D> Quartet<A, B, C, D> of(A first, B second, C third, D fourth) {
        return new Quartet<>(first, second, third, fourth);
    }

    @Override
    public String toString() {
        return String.format("(%s,%s,%s,%s)", first, second, third, fourth);
    }
}
