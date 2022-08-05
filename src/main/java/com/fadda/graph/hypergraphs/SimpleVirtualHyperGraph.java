package com.fadda.graph.hypergraphs;

import java.util.List;
import java.util.Set;

/**
 * <p> Implementaci�n de un hypergrafo virtual simple
 * Asumimos que los v�rtices son subtipo de VirtualHyperVertex &lt; V,E &gt;
 * Asumimos que las aristas son subtipos de SimpleHyperEdge &lt; V &gt;
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
 */

public record SimpleVirtualHyperGraph<V extends VirtualHyperVertex<V, E, A>, E extends SimpleHyperEdge<V, E, A>, A>(
        V startVertex) {


    public static <V extends VirtualHyperVertex<V, E, A>, E extends SimpleHyperEdge<V, E, A>, A> SimpleVirtualHyperGraph<V, E, A>
    simpleVirtualHyperGraph(V start) {
        return new SimpleVirtualHyperGraph<>(start);
    }


    public boolean containsEdge(E e) {

        return e.source().actions().contains(e.action());
    }


    public boolean containsEdge(V v1, V v2) {


        return this.edgesOf(v1).stream().anyMatch(e -> e.targets().contains(v2));

    }

    public boolean containsVertex(V v) {
        return v.isValid();
    }

    public V getEdgeSource(E e) {

        return e.source();
    }


    public List<V> getEdgeTargets(E e) {

        return e.targets();
    }

    public double getEdgeWeight(E e, List<Double> weights) {


        return e.weight(weights);
    }


    public E getEdge(V v1, V v2) {
        return this.edgesOf(v1).stream().filter(e -> e.targets().contains(v2)).findFirst().get();

    }

    /**
     * @return Conjunto de v�rtices del grafo que se han hecho expl�citos en el constructor.
     */

    public Set<V> vertexSet() {
        return Set.of(this.startVertex);
    }

    public List<E> edgesOf(V v) {
        return v.edgesOf();
    }

    public int degreeOf(V v) {
        return v.edgesOf().size();
    }

    public int outDegreeOf(V v) {
        return v.edgesOf().size();
    }

    public List<E> outgoingEdgesOf(V v) {
        return edgesOf(v);
    }

    public Boolean isBaseCase(V v) {
        return v.isBaseCase();
    }

    public Double baseCaseSolution(V v) {
        return v.baseCaseSolution();
    }

}
