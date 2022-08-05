package com.fadda.genetics.agchromosomes;

import com.fadda.common.Preconditions;
import com.fadda.common.extension.List2;
import com.fadda.genetics.Chromosome;
import com.fadda.genetics.SeqNormalData;
import org.apache.commons.math3.genetics.AbstractListChromosome;
import org.apache.commons.math3.genetics.BinaryChromosome;
import org.apache.commons.math3.genetics.InvalidRepresentationException;

import java.util.List;

/**
 * @author Miguel Toro
 * <p> <p>
 * * <p> Una implementaci�n del tipo IndexChromosome. Toma como informaci�n la definici�n de un problema que implementa el interfaz IndexProblemAG.
 * A partir de esa informaci�n construye una secuencia normal.
 * Asumimos que el n�mero de objetos es <code>n </code>y el tama�o de la secuencia normal <code>r</code>.
 * La lista decodificada es un subconjunto no permutado de la secuencia normal.</p>
 *
 * <p> La lista decodificada est� formada por una lista de  tama�o <code>r</code>, cuyos valores son
 * �ndices en el rango <code> [0,n-1]</code>, y cada �ndice <code>i</code> se  repite un n�mero de veces igual al
 * dado por la multiplicidad m�xima del objeto <code> i </code>
 * definida en el problema. </p>
 *
 * <p> La implementaci�n usa un cromosoma binario de tama�o <code> r </code>.
 * Es un cromosoma adecuado para codificar problemas de multiconjuntos.
 **/

public class SubListChromosome extends BinaryChromosome
        implements SeqNormalData<Object>, Chromosome<List<Integer>> {


    public static List<Integer> normalSequence;
    public static SeqNormalData<Object> data;

    /**
     * Dimensi�n del cromosoma
     */

    public static int DIMENSION;
    private final Double ft;

    public SubListChromosome(List<Integer> representation)
            throws InvalidRepresentationException {
        super(representation);
        this.ft = this.calculateFitness();
    }

    public SubListChromosome(Integer[] representation)
            throws InvalidRepresentationException {
        super(representation);
        this.ft = this.calculateFitness();
    }

    public static void iniValues(SeqNormalData<Object> data) {
        SubListChromosome.data = data;
        SubListChromosome.normalSequence = SubListChromosome.data.normalSequence();
        SubListChromosome.DIMENSION = SubListChromosome.normalSequence.size();
    }

    public static SubListChromosome getInitialChromosome() {
        List<Integer> ls = BinaryChromosome.randomBinaryRepresentation(SubListChromosome.DIMENSION);
        return new SubListChromosome(ls);
    }

    @Override
    public AbstractListChromosome<Integer> newFixedLengthChromosome(List<Integer> ls) {
        return new SubListChromosome(ls);
    }

    /**
     * @return Una lista de enteros obtenida filtrando la secuencia normal para incluir
     * s�lo los seleccionados por el cromosoma binario
     */
    @Override
    public List<Integer> decode() {
        List<Integer> r = List2.empty();
        List<Integer> bn = this.getRepresentation();
        Preconditions.checkArgument(normalSequence.size() == bn.size(), normalSequence.size() + "," + bn.size());
        for (int i = 0; i < normalSequence.size(); i++) {
            if (bn.get(i) == 1) {
                r.add(normalSequence.get(i));
            }
        }
        return r;
    }

    @Override
    public double fitness() {
        return ft;
    }

    private double calculateFitness() {
        return SubListChromosome.data.fitnessFunction(this.decode());
    }


    public Integer getObjectsNumber() {
        return SubListChromosome.data.size();
    }


    public Integer getMax(Integer i) {
        return SubListChromosome.data.maxMultiplicity(i);
    }


    @Override
    public ChromosomeFactory.ChromosomeType type() {
        return ChromosomeFactory.ChromosomeType.SubList;
    }

    @Override
    public Integer size() {
        return SubListChromosome.data.size();
    }

    @Override
    public Double fitnessFunction(List<Integer> dc) {
        return SubListChromosome.data.fitnessFunction(dc);
    }

    @Override
    public Object solution(List<Integer> dc) {
        return SubListChromosome.data.solution(dc);
    }

    @Override
    public Integer itemsNumber() {
        return SubListChromosome.data.itemsNumber();
    }

}
