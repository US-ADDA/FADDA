package com.fadda.iterables.iterator;

import java.util.Iterator;
import java.util.function.Predicate;

public class IteratorFilter<E> implements Iterator<E>, Iterable<E> {

    private final Iterator<E> iterator;
    private final Predicate<E> p;
    private E ne;


    public IteratorFilter(Iterator<E> iterator, Predicate<E> p) {
        super();
        this.iterator = iterator;
        this.p = p;
        this.ne = first(this.iterator, this.p);
    }

    public static <E> IteratorFilter<E> of(Iterator<E> iterator, Predicate<E> p) {
        return new IteratorFilter<>(iterator, p);
    }

    public static <E> E first(Iterator<E> s, Predicate<E> p) {
        E r = null;
        while (s.hasNext() && r == null) {
            E e = s.next();
            if (p.test(e)) r = e;
        }
        return r;
    }

    @Override
    public boolean hasNext() {
        return this.ne != null;
    }


    @Override
    public E next() {
        E e = ne;
        this.ne = first(this.iterator, this.p);
        return e;
    }

    @Override
    public Iterator<E> iterator() {
        return this;
    }
}

