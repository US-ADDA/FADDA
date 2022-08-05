package com.fadda.graph.graphs;

import com.fadda.common.Preconditions;

public interface SimpleEdge<V> {

    /**
     * @param v1     Un v�rtice
     * @param v2     Un segundo v�rtice
     * @param weight El peso de la arista
     * @param <V>    el tipo de los v�rtices
     * @return Una arista entre ambos v�rtices
     */
    static <V> SimpleEdge<V> of(V v1, V v2, Double weight) {
        return new SimpleEdgeR<>(v1, v2, weight);
    }


    /**
     * @param v1  Un v�rtice
     * @param v2  Un segundo v�rtice
     * @param <V> el tipo de los v�rtices
     * @return Una arista entre ambos v�rtices
     */
    static <V> SimpleEdge<V> of(V v1, V v2) {
        return new SimpleEdgeR<>(v1, v2, 1.);
    }


    V source();

    V target();

    Double weight();

    /**
     * @param v Un v�rtice de la arista
     * @return El otro v�rtice
     */

    default V otherVertex(V v) {
        Preconditions.checkNotNull(v, "El v�rtice no puede ser null");
        V r = null;
        if (v.equals(this.source())) r = this.target();
        else if (v.equals(this.target())) r = this.source();
        return r;
    }

    record SimpleEdgeR<V>(V source, V target, Double weight) implements SimpleEdge<V> {
    }

}
