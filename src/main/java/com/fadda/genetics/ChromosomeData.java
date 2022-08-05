package com.fadda.genetics;

import com.fadda.genetics.agchromosomes.ChromosomeFactory;

public interface ChromosomeData<E, S> {

    /**
     * @return Numero de casillas del cromosoma
     */
    Integer size();

    /**
     * @return El tipo del cromosoma
     */
    ChromosomeFactory.ChromosomeType type();

    /**
     * @return La funci�n de fitnes del cromosoma
     */
    Double fitnessFunction(E value);

    /**
     * @return La soluci�n definida por el cromosoma
     */
    S solution(E value);

}
