package com.fadda.common.extension;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Files2 {


    public static final Long NO_SKIP = 0L;
    private static final String DEFAULT_ENCODING = "utf-8";
    public static PrintWriter writer = null;

    private Files2() {
    }


    public static void toFile(String s, String file) {
        try {
            final PrintWriter f = new PrintWriter(new BufferedWriter(new FileWriter(file)));
            f.println(s);
            f.close();
        } catch (IOException e) {
            throw new IllegalArgumentException("No se ha podido crear el fichero " + file);
        }
    }


    /**
     * @param s    Una stream
     * @param file Un fichero donde guardar los elementos de la stream
     */
    public static void toFile(Stream<String> s, String file) {
        try {
            final PrintWriter f = new PrintWriter(new BufferedWriter(new FileWriter(file)));
            s.forEach(f::println);
            f.close();
        } catch (IOException e) {
            throw new IllegalArgumentException("No se ha podido crear el fichero " + file);
        }
    }


    /**
     * @param file Un fichero
     * @return Un stream formado por las l�neas del fichero
     * @throws IllegalArgumentException si no se encucntra el fichero
     */

    public static Stream<String> streamFromFile(String file) {
        Stream<String> r;
        try {
            r = Files.lines(Paths.get(file), Charset.defaultCharset());
        } catch (IOException e) {
            throw new IllegalArgumentException("No se ha encontrado el fichero " + file);
        }
        return r;
    }


    public static List<String> linesFromFile(String file) {
        List<String> r;
        try {
            r = Files.readAllLines(Paths.get(file), Charset.defaultCharset());
        } catch (IOException e) {
            throw new IllegalArgumentException("No se ha encontrado el fichero " + file);
        }
        return r;
    }


    public static OutputStream getOutputStream(String file) {
        OutputStream r = null;
        try {
            r = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return r;
    }


    public static PrintWriter getWriter(String file) {

        PrintWriter r = null;
        try {

            r = new PrintWriter(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();


        }
        return r;
    }

    public static PrintWriter getWriter() {
        return writer;
    }

    public static void setPrintWriter(String file) {
        PrintWriter r = null;
        try {
            r = new PrintWriter(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        writer = r;
    }


    /**
     * Método que permite leer un fichero.
     *
     * @param <A>   tipo que se devolverá dentro del {@code Stream}.
     * @param path  ruta correspondiente al fichero que se desea leer.
     * @param parse que permite pasar del tipo {@code String} al tipo deseado.
     * @return un {@code Stream} parseado al tipo deseado.
     */

    public static <A> Stream<A> asStream(String path, Function<String, A> parse) {
        return asStream(path, parse, DEFAULT_ENCODING, NO_SKIP);
    }


    /**
     * Método que permite leer un fichero.
     *
     * @param <A>   tipo que se devolverá dentro del {@code Stream}.
     * @param path  ruta correspondiente al fichero que se desea leer.
     * @param parse que permite pasar del tipo {@code String} al tipo deseado.
     * @param n     número de líneas al inicio del fichero que se desean saltar.
     * @return un {@code Stream} parseado al tipo deseado.
     */
    public static <A> Stream<A> asStream(String path, Function<String, A> parse, Long n) {

        return asStream(path, parse, DEFAULT_ENCODING, n);
    }

    /**
     * Método que permite leer un fichero.
     *
     * @param <A>      tipo que se devolverá dentro del {@code Stream}.
     * @param path     ruta correspondiente al fichero que se desea leer.
     * @param parse    que permite pasar del tipo {@code String} al tipo deseado.
     * @param encoding codificación del fichero.
     * @return un {@code Stream} parseado al tipo deseado.
     */
    public static <A> Stream<A> asStream(String path, Function<String, A> parse, String encoding) {
        return asStream(path, parse, encoding, NO_SKIP);
    }

    /**
     * Método que permite leer un fichero.
     *
     * @param <A>      tipo que se devolverá dentro del {@code Stream}.
     * @param path     ruta correspondiente al fichero que se desea leer.
     * @param parse    que permite pasar del tipo {@code String} al tipo deseado.
     * @param encoding codificación del fichero.
     * @param n        número de líneas al inicio del fichero que se desean saltar.
     * @return un {@code Stream} parseado al tipo deseado.
     */
    public static <A> Stream<A> asStream(String path, Function<String, A> parse, String encoding, Long n) {
        try {
            return Files.newBufferedReader(Paths.get(path), Charset.forName(encoding))
                    .lines().map(parse).skip(n);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * Método que permite leer un fichero.
     *
     * @param <A>   tipo que se devolverá dentro del {@code Stream}.
     * @param path  ruta correspondiente al fichero que se desea leer.
     * @param parse que permite pasar del tipo {@code String} al tipo deseado.
     * @return un {@code List} parseado al tipo deseado.
     */
    public static <A> List<A> asList(String path, Function<String, A> parse) {

        return Objects.requireNonNull(asStream(path, parse, DEFAULT_ENCODING, NO_SKIP)).collect(Collectors.toList());
    }


    /**
     * Método que permite leer un fichero.
     *
     * @param <A>   tipo que se devolverá dentro del {@code Stream}.
     * @param path  ruta correspondiente al fichero que se desea leer.
     * @param parse que permite pasar del tipo {@code String} al tipo deseado.
     * @param n     número de líneas al inicio del fichero que se desean saltar.
     * @return un {@code List} parseado al tipo deseado.
     */
    public static <A> List<A> asList(String path, Function<String, A> parse, Long n) {

        return Objects.requireNonNull(asStream(path, parse, DEFAULT_ENCODING, n)).collect(Collectors.toList());
    }

    /**
     * Método que permite leer un fichero.
     *
     * @param <A>      tipo que se devolverá dentro del {@code Stream}.
     * @param path     ruta correspondiente al fichero que se desea leer.
     * @param parse    que permite pasar del tipo {@code String} al tipo deseado.
     * @param encoding codificación del fichero.
     * @return un {@code List} parseado al tipo deseado.
     */
    public static <A> List<A> asList(String path, Function<String, A> parse, String encoding) {
        return Objects.requireNonNull(asStream(path, parse, encoding, NO_SKIP)).collect(Collectors.toList());
    }

    /**
     * Método que permite leer un fichero.
     *
     * @param <A>      tipo que se devolverá dentro del {@code Stream}.
     * @param path     ruta correspondiente al fichero que se desea leer.
     * @param parse    que permite pasar del tipo {@code String} al tipo deseado.
     * @param encoding codificación del fichero.
     * @param n        número de líneas al inicio del fichero que se desean saltar.
     * @return un {@code List} parseado al tipo deseado.
     */
    public static <A> List<A> asList(String path, Function<String, A> parse, String encoding, Long n) {
        return Objects.requireNonNull(asStream(path, parse, encoding, n)).collect(Collectors.toList());
    }
}

