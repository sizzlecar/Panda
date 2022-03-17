package com.bluslee.panda.lexical.analysis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.statemachine.ExtendedState;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Component;

/**
 * 默认词法分析器
 * @author chejinxuan
 */
@RequiredArgsConstructor
@Component
@Slf4j
public class DefaultLexer implements Lexer {

    private final StateMachine<States, Events> statesEventsStateMachine;

    @Override
    public TokenReader parse(String script) {
        log.info("parse is running");
        //启动状态机
        statesEventsStateMachine.start();
        //注册状态机关闭监听
        statesEventsStateMachine.addStateListener(new PandaStateMachineListener());
        ExtendedState extendedState = statesEventsStateMachine.getExtendedState();
        extendedState.getVariables().put(Constants.StateMachineConstants.EXPRESSION_KEY, script);
        extendedState.getVariables().put(Constants.StateMachineConstants.TOKEN_INDEX_KEY, 0);
        for (int i = 0; i < script.length(); i++) {
            extendedState.getVariables().put(Constants.StateMachineConstants.EXPRESSION_INDEX_KEY, i);
            String charStr = Character.toString(script.charAt(i));
            String currentStateName = statesEventsStateMachine.getState().getId().name();
            CharType charType = TokenUtils.charType(script.charAt(i));
            extendedState.getVariables().put(Constants.StateMachineConstants.BASE_TOKEN_KEY, charType);
            statesEventsStateMachine.sendEvent(Events.INPUT);
            String nextStateName = statesEventsStateMachine.getState().getId().name();
            log.info("Current state:{}, Send event char: {}, Next state: {}",
                    currentStateName,
                    charStr,
                    nextStateName);
        }
        //关闭状态机
        statesEventsStateMachine.stop();
        TokenReader tokenReader = extendedState.get(Constants.StateMachineConstants.TOKEN_READER, TokenReader.class);
        Token token = tokenReader.next();
        while (token != null) {
            log.info("token name :{}, token content :{}", token.getType(), token.getContent());
            token = tokenReader.next();
        }
        return tokenReader;
    }
}
