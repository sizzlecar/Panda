package com.bluslee.panda.syntactic.analysis;

/**
 * AST node type
 * @author chejinxuan
 */
public enum ASTType {

    /**
     * 根节点类型
     */
    PROGRAMMER,

    /**
     * 表达式节点
     */
    EXPRESSION,

    /**
     * 加法表达式
     */
    ADDITIVE,

    /**
     * 乘法表达式
     */
    MULTIPLICATIVE,

    /**
     * 整数
     */
    INT,

    /**
     * +
     */
    ADD,

    /**
     * -
     */
    SUB,

    /**
     * *
     */
    MULTI,

    /**
     * /
     */
    DIV,

    /**
     * (
     */
    LEFT_BRACKETS,

    /**
     * )
     */
    RIGHT_BRACKETS
}
