package com.fadda.genetics;

import com.fadda.genetics.agchromosomes.ChromosomeFactory;

public interface BinaryData<S> extends ValuesInRangeData<Integer, S> {

    default Integer max(Integer i) {
        return 2;
    }

    default Integer min(Integer i) {
        return 0;
    }

    default ChromosomeFactory.ChromosomeType type() {
        return ChromosomeFactory.ChromosomeType.Binary;
    }

}
