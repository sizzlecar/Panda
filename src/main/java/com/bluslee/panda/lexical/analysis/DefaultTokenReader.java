package com.bluslee.panda.lexical.analysis;

import java.util.List;

/**
 * DefaultTokenReader
 * @author chejinxuan
 */
public class DefaultTokenReader implements TokenReader {

    private final List<Token> tokenList;

    private int index = -1;

    public DefaultTokenReader(List<Token> tokenList) {
        this.tokenList = tokenList;
    }

    @Override
    public Token next() {
        if (index >= tokenList.size() - 1) {
            return null;
        }
        return tokenList.get(++index);
    }

    @Override
    public Token peek() {
        if (index >= tokenList.size() - 1) {
            return null;
        }
        return tokenList.get(index + 1);
    }

    @Override
    public void previous() {
        if (index <= 0) {
            throw new RuntimeException("index already <= 0");
        }
        index--;
    }

    @Override
    public int getIndex() {
        return index;
    }

    @Override
    public void setIndex(int index) {
        if (index < 0 || index >= tokenList.size()) {
            throw new IndexOutOfBoundsException();
        }
        this.index = index;
    }
}
