package com.fadda.iterables.iterator;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class IterableRangeObject<T> implements Iterable<T> {

    // Attributes -------------------------------------------------------------
    private final int end;
    public final List<T> elements;
    private int start;

    // Constructors -----------------------------------------------------------
    public IterableRangeObject(List<T> elements, Integer limit, Integer offset) {
        if (limit == -1) limit = elements.size();
        this.elements = elements;
        this.start = offset == null || offset < 1 ? 0 : offset - 1;
        this.end = limit > elements.size() || limit + start > elements.size() ? elements.size() : start + limit;
    }

    // Methods ----------------------------------------------------------------

    @Override
    public Iterator<T> iterator() {
        return new IteratorRangeObject(this);
    }

    public Stream<T> stream() {
        return StreamSupport.stream(spliterator(), false);
    }

    // Inner classes ----------------------------------------------------------
    private class IteratorRangeObject implements Iterator<T> {

        // Attributes ---------------------------------------------------------
        private final IterableRangeObject<T> iterableRangeObject;

        // Constructors -------------------------------------------------------
        public IteratorRangeObject(IterableRangeObject<T> iterableRangeObject) {
            this.iterableRangeObject = iterableRangeObject;
        }

        // Methods ------------------------------------------------------------
        @Override
        public boolean hasNext() {
            return start < end;
        }

        @Override
        public T next() {
            return iterableRangeObject.elements.get(start++);
        }
    }
}
