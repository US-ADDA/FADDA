package com.fadda.graph.graphs.virtual;

import com.fadda.common.tri.TriFunction;
import com.fadda.graph.path.EGraphPath;
import com.fadda.graph.path.EGraphPath.PathType;
import org.jgrapht.Graph;
import org.jgrapht.GraphType;
import org.jgrapht.Graphs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;


public class EGraphI<V, E, G extends Graph<V, E>> implements EGraph<V, E> {


    private final G graph;
    private final Function<E, Double> edgeWeight;
    private final Function<V, Double> vertexWeight;
    private final TriFunction<V, E, E, Double> vertexPassWeight;
    private final V startVertex;
    private final Predicate<V> goal;
    private final V endVertex;
    private final Predicate<V> constraint;
    private final PathType type;
    private EGraphPath<V, E> path;

    public EGraphI(G graph, V startVertex, Predicate<V> goal, V endVertex, Predicate<V> constraint,
                   PathType type, Function<E, Double> edgeWeight, Function<V, Double> vertexWeight,
                   TriFunction<V, E, E, Double> vertexPassWeight) {
        super();
        this.graph = graph;
        this.edgeWeight = edgeWeight;
        this.vertexWeight = vertexWeight;
        this.vertexPassWeight = vertexPassWeight;
        this.startVertex = startVertex;
        this.goal = goal;
        this.endVertex = endVertex;
        this.constraint = constraint;
        this.type = type;
    }

    @Override
    public boolean addEdge(V arg0, V arg1, E arg2) {
        return graph.addEdge(arg0, arg1, arg2);
    }

    @Override
    public E addEdge(V arg0, V arg1) {
        return graph.addEdge(arg0, arg1);
    }

    @Override
    public V addVertex() {
        return graph.addVertex();
    }

    @Override
    public boolean addVertex(V arg0) {
        return graph.addVertex(arg0);
    }

    @Override
    public boolean containsEdge(E arg0) {
        return graph.containsEdge(arg0);
    }

    @Override
    public boolean containsEdge(V arg0, V arg1) {
        return graph.containsEdge(arg0, arg1);
    }

    @Override
    public boolean containsVertex(V arg0) {
        return graph.containsVertex(arg0);
    }

    @Override
    public int degreeOf(V arg0) {
        return graph.degreeOf(arg0);
    }

    @Override
    public Set<E> edgeSet() {
        return graph.edgeSet();
    }

    @Override
    public Set<E> edgesOf(V v) {
        return graph.edgesOf(v);
    }

    @Override
    public Set<E> getAllEdges(V arg0, V arg1) {
        return graph.getAllEdges(arg0, arg1);
    }

    @Override
    public E getEdge(V arg0, V arg1) {
        return graph.getEdge(arg0, arg1);
    }

    @Override
    public V getEdgeSource(E arg0) {
        return graph.getEdgeSource(arg0);
    }

    @Override
    public Supplier<E> getEdgeSupplier() {
        return graph.getEdgeSupplier();
    }

    @Override
    public V getEdgeTarget(E arg0) {
        return graph.getEdgeTarget(arg0);
    }

    @Override
    public GraphType getType() {
        return graph.getType();
    }

    @Override
    public Supplier<V> getVertexSupplier() {
        return graph.getVertexSupplier();
    }

    @Override
    public int inDegreeOf(V arg0) {
        return graph.inDegreeOf(arg0);
    }

    @Override
    public Set<E> incomingEdgesOf(V arg0) {
        return graph.incomingEdgesOf(arg0);
    }

    @Override
    public int outDegreeOf(V arg0) {
        return graph.outDegreeOf(arg0);
    }

    @Override
    public Set<E> outgoingEdgesOf(V arg0) {
        return graph.outgoingEdgesOf(arg0);
    }

    @Override
    public boolean removeAllEdges(Collection<? extends E> arg0) {
        return graph.removeAllEdges(arg0);
    }

    @Override
    public Set<E> removeAllEdges(V arg0, V arg1) {
        return graph.removeAllEdges(arg0, arg1);
    }

    @Override
    public boolean removeAllVertices(Collection<? extends V> arg0) {
        return graph.removeAllVertices(arg0);
    }

    @Override
    public boolean removeEdge(E arg0) {
        return graph.removeEdge(arg0);
    }

    @Override
    public E removeEdge(V arg0, V arg1) {
        return graph.removeEdge(arg0, arg1);
    }

    @Override
    public boolean removeVertex(V arg0) {
        return graph.removeVertex(arg0);
    }

    @Override
    public void setEdgeWeight(E arg0, double arg1) {
        graph.setEdgeWeight(arg0, arg1);
    }

    @Override
    public Set<V> vertexSet() {
        return graph.vertexSet();
    }

    @Override
    public double getEdgeWeight(E edge) {
        Double r;
        if (edgeWeight != null) r = edgeWeight.apply(edge);
        else r = graph.getEdgeWeight(edge);
        return r;
    }

    /**
     * @param vertex es el v�rtice actual
     * @return El peso de vertex
     */
    @Override
    public double getVertexWeight(V vertex) {
        Double r = 0.;
        if (vertexWeight != null) r = vertexWeight.apply(vertex);
        return r;
    }

    /**
     * @param vertex  El v�rtice actual
     * @param edgeIn  Una arista entrante o incidente en el v�rtice actual. Es null en el v�rtice inicial.
     * @param edgeOut Una arista saliente o incidente en el v�rtice actual. Es null en el v�rtice final.
     * @return El peso asociado al v�rtice suponiendo las dos aristas dadas.
     */
    @Override
    public double getVertexPassWeight(V vertex, E edgeIn, E edgeOut) {
        Double r = 0.;
        if (vertexPassWeight != null) r = vertexPassWeight.apply(vertex, edgeIn, edgeOut);
        return r;
    }

    @Override
    public List<E> edgesListOf(V v) {
        if (graph.getType().isUndirected()) return new ArrayList<>(graph.edgesOf(v));
        else return new ArrayList<>(graph.outgoingEdgesOf(v));
    }

    @Override
    public EGraphPath<V, E> initialPath() {
        if (this.path == null) {
            this.path = EGraphPath.ofVertex(this, this.startVertex, this.type);
        }
        return this.path.copy();
    }

    @Override
    public V startVertex() {
        return startVertex;
    }

    @Override
    public PathType pathType() {
        return type;
    }

    @Override
    public Predicate<V> goal() {
        return goal;
    }

    @Override
    public V endVertex() {
        return endVertex;
    }

    @Override
    public Predicate<V> constraint() {
        return constraint;
    }

    @Override
    public V oppositeVertex(E edge, V v) {
        return Graphs.getOppositeVertex(graph, edge, v);
    }

}
