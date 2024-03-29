package com.fadda.basictypes;

import com.fadda.common.Preconditions;

/**
 * Una implementaci�n de una lista enlazada
 *
 * @param <E> Tipo de los elementos
 * @author Miguel Toro
 */
public class LList<E> {
    private Entry<E> first;
    private Entry<E> last;
    private int size;

    public LList() {
        super();
        this.first = null;
        this.last = null;
        this.size = 0;
    }


    public int size() {
        return size;
    }


    public boolean isEmpty() {
        return size == 0;
    }


    public E get(int index) {
        return entryInPos(index).key();

    }


    public E set(int index, E e) {
        Entry<E> e1 = entryInPos(index);
        E r = e1.key();
        e1.setKey(e);
        return r;
    }

    public boolean add(E e) {
        Entry<E> e1 = new Entry<>(e);
        if (last == null) {
            first = e1;
        } else {
            last.setNext(e1);
        }
        last = e1;
        size++;
        return true;
    }


    public boolean add(int index, E e) {
        Preconditions.checkPositionIndex(index, size);
        Entry<E> ne = new Entry<>(e);
        if (index == size) {
            add(e);
        } else if (index == 0) {
            ne.setNext(first);
            first = ne;
        } else {
            Entry<E> pe = entryInPos(index - 1);

            ne.setNext(pe.next());
            pe.setNext(ne);
        }
        size++;
        return true;
    }


    private Entry<E> entryInPos(int index) {
        Preconditions.checkElementIndex(index, size);
        Entry<E> pe = first;
        for (int p = 0; p < index; p++) {
            pe = pe.next();
        }
        return pe;
    }

    public E remove(int index) {
        Preconditions.checkElementIndex(index, size);
        Entry<E> e;
        E element;
        if (index == 0) {
            e = first;

            first = first.next();
            element = e.key();
        } else {
            Entry<E> pe = entryInPos(index - 1);
            element = pe.next().key();
            if (index == size - 1) {
                last = pe;
            } else {
                pe.setNext(pe.next().next());
            }
        }
        size--;
        return element;
    }


    public String toString() {
        StringBuilder s = new StringBuilder("{");
        boolean prim = true;
        for (Entry<E> e = first; e != null; e = e.next()) {
            if (prim) {
                prim = false;
                s.append(e.key());
            } else {
                s.append(",").append(e.key());
            }
        }
        s.append("}");
        return s.toString();
    }

    public static class Entry<F> {
        private F key;
        private Entry<F> next;

        public Entry(F element, Entry<F> next) {
            super();
            this.key = element;
            this.next = next;
        }

        public Entry(F element) {
            super();
            this.key = element;
            this.next = null;
        }

        public F key() {
            return key;
        }

        public void setKey(F key) {
            this.key = key;
        }

        public Entry<F> next() {
            return next;
        }

        public void setNext(Entry<F> next) {
            this.next = next;
        }
    }
}
