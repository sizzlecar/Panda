package com.bluslee.panda;

import com.bluslee.panda.lexical.analysis.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.statemachine.ExtendedState;
import org.springframework.statemachine.StateMachine;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

/**
 * @author jinxuan.che
 */
@SpringBootApplication
@Slf4j
public class PandaScriptApplication implements ApplicationRunner {

    @Autowired
    private StateMachine<States, Events> statesEventsStateMachine;

    public static void main(String[] args) {
        SpringApplication.run(PandaScriptApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Stream.of(args.getOptionNames()).flatMap(Set::stream)
                .forEach(name -> {
                    List<String> optionValues = args.getOptionValues(name);
                    log.info("input parameter name:{}, val:{}", name, optionValues.toString());
                });
        Optional<String> expressionOpt = Stream.of(args.getOptionNames())
                .flatMap(Set::stream)
                .filter(Constants.StateMachineConstants.EXPRESSION_KEY::equals)
                .map(args::getOptionValues)
                .map(list -> list.get(0))
                .findFirst();
        if (!expressionOpt.isPresent()) {
            log.info("not found para {}", Constants.StateMachineConstants.EXPRESSION_KEY);
            return;
        }
        //启动状态机
        statesEventsStateMachine.start();
        //注册状态机关闭监听
        statesEventsStateMachine.addStateListener(new PandaStateMachineListener());
        String express = expressionOpt.get();
        ExtendedState extendedState = statesEventsStateMachine.getExtendedState();
        extendedState.getVariables().put(Constants.StateMachineConstants.EXPRESSION_KEY, express);
        extendedState.getVariables().put(Constants.StateMachineConstants.TOKEN_INDEX_KEY, 0);
        for (int i = 0; i < express.length(); i++) {
            extendedState.getVariables().put(Constants.StateMachineConstants.EXPRESSION_INDEX_KEY, i);
            String charStr = Character.toString(express.charAt(i));
            String currentStateName = statesEventsStateMachine.getState().getId().name();
            BaseTokens baseTokens = TokenUtils.tokenBaseType(express.charAt(i));
            extendedState.getVariables().put(Constants.StateMachineConstants.BASE_TOKEN_KEY, baseTokens);
            statesEventsStateMachine.sendEvent(Events.INPUT);
            String nextStateName = statesEventsStateMachine.getState().getId().name();
            log.info("Current state:{}, Send event char: {}, Next state: {}",
                    currentStateName,
                    charStr,
                    nextStateName);
        }
        //关闭状态机
        statesEventsStateMachine.stop();
        List<String> tokenList = extendedState.get(Constants.StateMachineConstants.TOKENS_KEY, List.class);
        log.info("token list:{}", String.join(",", tokenList));
    }

}
