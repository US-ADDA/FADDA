package com.fadda.genetics.agchromosomes;

import com.fadda.genetics.BinaryData;
import com.fadda.genetics.Chromosome;
import com.fadda.genetics.ChromosomeData;
import org.apache.commons.math3.genetics.AbstractListChromosome;
import org.apache.commons.math3.genetics.InvalidRepresentationException;

import java.util.List;

/**
 * @author Miguel Toro
 *
 * <p>
 * Un cromosoma cuya valor decodificado es una lista de ceros y unos del tama�o especificado en el problema.
 * La implementaci�n es una adaptaci�n de la clase {@link org.apache.commons.math3.genetics.Chromosome Chromosome} de Apache. </p>
 */
public class BinaryChromosome<S> extends org.apache.commons.math3.genetics.BinaryChromosome implements
        BinaryData<S>, Chromosome<List<Integer>> {

    /**
     * Dimensi�n del cromosoma
     */

    protected static Integer DIMENSION;
    protected static ChromosomeData<List<Integer>, Object> data;
    private final Double ft;

    public BinaryChromosome(List<Integer> representation) throws InvalidRepresentationException {
        super(representation);
        this.ft = this.calculateFt();
    }

    public BinaryChromosome(Integer[] representation) throws InvalidRepresentationException {
        super(representation);
        this.ft = this.calculateFt();
    }

    @SuppressWarnings("unchecked")
    public static <S> ChromosomeData<List<Integer>, S> data() {
        return (ChromosomeData<List<Integer>, S>) data;
    }

    public static void initialValues(ChromosomeData<List<Integer>, Object> data) {
        BinaryChromosome.data = data;
        BinaryChromosome.DIMENSION = data.size();
    }

    public static <S> BinaryChromosome<S> getInitialChromosome() {
        List<Integer> ls = BinaryChromosome.randomBinaryRepresentation(BinaryChromosome.DIMENSION);
        return new BinaryChromosome<>(ls);
    }

    @Override
    public List<Integer> decode() {
        return getRepresentation();
    }

    @Override
    public double fitness() {
        return ft;
    }

    private double calculateFt() {
        return this.fitnessFunction(this.decode());
    }

    @Override
    public AbstractListChromosome<Integer> newFixedLengthChromosome(List<Integer> ar) {
        return new BinaryChromosome<>(ar);
    }

    @Override
    public ChromosomeFactory.ChromosomeType type() {
        return ChromosomeFactory.ChromosomeType.Binary;
    }

    @Override
    public Integer size() {
        return BinaryChromosome.data.size();
    }

    @Override
    public Double fitnessFunction(List<Integer> dc) {
        return BinaryChromosome.data.fitnessFunction(dc);
    }

    @Override
    public S solution(List<Integer> dc) {
        return BinaryChromosome.<S>data().solution(dc);
    }

}
