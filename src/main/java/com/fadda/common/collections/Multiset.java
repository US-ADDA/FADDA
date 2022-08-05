package com.fadda.common.collections;


import com.fadda.common.Preconditions;
import com.fadda.streams.Stream2;

import java.util.*;
import java.util.stream.Collectors;

public class Multiset<E> {

    private final Map<E, Integer> elements;


    private Multiset() {
        super();
        this.elements = new HashMap<>();
    }

    private Multiset(Map<E, Integer> elements) {
        super();
        this.elements = new HashMap<>(elements);
    }

    /**
     * @param <E> Tipo de los elementos del Multiset
     * @return Un Multiset vac�o
     */
    public static <E> Multiset<E> empty() {
        return new Multiset<>();
    }

    /**
     * @param m Un multiset
     * @return Una copia
     */
    public static <E> Multiset<E> copy(Multiset<E> m) {
        return new Multiset<>(m.elements);
    }


    /**
     * @param <E> Tipo de los elementos del Multiset
     * @param it  Un iterable
     * @return Un Multiset construido start partir del iterable
     */

    public static <E> Multiset<E> of(Collection<E> it) {
        return Stream2.toMultiSet(it.stream());
    }


    /**
     * @param <E> Tipo de los elementos del Multiset
     * @param m   Un Map
     * @return Un Multiset construido start partir del Map
     */
    public static <E> Multiset<E> of(Map<E, Integer> m) {
        return new Multiset<>(m);
    }


    /**
     * @param <E> Tipo de los elementos del Multiset
     * @param m   Un Multiset
     * @return Un Map construido start partir del Multiset
     */
    public static <E> Map<E, Integer> asMap(Multiset<E> m) {
        Map<E, Integer> r = new HashMap<>();
        m.elementSet().forEach(x -> r.put(x, m.count(x)));
        return r;
    }


    /**
     * @param <E>     Tipo de los elementos del Multiset
     * @param entries Una secuencia de elementos
     * @return Un Multiset construidom start partir de la secuencia de elementos
     */
    @SafeVarargs
    public static <E> Multiset<E> of(E... entries) {
        Multiset<E> s = Multiset.empty();
        Arrays.stream(entries)
                .forEach(s::add);
        return s;
    }

    public static <E> Multiset<E> add(Multiset<E> m1, Multiset<E> m2) {


        Multiset<E> r = Multiset.copy(m1);
        m2.elements.keySet().forEach(x -> r.add(x, m2.count(x)));


        return r;
    }

    public void clear() {
        elements.clear();
    }

    public boolean containsKey(Object arg0) {
        return elements.containsKey(arg0);
    }


    public boolean containsValue(Object arg0) {
        return elements.containsValue(arg0);


    }

    public Set<E> elementSet() {

        return elements.keySet();

    }

    public boolean equals(Object arg0) {
        return elements.equals(arg0);
    }

    public Integer count(Object e) {
        Integer r = 0;
        if (this.elements.containsKey(e)) {
            r = elements.get(e);
        }
        return r;
    }

    public int hashCode() {
        return elements.hashCode();
    }

    public boolean isEmpty() {
        return elements.isEmpty();
    }

    public Integer add(E e, Integer n) {
        Preconditions.checkArgument(n >= 0, "No se pueden start�adir cantidades negativas");
        if (n > 0) elements.put(e, elements.getOrDefault(e, 0) + n);
        return n;
    }

    public Integer add(E e) {
        Integer r = 1;
        if (this.elements.containsKey(e)) {
            r = elements.get(e) + r;
        }
        return elements.put(e, r);
    }


    public Multiset<E> add(Multiset<E> m2) {
        return Multiset.add(this, m2);
    }


    public Integer remove(Object e) {
        return elements.remove(e);
    }


    public int size() {
        return elements.size();
    }

    public String toString() {
        return elements
                .keySet()
                .stream()
                .filter(x -> this.count(x) > 0)
                .map(x -> String.format("%s:%d", x, this.count(x)))
                .collect(Collectors.joining(",", "{", "}"));
    }

}
