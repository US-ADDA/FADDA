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

public class GraphPathLast<V, E> extends GraphPath2<V, E> implements EGraphPath<V, E> {


    private final EGraph<V, E> graph;

    protected GraphPathLast(EGraph<V, E> graph,
                            List<V> vertexList, List<E> edgeList,
                            double weight) {
        super(graph, vertexList, edgeList, weight);
        this.graph = graph;
    }


    public static <V, E> GraphPathLast<V, E> ofMap(EGraph<V, E> graph, V vertex, Map<V, Sp<E>> solutions) {
        Preconditions.checkArgument(graph.pathType() == PathType.Last,
                String.format("El tipo del EGraphPath debe ser Last y es %s", graph.pathType()));
        Sp<E> sp = solutions.get(vertex);
        GraphPathLast<V, E> gp = GraphPathLast.ofEdge(graph, sp.edge());
        while (sp.edge() != null) {
            vertex = Graphs.getOppositeVertex(graph, sp.edge(), vertex);
            sp = solutions.get(vertex);
            gp.add(sp.edge());
        }
        return gp;
    }


    public static <V, E> GraphPathLast<V, E> ofEdge(EGraph<V, E> graph, E edge) {
        Preconditions.checkArgument(graph.pathType() == PathType.Last,
                String.format("El tipo del EGraphPath debe ser Last y es %s", graph.pathType()));
        V startVertex = graph.getEdgeSource(edge);
        V endVertex = graph.getEdgeTarget(edge);
        List<V> vertexList = List2.of(startVertex, endVertex);
        List<E> edgeList = List2.of(edge);
        double weight = graph.getVertexWeight(endVertex);
        return new GraphPathLast<>(graph, vertexList, edgeList, weight);
    }

    public static <V, E> GraphPathLast<V, E> ofVertex(EGraph<V, E> graph, V vertex) {
        Preconditions.checkArgument(graph.pathType() == PathType.Last,
                String.format("El tipo del EGraphPath debe ser Last y es %s", graph.pathType()));
        List<V> vertexList = List2.of(vertex);
        List<E> edgeList = List2.of();
        double weight = 0.;
        weight += graph.getVertexWeight(vertex);
        return new GraphPathLast<>(graph, vertexList, edgeList, weight);
    }

    public GraphPathLast<V, E> add(E edge) {
        Preconditions.checkNotNull(edge, "La arista no puede ser null");
        V vertexActual = List2.last(vertexList);
        super.edgeList.add(edge);
        V target = Graphs.getOppositeVertex(graph, edge, vertexActual);
        super.vertexList.add(target);
        super.weight = graph.getVertexWeight(target);
        return this;
    }

    @Override
    public GraphPathLast<V, E> remove(E edge) {
        Preconditions.checkNotNull(edge, "La arista no puede ser null");
        V vertexActual = List2.last(vertexList);
        Preconditions.checkNotNull(vertexActual, "El v�rtice actual no puede ser null");
        super.edgeList.remove(super.edgeList.size() - 1);
        super.vertexList.remove(super.vertexList.size() - 1);
        super.weight = graph.getVertexWeight(vertexActual);
        return this;
    }

    @Override
    public Double add(Double acumulateValue, V vertexActual, E edge, E lastEdge) {
        Preconditions.checkNotNull(edge, "La arista no puede ser null");
        V target = Graphs.getOppositeVertex(graph, edge, vertexActual);
        return graph.getVertexWeight(target);
    }

    public GraphPathLast<V, E> copy() {
        return new GraphPathLast<>(this.graph,
                new ArrayList<>(this.vertexList),
                new ArrayList<>(this.edgeList),
                this.weight);
    }

    public GraphPathLast<V, E> concat(GraphPath<V, E> path) {
        GraphPathLast<V, E> r = this.copy();
        r.getEdgeList().forEach(this::add);
        return r;
    }

    @Override
    public Double boundedValue(Double accumulateValue, V vertexActual, E edge, Predicate<V> goal, V end,
                               TriFunction<V, Predicate<V>, V, Double> heuristic) {
        Preconditions.checkNotNull(edge, "La arista no puede ser null");
        V target = Graphs.getOppositeVertex(super.graph, edge, vertexActual);
        return heuristic.apply(target, goal, end);
    }

    @Override
    public Double estimatedWeightToEnd(Double accumulateValue, V vertexActual, Predicate<V> goal, V end,
                                       TriFunction<V, Predicate<V>, V, Double> heuristic) {
        return heuristic.apply(vertexActual, goal, end);
    }

    @Override
    public E lastEdge() {
        return List2.last(this.edgeList);
    }

    @Override
    public PathType type() {
        return PathType.Last;
    }

    @Override
    public Double goalBaseSolution(V vertexActual) {
        return graph.getVertexWeight(vertexActual);
    }

    @Override
    public Double fromNeighbordSolution(Double weight, V vertexActual, E edge, E lastEdge) {
        return weight;
    }
}

