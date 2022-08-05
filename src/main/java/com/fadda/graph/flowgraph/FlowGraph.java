package com.fadda.graph.flowgraph;

import com.fadda.common.Preconditions;
import com.fadda.graph.colors.GraphColors;
import com.fadda.graph.graphs.GraphsReader;
import com.fadda.graph.graphs.views.IntegerVertexGraphView;
import org.jgrapht.graph.SimpleDirectedGraph;

import java.io.Serial;


/**
 * @author Miguel Toro
 * <p>
 * Un grafo simple dirigido y sin peso. La informaci�n de la red est�
 * guardada en los v�rtices y las aristas que son de los tipos
 * FlowVertex y FlowEdge.
 */


public class FlowGraph extends SimpleDirectedGraph<FlowVertex, FlowEdge> {


    /**
     *
     */
    @Serial
    private static final long serialVersionUID = 1L;

    public static Boolean allInteger = false;
    private IntegerVertexGraphView<FlowVertex, FlowEdge> integerGraph = null;

    private FlowGraph(Class<? extends FlowEdge> arg0) {
        super(arg0);
    }

    public static FlowGraph create() {
        return new FlowGraph(FlowEdge.class);
    }

    public static FlowGraph newGraph(String file) {
        FlowGraph r = GraphsReader.
                newGraph(file,
                        FlowVertex::create,
                        FlowEdge::create,
                        FlowGraph::create);
        Preconditions.checkArgument(check(r), checkMessage(r));
        return r;
    }

    private static boolean check(FlowGraph fg) {
        boolean r = true;
        for (FlowVertex v : fg.vertexSet()) {
            if (v.isSource()) {
                r = fg.incomingEdgesOf(v).isEmpty();
            }
            if (!r) break;
            if (v.isSink()) {
                r = fg.outgoingEdgesOf(v).isEmpty();
            }
            if (!r) break;
            if (v.isIntermediate()) {
                r = !fg.incomingEdgesOf(v).isEmpty() && !fg.outgoingEdgesOf(v).isEmpty();
            }
        }
        return r;
    }

    private static String checkMessage(FlowGraph fg) {
        String r = "";
        for (FlowVertex v : fg.vertexSet()) {
            if (v.isSource() && !fg.incomingEdgesOf(v).isEmpty()) {
                r = String.format("El v�rtice %s es source pero tiene aristas de entrada", v);
                break;
            }
            if (v.isSink() && !fg.outgoingEdgesOf(v).isEmpty()) {
                r = String.format("El v�rtice %s es sumidero pero tiene aristas de salida", v);
                break;
            }
            if (v.isIntermediate() && (fg.incomingEdgesOf(v).isEmpty() || fg.outgoingEdgesOf(v).isEmpty())) {
                r = String.format("El v�rtice %s es intermedio pero o no tiene aristas de entrada o de salida", v);
                break;

            }
        }
        return r;
    }

    public void toDot(String file) {
        GraphColors.toDot(this, file,
                v -> v.name,
                e -> e.name,

                v -> GraphColors.color(v.getColor()),
                e -> GraphColors.style(GraphColors.Style.solid));

    }

    public void toDotIndex(String file) {
        GraphColors.toDot(this, file,
                v -> this.vertexIndex(v).toString(),

                e -> e.name,
                v -> GraphColors.color(v.getColor()),
                e -> GraphColors.style(GraphColors.Style.solid));
    }

    public IntegerVertexGraphView<FlowVertex, FlowEdge> integerGraph() {
        if (this.integerGraph == null) {
            this.integerGraph = IntegerVertexGraphView.of(this);
        }
        return this.integerGraph;
    }

    public FlowEdge edge(Integer i, Integer j) {
        FlowVertex v1 = this.integerGraph.getVertex(i);
        FlowVertex v2 = this.integerGraph.getVertex(j);
        return this.getEdge(v1, v2);
    }

    public FlowVertex vertex(Integer i) {
        if (this.integerGraph == null) {
            this.integerGraph = IntegerVertexGraphView.of(this);
        }
        return this.integerGraph.getVertex(i);
    }

    public Integer vertexIndex(FlowVertex v) {
        Preconditions.checkNotNull(v);
        if (this.integerGraph == null) {
            this.integerGraph = IntegerVertexGraphView.of(this);
        }
        return this.integerGraph.index(v);
    }

    public Double maxEdge(Integer i, Integer j) {
        if (this.integerGraph == null) {
            this.integerGraph = IntegerVertexGraphView.of(this);
        }
        Preconditions.checkArgument(this.integerGraph.containsEdge(i, j), String.format("La arista (%d,%d) no existe", i, j));
        FlowVertex v1 = this.integerGraph.getVertex(i);
        FlowVertex v2 = this.integerGraph.getVertex(j);
        return this.getEdge(v1, v2).max;
    }

    public Double minEdge(Integer i, Integer j) {
        if (this.integerGraph == null) {
            this.integerGraph = IntegerVertexGraphView.of(this);
        }
        Preconditions.checkArgument(this.integerGraph.containsEdge(i, j), String.format("La arista (%d,%d) no existe", i, j));
        FlowVertex v1 = this.integerGraph.getVertex(i);
        FlowVertex v2 = this.integerGraph.getVertex(j);
        return this.getEdge(v1, v2).min;
    }

    public Double costEdge(Integer i, Integer j) {
        if (this.integerGraph == null) {
            this.integerGraph = IntegerVertexGraphView.of(this);
        }
        Preconditions.checkArgument(this.integerGraph.containsEdge(i, j), String.format("La arista (%d,%d) no existe", i, j));
        FlowVertex v1 = this.integerGraph.getVertex(i);
        FlowVertex v2 = this.integerGraph.getVertex(j);
        return this.getEdge(v1, v2).cost;
    }

    public boolean containsEdge(Integer i, Integer j) {
        if (this.integerGraph == null) {
            this.integerGraph = IntegerVertexGraphView.of(this);
        }
        return this.integerGraph.containsEdge(i, j);
    }

    public Double maxVertex(Integer i) {
        if (this.integerGraph == null) {
            this.integerGraph = IntegerVertexGraphView.of(this);
        }
        Integer n = this.integerGraph.n;
        Preconditions.checkArgument(0 <= i && i < n, String.format("El vertice %d no existe", i));
        FlowVertex v1 = this.integerGraph.getVertex(i);
        return v1.max;
    }

    public Double minVertex(Integer i) {
        if (this.integerGraph == null) {
            this.integerGraph = IntegerVertexGraphView.of(this);
        }
        Integer n = this.integerGraph.n;
        Preconditions.checkArgument(0 <= i && i < n, String.format("El vertice %d no existe", i));
        FlowVertex v1 = this.integerGraph.getVertex(i);
        return v1.min;
    }

    public Double costVertex(Integer i) {
        if (this.integerGraph == null) {
            this.integerGraph = IntegerVertexGraphView.of(this);
        }
        Integer n = this.integerGraph.n;
        Preconditions.checkArgument(0 <= i && i < n, String.format("El vertice %d no existe", i));
        FlowVertex v1 = this.integerGraph.getVertex(i);
        return v1.cost;
    }

    public boolean containsVertex(Integer i) {
        if (this.integerGraph == null) {
            this.integerGraph = IntegerVertexGraphView.of(this);
        }
        Integer n = this.integerGraph.n;
        return 0 <= i && i < n;
    }

    public Integer getN() {
        if (this.integerGraph == null) {
            this.integerGraph = IntegerVertexGraphView.of(this);
        }
        return this.integerGraph.n;
    }

    public FlowEdge edge(String text) {
        String[] partes = text.split("_");
        return this.edge(Integer.parseInt(partes[1]), Integer.parseInt(partes[2]));
    }

    public FlowVertex vertex(String text) {
        String[] partes = text.split("_");
        return this.vertex(Integer.parseInt(partes[1]));
    }

    /**
     * Un v�rtice de una red de Flujo puede ser una Fuente, un Sumidero o
     * un -- Commented out by Inspection (05/08/2022 14:06): v�rtice intermedio
     */
    public enum FGType {Max, Min}


}
