package com.fadda.graph.path;

import com.fadda.common.tri.TriFunction;
import com.fadda.graph.graphs.alg.DynamicProgrammingReduction.Sp;
import com.fadda.graph.graphs.virtual.EGraph;
import org.jgrapht.GraphPath;

import java.util.Map;
import java.util.function.Predicate;

public interface EGraphPath<V, E> extends GraphPath<V, E> {

    static <V, E> Double weight(GraphPath<V, E> path) {
        return path.getEdgeList().stream().mapToDouble(e -> path.getGraph().getEdgeWeight(e)).sum();
    }


    static <V, E> GraphPathSum<V, E> ofMap(EGraph<V, E> graph, V vertex, Map<V, Sp<E>> solutions) {
        return GraphPathSum.ofMap(graph, vertex, solutions);
    }


    static <V, E> EGraphPath<V, E> ofVertex(EGraph<V, E> graph, V vertex, PathType type) {
        return switch (type) {
            case Sum -> GraphPathSum.ofVertex(graph, vertex);
            case Last -> GraphPathLast.ofVertex(graph, vertex);
        };
    }

    E lastEdge();

    EGraphPath<V, E> add(E edge);

    EGraphPath<V, E> remove(E edge);

    Double add(Double acumulateValue, V vertexActual, E edge, E lastEdge);

    Double goalBaseSolution(V vertexActual);

    Double fromNeighbordSolution(Double weight, V vertexActual, E edge, E lastEdge);

    EGraphPath<V, E> copy();

    Double boundedValue(Double acumulateValue, V vertexActual, E edge, Predicate<V> goal, V end,
                        TriFunction<V, Predicate<V>, V, Double> heuristic);

    Double estimatedWeightToEnd(Double acumulateValue, V vertexActual, Predicate<V> goal, V end,
                                TriFunction<V, Predicate<V>, V, Double> heuristic);

    EGraphPath<V, E> concat(GraphPath<V, E> path);

    GraphPath<V, E> reverse();

    PathType type();

    enum PathType {Sum, Last}

}
