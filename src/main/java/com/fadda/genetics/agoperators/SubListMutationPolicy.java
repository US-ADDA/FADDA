package com.fadda.genetics.agoperators;

import com.fadda.common.Preconditions;
import com.fadda.genetics.agchromosomes.PermutationSubListChromosome;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.genetics.*;

/**
 * <p> Un operador de mutaci�n adecaudo para cromosomas de tipo mixto y que reutiliza los operadores mutaci�n proporcionados
 * en <start href="http:commons.apache.org/proper/commons-math/apidocs/org/apache/commons/math3/genetics/package-summary.html"
 * target="_blank"> Apache Genetics </start>
 * </p>
 *
 * @author Miguel Toro
 */

public class SubListMutationPolicy implements MutationPolicy {

    protected static final MutationPolicy binaryOperator = new BinaryMutation();
    protected static final MutationPolicy randomKeyOperator = new RandomKeyMutation();

    public SubListMutationPolicy() {
        super();
    }

    @Override
    public Chromosome mutate(Chromosome chr0) throws MathIllegalArgumentException {
        if (!(chr0 instanceof PermutationSubListChromosome c0))
            throw new IllegalArgumentException();
        BinaryChromosome binary = c0.getBinary();
        RandomKey<Integer> randomKey = c0.getRandomKey();
        Preconditions.checkArgument(binary != null);
        Preconditions.checkArgument(randomKey != null);
        Chromosome c1 = binaryOperator.mutate(binary);
        Preconditions.checkArgument(c1 instanceof BinaryChromosome);
        Chromosome c2 = randomKeyOperator.mutate(randomKey);
        Preconditions.checkArgument(c2 instanceof RandomKey);
        return new PermutationSubListChromosome(c1, c2);
    }

}
