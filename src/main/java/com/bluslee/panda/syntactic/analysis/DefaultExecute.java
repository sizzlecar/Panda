package com.bluslee.panda.syntactic.analysis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;

/**
 *
 * @author chejinxuan
 */
@Slf4j
@Component
public class DefaultExecute implements Execute<Integer> {

    @Override
    public Integer execute(ASTNode astNode) {
        if (astNode == null || CollectionUtils.isEmpty(astNode.getChildList())) {
            return null;
        }
        return executeAdditive(astNode.getChildList().get(0));
    }

    private Integer executeAdditive(ASTNode astNode) {
        if (astNode == null) {
            return null;
        }
        List<ASTNode> childList = astNode.getChildList();
        if (CollectionUtils.isEmpty(childList)) {
            return null;
        } else if (childList.size() == 1) {
            //只有乘法表达式
            return executeMultiplicative(childList.get(0));
        } else if (childList.size() == 3) {
            // multiplicative (ADD|SUB) additive
            Integer leftRes = Optional.ofNullable(executeMultiplicative(childList.get(0)))
                    .orElse(0);
            ASTNode opNode = childList.get(1);
            boolean plusFlag = opNode.getASTType() == ASTType.ADD;
            Integer rightRes = Optional.ofNullable(executeAdditive(childList.get(2)))
                    .orElse(0);
            return plusFlag ? leftRes + rightRes : leftRes - rightRes;
        } else {
            throw new RuntimeException("execute error");
        }
    }

    private Integer executeMultiplicative(ASTNode astNode) {
        if (astNode == null || CollectionUtils.isEmpty(astNode.getChildList())) {
            return null;
        }
        List<ASTNode> childList = astNode.getChildList();
        if (childList.size() == 1) {
            //单个primary
            return executePrimary(childList.get(0));
        } else if (childList.size() == 3) {
            // multiplicative (ADD|SUB) additive
            Integer leftRes = Optional.ofNullable(executePrimary(childList.get(0)))
                    .orElse(0);
            ASTNode opNode = childList.get(1);
            boolean multiFlag = opNode.getASTType() == ASTType.MULTI;
            Integer rightRes = Optional.ofNullable(executeMultiplicative(childList.get(2)))
                    .orElse(0);
            return multiFlag ? leftRes * rightRes : leftRes / rightRes;
        } else {
            throw new RuntimeException("execute error");
        }
    }

    private Integer executePrimary(ASTNode astNode) {
        if (astNode == null || CollectionUtils.isEmpty(astNode.getChildList())) {
            return null;
        }
        List<ASTNode> childList = astNode.getChildList();
        if (childList.size() == 1) {
            //INT
            return Integer.parseInt(childList.get(0).getContent());
        } else if (childList.size() == 3) {
            // LEFT additive RIGHT
            return executeAdditive(childList.get(1));
        } else {
            throw new RuntimeException("execute error");
        }
    }
}
