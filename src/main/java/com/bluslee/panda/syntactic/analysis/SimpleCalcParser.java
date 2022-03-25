package com.bluslee.panda.syntactic.analysis;

import com.bluslee.panda.lexical.analysis.Token;
import com.bluslee.panda.lexical.analysis.TokenReader;
import com.bluslee.panda.lexical.analysis.Tokens;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * grammar Calc ;
 * programmer: (additive NEWLINE)* ;
 * additive: multiplicative | multiplicative (ADD|SUB) additive ;
 * multiplicative: INT | INT (MULTI|DIV) multiplicative ;
 * NEWLINE : [\r\n]+ ;
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
public class SimpleCalcParser implements Parser {

    @Override
    public ASTNode parse(TokenReader tokenReader) {
        if (tokenReader == null || tokenReader.peek() == null) {
            throw new RuntimeException("token reader not allow null");
        }
        //根节点
        ASTNode root = new DefaultASTNode(null, ASTType.PROGRAMMER);
        while (tokenReader.peek() != null) {
            ASTNode astNode = parseAdditiveExpress(tokenReader, root);
            root.addChild(astNode);
        }
        dumpAST(root, "");
        return root;
    }

    /**
     * 解析加法表达式
     * @param tokenReader token 流
     * @param root 根节点
     * @return 解析到的ASTNode
     */
    private ASTNode parseAdditiveExpress(TokenReader tokenReader, ASTNode root) {
        ASTNode addExpressNode = new DefaultASTNode(root, ASTType.ADDITIVE);
        ASTNode multiplicativeExpress = parseMultiplicativeExpress(tokenReader, addExpressNode);
        if (multiplicativeExpress != null) {
            Token token = tokenReader.peek();
            if (token != null
                    && Tokens.OPERATOR.equals(token.getType())
                    && ("+".equals(token.getContent()) || "-".equals(token.getContent()))) {
                boolean plusFlag = "+".equals(token.getContent());
                tokenReader.next();
                ASTNode opNode = new DefaultASTNode(root, plusFlag ? ASTType.ADD : ASTType.SUB);
                ASTNode rightNode = parseAdditiveExpress(tokenReader, addExpressNode);
                if (rightNode == null) {
                    throw new RuntimeException("right node is not null");
                }
                addExpressNode.setChildList(Arrays.asList(multiplicativeExpress, opNode, rightNode));
            } else {
                addExpressNode.addChild(multiplicativeExpress);
            }
        } else {
            return null;
        }
        return addExpressNode;
    }

    /**
     * 解析乘法表达式，包括除法
     * @param tokenReader token 流
     * @param root 根节点
     * @return ASTNode
     */
    private ASTNode parseMultiplicativeExpress(TokenReader tokenReader, ASTNode root) {
        ASTNode multiplicativeExpressNode = new DefaultASTNode(root, ASTType.MULTIPLICATIVE);
        ASTNode astNode = parseInt(tokenReader);
        if (astNode != null) {
            Token token = tokenReader.peek();
            if (token != null
                    && Tokens.OPERATOR.equals(token.getType())
                    && ("*".equals(token.getContent()) || "/".equals(token.getContent()))) {
                boolean multiFlag = "*".equals(token.getContent());
                tokenReader.next();
                ASTNode opNode = new DefaultASTNode(root, multiFlag ? ASTType.MULTI : ASTType.DIV);
                ASTNode rightNode = parseMultiplicativeExpress(tokenReader, multiplicativeExpressNode);
                if (rightNode == null) {
                    throw new RuntimeException("right node is not null");
                }
                multiplicativeExpressNode.setChildList(Arrays.asList(astNode, opNode, rightNode));
            } else {
                multiplicativeExpressNode.addChild(astNode);
            }
        } else {
            return null;
        }
        return multiplicativeExpressNode;
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
     * 打印输出AST的树状结构
     * @param node ast
     * @param indent 缩进字符，由tab组成，每一级多一个tab
     */
    private void dumpAST(ASTNode node, String indent) {
        log.debug("{}{} {}",indent, node.getASTType(), node.getContent());
        if (node.getChildList() == null) {
            return;
        }
        for (ASTNode child : node.getChildList()) {
            dumpAST(child, indent + "\t");
        }
    }
}
