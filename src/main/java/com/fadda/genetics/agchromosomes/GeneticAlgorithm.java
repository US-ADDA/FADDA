package com.fadda.genetics.agchromosomes;

import com.fadda.common.Preconditions;
import com.fadda.genetics.Chromosome;
import com.fadda.genetics.ChromosomeData;
import com.fadda.genetics.agchromosomes.ChromosomeFactory.ChromosomeType;
import com.fadda.genetics.agstopping.StoppingConditionFactory;
import org.apache.commons.math3.genetics.*;
import org.apache.commons.math3.random.JDKRandomGenerator;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p> Implementaci�n de un Algoritmo Gen�tico </p>
 *
 * @author Miguel Toro
 */
public record GeneticAlgorithm<E, S>(ChromosomeData<E, S> data) {

    /**
     * Tama�o de la poblaci�n. Usualmente de un valor cercano start la DIMENSION de los cromosomas o mayor
     */
    public static final int POPULATION_SIZE = 30;
    /**
     * Tasa de elitismo. El porcentaje especificado de los mejores cromosomas pasa start la siguiente generaci�n sin cambio
     */
    public static final double ELITISM_RATE = 0.2;
    /**
     * Tasa de cruce: Indica con qu� frecuencia se va start realizar la cruce.
     * Si no hay un cruce, la descendencia es copia exacta de los padres.
     * Si hay un cruce, la descendencia est� hecha de partes del cromosoma de los padres.
     * Si la probabilidad de cruce es 100%, entonces todos los hijos se hacen mediante cruce de los padres
     * Si es 0%, la nueva generaci�n se hace de copias exactas de los cromosomas de los padres.
     * El cruce se hace con la esperanza de que los nuevos cromosomas tendr�n las partes buenas de los padres
     * y tal vez los nuevos cromosomas ser�n mejores. Sin embargo, es bueno dejar una cierta parte de la poblaci�n sobrevivir start la siguiente generaci�n.
     *
     * <br>
     * Tasa de cruce. Valores usuales entre  0,.8 y 0.95
     */

    public static final double CROSSOVER_RATE = 0.8;
    /**
     * La tasa de de mutaci�n indica con qu� frecuencia ser�n mutados cada uno de los cromosomas mutados.
     * Si no hay mutaci�n, la descendencia se toma despu�s de cruce sin ning�n cambio.
     * Si se lleva start cabo la mutaci�n, se cambia una parte del cromosoma.
     * Si probabilidad de mutaci�n es 100%, toda cromosoma se cambia, si es 0%, no se cambia ninguno.
     * La mutaci�n se hace para evitar que se caiga en un m�ximo local.
     * <p>
     * <p>
     * Tasa de mutaci�n. Valores usales entre 0.5 y 1.
     */
    public static final double MUTATION_RATE = 0.6;
    public static long INITIAL_TIME;
    public static ChromosomeType tipo;
    public static CrossoverPolicy crossOverPolicy;
    public static MutationPolicy mutationPolicy;
    public static SelectionPolicy selectionPolicy;
    public static StoppingCondition stopCond;
    /**
     * Lista con los mejores cromosomas de cada una de la generaciones si se usa la condici�n de parada SolutionsNumbers.
     * En otro caso null.
     */
    public static List<org.apache.commons.math3.genetics.Chromosome> bestChromosomes;
    public static JDKRandomGenerator random;
    private static Population initialPopulation;
    private static org.apache.commons.math3.genetics.Chromosome bestFinal;
    private static Population finalPopulation;

    /**
     *
     */
    public GeneticAlgorithm(ChromosomeData<E, S> data) {
        random = new JDKRandomGenerator();
        random.setSeed((int) System.currentTimeMillis());
        org.apache.commons.math3.genetics.GeneticAlgorithm.setRandomGenerator(random);
        this.data = data;
        GeneticAlgorithm.tipo = data.type();
        GeneticAlgorithm.selectionPolicy = ChromosomeFactory.getSelectionPolicy();
        GeneticAlgorithm.mutationPolicy = ChromosomeFactory.getMutationPolicy(tipo);
        GeneticAlgorithm.crossOverPolicy = ChromosomeFactory.getCrossoverPolicy(tipo);
        GeneticAlgorithm.stopCond = StoppingConditionFactory.getStoppingCondition();
        ChromosomeFactory.iniValues(data, tipo);
    }

    /**
     * @return GeneticAlgorithm
     */

    public static <E, S> GeneticAlgorithm<E, S> of(ChromosomeData<E, S> data) {
        return new GeneticAlgorithm<>(data);
    }

    /**
     * Inicializa aleatoriamente la poblaci�n.
     */
    public ElitisticListPopulation randomPopulation() {
        List<org.apache.commons.math3.genetics.Chromosome> popList = new LinkedList<>();

        for (int i = 0; i < POPULATION_SIZE; i++) {
            org.apache.commons.math3.genetics.Chromosome randChrom =
                    (org.apache.commons.math3.genetics.Chromosome) ChromosomeFactory.randomChromosome(GeneticAlgorithm.tipo);
            popList.add(randChrom);
        }
        return new ElitisticListPopulation(popList, popList.size(), ELITISM_RATE);
    }

    /**
     * Ejecuta el algoritmo
     */
    public void execute() {
        INITIAL_TIME = System.currentTimeMillis();
        GeneticAlgorithm.initialPopulation = randomPopulation();
        Preconditions.checkNotNull(GeneticAlgorithm.initialPopulation);

        org.apache.commons.math3.genetics.GeneticAlgorithm ga = new org.apache.commons.math3.genetics.GeneticAlgorithm(
                crossOverPolicy,
                CROSSOVER_RATE,
                mutationPolicy,
                MUTATION_RATE,
                selectionPolicy);

        GeneticAlgorithm.finalPopulation = ga.evolve(GeneticAlgorithm.initialPopulation, GeneticAlgorithm.stopCond);
        Preconditions.checkNotNull(GeneticAlgorithm.finalPopulation);
        GeneticAlgorithm.bestFinal = GeneticAlgorithm.finalPopulation.getFittestChromosome();
    }

    /**
     * @return Poblaci�n inicial
     */
    public Population getInitialPopulation() {
        return initialPopulation;
    }

    /**
     * @return El mejor cromosoma en la poblaci�n final
     */
    @SuppressWarnings("unchecked")
    public Chromosome<E> getBestChromosome() {
        return (Chromosome<E>) bestFinal;
    }

    @SuppressWarnings("unchecked")
    public ChromosomeData<E, S> getBestChromosomeData() {
        return (ChromosomeData<E, S>) bestFinal;
    }

    @SuppressWarnings("unchecked")
    public List<Chromosome<E>> getBestChromosomes() {
        return bestChromosomes.stream()
                .map(x -> (Chromosome<E>) x)
                .collect(Collectors.toList());
    }

    /**
     * @return Poblaci�n final
     */
    public Population getFinalPopulation() {
        return finalPopulation;
    }

    public S bestSolution() {
        return this.getBestChromosomeData().solution(this.getBestChromosome().decode());
    }

    public Set<S> bestSolutions() {
        ChromosomeData<E, S> d = this.getBestChromosomeData();
        return this.getBestChromosomes().stream().map(c -> d.solution(c.decode())).collect(Collectors.toSet());
    }

}
