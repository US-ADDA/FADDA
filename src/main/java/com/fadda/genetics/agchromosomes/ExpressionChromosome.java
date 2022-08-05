package com.fadda.genetics.agchromosomes;

import com.fadda.common.Preconditions;
import com.fadda.common.extension.List2;
import com.fadda.genetics.Chromosome;
import com.fadda.genetics.ExpressionData;
import com.fadda.recursivetypes.ast.*;
import org.apache.commons.math3.genetics.AbstractListChromosome;
import org.apache.commons.math3.genetics.InvalidRepresentationException;
import org.apache.commons.math3.genetics.RandomKey;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


/**
 * @author Miguel Toro
 */
public class ExpressionChromosome extends RandomKey<Integer> implements Chromosome<Exp> {

    /**
     * Identificadores de las variables start usar
     */
    public static final List<String> variablesNames = Arrays.asList("x", "y", "z" + "t", "u", "v" + "w");
    /**
     * Identificadores de las constantes start usar
     */
    public static final List<String> constantsNames = Arrays.asList("start", "end", "sep" + "d", "e", "f" + "g");
    /**
     * Data del cromosoma
     */
    private static ExpressionData data;
    /**
     * Indice de la primera contante en el cromosoma.
     * Las contantes est�n ubicadas despu�s de los genes
     */
    private static Integer constantsIndex;
    /**
     * El rango de valores que puede tomar cada casilla del cromosoma.
     * La casilla i contendr� un valor v tal que 0 &lg; v &lt;maxRanges.get(i)
     */
    private static List<Integer> maxRanges;
    /**
     * Las variables disponibles
     */
    private static List<Var> variables;
    /**
     * Las variables disponibles
     */
    private static List<Const> constants;
    /**
     * Operadores disponibles
     */
    private static List<Operator> operators;
    private final double ft;

    public ExpressionChromosome(Double[] representation) throws InvalidRepresentationException {
        super(representation);
        this.ft = data.fitnessFunction(decode());
    }

    public ExpressionChromosome(List<Double> representation) throws InvalidRepresentationException {
        super(representation);
        this.ft = data.fitnessFunction(decode());
    }

    /**
     * @pos Inicializa los par�metros de la clase
     */
    public static void initialValues(ExpressionData data) {
        ExpressionChromosome.data = data;
        ExpressionChromosome.operators = data.operators();
        constantsIndex = data.numGens() * data.numItemsPorGen();
        maxRanges = IntStream.range(0, data.size()).map(ExpressionChromosome::getMax).boxed().collect(Collectors.toList());
        variables = getVariables(data.numVariables());
    }

    /**
     * @param i Un indice en maxRanges
     * @return Un valor que especifica el rango de valores en maxRanges[i]
     */
    private static Integer getMax(int i) {
        Integer r = null;
        if (i >= constantsIndex) {
            r = data.maxValueConstant();
        } else {
            i = i % data.numItemsPorGen();
            if (i < data.headLength()) {
                r = data.numConstants() + data.numVariables() + data.operators().size();
            } else if (i < data.headLength() + data.tailLength()) {
                r = data.numConstants() + data.numVariables();
            }
        }
        return r;
    }

    private static List<Var> getVariables(int num) {
        List<Var> ls = List2.empty();
        for (int i = 0; i < num; i++) {
            ls.add(Var.of(variablesNames.get(i), Type.Double));
        }
        return ls;
    }

    public static Var getVariable(int i) {
        return variables.get(i);
    }

    public static Const getConstant(int i) {
        return constants.get(i);
    }

    public static Const getConstant(int i, Object value) {
        value = switch (data.constType()) {
            case Boolean, String -> null;
            case Double -> Operators.toDouble(value);
            case Int -> Operators.toInt(value);
        };
        return Const.of(constantsNames.get(i), value, data.constType());
    }

    public static Operator getOperator(int i) {
        return operators.get(i);
    }

    public static Operator operatorOfInt(int i) {
        Operator op;
        if (i < data.numConstants()) op = getConstant(i);
        else if (i < data.numConstants() + data.numVariables()) op = getVariable(i - data.numConstants());
        else op = getOperator(i - (data.numConstants() + data.numVariables()));
        return op;
    }

    public static List<Integer> gen(List<Integer> ls, int i) {
        Preconditions.checkArgument(i < data.numGens());
        return ls.subList(data.numItemsPorGen() * i, data.numItemsPorGen() * i + data.numItemsPorGen());
    }

    public static List<List<Operator>> levelsOfGen(List<Integer> g) {
        int index = 0;
        int level = 0;
        List<List<Operator>> r = new ArrayList<>();
        r.add(List2.of(operatorOfInt(g.get(index))));
        index++;
        int a = r.get(level).stream().mapToInt(op -> op.id().arity()).sum();
        while (a > 0 && index < g.size()) {
            a = r.get(level).stream().mapToInt(op -> op.id().arity()).sum();
            List<Operator> lv = new ArrayList<>();
            int i = index;
            while (a > 0 && i < index + a) {
                lv.add(operatorOfInt(g.get(index)));
                i++;
            }
            r.add(lv);
            level++;
            index += a;
        }
        return r;
    }

    private static Integer scale(Double e, Integer i) {
        return (int) (maxRanges.get(i) * e);
    }

    public static ExpressionChromosome getInitialChromosome() {
        List<Double> ls = RandomKey.randomPermutation(data.size());
        return new ExpressionChromosome(ls);
    }

    public Double getConstantValue(int i) {
        return super.getRepresentation().get(constantsIndex + i) * data.maxValueConstant();
    }

    @Override
    public AbstractListChromosome<Double> newFixedLengthChromosome(List<Double> ls) {
        return new ExpressionChromosome(ls);
    }

    @Override
    public Exp decode() {
        List<Double> ls = super.getRepresentation();
        List<Integer> items = IntStream.range(0, ls.size()).boxed().map(i -> scale(ls.get(i), i)).toList();
        constants = IntStream.range(0, data.numConstants()).boxed()
                .map(i -> getConstant(i, getConstantValue(i))).toList();
        List<Exp> exps = new ArrayList<>();
        for (int i = 0; i < data.numGens(); i++) {
            List<Integer> g = gen(items, i);
            List<List<Operator>> levels = levelsOfGen(g);
            exps.add(Exp.ofOperatorsInLevels(levels).get(0));
        }
        Exp r = Exp.of(exps, data.nAryOperator());
        return r.simplify();
    }

    @Override
    public List<Double> getRepresentation() {
        return super.getRepresentation();
    }

    @Override
    public double fitness() {
        return ft;
    }

}
