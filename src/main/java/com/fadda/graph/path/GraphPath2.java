package com.fadda.graph.path;

import com.fadda.common.Preconditions;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

public class GraphPath2<V, E> implements GraphPath<V, E> {

    protected final Graph<V, E> graph;
    protected final List<V> vertexList;
    protected final List<E> edgeList;
    protected Double weight;

    protected GraphPath2(Graph<V, E> graph, List<V> vertexList, List<E> edgeList, Double weight) {
        super();
        this.graph = graph;
        this.vertexList = vertexList;
        this.edgeList = edgeList;
        this.weight = weight;
    }

    public static <V, E> GraphPath2<V, E> of(Graph<V, E> graph, List<V> vertexList, List<E> edgeList, Double weight) {
        return new GraphPath2<>(graph, vertexList, edgeList, weight);
    }


    public static <V, E> GraphPath2<V, E> ofVertices(Graph<V, E> graph, List<V> vertexList) {
        Preconditions.checkNotNull(graph);
        Preconditions.checkNotNull(vertexList);
        int n = vertexList.size();
        List<E> edgeList = new ArrayList<>();
        Double r = IntStream.range(0, n - 1)
                .mapToDouble(i -> {
                    E e = graph.getEdge(vertexList.get(i), vertexList.get(i + 1));
                    Preconditions.checkNotNull(e, String.format("No existe la arista %d, %d", i, i + 1));
                    edgeList.add(e);
                    return graph.getEdgeWeight(e);
                })
                .sum();
        return new GraphPath2<>(graph, vertexList, edgeList, r);
    }


    public static <V, E> GraphPath2<V, E> ofEdges(Graph<V, E> graph, List<E> edgeList) {
        Preconditions.checkNotNull(graph);
        Preconditions.checkNotNull(edgeList);
        int n = edgeList.size();
        List<V> vertexList = new ArrayList<>();
        V v = graph.getEdgeSource(edgeList.get(0));
        vertexList.add(v);
        Double r = IntStream.range(0, n - 1)
                .mapToDouble(i -> {
                    E e = edgeList.get(i);
                    Preconditions.checkNotNull(e, String.format("No existe la arista %d, %d", i, i + 1));
                    vertexList.add(graph.getEdgeTarget(e));
                    return graph.getEdgeWeight(e);
                })
                .sum();
        return new GraphPath2<>(graph, vertexList, edgeList, r);
    }


    @Override
    public V getEndVertex() {

        return vertexList.get(vertexList.size() - 1);
    }

    @Override
    public Graph<V, E> getGraph() {
        return graph;
    }

    @Override
    public V getStartVertex() {
        return vertexList.get(0);
    }

    @Override
    public double getWeight() {
        return weight;

    }

    public GraphPath2<V, E> reverse() {
        List<V> lv = new ArrayList<>(this.vertexList);
        List<E> le = new ArrayList<>(this.edgeList);
        Collections.reverse(lv);
        Collections.reverse(le);
        return GraphPath2.of(graph, lv, le, weight);
    }

    public List<V> getVertexList() {
        return vertexList;
    }

    public List<E> getEdgeList() {
        return edgeList;
    }

}
