package com.bluslee.panda.syntactic.analysis;

import com.bluslee.panda.PandaScriptApplicationTests;
import com.bluslee.panda.lexical.analysis.Lexer;
import com.bluslee.panda.lexical.analysis.TokenReader;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

@Slf4j
public class ExecuteTest extends PandaScriptApplicationTests {

    @Autowired
    private Execute<Integer> execute;

    @Autowired
    @Qualifier("simpleCalcParser")
    private Parser parser;

    @Autowired
    Lexer lexer;

    @Test
    public void executeTest() {
        String s1 = "( 2 + 1  + ( 2 + 1 ) )";
        TokenReader tokenReader = lexer.parse(s1);
        ASTNode astNode = parser.parse(tokenReader);
        Integer execute = this.execute.execute(astNode);
        log.info("res:{}", execute);
    }

}
