package main.java.common.tuples.triplet;

public class IntTriplet {

    private final Integer first, second, third;

    public IntTriplet(Integer first, Integer second, Integer third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    public Integer first() {
        return first;
    }

    public Integer second() {
        return second;
    }

    public Integer third() {
        return third;
    }

    public static IntTriplet of(Integer first, Integer second, Integer third) {
        return new IntTriplet(first, second, third);
    }

    public static IntTriplet parse(String s) {
        String[] partes = s.split("[(),]");
        return new IntTriplet(Integer.parseInt(partes[0].trim()),
                Integer.parseInt(partes[1].trim()),
                Integer.parseInt(partes[2].trim()));
    }

    @Override
    public String toString() {
        return String.format("(%d,%d,%d)", this.first(), this.second(), this.third());
    }

}
