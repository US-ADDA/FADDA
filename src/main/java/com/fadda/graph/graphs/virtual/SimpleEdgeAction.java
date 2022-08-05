package com.fadda.graph.graphs.virtual;

import com.fadda.graph.graphs.SimpleEdge;

public interface SimpleEdgeAction<V, A> extends SimpleEdge<V> {


    static <V, A> SimpleEdgeAction<V, A> of(V c1, V c2, A action, Double weight) {
        return new ActionSimpleEdgeR<>(c1, c2, action, weight);
    }


    A action();

    record ActionSimpleEdgeR<V, A>(V source, V target, A action, Double weight)
            implements SimpleEdgeAction<V, A> {
    }

}
