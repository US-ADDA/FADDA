package com.fadda.common;

import java.lang.reflect.InvocationTargetException;

public class Parse {

    public static String start = "", end = "";
    public static String sep = ";";
    public static String typeIndicator = "&";
    public static String statrData = "(", endData = ")";

    public static <E> E parse(String expr, String info, Class<E> clazz) {
        expr = expr.replaceAll(start, "").replaceAll(end, "");
        info = info.replaceAll(start, "").replaceAll(end, "");
        String[] tokens = expr.split(sep);
        String[] data = info.split(sep);
        for (int i = 0; i < tokens.length; i++) {
            int start = tokens[i].indexOf(statrData);
            int end = tokens[i].indexOf(endData);
            String deleteBefore = tokens[i].substring(0, start);
            String deleteAfter = (end == tokens[i].length() - 1) ? "": tokens[i].substring(end + 1);
            tokens[i] = tokens[i].substring(start + 1, end);
            data[i] = data[i].replaceAll(deleteAfter, "").replaceAll(deleteBefore, "");
        }
        Object instance;
        try {
            instance = clazz.getConstructors()[0].newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        if (clazz.isRecord())
            throw new IllegalArgumentException("Record class not supported");
        for (var i = 0; i < tokens.length; i++) {
            String token = tokens[i];
            String value = data[i];
            String name = token.split(typeIndicator)[0];
            String type = token.split(typeIndicator)[1];
            String method = "set" + name.substring(0, 1).toUpperCase() + name.substring(1);
            try {
                switch (type) {
                    case "int" -> clazz.getMethod(method, int.class).invoke(instance, Integer.parseInt(value));
                    case "double" -> clazz.getMethod(method, double.class).invoke(instance, Double.parseDouble(value));
                    case "boolean" -> clazz.getMethod(method, boolean.class).invoke(instance, Boolean.parseBoolean(value));
                    case "String" -> clazz.getMethod(method, String.class).invoke(instance, value);
                    default -> throw new IllegalStateException("Unexpected value: " + type);
                }
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }

        }
        return clazz.cast(instance);
    }
}
