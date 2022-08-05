package com.fadda.basictypes;

import com.fadda.common.Preconditions;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public record LargeInteger(Boolean positive, List<Long> digits) implements Comparable<LargeInteger> {
    private static final Long base = 10L;


    public static LargeInteger of(String s) {
        List<Long> ls = IntStream.range(0, s.length()).boxed()
                .map(i -> (long) Character.getNumericValue(s.charAt(i)))
                .toList();
        return new LargeInteger(true, ls);
    }


    public static LargeInteger ofLong(Boolean neg, List<Long> ls) {
        return new LargeInteger(neg, new ArrayList<>(ls));
    }

    public static LargeInteger ofLong(List<Long> ls) {
        return new LargeInteger(true, new ArrayList<>(ls));
    }

    public static LargeInteger zero() {

        return new LargeInteger(true, List.of(0L));
    }

    public static LargeInteger one() {
        return new LargeInteger(true, List.of(1L));
    }

    public static LargeInteger ofInteger(List<Integer> ls) {

        return new LargeInteger(true, ls.stream().map(Integer::longValue).collect(Collectors.toList()));
    }

    public static LargeInteger sum(LargeInteger x, LargeInteger y) {
        if (Boolean.TRUE.equals(y.isZero())) return x;
        if (Boolean.TRUE.equals(x.isZero())) return y;
        Integer n1 = x.size();
        Integer n2 = y.size();
        if (n1 < n2) x = x.completeZerosLeft(n2);
        if (n1 > n2) y = y.completeZerosLeft(n1);
        int n = x.size();
        List<Long> r = new ArrayList<>();
        long ac = 0L;
        for (int i = n - 1; i >= 0; i--) {
            Long sm = ac + x.digit(i) + y.digit(i);
            Long d = sm % base;
            ac = sm / base;
            r.add(0, d);
        }
        r.add(0, ac);
        return LargeInteger.ofLong(r);
    }

    public static LargeInteger minus(LargeInteger x, LargeInteger y) {
        boolean positive = true;
        if (x.compareTo(y) < 0) {
            LargeInteger a = x;
            x = y;
            y = a;
            positive = false;
        }
        if (Boolean.TRUE.equals(y.isZero())) return LargeInteger.ofLong(positive, x.digits());
        Integer n1 = x.size();
        Integer n2 = y.size();
        if (n1 < n2) x = x.completeZerosLeft(n2);
        if (n1 > n2) y = y.completeZerosLeft(n1);
        int n = x.size();
        List<Long> r = new ArrayList<>();
        Long ac = 0L;
        for (int i = n - 1; i >= 0; i--) {
            long d;
            if (x.digit(i) >= (y.digit(i) + ac)) {
                d = x.digit(i) - (y.digit(i) + ac);
                ac = 0L;
            } else {
                d = LargeInteger.base + x.digit(i) - (y.digit(i) + ac);
                ac = 1L;
            }
            r.add(0, d);
        }
        return LargeInteger.ofLong(positive, r);
    }

    public static LargeInteger multiplyOneDigit(LargeInteger x, LargeInteger y) {
        Long sm = x.digit(0) * y.digit(0);
        Long d = sm % base;
        Long ac = sm / base;
        return LargeInteger.ofLong(List.of(ac, d));
    }

    public static LargeInteger multiply(LargeInteger x, LargeInteger y) {
        if (x.isZero() || y.isZero()) return LargeInteger.zero();
        Integer n1 = x.size();
        Integer n2 = y.size();
        if (n1 < n2) x = x.completeZerosLeft(n2);
        if (n1 > n2) y = y.completeZerosLeft(n1);
        int n = x.size();
        LargeInteger r;
        if (n == 1) r = LargeInteger.multiplyOneDigit(x, y);
        else {
            int k = n / 2;
            int m = n - k;
            LargeInteger x1 = LargeInteger.ofLong(x.digits().subList(0, k));
            LargeInteger x0 = LargeInteger.ofLong(x.digits().subList(k, n));
            LargeInteger y1 = LargeInteger.ofLong(y.digits().subList(0, k));
            LargeInteger y0 = LargeInteger.ofLong(y.digits().subList(k, n));
            LargeInteger z2 = x1.multiply(y1);
            LargeInteger z0 = x0.multiply(y0);
            LargeInteger z1 = x1.sum(x0).multiply(y1.sum(y0)).minus(z2.sum(z0));
            z2 = z2.addZerosRight(2 * m);
            z1 = z1.addZerosRight(m);
            r = z2.sum(z1).sum(z0);
        }
        return r;
    }

    public static LargeInteger pow(LargeInteger x, Integer n) {
        LargeInteger r = x;
        LargeInteger u = LargeInteger.one();
        while (n > 0) {
            if (n % 2 == 1) {
                u = u.multiply(r);
            }
            r = r.multiply(r);
            n = n / 2;
        }
        return u;
    }

    public Integer size() {
        return digits.size();
    }

    public Long digit(int i) {
        return this.digits().get(i);
    }

    public Boolean isZero() {
        return this.digits().stream().allMatch(e -> e.equals(0L));
    }


    public Boolean isOne() {
        LargeInteger z = this.removeZerosLeft();
        return z.positive() && z.digits().stream().allMatch(e -> e.equals(1L));
    }


    public LargeInteger completeZerosLeft(Integer n) {
        Preconditions.checkArgument(n > this.size(), "Demasiado largo");
        List<Long> r = new ArrayList<>(this.digits);
        for (int i = this.size(); i < n; i++) r.add(0, 0L);
        return LargeInteger.ofLong(r);
    }

    public LargeInteger removeZerosLeft() {
        List<Long> ls = this.digits().stream().dropWhile(e -> e.equals(0L)).collect(Collectors.toList());
        return LargeInteger.ofLong(ls);
    }

    public LargeInteger addZerosRight(Integer n) {
        List<Long> r = new ArrayList<>(this.digits);
        for (int i = 0; i < n; i++) r.add(0L);
        return LargeInteger.ofLong(r);
    }

    @Override
    public int compareTo(LargeInteger other) {
        return compare(this, other);
    }

    public int compare(LargeInteger x, LargeInteger y) {
        Integer n1 = x.size();
        Integer n2 = y.size();
        if (n1 < n2) x = x.completeZerosLeft(n2);
        if (n1 > n2) y = y.completeZerosLeft(n1);
        int n = x.size();
        int d = -1;
        for (int i = 0; i < n; i++) {
            if (!Objects.equals(x.digit(i), y.digit(i))) {
                d = i;
                break;
            }
        }
        int r;
        if (d == -1) r = 0;

        else r = x.digits().get(d) < y.digits().get(d) ? -1 : 1;
        return r;
    }

    public LargeInteger sum(LargeInteger e) {

        return LargeInteger.sum(this, e);
    }

    public LargeInteger minus(LargeInteger e) {
        return LargeInteger.minus(this, e);
    }

    public LargeInteger multiply(LargeInteger e) {
        return LargeInteger.multiply(this, e);
    }

    public LargeInteger pow(Integer n) {
        return LargeInteger.pow(this, n);
    }

    @Override
    public String toString() {
        LargeInteger r = this.removeZerosLeft();
        return (Boolean.TRUE.equals(r.positive) ? "" : "-") + r.digits.stream().map(Object::toString).collect(Collectors.joining(""));
    }


}
