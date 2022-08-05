package com.fadda.iterables.iterator;

import java.util.Iterator;

public class IteratorEmpty<E> implements Iterator<E>, Iterable<E> {

    private IteratorEmpty() {
    }

    public static <E> IteratorEmpty<E> of() {
        return new IteratorEmpty<>();
    }

    @Override
    public Iterator<E> iterator() {
        return this;
    }

    @Override
    public boolean hasNext() {
        return false;
    }

    @Override
    public E next() {
        return null;
    }
}
