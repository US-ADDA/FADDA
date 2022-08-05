package com.fadda.genetics.agchromosomes;

import com.fadda.common.Preconditions;
import com.fadda.common.extension.List2;
import com.fadda.genetics.Chromosome;
import com.fadda.genetics.SeqNormalData;
import org.apache.commons.math3.genetics.AbstractListChromosome;
import org.apache.commons.math3.genetics.BinaryChromosome;
import org.apache.commons.math3.genetics.InvalidRepresentationException;
import org.apache.commons.math3.genetics.RandomKey;

import java.util.List;


/**
 * @author Miguel Toro
 * <p>
 *
 * <p> Una implementaci�n del tipo IndexChromosome. Toma como informaci�n la definici�n de un problema que implementa el interfaz IndexProblemAG.
 * A partir de esa informaci�n construye una secuencia normal.
 * Asumimos que el n�mero de objetos es <code>n </code>y el tama�o de la secuencia normal <code>r</code>.
 * La lista decodificada es una permutaci�n de un subconjunto de la secuencia normal.</p>
 *
 * <p> La lista decodificada est� formada, por tanto,
 * por una lista de  tama�o menor o igual start <code>r</code>, cuyos valores son
 * �ndices en el rango <code> [0,n-1]</code>, y cada �ndice <code>i</code> se  repite un n�mero de veces igual o menor
 * al dado por la multiplicidad m�xima del objeto <code> i </code>
 * definida en el problema. </p>
 *
 * <p> La implementaci�n usa un cromosoma de clave aleatoria de tama�o <code> r </code> y otro binario del
 * mismo tama�o.
 * Es un cromosoma adecuado para codificar problemas de subcojuntos de permutaciones.</p>
 */
public class PermutationSubListChromosome extends org.apache.commons.math3.genetics.Chromosome
        implements SeqNormalData<Object>, Chromosome<List<Integer>> {

    public static List<Integer> normalSequence;
    public static SeqNormalData<Object> data;

    /**
     * Dimensi�n del cromosoma
     */

    protected static int DIMENSION;
    private final Double ft;
    private BinaryChromosome2 binary;
    private RandomKey2 randomKey;


    /**
     * @param binary    Un cromosoma binario
     * @param randomKey Un cromosoma randomKey
     */
    public PermutationSubListChromosome(org.apache.commons.math3.genetics.Chromosome binary,
                                        org.apache.commons.math3.genetics.Chromosome randomKey) {
        super();
        if (binary instanceof BinaryChromosome2) this.binary = (BinaryChromosome2) binary;
        if (randomKey instanceof RandomKey2) this.randomKey = (RandomKey2) randomKey;
        Preconditions.checkArgument(this.binary != null);
        Preconditions.checkArgument(this.randomKey != null);
        Preconditions.checkArgument(this.binary.getLength() == this.randomKey.getLength());
        this.ft = this.calculateFitness();
    }

    /**
     * Un constructor adecuado para crear un objeto por defecto de este tipo
     */
    public PermutationSubListChromosome() {
        super();
        List<Integer> ls1 = BinaryChromosome2.randomBinaryRepresentation(100);
        List<Double> ls2 = RandomKey2.randomPermutation(100);
        BinaryChromosome2 c1 = new BinaryChromosome2(ls1);
        RandomKey2 c2 = new RandomKey2(ls2);
        this.binary = c1;
        this.randomKey = c2;
        this.ft = 0.;
    }

    public static void initialValues(SeqNormalData<Object> data) {
        PermutationSubListChromosome.data = data;
        PermutationSubListChromosome.normalSequence = PermutationSubListChromosome.data.normalSequence();
        PermutationSubListChromosome.DIMENSION = PermutationSubListChromosome.normalSequence.size();
    }

    /**
     * @param dimension La dimensi�n del cromosoma
     * @return Un cromosoma mixto aleatorio
     * @pre Debe estar inicializada la propiedad factory
     */
    private static PermutationSubListChromosome random(Integer dimension) {
        List<Integer> ls1 = BinaryChromosome2.randomBinaryRepresentation(dimension);
        List<Double> ls2 = RandomKey2.randomPermutation(dimension);
        BinaryChromosome2 c1 = new BinaryChromosome2(ls1);
        RandomKey2 c2 = new RandomKey2(ls2);
        return new PermutationSubListChromosome(c1, c2);
    }

    public static PermutationSubListChromosome getInitialChromosome() {
        return PermutationSubListChromosome.random(PermutationSubListChromosome.DIMENSION);
    }

    @Override
    public double fitness() {
        return ft;
    }

    private double calculateFitness() {
        return PermutationSubListChromosome.data.fitnessFunction(this.decode());
    }


    public Integer getObjectsNumber() {
        return PermutationSubListChromosome.data.size();
    }


    public Integer getMax(int i) {
        return PermutationSubListChromosome.data.maxMultiplicity(i);
    }


    @Override
    public ChromosomeFactory.ChromosomeType type() {
        return ChromosomeFactory.ChromosomeType.PermutationSubList;
    }

    @Override
    public Integer size() {
        return PermutationSubListChromosome.data.size();
    }

    @Override
    public Integer itemsNumber() {
        return PermutationSubListChromosome.data.itemsNumber();
    }

    @Override
    public Double fitnessFunction(List<Integer> cr) {
        return PermutationSubListChromosome.data.fitnessFunction(cr);
    }

    @Override
    public Object solution(List<Integer> dc) {
        return PermutationSubListChromosome.data.solution(dc);
    }

    public int compareTo(org.apache.commons.math3.genetics.Chromosome another) {
        if (!(another instanceof PermutationSubListChromosome other))
            throw new IllegalArgumentException();
        Double f1 = this.fitness();
        Double f2 = other.fitness();
        return f1.compareTo(f2);
    }

    /**
     * @return Una lista de enteros obtenida permutando la secuencia normal (0, 1, 2, ..., r-1) filtrada para incluir
     * s�lo los seleccionados por el cromosoma binario
     */
    public List<Integer> decode() {
        List<Integer> rk = randomKey.decode(normalSequence);
        List<Integer> r = List2.empty();
        List<Integer> bn = binary.getRepresentation();
        Preconditions.checkArgument(rk.size() == bn.size());
        for (int i = 0; i < rk.size(); i++) {
            if (bn.get(i) == 1) {
                r.add(rk.get(i));
            }
        }
        return r;
    }


    /**
     * @return La dimensi�n del cromosoma
     */
    public int getLength() {
        return randomKey.getLength();
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof PermutationSubListChromosome other))
            return false;
        if (binary == null) {
            if (other.binary != null)
                return false;
        } else if (!binary.equals(other.binary))
            return false;
        if (randomKey == null) {
            return other.randomKey == null;
        } else return randomKey.equals(other.randomKey);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((binary == null) ? 0 : binary.hashCode());
        result = prime * result
                + ((randomKey == null) ? 0 : randomKey.hashCode());
        return result;
    }

    public String toString() {
        return this.fitness() + "," + this.decode();
    }

    public BinaryChromosome getBinary() {
        return binary;
    }

    public RandomKey<Integer> getRandomKey() {
        return randomKey;
    }

    private static class BinaryChromosome2 extends BinaryChromosome {


        public BinaryChromosome2(Integer[] representation)
                throws InvalidRepresentationException {
            super(representation);
        }


        public BinaryChromosome2(List<Integer> representation)
                throws InvalidRepresentationException {

            super(representation);
        }

        @Override
        public double fitness() {
            return 0;
        }

        @Override

        public AbstractListChromosome<Integer> newFixedLengthChromosome(List<Integer> ls) {
            return new BinaryChromosome2(ls);
        }

        @Override
        public List<Integer> getRepresentation() {
            return super.getRepresentation();
        }
    }

    private static class RandomKey2 extends RandomKey<Integer> {


        public RandomKey2(Double[] representation) throws InvalidRepresentationException {
            super(representation);
        }

        public RandomKey2(List<Double> representation) throws InvalidRepresentationException {
            super(representation);
        }

        @Override
        public double fitness() {
            return 0;
        }

        @Override
        public AbstractListChromosome<Double> newFixedLengthChromosome(List<Double> ls) {
            return new RandomKey2(ls);
        }


    }


}
