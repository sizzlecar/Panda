package com.bluslee.panda.lexical.analysis;

/**
 * @author jinxuan.che
 */
public class DefaultToken implements Token {

    /**
     * token 类型
     */
    private Tokens type;

    /**
     * 内容
     */
    private String content;

    public DefaultToken(Tokens type, String content) {
        this.type = type;
        this.content = content;
    }

    public DefaultToken() {
    }

    @Override
    public Tokens getType() {
        return type;
    }

    public void setType(Tokens type) {
        this.type = type;
    }

    @Override
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
