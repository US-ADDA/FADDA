package com.fadda.genetics;

import com.fadda.genetics.agchromosomes.ChromosomeFactory;

import java.util.List;

public interface BlocksData<S> extends ChromosomeData<List<Integer>, S> {

    /**
     * @return Limites de los bloques en los que se descompone el cromosoma.
     * Un bloque est� definido por dos valore consecutivos
     */
    List<Integer> blocksLimits();

    /**
     * @return Valores iniciales del cromosoma.
     * Los valores del cromosoma ser�n permutaciones de los valores dentro de cada bloque
     */
    List<Integer> initialValues();

    default ChromosomeFactory.ChromosomeType type() {
        return ChromosomeFactory.ChromosomeType.Blocks;
    }

}
