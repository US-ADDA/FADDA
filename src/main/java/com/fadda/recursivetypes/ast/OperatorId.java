package com.fadda.recursivetypes.ast;

public interface OperatorId {

    static OperatorId of0(String name) {
        return new OperatorId0(name, 0);
    }

    static OperatorId of1(String name, Type tp) {
        return new OperatorId1(name, tp, 1);
    }

    static OperatorId of2(String name, Type tp1, Type tp2) {
        return new OperatorId2(name, tp1, tp2, 2);
    }

    static OperatorId ofN(String name, Type tp) {
        return new OperatorIdN(name, tp, -1);
    }

    String name();

    Integer arity();

    String longName();

    record OperatorId0(String name, Integer arity) implements OperatorId {
        public String longName() {
            return String.format("%s%d", name, arity);
        }
    }

    record OperatorId1(String name, Type tp, Integer arity) implements OperatorId {
        public String longName() {
            return String.format("%s%d%s", name, arity, tp);
        }
    }

    record OperatorId2(String name, Type tp1, Type tp2, Integer arity) implements OperatorId {
        public String longName() {
            return String.format("%s%d%s%s", name, arity, tp1, tp2);
        }
    }

    record OperatorIdN(String name, Type tp, Integer arity) implements OperatorId {
        public String longName() {
            return String.format("%s%d%s", name, arity, tp);
        }
    }
}
