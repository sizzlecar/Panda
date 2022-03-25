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
     * 设置当前节点父节点
     * @param parent 父节点
     */
    void setParent(ASTNode parent);

    /**
     * 获取当前节点的所有子节点
     * @return 子节点
     */
    List<ASTNode> getChildList();

    /**
     * 设置当前节点的子节点
     * @param childList 子节点
     */
    void setChildList(List<ASTNode> childList);

    /**
     * add child
     * @param node 节点
     */
    void addChild(ASTNode node);

    /**
     * 获取当前节点的类型
     * @return  类型
     */
    ASTType getASTType();

    /**
     * 设置AST type
     * @param astType AST type
     */
    void setASTType(ASTType astType);

    /**
     * 获取当前节点内容
     * @return 内容
     */
    String getContent();

    /**
     * 设置当前节点内容
     * @param content 内容
     */
    void setContent(String content);
}
