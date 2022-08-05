package com.fadda.common.extension;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;


/**
 * Un Comparator con m�s m�todos
 *
 * @author Miguel Toro
 */
public interface Comparator2 {


    static <T extends Comparable<? super T>> Type compare(T e1, T e2) {
        Type r;
        if (e1.compareTo(e2) == 0) {
            r = Type.EQ;
        } else if (e1.compareTo(e2) < 0) {
            r = Type.LT;

        } else {
            r = Type.GT;
        }
        return r;
    }


    static <T extends Comparable<? super T>> boolean isGT(T e1, T e2) {
        return e1.compareTo(e2) > 0;

    }

    static <T extends Comparable<? super T>> boolean isGE(T e1, T e2) {

        return e1.compareTo(e2) >= 0;
    }

    static <T extends Comparable<? super T>> boolean isLT(T e1, T e2) {

        return e1.compareTo(e2) < 0;
    }

    static <T extends Comparable<? super T>> boolean isLE(T e1, T e2) {


        return e1.compareTo(e2) <= 0;
    }


    static <T extends Comparable<? super T>> boolean isGTNull(T e1, T e2) {
        if (e2 == null) return true;
        return e1.compareTo(e2) > 0;
    }

    static <T extends Comparable<? super T>> boolean isGENull(T e1, T e2) {


        if (e2 == null) return true;
        return e1.compareTo(e2) >= 0;
    }


    static <T extends Comparable<? super T>> boolean isLTNull(T e1, T e2) {
        if (e2 == null) return true;
        return e1.compareTo(e2) < 0;
    }

    static <T extends Comparable<? super T>> boolean isLENull(T e1, T e2) {

        if (e2 == null) return true;
        return e1.compareTo(e2) <= 0;
    }

    static <T> Type compare(T e1, T e2, Comparator<? super T> cmp) {
        Type r;
        if (cmp.compare(e1, e2) == 0) {
            r = Type.EQ;

        } else if (cmp.compare(e1, e2) < 0) {
            r = Type.LT;
        } else {
            r = Type.GT;
        }
        return r;
    }


    static <T> boolean isGT(T e1, T e2, Comparator<? super T> cmp) {
        return cmp.compare(e1, e2) > 0;
    }


    static <T> boolean isGE(T e1, T e2, Comparator<? super T> cmp) {

        return cmp.compare(e1, e2) >= 0;


    }


    static <T> boolean isLT(T e1, T e2, Comparator<? super T> cmp) {

        return cmp.compare(e1, e2) < 0;


    }

    static <T> boolean isLE(T e1, T e2, Comparator<? super T> cmp) {


        return cmp.compare(e1, e2) <= 0;

    }


    static <T> boolean isEQ(T e1, T e2, Comparator<? super T> cmp) {

        return cmp.compare(e1, e2) == 0;
    }

    static <T> boolean isGTNull(T e1, T e2, Comparator<? super T> cmp) {

        if (e2 == null) return true;
        return cmp.compare(e1, e2) > 0;
    }

    static <T> boolean isGENull(T e1, T e2, Comparator<? super T> cmp) {
        if (e2 == null) return true;
        return cmp.compare(e1, e2) >= 0;
    }

    static <T> boolean isLTNull(T e1, T e2, Comparator<? super T> cmp) {
        if (e2 == null) return true;

        return cmp.compare(e1, e2) < 0;
    }


    static <T> boolean isLENull(T e1, T e2, Comparator<? super T> cmp) {

        if (e2 == null) return true;
        return cmp.compare(e1, e2) <= 0;
    }

    static <T> boolean isEQNull(T e1, T e2, Comparator<? super T> cmp) {

        if (e2 == null) return false;
        return cmp.compare(e1, e2) == 0;
    }

    static <T extends Comparable<? super T>> T max(T e1, T e2) {
        return isGE(e1, e2) ? e1 : e2;
    }

    static <T> T max(T e1, T e2, Comparator<? super T> cmp) {
        return isGE(e1, e2, cmp) ? e1 : e2;
    }


    @SuppressWarnings("unchecked")

    static <T extends Comparable<? super T>> T max(T... elements) {


        return Arrays.stream(elements).max(Comparator.naturalOrder()).get();
    }

    @SuppressWarnings("unchecked")
    static <T> T max(Comparator<? super T> cmp, T... elements) {


        return Arrays.stream(elements).max(cmp).get();
    }

    static <T extends Comparable<? super T>> T min(T e1, T e2) {
        return isLE(e1, e2) ? e1 : e2;
    }


    static <T> T min(T e1, T e2, Comparator<? super T> cmp) {
        return isLE(e1, e2, cmp) ? e1 : e2;
    }

    @SuppressWarnings("unchecked")
    static <T extends Comparable<? super T>> T min(T... elements) {

        return Arrays.stream(elements).min(Comparator.naturalOrder()).get();
    }

    @SafeVarargs
    static <T> T min(Comparator<? super T> cmp, T... elements) {
        return Arrays.stream(elements).min(cmp).get();
    }

    static <T> boolean isInOpenInterval(T e, T e1, T e2, Comparator<? super T> cmp) {
        return cmp.compare(e, e1) > 0 && cmp.compare(e, e2) < 0;
    }

    static <T extends Comparable<? super T>> boolean isInOpenInterval(T e, T e1, T e2) {
        return e.compareTo(e1) > 0 && e.compareTo(e2) < 0;
    }

    static <T> boolean isInClosedInterval(T e, T e1, T e2, Comparator<? super T> cmp) {
        return cmp.compare(e, e1) >= 0 && cmp.compare(e, e2) <= 0;
    }

    static <T extends Comparable<? super T>> boolean isInClosedInterval(T e, T e1, T e2) {
        return e.compareTo(e1) >= 0 && e.compareTo(e2) <= 0;
    }

    static <T> boolean isOrdered(List<T> ls, Comparator<? super T> cmp) {
        boolean r = true;
        for (int j = 0; j < ls.size() - 1; j++) {
            r = Comparator2.isLE(ls.get(j), ls.get(j + 1), cmp);
            if (!r) break;
        }
        return r;
    }

    static <T extends Comparable<? super T>> boolean isOrdered(List<T> ls) {
        return isOrdered(ls, Comparator.naturalOrder());
    }

    static <E> List<E> sortedCopy(List<E> lista, Comparator<E> comparator) {
        return lista.stream().sorted(comparator).toList();
    }

    enum Type {EQ, LT, GT}

}
