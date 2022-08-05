package com.fadda.recursivetypes.ast;

import com.fadda.common.Preconditions;
import com.fadda.common.extension.Set2;

import java.io.PrintStream;
import java.util.Map;
import java.util.Set;

public final class Var implements Exp, Declaration, Operator {

    private final String name;
    private final Type type;
    private Object value;

    private Var(String name, Type type, Object value) {
        super();
        this.name = name;
        this.value = value;
        this.type = type;
    }

    public static Var of(String name, Type type) {
        return new Var(name, type, null);
    }

    public static Var of(String id, Type type, Object value) {
        return new Var(id, type, value);
    }

    public String name() {
        return name;
    }

    public Object value() {
        Preconditions.checkNotNull(this.value, String.format("Valor nulo de %s", this.name()));
        return value;
    }


    public void setValue(Object value) {
        this.value = value;
    }


    public Type type() {
        return type;
    }

    @Override
    public String toString() {
        return String.format("%s", this.name());
    }

    @Override
    public void toDot(PrintStream file, Map<Object, Integer> map) {
        Ast.getIndex(this, map, this.name(), file);
    }

    @Override
    public Operator operator() {
        return this;
    }

    @Override
    public Set<Var> vars() {
        return Set2.of(this);
    }

    @Override
    public OperatorId id() {
        return OperatorId.of0(name);
    }

    @Override
    public Type resultType() {
        return this.type();
    }

    @Override
    public Boolean isConst() {
        return false;
    }

    @Override
    public Var simplify() {
        return this;
    }
}
