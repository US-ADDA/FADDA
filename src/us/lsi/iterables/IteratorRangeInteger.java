package us.lsi.iterables;

import us.lsi.common.Preconditions;

import java.util.Iterator;

public class IteratorRangeInteger implements Iterator<Integer>, Iterable<Integer> {

    public static IteratorRangeInteger of(Integer a, Integer b, Integer c) {
        return new IteratorRangeInteger(a, b, c);
    }

    private Integer i;
    private final Integer a;
    private final Integer b;
    private final Integer c;

    private IteratorRangeInteger(Integer a, Integer b, Integer c) {
        Preconditions.checkArgument((b >= a && c > 0) || (b <= a && c < 0), String.format("Valores a=%d,b=%d,c=%d no validos", a, b, c));
        this.a = a;
        this.b = b;
        this.c = c;
        this.i = a;
    }

    @Override
    public Iterator<Integer> iterator() {
        return of(a, b, c);
    }

    @Override
    public boolean hasNext() {
        return (c > 0 && i < b) || (c < 0 && i > b);
    }

    @Override
    public Integer next() {
        Integer e = i;
        i = i + c;
        return e;
    }
}
