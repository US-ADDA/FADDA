package com.fadda.graph.path;


import com.fadda.common.Preconditions;
import com.fadda.common.extension.List2;
import com.fadda.common.tri.TriFunction;
import com.fadda.graph.graphs.alg.DynamicProgrammingReduction.Sp;
import com.fadda.graph.graphs.virtual.EGraph;
import org.jgrapht.GraphPath;
import org.jgrapht.Graphs;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class GraphPathSum<V, E> extends GraphPath2<V, E> implements EGraphPath<V, E> {


    protected final EGraph<V, E> graph;

    protected GraphPathSum(EGraph<V, E> graph, List<V> vertexList, List<E> edgeList, Double weight) {
        super(graph, vertexList, edgeList, weight);
        this.graph = graph;
    }

    /**
     *
     */


    public static <V, E> GraphPathSum<V, E> ofMap(EGraph<V, E> graph, V vertex, Map<V, Sp<E>> solutions) {
        Preconditions.checkArgument(graph.pathType() == PathType.Sum,
                String.format("El tipo del EGraphPath debe ser Sum y es %s", graph.pathType()));
        Sp<E> sp = solutions.get(vertex);
        GraphPathSum<V, E> gp = GraphPathSum.ofEdge(graph, sp.edge());
        while (sp.edge() != null) {
            vertex = Graphs.getOppositeVertex(graph, sp.edge(), vertex);
            sp = solutions.get(vertex);
            gp.add(sp.edge());
        }
        return gp;
    }

    public static <V, E> GraphPathSum<V, E> ofEdge(EGraph<V, E> graph, E edge) {
        Preconditions.checkArgument(graph.pathType() == PathType.Sum,
                String.format("El tipo del EGraphPath debe ser Sum y es %s", graph.pathType()));
        V startVertex = graph.getEdgeSource(edge);
        V endVertex = graph.getEdgeTarget(edge);
        List<V> vertexList = List2.of(startVertex, endVertex);
        List<E> edgeList = List2.of(edge);
        double weight = graph.getEdgeWeight(edge);
        weight += graph.getVertexWeight(startVertex);
        weight += graph.getVertexWeight(endVertex);
        return new GraphPathSum<>(graph, vertexList, edgeList, weight);
    }

    public static <V, E> GraphPathSum<V, E> ofVertex(EGraph<V, E> graph, V vertex) {
        Preconditions.checkArgument(graph.pathType() == PathType.Sum,
                String.format("El tipo del EGraphPath debe ser Sum y es %s", graph.pathType()));
        List<V> vertexList = List2.of(vertex);
        List<E> edgeList = List2.of();
        double weight = 0.;
        weight += graph.getVertexWeight(vertex);
        return new GraphPathSum<>(graph, vertexList, edgeList, weight);
    }

    @Override
    public GraphPathSum<V, E> add(E edge) {
        Preconditions.checkNotNull(edge, "La arista no puede ser null");
        E lastEdge = this.getEdgeList().isEmpty() ? null : List2.last(edgeList);
        V vertexActual = List2.last(vertexList);
        Preconditions.checkNotNull(vertexActual, "El v�rtice actual no puede ser null");
        super.edgeList.add(edge);
        V target = graph.oppositeVertex(edge, vertexActual);
        Preconditions.checkNotNull(target, "El v�rtice destino no puede ser null");
        super.vertexList.add(target);
        super.weight += graph.getEdgeWeight(edge);
        super.weight += graph.getVertexWeight(target);
        if (lastEdge != null) weight += graph.getVertexPassWeight(target, lastEdge, edge);
        return this;
    }

    @Override
    public GraphPathSum<V, E> remove(E edge) {
        Preconditions.checkNotNull(edge, "La arista no puede ser null");
        V vertexActual = List2.last(vertexList);
        Preconditions.checkNotNull(vertexActual, "El v�rtice actual no puede ser null");
        super.edgeList.remove(super.edgeList.size() - 1);
        E lastEdge = this.getEdgeList().isEmpty() ? null : List2.last(edgeList);
        super.vertexList.remove(super.vertexList.size() - 1);
        super.weight -= graph.getEdgeWeight(edge);
        super.weight -= graph.getVertexWeight(vertexActual);
        if (lastEdge != null) weight -= graph.getVertexPassWeight(vertexActual, lastEdge, edge);
        return this;
    }

    @Override
    public Double add(Double acumulateValue, V vertexActual, E edge, E lastEdge) {
        Preconditions.checkNotNull(edge, "La arista no puede ser null");
        Double weight = acumulateValue;
        V target = Graphs.getOppositeVertex(graph, edge, vertexActual);
        weight += graph.getEdgeWeight(edge);
        weight += graph.getVertexWeight(target);
        if (lastEdge != null) weight += graph.getVertexPassWeight(vertexActual, lastEdge, edge);
        return weight;
    }

    public GraphPathSum<V, E> copy() {
        return new GraphPathSum<>(this.graph,
                new ArrayList<>(this.vertexList),
                new ArrayList<>(this.edgeList),
                this.weight);
    }

    public GraphPathSum<V, E> concat(GraphPath<V, E> path) {
        GraphPathSum<V, E> r = this.copy();
        r.getEdgeList().forEach(this::add);
        return r;
    }

    @Override
    public Double boundedValue(Double acumulateValue, V vertexActual, E edge, Predicate<V> goal, V end,
                               TriFunction<V, Predicate<V>, V, Double> heuristic) {
        Double weight = acumulateValue;
        V target = Graphs.getOppositeVertex(graph, edge, vertexActual);
        weight += graph.getEdgeWeight(edge);
        weight += graph.getVertexWeight(target);
        return weight + heuristic.apply(target, goal, end);
    }

    @Override
    public Double estimatedWeightToEnd(Double acumulateValue, V vertexActual, Predicate<V> goal, V end,
                                       TriFunction<V, Predicate<V>, V, Double> heuristic) {
        return acumulateValue + heuristic.apply(vertexActual, goal, end);
    }

    @Override
    public E lastEdge() {
        return List2.last(this.edgeList);
    }

    @Override
    public PathType type() {
        return PathType.Sum;
    }

    @Override
    public Double goalBaseSolution(V vertexActual) {
        return 0.;
    }

    @Override
    public Double fromNeighbordSolution(Double weight, V vertexActual, E edge, E lastEdge) {
        return this.add(weight, vertexActual, edge, lastEdge);
    }
}
