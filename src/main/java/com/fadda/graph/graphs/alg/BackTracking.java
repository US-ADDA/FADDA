package com.fadda.graph.graphs.alg;


import com.fadda.common.Preconditions;
import com.fadda.common.extension.List2;
import com.fadda.common.tri.TriFunction;
import com.fadda.graph.graphs.Graphs2;
import com.fadda.graph.graphs.virtual.EGraph;
import com.fadda.graph.path.EGraphPath;
import org.jgrapht.GraphPath;
import org.jgrapht.Graphs;
import org.jgrapht.graph.SimpleDirectedGraph;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class BackTracking<V, E, S> {

    public final BTType type;
    public final EGraph<V, E> graph;
    public final Set<S> solutions;
    public final Boolean withGraph = false;
    protected final TriFunction<V, Predicate<V>, V, Double> heuristic;
    protected final Function<GraphPath<V, E>, S> solution;
    private final Comparator<Double> comparator;
    public Double bestValue;
    public GraphPath<V, E> optimalPath;
    private SimpleDirectedGraph<V, E> outGraph;

    BackTracking(EGraph<V, E> graph, TriFunction<V, Predicate<V>, V, Double> heuristic,
                 Function<GraphPath<V, E>, S> solution, BTType type) {
        this.type = type;
        this.graph = graph;
        this.heuristic = heuristic;
        this.solutions = new HashSet<>();
        this.solution = solution;
        this.comparator = switch (this.type) {
            case All, One -> null;
            case Max -> Comparator.reverseOrder();
            case Min -> Comparator.naturalOrder();
        };
        Preconditions.checkNotNull(graph.goal(), "El predicado no puede ser null");
    }


    public static <V, E, S> BackTracking<V, E, S> of(
            EGraph<V, E> graph,
            TriFunction<V, Predicate<V>, V, Double> heuristic,
            Function<GraphPath<V, E>, S> solution,
            BTType type) {
        return new BackTracking<>(graph, heuristic, solution, type);
    }


    protected Boolean forget(State<V, E> state, E edge) {
        boolean r = false;
        Double w = state.getPath().boundedValue(state.getAccumulateValue(), state.getActualVertex(),
                edge, graph.goal(), graph.endVertex(), heuristic);
        if (this.bestValue != null) r = comparator.compare(w, this.bestValue) > 0;
        return r;
    }

    protected void update(State<V, E> state) {
        if (graph.constraint().test(state.getActualVertex())) {
            if (this.type == BTType.All || this.type == BTType.One) {
                S s = solution.apply(state.getPath());
                this.solutions.add(s);
            } else if (this.type == BTType.Min || this.type == BTType.Max) {
                if (this.bestValue == null || this.comparator.compare(state.getAccumulateValue(), this.bestValue) < 0) {
                    this.bestValue = state.getAccumulateValue();
                    this.optimalPath = state.getPath();
                }
            }
        }
    }

    private void initialGraph() {
        if (Boolean.TRUE.equals(this.withGraph)) this.outGraph = Graphs2.simpleDirectedGraph();
    }

    private void addGraph(V v, E edge) {
        if (Boolean.TRUE.equals(withGraph)) {
            V v2 = Graphs.getOppositeVertex(graph, edge, v);
            if (!this.outGraph.containsVertex(v)) this.outGraph.addVertex(v);
            if (!this.outGraph.containsVertex(v2)) this.outGraph.addVertex(v2);

            if (!this.outGraph.containsEdge(edge)) this.outGraph.addEdge(v, v2, edge);
        }
    }


    public SimpleDirectedGraph<V, E> graph() {
        return this.outGraph;

    }


    public void search() {
        initialGraph();
        State<V, E> initialState = StatePath.of(graph, graph.goal(), graph.endVertex());
        search(initialState);
    }

    public void search(State<V, E> state) {
        V actual = state.getActualVertex();
        if (graph.goal().test(actual)) {
            this.update(state);
        } else {
            for (E edge : graph.edgesListOf(actual)) {
                if (Boolean.TRUE.equals(this.forget(state, edge))) continue;
                state.forward(edge);
                search(state);
                addGraph(actual, edge);
                state.back(edge);
            }
        }
    }


    public Optional<S> getSolution() {
        return switch (this.type) {
            case All, One -> this.solutions.stream().findAny();


            case Max, Min -> this.optimalPath().map(this.solution::apply);
        };
    }


    public Set<S> getSolutions() {
        return this.solutions;
    }

    public Optional<GraphPath<V, E>> optimalPath() {
        return Optional.ofNullable(this.optimalPath);
    }

    public String toStringSolutions() {
        return this.solutions.stream().sorted().map(Object::toString).collect(Collectors.joining("\n"));
    }

    public enum BTType {Min, Max, All, One}

    public interface State<V, E> {
        void forward(E edge);

        void back(E edge);

        Double getAccumulateValue();

        EGraphPath<V, E> getPath();

        V getActualVertex();
    }


    public static class StatePath<V, E> implements State<V, E> {
        private final EGraphPath<V, E> path;
        private final EGraph<V, E> graph;
        private final List<E> edges;
        private final List<Double> weights;
        private V actualVertex;
        private Double accumulateValue;

        public StatePath(EGraph<V, E> graph, Predicate<V> goal, V end) {
            super();
            this.actualVertex = graph.startVertex();
            this.graph = graph;
            this.path = graph.initialPath();
            this.edges = new ArrayList<>();
            this.weights = new ArrayList<>();
            this.accumulateValue = this.path.getWeight();
        }

        public static <V, E> State<V, E> of(EGraph<V, E> graph, Predicate<V> goal, V end) {
            return new StatePath<>(graph, goal, end);
        }

        @Override
        public void forward(E edge) {
            E lastEdge = edges.isEmpty() ? null : List2.last(edges);
            this.accumulateValue = this.path.add(this.accumulateValue, this.actualVertex, edge, lastEdge);
            this.actualVertex = Graphs.getOppositeVertex(graph, edge, this.actualVertex);
            this.edges.add(edge);
            this.weights.add(this.accumulateValue);
        }

        @Override
        public void back(E edge) {
            this.actualVertex = Graphs.getOppositeVertex(graph, edge, this.actualVertex);
            this.edges.remove(this.edges.size() - 1);
            this.weights.remove(this.weights.size() - 1);
            this.accumulateValue = !this.weights.isEmpty() ? List2.last(this.weights) : graph.initialPath().getWeight();
        }

        @Override
        public Double getAccumulateValue() {
            return this.accumulateValue;
        }

        @Override
        public EGraphPath<V, E> getPath() {
            EGraphPath<V, E> ePath = graph.initialPath();
            for (E e : this.edges) {
                ePath.add(e);
            }
            return ePath;
        }

        @Override
        public V getActualVertex() {
            return actualVertex;
        }

        @Override
        public String toString() {
            return String.format("%s,\n%.2f,\n%s",
                    this.actualVertex, this.getAccumulateValue(),
                    this.getPath().getEdgeList().stream().map(Object::toString).collect(Collectors.joining(",", "{", "}")));
        }
    }
}
