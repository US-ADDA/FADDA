package com.fadda.genetics;

import java.util.List;

public interface ValuesInRangeData<E, S> extends ChromosomeData<List<E>, S> {

    /**
     * @param i Un entero
     * @return El m�ximo valor, sin incluir, del rango de valores de la variable i
     * @pre 0 &le; i &lt; getVariableNumber()
     */
    E max(Integer i);

    /**
     * @param i Un entero getVariableNumber()
     * @return El m�nimo valor del rango de valores de la variable i
     * @pre 0 &le; i &lt;
     */
    E min(Integer i);

}
