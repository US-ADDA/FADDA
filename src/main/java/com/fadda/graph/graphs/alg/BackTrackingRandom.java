package com.fadda.graph.graphs.alg;

import com.fadda.common.Preconditions;
import com.fadda.common.extension.List2;
import com.fadda.graph.graphs.virtual.EGraph;
import com.fadda.math.Math2;
import org.jgrapht.GraphPath;

import java.util.List;
import java.util.function.Function;

public class BackTrackingRandom<V, E, S extends Comparable<S>> extends BackTracking<V, E, S> {

    public static Integer threshold;
    public static Integer solutionsNumber;
    private static Boolean work = true;
    protected final Function<V, Integer> size;
    public Integer iterations;

    BackTrackingRandom(EGraph<V, E> graph,
                       Function<GraphPath<V, E>, S> solution,
                       BTType type,
                       Function<V, Integer> size) {
        super(graph, null, solution, type);
        this.size = size;
        Preconditions.checkNotNull(graph.goal(), "El predicado no puede ser null");
    }


    public static <V, E, S extends Comparable<S>> BackTrackingRandom<V, E, S> of(
            EGraph<V, E> graph,
            Function<GraphPath<V, E>, S> solution,
            BTType type,
            Function<V, Integer> size) {
        return new BackTrackingRandom<>(graph, solution, type, size);
    }


    @Override
    protected void update(State<V, E> state) {
        S s = solution.apply(state.getPath());
        if (s != null) this.solutions.add(s);
        BackTrackingRandom.work = super.solutions.size() < BackTrackingRandom.solutionsNumber;
    }

    @Override
    public void search() {
        State<V, E> initialState = StatePath.of(graph, graph.goal(), graph.endVertex());
        this.iterations = 0;
        Math2.initialRandom();
        while (BackTrackingRandom.work) {
            this.iterations++;
            search(initialState);
        }
    }

    @Override
    public void search(State<V, E> state) {
        V actual = state.getActualVertex();
        if (graph.goal().test(actual)) update(state);
        else {
            List<E> edges = graph.edgesListOf(actual);
            if (size.apply(actual) > BackTrackingRandom.threshold) edges = List2.randomUnitary(edges);
            for (E edge : edges) {
                state.forward(edge);
                search(state);
                if (!BackTrackingRandom.work) return;
                state.back(edge);
            }
        }
    }


}
