package com.bluslee.panda.syntactic.analysis;

import java.util.List;

/**
 *
 * 默认ASTNode
 * @author chejinxuan
 */
public class DefaultASTNode implements ASTNode {

    /**
     * 父节点
     */
    private ASTNode parent;

    /**
     * 子节点
     */
    private List<ASTNode> childList;

    /**
     * AST type
     */
    private ASTType astType;

    /**
     * 内容
     */
    private String content;

    public DefaultASTNode(ASTNode parent, List<ASTNode> childList, ASTType astType) {
        this.parent = parent;
        this.childList = childList;
        this.astType = astType;
    }

    public DefaultASTNode(ASTNode parent, ASTType astType) {
        this.parent = parent;
        this.astType = astType;
    }

    public DefaultASTNode(ASTNode parent, ASTType astType, String content) {
        this.parent = parent;
        this.astType = astType;
        this.content = content;
    }

    public DefaultASTNode(ASTType astType, String content) {
        this.astType = astType;
        this.content = content;
    }

    public DefaultASTNode() {
    }

    @Override
    public ASTNode getParent() {
        return parent;
    }

    @Override
    public List<ASTNode> getChildList() {
        return childList;
    }

    @Override
    public ASTType getASTType() {
        return astType;
    }

    public void setParent(ASTNode parent) {
        this.parent = parent;
    }

    public void setChildList(List<ASTNode> childList) {
        this.childList = childList;
    }

    public void setASTType(ASTType astType) {
        this.astType = astType;
    }

    @Override
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
