package com.bluslee.panda.lexical.analysis;

/**
 * 词法分析器
 * @author chejinxuan
 */
public interface Lexer {

    /**
     * 解析脚本，返回token 流
     * @param script 脚本
     * @return token 流
     */
    TokenReader parse(String script);
}
