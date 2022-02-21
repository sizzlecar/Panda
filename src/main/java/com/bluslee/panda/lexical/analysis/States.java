package com.bluslee.panda.lexical.analysis;

/**
 * 状态机状态枚举.
 * @author jinxuan.che
 */
public enum States {

    /**
     * 初始状态
     */
    INITIAL,

    /**
     * 标识符状态
     */
    ID,

    /**
     * 数字状态
     */
    NUMBER,

    /**
     * 操作符状态
     */
    OPERATOR;

}
