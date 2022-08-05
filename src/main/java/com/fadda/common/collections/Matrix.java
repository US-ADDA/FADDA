package com.fadda.common.collections;

import com.fadda.common.Preconditions;
import com.fadda.common.tuples.pair.IntPair;
import com.fadda.common.views.View2;
import com.fadda.common.views.View4;
import com.fadda.streams.Stream2;

import java.lang.reflect.Array;
import java.util.List;
import java.util.Random;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;


/**
 * @param <E> El tipo de los elementos de la matriz que deben ser elementos de un campo num�rico
 * @author migueltoro
 */
public class Matrix<E> {


    public final E[][] datos;
    protected int nf;
    protected int nc;
    protected int fv;
    protected int cv;

    protected Matrix(E[][] datos) {
        super();
        this.datos = datos;
        this.nf = datos.length;
        this.nc = datos[0].length;
        this.fv = 0;
        this.cv = 0;
    }

    private Matrix(E[][] datos, Integer nf, Integer nc, Integer fv, Integer cv) {
        super();
        this.datos = datos;
        Preconditions.checkArgument(nf >= 0 && nc >= 0, String.format("Numero de filas menor que cero nf = %d, nc = %d", nf, nc));
        this.nf = nf;
        this.nc = nc;
        this.fv = fv;
        this.cv = cv;
    }


    public static <E> Matrix<E> of(E[] datos, Integer nf, Integer nc) {
        Preconditions.checkArgument(nf >= 0 && nc >= 0, String.format("Numero de filas menor que cero nf = %d, nc = %d", nf, nc));
        Preconditions.checkArgument(datos.length == nf * nc, "Data no v�lidos");
        @SuppressWarnings("unchecked") E[][] dd = (E[][]) Array.newInstance(datos[0].getClass(), nf, nc);
        for (int f = 0; f < nf; f++) {
            for (int c = 0; c < nc; c++) {
                int i = f * nc + c;
                dd[f][c] = datos[i];
            }
        }
        return Matrix.of(dd);
    }


    public static <E> Matrix<E> of(E[][] datos) {
        return new Matrix<>(datos);
    }


    public static <E> Matrix<E> empty() {
        return new Matrix<>(null, 0, 0, 0, 0);
    }


    public static <E> Matrix<E> of(Integer nf, Integer nc, E value) {
        Preconditions.checkArgument(nf >= 0 && nc >= 0, String.format("Numero de filas menor que cero nf = %d, nc = %d", nf, nc));

        @SuppressWarnings("unchecked") E[][] datos = (E[][]) Array.newInstance(value.getClass(), nf, nc);
        Matrix<E> r = Matrix.of(datos);
        for (int i = 0; i < nf; i++) {
            for (int j = 0; j < nc; j++)
                r.set(i, j, value);
        }
        return r;
    }

    public static Matrix<Integer> random(Integer nf, Integer nc, Integer a, Integer b) {
        Preconditions.checkArgument(nf >= 0 && nc >= 0, String.format("Numero de filas menor que cero nf = %d, nc = %d", nf, nc));
        Integer[][] datos = new Integer[nf][nc];


        Matrix<Integer> r = Matrix.of(datos);
        Random rr = new Random(System.nanoTime());
        for (int i = 0; i < nf; i++) {
            for (int j = 0; j < nc; j++)
                r.set(i, j, a + rr.nextInt(b - a));
        }
        return r;

    }

    public static <E> Matrix<E> of(Integer nf, E value) {
        Preconditions.checkArgument(nf >= 0, String.format("Numero de filas menor que cero nf = %d", nf));
        @SuppressWarnings("unchecked") E[][] datos = (E[][]) Array.newInstance(value.getClass(), nf, nf);

        Matrix<E> r = Matrix.of(datos);
        for (int i = 0; i < nf; i++) {
            for (int j = 0; j < nf; j++)
                r.set(i, j, value);
        }
        return r;
    }


    public static <E> Integer area(Matrix<E> m) {
        return m.area();
    }


    public static <E> Stream<Matrix<E>> allSubMatrix(Matrix<E> m) {
        return Stream2.allQuartets(0, m.numRows(), 0, m.numColumns(), 1, m.numRows(), 1, m.numColumns()).filter(q -> q.third() <= m.numRows() - q.first() && q.fourth() <= m.numColumns() - q.second()).map(q -> m.view(q.first(), q.second(), q.third(), q.fourth()));
    }

    public static <E> Boolean allElements(Matrix<E> m, Predicate<E> pd) {
        return Stream2.allPairs(m.numRows(), m.numColumns()).map(m::get).allMatch(pd);
    }


    public static <E> Boolean anyElements(Matrix<E> m, Predicate<E> pd) {
        return Stream2.allPairs(m.numRows(), m.numColumns()).map(m::get).anyMatch(pd);
    }

    public static <E> void copy(Matrix<E> out, Matrix<E> in) {
        boolean ch = in.nc == out.nc && in.nf == out.nf;
        Preconditions.checkArgument(ch, "No se cumplen condiciones de copia");
        for (int i = 0; i < in.nf; i++) {


            for (int j = 0; j < in.nc; j++) {

                out.set(i, j, in.get(i, j));
            }
        }
    }

    public static <E> Matrix<E> compose(Matrix<E> a, Matrix<E> b, Matrix<E> c, Matrix<E> d) {
        int nf = a.nf + c.nf;
        int nc = a.nc + b.nc;
        E e = a.get(0, 0);
        Matrix<E> r = Matrix.of(nf, nc, e);
        View4<Matrix<E>> vr = r.views4();

        a.copy(vr.a());
        b.copy(vr.b());
        c.copy(vr.c());
        d.copy(vr.d());
        return r;
    }

    public E get(Integer f, Integer c) {
        return this.datos[this.fv + f][this.cv + c];
    }

    public E get(IntPair p) {
        return this.get(p.first(), p.second());
    }


    public void set(Integer f, Integer c, E value) {
        this.datos[this.fv + f][this.cv + c] = value;
    }

    public void set(IntPair p, E value) {

        this.set(p.first(), p.second(), value);
    }

    public Integer numRows() {


        return this.nf;
    }

    public Integer numColumns() {
        return this.nc;
    }

    public Integer area() {
        return this.numRows() * this.numColumns();
    }

    public List<E> corners() {


        return List.of(this.get(0, 0), this.get(0, this.numColumns() - 1), this.get(this.numRows() - 1, 0), this.get(this.numRows() - 1, this.numColumns() - 1));

    }


    public E center() {

        int nf = this.nf / 2;
        int nc = this.nc / 2;
        return this.get(nf, nc);
    }

    public IntPair centerIntPair() {
        int nf = this.nf / 2;
        int nc = this.nc / 2;
        return IntPair.of(nf, nc);
    }

    public <R> Matrix<R> map(Function<E, R> ft) {
        R v = ft.apply(this.get(0, 0));
        @SuppressWarnings("unchecked") R[][] datos = (R[][]) Array.newInstance(v.getClass(), this.nf, this.nc);
        Matrix<R> r = Matrix.of(datos);
        for (int f = 0; f < this.nf; f++) {
            for (int c = 0; c < this.nc; c++)
                r.set(f, c, ft.apply(this.get(f, c)));
        }
        return r;
    }

    public Matrix<E> view(IntPair origin, IntPair tam) {
        return view(origin.first(), origin.second(), tam.first(), tam.second());
    }


    public Matrix<E> view(Integer fMin, Integer cMin, Integer nf, Integer nc) {
        Preconditions.checkArgument(nf >= 0 && nc >= 0, String.format("Numero de filas menor que cero nf = %d, nc = %d", nf, nc));
        Matrix<E> r = of(this.datos);
        r.fv = this.fv + fMin;
        r.nf = nf;
        r.cv = this.cv + cMin;
        r.nc = nc;
        return r;
    }

    public Matrix<E> view(Integer nv) {
        Preconditions.checkArgument(this.nf > 1 && this.nc > 1, String.format("Las filas y las columnas deben ser mayor que 1 y son nf = %d, nc = %d", this.nf, this.nc));

        int nf = this.nf / 2;
        int nc = this.nc / 2;
        Matrix<E> r = of(this.datos);
        switch (nv) {
            case 0 -> r = view(0, 0, nf, nc);
            case 1 -> r = view(0, nc, nf, this.nc - nc);

            case 2 -> r = view(nf, 0, this.nf - nf, nc);
            case 3 -> r = view(nf, nc, this.nf - nf, this.nc - nc);
            default -> Preconditions.checkArgument(false, "Opci�n no v�lida");

        }
        return r;
    }


    public Stream<Matrix<E>> allSubMatrix() {
        return Matrix.allSubMatrix(this);

    }


    public boolean allElements(Predicate<E> pd) {

        return Matrix.allElements(this, pd);
    }


    public boolean anyElements(Predicate<E> pd) {
        return Matrix.anyElements(this, pd);
    }

    public void print(String title) {

        System.out.printf("%n%s = (nf = %d, nc = %d, fv = %d, cv = %d)%n%n", title, this.nf, this.nc, this.fv, this.cv);
        IntFunction<String> f = i -> IntStream.range(0, this.nc).boxed().map(j -> this.get(i, j).toString()).collect(Collectors.joining(", ", "[", "]"));
        String s = IntStream.range(0, this.nf).boxed().map(f::apply).collect(Collectors.joining(",\n", "[", "]"));
        System.out.println(s);

    }

    @Override
    public String toString() {
        Function<Integer, String> f = i -> IntStream.range(0, this.nc).boxed().map(j -> this.get(i, j).toString()).collect(Collectors.joining(", ", "[", "]"));
        return IntStream.range(0, this.nf).boxed().map(f).collect(Collectors.joining(",\n ", "[", "]"));
    }

    public Matrix<E> copy() {
        return new Matrix<>(this.datos, this.nf, this.nc, this.fv, this.cv);
    }

    public void copy(Matrix<E> r) {
        boolean ch = this.nc == r.nc && this.nf == r.nf;
        Preconditions.checkArgument(ch, "No se cumplen condiciones de copia");
        for (int i = 0; i < this.nf; i++) {
            for (int j = 0; j < this.nc; j++) {
                r.set(i, j, this.get(i, j));
            }
        }
    }

    public View4<Matrix<E>> views4() {
        return View4.of(this.view(0), this.view(1), this.view(2), this.view(3));
    }

    public View2<Matrix<E>> views2C() {
        return View2.of(this.view(0, 0, this.numRows(), this.numColumns() / 2), this.view(0, this.numColumns() / 2, this.numRows(), this.numColumns() - this.numColumns() / 2));
    }

    public View2<Matrix<E>> views2F() {
        return View2.of(this.view(0, 0, this.numRows() / 2, this.numColumns()), this.view(this.numRows() / 2, 0, this.numRows() - this.numRows() / 2, this.numColumns()));
    }
}
