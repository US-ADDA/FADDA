package com.fadda.graph.hypergraphs2;

import com.fadda.graph.graphs.alg.DynamicProgramming.Sp;

import java.util.List;

public interface HyperEdge2<V extends HyperVertex2<V, E, A, S>,
        E extends HyperEdge2<V, E, A, S>, A, S> {
    V source();

    A action();

    Double solutionWeight(List<Double> solutions);

    S solution(List<S> solutions);

    E me();

    default List<V> targets() {
        return this.source().neighbors(this.action());
    }

    default Sp<E> solutionWeight() {
        List<Sp<E>> ls = this.targets().stream().map(HyperVertex2::solutionWeight).toList();
        Double weight;
        if (ls.contains(null)) return null;
        else {
            weight = this.solutionWeight(ls.stream().map(Sp::weight).toList());
            return Sp.of(weight, me());
        }
    }

    default S solution() {
        return solution(this.targets().stream().map(HyperVertex2::solution).toList());
    }
}
