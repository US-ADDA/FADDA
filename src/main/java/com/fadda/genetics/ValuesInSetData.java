package com.fadda.genetics;

import com.fadda.genetics.agchromosomes.ChromosomeFactory;

import java.util.List;


/**
 * @param <S> El tipo de soluci�n del problema
 * @author Miguel Toro
 */
public interface ValuesInSetData<S> extends ChromosomeData<List<Integer>, S> {

    /**
     * @param i Un entero
     * @return El conjunto de valores de la variable i
     * @pre 0 &le; i &lt; getVariableNumber()
     */
    List<Integer> values(Integer i);

    default ChromosomeFactory.ChromosomeType type() {
        return ChromosomeFactory.ChromosomeType.InSet;
    }

}
