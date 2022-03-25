package com.bluslee.panda.lexical.analysis;

import com.bluslee.panda.PandaScriptApplicationTests;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class LexerTest extends PandaScriptApplicationTests {

    @Autowired
    private Lexer lexer;

    @Test
    public void parseTest() {
        TokenReader tokenReader = lexer.parse("1");
        while (tokenReader.peek() != null) {
            Token token = tokenReader.next();
            log.debug("type:{}, content: {}", token.getType(), token.getContent());
        }
    }
}
