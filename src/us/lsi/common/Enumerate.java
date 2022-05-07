package us.lsi.common;

public class Enumerate<E> {

    private final Integer counter;
    private final E value;

    public Enumerate(Integer counter, E value) {
        this.counter = counter;
        this.value = value;
    }

    public Integer counter() {
        return counter;
    }

    public E value() {
        return value;
    }

    public static <E> Enumerate<E> of(Integer num, E value) {
        return new Enumerate<>(num, value);
    }

    @Override
    public String toString() {
        return String.format("(%d,%s)", counter(), value().toString());
    }

}