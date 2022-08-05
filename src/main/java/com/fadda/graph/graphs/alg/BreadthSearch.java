package com.fadda.graph.graphs.alg;


import com.fadda.graph.graphs.Graphs2;
import com.fadda.graph.graphs.virtual.EGraph;
import com.fadda.streams.Stream2;
import org.jgrapht.Graph;
import org.jgrapht.Graphs;

import java.util.*;
import java.util.stream.Stream;

public class BreadthSearch<V, E> implements Iterator<V>, Iterable<V> {

    public final Map<V, E> edgeToOrigin;
    public final Queue<V> queue;
    private final Graph<V, E> graph;
    private final V startVertex;

    BreadthSearch(Graph<V, E> g, V startVertex) {
        this.graph = g;
        this.startVertex = startVertex;
        this.edgeToOrigin = new HashMap<>();
        this.edgeToOrigin.put(startVertex, null);
        this.queue = new LinkedList<>();
        this.queue.add(startVertex);
    }

    /**
     * @param <V>         El tipo de los v&eacute;rtices
     * @param <E>         El tipo de las aristas
     * @param g           Un grafo
     * @param startVertex El v�rtice inicial
     * @return Una algoritmo de end&uacute;squeda en anchura
     */
    public static <V, E> BreadthSearch<V, E> of(Graph<V, E> g, V startVertex) {
        return new BreadthSearch<>(g, startVertex);
    }


    public Stream<V> stream() {
        return Stream2.of(this);
    }


    public BreadthSearch<V, E> copy() {
        return BreadthSearch.of(this.graph, this.startVertex);
    }


    public Iterator<V> iterator() {

        return this;
    }


    public boolean isSeenVertex(V v) {
        return this.edgeToOrigin.containsKey(v);
    }

    public boolean hasNext() {
        return !this.queue.isEmpty();
    }

    @Override
    public V next() {
        V actual = this.queue.remove();
        for (V v : Graphs.neighborListOf(graph, actual)) {
            if (!this.edgeToOrigin.containsKey(v)) {
                this.queue.add(v);
                this.edgeToOrigin.put(v, graph.getEdge(actual, v));
            }

        }
        return actual;
    }


    public E getEdgeToOrigin(V v) {
        return this.edgeToOrigin.get(v);

    }

    public EGraph<V, E> getGraph() {

        return Graphs2.eGraphSum(this.graph, startVertex(), null, null);
    }

    public V startVertex() {
        return this.startVertex;
    }

    public Double distanceToOrigen(V vertex) {
        double r = 0.;

        while (!vertex.equals(startVertex)) {
            E edge = this.getEdgeToOrigin(vertex);
            vertex = Graphs.getOppositeVertex(graph, edge, vertex);


            r = r + 1;
        }
        return r;
    }

    public Set<E> edges() {
        return new HashSet<>(this.edgeToOrigin.values());
    }

}
