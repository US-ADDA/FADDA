package main.java.streams;


import main.java.common.extension.List2;
import main.java.common.views.View1;
import main.java.common.views.View2;

import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators.AbstractSpliterator;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

public class Spliterators2 {

    public static <T> Spliterator<T> of(Stream<T> stream) {
        return stream.spliterator();
    }

    public static <L, R, T> Spliterator<T> zip(Spliterator<L> lefts, Spliterator<R> rights, BiFunction<L, R, T> combiner) {
        return new AbstractSpliterator<>(
                Long.min(lefts.estimateSize(), rights.estimateSize()),
                lefts.characteristics() & rights.characteristics()) {
            @Override
            public boolean tryAdvance(Consumer<? super T> action) {
                return lefts.tryAdvance(left -> rights.tryAdvance(right -> action.accept(combiner.apply(left, right))));
            }
        };
    }

    public static <D, E> SpliteratorOfView<D, E> of(D d, Function<D, View2<D>> view2,
                                                    Function<D, View1<D, E>> view1, Function<D, Integer> size, Function<D, Integer> nu) {
        return new SpliteratorOfView<>(d, view2, view1, size, nu);
    }

    public static <E> SpliteratorOfView<List<E>, E> ofList(List<E> d) {
        return new SpliteratorOfView<>(d, List2::view2, List2::view1, List::size, ls -> 5);
    }

    static class SpliteratorOfView<D, E> implements Spliterator<E> {

        private D d;
        private final Function<D, View2<D>> view2;
        private final Function<D, View1<D, E>> view1;
        private final Function<D, Integer> size;
        private final Function<D, Integer> nu;

        private SpliteratorOfView(D d, Function<D, View2<D>> view2, Function<D, View1<D, E>> view1,
                                  Function<D, Integer> size, Function<D, Integer> nu) {
            super();
            this.d = d;
            this.view2 = view2;
            this.view1 = view1;
            this.size = size;
            this.nu = nu;
        }

        @Override
        public boolean tryAdvance(Consumer<? super E> action) {
            boolean r = false;
            if (this.size.apply(this.d) > 0) {
                View1<D, E> vw = this.view1.apply(d);
                E e = vw.e();
                this.d = vw.r();
                action.accept(e);
                r = true;
            }
            return r;
        }

        @Override
        public Spliterator<E> trySplit() {
            Spliterator<E> r = null;
            if (this.size.apply(this.d) > this.nu.apply(this.d)) {
                View2<D> vw = this.view2.apply(d);
                r = Spliterators2.of(vw.left(), view2, view1, size, nu);
                this.d = vw.right();
            }
            return r;
        }

        @Override
        public long estimateSize() {
            return this.size.apply(this.d);
        }

        @Override
        public int characteristics() {
            return 0;
        }

        public Stream<E> stream() {
            return Stream2.of(this);
        }
    }

}
