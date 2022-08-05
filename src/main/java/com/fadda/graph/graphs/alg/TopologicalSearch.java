package com.fadda.graph.graphs.alg;

import com.fadda.graph.graphs.Graphs2;
import com.fadda.graph.graphs.virtual.EGraph;
import com.fadda.streams.Stream2;
import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import java.util.*;
import java.util.stream.Stream;

public class TopologicalSearch<V, E> implements Iterator<V>, Iterable<V> {

    public final Graph<V, E> graph;
    public final Boolean withGraph = false;
    protected final Map<V, E> edgeToOrigin;
    protected final Stack<V> stackPre;
    protected final Queue<V> queue;
    protected final V startVertex;
    public Graph<V, E> outGraph;
    protected Integer n;
    protected Integer i;

    TopologicalSearch(Graph<V, E> g, V startVertex) {
        this.graph = g;
        this.startVertex = startVertex;
        this.edgeToOrigin = new HashMap<>();
        this.edgeToOrigin.put(startVertex, null);
        this.stackPre = new Stack<>();

        this.stackPre.add(startVertex);
        this.queue = new LinkedList<>();
        this.preorder();
    }

    /**
     * @param <V>         El tipo de los v&eacute;rtices
     * @param <E>         El tipo de las aristas
     * @param g           Un grafo
     * @param startVertex El v�rtice inicial
     * @return Una algoritmo de end&uacute;squeda en orden topologico
     */


    public static <V, E> TopologicalSearch<V, E> topological(Graph<V, E> g, V startVertex) {
        return new TopologicalSearch<>(g, startVertex);
    }

    public Stream<V> stream() {
        if (Boolean.TRUE.equals(this.withGraph)) outGraph = new SimpleDirectedWeightedGraph<>(null, null);

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

    public boolean hasNextP() {
        return !stackPre.isEmpty();
    }

    public V nextP() {
        V actual = stackPre.pop();
        if (Boolean.TRUE.equals(this.withGraph)) outGraph.addVertex(actual);
        this.queue.add(actual);
        for (V v : Graphs.neighborListOf(graph, actual)) {
            if (!this.edgeToOrigin.containsKey(v)) {
                stackPre.add(v);
                this.edgeToOrigin.put(v, graph.getEdge(actual, v));
                if (Boolean.TRUE.equals(this.withGraph)) {
                    outGraph.addVertex(v);
                    outGraph.addEdge(actual, v, graph.getEdge(actual, v));
                }
            }
        }
        return actual;
    }

    private void preorder() {
        while (this.hasNextP()) {
            this.nextP();
        }
    }

    @Override
    public boolean hasNext() {


        return !this.queue.isEmpty();
    }

    @Override
    public V next() {
        return this.queue.remove();


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


}