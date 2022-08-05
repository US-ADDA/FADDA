package com.fadda.iterables.iterator;

import com.fadda.common.extension.Comparator2;

import java.util.Comparator;
import java.util.Iterator;

public class IteratorFusionOrdered<E> implements Iterator<E>, Iterable<E> {

    private final Iterator<E> it1;
    private final Iterator<E> it2;
    private final Comparator<E> cmp;
    private E e1;
    private E e2;

    private IteratorFusionOrdered(Iterator<E> iteratorA, Iterator<E> iteratorB, Comparator<E> cmp) {
        super();
        this.it1 = iteratorA;
        this.it2 = iteratorB;
        this.cmp = cmp;
        this.e1 = null;
        if (it1.hasNext()) e1 = it1.next();
        this.e2 = null;
        if (it2.hasNext()) e2 = it2.next();

    }


    public static <E> Iterable<E> of(Iterable<E> iteratorA, Iterable<E> iteratorB, Comparator<E> cmp) {
        return new IteratorFusionOrdered<>(iteratorA.iterator(), iteratorB.iterator(), cmp);
    }


    @Override
    public Iterator<E> iterator() {
        return this;
    }

    @Override
    public boolean hasNext() {
        return this.e1 != null || this.e2 != null;
    }

    @Override
    public E next() {
        E e;
        if (e2 == null || e1 != null && Comparator2.isLE(e1, e2, this.cmp)) {
            e = e1;
            e1 = null;
            if (it1.hasNext()) e1 = it1.next();
        } else {
            e = e2;
            e2 = null;
            if (it2.hasNext()) e2 = it2.next();
        }
        return e;
    }
}
