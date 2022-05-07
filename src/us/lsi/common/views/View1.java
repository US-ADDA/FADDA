package us.lsi.common.views;

public class View1<D, E> {

    private final E e;
    private final D r;

    public View1(E e, D r) {
        this.e = e;
        this.r = r;
    }

    public E e() {
        return e;
    }

    public D r() {
        return r;
    }

    public static <D, E> View1<D, E> of(E e, D r) {
        return new View1<>(e, r);
    }
}
