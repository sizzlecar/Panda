package com.bluslee.panda.syntactic.analysis;

import com.bluslee.panda.lexical.analysis.TokenReader;

/**
 * 语法解析器，将token解析为AST
 * @author chejinxuan
 */
public interface Parser {

    /**
     * 将token 流解析为 ASTNode
     * @param tokenReader token 流
     * @return ASTNode
     */
    ASTNode parse(TokenReader tokenReader);
}
