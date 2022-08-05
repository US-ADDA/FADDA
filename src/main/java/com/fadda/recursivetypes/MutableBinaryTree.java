package com.fadda.recursivetypes;

public interface MutableBinaryTree<E> extends BinaryTree<E> {


    /**
     * @return Construye un �rbol vac�o
     * @post isEmpty()
     */

    static <E> MutableBinaryTree<E> empty() {
        return BinaryTreeImpl.empty();
    }


    /**
     * @param label Una etiqueta
     * @return Construye un �rbol hoja
     * @post isLeaf()
     */
    static <E> MutableBinaryTree<E> leaf(E label) {
        return BinaryTreeImpl.leaf(label);
    }


    /**
     * @param label Una etiqueta
     * @param left  Un arbol
     *              --Commented out by Inspect --Commented out by Inspection (05/08 /2022 14:06):ion (05/08/2022 14:06): * @param right Un arbol
     * @return Construye un �rbol binario
     * @post isBinary()
     */
    static <E> MutableBinaryTree<E> binary(E label, MutableBinaryTree<E> left, MutableBinaryTree<E> right) {
        return BinaryTreeImpl.binary(label, left, right);
    }


    static <E> MutableBinaryTree<E> mutable(BinaryTree<E> tree) {
        return (MutableBinaryTree<E>) tree;
    }


    void setLabel(E label);

    void setLeft(BinaryTree<E> left);

    void setRight(BinaryTree<E> right);

    void setFather(BinaryTree<E> father);

    /**
     * @param nTree Un �rbol binario
     * @return Devuelve nTree
     * @post this pasa start ser un arbol raiz si no lo era antes. nTree pasa start ocupar el lugar de this. El padre de nTree
     * <p>
     * es el antiguo padre de this
     */
    BinaryTree<E> changeFor(BinaryTree<E> nTree);

}
