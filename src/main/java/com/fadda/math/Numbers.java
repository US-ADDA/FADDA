package com.fadda.math;

import java.math.BigInteger;

public class Numbers {


    public static Long stringToLong(String s) {
        long r;
        try {
            r = Long.parseLong(s);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("La cadena " + s + " no constiuye un Long");
        }
        return r;
    }


    public static Integer stringToInteger(String s) {
        int r;
        try {
            r = Integer.parseInt(s);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("La cadena " + s + " no constituye un Integer");
        }
        return r;
    }


    public static Float stringToFloat(String s) {
        float r;
        try {
            r = Float.parseFloat(s);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("La cadena " + s + " no constituye un Float");
        }
        return r;
    }


    public static Double stringToDouble(String s) {
        double r;
        try {
            r = Double.parseDouble(s);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("La cadena " + s + " no constituye un Double");
        }
        return r;
    }


    public static BigInteger stringToBigInteger(String s) {
        BigInteger r;
        try {
            r = new BigInteger(s);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("La cadena " + s + " no constituye un BigInteger");
        }
        return r;
    }

}
