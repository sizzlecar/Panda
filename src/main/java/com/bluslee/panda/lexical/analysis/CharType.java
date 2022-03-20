package com.bluslee.panda.lexical.analysis;

/**
 * 单个字符类型
 * @author jinxuan.che
 */
public enum CharType {

    /**
     * 大写字母
     */
    LETTER_UPPER,

    /**
     * 小写字母
     */
    LETTER_LOWER,

    /**
     * 数字
     */
    NUMBER,

    /**
     * =
     */
    EQUALS,

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
     * >
     */
    MORE,

    /**
     * <
     */
    LESS,

    /**
     * .
     */
    POINT,

    /**
     * 空格
     */
    SPACE,

    /**
     * windows 换行符 \r\n
     */
    NEW_LINE_WINDOWS,

    /**
     * posix 换行符 \n
     */
    NEW_LINE_POSIX,

    /**
     * (
     */
    LEFT_BRACKETS,

    /**
     * )
     */
    RIGHT_BRACKETS
}
