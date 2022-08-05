package com.fadda.graph.graphs.alg;

import com.fadda.graph.graphs.Graphs2;
import com.fadda.graph.graphs.virtual.EGraph;
import com.fadda.streams.Stream2;
import org.jgrapht.Graph;
import org.jgrapht.Graphs;

import java.util.*;
import java.util.stream.Stream;

public class DephtSearch<V, E> implements Iterator<V>, Iterable<V> {

    public final Graph<V, E> graph;
    protected final Map<V, E> edgeToOrigin;
    protected final Stack<V> stack;
    protected final V startVertex;

    DephtSearch(Graph<V, E> g, V startVertex) {
        this.graph = g;
        this.startVertex = startVertex;
        this.edgeToOrigin = new HashMap<>();
        this.edgeToOrigin.put(startVertex, null);
        this.stack = new Stack<>();
        this.stack.add(startVertex);
    }

    /**
     * @param <V>         El tipo de los v&eacute;rtices
     * @param <E>         El tipo de las aristas
     * @param g           Un grafo
     * @param startVertex El v�rtice inicial
     * @return Una algoritmo de end&uacute;squeda en profundidad en preorden
     */
    public static <V, E> DephtSearch<V, E> of(Graph<V, E> g, V startVertex) {
        return new DephtSearch<>(g, startVertex);
    }


    public Stream<V> stream() {
        return Stream2.of(this);
    }


    public DephtSearch<V, E> copy() {
        return DephtSearch.of(this.graph, this.startVertex);
    }


    public Iterator<V> iterator() {

        return this;
    }

    public boolean isSeenVertex(V v) {
        return this.edgeToOrigin.containsKey(v);
    }

    public boolean hasNext() {
        return !stack.isEmpty();
    }

    @Override
    public V next() {
        V actual = stack.pop();
        for (V v : Graphs.neighborListOf(graph, actual)) {
            if (!this.edgeToOrigin.containsKey(v)) {
                stack.add(v);


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

    public Set<E> edges() {
        return new HashSet<>(this.edgeToOrigin.values());
    }


}
