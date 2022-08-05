package com.fadda.iterables;

import java.util.Iterator;
import java.util.List;

public class ListIterator<E> implements Iterator<E>, Iterable<E> {
    private final List<E> ls;
    private Integer i;

    public ListIterator(List<E> ls) {
        this.i = 0;
        this.ls = ls;
    }

    @Override
    public Iterator<E> iterator() {
        return new ListIterator<>(ls);
    }

    @Override
    public boolean hasNext() {
        return i < ls.size();
    }

    @Override
    public E next() {
        E e = ls.get(i);
        i = i + 1;
        return e;
    }
}

