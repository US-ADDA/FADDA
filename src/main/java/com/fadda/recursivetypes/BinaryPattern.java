package com.fadda.recursivetypes;


import com.fadda.recursivetypes.BinaryPatternImpl.Matches;

import java.util.function.Function;

public interface BinaryPattern<E> {

    static <E> BinaryPattern<E> binary(E label, BinaryPattern<E> left, BinaryPattern<E> right) {
        return BinaryPatternImpl.binary(label, left, right);
    }

    static <E> BinaryPattern<E> binary_variable(String variable_name, BinaryPattern<E> left, BinaryPattern<E> right) {
        return BinaryPatternImpl.binary_variable(variable_name, left, right);
    }

    static <E> BinaryPattern<E> leaf(E label) {
        return BinaryPatternImpl.leaf(label);
    }

    static <E> BinaryPattern<E> empty() {
        return BinaryPatternImpl.empty();
    }

    static <E> BinaryPattern<E> variable(String name) {
        return BinaryPatternImpl.variable(name);
    }

    static <E> BinaryPattern<E> parse(String s, Function<String, E> f) {
        return BinaryPatternImpl.parse(s, f);
    }

    static <E> BinaryPattern<E> parse(String s) {
        return BinaryPattern.parse(s, x -> null);
    }

    static <E> Matches<E> match(BinaryTree<E> tree, BinaryPattern<E> pt) {
        return BinaryPatternImpl.match(tree, pt);
    }

    static <E> BinaryTree<E> transform(BinaryTree<E> tree, BinaryPattern<E> pattern, BinaryPattern<E> result) {
        return BinaryPatternImpl.transform(tree, pattern, result);
    }


    boolean isEmpty();

    boolean isLeaf();


    boolean isBinary();

    boolean isBinary_Variable();

    boolean isVariable();

    E getLabel();

    BinaryPattern<E> getLeft();

    BinaryPattern<E> getRight();

    PatternType getType();

    String getVariable_Name();

    String toString();

    <R> BinaryPattern<R> map(Function<E, R> f);

    BinaryTree<E> toBinaryTree(Matches<E> matches);

    enum PatternType {Empty, Leaf, Binary, Binary_Variable, Variable}

}
