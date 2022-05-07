package main.java.streams;

import main.java.common.MutableType;
import main.java.common.collections.Multiset;
import main.java.common.extension.List2;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collector;

public class Collectors2 {

    public static <E, B, R> Collector<E, MutableType<B>, R> of(
            Supplier<B> supplier,
            BiFunction<B, E, B> consumer,
            BinaryOperator<B> combiner,
            Function<B, R> finisher) {
        return Collector.of(() -> MutableType.of(supplier.get()),
                (b, e) -> b.setValue(consumer.apply(b.value(), e)),
                (x, y) -> MutableType.of(combiner.apply(x.value(), y.value())),
                x -> finisher.apply(x.value()));
    }


    public static <E> Collector<E, Multiset<E>, Multiset<E>> toMultiset() {
        return Collector.of(
                Multiset::empty,
                (x, e) -> x.add(e),
                (x, y) -> Multiset.add(x, y),
                x -> x);
    }

    public static <E> Collector<E, List<E>, List<E>> mergeSort(Comparator<? super E> cmp) {
        return Collector.of(
                ArrayList::new,
                (x, e) -> List2.insertOrdered(x, e, cmp),
                (x, y) -> List2.mergeOrdered(x, y, cmp),
                x -> x);
    }

    public static <E extends Comparable<? super E>> Collector<E, List<E>, List<E>> mergeSort() {
        return mergeSort(Comparator.naturalOrder());
    }

    public static <E> Collector<E, MutableType<Boolean>, Boolean> all(Predicate<E> p) {
        return of(
                () -> true,
                (b, e) -> p.test(e),
                (b1, b2) -> b1 && b2,
                b -> b);
    }

    public static <E> Collector<E, Set<E>, Integer> countDistinct() {
        return Collector.of(
                HashSet::new,
                Set::add,
                (s1, s2) -> {
                    Set<E> c = new HashSet<>(s1);
                    c.addAll(s2);
                    return c;
                },
                Set::size);
    }

    private Collectors2() {
    }
}
