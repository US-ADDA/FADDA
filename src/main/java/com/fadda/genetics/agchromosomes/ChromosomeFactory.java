package com.fadda.genetics.agchromosomes;

import com.fadda.common.Preconditions;
import com.fadda.genetics.Chromosome;
import com.fadda.genetics.*;
import com.fadda.genetics.agoperators.SubListCrossoverPolicy;
import com.fadda.genetics.agoperators.SubListMutationPolicy;
import org.apache.commons.math3.genetics.*;

import java.util.List;


/**
 * @author Miguel Toro
 *
 * <p> Una factor�start de cromosomas de los distintos tipos implementados. </p>
 */
public class ChromosomeFactory {

    /**
     * Tipo del operador de cruce
     */
    public static final CrossoverType crossoverType = CrossoverType.OnePoint;
    /**
     * N�mero de puntos usados en la partici�n si se usa un operador de cruce de tipo NPointCrossover
     */
    public static final int NPOINTCROSSOVER = 3;
    /**
     * La ratio si se usa el operador de cruce de tipo UniformCrossover
     */
    public static final double RATIO_UNIFORMCROSSOVER = 0.7;
    /**
     * <p> Para aplicar los operadores de mutaci�n se escogen dos cromosomas.
     * La t�cnica implementada para escoger cada uno de los dos cromosomas se denomina selecci�n por torneo.
     * Se trata de organizar dos torneos.
     * En cada uno se elige el mejor cromosoma de entrre <code> TOURNAMENT_ARITY </code> cromosomas de la poblaci�n seleccionados al azar.
     * Si el tama�o de <code> TOURNAMENT_ARITY </code> es m�s grande, los cromosomas
     * d�biles tienen menor probabilidad de ser seleccionados.</p>
     *
     * <p> N�mero de participantes en el torneo para elegir los cromosomas que participar�n en el cruce </p>
     * <p> Un valor t�pico es 2 </p>
     */

    public static final int TOURNAMENT_ARITY = 2;
    public static ChromosomeType tipo;

    /**
     * @param tipo El tipo de cromosoma
     * @return Un cromosoma aleatorio del tipo indicado
     */
    @SuppressWarnings("unchecked")
    public static <E> Chromosome<E> randomChromosome(ChromosomeType tipo) {
        return switch (tipo) {
            case Binary -> (Chromosome<E>) BinaryChromosome.getInitialChromosome();
            case SubList -> (Chromosome<E>) SubListChromosome.getInitialChromosome();
            case Range -> (Chromosome<E>) RangeChromosome.getInitialChromosome();
            case Permutation -> (Chromosome<E>) PermutationChromosome.getInitialChromosome();
            case PermutationSubList -> (Chromosome<E>) PermutationSubListChromosome.getInitialChromosome();
            case Real -> (Chromosome<E>) DoubleChromosome.getInitialChromosome();
            case InSet -> (Chromosome<E>) ValuesInSetChromosomeC.getInitialChromosome();
            case Blocks -> (Chromosome<E>) BlocksChromosomePermutation.getInitialChromosome();
            case Expression -> (Chromosome<E>) ExpressionChromosome.getInitialChromosome();
        };
    }

    /**
     * @param tipo El tipo del cromosoma
     * @return Un operador de cruce adecuado para un cromosma del tipo indicado
     */
    public static CrossoverPolicy getCrossoverPolicy(ChromosomeType tipo) {
        CrossoverPolicy crossOverPolicyBin = switch (crossoverType) {
            case Cycle -> new CycleCrossover<Integer>();
            case NPoint -> new NPointCrossover<Integer>(NPOINTCROSSOVER);
            case OnePoint -> new OnePointCrossover<Integer>();
            case Ordered -> new OrderedCrossover<Integer>();
            case Uniform -> new UniformCrossover<Integer>(RATIO_UNIFORMCROSSOVER);
        };
        CrossoverPolicy crossOverPolicyKey = switch (crossoverType) {
            case Cycle -> new CycleCrossover<Double>();
            case NPoint -> new NPointCrossover<Double>(NPOINTCROSSOVER);
            case OnePoint -> new OnePointCrossover<Double>();
            case Ordered -> new OrderedCrossover<Double>();
            case Uniform -> new UniformCrossover<Double>(RATIO_UNIFORMCROSSOVER);
        };
        return switch (tipo) {
            case Binary, SubList -> crossOverPolicyBin;
            case Range, Permutation, Real, InSet, Blocks, Expression -> crossOverPolicyKey;
            case PermutationSubList -> new SubListCrossoverPolicy(crossOverPolicyBin, crossOverPolicyKey);
        };
    }

    /**
     * @param tipo El tipo del cromosoma
     * @return Un operador de mutaci�n adecuado para un cromosoma del tipo indicado
     */
    public static MutationPolicy getMutationPolicy(ChromosomeType tipo) {
        MutationPolicy mutationPolicy = null;
        switch (tipo) {
            case Binary, SubList -> mutationPolicy = new BinaryMutation();
            case Range, Real, Permutation, InSet, Blocks, Expression -> mutationPolicy = new RandomKeyMutation();
            case PermutationSubList -> mutationPolicy = new SubListMutationPolicy();
        }
        Preconditions.checkState(mutationPolicy != null);
        return mutationPolicy;
    }

    /**
     * @return Un operador que implementa la pol�tica de selecci�n escogida
     */
    public static SelectionPolicy getSelectionPolicy() {
        return new TournamentSelection(TOURNAMENT_ARITY);
    }

    /**
     * @param tipo Tipo de cromosoma
     * @post El m�todo inicializa los par�metros relevantes de la clase que implementa el tipo indicado de cromosoma
     */
    @SuppressWarnings("unchecked")
    public static <E, S> void iniValues(ChromosomeData<E, S> data, ChromosomeType tipo) {
        switch (tipo) {
            case Binary -> BinaryChromosome.initialValues((ChromosomeData<List<Integer>, Object>) data);
            case SubList -> SubListChromosome.iniValues((SeqNormalData<Object>) data);
            case Range -> RangeChromosome.initialValues((ValuesInRangeData<Integer, Object>) data);
            case Permutation -> PermutationChromosome.iniValues((SeqNormalData<Object>) data);
            case PermutationSubList -> PermutationSubListChromosome.initialValues((SeqNormalData<Object>) data);
            case Real -> DoubleChromosome.initialValues((ValuesInRangeData<Double, Object>) data);
            case InSet -> ValuesInSetChromosomeC.initialValues((ValuesInSetData<Object>) data);
            case Blocks -> BlocksChromosomePermutation.initialValues((BlocksData<Object>) data);
            case Expression -> ExpressionChromosome.initialValues((ExpressionData) data);
        }
    }

    /**
     * Los diferentes tipos de cromosmomas implementados
     */
    public enum ChromosomeType {Binary, Range, Real, InSet, SubList, Permutation, PermutationSubList, Blocks, Expression}

    /**
     * <p>Distintos tipo de operadores de cruce </p>
     *
     * <ul>
     * <li> <start href="http:commons.apache.org/proper/commons-math/apidocs/org/apache/commons/math3/genetics/OnePointCrossover.html" target="_blank"> OnePointCrossover </start>
     * <li> <start href="http:commons.apache.org/proper/commons-math/apidocs/org/apache/commons/math3/genetics/NPointCrossover.html" target="_blank"> NPointCrossover </start>
     * <li> <start href="http:commons.apache.org/proper/commons-math/apidocs/org/apache/commons/math3/genetics/CycleCrossover.html" target="_blank"> CycleCrossover </start>
     * <li> <start href="http:commons.apache.org/proper/commons-math/apidocs/org/apache/commons/math3/genetics/OrderedCrossover.html" target="_blank"> OrderedCrossover </start>
     * <li> <start href="http:commons.apache.org/proper/commons-math/apidocs/org/apache/commons/math3/genetics/UniformCrossover.html" target="_blank"> UniformCrossover </start>
     * </ul>
     */

    public enum CrossoverType {Cycle, NPoint, OnePoint, Ordered, Uniform}
}
