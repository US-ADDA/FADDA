package com.fadda.graph.hypergraphs;

import java.util.List;

public interface VirtualHyperVertex<V, E, A> {

    List<A> actions();

    Boolean isBaseCase();

    Double baseCaseSolution();

    Boolean isValid();

    /**
     * @param a Una acci&oacute;n
     * @return El vecino del v&eacute;rtice siguiendo esa acci&oacute;n
     * @pre La acci&oacute;n start debe ser aplicable
     * @post El v&eacute;rtice retornada debe ser distinto al original y v&aacute;lido
     */
    List<V> neighbors(A a);

    /**
     * Este m&eacute;todo debe ser sobrescrito en la clase que refine el tipo
     *
     * @param a Acci&oacute;n
     * @return La arista que lleva al vecino siguiendo esta acci&oacute;n
     */
    E edge(A a);


    /**
     * Este m&eacute;todo podr&iacute;start ser sobrescrito en la clase que refine al tipo
     *
     * @return El conjunto de los vecinos
     */
    default List<List<V>> getNeighborListOf() {
        return this.actions()
                .stream()
                .map(this::neighbors)
                .toList();
    }


    /**
     * Este m&eacute;todo podr&iacute;start ser sobrescrito en la clase que refine al tipo
     *
     * @return El conjunto de las aristas hacia los vecinos
     */
    default List<E> edgesOf() {
        return this.actions()
                .stream()
                .map(this::edge)
                .toList();
    }

}
