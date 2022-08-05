package com.fadda.iterables.seq;

import java.util.function.*;

public record SeqCollector<E, B, R>(Supplier<B> supplier, BiFunction<B, E, B> accumulator, Function<B, R> finisher,
                                    Predicate<B> isDone) {


    public static <E, B, R> SeqCollector<E, B, R> of(
            Supplier<B> supplier,
            BiFunction<B, E, B> accumulator,
            Function<B, R> finisher,
            Predicate<B> isDone) {
        return new SeqCollector<>(supplier, accumulator, finisher, isDone);
    }


    public static <E, B> SeqCollector<E, B, B> of(
            Supplier<B> supplier,
            BiFunction<B, E, B> accumulator) {
        return new SeqCollector<>(supplier, accumulator, x -> x, x -> false);
    }


    public static <E> SeqCollector<E, E, E> of(
            BinaryOperator<E> accumulator) {
        return new SeqCollector<>(() -> null, accumulator, x -> x, x -> false);
    }


    public static <E, B, R> SeqCollector<E, B, R> ofBaseMutable(
            Supplier<B> supplier,
            BiConsumer<B, E> accumulator,

            Function<B, R> finisher,
            Predicate<B> isDone) {
        return SeqCollector.of(supplier, (b, e) -> {
            accumulator.accept(b, e);
            return b;
        }, finisher, isDone);
    }

}


