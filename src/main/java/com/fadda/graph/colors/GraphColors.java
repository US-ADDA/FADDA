package com.fadda.graph.colors;

import com.fadda.common.extension.Files2;
import com.fadda.common.extension.Map2;
import org.jgrapht.Graph;
import org.jgrapht.nio.Attribute;
import org.jgrapht.nio.DefaultAttribute;
import org.jgrapht.nio.dot.DOTExporter;

import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


public class GraphColors {


    /**
     * @param c color
     * @return Un Map para ser start�adido en un exportToDot.
     */
    public static Map<String, Attribute> color(Color c) {
        String cl = c == Color.blank ? "" : c.toString();
        return Map.of("color", DefaultAttribute.createAttribute(cl));
    }

    public static Map<String, Attribute> color(Integer c) {
        return color(Color.values()[c]);
    }


    public static Map<String, Attribute> colorIf(Color yesColor, Color noColor, Boolean test) {
        Color c;
        if (test) c = yesColor;
        else c = noColor;
        String cl = c.toString();
        return Map.of("color", DefaultAttribute.createAttribute(cl));
    }


    public static Map<String, Attribute> colorIf(Color yesColor, Boolean test) {
        Map<String, Attribute> m = new HashMap<>();
        if (test) m = Map.of("color", DefaultAttribute.createAttribute(yesColor.toString()));
        return m;
    }

    public static Map<String, Attribute> label(String label) {
        if (label.equals("")) return new HashMap<>();
        return Map.of("label", DefaultAttribute.createAttribute(label));
    }


    public static Map<String, Attribute> style(Style style) {
        return Map.of("style", DefaultAttribute.createAttribute(style.name()));
    }

    public static Map<String, Attribute> shape(Shape shape) {


        return Map.of("shape", DefaultAttribute.createAttribute(shape.name()));
    }

    public static Map<String, Attribute> styleIf(Style style, Boolean test) {
        if (!test) style = Style.solid;

        return Map.of("style", DefaultAttribute.createAttribute(style.name()));
    }

    public static Map<String, Attribute> style(Integer value) {
        return Map.of("style", DefaultAttribute.createAttribute(Style.values()[value].toString()));
    }

    public static Map<String, Attribute> shapeIf(Shape shape, Boolean test) {

        if (!test) shape = Shape.ellipse;
        return Map.of("shape", DefaultAttribute.createAttribute(shape.name()));
    }

    public static Map<String, Attribute> shape(Integer value) {


        return Map.of("shape", DefaultAttribute.createAttribute(Shape.values()[value].toString()));
    }

    public static Map<String, Attribute> arrowHead(ArrowHead head) {

        return Map.of("arrowhead", DefaultAttribute.createAttribute(head.name()));
    }

    @SafeVarargs
    public static Map<String, Attribute> all(Map<String, Attribute>... properties) {

        final Map<String, Attribute> r = new HashMap<>();

        for (Map<String, Attribute> f : properties)
            r.putAll(f);

        return r;
    }

    public static <V, E> void toDot(Graph<V, E> graph, String file) {
        DOTExporter<V, E> de = new DOTExporter<>();
        de.setVertexAttributeProvider(v -> GraphColors.label(v.toString()));
        Writer f1 = Files2.getWriter(file);
        de.exportGraph(graph, f1);
    }

    public static <V, E> void toDot(Graph<V, E> graph, String file, Function<V, String> vertexLabel) {

        DOTExporter<V, E> de = new DOTExporter<>();
        de.setVertexAttributeProvider(v -> GraphColors.label(vertexLabel.apply(v)));
        Writer f1 = Files2.getWriter(file);
        de.exportGraph(graph, f1);
    }

    public static <V, E> void toDot(Graph<V, E> graph, String file,
                                    Function<V, String> vertexLabel,
                                    Function<E, String> edgeLabel) {
        DOTExporter<V, E> de = new DOTExporter<>();
        de.setVertexAttributeProvider(v -> GraphColors.label(vertexLabel.apply(v)));
        de.setEdgeAttributeProvider(e -> GraphColors.label(edgeLabel.apply(e)));
        Writer f1 = Files2.getWriter(file);
        de.exportGraph(graph, f1);
    }

    public static <V, E> void toDot(Graph<V, E> graph, String file,
                                    Function<V, String> vertexLabel,
                                    Function<E, String> edgeLabel,
                                    Function<V, Map<String, Attribute>> vertexAttribute,
                                    Function<E, Map<String, Attribute>> edgeAttribute) {

        DOTExporter<V, E> de = new DOTExporter<>();

        Function<V, Map<String, Attribute>> m1 =
                v -> Map2.merge(GraphColors.label(vertexLabel.apply(v)), vertexAttribute.apply(v));
        Function<E, Map<String, Attribute>> m2 =
                e -> Map2.merge(GraphColors.label(edgeLabel.apply(e)), edgeAttribute.apply(e));

        de.setVertexAttributeProvider(m1);
        de.setEdgeAttributeProvider(m2);


        Writer f1 = Files2.getWriter(file);
        de.exportGraph(graph, f1);
    }


    public enum Color {
        blank, red, yellow, gray, cyan, orange, magenta, blue, black, green

    }

    public enum ArrowHead {
        none, normal, dot, inv, crow, tee, vee, diamond, box, curve, icurve
    }

    public enum Style {
        dotted, bold, filled, solid, invis, arrowhead
    }


    public enum Shape {
        box, polygon, ellipse, point, triangle, doublecircle
    }


}
