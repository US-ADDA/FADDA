package com.fadda.recursivetypes;

import java.util.Comparator;
import java.util.Optional;
import java.util.function.Predicate;

public class BinaryTrees {


    public static <E> Integer size(BinaryTree<E> tree) {
        return (int) tree.stream().filter(t -> !t.isEmpty()).mapToInt(t -> 1).count();
    }


    public static <E> Integer countIfPredicate(BinaryTree<E> tree, Predicate<E> pd) {

        return (int) tree.stream().filter(t -> !t.isEmpty()).map(BinaryTree::getLabel).mapToInt(t -> 1).count();
    }


    public static <E> Optional<E> minLabel(BinaryTree<E> tree, Comparator<E> cmp) {

        return tree.stream().filter(t -> !t.isEmpty()).map(BinaryTree::getLabel).min(cmp);
    }
}