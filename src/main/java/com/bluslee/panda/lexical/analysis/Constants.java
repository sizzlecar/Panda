package com.bluslee.panda.lexical.analysis;

/**
 * 词法分析相关常量
 * @author jinxuan.che
 */
public interface Constants {

    interface StateMachineConstants {

        //表达式属性名
        String EXPRESSION_KEY = "expression";

        //表达式索引属性名
        String EXPRESSION_INDEX_KEY = "expression_index";

        //token索引属性名
        String TOKEN_INDEX_KEY = "token_index";

        //解析表达式得到的token
        String TOKENS_KEY = "tokens";

        //expression_index 对应字符对应的token
        String BASE_TOKEN_KEY = "base_token";
    }

}
