package com.fadda.genetics;

/**
 * @param <E> El tipo de los valores del cromosoma
 *
 *            <p> El tipo de un cromosoma </p>
 * @author Miguel Toro
 */
public interface Chromosome<E> {
    /**
     * @return Un valor de tipo E asociado al cromosoma
     */
    E decode();


    /**
     * @return El valor de fitness del cromosoma
     */
    double fitness();


}
