package us.lsi.common.tuples;

public class IntQuartet {

    private final Integer first, second, third, fourth;

    public IntQuartet(Integer first, Integer second, Integer third, Integer fourth) {
        this.first = first;
        this.second = second;
        this.third = third;
        this.fourth = fourth;
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

    public Integer fourth() {
        return fourth;
    }

    public static IntQuartet of(Integer first, Integer second, Integer third, Integer fourth) {
        return new IntQuartet(first, second, third, fourth);
    }

    public static IntQuartet parse(String s) {
        String[] partes = s.split("[(),]");
        return new IntQuartet(Integer.parseInt(partes[0].trim()),
                Integer.parseInt(partes[1].trim()),
                Integer.parseInt(partes[2].trim()),
                Integer.parseInt(partes[3].trim()));
    }

    @Override
    public String toString() {
        return String.format("(%d,%d,%d,%d)", this.first(), this.second(), this.third(), this.fourth());
    }

}
