package com.fadda.graph.graphs.alg;

import com.fadda.common.extension.List2;
import com.fadda.graph.graphs.virtual.EGraph;
import com.fadda.math.Math2;
import com.fadda.streams.Stream2;

import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class SimulatedAnnealingSearch<V, E> implements Iterator<V>, Iterable<V> {

    public static final Integer numTries = 10;
    public static final Integer numPerTries = 200;
    public static final Integer numSameTemperature = 10;
    public static final double initialTemperature = 1000;
    public static final double alpha = 0.97;
    public static final Predicate<Double> stop = e -> false;
    private final EGraph<V, E> graph;
    private final V startVertex;
    private final Function<V, Double> fitness;
    public V bestVertex;
    public Double bestWeight;
    public Integer n;
    public Integer i;
    public Integer s;
    private V actualVertex;
    private double temperature;

    SimulatedAnnealingSearch(EGraph<V, E> graph, V startVertex, Function<V, Double> fitness) {
        super();
        this.graph = graph;
        this.startVertex = startVertex;
        this.fitness = fitness;
        this.n = 0;
        this.i = 0;
        this.s = 0;
        this.temperature = initialTemperature;

        this.actualVertex = this.startVertex;
    }

    public static <V, E> SimulatedAnnealingSearch<V, E> simulatedAnnealing(EGraph<V, E> graph, V startVertex,
                                                                           Function<V, Double> fitness) {


        return new SimulatedAnnealingSearch<>(graph, startVertex, fitness);
    }

    public EGraph<V, E> getGraph() {

        return graph;
    }

    private V nextVertex() {
        List<E> edges = this.graph.edgesListOf(this.actualVertex);
        List<E> edge = List2.randomUnitary(edges);
        return this.graph.getEdgeTarget(edge.get(0));
    }

    private void actualizaMejorValor() {
        Double w = this.fitness.apply(this.actualVertex);

        if (this.bestWeight == null || w < this.bestWeight) {
            this.bestVertex = this.actualVertex;

            this.bestWeight = w;
        }
    }

    private double nexTemperatura(Integer i) {
        return alpha * temperature;
    }

    public Stream<V> stream() {
        return Stream2.of(this);
    }

    @Override
    public Iterator<V> iterator() {
        return this;
    }


    @Override
    public boolean hasNext() {
        return this.n < numTries || SimulatedAnnealingSearch.stop.test(this.bestWeight);
    }

    @Override
    public V next() {
        this.temperature = nexTemperatura(i);
        V nv = nextVertex();
        double incr = fitness.apply(nv) - fitness.apply(this.actualVertex);
        if (Math2.acceptBoltzmann(incr, temperature)) {
            this.actualVertex = nv;
            actualizaMejorValor();
        }
        this.s++;

        if (this.s >= numSameTemperature) {
            this.s = 0;
            this.i++;
            if (this.i >= numPerTries) {

                this.i = 0;
                this.n++;
            }

        }
        return this.actualVertex;
    }

    public E getEdgeToOrigin() {
        throw new UnsupportedOperationException();

    }


    public V startVertex() {
        return this.startVertex;
    }


    public SimulatedAnnealingSearch<V, E> copy() {
        return new SimulatedAnnealingSearch<>(graph, startVertex, fitness);

    }


}
