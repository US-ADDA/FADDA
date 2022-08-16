package com.fadda.common;

import com.fadda.common.tuples.pair.Pair;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Parse {

    public static String start = "", end = "";
    public static String sep = ";";
    public static String typeIndicator = "&";
    public static String startData = "(", endData = ")";

    public static <E> E ofFormat(String expr, String info, Class<E> clazz) {
        expr = expr.replaceAll(start, "").replaceAll(end, "");
        info = info.replaceAll(start, "").replaceAll(end, "");
        String[] tokens = expr.split(sep);
        String[] data = info.split(sep);
        Object instance;
        try {
            instance = clazz.getConstructors()[0].newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        if (clazz.isRecord())
            throw new IllegalArgumentException("Record class not supported");
        for (var i = 0; i < tokens.length; i++) {
            int startWord = tokens[i].indexOf(startData);
            int endWord = tokens[i].indexOf(endData);
            String deleteBefore = tokens[i].substring(0, startWord);
            String deleteAfter = (endWord == tokens[i].length() - 1) ? "" : tokens[i].substring(endWord + 1);
            tokens[i] = tokens[i].substring(startWord + 1, endWord);
            data[i] = data[i].replaceAll(deleteAfter, "").replaceAll(deleteBefore, "");
            String token = tokens[i];
            String value = data[i];
            String name = token.split(typeIndicator)[0];
            String type = token.split(typeIndicator)[1];
            String method = "set" + name.substring(0, 1).toUpperCase() + name.substring(1);

            try {
                switch (type) {
                    case "int", "Int", "Integer" -> clazz.getMethod(method, int.class).invoke(instance, Integer.parseInt(value));
                    case "double", "Double" -> clazz.getMethod(method, double.class).invoke(instance, Double.parseDouble(value));
                    case "boolean", "Boolean" -> clazz.getMethod(method, boolean.class).invoke(instance, Boolean.parseBoolean(value));
                    case "String" -> clazz.getMethod(method, String.class).invoke(instance, value);
                    case "char", "Char", "Character" -> clazz.getMethod(method, char.class).invoke(instance, value.charAt(0));
                    case "long", "Long" -> clazz.getMethod(method, long.class).invoke(instance, Long.parseLong(value));
                    case "float", "Float" -> clazz.getMethod(method, float.class).invoke(instance, Float.parseFloat(value));
                    case "short", "Short" -> clazz.getMethod(method, short.class).invoke(instance, Short.parseShort(value));
                    case "byte", "Byte" -> clazz.getMethod(method, byte.class).invoke(instance, Byte.parseByte(value));
                    case "localDate", "LocalDate" -> clazz.getMethod(method, LocalDate.class).invoke(instance, LocalDate.parse(value));
                    case "string[]", "String[]", "char[]", "Char[]" -> clazz.getMethod(method, List.class).invoke(instance, Arrays.stream(value.split(",")).toList());
                    case "int[]", "Int[]", "Integer[]" -> clazz.getMethod(method, List.class).invoke(instance, Arrays.stream(value.split(",")).map(Integer::parseInt).toList());
                    case "double[]", "Double[]" -> clazz.getMethod(method, List.class).invoke(instance, Arrays.stream(value.split(",")).map(Double::parseDouble).toList());
                    case "boolean[]", "Boolean[]" -> clazz.getMethod(method, List.class).invoke(instance, Arrays.stream(value.split(",")).map(Boolean::parseBoolean).toList());
                    case "long[]", "Long[]" -> clazz.getMethod(method, List.class).invoke(instance, Arrays.stream(value.split(",")).map(Long::parseLong).toList());
                    case "float[]", "Float[]" -> clazz.getMethod(method, List.class).invoke(instance, Arrays.stream(value.split(",")).map(Float::parseFloat).toList());
                    case "short[]", "Short[]" -> clazz.getMethod(method, List.class).invoke(instance, Arrays.stream(value.split(",")).map(Short::parseShort).toList());
                    case "byte[]", "Byte[]" -> clazz.getMethod(method, List.class).invoke(instance, Arrays.stream(value.split(",")).map(Byte::parseByte).toList());
                    case "localDate[]", "LocalDate[]" -> clazz.getMethod(method, List.class).invoke(instance, Arrays.stream(value.split(",")).map(LocalDate::parse).toList());
                    case "string:int" -> clazz.getMethod(method, Map.class).invoke(instance,
                                Arrays.stream(value.split(","))
                                        .map(s -> s.replace(startData, "").replace(endData, ""))
                                        .map(s -> new Pair<>(s.split(":")[0], Integer.parseInt(s.split(":")[1])))
                                        .collect(Collectors.groupingBy(Pair::first, Collectors.mapping(Pair::second, Collectors.toList()))));
                    case "int:string" -> clazz.getMethod(method, Map.class).invoke(instance,
                                Arrays.stream(value.split(","))
                                        .map(s -> s.replace(startData, "").replace(endData, ""))
                                        .map(s -> new Pair<>(Integer.parseInt(s.split(":")[0]), s.split(":")[1]))
                                        .collect(Collectors.groupingBy(Pair::first, Collectors.mapping(Pair::second, Collectors.toList()))));
                    case "string:char" -> clazz.getMethod(method, Map.class).invoke(instance,
                                Arrays.stream(value.split(","))
                                        .map(s -> s.replace(startData, "").replace(endData, ""))
                                        .map(s -> new Pair<>(s.split(":")[0], s.split(":")[1].charAt(0)))
                                        .collect(Collectors.groupingBy(Pair::first, Collectors.mapping(Pair::second, Collectors.toList()))));
                    case "char:string" -> clazz.getMethod(method, Map.class).invoke(instance,
                                Arrays.stream(value.split(","))
                                        .map(s -> s.replace(startData, "").replace(endData, ""))
                                        .map(s -> new Pair<>(s.split(":")[0].charAt(0), s.split(":")[1]))
                                        .collect(Collectors.groupingBy(Pair::first, Collectors.mapping(Pair::second, Collectors.toList()))));
                    case "string:double" -> clazz.getMethod(method, Map.class).invoke(instance,
                                Arrays.stream(value.split(","))
                                        .map(s -> s.replace(startData, "").replace(endData, ""))
                                        .map(s -> new Pair<>(s.split(":")[0], Double.parseDouble(s.split(":")[1])))
                                        .collect(Collectors.groupingBy(Pair::first, Collectors.mapping(Pair::second, Collectors.toList()))));
                    case "double:string" -> clazz.getMethod(method, Map.class).invoke(instance,
                                Arrays.stream(value.split(","))
                                        .map(s -> s.replace(startData, "").replace(endData, ""))
                                        .map(s -> new Pair<>(Double.parseDouble(s.split(":")[0]), s.split(":")[1]))
                                        .collect(Collectors.groupingBy(Pair::first, Collectors.mapping(Pair::second, Collectors.toList()))));
                    case "string:boolean" -> clazz.getMethod(method, Map.class).invoke(instance,
                                Arrays.stream(value.split(","))
                                        .map(s -> s.replace(startData, "").replace(endData, ""))
                                        .map(s -> new Pair<>(s.split(":")[0], Boolean.parseBoolean(s.split(":")[1])))
                                        .collect(Collectors.groupingBy(Pair::first, Collectors.mapping(Pair::second, Collectors.toList()))));
                    case "boolean:string" -> clazz.getMethod(method, Map.class).invoke(instance,
                                Arrays.stream(value.split(","))
                                        .map(s -> s.replace(startData, "").replace(endData, ""))
                                        .map(s -> new Pair<>(Boolean.parseBoolean(s.split(":")[0]), s.split(":")[1]))
                                        .collect(Collectors.groupingBy(Pair::first, Collectors.mapping(Pair::second, Collectors.toList()))));
                    case "string:long" -> clazz.getMethod(method, Map.class).invoke(instance,
                                Arrays.stream(value.split(","))
                                        .map(s -> s.replace(startData, "").replace(endData, ""))
                                        .map(s -> new Pair<>(s.split(":")[0], Long.parseLong(s.split(":")[1])))
                                        .collect(Collectors.groupingBy(Pair::first, Collectors.mapping(Pair::second, Collectors.toList()))));
                    case "long:string" -> clazz.getMethod(method, Map.class).invoke(instance,
                                Arrays.stream(value.split(","))
                                        .map(s -> s.replace(startData, "").replace(endData, ""))
                                        .map(s -> new Pair<>(Long.parseLong(s.split(":")[0]), s.split(":")[1]))
                                        .collect(Collectors.groupingBy(Pair::first, Collectors.mapping(Pair::second, Collectors.toList()))));
                    case "string:string" -> clazz.getMethod(method, Map.class).invoke(instance,
                                Arrays.stream(value.split(","))
                                        .map(s -> s.replace(startData, "").replace(endData, ""))
                                        .map(s -> new Pair<>(s.split(":")[0], s.split(":")[1]))
                                        .collect(Collectors.groupingBy(Pair::first, Collectors.mapping(Pair::second, Collectors.toList()))));
                    case "string:localDate" -> clazz.getMethod(method, Map.class).invoke(instance,
                                Arrays.stream(value.split(","))
                                        .map(s -> s.replace(startData, "").replace(endData, ""))
                                        .map(s -> new Pair<>(s.split(":")[0], LocalDate.parse(s.split(":")[1])))
                                        .collect(Collectors.groupingBy(Pair::first, Collectors.mapping(Pair::second, Collectors.toList()))));
                    default -> throw new IllegalStateException("Unexpected value: " + type);
                }
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
        return clazz.cast(instance);
    }
}
