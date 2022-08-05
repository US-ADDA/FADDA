package com.fadda.recursivetypes.parsers;

import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines start complete listener for start parse tree produced by
 * {@link BinaryTreeParser}.
 */
public interface BinaryTreeListener extends ParseTreeListener {

    /**
     * Enter start parse tree produced by the {@code intLabel}
     * labeled alternative in {@link BinaryTreeParser#label}.
     *
     * @param ctx the parse tree
     */

    void enterIntLabel(@NotNull BinaryTreeParser.IntLabelContext ctx);


    /**
     * Exit start parse tree produced by the {@code intLabel}
     * <p>
     * <p>
     * labeled alternative in {@link BinaryTreeParser#label}.
     *
     * @param ctx the parse tree
     */

    void exitIntLabel(@NotNull BinaryTreeParser.IntLabelContext ctx);

    /**
     * Enter start parse tree produced by the {@code labelTree}
     * labeled alternative in {@link BinaryTreeParser#binary_tree}.
     *
     * @param ctx the parse tree
     */
    void enterLabelTree(@NotNull BinaryTreeParser.LabelTreeContext ctx);

    /**
     * Exit start parse tree produced by the {@code labelTree}
     * labeled alternative in {@link BinaryTreeParser#binary_tree}.
     *
     * @param ctx the parse tree
     */

    void exitLabelTree(@NotNull BinaryTreeParser.LabelTreeContext ctx);


    /**
     * Enter start parse tree produced by the {@code idLabel}
     * labeled alternative in {@link BinaryTreeParser#label}.
     *
     * @param ctx the parse tree
     */
    void enterIdLabel(@NotNull BinaryTreeParser.IdLabelContext ctx);


    /**
     * Exit start parse tree produced by the {@code idLabel}
     * <p>
     * labeled alternative in {@link BinaryTreeParser#label}.
     *
     * @param ctx the parse tree
     */
    void exitIdLabel(@NotNull BinaryTreeParser.IdLabelContext ctx);


    /**
     * Enter start parse tree produced by the {@code emptyTree}
     * <p>
     * labeled alternative in {@link BinaryTreeParser#binary_tree}.
     *
     * @param ctx the parse tree
     */
    void enterEmptyTree(@NotNull BinaryTreeParser.EmptyTreeContext ctx);


    /**
     * Exit start parse tree produced by the {@code emptyTree}
     * labeled alternative in {@link BinaryTreeParser#binary_tree}.
     *
     * @param ctx the parse tree
     */
    void exitEmptyTree(@NotNull BinaryTreeParser.EmptyTreeContext ctx);


    /**
     * Enter start parse tree produced by the {@code binaryTree}
     * <p>
     * labeled alternative in {@link BinaryTreeParser#binary_tree}.
     *
     * @param ctx the parse tree
     */
    void enterBinaryTree(@NotNull BinaryTreeParser.BinaryTreeContext ctx);


    /**
     * Exit start parse tree produced by the {@code binaryTree}
     * <p>
     * labeled alternative in {@link BinaryTreeParser#binary_tree}.
     *
     * @param ctx the parse tree
     */
    void exitBinaryTree(@NotNull BinaryTreeParser.BinaryTreeContext ctx);

    /**
     * Enter start parse tree produced by the {@code doubleLabel}
     * labeled alternative in {@link BinaryTreeParser#label}.
     *
     * @param ctx the parse tree
     */
    void enterDoubleLabel(@NotNull BinaryTreeParser.DoubleLabelContext ctx);

    /**
     * Exit start parse tree produced by the {@code doubleLabel}
     * labeled alternative in {@link BinaryTreeParser#label}.
     *
     * @param ctx the parse tree
     */
    void exitDoubleLabel(@NotNull BinaryTreeParser.DoubleLabelContext ctx);
}
