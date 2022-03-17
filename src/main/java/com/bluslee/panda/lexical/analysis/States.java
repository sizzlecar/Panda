package com.bluslee.panda.lexical.analysis;

import lombok.Getter;

/**
 * 状态机状态枚举.
 * @author jinxuan.che
 */
@Getter
public enum States {

    /**
     * 初始状态
     */
    INITIAL(null),

    /**
     * 标识状态
     */
    ID(Tokens.IDENTIFIER),

    /**
     * 数字状态
     */
    NUMBER(Tokens.NUMBER),

    /**
     * 操作符状态
     */
    OPERATOR(Tokens.OPERATOR);

    private final Tokens tokens;

    States(Tokens tokens) {
        this.tokens = tokens;
    }
}
