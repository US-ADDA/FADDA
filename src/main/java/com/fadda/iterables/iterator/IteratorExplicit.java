package com.fadda.iterables.iterator;

import java.util.Iterator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

public class IteratorExplicit<T, E> implements Iterator<E>, Iterable<E> {

    private final Predicate<T> hn;
    private final Function<T, E> nx1;
    private final UnaryOperator<T> nx2;
    private T t;


    public IteratorExplicit(T t0, Predicate<T> hn, Function<T, E> nx1, UnaryOperator<T> nx2) {
        super();
        this.t = t0;
        this.hn = hn;
        this.nx1 = nx1;
        this.nx2 = nx2;
    }


    @Override
    public Iterator<E> iterator() {
        return this;
    }


    @Override
    public boolean hasNext() {
        return hn.test(t);
    }


    @Override
    public E next() {
        E e = nx1.apply(t);
        this.t = nx2.apply(t);
        return e;
    }
}
