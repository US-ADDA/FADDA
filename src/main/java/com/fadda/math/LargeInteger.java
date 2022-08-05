package com.fadda.math;


import com.fadda.common.Preconditions;
import com.fadda.common.extension.List2;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * La clase implementa un entero largo.
 * Los detalles del tipo pueden encontrarse
 * en <start href="/document/LargeInteger.html" target="_blank">LargeInteger</start>
 */
public class LargeInteger implements Comparable<LargeInteger> {


    public static final LargeInteger ZERO = LargeInteger.create(List2.of(0));
    public static final LargeInteger ONE = LargeInteger.create(List2.of(1));
    public static final LargeInteger TWO = LargeInteger.create(List2.of(2));
    public static final LargeInteger TEN = LargeInteger.create(List2.of(10));
    /**
     * La base usada
     */
    private static final Long radix = 2147483647L;
    /**
     * Los d�gitos est�n ordenados de m�s significativo start menos significativos
     */
    private final List<Integer> digits;
    /**
     * El signo del entero grande
     */
    private final Boolean isPositive;

    private LargeInteger(List<Integer> digits) {
        this(List2.ofCollection(digits), true);
    }

    private LargeInteger(Integer number) {
        this(List2.of(number), true);
    }

    private LargeInteger(List<Integer> digits, Boolean isPositive) {
        super();
        Preconditions.checkArgument(!digits.isEmpty(), "Debe haber un d�gito al menos");
        Preconditions.checkArgument(digits.stream().allMatch(x -> x >= 0), "Los digitos deben se positivos o cero");
        if (digits.size() == 1 && digits.get(0).equals(0)) isPositive = true;
        this.digits = List2.ofCollection(digits);
        this.isPositive = isPositive;
    }


    public static LargeInteger create(String digits) {
        boolean isPositive = true;
        if (digits.charAt(0) == '-') {
            digits = digits.substring(1);
            isPositive = false;
        }
        List<Integer> dg = digits.chars().map(x -> x - '0').boxed().collect(Collectors.toList());
        LargeInteger r = eval(dg);
        if (isPositive) return r;
        else return r.negate();
    }


    public static LargeInteger create(List<Integer> digits) {
        return new LargeInteger(digits);
    }

    public static LargeInteger create(Integer number) {
        return new LargeInteger(number);
    }


    public static LargeInteger create(Long number) {
        Integer[] r = {(int) (number / radix), (int) (number % radix)};
        return LargeInteger.create(Arrays.asList(r));
    }


    public static LargeInteger create(List<Integer> digits, Boolean isPositive) {
        return new LargeInteger(digits, isPositive);
    }


    public static LargeInteger create(LargeInteger m) {
        return new LargeInteger(m.digits, m.isPositive);
    }


    private static LargeInteger eval(List<Integer> n) {
        LargeInteger a = ZERO;
        LargeInteger b = ONE;
        for (int i = n.size() - 1; i >= 0; i--) {
            a = a.add(b.multiply(create(n.get(i))));
            b = b.multiply(TEN);
        }
        return a;
    }

    /**
     * @param a Un entero largo
     * @param m El nuevo n�mero de d�gitos
     * @return Un nuevo entero largo completado con d�gitos no significativos start la izquierda
     */

    public static LargeInteger completeWithLeftZeros(LargeInteger a, int m) {
        int n = m - a.size();
        List<Integer> nDigits = List2.of(n, 0);
        nDigits.addAll(a.digits);
        return LargeInteger.create(nDigits, a.isPositive);
    }

    public static int compare(LargeInteger a, LargeInteger b) {
        if (!a.isNegative() && b.isNegative()) return 1;
        if (a.isNegative() && !b.isNegative()) return -1;
        int r = 1;
        if (a.isNegative() && b.isNegative()) {
            r = -1;
        }
        int mx = Math.max(a.size(), b.size());
        a = completeWithLeftZeros(a, mx);
        b = completeWithLeftZeros(b, mx);
        int k = a.size();
        long ac = 0L;
        for (int i = 0; i < k; i++) {
            ac = ac + a.getDigit(i) - b.getDigit(i);
            if (ac != 0) break;
        }
        return r * (int) ac;
    }

    private static Integer[] addDigit(Integer a, Integer b) {
        Long ab = a.longValue() + b.longValue();
        return new Integer[]{(int) (ab / radix), (int) (ab % radix)};
    }

    private static Integer[] minusDigit(Integer a, Integer b) {
        int ab = a - b;
        int r = 0;
        if (ab < 0) {
            ab = (int) (radix - b + a);
            r = -1;
        }
        return new Integer[]{r, ab};
    }

    private static Integer[] multiplyDigit(Integer a, Integer b) {
        Long ab = a.longValue() * b.longValue();
        return new Integer[]{(int) (ab / radix), (int) (ab % radix)};
    }

    private static Integer[] divideDigit(Integer[] a, Integer b) {
        Preconditions.checkArgument(a.length == 2);
        Long n = a[0].longValue() * radix + a[1];
        return new Integer[]{(int) (n / b), (int) (n % b)};
    }

    public static LargeInteger add(LargeInteger a, LargeInteger b) {
        boolean aPositive = true;
        boolean bPositive = true;
        if (a.isNegative()) {
            a = a.abs();
            aPositive = false;
        }
        if (b.isNegative()) {
            b = b.abs();
            bPositive = false;
        }
        if (aPositive && !bPositive)
            return minus(a, b);
        else if (!aPositive && !bPositive)
            return add(a, b).negate();
        else if (!aPositive)
            return minus(b, a);

        int mx = Math.max(a.size(), b.size());
        a = completeWithLeftZeros(a, mx);
        b = completeWithLeftZeros(b, mx);
        int i = a.size() - 1;
        int q = 0;
        List<Integer> digits = List2.empty();
        while (i >= 0) {
            Integer[] sd = addDigit(a.getDigit(i), q);
            q = sd[0];
            sd = addDigit(sd[1], b.getDigit(i));
            q = q + sd[0];
            digits.add(0, sd[1]);
            i--;
        }
        digits.add(0, q);
        return LargeInteger.create(digits).deleteNonSignificantZeros();
    }

    public static LargeInteger minus(LargeInteger a, LargeInteger b) {
        int mx = Math.max(a.size(), b.size());
        a = completeWithLeftZeros(a, mx);
        b = completeWithLeftZeros(b, mx);
        boolean positive = true;
        int cmp = compare(a, b);
        if (cmp == 0) return ZERO;
        if (cmp < 0) {
            LargeInteger c = b;
            b = a;
            a = c;
            positive = false;
        }
        int i = a.size() - 1;
        int q = 0;
        List<Integer> digits = List2.empty();
        while (i >= 0) {
            Integer[] sd = addDigit(a.getDigit(i), q);
            sd = minusDigit(sd[1], b.getDigit(i));
            q = sd[0];
            digits.add(0, sd[1]);
            i--;
        }
        LargeInteger r = LargeInteger.create(digits).deleteNonSignificantZeros();
        if (positive) return r;
        else return r.negate();
    }

    public static LargeInteger multiply(LargeInteger m, LargeInteger n) {
        boolean aPositive = true;
        boolean bPositive = true;
        boolean rPositive = true;
        if (m.isNegative()) {
            m = m.abs();
            aPositive = false;
        }
        if (n.isNegative()) {
            n = n.abs();
            bPositive = false;
        }
        if ((aPositive && !bPositive) || (!aPositive && bPositive))
            rPositive = false;
        int mx = Math.max(m.size(), n.size());
        LargeInteger r;
        if (mx == 1) {
            Integer[] r0 = LargeInteger.multiplyDigit(m.getDigit(0), n.getDigit(0));
            r = LargeInteger.create(Arrays.asList(r0));
        } else {
            if (mx % 2 == 1) mx = mx + 1;
            m = completeWithLeftZeros(m, mx);
            n = completeWithLeftZeros(n, mx);
            int k = mx / 2;
            LargeInteger m1 = LargeInteger.create(m.digits.subList(0, k));
            LargeInteger m0 = LargeInteger.create(m.digits.subList(k, mx));
            LargeInteger n1 = LargeInteger.create(n.digits.subList(0, k));
            LargeInteger n0 = LargeInteger.create(n.digits.subList(k, mx));
            LargeInteger z2 = m1.multiply(n1);
            LargeInteger z1 = m1.multiply(n0).add(m0.multiply(n1));
            LargeInteger z0 = m0.multiply(n0);
            r = z2.multiplyRadixToM(2 * k).add(z1.multiplyRadixToM(k)).add(z0);
        }

        if (rPositive) return r.deleteNonSignificantZeros();
        else return r.negate().deleteNonSignificantZeros();
    }

    private static LargeInteger[] divide(LargeInteger a, Integer b) {
        LargeInteger[] zr1 = {ZERO, a};
        LargeInteger[] zr2 = {a, ZERO};
        if (a.equals(ZERO)) return zr1;
        if (b.equals(1)) return zr2;
        boolean aPositive = true;
        boolean bPositive = true;
        if (a.isNegative()) {
            a = a.abs();
            aPositive = false;
        }
        if (b < 0) {
            b = Math.abs(b);
            bPositive = false;
        }
        boolean sigr = (!aPositive || bPositive) && (aPositive || !bPositive);
        int k = a.size();
        List<Integer> digits = List2.empty();
        Integer[] cp = {a.getDigit(0), 0};
        for (int i = 0; i < k; i++) {
            Integer[] s = {cp[1], a.getDigit(i)};
            cp = divideDigit(s, b);
            digits.add(cp[0]);
        }
        LargeInteger c;
        if (sigr) c = LargeInteger.create(digits).deleteNonSignificantZeros();
        else c = LargeInteger.create(digits).deleteNonSignificantZeros().negate();
        return new LargeInteger[]{c, create(cp[1])};
    }

    public static LargeInteger[] divide(LargeInteger num, LargeInteger den) {
        boolean numPositive = true;
        boolean denPositive = true;
        if (num.isNegative()) {
            num = num.abs();
            numPositive = false;
        }
        if (den.isNegative()) {
            den = den.abs();
            denPositive = false;
        }
        boolean sigr = (!numPositive || denPositive) && (numPositive || !denPositive);

        if (compare(num, den) < 0) {
            return new LargeInteger[]{ZERO, den};
        }

        int kd = den.size();
        Integer denD = den.getDigit(0);
        LargeInteger numD = num.divideRadixToM(kd - 1);
        LargeInteger q = divide(numD, denD)[0];
        LargeInteger r = add(den, ONE);
        LargeInteger s;
        LargeInteger t = TWO;
        while (compare(r.abs(), den) >= 0 && compare(t.abs(), ONE) > 0) {
            r = num.minus(q.multiply(den));
            s = divide(r.divideRadixToM(kd - 1), denD)[0];
            t = divide(s, 2)[0];
            q = add(q, t);
        }

        r = minus(num, multiply(q, den));
        while (r.isNegative()) {
            q = q.minus(ONE);


            r = r.add(den);
        }
        q = LargeInteger.create(q.digits, sigr);
        return new LargeInteger[]{q, r};
    }

    public int size() {
        return this.digits.size();
    }

    public Integer getDigit(int i) {

        return this.digits.get(i);
    }

    public List<Integer> getDigits() {
        return List2.ofCollection(this.digits);
    }

    public boolean isNegative() {
        return !this.isPositive;
    }

    /**
     * @return Multiplica por la base start�adiendo un cero start la derecha
     */
    public LargeInteger multiplyRadix() {
        List<Integer> r = List2.ofCollection(this.digits);
        r.add(0);
        return LargeInteger.create(r, this.isPositive);
    }


    public LargeInteger completeWithLeftZeros(int m) {
        return LargeInteger.completeWithLeftZeros(this, m);
    }


    /**
     * @param m Un entero
     * @return Multiplica por la base elevada start m start�adiendo m ceros start las derecha
     */
    public LargeInteger multiplyRadixToM(int m) {
        List<Integer> r = List2.ofCollection(this.digits);
        List<Integer> nDigits = List2.of(m, 0);
        r.addAll(nDigits);
        return LargeInteger.create(r, this.isPositive);
    }

    /**
     * @return Elimina los ceros no significativos de la izquierda
     */
    public LargeInteger deleteNonSignificantZeros() {
        int m = 0;
        for (int i = 0; i < this.size(); i++) {
            if (!this.digits.get(i).equals(0)) {
                m = i;
                break;
            }
        }
        List<Integer> nDigits = this.digits.subList(m, this.size());
        return LargeInteger.create(nDigits, this.isPositive);
    }

    public LargeInteger divideRadixToM(int m) {
        int n = this.size() - m;
        List<Integer> nDigits = this.digits.subList(0, n);
        return LargeInteger.create(nDigits, this.isPositive);

    }

    public LargeInteger abs() {
        return LargeInteger.create(this.digits);

    }

    public LargeInteger negate() {
        boolean sig;
        sig = !Boolean.TRUE.equals(this.isPositive);
        return LargeInteger.create(this.digits, sig);
    }

    @Override

    public int compareTo(LargeInteger object) {
        return compare(this, object);
    }

    public LargeInteger add(LargeInteger b) {

        return add(this, b);
    }


    public LargeInteger add(Integer b) {
        return add(this, create(b));
    }


    public LargeInteger minus(LargeInteger b) {
        return minus(this, b);
    }

    public LargeInteger minus(Integer b) {
        return minus(this, create(b));
    }

    public LargeInteger multiply(LargeInteger b) {
        return multiply(this, b);
    }

    public LargeInteger[] divide(LargeInteger den) {
        return divide(this, den);
    }

    public LargeInteger[] divide(Integer den) {
        return divide(this, den);
    }

    @Override
    public String toString() {
        String r = Boolean.TRUE.equals(isPositive) ? "" : "-";
        LargeInteger n = this;
        StringBuilder rst = new StringBuilder();
        while (n.compareTo(TEN) >= 0) {
            LargeInteger[] cr = n.divide(10);
            n = cr[0];
            rst.insert(0, cr[1].getDigit(0));
        }
        rst.insert(0, n.getDigit(0));
        return r + rst;
    }
}
