package com.fadda.iterables.iterator;

import com.fadda.common.tuples.pair.Pair;

import java.util.Iterator;

public class IteratorConsecutivePairs<E> implements Iterator<Pair<E, E>>, Iterable<Pair<E, E>> {

    private final Iterator<E> iterator;
    private E last;

    private IteratorConsecutivePairs(Iterator<E> iterator, E last) {
        super();
        this.iterator = iterator;
        this.last = last;
    }


    public static <E> Iterable<Pair<E, E>> of(Iterable<E> iterable) {
        Iterable<Pair<E, E>> r = IteratorEmpty.of();
        Iterator<E> it = iterable.iterator();
        E last;
        if (it.hasNext()) {
            last = it.next();
            if (it.hasNext()) {
                r = new IteratorConsecutivePairs<>(it, last);
            }
        }
        return r;
    }


    @Override
    public Iterator<Pair<E, E>> iterator() {
        return this;
    }

    @Override
    public boolean hasNext() {
        return this.iterator.hasNext();
    }

    @Override
    public Pair<E, E> next() {
        E oldLast = this.last;
        this.last = this.iterator.next();
        return Pair.of(oldLast, this.last);
    }

}

