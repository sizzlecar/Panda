package com.bluslee.panda.lexical.analysis;

/**
 * 事件枚举
 *
 * @author jinxuan.che
 */
public enum Events {
    /**
     * 接收到数字
     */
    INPUT_NUMBER,

    /**
     * 接收到标识符
     */
    INPUT_ID,

    /**
     * 接收到操作符
     */
    INPUT_OPERATOR,

    /**
     * 接收到字符
     */
    INPUT;
}
