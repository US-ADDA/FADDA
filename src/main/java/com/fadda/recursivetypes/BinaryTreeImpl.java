package com.fadda.recursivetypes;

import com.fadda.common.Preconditions;
import com.fadda.common.Printers;
import com.fadda.common.extension.List2;
import com.fadda.common.views.View2E;
import com.fadda.recursivetypes.BinaryPatternImpl.Matches;
import com.fadda.recursivetypes.parsers.BinaryTreeLexer;
import com.fadda.recursivetypes.parsers.BinaryTreeParser;
import com.fadda.streams.Stream2;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.PrintStream;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BinaryTreeImpl<E> implements MutableBinaryTree<E> {

    private static final BinaryTreeImpl<Object> empty = new BinaryTreeImpl<Object>();

    private final BinaryType type;
    protected E label;
    protected BinaryTreeImpl<E> left;
    protected BinaryTreeImpl<E> right;
    protected BinaryTreeImpl<E> father;

    protected BinaryTreeImpl() {
        super();
        this.label = null;
        this.left = null;
        this.right = null;
        this.type = BinaryType.Empty;
        this.father = null;
    }

    protected BinaryTreeImpl(E label) {
        super();
        this.label = label;
        this.left = null;
        this.right = null;
        this.type = BinaryType.Leaf;
        this.father = null;
    }

    protected BinaryTreeImpl(E label, BinaryTreeImpl<E> left, BinaryTreeImpl<E> right) {
        super();
        this.label = label;
        this.left = left;
        this.right = right;
        this.type = BinaryType.Binary;
        this.father = null;
        this.left.father = this;
        this.right.father = this;
    }

    public static <E> BinaryTreeImpl<E> empty() {
        return new BinaryTreeImpl<>();
    }

    public static <E> BinaryTreeImpl<E> leaf(E label) {
        return new BinaryTreeImpl<>(label);
    }

    public static <E> BinaryTreeImpl<E> binary(E label, BinaryTree<E> left, BinaryTree<E> right) {
        BinaryTreeImpl<E> r;
        Preconditions.checkNotNull(left);
        Preconditions.checkNotNull(right);
        if (left.isEmpty() && right.isEmpty()) {
            r = new BinaryTreeImpl<>(label);
        } else {
            r = new BinaryTreeImpl<>(label, (BinaryTreeImpl<E>) left, (BinaryTreeImpl<E>) right);
        }
        return r;
    }

    @SuppressWarnings("unchecked")
    public static BinaryTree<String> parse(String s) {
        BinaryTreeLexer lexer = new BinaryTreeLexer(CharStreams.fromString(s));
        BinaryTreeParser parser = new BinaryTreeParser(new CommonTokenStream(lexer));
        ParseTree parseTree = parser.binary_tree();
        return (BinaryTree<String>) parseTree.accept(new BinaryTreeVisitorC());
    }


    public static <E> BinaryTree<E> parse(String s, Function<String, E> f) {
        Preconditions.checkNotNull(s);
        BinaryTree<String> tree = BinaryTreeImpl.parse(s);
        return tree.map(f);
    }


    private static <E> List<BinaryTree<E>> nextLevel(List<BinaryTree<E>> ls) {
        List<BinaryTree<E>> r = List2.empty();
        for (BinaryTree<E> tree : ls) {
            switch (tree.getType()) {
                case Empty:
                    break;
                case Leaf:
                    r.add(BinaryTree.empty());
                    r.add(BinaryTree.empty());
                    break;
                case Binary:
                    r.add(tree.getLeft());
                    r.add(tree.getRight());
                    break;
            }
        }
        return r;
    }

    private static <E> List<BinaryTree<E>> nextExtendedLevel(List<BinaryTree<E>> ls) {
        List<BinaryTree<E>> r = List2.empty();
        for (BinaryTree<E> tree : ls) {
            switch (tree.getType()) {
                case Empty, Leaf -> {
                    r.add(BinaryTree.empty());
                    r.add(BinaryTree.empty());
                }
                case Binary -> {
                    r.add(tree.getLeft());
                    r.add(tree.getRight());
                }
            }
        }
        return r;
    }

    private static Integer getIndex(Object e, Map<Object, Integer> map, String label, PrintStream file) {
        Integer r;
        if (map.containsKey(e)) r = map.get(e);
        else {
            r = map.get("maxValue") + 1;
            map.put("maxValue", r);
            map.put(e, r);
            vertex(r, label, file);
        }
        return r;
    }

    private static void vertex(Integer n, String text, PrintStream file) {
        String txt = String.format("\"%s\" [label=\"%s\"];", n, text);
        file.println(txt);
    }

    private static void edge(Integer v1, Integer v2, PrintStream file) {
        edge(v1, v2, null, file);
    }

    private static void edge(Integer v1, Integer v2, String text, PrintStream file) {
        String txt;
        if (text != null)
            txt = String.format("\"%s\" -> \"%s\" [label=\"%s\"];", v1, v2, text);
        else
            txt = String.format("\"%s\" -> \"%s\";", v1, v2);
        file.println(txt);
    }

    public static <E> BinaryTree<E> equilibrate(BinaryTree<E> tree) {
        Patterns<E> pt = Patterns.of();
        BinaryTree<E> r = switch (getTypeEquilibrate(tree)) {
            case Equilibrate -> tree;
            case LeftLeft -> tree.transform(pt.leftLeft, pt.result);
            case LeftRight -> tree.transform(pt.leftRight, pt.result);
            case RightLeft -> tree.transform(pt.rightLeft, pt.result);
            case RightRight -> tree.transform(pt.rightRight, pt.result);
        };
        return r;
    }

    public static <E> TypeEquilibrate getTypeEquilibrate(BinaryTree<E> tree) {

        TypeEquilibrate r = null;
        List<Integer> n1 = tree.getHeights(1);
        List<Integer> n2 = tree.getHeights(2);
        int left = n1.get(0);
        int right = n1.get(1);
        int leftleft = n2.get(0);
        int leftright = n2.get(1);
        int rightleft = n2.get(2);
        int rightright = n2.get(3);
        if (Math.abs(left - right) < 2) {
            r = TypeEquilibrate.Equilibrate;
        } else if (left - right >= 2) {
            if (leftleft >= leftright) {
                r = TypeEquilibrate.LeftLeft;
            } else {
                r = TypeEquilibrate.LeftRight;
            }
        } else if (left - right < 2) {
            if (rightleft >= rightright) {
                r = TypeEquilibrate.RightLeft;
            } else {
                r = TypeEquilibrate.RightRight;
            }
        }
        Preconditions.checkArgument(r != null, String.format("%d,%d,%d,%d,%d,%d,%s", left, right, leftleft, leftright,
                rightleft, rightright, tree));
        return r;
    }

    @Override
    public boolean isEmpty() {
        return type.equals(BinaryType.Empty);
    }

    @Override
    public boolean isLeaf() {
        return type.equals(BinaryType.Leaf);
    }

    @Override
    public boolean isBinary() {
        return type.equals(BinaryType.Binary);
    }

    @Override
    public BinaryType getType() {
        return type;
    }

    @Override
    public BinaryTree<E> getFather() {
        return this.father;
    }

    public void setFather(BinaryTree<E> father) {
        this.father = (BinaryTreeImpl<E>) father;
    }

    @Override
    public boolean isRoot() {
        return this.father == null;
    }

    @Override
    public Boolean isLeftChild() {
        boolean r;
        if (isRoot()) {
            r = false;
        } else {
            r = getFather().getLeft() == this;
        }
        return r;
    }

    @Override
    public Boolean isRightChild() {
        boolean r;
        if (isRoot()) {
            r = false;
        } else {
            r = getFather().getRight() == this;
        }
        return r;
    }

    @Override
    public ChildType getChildType() {
        ChildType r = null;
        if (isRoot()) {
            r = ChildType.Root;
        } else if (this.getFather().getLeft() == this) {
            r = ChildType.Left;
        } else if (this.getFather().getRight() == this) {
            r = ChildType.Right;
        } else {
            Preconditions.checkState(false, "Fallo interno");
        }
        return r;
    }

    @Override
    public E getLabel() {
        Preconditions.checkState(!isEmpty(), "El �rbol es vac�o");
        return this.label;
    }

    public void setLabel(E label) {
        this.label = label;
    }

    @Override
    public BinaryTreeImpl<E> getLeft() {
        Preconditions.checkState(isBinary(), "El �rbol no es binario");
        return this.left;
    }

    public void setLeft(BinaryTree<E> left) {
        BinaryTreeImpl<E> tt = (BinaryTreeImpl<E>) left;
        this.left = tt;
        tt.father = this;
    }

    @Override
    public BinaryTreeImpl<E> getRight() {
        Preconditions.checkState(isBinary(), "El �rbol no es binario");
        return this.right;
    }

    public void setRight(BinaryTree<E> right) {
        BinaryTreeImpl<E> tt = (BinaryTreeImpl<E>) right;
        this.right = tt;
        tt.father = this;
    }

    /**
     * @param nTree Un �rbol binario
     * @return Devuelve nTree
     * @post this pasa start ser un arbol raiz si no lo era antes. nTree pasa start ocupar
     * el lugar de this. El padre de nTree es el antiguo padre de this
     */
    public BinaryTree<E> changeFor(BinaryTree<E> nTree) {
        BinaryTreeImpl<E> tt = (BinaryTreeImpl<E>) nTree;
        switch (this.getChildType()) {
            case Root:
                break;
            case Left:
                this.father.left = tt;
                break;
            case Right:
                this.father.right = tt;
                break;
        }
        return tt;
    }

    @Override
    public int size() {
        return switch (this.getType()) {
            case Empty -> 0;
            case Leaf -> 1;
            case Binary -> 1 + this.getLeft().size() + this.getRight().size();
        };
    }

    @Override
    public int getHeight() {
        return switch (this.getType()) {
            case Empty, Leaf -> 0;
            case Binary -> 1 + Math.max(this.getLeft().getHeight(), this.getRight().getHeight());
        };
    }

    @Override
    public List<BinaryTree<E>> getLevel(int n) {
        List<BinaryTree<E>> r = List2.of(this);
        for (int i = 0; i < n; i++) {
            r = nextLevel(r);
        }
        return r;
    }

    public List<BinaryTree<E>> getExtendedLevel(int n) {
        List<BinaryTree<E>> r = List2.of(this);
        for (int i = 0; i < n; i++) {
            r = nextExtendedLevel(r);
        }
        return r;
    }

    @Override
    public List<Integer> getHeights(int n) {
        return this.getExtendedLevel(n).stream().map(BinaryTree::getHeight).collect(Collectors.toList());
    }

    @Override
    public BinaryTree<E> copy() {
        return switch (this.getType()) {
            case Empty -> BinaryTree.empty();
            case Leaf -> BinaryTree.leaf(label);
            case Binary -> BinaryTree.binary(label, this.getLeft().copy(), this.getRight().copy());
        };
    }

    @Override
    public BinaryTree<E> getReverse() {
        BinaryTree<E> r = switch (this.getType()) {
            case Empty -> BinaryTree.empty();
            case Leaf -> BinaryTree.leaf(label);
            case Binary -> BinaryTree.binary(label, this.getRight().copy(), this.getLeft().copy());
        };
        return r;
    }

    @Override
    public <R> BinaryTree<R> map(Function<E, R> f) {
        BinaryTree<R> r = switch (this.getType()) {
            case Empty -> BinaryTree.empty();
            case Leaf -> BinaryTree.leaf(f.apply(this.getLabel()));
            case Binary -> BinaryTree.binary(f.apply(this.getLabel()), this.getLeft().map(f), this.getRight().map(f));
        };
        return r;
    }

    @Override
    public BinaryTree<E> transform(BinaryPattern<E> pattern, BinaryPattern<E> result) {
        return BinaryPattern.transform(this, pattern, result);
    }

    @Override
    public Matches<E> match(BinaryPattern<E> pt) {
        return BinaryPattern.match(this, pt);
    }

    @Override
    public String toString() {
        String r = switch (this.getType()) {
            case Empty -> "_";
            case Leaf -> label.toString();
            case Binary -> label.toString() + "(" + this.getLeft().toString() + "," + this.getRight().toString() + ")";
        };
        return r;
    }


    @Override
    public List<E> getPreOrder() {
        List<E> r = null;
        switch (this.getType()) {
            case Empty -> r = List2.empty();
            case Leaf -> r = List2.of(this.label);
            case Binary -> {
                r = List2.of(this.label);
                r.addAll(this.getLeft().getPreOrder());
                r.addAll(this.getRight().getPreOrder());
            }
        }
        return r;
    }


    @Override
    public List<E> getPostOrder() {
        List<E> r = null;
        switch (this.getType()) {
            case Empty -> r = List2.empty();
            case Leaf -> r = List2.of(this.label);
            case Binary -> {
                r = this.getLeft().getPostOrder();
                r.addAll(this.getRight().getPostOrder());
                r.add(this.getLabel());
            }
        }
        return r;
    }


    @Override
    public List<E> getInOrder() {
        List<E> r = null;
        switch (this.getType()) {
            case Empty -> r = List2.empty();
            case Leaf -> r = List2.of(this.label);
            case Binary -> {
                r = this.getLeft().getInOrder();
                r.add(this.getLabel());
                r.addAll(this.getRight().getInOrder());
            }
        }
        return r;
    }


    public void toDot(String file) {
        PrintStream p = Printers.file(file);
        Map<Object, Integer> map = new HashMap<>();
        map.put("maxValue", 0);
        String txt = "digraph BinaryTree { \n \n";
        p.println(txt);
        toDot(p, map);
        p.println("}");
        p.close();
    }

    private void toDot(PrintStream p, Map<Object, Integer> map) {
        switch (this.getType()) {
            case Binary -> {
                Integer n = getIndex(this, map, this.getLabel().toString(), p);
                Integer left = getIndex(this.getLeft(), map,
                        this.getLeft().isEmpty() ? "_" : this.getLeft().getLabel().toString(), p);
                Integer right = getIndex(this.getRight(), map,
                        this.getRight().isEmpty() ? "_" : this.getRight().getLabel().toString(), p);
                edge(n, left, p);
                edge(n, right, p);
                this.getLeft().toDot(p, map);
                this.getRight().toDot(p, map);
            }
            case Empty -> getIndex(this, map, "_", p);
            case Leaf -> getIndex(this, map, this.getLabel().toString(), p);
        }
    }

    public BinaryTree<E> equilibrate() {
        return equilibrate(this);
    }

    @Override
    public Stream<BinaryTree<E>> stream() {
        return Stream2.of(this);
    }

    @Override
    public View2E<BinaryTree<E>, E> view() {
        Preconditions.checkArgument(!this.isEmpty(), "El arbol no puede estar vaci�");
        return View2E.of(this.getLabel(), this.getLeft(), this.getRight());
    }

    @Override
    public Iterator<BinaryTreeLevel<E>> byLevel() {
        return BreadthPathBinaryTree.of(this);
    }

    @Override
    public Iterator<BinaryTree<E>> iterator() {
        return DepthPathBinaryTree.of(this);
    }

    public enum TypeEquilibrate {LeftRight, LeftLeft, RightLeft, RightRight, Equilibrate}

    static class Patterns<R> {
        private static Patterns<?> patterns = null;
        final BinaryPattern<R> leftRight;
        final BinaryPattern<R> rightLeft;
        final BinaryPattern<R> leftLeft;
        final BinaryPattern<R> rightRight;
        final BinaryPattern<R> result;

        public Patterns() {
            super();
            this.leftRight = BinaryPattern.parse("_e5(_e3(_A,_e4(_B,_C)),_D)");
            this.rightLeft = BinaryPattern.parse("_e3(_A,_e5(_e4(_B,_C),_D))");
            this.leftLeft = BinaryPattern.parse("_e5(_e4(_e3(_A,_B),_C),_D)");
            this.rightRight = BinaryPattern.parse("_e3(_A,_e4(_B,_e5(_C,_D)))");
            this.result = BinaryPattern.parse("_e4(_e3(_A,_B),_e5(_C,_D))");
        }

        @SuppressWarnings("unchecked")
        public static <R> Patterns<R> of() {
            if (patterns == null) patterns = new Patterns<R>();
            return (Patterns<R>) patterns;
        }

    }

    public static class DepthPathBinaryTree<E> implements Iterator<BinaryTree<E>>, Iterable<BinaryTree<E>> {

        private final Stack<BinaryTree<E>> stack;

        public DepthPathBinaryTree(BinaryTree<E> tree) {
            super();
            this.stack = new Stack<>();
            this.stack.add(tree);
        }

        public static <E> DepthPathBinaryTree<E> of(BinaryTree<E> tree) {
            return new DepthPathBinaryTree<>(tree);
        }

        @Override
        public Iterator<BinaryTree<E>> iterator() {
            return this;
        }

        @Override
        public boolean hasNext() {
            return !this.stack.isEmpty();
        }

        @Override
        public BinaryTree<E> next() {
            BinaryTree<E> actual = stack.pop();
            switch (actual.getType()) {
                case Binary:
                    stack.addAll(List.of(actual.getLeft(), actual.getRight()));
                    break;
                case Empty:
                case Leaf:
                    break;
            }
            return actual;
        }

    }

    public static class BreadthPathBinaryTree<E> implements Iterator<BinaryTreeLevel<E>>, Iterable<BinaryTreeLevel<E>> {

        private final Queue<BinaryTreeLevel<E>> queue;

        public BreadthPathBinaryTree(BinaryTree<E> tree) {
            super();
            this.queue = new LinkedList<>();
            this.queue.add(BinaryTreeLevel.of(0, tree));
        }

        public static <E> BreadthPathBinaryTree<E> of(BinaryTree<E> tree) {
            return new BreadthPathBinaryTree<>(tree);
        }

        @Override
        public Iterator<BinaryTreeLevel<E>> iterator() {
            return this;
        }

        @Override
        public boolean hasNext() {
            return !this.queue.isEmpty();
        }

        @Override
        public BinaryTreeLevel<E> next() {
            BinaryTreeLevel<E> actual = queue.remove();
            switch (actual.tree().getType()) {
                case Binary:
                    queue.addAll(List.of(actual.tree().getLeft(), actual.tree().getRight()).stream()
                            .map(t -> BinaryTreeLevel.of(actual.level() + 1, t)).toList());
                    break;
                case Empty:
                case Leaf:
                    break;
            }
            return actual;
        }

    }
}

