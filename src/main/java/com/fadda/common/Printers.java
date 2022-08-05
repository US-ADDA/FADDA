package com.fadda.common;

import java.io.FileNotFoundException;
import java.io.PrintStream;

public class Printers {

    private Printers() {
    }

    public static PrintStream file(String file) {
        PrintStream p;
        try {
            p = new PrintStream(file);
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("No se puede abrir el fichero " + file);
        }
        return p;
    }
}
