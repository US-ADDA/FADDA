package com.fadda.graph.graphs.alg;

import com.fadda.graph.graphs.virtual.EGraph;
import com.fadda.streams.Stream2;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

public class LocalSearch<V, E> implements Iterator<V>, Iterable<V> {

    public final Map<V, E> edgeToOrigin;
    private final EGraph<V, E> graph;
    private final V startVertex;
    private final List<V> path;
    private final Function<V, V> nextVertex;
    private final Double error;
    private V actualVertex;
    private V oldVertex;
    private Boolean hasNext;

    LocalSearch(EGraph<V, E> graph, Function<V, V> nextVertex, Double error) {
        this.graph = graph;
        this.startVertex = graph.startVertex();
        this.actualVertex = this.startVertex;
        this.edgeToOrigin = new HashMap<>();
        this.path = new ArrayList<>();
        this.nextVertex = nextVertex;
        this.oldVertex = null;
        this.error = error;
        this.hasNext = true;
    }

    public static <V, E> LocalSearch<V, E> of(EGraph<V, E> graph, Function<V, V> nextVertex, Double error) {
        return new LocalSearch<>(graph, nextVertex, error);
    }


    public LocalSearch<V, E> copy() {

        return LocalSearch.of(this.graph, this.nextVertex, this.error);
    }


    public Stream<V> stream() {

        return Stream2.of(this);
    }

    public Iterator<V> iterator() {


        return this;
    }


    public E getEdgeToOrigin(V v) {
        return this.edgeToOrigin.get(v);
    }


    public boolean isSeenVertex(V v) {
        return this.edgeToOrigin.containsKey(v);
    }

    public EGraph<V, E> getGraph() {
        return graph;
    }

    @Override

    public boolean hasNext() {
        return hasNext;
    }

    @Override
    public V next() {

        this.oldVertex = this.actualVertex;
        this.path.add(oldVertex);
        this.actualVertex = this.nextVertex.apply(this.oldVertex);
        this.hasNext = !this.actualVertex.equals(this.oldVertex) && !this.path.contains(this.actualVertex) &&
                Math.abs(graph.getVertexWeight(this.oldVertex) - graph.getVertexWeight(this.actualVertex)) >= this.error;
        return this.oldVertex;
    }

    public V startVertex() {
        return this.startVertex;
    }


}
