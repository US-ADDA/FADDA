package com.fadda.iterables.iterator;

import java.util.Iterator;
import java.util.function.Function;

public class IteratorMap<E, R> implements Iterator<R>, Iterable<R> {

    private final Iterator<E> iterator;
    private final Function<E, R> fmap;

    public IteratorMap(Iterator<E> iterator, Function<E, R> fmap) {
        super();
        this.iterator = iterator;
        this.fmap = fmap;
    }


    public static <E, R> IteratorMap<E, R> of(Iterable<E> iterator, Function<E, R> fmap) {
        return new IteratorMap<>(iterator.iterator(), fmap);
    }


    @Override
    public boolean hasNext() {
        return this.iterator.hasNext();
    }

    @Override
    public R next() {
        return this.fmap.apply(this.iterator.next());
    }

    @Override
    public Iterator<R> iterator() {
        return this;
    }
}
