package main.java.common.views;

import java.util.List;

public class ViewL<D, E> {

    private final E e;
    private final List<D> elems;

    public ViewL(E e, List<D> elems) {
        this.e = e;
        this.elems = elems;
    }

    public E e() {
        return e;
    }

    public List<D> elems() {
        return elems;
    }

    public static <D, E> ViewL<D, E> of(E e, List<D> elems) {
        return new ViewL<>(e, elems);
    }
}
