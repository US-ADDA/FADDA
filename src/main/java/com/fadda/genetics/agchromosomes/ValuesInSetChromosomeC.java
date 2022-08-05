package com.fadda.genetics.agchromosomes;


import com.fadda.genetics.Chromosome;
import com.fadda.genetics.ValuesInSetData;
import org.apache.commons.math3.genetics.AbstractListChromosome;
import org.apache.commons.math3.genetics.InvalidRepresentationException;
import org.apache.commons.math3.genetics.RandomKey;

import java.util.List;
import java.util.stream.IntStream;

public class ValuesInSetChromosomeC extends RandomKey<Integer>
        implements ValuesInSetData<Object>, Chromosome<List<Integer>> {


    public static ValuesInSetData<Object> data;

    /**
     * Dimensi�n del cromosoma igual start size()
     */

    public static int DIMENSION;
    private final double ft;

    public ValuesInSetChromosomeC(Double[] representation) throws InvalidRepresentationException {
        super(representation);
        this.ft = this.calculateFitness();
    }

    public ValuesInSetChromosomeC(List<Double> representation) throws InvalidRepresentationException {
        super(representation);
        this.ft = this.calculateFitness();
    }

    public static void initialValues(ValuesInSetData<Object> data) {
        ValuesInSetChromosomeC.data = data;
        ValuesInSetChromosomeC.DIMENSION = ValuesInSetChromosomeC.data.size();
    }

    public static ValuesInSetChromosomeC getInitialChromosome() {
        List<Double> ls = RandomKey.randomPermutation(ValuesInSetChromosomeC.DIMENSION);
        return new ValuesInSetChromosomeC(ls);
    }

    @Override
    public AbstractListChromosome<Double> newFixedLengthChromosome(List<Double> ls) {
        return new ValuesInSetChromosomeC(ls);
    }

    private Integer convert(Double e, Integer i) {
        int index = (int) (this.values(i).size() * e);
        return this.values(i).get(index);
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
        return ValuesInSetChromosomeC.data.fitnessFunction(this.decode());
    }


    public Integer getObjectsNumber() {

        return ValuesInSetChromosomeC.data.size();
    }


    public List<Integer> values(int i) {
        return ValuesInSetChromosomeC.data.values(i);
    }

    @Override
    public ChromosomeFactory.ChromosomeType type() {
        return ChromosomeFactory.ChromosomeType.InSet;
    }

    @Override
    public Integer size() {
        return ValuesInSetChromosomeC.data.size();
    }

    @Override
    public List<Integer> values(Integer i) {
        return ValuesInSetChromosomeC.data.values(i);
    }

    @Override
    public Double fitnessFunction(List<Integer> dc) {
        return ValuesInSetChromosomeC.data.fitnessFunction(dc);
    }

    @Override
    public Object solution(List<Integer> dc) {
        return ValuesInSetChromosomeC.data.solution(dc);
    }
}
