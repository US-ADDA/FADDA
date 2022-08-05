package com.fadda.graph.graphs.alg;

import com.fadda.common.tri.TriFunction;
import com.fadda.graph.graphs.Graphs2;
import com.fadda.graph.graphs.virtual.EGraph;
import com.fadda.graph.path.EGraphPath;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.Graphs;

import java.util.*;
import java.util.function.Predicate;

public class DynamicProgrammingReduction<V, E> {

    public final EGraph<V, E> graph;
    public final Boolean withGraph = false;
    private final TriFunction<V, Predicate<V>, V, Double> heuristic;
    private final Comparator<Sp<E>> comparatorEdges;
    private final Comparator<Double> comparator;
    private final EGraphPath<V, E> path;
    public Double bestValue = null;
    public Map<V, Sp<E>> solutionsTree;
    public GraphPath<V, E> optimalPath;
    public Graph<V, E> outGraph;

    DynamicProgrammingReduction(EGraph<V, E> g,
                                TriFunction<V, Predicate<V>, V, Double> heuristic, DynamicProgramming.PDType type) {
        this.graph = g;
        this.heuristic = heuristic;
        this.comparatorEdges = type == DynamicProgramming.PDType.Min ? Comparator.naturalOrder() : Comparator.reverseOrder();
        this.comparator = type == DynamicProgramming.PDType.Min ? Comparator.naturalOrder() : Comparator.reverseOrder();
        this.solutionsTree = new HashMap<>();
        this.path = graph.initialPath();
    }


    public static <V, E> DynamicProgrammingReduction<V, E> of(
            EGraph<V, E> graph,
            TriFunction<V, Predicate<V>, V, Double> heuristic,
            DynamicProgramming.PDType type) {

        return new DynamicProgrammingReduction<>(graph, heuristic, type);
    }


    public Optional<GraphPath<V, E>> optimalPath() {
        return Optional.ofNullable(this.optimalPath);
    }

    private Boolean forget(E edge, V actual, Double accumulateValue, Predicate<V> goal, V end) {
        boolean r = false;
        Double w = this.path.boundedValue(accumulateValue, actual, edge, goal, end, this.heuristic);
        if (this.bestValue != null) r = comparator.compare(w, this.bestValue) > 0;
        return r;
    }

    protected void update(Double accumulateValue) {
        if (this.bestValue == null || comparator.compare(accumulateValue, this.bestValue) < 0) {
            this.bestValue = accumulateValue;
        }
    }

    public void startGraph() {
        if (Boolean.TRUE.equals(this.withGraph)) outGraph = Graphs2.simpleDirectedGraph();
    }

    private void addGraph(V v, E edge) {
        if (Boolean.TRUE.equals(withGraph)) {
            V v2 = Graphs.getOppositeVertex(graph, edge, v);

            if (!this.outGraph.containsVertex(v)) this.outGraph.addVertex(v);
            if (!this.outGraph.containsVertex(v2)) this.outGraph.addVertex(v2);
            if (!this.outGraph.containsEdge(edge)) this.outGraph.addEdge(v, v2, edge);

        }
    }

    public Optional<GraphPath<V, E>> search() {
        startGraph();
        this.solutionsTree = new HashMap<>();
        search(graph.startVertex(), 0., null);
        return pathFrom(graph.startVertex());
    }

    private Sp<E> search(V actual, Double accumulateValue, E edgeToOrigin) {
        Sp<E> r = null;
        if (this.solutionsTree.containsKey(actual)) {
            r = this.solutionsTree.get(actual);
        } else if (graph.goal().test(actual)) {
            if (graph.constraint().test(actual)) {
                update(accumulateValue);
                r = Sp.of(path.goalBaseSolution(actual), null);
            }
            this.solutionsTree.put(actual, r);
        } else {
            List<Sp<E>> rs = new ArrayList<>();
            for (E edge : graph.edgesListOf(actual)) {
                if (Boolean.TRUE.equals(this.forget(edge, actual, accumulateValue, graph.goal(), graph.endVertex())))
                    continue;
                V v = Graphs.getOppositeVertex(graph, edge, actual);
                Double ac = this.path.add(accumulateValue, actual, edge, edgeToOrigin);
                Sp<E> s = search(v, ac, edge);
                if (s != null) {
                    E lastEdge = this.solutionsTree.get(v).edge;
                    Double spv = this.path.fromNeighbordSolution(s.weight, v, edge, lastEdge);
                    Sp<E> sp = Sp.of(spv, edge);
                    rs.add(sp);
                }
                addGraph(actual, edge);
            }
            r = rs.stream().filter(Objects::nonNull).min(this.comparatorEdges).orElse(null);
            this.solutionsTree.put(actual, r);
        }
        return r;
    }

    private Optional<GraphPath<V, E>> pathFrom(V vertex) {
        if (this.solutionsTree.get(vertex) == null && this.optimalPath != null)
            return Optional.of(this.optimalPath);
        if (this.solutionsTree.get(vertex) == null) return Optional.empty();
        E edge = this.solutionsTree.get(vertex).edge;
        EGraphPath<V, E> ePath = graph.initialPath();
        while (edge != null) {
            ePath.add(edge);
            vertex = Graphs.getOppositeVertex(graph, edge, vertex);
            edge = this.solutionsTree.get(vertex).edge;
        }
        this.optimalPath = ePath;
        return Optional.of(ePath);
    }


    public record Sp<E>(Double weight, E edge) implements Comparable<Sp<E>> {

        public static <E> Sp<E> of(Double weight, E edge) {
            return new Sp<>(weight, edge);
        }

        @Override
        public int compareTo(Sp<E> sp) {
            return this.weight.compareTo(sp.weight);
        }

    }
}
