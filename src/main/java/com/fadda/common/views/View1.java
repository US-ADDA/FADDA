package com.fadda.common.views;

public record View1<D, E>(E e, D r) {

    public static <D, E> View1<D, E> of(E e, D r) {
        return new View1<>(e, r);
    }
}
