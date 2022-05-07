package us.lsi.basictypes;


import us.lsi.common.Preconditions;

import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * Una implementación de un array de tamaño variable
 *
 * @param <E> Tipo de los elementos
 * @author Miguel Toro
 */
public class AList<E> {

    public static <E> AList<E> empty() {
        return new AList<>();
    }

    public static <E> AList<E> of(int capacity) {
        return new AList<>(capacity);
    }

    public static <E> AList<E> of(AList<E> a) {
        return new AList<>(a);
    }

    public static <E> AList<E> of(E[] a) {
        return new AList<>(a);
    }

    private int capacity;
    private int size;
    private E[] data;

    private AList() {
        super();
        this.capacity = 10;
        this.size = 0;
        this.data = null;
    }

    private AList(int capacity) {
        super();
        this.capacity = capacity;
        this.size = 0;
        this.data = null;
    }

    private AList(AList<E> a) {
        super();
        this.capacity = a.capacity;
        this.size = a.size();
        this.data = Arrays.copyOf(a.data, a.capacity);
    }

    private AList(E[] a) {
        super();
        this.capacity = a.length;
        this.size = capacity;
        this.data = Arrays.copyOf(a, capacity);
    }

    private void grow() {
        if (size == capacity) {
            E[] oldElements = data;
            capacity = capacity * 2;
            data = Arrays.copyOf(oldElements, capacity);
        }
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public E get(int index) {
        Preconditions.checkElementIndex(index, size);
        return data[index];
    }

    @SuppressWarnings("unchecked")
    public E set(int index, E e) {
        Preconditions.checkPositionIndex(index, this.size);
        if (this.data == null) this.data = (E[]) Array.newInstance(e.getClass(), capacity);
        if (index == this.size) {
            this.size = this.size + 1;
            grow();
        }
        E r = get(index);
        data[index] = e;
        return r;
    }

    @SuppressWarnings("unchecked")
    public boolean add(E e) {
        if (this.data == null) this.data = (E[]) Array.newInstance(e.getClass(), capacity);
        grow();
        data[size] = e;
        size++;
        return true;
    }

    public void add(int index, E e) {
        Preconditions.checkPositionIndex(index, size);
        add(e);
        // size ya ha quedado aumentado
        if (size - 1 - index >= 0) System.arraycopy(data, index, data, index + 1, size - 1 - index);
        data[index] = e;
    }

    public E remove(int index) {
        Preconditions.checkElementIndex(index, size);
        E e = data[index];
        if (size - 1 - index >= 0) System.arraycopy(data, index + 1, data, index, size - 1 - index);
        size--;
        return e;
    }

    public E[] toArray() {
        return Arrays.copyOf(this.data, size);
    }

    public String toString() {
        StringBuilder s = new StringBuilder("{");
        boolean prim = true;
        for (int i = 0; i < size; i++) {
            if (prim) {
                prim = false;
                s.append(data[i]);
            } else {
                s.append(",").append(data[i]);
            }
        }
        s.append("}");
        return s.toString();
    }
}
