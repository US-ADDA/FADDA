package com.fadda.genetics.agoperators;

import com.fadda.common.Preconditions;
import com.fadda.genetics.agchromosomes.PermutationSubListChromosome;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.genetics.*;

/**
 * <p>
 * Un operador de cruce adecuado para cromosomas de tipo mixto y que reutiliza
 * los operadores de cruce proporcionados en <start href=
 * "http:commons.apache.org/proper/commons-math/apidocs/org/apache/commons/math3/genetics/package-summary.html"
 * target="_blank"> Apache Genetics </start>
 * </p>
 *
 * @author Miguel Toro
 */
public class SubListCrossoverPolicy implements CrossoverPolicy {

    protected final CrossoverPolicy operatorBin;
    protected final CrossoverPolicy operatorKey;

    public SubListCrossoverPolicy(CrossoverPolicy operatorBin, CrossoverPolicy operatorKey) {
        super();
        this.operatorBin = operatorBin;
        this.operatorKey = operatorKey;
    }

    @Override
    public ChromosomePair crossover(Chromosome chr0, Chromosome chr1)
            throws MathIllegalArgumentException {
        if (!(chr0 instanceof PermutationSubListChromosome c0))
            throw new IllegalArgumentException();
        if (!(chr1 instanceof PermutationSubListChromosome c1))
            throw new IllegalArgumentException();
        ChromosomePair binary = operatorBin.crossover(c0.getBinary(), c1.getBinary());
        Preconditions.checkArgument(binary.getFirst() instanceof BinaryChromosome);
        Preconditions.checkArgument(binary.getSecond() instanceof BinaryChromosome);
        ChromosomePair randomKey = operatorKey.crossover(c0.getRandomKey(), c1.getRandomKey());
        Preconditions.checkArgument(randomKey.getFirst() instanceof RandomKey);
        Preconditions.checkArgument(randomKey.getSecond() instanceof RandomKey);
        return new ChromosomePair(new PermutationSubListChromosome(binary.getFirst(),
                randomKey.getFirst()), new PermutationSubListChromosome(binary.getSecond(),
                randomKey.getSecond()));
    }
}
