package com.bluslee.panda.syntactic.analysis;

import com.bluslee.panda.lexical.analysis.Token;
import com.bluslee.panda.lexical.analysis.TokenReader;
import com.bluslee.panda.lexical.analysis.Tokens;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;

/**
 * 四则运算语法解析器
 * grammar Expr;
 * programmer:	(expr NEWLINE)* ;
 * expr:	expr (MULTI|DIV) expr
 * |	    expr (ADD|SUB) expr
 * |	    INT
 * |	    '(' expr ')'
 * ;
 * NEWLINE : [\n]+ ;
 * INT     : [0-9]+ ;
 * ADD     : '+' ;
 * SUB     : '-' ;
 * MULTI   : '*' ;
 * DIV     : '/' ;
 *
 * @author chejinxuan
 */
@Slf4j
@Component
public class CalcParser implements Parser {

    @Override
    public ASTNode parse(TokenReader tokenReader) {
        if (tokenReader == null || tokenReader.peek() == null) {
            log.error("tokenReader is null");
            return null;
        }
        //初始化根节点
        DefaultASTNode root = new DefaultASTNode(null, ASTType.PROGRAMMER);
        //递归解析成AST
        parse(tokenReader, root);
        return root;
    }

    private ASTNode parse(TokenReader tokenReader, DefaultASTNode root) {
        if (tokenReader.peek() == null) {
            return null;
        }
        ASTNode childNode;
        childNode = parseMultiExpression(tokenReader, root);
        if (childNode == null) {
            childNode = parseAddExpression(tokenReader, root);
        }
        if (childNode == null) {
            childNode = parseInt(tokenReader);
        }
        root.setChildList(Collections.singletonList(childNode));
        return parse(tokenReader, root);
    }

    /**
     * 解析加法表达式,这里减法也看作特殊的加法
     *
     * @param tokenReader token 流
     * @return ASTNode
     */
    private ASTNode parseAddExpression(TokenReader tokenReader, DefaultASTNode root) {
        DefaultASTNode leftExp = new DefaultASTNode(ASTType.EXPRESSION, null);
        ASTNode leftNode = parse(tokenReader, leftExp);
        ASTNode opNode = parseAddOperator(tokenReader);
        if (opNode == null) {
            return null;
        }
        DefaultASTNode rightExp = new DefaultASTNode(ASTType.EXPRESSION, null);
        ASTNode rightNode = parse(tokenReader, rightExp);
        root.setChildList(Arrays.asList(leftNode, opNode, rightNode));
        return root;
    }

    /**
     * 解析乘法表达式，这里除法也看作特殊的乘法
     *
     * @param tokenReader token 流
     * @return ASTNode
     */
    private ASTNode parseMultiExpression(TokenReader tokenReader, DefaultASTNode root) {
        DefaultASTNode leftExp = new DefaultASTNode(ASTType.EXPRESSION, null);
        ASTNode leftNode = parse(tokenReader, leftExp);
        ASTNode opNode = parseMultiOperator(tokenReader);
        if (opNode == null) {
            return null;
        }
        DefaultASTNode rightExp = new DefaultASTNode(ASTType.EXPRESSION, null);
        ASTNode rightNode = parse(tokenReader, rightExp);
        root.setChildList(Arrays.asList(leftNode, opNode, rightNode));
        return root;
    }

    /**
     * 解析整数
     *
     * @param tokenReader token 流
     * @return ASTNode
     */
    private ASTNode parseInt(TokenReader tokenReader) {
        Token token = tokenReader.peek();
        if (!Tokens.NUMBER.equals(token.getType())) {
            return null;
        }
        tokenReader.next();
        return new DefaultASTNode(ASTType.INT, token.getContent());
    }

    /**
     * 解析加法操作符
     *
     * @param tokenReader token 流
     * @return ASTNode
     */
    private ASTNode parseAddOperator(TokenReader tokenReader) {
        Token token = tokenReader.peek();
        String content = token.getContent();
        Tokens type = token.getType();
        boolean opTypeFlag = Tokens.OPERATOR.equals(type);
        boolean plusFlag = "+".equals(content);
        boolean subFlag = "-".equals(content);
        if (!opTypeFlag || (!plusFlag && !subFlag)) {
            return null;
        }
        tokenReader.next();
        return new DefaultASTNode(plusFlag ? ASTType.ADD : ASTType.SUB, content);
    }

    /**
     * 解析乘法操作符
     *
     * @param tokenReader token 流
     * @return ASTNode
     */
    private ASTNode parseMultiOperator(TokenReader tokenReader) {
        Token token = tokenReader.peek();
        String content = token.getContent();
        Tokens type = token.getType();
        boolean opTypeFlag = Tokens.OPERATOR.equals(type);
        boolean multiFlag = "*".equals(content);
        boolean divFlag = "/".equals(content);
        if (!opTypeFlag || (!multiFlag && !divFlag)) {
            return null;
        }
        tokenReader.next();
        return new DefaultASTNode(multiFlag ? ASTType.MULTI : ASTType.DIV, content);
    }
}
