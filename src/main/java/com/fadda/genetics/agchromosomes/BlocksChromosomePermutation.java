package com.fadda.genetics.agchromosomes;

import com.fadda.genetics.BlocksData;
import com.fadda.genetics.Chromosome;
import org.apache.commons.math3.genetics.AbstractListChromosome;
import org.apache.commons.math3.genetics.InvalidRepresentationException;
import org.apache.commons.math3.genetics.RandomKey;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

public class BlocksChromosomePermutation extends RandomKey<Integer>
        implements BlocksData<Object>, Chromosome<List<Integer>> {

    public static BlocksData<Object> data;
    public static int DIMENSION;
    private final double ft;

    public BlocksChromosomePermutation(Double[] representation) throws InvalidRepresentationException {
        super(representation);
        this.ft = this.calculateFitness();
    }

    public BlocksChromosomePermutation(List<Double> representation) throws InvalidRepresentationException {
        super(representation);
        this.ft = this.calculateFitness();
    }

    public static void initialValues(BlocksData<Object> data) {
        BlocksChromosomePermutation.data = data;
        BlocksChromosomePermutation.DIMENSION = BlocksChromosomePermutation.data.size();
    }

    public static BlocksChromosomePermutation getInitialChromosome() {
        List<Double> ls = RandomKey.randomPermutation(BlocksChromosomePermutation.DIMENSION);
        return BlocksChromosomePermutation.of(ls);
    }

    public static BlocksChromosomePermutation of(List<Double> representation) throws InvalidRepresentationException {
        return new BlocksChromosomePermutation(representation);
    }


    public static BlocksChromosomePermutation of(Double[] representation) throws InvalidRepresentationException {
        return new BlocksChromosomePermutation(representation);
    }


    @Override
    public AbstractListChromosome<Double> newFixedLengthChromosome(List<Double> ls) {
        return new BlocksChromosomePermutation(ls);
    }

    @Override
    public List<Integer> decode() {
        List<Double> r = super.getRepresentation();
        List<Integer> s = new ArrayList<>();
        List<Integer> p = this.blocksLimits();
        int pn = p.size();
        for (int i = 0; i < pn - 1; i++) {
            int tb = p.get(i + 1) - p.get(i);
            List<Double> rp = r.subList(p.get(i), p.get(i + 1));
            List<Integer> values = this.initialValues().subList(p.get(i), p.get(i + 1));
            List<Dt> dts = new ArrayList<>(IntStream.range(0, tb)
                    .boxed()
                    .map(x -> new Dt(rp.get(x), values.get(x)))
                    .toList());
            dts.sort(Comparator.comparing(d -> d.first));
            List<Integer> sortValues = dts.stream().map(Dt::second).toList();
            s.addAll(sortValues);
        }
        return s;
    }

    private double calculateFitness() {
        return BlocksChromosomePermutation.data.fitnessFunction(this.decode());
    }

    @Override
    public double fitness() {
        return ft;
    }

    @Override
    public Double fitnessFunction(List<Integer> cr) {
        return BlocksChromosomePermutation.data.fitnessFunction(cr);
    }

    @Override
    public Object solution(List<Integer> cr) {
        return BlocksChromosomePermutation.data.solution(cr);
    }

    @Override
    public Integer size() {
        return BlocksChromosomePermutation.data.size();
    }

    @Override
    public ChromosomeFactory.ChromosomeType type() {
        return BlocksChromosomePermutation.data.type();
    }

    @Override
    public List<Integer> blocksLimits() {
        return BlocksChromosomePermutation.data.blocksLimits();
    }

    @Override
    public List<Integer> initialValues() {
        return BlocksChromosomePermutation.data.initialValues();
    }

    private record Dt(Double first, Integer second) {
    }

}
