package com.fadda.recursivetypes;

import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import com.fadda.common.views.View2E;
import com.fadda.recursivetypes.BinaryPatternImpl.Matches;


public interface BinaryTree<E> extends Iterable<BinaryTree<E>> {

	public enum BinaryType{Empty,Leaf,Binary}
	public enum ChildType{Left,Right,Root}
	public enum PathType{Depth,ByLevel}

	/**
	 * @post isEmpty()
	 * @return Construye un �rbol vac�o
	 */
	public static <E> BinaryTree<E> empty() {
		return BinaryTreeImpl.empty();
	}

	/**
	 * @param label Una etiqueta
	 * @post isLeaf()
	 * @return Construye un �rbol hoja
	 */
	public static <E> BinaryTree<E> leaf(E label) {
		return BinaryTreeImpl.leaf(label);
	}

	/**
	 * @param label Una etiqueta
	 * @param left Un arbol
	 * @param right Un arbol
	 * @post isBinary()
	 * @return Construye un �rbol binario
	 */
	public static <E> BinaryTree<E> binary(E label, BinaryTree<E> left, BinaryTree<E> right) {
		return BinaryTreeImpl.binary(label, left, right);
	}

	/**
	 * @param s Una cadena
	 * @return Construye un �rbol a partir de su representaci�n textual en s
	 */
	public static BinaryTree<String> parse(String s){
		return BinaryTreeImpl.parse(s);
	}

	/**
	 * @param s Una cadena que representa el �rbol
	 * @param f Una funci�n de String al tipo E
	 * @return Construye un �rbol a partir de la cadena s y aplicando posteriormente la funci�n f a las etiquetas
	 */
	public static <E> BinaryTree<E> parse(String s, Function<String,E> f) {
		BinaryTree<String> tree = BinaryTreeImpl.parse(s);
		return tree.map(f);
	}

	/**
	 * @return Si es �rbol es vac�o
	 */
	boolean isEmpty();

	/**
	 * @return Si el �rbol es hoja
	 */
	boolean isLeaf();

	/**
	 * @return Si el �rbol es binario
	 */
	boolean isBinary();

	/**
	 * @return El tipo del �rbol
	 */
	BinaryType getType();

	/**
	 * @return El �bol del cual this es hijo
	 */
	BinaryTree<E> getFather();

	/**
	 * @return Si no tiene padre
	 */
	boolean isRoot();

	/**
	 * @return Si es un hijo izquierdo
	 */
	Boolean isLeftChild();

	/**
	 * @return Si es un hijo derecho
	 */
	Boolean isRightChild();

	/**
	 * @return Si es hijo izquierdo, derecho o raiz
	 */
	ChildType getChildType();

	/**
	 * @pre isLeaf() || isBinary()
	 * @return La etiqueta
	 */
	E getLabel();

	/**
	 * @pre isBinary()
	 * @return El hijo izquierdo
	 */
	BinaryTree<E> getLeft();

	/**
	 * @pre isBinary()
	 * @return El hijo derecho
	 */
	BinaryTree<E> getRight();

	/**
	 * @return El n�mero de etiquetas del �rbol
	 */
	int size();

	/**
	 * @return La altura del �rbol: longitud del camino m�s largo a un �rbol vac�o o una de las hojas
	 */
	int getHeight();

	/**
	 * @param n Un entero mayor o igual a cero
	 * @return Los �rboles que est�n a nivel n
	 */
	List<BinaryTree<E>> getLevel(int n);

	/**
	 * @param n Un entero
	 * @return Las alturas de los �rboles de nivel n
	 */
	List<Integer> getHeights(int n);

	/**
	 * @return Una copia del �rbol
	 */
	BinaryTree<E> copy();

	/**
	 * @return Una copia sim�trica del �rbol
	 */
	BinaryTree<E> getReverse();

	/**
	 * @param f Una funci�n
	 * @return El �rbol resultante tras aplicar la funci�n de a cada una de las etiquetas
	 */
	<R> BinaryTree<R> map(Function<E, R> f);

	/**
	 * @param pattern Un patr�n con el que se debe hacer matching
	 * @param result Un patr�n que ser� el esquema al que queremos transformar el �rbol
	 * @return Un �rbol que se obtiene sustituyendo en result las variables obtenidas en el matching de this con pattern.
	 * Si no hay matching se devuelve el �rbol sin transformar
	 */
	BinaryTree<E> transform(BinaryPattern<E> pattern, BinaryPattern<E> result);

	/**
	 * @param pt Un patr�n
	 * @return Un matching que contiene si ha habido o no matching y los valores para las etiquetas
	 * y variables obtenidos en el matching
	 */
	Matches<E> match(BinaryPattern<E> pt);

	/**
	 * @return la representaci�n de �rbol en cadenas de caracteres
	 */
	String toString();

	/**
	 * @return Una lista con las etiquetas el �rbol en preorden
	 */
	List<E> getPreOrder();
	/**
	 * @return Una lista con las etiquetas el �rbol en postorden
	 */
	List<E> getPostOrder();
	/**
	 * @return Una lista con las etiquetad el �rbol en ineorden
	 */
	List<E> getInOrder();

	/**
	 * @return Un �rbol equilibrado con las mismas etiquetas
	 */
	BinaryTree<E> equilibrate();

	/**
	 * @return Un flujo de datos en preorden
	 */

	Stream<BinaryTree<E>> stream();

	/**
	 * @return Una vista de tipo 2E del arbol binario
	 */
	View2E<BinaryTree<E>,E> view();

	/**
	 * @return Un iterador por niveles
	 */

	Iterator<BinaryTreeLevel<E>> byLevel();
	/**
	 *  Genera un fichero en formato gv
	 */

	void toDot(String file);

	public static record BinaryTreeLevel<E>(Integer level, BinaryTree<E> tree){
		public static <R> BinaryTreeLevel<R> of(Integer level, BinaryTree<R> tree){
			return new BinaryTreeLevel<R>(level,tree);
		}
		@Override
		public String toString() {
			return String.format("(%d,%s)",this.level,this.tree);
		}
	}

	List<E> getPath();

}
