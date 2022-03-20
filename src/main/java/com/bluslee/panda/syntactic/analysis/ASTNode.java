package com.bluslee.panda.syntactic.analysis;

import java.util.List;

/**
 * 抽象语法树接口
 * @author chejinxuan
 */
public interface ASTNode {

    /**
     * 获取当前节点的父节点
     * @return 父节点
     */
    ASTNode getParent();

    /**
     * 获取当前节点的所有子节点
     * @return 子节点
     */
    List<ASTNode> getChildList();

    /**
     * 获取当前节点的类型
     * @return  类型
     */
    ASTType getASTType();

    /**
     * 获取当前节点内容
     * @return 内容
     */
    String getContent();
}
