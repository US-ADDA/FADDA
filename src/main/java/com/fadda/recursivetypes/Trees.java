package com.fadda.recursivetypes;

import com.fadda.common.extension.String2;
import com.fadda.streams.Stream2;

public class Trees {

	public static <E> Integer size(Tree<E> tree) {
		return (int) tree.stream().filter(t->!t.isEmpty()).mapToInt(t->1).count();
	}
}
