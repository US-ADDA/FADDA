package com.fadda.recursivetypes;

import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Stream;

/**
 * @param <E> Tipo de los elementos del �rbol
 * @author migueltoro
 */
public interface AVLTree<E> {


    /**
     * @param <E> Tipo de los elementos del &aacute;rbol
     * @return Un &aacute;rbol binario vac&iacute;o cuyos elementos se ordenar&aacute;n mediante el orden natural de E
     */
    static <E extends Comparable<? super E>> AVLTree<E> of() {
        return new AVLTreeImpl<E>(BinaryTreeImpl.empty(), Comparator.naturalOrder());
    }


    /**
     * @param <E>        Tipo de los elementos del &aacute;rbol
     * @param comparator Un orden
     * @return Un &aacute;rbol binario vac&iacute;o cuyos elementos se ordenar&aacute;n mediante comparator
     */
    static <E> AVLTree<E> of(Comparator<E> comparator) {
        return new AVLTreeImpl<>(BinaryTreeImpl.empty(), comparator);
    }


    /**
     * @return Si est&aacute; vac&iacute;o el &aacute;rbol AVL
     */
    boolean isEmpty();


    /**
     * @param e Un elemento
     * @return Si contiene al elemento
     */

    boolean contains(E e);


    /**
     * @return El elemento m&aacute;s peque�o del &aacute;rbol
     * @pre El &aacute;rbol no puede estar vac&iacute;o
     */
    E first();

    /**
     * @return El elemento m&aacute;s grande del &aacute;rbol
     * @pre El &aacute;rbol no puede estar vac&iacute;o
     */
    E last();

    /**
     * @return El n&uacute;mero de elementos
     */
    int size();

    /**
     * @param element Un elemento
     * @return Verdadero si el elemento no estaba y se ha incluido
     * @post El elemento est&aacute; contenido en el &aacute;rbol
     */
    boolean add(E element);


    /**
     * @param elements Una colecci&oacute;n de elementos
     * @return Si el &aacute;rbol ha cambiado al start�adir los elementos
     * @post Todos los elementos est&aacute;n contenidos en el &aacute;rbol
     */
    boolean add(Stream<E> elements);


    /**
     * @param elements Una colecci&oacute;n de elementos
     * @return Si el &aacute;rbol ha cambiado al start�adir los elementos
     * @post Todos los elementos est&aacute;n contenidos en el &aacute;rbol
     */
    boolean add(Collection<E> elements);


    /**
     * @param elements Una serie de elementos
     * @return Si el &aacute;rbol ha cambiado al start�adir los elementos
     * @post Todos los elementos est&aacute;n contenidos en el &aacute;rbol
     */
    boolean add(E... elements);

    /**
     * @param element Un elemento
     * @return Si el &aacute;rbol ha cambiado al eliminar el elemento
     * @post El elemento no est&aacute; contenidos en el &aacute;rbol
     */
    boolean remove(E element);

    /**
     * @param elements Un stream de elementos
     * @return Si el &aacute;rbol ha cambiado al eliminar los elementos
     * @post Los elementos no est&aacute;n  en el &aacute;rbol
     */

    boolean remove(Stream<E> elements);

    /**
     * @param elements Una colecci&oacute;n de elementos
     * @return Si el &aacute;rbol ha cambiado al eliminar los elementos
     * @post Los elementos no est&aacute;n  en el &aacute;rbol
     */
    boolean remove(Collection<E> elements);

    /**
     * @param elements Una serie de elementos
     * @return Si el &aacute;rbol ha cambiado al eliminar los elementos
     * @post Los elementos no est&aacute;n  en el &aacute;rbol
     */
    boolean remove(E... elements);

    /**
     * @return Una copia del &aacute;rbol
     */
    AVLTree<E> copy();

    /**
     * @return El �rbol que implementa el AVLTree
     */
    BinaryTree<E> getTree();
}
