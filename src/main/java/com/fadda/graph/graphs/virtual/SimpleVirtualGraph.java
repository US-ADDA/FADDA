package com.fadda.graph.graphs.virtual;

import com.fadda.common.tri.TriFunction;
import com.fadda.graph.path.EGraphPath;
import com.fadda.graph.path.EGraphPath.PathType;
import org.jgrapht.GraphType;
import org.jgrapht.graph.DefaultGraphType;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * <p> Implementaci�n de un grafo virtual simple
 * Asumimos que los v�rtices son subtipo de VirtualVertex &lt; V,E &gt;
 * Asumimos que las aristas son subtipos de SimpleEdge &lt; V &gt;
 * </p>
 *
 * <p> El grafo es inmutable por lo que no est�n permitadas las operaci�n de modificaci�n. Tampoco
 * est�n permitidas las operaciones de consulta de todos los v�rtices o todas las aristas.
 * Si se invocan alguna de ellas se disparar�
 * la excepci�n UnsupportedOperationException </p>
 *
 * @param <V> El tipo de los v�rtices
 * @param <E> El tipo de las aristas
 * @author Miguel Toro
 * @see VirtualVertex
 */
public class SimpleVirtualGraph<V extends VirtualVertex<V, E, ?>, E extends SimpleEdgeAction<V, ?>> implements EGraph<V, E> {


    private final Set<V> vertexSet;
    private final Function<E, Double> edgeWeight;
    private final Function<V, Double> vertexWeight;
    private final TriFunction<V, E, E, Double> vertexPassWeight;
    private final EGraphPath<V, E> path;
    private final V startVertex;
    private final Predicate<V> goal;
    private final V endVertex;
    private final Predicate<V> constraint;
    private final PathType type;

    public SimpleVirtualGraph(V startVertex, Predicate<V> goal, PathType type, Function<V, Double> vertexWeight,
                              Function<E, Double> edgeWeight, V endVertex, Predicate<V> constraint,
                              TriFunction<V, E, E, Double> vertexPassWeight) {
        super();
        this.edgeWeight = edgeWeight;
        this.vertexWeight = vertexWeight;
        this.vertexPassWeight = vertexPassWeight;
        this.startVertex = startVertex;
        this.vertexSet = new HashSet<>();
        this.vertexSet.add(this.startVertex);
        this.type = type;
        this.goal = goal;
        this.endVertex = endVertex;
        this.constraint = constraint;
        this.path = EGraphPath.ofVertex(this, this.startVertex, this.type);
    }


    public static <V extends VirtualVertex<V, E, A>, E extends SimpleEdgeAction<V, A>, A> EGraph<V, E> last(
            V startVertex, Predicate<V> goal, Function<V, Double> vertexWeight) {

        return of(startVertex, goal, PathType.Last, vertexWeight, null, null, v -> true, null);
    }


    public static <V extends VirtualVertex<V, E, A>, E extends SimpleEdgeAction<V, A>, A> EGraph<V, E> last(

            V startVertex, Predicate<V> goal, Function<V, Double> vertexWeight, Predicate<V> constraint) {

        return of(startVertex, goal, PathType.Last, vertexWeight, null, null, constraint, null);

    }

    public static <V extends VirtualVertex<V, E, A>, E extends SimpleEdgeAction<V, A>, A> EGraph<V, E> last(


            V startVertex, Predicate<V> goal, Function<V, Double> vertexWeight, V endVertex) {
        return of(startVertex, goal, PathType.Last, vertexWeight, null, endVertex, v -> true, null);

    }


    public static <V extends VirtualVertex<V, E, A>, E extends SimpleEdgeAction<V, A>, A> EGraph<V, E> sum(

            V startVertex, Predicate<V> goal, Function<E, Double> edgeWeight) {
        return of(startVertex, goal, PathType.Sum, null, edgeWeight, null, v -> true, null);

    }

    public static <V extends VirtualVertex<V, E, A>, E extends SimpleEdgeAction<V, A>, A> EGraph<V, E> sum(
            V startVertex, Predicate<V> goal, Function<E, Double> edgeWeight, Predicate<V> constraint) {
        return of(startVertex, goal, PathType.Sum, null, edgeWeight, null, constraint, null);
    }

    public static <V extends VirtualVertex<V, E, A>, E extends SimpleEdgeAction<V, A>, A> EGraph<V, E> sum(
            V startVertex, Predicate<V> goal, Function<E, Double> edgeWeight, V endVertex) {
        return of(startVertex, goal, PathType.Sum, null, edgeWeight, endVertex, v -> true, null);
    }

    public static <V extends VirtualVertex<V, E, A>, E extends SimpleEdgeAction<V, A>, A>
    SimpleVirtualGraph<V, E> of(
            V startVertex,
            Predicate<V> goal,
            PathType type,
            Function<V, Double> vertexWeight,
            Function<E, Double> edgeWeight,
            V endVertex,
            Predicate<V> constraint,
            TriFunction<V, E, E, Double> vertexPassWeight) {
        return new SimpleVirtualGraph<>(startVertex, goal, type, vertexWeight, edgeWeight, endVertex, constraint, vertexPassWeight);
    }

    @Override
    public boolean containsEdge(E e) {
        return e.source().isNeighbor(e.target());
    }

    @Override
    public boolean containsEdge(V v1, V v2) {
        return v1.isNeighbor(v2);
    }

    @Override
    public boolean containsVertex(V v) {
        return v.isValid();
    }

    @Override
    public V getEdgeSource(E e) {
        return e.source();
    }

    @Override
    public V getEdgeTarget(E e) {
        return e.target();
    }

    /**
     * @param edge es una arista
     * @return El peso de edge
     */
    @Override
    public double getEdgeWeight(E edge) {
        Double r;
        if (edgeWeight != null) r = edgeWeight.apply(edge);
        else r = edge.weight();
        return r;
    }

    /**
     * @param vertex es el v�rtice actual
     * @return El peso de vertex
     */
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
    public double getVertexPassWeight(V vertex, E edgeIn, E edgeOut) {
        Double r = 0.;
        if (vertexPassWeight != null) r = vertexPassWeight.apply(vertex, edgeIn, edgeOut);
        return r;
    }


    @Override
    public E getEdge(V v1, V v2) {
        return v1.getEdgeToVertex(v2);
    }

    @Override
    public Set<E> getAllEdges(V v1, V v2) {
        Set<E> s = new HashSet<>();
        if (Boolean.TRUE.equals(v1.isNeighbor(v2))) s = Set.of(this.getEdge(v1, v2));
        return s;
    }

    /**
     * @return Conjunto de v�rtices del grafo que se han hecho expl�citos en el constructor.
     */
    @Override
    public Set<V> vertexSet() {
        return this.vertexSet;
    }

    @Override
    public Set<E> edgesOf(V v) {
        return v.edgesOf();
    }

    @Override
    public List<E> edgesListOf(V v) {
        return v.edgesListOf();
    }

    public V oppositeVertex(E edge, V v) {
        return edge.otherVertex(v);
    }

    @Override
    public int degreeOf(V v) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int outDegreeOf(V v) {
        return v.edgesListOf().size();
    }

    @Override
    public Set<E> outgoingEdgesOf(V v) {
        return new HashSet<>(v.edgesListOf());
    }

    @Override
    public void setEdgeWeight(E edge, double weight) {
        throw new UnsupportedOperationException();
    }

    /**
     * @throw UnsupportedOperationException
     */
    @Override
    public int inDegreeOf(V v) {
        throw new UnsupportedOperationException();
    }

    /**
     * @throw UnsupportedOperationException
     */
    @Override
    public Set<E> incomingEdgesOf(V v) {
        throw new UnsupportedOperationException();
    }


    /**
     * @throw UnsupportedOperationException
     */
    @Override
    public GraphType getType() {
        return new DefaultGraphType.Builder().build().asDirected();
    }

    /**
     * @throw UnsupportedOperationException
     */
    @Override
    public Set<E> edgeSet() {
        throw new UnsupportedOperationException();
    }

    /**
     * @throw UnsupportedOperationException
     */
    @Override
    public E addEdge(V arg0, V arg1) {
        throw new UnsupportedOperationException();
    }


    /**
     * @throw UnsupportedOperationException
     */
    @Override
    public boolean addEdge(V arg0, V arg1, E arg2) {
        throw new UnsupportedOperationException();
    }


    /**
     * @throw UnsupportedOperationException
     */
    @Override
    public boolean addVertex(V arg0) {
        throw new UnsupportedOperationException();
    }

    /**
     * @throw UnsupportedOperationException
     */
    @Override
    public boolean removeAllEdges(Collection<? extends E> arg0) {
        throw new UnsupportedOperationException();
    }

    /**
     * @throw UnsupportedOperationException
     */
    @Override
    public Set<E> removeAllEdges(V arg0, V arg1) {
        throw new UnsupportedOperationException();
    }

    /**
     * @throw UnsupportedOperationException
     */
    @Override
    public boolean removeAllVertices(Collection<? extends V> arg0) {
        throw new UnsupportedOperationException();
    }

    /**
     * @throw UnsupportedOperationException
     */
    @Override
    public boolean removeEdge(E arg0) {
        throw new UnsupportedOperationException();
    }

    /**
     * @throw UnsupportedOperationException
     */
    @Override
    public E removeEdge(V arg0, V arg1) {
        throw new UnsupportedOperationException();
    }

    /**
     * @throw UnsupportedOperationException
     */
    @Override
    public boolean removeVertex(V arg0) {
        throw new UnsupportedOperationException();
    }

    @Override
    public V addVertex() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Supplier<E> getEdgeSupplier() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Supplier<V> getVertexSupplier() {
        throw new UnsupportedOperationException();
    }

    @Override
    public EGraphPath<V, E> initialPath() {
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


}
