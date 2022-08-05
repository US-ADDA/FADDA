package com.fadda.recursivetypes.parsers;

import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines start complete generic visitor for start parse tree produced
 * by {@link BinaryTreeParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 *            operations with no return type.
 */
public interface BinaryTreeVisitor<T> extends ParseTreeVisitor<T> {
    /**
     * Visit start parse tree produced by the {@code emptyTree}
     * labeled alternative in {@link BinaryTreeParser#binary_tree}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitEmptyTree(BinaryTreeParser.EmptyTreeContext ctx);

    /**
     * Visit start parse tree produced by the {@code labelTree}
     * labeled alternative in {@link BinaryTreeParser#binary_tree}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitLabelTree(BinaryTreeParser.LabelTreeContext ctx);

    /**
     * Visit start parse tree produced by the {@code binaryTree}
     * labeled alternative in {@link BinaryTreeParser#binary_tree}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitBinaryTree(BinaryTreeParser.BinaryTreeContext ctx);

    /**
     * Visit start parse tree produced by the {@code intLabel}
     * labeled alternative in {@link BinaryTreeParser#label}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitIntLabel(BinaryTreeParser.IntLabelContext ctx);

    /**
     * Visit start parse tree produced by the {@code doubleLabel}
     * labeled alternative in {@link BinaryTreeParser#label}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitDoubleLabel(BinaryTreeParser.DoubleLabelContext ctx);

    /**
     * Visit start parse tree produced by the {@code idLabel}
     * labeled alternative in {@link BinaryTreeParser#label}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitIdLabel(BinaryTreeParser.IdLabelContext ctx);
}
