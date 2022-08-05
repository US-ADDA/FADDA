package com.fadda.genetics.agchromosomes;

import com.fadda.genetics.Chromosome;
import com.fadda.genetics.ValuesInRangeData;
import org.apache.commons.math3.genetics.AbstractListChromosome;
import org.apache.commons.math3.genetics.InvalidRepresentationException;
import org.apache.commons.math3.genetics.RandomKey;

import java.util.List;
import java.util.stream.IntStream;


/**
 * @author Miguel Toro
 * <p> <p>
 *
 * <p> Una implementaci�n del tipo ValuesInRangeCromosome&lt;Integer&gt;. Toma como informaci�n la definici�n de un problema que implementa el interfaz
 * ValuesInRangeProblemAG. </p>
 *
 * <p> Asumimos que el n�mero de varibles es n. La lista decodificada est� formada por una lista de
 * enteros de tama�o n cuyos elementos para cada i son
 * valores en en rango [getMin(i),getMax(i)]. </p>
 *
 * <p> La implementaci�n usa un cromosoma binario del tama�o n*nbits.
 * Siendo nbits el n�mero de bits usados para representar cada uno de los enteros. </p>
 *
 * <p> Es un cromosoma adecuado para codificar problemas de subconjuntos de multiconjuntos</p>
 */
public class DoubleChromosome extends RandomKey<Double>
        implements ValuesInRangeData<Double, Object>, Chromosome<List<Double>> {

    public static ValuesInRangeData<Double, Object> data;

    /**
     * Dimensi�n del cromosoma igual start bitsNumber*getVariableNumber()
     */

    public static int DIMENSION;
    private final double ft;

    public DoubleChromosome(Double[] representation) throws InvalidRepresentationException {
        super(representation);
        this.ft = this.calculateFt();
    }

    public DoubleChromosome(List<Double> representation) throws InvalidRepresentationException {
        super(representation);
        this.ft = this.calculateFt();
    }

    public static void initialValues(ValuesInRangeData<Double, Object> data) {
        DoubleChromosome.data = data;
        DoubleChromosome.DIMENSION = DoubleChromosome.data.size();
    }

    public static DoubleChromosome getInitialChromosome() {
        List<Double> ls = RandomKey.randomPermutation(DoubleChromosome.DIMENSION);
        return new DoubleChromosome(ls);
    }

    @Override
    public AbstractListChromosome<Double> newFixedLengthChromosome(List<Double> ls) {
        return new DoubleChromosome(ls);
    }

    private Double convert(Double e, Integer i) {
        return (this.min(i) + (this.max(i) - this.min(i)) * e);
    }

    public List<Double> decode() {
        List<Double> ls = super.getRepresentation();
        return IntStream.range(0, ls.size()).boxed().map(i -> this.convert(ls.get(i), i)).toList();
    }

    public List<Double> getRepresentation() {
        return super.getRepresentation();
    }

    @Override
    public double fitness() {
        return ft;
    }

    protected double calculateFt() {
        return DoubleChromosome.data.fitnessFunction(this.decode());
    }

    @Override
    public Double max(Integer i) {
        return DoubleChromosome.data.max(i);
    }

    @Override
    public Double min(Integer i) {
        return DoubleChromosome.data.min(i);
    }

    @Override
    public ChromosomeFactory.ChromosomeType type() {
        return ChromosomeFactory.ChromosomeType.Real;
    }

    @Override
    public Integer size() {
        return DoubleChromosome.data.size();
    }

    @Override
    public Double fitnessFunction(List<Double> dc) {
        return DoubleChromosome.data.fitnessFunction(dc);
    }

    @Override
    public Object solution(List<Double> dc) {
        return DoubleChromosome.data.solution(dc);
    }


}


