package com.bluslee.panda.syntactic.analysis;

import com.bluslee.panda.PandaScriptApplicationTests;
import com.bluslee.panda.lexical.analysis.Lexer;
import com.bluslee.panda.lexical.analysis.TokenReader;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * CalcParserTest
 * @author chejinxuan
 */
public class CalcParserTest extends PandaScriptApplicationTests {

    @Autowired
    Parser parser;

    @Autowired
    Lexer lexer;

    @Test
    public void test() {
        TokenReader tokenReader = lexer.parse("1");
        ASTNode astNode = parser.parse(tokenReader);
        System.out.println(astNode.toString());
    }

}
