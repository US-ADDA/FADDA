package com.fadda.common.collections;

import com.fadda.common.Preconditions;
import com.fadda.common.views.View1;
import com.fadda.common.views.View2E;

import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public class Ranges {

    public record DoubleRange(Double start, Double end, Double sep) {

        public static DoubleRange of(Double a, Double b, Double c) {
            return new DoubleRange(a, b, c);
        }

        public static DoubleRange of(Double a, Double b) {
            return new DoubleRange(a, b, 1.);
        }


        public Boolean isEmpty() {
            return start >= end;
        }


        public Boolean contains(Double e) {
            return e >= start && e < end;
        }


        public Integer size() {
            return (int) ((end - start) / sep);

        }

        public Double get(Integer i) {


            Preconditions.checkElementIndex(0, this.size());

            return this.start() + i * this.sep();
        }

        public Stream<Double> stream() {

            return DoubleStream.of(start, end, sep).boxed();
        }

        public View1<DoubleRange, Double> view1() {

            Integer n = this.size();
            Preconditions.checkArgument(n > 0, String.format("La lista debe ser de tama�o mayor que 0 y es %d  ", n));
            return View1.of(this.start, DoubleRange.of(start + sep, end));
        }

        public View2E<DoubleRange, Double> view2() {

            Integer n = this.size();
            Preconditions.checkArgument(n > 1, String.format("La lista debe ser de tama�o mayor que 1 y es %d  ", n));
            Double central = (end + start) / 2;
            return View2E.of(central, DoubleRange.of(start, central, sep), DoubleRange.of(central, end, sep));
        }

        public View2E<DoubleRange, Double> view2Overlapping() {
            Integer n = this.size();
            Preconditions.checkArgument(n > 1, String.format("La lista debe ser de tama�o mayor que 1 y es %d  ", n));
            Double central = (end + start) / 2;

            return View2E.of(central, DoubleRange.of(start, central + sep, sep), DoubleRange.of(central, end, sep));
        }
    }

    public record IntRange(Integer start, Integer end, Integer sep) {


        public static IntRange of(Integer a, Integer b, Integer c) {
            return new IntRange(a, b, c);
        }

        public static IntRange of(Integer a, Integer b) {


            return new IntRange(a, b, 1);
        }

        public Boolean isEmpty() {
            return start >= end;


        }

        public Boolean contains(Integer e) {
            return e >= start && e < end;
        }

        public Integer size() {
            return (end - start) / sep;
        }

        public Integer get(Integer i) {
            Preconditions.checkElementIndex(0, this.size());
            return this.start() + i * this.sep();
        }

        public Stream<Integer> stream() {
            return IntStream.of(start, end, sep).boxed();
        }

        public View1<IntRange, Integer> view1() {
            Integer n = this.size();
            Preconditions.checkArgument(n > 0, String.format("La lista debe ser de tama�o mayor que 0 y es %d  ", n));
            return View1.of(this.start, IntRange.of(start + sep, end));
        }

        public View2E<IntRange, Integer> view2() {
            Integer n = this.size();
            Preconditions.checkArgument(n > 1, String.format("La lista debe ser de tama�o mayor que 1 y es %d  ", n));
            Integer central = (end + start) / (2 * sep) * sep;


            return View2E.of(central, IntRange.of(start, central, sep), IntRange.of(central, end, sep));
        }


        public View2E<IntRange, Integer> view2Overlapping() {


            Integer n = this.size();

            Preconditions.checkArgument(n > 1, String.format("La lista debe ser de tama�o mayor que 1 y es %d  ", n));
            Integer central = (end + start) / (2 * sep) * sep;
            return View2E.of(central, IntRange.of(start, central + sep, sep), IntRange.of(central, end, sep));
        }
    }


    public record LongRange(Long start, Long end, Long sep) {

        public static LongRange of(Long a, Long b, Long c) {

            return new LongRange(a, b, c);

        }

        public static LongRange of(Long a, Long b) {

            return new LongRange(a, b, 1L);
        }

        public Boolean isEmpty() {
            return start >= end;
        }

        public Boolean contains(Long e) {
            return e >= start && e < end;
        }

        public Long size() {
            return (end - start) / sep;
        }

        public Long get(Integer i) {
            Preconditions.checkElementIndex(0, this.size().intValue());
            return this.start() + i * this.sep();
        }

        public Stream<Long> stream() {
            return LongStream.of(start, end, sep).boxed();
        }

        public View1<LongRange, Long> view1() {
            Long n = this.size();
            Preconditions.checkArgument(n > 0, String.format("La lista debe ser de tama�o mayor que 0 y es %d  ", n));
            return View1.of(this.start, LongRange.of(start + sep, end));
        }

        public View2E<LongRange, Long> view2() {
            Long n = this.size();
            Preconditions.checkArgument(n > 1, String.format("La lista debe ser de tama�o mayor que 1 y es %d  ", n));
            Long central = (end + start) / (2 * sep) * sep;
            return View2E.of(central, LongRange.of(start, central, sep), LongRange.of(central, end, sep));
        }

        public View2E<LongRange, Long> view2Overlapping() {
            Long n = this.size();
            Preconditions.checkArgument(n > 1, String.format("La lista debe ser de tama�o mayor que 1 y es %d  ", n));
            Long central = (end + start) / (2 * sep) * sep;
            return View2E.of(central, LongRange.of(start, central + sep, sep), LongRange.of(central, end, sep));
        }
    }
}
