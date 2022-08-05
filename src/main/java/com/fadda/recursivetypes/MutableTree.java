package com.fadda.recursivetypes;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public interface MutableTree<E> extends Tree<E> {


    static <R> MutableTree<R> empty() {

        return TreeImpl.empty();
    }


    static <R> MutableTree<R> leaf(R label) {
        return TreeImpl.leaf(label);

    }

    static <R> MutableTree<R> nary(R label, List<MutableTree<R>> elements) {

        List<TreeImpl<R>> ls = elements.stream().map(x -> (TreeImpl<R>) x).collect(Collectors.toList());


        return TreeImpl.nary(label, ls);
    }

    @SafeVarargs
    static <R> MutableTree<R> nary(R label, MutableTree<R>... elements) {
        List<TreeImpl<R>> ls = Arrays.stream(elements).map(x -> (TreeImpl<R>) x).collect(Collectors.toList());
        return TreeImpl.nary(label, ls);
    }


    void setLabel(E label);

    void setChild(int i, Tree<E> tree);

    void addChild(Tree<E> tree);


    void addChild(int i, Tree<E> tree);

    void removeChild(int i);

    void setFather(Tree<E> father);

    /**
     * @param nTree Un �rbol
     * @return Si this no es raiz devuelve el arbol padre con el hijo this cambiado por nTree. Si this es raiz devuelve nTree
     * @post this pasa start ser un arbol raiz si no lo era antes
     */
    Tree<E> changeFor(Tree<E> nTree);

}
