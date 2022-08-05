module partecomun {
    // Dependecias
    requires commons.math3;
    requires org.jgrapht.core;
    requires org.antlr.antlr4.runtime;
    requires org.jgrapht.io;
    requires org.jheaps;
    requires gurobi;

    // Tipos báscicos
    exports com.fadda.basictypes;
    // Clases más útiles
    exports com.fadda.common;
    exports com.fadda.common.collections;
    exports com.fadda.common.extension;
    exports com.fadda.common.tri;
    exports com.fadda.common.tuples.pair;
    exports com.fadda.common.tuples.quartet;
    exports com.fadda.common.tuples.triplet;
    exports com.fadda.common.views;
    // Genéticos
    exports com.fadda.iterables.seq;
    exports com.fadda.genetics;
    exports com.fadda.genetics.agchromosomes;
    exports com.fadda.genetics.agoperators;
    exports com.fadda.genetics.agstopping;
    // Grafo
    exports com.fadda.graph.colors;
    exports com.fadda.graph.flowgraph;
    exports com.fadda.graph.graphs;
    exports com.fadda.graph.graphs.alg;
    exports com.fadda.graph.graphs.tour;
    exports com.fadda.graph.graphs.views;
    exports com.fadda.graph.graphs.virtual;
    exports com.fadda.graph.hypergraphs;
    exports com.fadda.graph.hypergraphs2;
    exports com.fadda.graph.path;
    // Gurobi
    exports com.fadda.gurobi;
    // Iterables
    exports com.fadda.iterables;
    exports com.fadda.iterables.iterator;
    // Mates
    exports com.fadda.math;
    // Funcional
    exports com.fadda.streams;
}
