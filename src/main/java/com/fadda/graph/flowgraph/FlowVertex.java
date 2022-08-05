package com.fadda.graph.flowgraph;

import com.fadda.common.Preconditions;
import com.fadda.common.extension.List2;

import java.util.List;

/**
 * Un v�rtice de una Red de Fujo.
 * Un v�rtice de este tipo tiene asociado un coste unitario, un flujo m�ximo, otro m�nimo
 * y el tipo de v�rtice
 *
 * @author Miguel Toro
 */
public class FlowVertex {

    public static final List<FlowVertex> vertices = List2.empty();
    public static final Double maxDouble = Double.MAX_VALUE;
    public final typeVertex type;
    public final Double min;
    public final Double max;
    public final Double cost;
    public final String name;
    private final String id;

    private FlowVertex(String[] formato) {
        this.id = formato[0];
        if (formato.length == 1) {
            this.type = typeVertex.Intermediate;
            this.min = 0.;
            this.max = maxDouble;
            this.cost = 0.;
            this.name = "";
        } else if (formato.length == 2) {
            this.type = typeVertex(formato[1]);
            this.min = 0.;
            this.max = maxDouble;
            this.cost = 0.;
            this.name = "";
        } else if (formato.length == 5) {
            this.type = typeVertex(formato[1]);
            this.min = convert(formato[2]);
            this.max = convert(formato[3]);
            this.cost = convert(formato[4]);
            this.name = "";
        } else if (formato.length == 6) {
            this.type = typeVertex(formato[1]);
            this.min = convert(formato[2]);
            this.max = convert(formato[3]);
            this.cost = convert(formato[4]);
            this.name = formato[5];
        } else {
            throw new IllegalArgumentException("Formato incorrecto");
        }
    }

    public static FlowVertex create(String[] formato) {
        FlowVertex v = new FlowVertex(formato);
        FlowVertex.vertices.add(v);
        return v;
    }


    private Double convert(String s) {
        Double r;
        if (s.equals("inf")) {
            r = maxDouble;
        } else {
            r = Double.parseDouble(s);
        }
        return r;
    }


    public String convert(Double s) {
        String r;
        if (s > 1000000000.0) {
            r = "inf";
        } else {
            r = String.format("%.2f", s);
        }
        return r;
    }


    private typeVertex typeVertex(String text) {
        typeVertex r = null;
        text = text.trim();
        switch (text) {
            case "1", "Source" -> r = typeVertex.Source;
            case "2", "Sink" -> r = typeVertex.Sink;
            case "0", "Intermediate" -> r = typeVertex.Intermediate;
            default -> Preconditions.checkArgument(false, String.format("Tipo %s no permitido", text));
        }
        return r;
    }

    public Integer getColor() {
        return switch (this.type) {
            case Source -> 9;
            case Sink -> 4;
            case Intermediate -> 0;
        };
    }

    public boolean isSource() {
        return this.type == typeVertex.Source;
    }

    public boolean isSink() {
        return this.type == typeVertex.Sink;
    }

    public boolean isIntermediate() {
        return this.type == typeVertex.Intermediate;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        FlowVertex other = (FlowVertex) obj;
        if (id == null) {
            return other.id == null;
        } else return id.equals(other.id);
    }

    @Override
    public String toString() {
        return name.equals("") ? id : name;
    }


    public enum typeVertex {Source, Sink, Intermediate}

}
