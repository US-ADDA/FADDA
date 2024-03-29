package com.fadda.iterables.iterator;

import java.util.Iterator;
import java.util.function.Function;

public class IteratorFlatMap<E, R> implements Iterator<R>, Iterable<R> {

    private final Iterator<E> iterator;
    private final Function<E, Iterable<R>> fmap;
    private Iterator<R> actual;

    IteratorFlatMap(Iterator<E> iterator, Function<E, Iterable<R>> fmap) {
        this.iterator = IteratorFilter.of(iterator, e -> fmap.apply(e).iterator().hasNext());
        this.fmap = fmap;
        this.actual = this.iterator.hasNext() ? fmap.apply(this.iterator.next()).iterator() : null;
    }


    public static <E, R> IteratorFlatMap<E, R> of(Iterator<E> iterator, Function<E, Iterable<R>> fmap) {
        return new IteratorFlatMap<>(iterator, fmap);
    }


    @Override
    public boolean hasNext() {
        return this.actual.hasNext() || this.iterator.hasNext();
    }

    @Override
    public R next() {
        if (!this.actual.hasNext()) {
            do
                this.actual = fmap.apply(this.iterator.next()).iterator();
            while (!this.actual.hasNext());
        }
        return this.actual.next();
    }

    @Override
    public Iterator<R> iterator() {
        return this;
    }
}

