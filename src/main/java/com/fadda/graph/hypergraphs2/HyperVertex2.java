package com.fadda.graph.hypergraphs2;

import com.fadda.graph.graphs.alg.DynamicProgramming.Sp;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public interface HyperVertex2<V extends HyperVertex2<V, E, A, S>, E extends HyperEdge2<V, E, A, S>, A, S> {

    List<A> actions();

    Boolean isBaseCase();

    Double baseCaseSolutionWeight();

    S baseCaseSolution();

    V me();

    List<V> neighbors(A a);

    E edge(A a);


    default Boolean isValid() {
        return true;
    }


    default Data<V, E, A> datos() {
        return Data.get();
    }

    default Sp<E> solutionWeight() {
        Comparator<Sp<E>> cmp = Data.type.equals(Data.DpType.Min) ? Comparator.naturalOrder() : Comparator.reverseOrder();
        Sp<E> r;
        if (datos().contains(me()))
            r = datos().get(me());
        else {
            if (Boolean.TRUE.equals(this.isBaseCase())) {
                Double br = baseCaseSolutionWeight();
                if (br == null)
                    r = null;
                else
                    r = Sp.of(br, null);
            } else if (this.edgesOf().isEmpty()) {
                r = null;
            } else {
                this.edgesOf().stream().map(HyperEdge2::solutionWeight).filter(Objects::nonNull)
                        .forEach(e -> datos().putAll(me(), e));
                r = this.edgesOf().stream().map(HyperEdge2::solutionWeight).filter(Objects::nonNull)
                        .min(cmp).orElse(null);
            }
            datos().put(me(), r);
        }
        return r;
    }

    default S solution() {
        Sp<E> sp = this.solutionWeight();
        S r;
        if (Boolean.TRUE.equals(this.isBaseCase()))
            r = this.baseCaseSolution();
        else {
            r = sp.edge().solution();
        }
        return r;
    }

    /**
     * Este m&eacute;todo podr&iacute;start ser sobrescrito en la clase que refine al
     * tipo
     *
     * @return El conjunto de las aristas hacia los vecinos
     */
    default List<E> edgesOf() {
        return this.actions().stream().map(this::edge).toList();
    }

    default GraphTree2<V, E, A, S> graphTree() {
        return GraphTree2.of(this.me(), this.solutionWeight().edge().action());
    }
}
