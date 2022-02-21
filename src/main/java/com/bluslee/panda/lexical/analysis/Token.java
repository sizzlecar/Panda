package com.bluslee.panda.lexical.analysis;

import lombok.Data;

/**
 * @author jinxuan.che
 */
@Data
public class Token {

    /**
     * token 类型
     */
    private Tokens type;

    /**
     * from index
     */
    private Integer fromIndex;

    /**
     * to index
     */
    private Integer toIndex;

    /**
     * 内容
     */
    private String content;
}
