package com.bluslee.panda.lexical.analysis;

/**
 * token 接口
 * @author chejinxuan
 */
public interface Token {

    /**
     * 返回 token 类型
     * @return token 的类型
     */
    Tokens getType();

    /**
     * token 内容
     * @return token 内容
     */
    String getContent();
}
