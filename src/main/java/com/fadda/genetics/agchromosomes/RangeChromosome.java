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
 * <p>
 *
 * <p> Una implementaci�n del tipo ValuesInRangeCromosome&lt;Integer&gt;. Toma como informaci�n la definici�n de un problema que implementa el interfaz
 * ValuesInRangeProblemAG. </p>
 *
 * <p> Asumimos que el n�mero de variables es n. La lista decodificada est� formada por una lista de
 * enteros de tama�o n cuyos elementos para cada i son
 * valores en en rango [getMin(i),getMax(i)]. </p>
 *
 * <p> La implementaci�n usa un cromosoma RandomKey del tama�o n.  </p>
 *
 * <p> Es un cromosoma adecuado para codificar problemas de subconjuntos de multiconjuntos</p>
 */
public class RangeChromosome extends RandomKey<Integer>
        implements ValuesInRangeData<Integer, Object>, Chromosome<List<Integer>> {

    public static ValuesInRangeData<Integer, Object> data;

    /**
     * Dimensi�n del cromosoma igual start size()
     */

    public static int DIMENSION;
    private final double ft;

    public RangeChromosome(Double[] representation) throws InvalidRepresentationException {
        super(representation);
        this.ft = this.calculateFitness();
    }

    public RangeChromosome(List<Double> representation) throws InvalidRepresentationException {
        super(representation);
        this.ft = this.calculateFitness();
    }

    public static void initialValues(ValuesInRangeData<Integer, Object> data) {
        RangeChromosome.data = data;
        RangeChromosome.DIMENSION = RangeChromosome.data.size();
    }

    public static RangeChromosome getInitialChromosome() {
        List<Double> ls = RandomKey.randomPermutation(RangeChromosome.DIMENSION);
        return new RangeChromosome(ls);
    }

    @Override
    public AbstractListChromosome<Double> newFixedLengthChromosome(List<Double> ls) {
        return new RangeChromosome(ls);
    }

    private Integer convert(Double e, Integer i) {
        return (int) (this.min(i) + (this.max(i) - this.min(i)) * e);
    }

    public List<Integer> decode() {
        List<Double> ls = super.getRepresentation();
        return IntStream.range(0, ls.size()).boxed().map(i -> this.convert(ls.get(i), i)).toList();
    }

    @Override
    public double fitness() {
        return ft;
    }

    protected double calculateFitness() {
        return RangeChromosome.data.fitnessFunction(this.decode());
    }

    @Override
    public Integer max(Integer i) {
        return RangeChromosome.data.max(i);
    }

    @Override
    public Integer min(Integer i) {
        return RangeChromosome.data.min(i);
    }

    @Override
    public ChromosomeFactory.ChromosomeType type() {
        return ChromosomeFactory.ChromosomeType.Range;
    }

    @Override
    public Integer size() {
        return RangeChromosome.data.size();
    }

    @Override
    public Double fitnessFunction(List<Integer> dc) {
        return RangeChromosome.data.fitnessFunction(dc);
    }

    @Override
    public Object solution(List<Integer> dc) {
        return RangeChromosome.data.solution(dc);
    }


}

