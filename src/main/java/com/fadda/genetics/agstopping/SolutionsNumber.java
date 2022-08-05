package com.fadda.genetics.agstopping;

import com.fadda.common.extension.List2;
import com.fadda.genetics.agchromosomes.GeneticAlgorithm;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.FixedGenerationCount;
import org.apache.commons.math3.genetics.Population;
import org.apache.commons.math3.genetics.StoppingCondition;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

/**
 * <p> Acumula una lista, en la propiedad
 * <code> GeneticAlgorithm.bestChromosomes </code> que guarda los mejores <code> numBestChromosomes </code>
 * distintos de cada generaci�n.</p>
 *
 *
 * <p> Implementa una condici�n de parada que se cumple cuando se encuentran <code> numBestChromosomes </code> cromosomas
 * que cumplan un  <code> predicate </code> o el n�mero de generaciones supera
 * <code> GeneticAlgorithm.NUM_GENERATIONS</code>  </p>
 *
 * <p> La propiedad <code> predicate </code> debe ser inicializada desde el problema concreto start resolver. La propiedad numeroDeGeneraciones
 * guarda el n�mero de generaciones </p>
 *
 * @author Miguel Toro
 */
public class SolutionsNumber implements StoppingCondition {

    public static Integer numGenerations = 0;
    public static Predicate<Chromosome> predicate = null;
    private final Integer ns;
    private final FixedGenerationCount fixed;

    public SolutionsNumber(Integer numBestChromosomes, Integer numGenerations) {
        super();
        this.ns = numBestChromosomes;
        GeneticAlgorithm.bestChromosomes = List2.empty();
        this.fixed = new FixedGenerationCount(numGenerations);
        predicate = (Chromosome x) -> x.fitness() >= StoppingConditionFactory.FITNESS_MIN;
    }

    @Override
    public boolean isSatisfied(Population population) {
        List<Chromosome> r = List2.empty();
        for (Chromosome cr : population) {
            r.add(cr);
        }
        List<Chromosome> ls = r.stream()
                .sorted(Comparator.reverseOrder())
                .distinct()
                .limit(ns)
                .toList();
        GeneticAlgorithm.bestChromosomes.addAll(ls);
        boolean ret = fixed.isSatisfied(population);
        ret = ret || ls.stream().allMatch((Chromosome x) -> predicate.test(x));
        numGenerations++;
        return ret;
    }

}
