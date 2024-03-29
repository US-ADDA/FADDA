package com.fadda.common.collections;

import com.fadda.common.MutableType;
import com.fadda.common.Preconditions;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author migueltoro
 * <p>
 * Un conjunto de un rango de enteros
 */
public class IntegerSet implements Set<Integer> {

    private final Integer infLimit;
    private final BitSet bits;

    private IntegerSet(Integer infLimit, Integer numDigits) {
        super();
        this.infLimit = infLimit;
        this.bits = new BitSet(numDigits);
        this.bits.clear();
    }

    private IntegerSet(IntegerSet s) {
        super();
        this.infLimit = s.infLimit;
        this.bits = s.bits.get(0, s.bits.length());
    }

    public IntegerSet(BitSet bits, Integer infLimit) {
        this.infLimit = infLimit;
        this.bits = (BitSet) bits.clone();
    }

    /**
     * @param infLimit  Limite inferior del conjunto de enteros
     * @param numDigits N�mero de digitos usados para representar el conjunto
     * @return Un RangeIntegerSet
     */
    public static IntegerSet empty(Integer infLimit, Integer numDigits) {
        return new IntegerSet(infLimit, numDigits);
    }

    public static IntegerSet empty() {
        return IntegerSet.empty(0, 10);
    }


    public static IntegerSet of(Integer... elems) {
        IntegerSet r = IntegerSet.empty();

        r.addAll(Arrays.asList(elems));
        return r;
    }


    public static IntegerSet ofRange(Integer from, Integer to) {
        IntegerSet r = IntegerSet.empty();

        r.addAll(IntStream.range(from, to).boxed().toList());
        return r;
    }


    public static IntegerSet copy(IntegerSet s) {
        return new IntegerSet(s);
    }


    public IntegerSet copy() {
        return new IntegerSet(this);
    }

    @Override
    public int size() {
        return this.bits.cardinality();
    }

    @Override
    public boolean isEmpty() {
        return this.bits.isEmpty();
    }

    @Override
    public boolean contains(Object obj) {
        Integer e = (int) obj;
        Preconditions.checkArgument(e >= infLimit, "Fuera de rango " + e);
        int ne = e - infLimit;
        return this.bits.get(ne);
    }

    @Override
    public Iterator<Integer> iterator() {
        return IteratorSet.of(this);
    }


    @Override
    public Object[] toArray() {
        return this.stream().toArray();
    }


    @Override
    public <T> T[] toArray(T[] a) {
        @SuppressWarnings("unchecked")
        T[] r = (T[]) this.toArray();
        System.arraycopy(r, 0, a, 0, r.length);
        return r;
    }

    @Override
    public boolean add(Integer e) {
        Preconditions.checkArgument(e >= infLimit, "Fuera de rango");
        int ne = e - infLimit;
        boolean c = this.bits.get(ne);
        this.bits.set(ne, true);
        return c != this.bits.get(ne);
    }


    public IntegerSet addNew(Integer e) {
        IntegerSet cp = this.copy();

        Preconditions.checkArgument(e >= infLimit, "Fuera de rango");
        int ne = e - infLimit;
        cp.bits.set(ne, true);
        return cp;
    }


    @Override
    public boolean remove(Object ob) {
        Integer e = (int) ob;

        Preconditions.checkArgument(e >= infLimit, "Fuera de rango");
        int ne = e - infLimit;
        boolean c = this.bits.get(ne);
        this.bits.set(ne, false);
        return c != this.bits.get(ne);
    }

    public IntegerSet removeNew(Object ob) {
        IntegerSet cp = this.copy();
        Integer e = (int) ob;
        Preconditions.checkArgument(e >= infLimit, "Fuera de rango");
        int ne = e - infLimit;
        cp.bits.set(ne, false);
        return cp;

    }

    @Override
    public boolean containsAll(Collection<?> c) {

        return c.stream().allMatch(this::contains);
    }

    public boolean containsAll(IntegerSet c) {
        Preconditions.checkArgument(this.infLimit.equals(c.infLimit),
                String.format("Sets no compatibles %d %d", this.infLimit, c.infLimit));


        return c.difference(this).isEmpty();
    }

    public boolean intersect(IntegerSet c) {
        Preconditions.checkArgument(this.infLimit.equals(c.infLimit),
                String.format("Sets no compatibles %d %d", this.infLimit, c.infLimit));
        return this.bits.intersects(c.bits);
    }

    @Override
    public boolean addAll(Collection<? extends Integer> c) {
        MutableType<Boolean> change = MutableType.of(false);
        c.forEach(x -> {
            Boolean r = this.add(x);
            change.setValue(change.value() || r);
        });
        return change.value();
    }


    public boolean addAll(IntegerSet c) {
        Preconditions.checkArgument(this.infLimit.equals(c.infLimit),
                String.format("Sets no compatibles %d %d", this.infLimit, c.infLimit));
        int n = this.bits.cardinality();
        this.bits.or(c.bits);
        return n != this.bits.cardinality();
    }


    public IntegerSet union(Collection<? extends Integer> c) {
        IntegerSet r = this.copy();
        r.addAll(c);
        return r;

    }

    public IntegerSet union(IntegerSet c) {
        Preconditions.checkArgument(Objects.equals(this.infLimit, c.infLimit),
                String.format("Sets no compatibles %d %d", this.infLimit, c.infLimit));
        BitSet cp = (BitSet) this.bits.clone();
        cp.or(c.bits);
        return new IntegerSet(cp, this.infLimit);

    }


    public boolean addAll(Integer... elems) {
        MutableType<Boolean> change = MutableType.of(false);
        Arrays.stream(elems).forEach(x -> {
            Boolean r = this.add(x);
            change.setValue(change.value() || r);
        });
        return change.value();
    }


    @Override

    public boolean retainAll(Collection<?> c) {
        MutableType<Boolean> change = MutableType.of(false);

        IntegerSet cp = this.copy();
        cp.stream().filter(x -> !c.contains(x))
                .forEach(x -> {
                    Boolean r = this.remove(x);

                    change.setValue(change.value() || r);

                });
        return change.value();
    }

    public boolean retainAll(IntegerSet c) {
        Preconditions.checkArgument(this.infLimit.equals(c.infLimit),
                String.format("Sets no compatibles %d %d", this.infLimit, c.infLimit));
        int n = this.bits.cardinality();

        this.bits.and(c.bits);
        return n != this.bits.cardinality();
    }

    public IntegerSet intersection(Collection<? extends Integer> c) {
        IntegerSet r = this.copy();
        r.retainAll(c);
        return r;
    }

    public IntegerSet intersection(IntegerSet c) {

        Preconditions.checkArgument(this.infLimit.equals(c.infLimit),

                String.format("Sets no compatibles %d %d", this.infLimit, c.infLimit));
        BitSet cp = (BitSet) this.bits.clone();
        cp.and(c.bits);
        return new IntegerSet(cp, this.infLimit);
    }

    public boolean retainAll(Integer... elems) {
        MutableType<Boolean> change = MutableType.of(false);

        IntStream.range(0, bits.length()).map(x -> x + infLimit).boxed()
                .filter(x -> !this.contains(x))
                .forEach(x -> {
                    Boolean r = this.remove(x);
                    change.setValue(change.value() || r);
                });

        return change.value();
    }

    @Override
    public boolean removeAll(Collection<?> c) {

        MutableType<Boolean> change = MutableType.of(false);

        c.forEach(x -> {
            Boolean r = this.remove(x);
            change.setValue(change.value() || r);
        });
        return change.value();
    }

    public boolean removeAll(IntegerSet c) {
        Preconditions.checkArgument(this.infLimit.equals(c.infLimit),

                String.format("Sets no compatibles %d %d", this.infLimit, c.infLimit));
        int n = this.bits.cardinality();
        this.bits.andNot(c.bits);
        return n != this.bits.cardinality();
    }

    public IntegerSet difference(Collection<? extends Integer> c) {
        IntegerSet r = this.copy();
        r.removeAll(c);
        return r;
    }

    public IntegerSet difference(IntegerSet c) {
        Preconditions.checkArgument(this.infLimit.equals(c.infLimit),
                String.format("Sets no compatibles %d %d", this.infLimit, c.infLimit));
        BitSet cp = (BitSet) this.bits.clone();
        cp.andNot(c.bits);
        return new IntegerSet(cp, this.infLimit);
    }

    public IntegerSet simetricDifference(IntegerSet c) {
        Preconditions.checkArgument(Objects.equals(this.infLimit, c.infLimit),
                String.format("Sets no compatibles %d %d", this.infLimit, c.infLimit));
        BitSet cp = (BitSet) this.bits.clone();
        cp.xor(c.bits);
        return new IntegerSet(cp, this.infLimit);
    }

    public boolean removeAll(Integer... elems) {
        MutableType<Boolean> change = MutableType.of(false);
        Arrays.stream(elems).forEach(x -> {
            Boolean r = this.remove(x);
            change.setValue(change.value() || r);
        });
        return change.value();
    }

    @Override
    public void clear() {
        this.bits.clear();
    }

    @Override
    public String toString() {
        return this.stream().map(Object::toString).collect(Collectors.joining(",", "{", "}"));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((infLimit == null) ? 0 : infLimit.hashCode());
        result = prime * result + ((bits == null) ? 0 : bits.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        IntegerSet other = (IntegerSet) obj;
        if (infLimit == null) {
            if (other.infLimit != null)
                return false;
        } else if (!infLimit.equals(other.infLimit))
            return false;
        if (bits == null) {
            return other.bits == null;
        } else return bits.equals(other.bits);
    }


    static class IteratorSet implements Iterator<Integer> {

        private final IntegerSet s;
        private int index;

        public IteratorSet(IntegerSet s) {
            super();
            this.index = 0;
            this.s = s;
        }

        public static IteratorSet of(IntegerSet s) {
            return new IteratorSet(s);
        }

        @Override
        public boolean hasNext() {
            return this.index < s.bits.length();
        }

        @Override
        public Integer next() {
            int r = s.bits.nextSetBit(index);
            this.index = r + 1;
            return r + s.infLimit;
        }

    }
}
