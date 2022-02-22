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
        ExtendedState extendedState = stateMachine.getExtendedState();
        String code = extendedState.get(Constants.StateMachineConstants.EXPRESSION_KEY, String.class);
        Integer index = extendedState.get(Constants.StateMachineConstants.EXPRESSION_INDEX_KEY, Integer.class);
        List<String> tokenList = extendedState.get(Constants.StateMachineConstants.TOKENS_KEY, List.class);
        Integer tokenIndex = extendedState.get(Constants.StateMachineConstants.TOKEN_INDEX_KEY, Integer.class);
        if(tokenIndex <= index) {
            if (index == code.length() - 1) {
                tokenList.add(code.substring(tokenIndex));
            } else {
                tokenList.add(code.substring(tokenIndex, index));
            }
        }
    }
}
