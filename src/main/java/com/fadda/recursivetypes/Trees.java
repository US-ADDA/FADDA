package com.fadda.recursivetypes;

public class Trees {


    public static <E> Integer size(Tree<E> tree) {
        return (int) tree.stream().filter(t -> !t.isEmpty()).mapToInt(t -> 1).count();
    }

}
