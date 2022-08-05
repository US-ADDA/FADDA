package com.fadda.recursivetypes;


import com.fadda.common.views.ViewL;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface Tree<E> extends Iterable<Tree<E>> {

    Tree<Object> empty = new TreeImpl<>();

    static <R> Tree<R> empty() {
        return TreeImpl.empty();
    }

    static <R> Tree<R> leaf(R label) {
        return TreeImpl.leaf(label);
    }

    static <R> Tree<R> nary(R label, List<Tree<R>> elements) {
        List<TreeImpl<R>> ls = elements.stream().map(x -> (TreeImpl<R>) x).collect(Collectors.toList());

        return TreeImpl.nary(label, ls);
    }

    @SafeVarargs
    static <R> Tree<R> nary(R label, Tree<R>... elements) {

        List<TreeImpl<R>> ls = Arrays.stream(elements).map(x -> (TreeImpl<R>) x).collect(Collectors.toList());


        return TreeImpl.nary(label, ls);


    }

    static <R> Tree<R> toTree(BinaryTree<R> t) {
        return TreeImpl.toTree(t);
    }

    static Tree<String> parse(String s) {
        return TreeImpl.parse(s);
    }

    static <R> Tree<R> parse(String s, Function<String, R> f) {
        return TreeImpl.parse(s, f);
    }

    /**
     * @return El tipo del �rbol
     */
    TreeType getType();

    /**
     * @return Verdadero si el �rbol es vacio.
     */
    boolean isEmpty();


    /**
     * @return Verdadero si el �rbol es hoja.
     * --Commented  out by Inspection STOP (05/08/2022 14:06)
     */
    boolean isLeaf();

    /**
     * @param i Un entero
     * @return Si this es el hijo i de su padre
     */
    boolean isChild(int i);

    /**
     * @return Verdadero si el �rbol es nario.
     */
    boolean isNary();


    E getLabel();

    List<Tree<E>> getChildren();

    Tree<E> getFather();

    boolean isRoot();

    Tree<E> getChild(int index);

    int getNumOfChildren();


    int size();


    int getHeight();

    Tree<E> copy();


    <R> Tree<R> map(Function<E, R> f);

    /**
     * @return Un �rbol que es la imagen especular de this
     */
    Tree<E> getReverse();

    void toDOT(String file, String titulo);

    /**
     * @return Una lista con el recorrido en preorden.
     */
    List<E> getPreOrder();

    /**
     * @return Una lista con el recorrido en postorden
     */
    List<E> getPostOrder();

    /**
     * @param k Posici�n de inserci�n de la etiqueta
     * @return Una lista con el recorrido en inorden.
     * @post La etiqueta se insertar� en al posici�n min(k,nh). Si k = 0 resulta el recorrido en preorden y si
     * <p>
     * k &ge; nh en postorden.
     */
    List<E> getInOrder(int k);

    /**
     * @return Una lista con los �rboles por niveles. Versi�n iterativa
     */
    List<Tree<E>> getByLevel();

    /**
     * @return Una lista con las etiquetas por niveles. Versi�n iterativa
     */
    List<E> getLabelByLevel();

    /**
     * @param level Los arboles de un nivel dado
     * @return Los arboles del siguiente nivel
     */
    List<Tree<E>> getNextLevel(List<Tree<E>> level);

    /**
     * @param n Un entero
     * @return Los arboles del nivel n
     */
    List<Tree<E>> getLevel(Integer n);

    /**
     * @param root La raiz del �rbol d�nde t es un subarbol
     * @return La profundidad de t en root o -1 si no est�
     */
    int getDepth(Tree<E> root);

    String toString();

    int hashCode();

    boolean equals(Object obj);

    Stream<Tree<E>> stream();

    /**
     * @return Una vista de tipo L del �rbol nario
     */
    ViewL<Tree<E>, E> viewL();

    Iterator<TreeLevel<E>> byLevel();

    void toDot(String file);

    enum TreeType {Empty, Leaf, Nary}

    record TreeLevel<E>(Integer level, Tree<E> tree) {
        public static <R> TreeLevel<R> of(Integer level, Tree<R> tree) {
            return new TreeLevel<>(level, tree);
        }

        @Override
        public String toString() {
            return String.format("(%d,%s)", this.level, this.tree);
        }
    }
}
