package com.bluslee.panda.lexical.analysis;

/**
 * TokenReader
 *
 * @author chejinxuan
 */
public interface TokenReader {
    /**
     * 返回Token流中下一个Token，并从流中取出。 如果流已经为空，返回null;
     *
     * @return Token流中下一个Token
     */
    Token next();

    /**
     * 返回Token流中下一个Token，但不从流中取出。 如果流已经为空，返回null;
     *
     * @return Token流中下一个Token
     */
    Token peek();

    /**
     * Token流回退一步。恢复原来的Token。
     */
    void previous();

    /**
     * 获取Token流当前的索引
     *
     * @return 当前索引
     */
    int getIndex();

    /**
     * 设置Token流当前的读取索引
     *
     * @param index 索引
     */
    void setIndex(int index);
}