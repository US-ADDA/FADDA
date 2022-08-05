package com.fadda.common.tuples.triplet;

public record IntTriplet(Integer first, Integer second, Integer third) {

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
