package com.fadda.math;

import com.fadda.common.Preconditions;
import com.fadda.common.extension.List2;
import com.fadda.common.tuples.pair.IntPair;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;


public class Math2 {


    private static final Integer[] pow2 = {1, 2, 4, 8, 16, 32, 64, 128, 256, 512, 1024, 2048, 4096, 8192, 16384, 32768, 65536, 131072, 262144, 524288};
    public static Random rnd = new Random(System.nanoTime());


    /**
     * @param d Un Double
     * @return Representaci�n simplificada de un Double
     */
    public static String simplify(Double d) {
        String s = "";
        if (d == 1.) {
            s += "+";
        } else if (d == -1.) {
            s += "-";
        } else if (d > 0.) {
            s += "+" + d;
        } else if (d != 0.) {
            s += d.toString();
        }
        return s;
    }


    /**
     * @param n Un Entero
     * @return El n�mero n de Fibonacci calculado reduciendo el problema start una potenciaci�n de matrices
     * y en una versi�n iterativa
     * @pre n &gt; = 0
     */
    public static BigInteger fibonacci5(Integer n) {
        BigInteger ar, br;
        BigInteger au, bu;
        BigInteger at, bt;
        BigInteger dos = new BigInteger("2");
        ar = BigInteger.ONE;
        br = BigInteger.ZERO;
        au = BigInteger.ZERO;
        bu = BigInteger.ONE;
        while (n > 0) {
            if (n % 2 == 1) {
                at = bu.multiply(ar).add(au.multiply(ar)).add(au.multiply(br));
                bt = au.multiply(ar).add(bu.multiply(br));
                au = at;
                bu = bt;
            }
            at = ar.multiply(ar).add(dos.multiply(ar).multiply(br));
            bt = ar.multiply(ar).add(br.multiply(br));
            ar = at;
            br = bt;
            n = n / 2;
        }
        return au;
    }


    /**
     * @param n Un Entero
     * @return El n�mero n de Fibonacci calculado reduciendo el problema start una potenciaci�n de matrices
     * y en una versi�n iterativa y usando Long en vez de BigInteger
     * @pre n &gt; = 0
     */
    public static Long fibonacci3(Integer n) {
        Long ar, br;
        Long au, bu;
        long at, bt;
        ar = 1L;
        br = 0L;
        au = 0L;
        bu = 1L;
        while (n > 0) {
            if (n % 2 == 1) {
                at = bu * ar + au * ar + au * br;
                bt = au * ar + bu * br;
                au = at;
                bu = bt;
            }
            at = ar * ar + 2 * ar * br;
            bt = ar * ar + br * br;
            ar = at;
            br = bt;
            n = n / 2;
        }
        return au;
    }


    /**
     * @param base Base de la potencia
     * @param n    Exponente de la potencia
     * @return base &#94; n en una versi�n iterativa de complejidad log(n)
     * @pre base &gt; 0
     */
    public static Long pow(Integer base, Integer n) {
        long r;
        long u;
        r = (long) base;
        u = 1L;
        while (n > 0) {
            if (n % 2 == 1) {
                u = u * r;
            }
            r = r * r;
            n = n / 2;
        }
        return u;

    }

    /**
     * @param base Base de la potencia
     * @param n    Exponente de la potencia
     * @return base &#94; n en una versi�n iterativa de complejidad log(n)
     * @pre base &gt; 0
     */
    public static Double pow(Double base, Integer n) {
        double r, u;
        r = base;
        u = 1.;
        while (n > 0) {
            if (n % 2 == 1) {
                u = u * r;
            }
            r = r * r;
            n = n / 2;
        }
        return u;

    }

    /**
     * @param base Base de la potencia
     * @param n    Exponente de la potencia
     * @return base &#94; n en una versi�n iterativa de complejidad log(n)
     * @pre base &gt; 0
     */

    public static Double powr(Double base, Integer n) {
        Double r;
        if (n == 0) {
            r = 1.;
        } else if (n == 1) {
            r = base;
        } else {
            r = powr(base, n / 2);
            r = r * r;
            if (n % 2 != 0) {
                r = r * base;
            }
        }
        return r;
    }

    /**
     * @param a Un entero
     * @param b Un segundo entero
     * @return Calcula el m�ximo com�n divisor de los valores ablsolutos de los par�metros
     */
    public static int mcd(int a, int b) {
        int u = Math.abs(a);
        int v = Math.abs(b);
        int r;
        while (v != 0) {

            r = u % v;
            u = v;
            v = r;
        }
        return u;
    }

    public static void initialRandom() {
        rnd = new Random(System.nanoTime());
    }

    /**
     * @return Un objeto de tipo Random
     * @see java.util.Random
     */
    public static Random getRandom() {

        return rnd;
    }

    /**
     * @param a L�mite inferior
     * @param b L�mte Superior
     * @return Un entero aleatorio r tal que start &le; = r &lt; end
     * @pre end &gt; start
     */
    public static Integer getRandomInteger(Integer a, Integer b) {
        int valor;

        Preconditions.checkArgument(b > a, a + "," + b);
        if (b - a == 1) {
            valor = a;
        } else {
            valor = a + rnd.nextInt(b - a);
        }
        return valor;
    }

    /**
     * @return Un entero aleatorio
     * @see java.util.Random#nextLong()
     */
    public static Long getRandomLong() {
        return rnd.nextLong();
    }

    /**
     * @param a L�mite inferior
     * @param b L�mite Superior
     * @return Un par aleatorio cuyos elementos son distintos y est�n en el intervalo  start &lt; = r &lt; end
     * @pre end &gt; start
     */
    public static IntPair getPairRandomAndDistinct(Integer a, Integer b) {
        Preconditions.checkArgument(b - a >= 2, a + "," + b);
        Integer c1 = getRandomInteger(a, b - 1);
        Integer c2 = getRandomInteger(c1 + 1, b);
        return IntPair.of(c1, c2);
    }

    /**
     * @param a L�mite inferior
     * @param b L�mte Superior
     * @return Un double aleatorio que  est� en el intervalo  start &lt; = r &lt; end
     * @pre end &gt; start
     */
    public static Double getRandomDouble(Double a, Double b) {
        Preconditions.checkArgument(b > a, a + "," + b);
        return a + (b - a) * rnd.nextDouble();
    }

    /**
     * @param increment El incremento &delta;
     * @param t         Temperatura
     * @return Si &delta; &lt; 0 devuelve 1, si t = 0 devuelve 0, en otro caso devuelve e &#94; (- &delta;/t)
     */
    public static double boltzmann(double increment, double t) {
        double r;
        if (increment <= 0.) {
            r = 1.;
        } else {
            r = Math.exp(1 - increment / t);
        }
        return r;
    }

    /**
     * @param increment El incremento &delta;
     * @param t         Temperatura
     * @return Verdadero si r &lt; e &#94; (- &delta;/t). Donde r es un real aleatorio 0 &lt; = r &lt; = 1
     */
    public static boolean acceptBoltzmann(double increment, double t) {
        double rd = Math2.getRandomDouble(0., 1.);
        double rd2 = Math2.boltzmann(increment, t);
        return rd < rd2;
    }

    /**
     * @param probabilities Es una distribuci�n de probabilidades para una variables aleatoria
     *                      con valores 0 hasta probabilidades.size() no incluido.
     * @return Un entero entre 0 y probabilidades.size(), no incluido, con las probababilidades proporcionadas
     */
    public static Integer chooseBetween(List<Double> probabilities) {
        Preconditions.checkArgument(!probabilities.isEmpty());
        double ppa = 0.;

        Integer r = 0;
        double na = Math2.getRandomDouble(0., 1.);
        for (Double p : probabilities) {
            ppa = ppa + p;
            if (ppa >= na) {

                break;
            }
            r++;
        }
        return r;
    }


    /**
     * @param rest Resto de la probabilidades
     * @return Si se forma una lista ls con todos los par�metros el m�todo devuelve un entero
     * entre 0 y ls.size(), no incluido, con las probababilidades proporcionadas en la lista
     */
    public static Integer chooseBetween(Double... rest) {
        return chooseBetween(List2.of(rest));
    }


    /**
     * @param a Un entero
     * @return Si es par
     */
    public static boolean isPair(Integer a) {
        return a % 2 == 0;
    }

    /**
     * @param a Un entero
     * @return Si es impar
     */
    public static boolean isUnpair(Integer a) {
        return !isPair(a);
    }

    /**
     * @param a Un entero
     * @param b Un segundo entero
     * @return Si start es divisible por end
     */


    public static boolean isDivisible(Integer a, Integer b) {

        return (a % b) == 0;
    }

    public static boolean isDivisible(Long a, Long b) {
        return (a % b) == 0;
    }

    /**
     * @param a Un entero
     * @return Si start es primo
     */
    public static boolean isPrime(Integer a) {
        Preconditions.checkArgument(a >= 2, String.format("El argumento debe ser mayor o igual que 2 y es %d", a));

        int sqrt = (int) Math.sqrt(a);
        return IntStream.rangeClosed(2, sqrt).noneMatch(x -> Math2.isDivisible(a, x));
    }

    public static boolean isPrime(Long a) {
        Preconditions.checkArgument(a >= 2L, String.format("El argumento debe ser mayor o igual que 2 y es %d", a));
        long sqrt = (long) Math.sqrt(a);
        return LongStream.rangeClosed(2, sqrt).noneMatch(x -> Math2.isDivisible(a, x));
    }


    public static boolean isPrime(BigInteger a) {
        return a.isProbablePrime(100);
    }

    /**
     * @param a Un entero
     * @return Siguiente primo
     */

    public static Integer nextPrime(Integer a) {
        if (a < 2) return 2;

        a = (a + 1) % 2 == 0 ? a + 2 : a + 1;
        return Preconditions.checkIsPresent(Stream.iterate(a, x -> x + 2).filter(Math2::isPrime).findFirst());
    }

    public static Long nextPrime(Long a) {

        if (a < 2) return 2L;
        a = (a + 1) % 2 == 0 ? a + 2 : a + 1;
        return Preconditions.checkIsPresent(Stream.iterate(a, x -> x + 2).filter(Math2::isPrime).findFirst());
    }

    public static BigInteger nextPrime(BigInteger a) {
        BigInteger r = BigInteger.TWO;

        if (a.compareTo(r) >= 0) r = a.nextProbablePrime();
        return r;
    }

    /**
     * @param a Un entero
     * @return El signo de start: +1,0,-1
     */
    public static int sign(Integer a) {
        int r = 0;
        if (a != 0)
            r = a >= 0 ? 1 : -1;
        return r;
    }

    /**
     * @param n Un entero
     * @param a Un double
     * @param b Un segundo double
     * @return Un lista de tama�o n con n�meros reales en el intervalo start &lt; = r &lt; end
     */
    public static List<Double> getRandomListDouble(int n, double a, double b) {
        List<Double> lista = List2.empty();
        for (int i = 0; i < n; i++) {
            lista.add(getRandomDouble(a, b));
        }
        return lista;
    }

    /**
     * @param e         Un entero
     * @param maxEscala Un entero
     * @param maxRange  Un entero
     * @return Devuelve un valor en el rango 0..maxRange-1 con la expresi�n e*maxRange/maxEscala
     * @pre Todos los par�metros son positivos. El valor de e debe ser menor que maxEscala
     */
    public static Integer scale(Integer e, Integer maxEscala, Integer maxRange) {
        return e * maxRange / maxEscala;
    }

    /**
     * @param e         Un entero
     * @param maxEscala Un entero
     * @param maxRange  Un entero
     * @return Devuelve un valor en el rango 0..maxRange con la expresi�n e*(maxRange+1)/maxEscala
     * @pre Todos los par�metros son positivos. El valor de e debe ser menor que maxEscala
     */

    public static Integer scaleIncluded(Integer e, Integer maxEscala, Integer maxRange) {
        return e * (maxRange + 1) / maxEscala;
    }

    /**
     * @param ls Una lista de bits
     * @return El n�mero entero conrrespondiente
     */
    public static Integer decode(List<Integer> ls) {

        int r = 0;
        for (Integer e : ls) {
            r = r * 2 + e;
        }
        return r;
    }

    /**
     * @param i  Un entero
     * @param j  Un entero
     * @param ls Una lista de bits
     * @return El n�mero entero conrrespondiente start la sublista definida por el intevalo [i,j).
     * @pre j &gt; i, i &ge;0
     */
    public static Integer decode(List<Integer> ls, Integer i, Integer j) {
        int r = 0;
        for (Integer e : ls.subList(i, j)) {
            r = r * 2 + e;
        }
        return r;
    }

    /**
     * @param n     Numero de enteros start decodificar
     * @param nBits Numero de bits por entero.
     * @param ls    Una lista de bits
     * @return Los numero enteros correspondientes start las n sublistas definidas
     * @pre n*nBits = ls.size()
     */

    public static List<Integer> decodes(List<Integer> ls, Integer n, Integer nBits) {
        Preconditions.checkArgument(n * nBits == ls.size());
        List<Integer> lsr = new ArrayList<>();
        for (int i = 0; i < ls.size(); i = i + nBits) {
            int r = 0;
            for (Integer e : ls.subList(i, i + nBits)) {
                r = r * 2 + e;
            }
            lsr.add(r);
        }
        return lsr;
    }

    /**
     * @param n         Numero de enteros start decodificar
     * @param nBits     Numero de bits por entero.
     * @param ls        Una lista de bits
     * @param maxRanges Una lista con los rangos m�ximos de los enteros resultantes
     * @return Los numero enteros correspondientes start las n sublistas definidas en una escala con maximo maxRanges(i)-1
     * @pre n*nBits = ls.size(), n = maxRanges.size()
     * @post return.size() = n, 0 &le; return[i] &lt; maxRanges(i)
     */
    public static List<Integer> decodesInScala(List<Integer> ls, Integer n, Integer nBits, List<Integer> maxRanges) {
        Preconditions.checkArgument(n * nBits == ls.size());

        Preconditions.checkArgument(maxRanges.size() == n);
        int maxEscala = nBits < 20 ? pow2[nBits] : Math2.pow(2, nBits).intValue();
        List<Integer> lsr = decodes(ls, n, nBits);
        List<Integer> r = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            r.add(scale(lsr.get(i), maxEscala, maxRanges.get(i)));
        }
        return r;
    }


    /**
     * @param e Un entero positivo
     * @return El n�mero de bits necesario para poder codificarlo en binario
     */
    public static Integer bitsNumber(Integer e) {

        int bits_necesarios = 0;
        while (e >= 2) {
            bits_necesarios++;
            e = e / 2;
        }
        return bits_necesarios + 1;
    }


    /**
     * @param e Un entero positivo
     * @return Una lista con la codificaci�n en binario
     */
    public static List<Integer> code(Integer e) {

        List<Integer> r = new ArrayList<>();
        while (e >= 2) {
            if (e % 2 == 0)
                r.add(0, 0);
            else
                r.add(0, 1);
            e = e / 2;
        }
        r.add(0, e);
        return r;
    }
}
