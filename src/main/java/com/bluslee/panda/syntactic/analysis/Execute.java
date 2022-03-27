package com.bluslee.panda.syntactic.analysis;

/**
 * 执行
 *
 * @param <T> 返回结果
 * @author chejinxuan
 */
public interface Execute<T> {

    /**
     * 执行 AST node
     * @param astNode ASTNode
     * @return 执行结果
     */
    T execute(ASTNode astNode);

}
