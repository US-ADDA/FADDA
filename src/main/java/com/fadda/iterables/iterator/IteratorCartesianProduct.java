package com.fadda.iterables.iterator;

import com.fadda.common.tuples.pair.Pair;

import java.util.Iterator;


public class IteratorCartesianProduct<A, B> implements Iterator<Pair<A, B>>, Iterable<Pair<A, B>> {

    private final Iterator<A> iteratorA;
    private final Iterable<B> iterableB;
    private A actualA;
    private Iterator<B> iteratorB;

    private IteratorCartesianProduct(Iterable<A> iterableA, Iterable<B> iterableB) {
        super();
        this.iteratorA = iterableA.iterator();
        this.actualA = this.iteratorA.next();
        this.iterableB = iterableB;
        this.iteratorB = this.iterableB.iterator();
    }


    public static <A> Iterable<Pair<A, A>> of(Iterable<A> iterableA) {
        Iterable<Pair<A, A>> r = IteratorEmpty.of();
        if (iterableA.iterator().hasNext()) {

            r = new IteratorCartesianProduct<>(iterableA, iterableA);
        }
        return r;
    }


    public static <A, B> Iterable<Pair<A, B>> of(Iterable<A> iterableA, Iterable<B> iterableB) {
        Iterable<Pair<A, B>> r = IteratorEmpty.of();
        if (iterableA.iterator().hasNext() && iterableB.iterator().hasNext()) {

            r = new IteratorCartesianProduct<>(iterableA, iterableB);
        }
        return r;
    }

    @Override
    public Iterator<Pair<A, B>> iterator() {
        return this;
    }

    @Override
    public boolean hasNext() {
        return this.iteratorA.hasNext() || this.iteratorB.hasNext();
    }

    @Override
    public Pair<A, B> next() {
        if (!this.iteratorB.hasNext()) {
            this.iteratorB = this.iterableB.iterator();
            this.actualA = this.iteratorA.next();
        }
        return Pair.of(this.actualA, this.iteratorB.next());
    }
}
