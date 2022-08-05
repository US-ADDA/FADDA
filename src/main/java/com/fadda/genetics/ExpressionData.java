package com.fadda.genetics;

import com.fadda.genetics.agchromosomes.ChromosomeFactory;
import com.fadda.recursivetypes.ast.Exp;
import com.fadda.recursivetypes.ast.Operator;
import com.fadda.recursivetypes.ast.Type;

import java.util.List;

public interface ExpressionData extends ChromosomeData<Exp, Exp> {

    /**
     * @return La longitud de la cabeza de un gen
     */
    Integer headLength();

    /**
     * @return N�mero de Genes
     */
    Integer numGens();

    /**
     * @return N�mero de variables
     */
    Integer numVariables();

    /**
     * @return Numero de constantes
     */
    Integer numConstants();

    /**
     * @return El rango m�ximo del valor de cada constante. Cada constante tendr� un valor en el rango 0..getMaxValueConstant()-1
     */
    Integer maxValueConstant();

    /**
     * @return Tipo de las constantes
     */
    Type constType();

    /**
     * @return Operadores disponibles
     */
    List<Operator> operators();

    /**
     * @return Operador n-ario para combinar los resultados de los genes
     */
    Operator.Nary nAryOperator();

    default Integer maxArity() {
        return operators().stream().mapToInt(x -> x.id().arity()).max().getAsInt();
    }

    default Integer tailLength() {
        return headLength() * (maxArity() - 1) + 1;
    }

    default Integer numItemsPorGen() {
        return headLength() + tailLength();
    }

    default Integer size() {
        return numItemsPorGen() * numGens() + numConstants();
    }

    default ChromosomeFactory.ChromosomeType type() {
        return ChromosomeFactory.ChromosomeType.Expression;
    }
}
