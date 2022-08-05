package com.fadda.graph.graphs.virtual;

import com.fadda.graph.path.EGraphPath;
import org.jgrapht.Graph;

import java.util.List;
import java.util.function.Predicate;

public interface EGraph<V, E> extends Graph<V, E> {

    double getVertexPassWeight(V vertex, E edgeIn, E edgeOut);

    double getVertexWeight(V vertex);

    List<E> edgesListOf(V v);

    EGraphPath<V, E> initialPath();

    V oppositeVertex(E edge, V v);

    V startVertex();

    Predicate<V> goal();

    V endVertex();

    Predicate<V> constraint();

    EGraphPath.PathType pathType();
}
