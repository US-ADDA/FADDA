package com.fadda.graph.graphs;


import com.fadda.common.Preconditions;
import com.fadda.common.tri.TriFunction;
import com.fadda.graph.graphs.virtual.EGraph;
import com.fadda.graph.graphs.virtual.EGraphI;
import com.fadda.graph.path.EGraphPath;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.Graphs;
import org.jgrapht.graph.*;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;


public class Graphs2 {

    public static final Object constraintG = (Predicate<Object>) v -> true;
    public static final Object endVertexG = null;
    public static final Object vertexPassWeightG = null;

    public static <V, E> Double weightToVertex(Graph<V, E> graph, V v1, V v2) {
        E e = graph.getEdge(v1, v2);
        return graph.getEdgeWeight(e);
    }


    public static <V, E> V closestVertex(Graph<V, E> graph, V vertex) {
        return closestVertex(graph, vertex, v -> true);
    }


    public static <V, E> V closestVertex(Graph<V, E> graph, V vertex, Predicate<V> p) {
        return (Graphs.neighborSetOf(graph, vertex)).stream()
                .filter(p)

                .min(Comparator.comparingDouble(v -> weightToVertex(graph, vertex, v)))
                .get();
    }

    public static <V, E> V furthestVertex(Graph<V, E> graph, V vertex) {

        return furthestVertex(graph, vertex, v -> true);
    }


    public static <V, E> V furthestVertex(Graph<V, E> graph, V vertex, Predicate<V> p) {

        return (Graphs.neighborSetOf(graph, vertex)).stream()
                .filter(p)


                .max(Comparator.comparingDouble(v -> weightToVertex(graph, vertex, v)))


                .get();
    }

    public static <V, E> SimpleGraph<V, E> simpleGraph() {
        return new SimpleGraph<>(null, null, false);
    }

    public static <V, E> SimpleGraph<V, E> simpleGraph(Supplier<V> vs, Supplier<E> es, boolean weighted) {
        return new SimpleGraph<>(vs, es, weighted);
    }

    public static <V, E> SimpleWeightedGraph<V, E> simpleWeightedGraph() {
        return new SimpleWeightedGraph<>(null, null);
    }


    public static <V, E> SimpleWeightedGraph<V, E> simpleWeightedGraph(Supplier<V> vs, Supplier<E> es) {
        return new SimpleWeightedGraph<>(vs, es);
    }

    public static <V, E> SimpleDirectedGraph<V, E> simpleDirectedGraph() {

        return new SimpleDirectedGraph<>(null, null, false);
    }


    public static <V, E> SimpleDirectedWeightedGraph<V, E> simpleDirectedWeightedGraph() {
        return new SimpleDirectedWeightedGraph<>(null, null);
    }


    public static <V, E> DirectedWeightedMultigraph<V, E> directedWeightedMultigraph() {

        return new DirectedWeightedMultigraph<>(null, null);
    }

    public static <V, E> Set<V> getVertices(Graph<V, E> graph, E edge) {
        return Set.of(graph.getEdgeSource(edge), graph.getEdgeTarget(edge));
    }

    @SuppressWarnings("unchecked")
    public static <V, E, G extends Graph<V, E>> EGraph<V, E> eGraph(G graph, V startVertex,
                                                                    Predicate<V> goal,
                                                                    Function<E, Double> edgeWeight,
                                                                    Function<V, Double> vertexWeight,
                                                                    EGraphPath.PathType type) {
        return new EGraphI<>(graph,
                startVertex, goal,
                (V) endVertexG,
                (Predicate<V>) constraintG,
                type, edgeWeight, vertexWeight,
                (TriFunction<V, E, E, Double>) vertexPassWeightG);
    }


    public static <V, E, G extends Graph<V, E>> EGraph<V, E> eGraphSum(G graph, V startVertex, Predicate<V> goal, Function<E, Double> edgeWeight) {
        return eGraph(graph, startVertex, goal, edgeWeight, null, EGraphPath.PathType.Sum);
    }

    public static <V, E, G extends Graph<V, E>> EGraph<V, E> eGraphLast(G graph, V startVertex, Predicate<V> goal,

                                                                        Function<V, Double> vertexWeight) {


        return eGraph(graph, startVertex, goal, null, vertexWeight, EGraphPath.PathType.Last);
    }

    public static <V, E> SimpleDirectedGraph<V, E> inversedDirectedGraph(SimpleDirectedGraph<V, E> graph) {
        SimpleDirectedGraph<V, E> gs = Graphs2.simpleDirectedGraph();
        for (V v : graph.vertexSet()) {
            gs.addVertex(v);
        }
        for (E e : graph.edgeSet()) {
            V s = graph.getEdgeSource(e);
            V t = graph.getEdgeTarget(e);
            gs.addEdge(t, s, e);
        }
        return gs;
    }


    public static <V, E> SimpleDirectedWeightedGraph<V, E> toDirectedWeightedGraph(SimpleWeightedGraph<V, E> graph,

                                                                                   Function<E, E> edgeReverse) {
        SimpleDirectedWeightedGraph<V, E> gs = new SimpleDirectedWeightedGraph<>(graph.getVertexSupplier(),
                graph.getEdgeSupplier());


        for (V v : graph.vertexSet()) {
            gs.addVertex(v);
        }
        for (E e : graph.edgeSet()) {
            V s = graph.getEdgeSource(e);
            V t = graph.getEdgeTarget(e);

            double w = graph.getEdgeWeight(e);
            gs.addEdge(s, t, e);
            gs.setEdgeWeight(e, w);
            E e1 = edgeReverse.apply(e);
            gs.addEdge(t, s, e1);
            gs.setEdgeWeight(e1, w);
        }
        return gs;
    }

    public static <V, E> SimpleDirectedGraph<V, E> toDirectedGraph(SimpleGraph<V, E> graph) {
        SimpleDirectedGraph<V, E> gs =
                new SimpleDirectedGraph<>(
                        graph.getVertexSupplier(),
                        graph.getEdgeSupplier(),
                        true);
        for (V v : graph.vertexSet()) {
            gs.addVertex(v);
        }
        for (E e : graph.edgeSet()) {
            gs.addEdge(graph.getEdgeSource(e), graph.getEdgeTarget(e));
            gs.addEdge(graph.getEdgeTarget(e), graph.getEdgeSource(e));
        }
        return gs;
    }

    public static <V, E, G extends Graph<V, E>> G subGraphOfVertices(G graph,
                                                                     Predicate<V> pv,
                                                                     Supplier<G> creator) {
        return subGraph(graph, pv, null, creator);
    }

    public static <V, E, G extends Graph<V, E>> G subGraphOfEdges(G graph, Predicate<E> pe, Supplier<G> creator) {
        return subGraph(graph, null, pe, creator);
    }

    public static <V, E, G extends Graph<V, E>> G subGraph(G graph, Predicate<V> pv, Predicate<E> pe,
                                                           Supplier<G> creator) {

        Predicate<V> npv = pv == null ? v -> true : pv;


        Set<V> vertices = graph.vertexSet().stream().filter(npv).collect(Collectors.toSet());


        Predicate<E> npe = e -> vertices.contains(graph.getEdgeSource(e)) && vertices.contains(graph.getEdgeTarget(e));


        Predicate<E> npe2 = pe == null ? npe : e -> npe.test(e) && pe.test(e);

        Set<E> edges = graph.edgeSet().stream().filter(npe2).collect(Collectors.toSet());

        G r = creator.get();

        vertices.forEach(r::addVertex);
        edges.forEach(x -> r.addEdge(graph.getEdgeSource(x), graph.getEdgeTarget(x), x));


        return r;
    }

    public static <V, E, G extends Graph<V, E>> G explicitCompleteGraph(
            G graph,
            Supplier<G> creator,
            Supplier<E> edgeCreator,
            Function<E, Double> edgeWeight) {


        G r = creator.get();

        graph.vertexSet().forEach(r::addVertex);
        graph.edgeSet().forEach(x -> r.addEdge(graph.getEdgeSource(x), graph.getEdgeTarget(x), x));

        for (V v1 : graph.vertexSet()) {
            for (V v2 : graph.vertexSet()) {


                if (!v1.equals(v2)) {
                    if (!graph.containsEdge(v1, v2)) {
                        E e = edgeCreator.get();
                        r.addEdge(v1, v2, e);
                    }
                }
            }
        }
        r.edgeSet().forEach(e -> r.setEdgeWeight(e, edgeWeight.apply(e)));
        return r;
    }

    public static <V, E extends SimpleEdge<V>> V getOppositeVertex(E edge, V vertex) {
        V r = null;
        if (edge.source().equals(vertex)) r = edge.target();
        if (edge.target().equals(vertex)) r = edge.source();
        Preconditions.checkNotNull(r);
        return r;
    }

    public static <V, E, G extends Graph<V, E>> G sustituteEdge(G graph, E edge, GraphPath<V, E> graphPath) {
        Graph<V, E> origin = graphPath.getGraph();
        graph.removeEdge(edge);
        graphPath.getVertexList().forEach(v -> {
            if (!graph.containsVertex(v)) graph.addVertex(v);
        });
        graphPath.getEdgeList()
                .forEach(e -> graph.addEdge(origin.getEdgeSource(e), origin.getEdgeTarget(e), e));
        return graph;
    }

    /**
     * @param graph       Un grafo no dirigido
     * @param edgeReverse Una funci�n que produce una arista inversa con el mismo peso
     * @param sources     Los v�rtices que ser�n fuentes
     * @param targets     Los v�rtices que ser�n sumideros
     * @return Un grafo dirigido donde los v�rtices fuente no tienen aristas de entrada y
     * los sumideros no tienen aristas de salida
     */
    public static <V, E> SimpleDirectedWeightedGraph<V, E> toDirectedWeightedGraphFlow(
            SimpleWeightedGraph<V, E> graph,
            Function<E, E> edgeReverse,
            Set<V> sources,
            Set<V> targets) {
        SimpleDirectedWeightedGraph<V, E> gs = Graphs2.toDirectedWeightedGraph(graph, edgeReverse);
        Set<E> remove = new HashSet<>();
        for (E e : gs.edgeSet()) {
            V s = gs.getEdgeSource(e);
            V t = gs.getEdgeTarget(e);
            if (sources.contains(t) || targets.contains(s)) remove.add(e);
        }
        gs.removeAllEdges(remove);
        return gs;
    }


}