package com.fadda.common.views;

public record View2E<D, E>(E e, D left, D right) {

    public static <D, E> View2E<D, E> of(E e, D left, D right) {
        return new View2E<>(e, left, right);
    }
}
