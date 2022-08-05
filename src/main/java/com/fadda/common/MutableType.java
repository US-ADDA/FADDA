package com.fadda.common;

/**
 * @param <T> Un tipo inmutable
 *            <p>
 *            Un versi�n mutable del tipo inmutable
 * @author migueltoro
 */
public class MutableType<T> {

    private T value;

    private MutableType(T e) {
        super();
        this.value = e;
    }

    public static <T> MutableType<T> of(T e) {
        return new MutableType<>(e);
    }

    public T setValue(T e) {
        T old = this.value;
        this.value = e;
        return old;
    }

    public T value() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((value == null) ? 0 : value.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof MutableType<?> other))
            return false;
        if (value == null) {
            return other.value == null;
        } else return value.equals(other.value);
    }
}
