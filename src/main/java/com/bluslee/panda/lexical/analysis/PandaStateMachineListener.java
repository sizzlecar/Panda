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
        ExtendedState extendedState = stateMachine.getExtendedState();
        String code = extendedState.get("code", String.class);
        Integer index = extendedState.get("index", Integer.class);
        log.info("stateMachineStopped code:{}, index:{}", code, index);
        List<String> tokenList = extendedState.get("tokenList", List.class);
        Integer tokenIndex = extendedState.get("tokenIndex", Integer.class);
        if(tokenIndex <= index) {
            if (index == code.length() - 1) {
                tokenList.add(code.substring(tokenIndex));
            } else {
                tokenList.add(code.substring(tokenIndex, index));
            }
        }
    }
}
