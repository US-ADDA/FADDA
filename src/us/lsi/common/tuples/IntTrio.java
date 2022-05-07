package us.lsi.common.tuples;

public class IntTrio {

    private final Integer first, second, third;

    public IntTrio(Integer first, Integer second, Integer third) {
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

    public static IntTrio of(Integer first, Integer second, Integer third) {
        return new IntTrio(first, second, third);
    }

    public static IntTrio parse(String s) {
        String[] partes = s.split("[(),]");
        return new IntTrio(Integer.parseInt(partes[0].trim()),
                Integer.parseInt(partes[1].trim()),
                Integer.parseInt(partes[2].trim()));
    }

    @Override
    public String toString() {
        return String.format("(%d,%d,%d)", this.first(), this.second(), this.third());
    }

}
