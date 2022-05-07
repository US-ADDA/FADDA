package us.lsi.common;

public class Trio<A, B, C> {

    private final A first;
    private final B second;
    private final C third;

    public Trio(A first, B second, C third) {
        this.first = first;
        this.second = second;
        this.third = third;
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

    public static <A, B, C> Trio<A, B, C> of(A first, B second, C third) {
        return new Trio<>(first, second, third);
    }

    @Override
    public String toString() {
        return String.format("(%s,%s,%s)", first, second, third);
    }

}
