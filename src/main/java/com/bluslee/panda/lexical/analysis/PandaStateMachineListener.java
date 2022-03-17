package com.bluslee.panda.lexical.analysis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.statemachine.ExtendedState;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;

import java.util.List;

/**
 * @author jinxuan.che
 */
@Slf4j
public class PandaStateMachineListener extends StateMachineListenerAdapter<States, Events> {

    @Override
    public void stateMachineStopped(StateMachine<States, Events> stateMachine) {
        log.info("stateMachineStopped is running");
        //状态机关闭之后，如果还有未处理完的token，就把正在解析的token放入token列表中
        ExtendedState extendedState = stateMachine.getExtendedState();
        String code = extendedState.get(Constants.StateMachineConstants.EXPRESSION_KEY, String.class);
        Integer index = extendedState.get(Constants.StateMachineConstants.EXPRESSION_INDEX_KEY, Integer.class);
        List<Token> tokenList = extendedState.get(Constants.StateMachineConstants.TOKENS_KEY, List.class);
        States currentStates = stateMachine.getState().getId();
        Integer tokenIndex = extendedState.get(Constants.StateMachineConstants.TOKEN_INDEX_KEY, Integer.class);
        if(tokenIndex <= index) {
            String tokenContext;
            if (index == code.length() - 1) {
                tokenContext = code.substring(tokenIndex);
            } else {
                tokenContext = code.substring(tokenIndex, index);
            }
            DefaultToken defaultToken = new DefaultToken(currentStates.getTokens(), tokenContext);
            tokenList.add(defaultToken);
        }
        extendedState.getVariables().put(Constants.StateMachineConstants.TOKEN_READER, new DefaultTokenReader(tokenList));
    }
}
