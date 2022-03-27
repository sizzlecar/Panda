package com.bluslee.panda.syntactic.analysis;

import com.bluslee.panda.PandaScriptApplicationTests;
import com.bluslee.panda.lexical.analysis.Lexer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * CalcParserTest
 * @author chejinxuan
 */
public class CalcParserTest extends PandaScriptApplicationTests {

    @Autowired
    @Qualifier("calcParser")
    Parser parser;

    @Autowired
    Lexer lexer;

    @Test
    public void parseTest() {
    }

}
