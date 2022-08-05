package com.fadda.graph.graphs.alg;

import com.fadda.common.extension.List2;
import com.fadda.graph.graphs.virtual.EGraph;
import com.fadda.graph.path.EGraphPath;
import com.fadda.streams.Stream2;
import org.jgrapht.GraphPath;

import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

public class GreedyOnGraph<V, E> implements Iterator<GreedyOnGraph.Gog<V, E>>, Iterable<GreedyOnGraph.Gog<V, E>> {

    private final EGraph<V, E> graph;
    private final Function<V, E> nextEdge;
    private V state;

    private GreedyOnGraph(EGraph<V, E> graph, Function<V, E> nextEdge) {
        super();
        this.graph = graph;
        this.state = graph.startVertex();
        this.nextEdge = nextEdge;
    }

    public static <V, E> GreedyOnGraph<V, E> of(EGraph<V, E> graph, Function<V, E> nextEdge) {
        return new GreedyOnGraph<>(graph, nextEdge);
    }


    public static <V, E> GreedyOnGraph<V, E> random(EGraph<V, E> graph) {
        Function<V, E> nextEdge = v -> graph.edgesListOf(v).isEmpty() ? null :
                List2.randomUnitary(graph.edgesListOf(v)).get(0);
        return new GreedyOnGraph<>(graph, nextEdge);
    }


    public Stream<V> stream() {
        return streamPair().map(Gog::vertex);
    }

    public Stream<E> streamEdges() {
        return streamPair().map(Gog::edge).filter(Objects::nonNull);
    }

    public Stream<Gog<V, E>> streamPair() {
        return Stream2.of(this);
    }

    public GraphPath<V, E> path() {

        EGraphPath<V, E> path = this.graph.initialPath();
        this.streamEdges().forEach(path::add);
        return path;
    }

    public Optional<GraphPath<V, E>> solutionPath() {
        GraphPath<V, E> r = path();
        if (!r.getVertexList().isEmpty()) {
            V last = r.getEndVertex();
            if (this.graph.constraint().test(last)) return Optional.of(r);

            else return Optional.empty();
        } else return Optional.empty();
    }


    public Boolean isSolution(GraphPath<V, E> gp) {
        V last = gp.getEndVertex();

        return graph.goal().test(last) && graph.constraint().test(last);
    }


    public Optional<V> last() {
        return Stream2.findLast(this.stream());
    }


    public GreedyOnGraph<V, E> copy() {
        return of(this.graph, this.nextEdge);
    }


    @Override
    public Iterator<Gog<V, E>> iterator() {
        return this;
    }

    @Override
    public boolean hasNext() {
        return state != null && !this.graph.edgesOf(state).isEmpty() && !this.graph.goal().test(state);
    }

    @Override
    public Gog<V, E> next() {
        V old = state;
        E edge = this.nextEdge.apply(state);
        if (edge != null) this.state = graph.oppositeVertex(edge, old);
        else this.state = null;
        return Gog.of(old, edge);
    }

    public record Gog<V, E>(V vertex, E edge) {
        public static <V, E> Gog<V, E> of(V vertex, E edge) {
            return new Gog<>(vertex, edge);
        }
    }

}