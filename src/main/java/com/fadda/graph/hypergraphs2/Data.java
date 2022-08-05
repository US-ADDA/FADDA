package com.fadda.graph.hypergraphs2;

import com.fadda.common.collections.SetMultimap;
import com.fadda.common.tuples.pair.Union;
import com.fadda.graph.colors.GraphColors;
import com.fadda.graph.colors.GraphColors.ArrowHead;
import com.fadda.graph.colors.GraphColors.Color;
import com.fadda.graph.colors.GraphColors.Shape;
import com.fadda.graph.graphs.alg.DynamicProgramming;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Data<V extends HyperVertex2<V, E, A, ?>, E extends HyperEdge2<V, E, A, ?>, A> {

    public static final DpType type = DpType.Min;
    private static Object datos = null;
    public final SetMultimap<V, DynamicProgramming.Sp<E>> allProblems = SetMultimap.create();
    private final Map<V, DynamicProgramming.Sp<E>> memory = new HashMap<>();

    private Data() {
        super();
    }

    @SuppressWarnings("unchecked")
    public static <V extends HyperVertex2<V, E, A, ?>, E extends HyperEdge2<V, E, A, ?>, A> Data<V, E, A> get() {
        if (datos == null) datos = new Data<>();
        return (Data<V, E, A>) datos;
    }

    public static <V, E> String stv(Union<V, E> un) {
        if (Boolean.TRUE.equals(un.isA())) return un.a().toString();
        else return un.b().toString().substring(0, 1).toUpperCase();
    }


    public static <V, E> String ste(Union<V, E> un) {
        if (Boolean.TRUE.equals(un.isA())) return un.a().toString();

        else return un.b().toString().substring(0, 1).toUpperCase();
    }


    public static <V extends HyperVertex2<V, E, A, ?>, E extends HyperEdge2<V, E, A, ?>, A>
    void toDotHypergraph(SimpleDirectedGraph<Union<V, E>, DefaultEdge> g, String file, V initial) {

        Set<Union<V, E>> vt = initial.graphTree().vertices().stream()
                .<Union<V, E>>map(Union::ofA).collect(Collectors.toSet());

        Set<Union<V, E>> et = initial.graphTree().hyperEdges().stream()
                .<Union<V, E>>map(Union::ofB)
                .collect(Collectors.toSet());

        Predicate<DefaultEdge> pd = e -> et.contains(g.getEdgeSource(e)) || et.contains(g.getEdgeTarget(e));

        GraphColors.toDot(g, file,
                Data::stv,
                x -> Boolean.TRUE.equals(g.getEdgeSource(x).isA()) ? g.getEdgeTarget(x).b().toString().substring(0, 1).toUpperCase() : "",
                x -> GraphColors.all(GraphColors.shapeIf(Shape.point, x.isB()),
                        GraphColors.colorIf(Color.red, vt.contains(x))),


                e -> GraphColors.all(GraphColors.arrowHead(ArrowHead.none),
                        GraphColors.colorIf(Color.red, pd.test(e))));
    }

    public static <V extends HyperVertex2<V, E, A, ?>, E extends HyperEdge2<V, E, A, ?>, A>
    void toDotAndOr(SimpleDirectedGraph<Union<V, E>, DefaultEdge> g,
                    String file, V initial) {

        Set<Union<V, E>> vt = initial.graphTree().vertices().stream()
                .<Union<V, E>>map(Union::ofA).collect(Collectors.toSet());

        Set<Union<V, E>> et = initial.graphTree().hyperEdges().stream()
                .<Union<V, E>>map(Union::ofB)
                .collect(Collectors.toSet());

        Predicate<DefaultEdge> pd = e -> et.contains(g.getEdgeSource(e)) || et.contains(g.getEdgeTarget(e));

        GraphColors.toDot(g, file,
                Data::stv,
                x -> "",
                x -> GraphColors.all(GraphColors.shapeIf(Shape.box, x.isA()),
                        GraphColors.colorIf(Color.red, vt.contains(x) || et.contains(x))),
                e -> GraphColors.all(GraphColors.arrowHead(ArrowHead.none),

                        GraphColors.colorIf(Color.red, pd.test(e))));
    }

    public DynamicProgramming.Sp<E> get(V v) {
        return memory.get(v);
    }

    public Set<DynamicProgramming.Sp<E>> getAll(V v) {
        return allProblems.get(v);
    }

    public void put(V v, DynamicProgramming.Sp<E> s) {
        memory.put(v, s);
    }

    public void putAll(V v, DynamicProgramming.Sp<E> s) {
        allProblems.put(v, s);
    }

    public Boolean contains(V v) {
        return memory.containsKey(v);
    }

    public Set<V> vertices() {
        return memory.keySet();
    }


    public SimpleDirectedGraph<Union<V, E>, DefaultEdge> graph() {

        SimpleDirectedGraph<Union<V, E>, DefaultEdge> graph =
                new SimpleDirectedGraph<>(null, DefaultEdge::new, true);

        Set<V> vertices = this.vertices();
        for (V v : vertices) {
            graph.addVertex(Union.ofA(v));
        }
        for (V v : vertices) {
            Set<DynamicProgramming.Sp<E>> alls = this.getAll(v);
            if (alls != null) {
                for (DynamicProgramming.Sp<E> s : alls) {
                    if (s != null) {
                        E e = s.edge();
                        if (e != null) {
                            Union<V, E> source = Union.ofA(e.source());
                            List<Union<V, E>> targets = e.targets().stream().map(Union::<V, E>ofA).toList();
                            Union<V, E> ve = Union.ofB(e);
                            graph.addVertex(ve);
                            graph.addEdge(source, ve);
                            for (Union<V, E> t : targets) {
                                graph.addEdge(ve, t);
                            }
                        }
                    }
                }
            }
        }
        return graph;
    }


    public enum DpType {Max, Min}


}
