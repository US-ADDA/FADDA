package com.fadda.common.extension;

import com.fadda.common.tuples.pair.IntPair;

import java.util.Arrays;
import java.util.function.IntPredicate;
import java.util.stream.IntStream;

public class Arrays2 {

    private Arrays2() {
    }


    public static double[] getArrayDouble(int n, double v) {
        double[] r = new double[n];
        Arrays.fill(r, v);
        return r;
    }


    public static Integer[] copyArray(Integer[] d) {
        int n = d.length;
        Integer[] r = new Integer[n];
        IntStream.range(0, n).boxed().forEach(i -> r[i] = d[i]);
        return r;
    }


    public static Double[] copyArray(Double[] d) {
        int n = d.length;
        Double[] r = new Double[n];
        IntStream.range(0, n).boxed().forEach(i -> r[i] = d[i]);
        return r;
    }


    /**
     * @param d Data
     * @param n N�mero de filas
     * @param m N�mero de columnas
     * @return El array bidimensional
     */
    public static Integer[][] toMultiArray(Integer[] d, Integer n, Integer m) {

        Integer[][] r = new Integer[n][m];
        IntStream.range(0, n).boxed()
                .flatMap(f -> IntStream.range(0, m).boxed().map(c -> IntPair.of(f, c)))
                .forEach(p -> r[p.first()][p.second()] = d[p.first() * n + p.second()]);
        return r;
    }

    public static IntPair findPosition(Integer[][] d, IntPredicate pd) {
        int n = d.length;
        int m = d[0].length;


        return IntStream.range(0, n).boxed()
                .flatMap(f -> IntStream.range(0, m).boxed().map(c -> IntPair.of(f, c)))
                .filter(p -> pd.test(d[p.first()][p.second()]))
                .findFirst()
                .orElse(null);
    }

    public static Integer[][] copyArray(Integer[][] d) {
        int n = d.length;
        int m = d[0].length;
        Integer[][] r = new Integer[n][m];

        IntStream.range(0, n).boxed()

                .flatMap(f -> IntStream.range(0, m).boxed().map(c -> IntPair.of(f, c)))
                .forEach(p -> r[p.first()][p.second()] = d[p.first()][p.second()]);
        return r;
    }

    public static Double[][] copyArray(Double[][] d) {
        int n = d.length;
        int m = d[0].length;
        Double[][] r = new Double[n][m];
        IntStream.range(0, n).boxed()
                .flatMap(f -> IntStream.range(0, m).boxed().map(c -> IntPair.of(f, c)))

                .forEach(p -> r[p.first()][p.second()] = d[p.first()][p.second()]);
        return r;
    }
}
