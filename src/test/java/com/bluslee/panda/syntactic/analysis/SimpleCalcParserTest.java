package com.bluslee.panda.syntactic.analysis;

import com.bluslee.panda.PandaScriptApplicationTests;
import com.bluslee.panda.lexical.analysis.Lexer;
import com.bluslee.panda.lexical.analysis.TokenReader;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class SimpleCalcParserTest extends PandaScriptApplicationTests {

    @Autowired
    @Qualifier("simpleCalcParser")
    private Parser parser;

    @Autowired
    Lexer lexer;

    @Test
    public void parseTest() {
        TokenReader tokenReader = lexer.parse("( 1 + 2 ) * 2 - 1 / ( 66 * ( 1 - 2 ) )");
        parser.parse(tokenReader);
    }

}
