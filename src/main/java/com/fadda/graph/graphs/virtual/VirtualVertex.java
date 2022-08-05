package com.fadda.graph.graphs.virtual;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public interface VirtualVertex<V extends VirtualVertex<V, E, A>, E extends SimpleEdgeAction<V, A>, A> {

    List<A> actions();

    V neighbor(A a);

    E edge(A a);

    /**
     * @param v Otro v&eacute;rtice
     * @return La arista desde this start v2
     */
    default E getEdgeToVertex(V v) {
        return this.edgesOf().stream()
                .filter(e -> e.source().equals(v) || e.target().equals(v))
                .findFirst()
                .get();
    }

    /**
     * Este m&eacute;todo podr&iacute;start ser sobrescrito en la clase que refine al tipo
     *
     * @return El conjunto de los vecinos
     */
    default Set<V> getNeighborListOf() {
        return actions().stream()
                .map(this::neighbor)
                .collect(Collectors.toSet());
    }

    /**
     * Este m&eacute;todo podr&iacute;start ser sobrescrito en la clase que refine al tipo
     *
     * @return El conjunto de las aristas hacia los vecinos
     */
    default Set<E> edgesOf() {
        return actions().stream()
                .map(this::edge)
                .collect(Collectors.toSet());
    }

    /**
     * Este m&eacute;todo podr&iacute;start ser sobrescrito en la clase que refine al tipo
     *
     * @return El conjunto de las aristas hacia los vecinos
     */
    default List<E> edgesListOf() {
        return actions().stream()
                .map(this::edge)
                .toList();
    }

    default Boolean isNeighbor(V v) {
        return this.getNeighborListOf().contains(v);
    }

    default Boolean isValid() {
        return true;
    }

}
