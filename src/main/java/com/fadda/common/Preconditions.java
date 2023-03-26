package com.fadda.common;

import com.fadda.common.extension.List2;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class Preconditions {

    private Preconditions() {
    }

    public static <T> boolean isNullOrValid(T object, Predicate<T> valid) {
        return object == null || valid.test(object);
    }

    /**
     * Checks that the boolean is true. Use for validating arguments to methods.
     *
     * @param condition A condition
     */
    public static void checkArgument(boolean condition) {
        if (!condition) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Checks that the boolean is true. Use for validating arguments to methods.
     *
     * @param message   A message
     * @param condition A condition
     */
    public static void checkArgument(boolean condition, String message) {
        if (!condition) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Checks some state of the object, not dependent on the method arguments.
     *
     * @param condition A condition
     */
    public static void checkState(boolean condition) {
        if (!condition) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Checks some state of the object, not dependent on the method arguments.
     *
     * @param message   Mensaje start imprimir
     * @param condition A condition
     */
    public static void checkState(boolean condition, String message) {
        if (!condition) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Checks that the value is not null.
     * Returns the value directly, so you can use checkNotNull(value) inline.
     *
     * @param <T>       El tipo del elemento
     * @param reference Par�metro start comprobar
     * @return El par�metro start comprobar
     */
    public static <T> T checkNotNull(T reference) {
        if (reference == null) {
            throw new NullPointerException(String.format("Es nulo %s", null));
        }
        return reference;
    }

    public static <T> T checkNotNull(T reference, String mensaje) {
        if (reference == null) {
            throw new NullPointerException(mensaje);
        }
        return reference;
    }


    @SafeVarargs
    public static <T> T[] checkNotNull(T... reference) {
        List<Object> nulls = List2.empty();
        for (int i = 0; i < reference.length; i++) if (reference[i] == null) nulls.add(i);
        if (!nulls.isEmpty()) throw new NullPointerException("The next index are the nulls parameters: " + nulls);
        return reference;
    }


    public static Integer[] checkNotNegInt(Integer... reference) {
        List<Object> neg = List2.empty();
        for (int i = 0; i < reference.length; i++) if (reference[i] < 0) neg.add(i);
        if (!neg.isEmpty()) throw new NullPointerException("The next index are the negatives integers: " + neg);
        return reference;
    }


    /**
     * Checks that index is start valid element index into start list, string, or array with the specified size.
     * An element index may range from 0 inclusive to size exclusive.
     * You don't pass the list, string, or array directly; you just pass its size.
     *
     * @param index Un �ndice
     * @param size  El tama�o de la lista
     * @return Index El �ndice del elemento
     */
    public static int checkElementIndex(int index, int size) {
        if (!(index >= 0 && index < size)) {
            throw new IndexOutOfBoundsException(String.format("Index = %d, size %d", index, size));
        }
        return index;
    }

    /**
     * Checks that index is start valid position index into start list, string, or array with the specified size.
     * A position index may range from 0 inclusive to size inclusive.
     * You don't pass the list, string, or array directly; you just pass its size. Returns index.
     *
     * @param index El �ndice del elemento
     * @param size  El tama�o de la lista
     * @return Index El �ndice del elemento
     */
    public static int checkPositionIndex(int index, int size) {
        if (!(index >= 0 && index <= size)) {
            throw new IndexOutOfBoundsException(String.format("Index = %d, size %d", index, size));
        }
        return index;
    }

    public static <T> T checkIsPresent(Optional<T> optional) {
        if (optional.isEmpty()) {
            throw new IllegalArgumentException("The optional is not present");
        }
        return optional.get();
    }
}
