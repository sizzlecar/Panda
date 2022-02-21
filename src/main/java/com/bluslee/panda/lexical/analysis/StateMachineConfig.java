package com.bluslee.panda.lexical.analysis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.ExtendedState;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.guard.Guard;
import org.springframework.statemachine.state.State;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;

/**
 * @author jinxuan.che
 */
@Configuration
@EnableStateMachine
@Slf4j
public class StateMachineConfig extends EnumStateMachineConfigurerAdapter<States, Events> {

    @Override
    public void configure(StateMachineStateConfigurer<States, Events> states) throws Exception {
        states.withStates()
                .initial(States.INITIAL)
                .states(EnumSet.allOf(States.class));
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<States, Events> transitions)
            throws Exception {
        transitions
                .withExternal()
                .source(States.INITIAL)
                .target(States.ID)
                .guard(toIdGuard())
                .event(Events.INPUT)
                .action(init2OtherAction())
                .and()
                .withExternal()
                .source(States.INITIAL)
                .target(States.NUMBER)
                .guard(toNumberGuard())
                .event(Events.INPUT)
                .action(init2OtherAction())
                .and()
                .withExternal()
                .source(States.INITIAL)
                .target(States.OPERATOR)
                .guard(toOperatorGuard())
                .event(Events.INPUT)
                .action(init2OtherAction())
                .and()
                .withExternal()
                .source(States.ID)
                .target(States.NUMBER)
                .guard(toNumberGuard())
                .event(Events.INPUT)
                .action(tokenAction())
                .and().withExternal()
                .source(States.ID)
                .target(States.OPERATOR)
                .guard(toOperatorGuard())
                .action(tokenAction())
                .event(Events.INPUT)
                .and().withExternal()
                .source(States.NUMBER)
                .target(States.OPERATOR)
                .guard(toOperatorGuard())
                .action(tokenAction())
                .event(Events.INPUT)
                .and().withExternal()
                .source(States.NUMBER)
                .target(States.ID)
                .guard(toIdGuard())
                .action(tokenAction())
                .event(Events.INPUT).and().withExternal()
                .source(States.OPERATOR)
                .target(States.ID)
                .guard(toIdGuard())
                .action(tokenAction())
                .event(Events.INPUT).and().withExternal()
                .source(States.OPERATOR)
                .target(States.NUMBER)
                .guard(toNumberGuard())
                .action(tokenAction())
                .event(Events.INPUT).and().withExternal()
                .source(States.ID)
                .target(States.INITIAL)
                .guard(toInitGuard())
                .action(tokenAction()).and().withExternal()
                .source(States.OPERATOR)
                .target(States.INITIAL)
                .guard(toInitGuard())
                .action(tokenAction()).and().withExternal()
                .source(States.NUMBER)
                .target(States.INITIAL)
                .guard(toInitGuard())
                .action(tokenAction());
    }

    @Bean
    public Action<States, Events> init2OtherAction() {
        return context -> {
            ExtendedState extendedState = context.getExtendedState();
            Integer index = extendedState.get(Constants.StateMachineConstants.EXPRESSION_INDEX_KEY, Integer.class);
            Integer tokenIndex = extendedState.get(Constants.StateMachineConstants.TOKEN_INDEX_KEY, Integer.class);
            extendedState.getVariables().put(Constants.StateMachineConstants.TOKEN_INDEX_KEY, index);
            log.info("init2OtherAction: {} from {} to {}",
                    Constants.StateMachineConstants.TOKEN_INDEX_KEY,
                    tokenIndex,
                    index);
        };
    }

    @Bean
    public Action<States, Events> tokenAction() {
        return context -> {
            ExtendedState extendedState = context.getExtendedState();
            String code = extendedState.get(Constants.StateMachineConstants.EXPRESSION_KEY, String.class);
            Integer index = extendedState.get(Constants.StateMachineConstants.EXPRESSION_INDEX_KEY, Integer.class);
            List<String> tokenList = extendedState.get(Constants.StateMachineConstants.TOKENS_KEY, List.class);
            if (tokenList == null) {
                tokenList = new LinkedList();
                extendedState.getVariables().put(Constants.StateMachineConstants.TOKENS_KEY, tokenList);
            }
            Integer tokenIndex = extendedState.get(Constants.StateMachineConstants.TOKEN_INDEX_KEY, Integer.class);
            String token = code.substring(tokenIndex, index);
            tokenList.add(token);
            log.info("tokenAction {} add element {}", Constants.StateMachineConstants.TOKENS_KEY, token);
            extendedState.getVariables().put(Constants.StateMachineConstants.TOKEN_INDEX_KEY, index);
            log.info("tokenAction {} from {} to {}", Constants.StateMachineConstants.TOKEN_INDEX_KEY, tokenIndex, index);
        };
    }

    @Bean
    public Guard<States, Events> toNumberGuard() {
        return context -> {
            ExtendedState extendedState = context.getExtendedState();
            String code = extendedState.get(Constants.StateMachineConstants.EXPRESSION_KEY, String.class);
            Integer index = extendedState.get(Constants.StateMachineConstants.EXPRESSION_INDEX_KEY, Integer.class);
            char charAt = code.charAt(index);
            BaseTokens baseTokens = TokenUtils.tokenBaseType(charAt);
            if (BaseTokens.NUMBER != baseTokens) {
                log.error("toNumberGuard：下一个字符不是数字，不能进入数字状态");
                return false;
            }
            //当前状态
            State<States, Events> currentState = context.getStateMachine().getState();
            States currentStateId = currentState.getId();
            if (States.NUMBER == currentStateId || States.INITIAL == currentStateId || States.OPERATOR == currentStateId) {
                return true;
            } else {
                log.info("当前状态不属于 数字，初始化 无法进入数字状态");
                return false;
            }
        };
    }

    @Bean
    public Guard<States, Events> toOperatorGuard() {
        List<BaseTokens> operatorList = Arrays.asList(BaseTokens.EQUALS, BaseTokens.ADD, BaseTokens.SUB, BaseTokens.MULTI, BaseTokens.DIV, BaseTokens.MORE, BaseTokens.LESS, BaseTokens.POINT);
        return context -> {
            ExtendedState extendedState = context.getExtendedState();
            String code = extendedState.get(Constants.StateMachineConstants.EXPRESSION_KEY, String.class);
            Integer index = extendedState.get(Constants.StateMachineConstants.EXPRESSION_INDEX_KEY, Integer.class);
            BaseTokens baseTokens = TokenUtils.tokenBaseType(code.charAt(index));
            if (!operatorList.contains(baseTokens)) {
                log.error("toOperatorGuard下一个字符不是操作符不能进入操作符状态");
                return false;
            }
            log.info("state machine 进入操作符号状态");
            return true;
        };
    }

    @Bean
    public Guard<States, Events> toIdGuard() {
        return context -> {
            ExtendedState extendedState = context.getExtendedState();
            String code = extendedState.get(Constants.StateMachineConstants.EXPRESSION_KEY, String.class);
            Integer index = extendedState.get(Constants.StateMachineConstants.EXPRESSION_INDEX_KEY, Integer.class);
            BaseTokens baseTokens = TokenUtils.tokenBaseType(code.charAt(index));
            List<BaseTokens> idTokens = Arrays.asList(BaseTokens.LETTER_LOWER, BaseTokens.LETTER_UPPER, BaseTokens.NUMBER);
            if (!idTokens.contains(baseTokens)) {
                log.error("toIdGuard下一个字符不是字母或数字，不能进入ID状态");
                return false;
            }
            State<States, Events> currentState = context.getStateMachine().getState();
            States currentStateId = currentState.getId();
            if (States.ID == currentStateId || States.INITIAL == currentStateId || States.OPERATOR == currentStateId) {
                log.info("state machine 进入ID状态");
                return true;
            } else {
                log.info("当前状态不属于 ID，初始化 无法进入ID状态");
                return false;
            }
        };
    }

    @Bean
    public Guard<States, Events> toInitGuard() {
        return context -> {
            ExtendedState extendedState = context.getExtendedState();
            String code = extendedState.get(Constants.StateMachineConstants.EXPRESSION_KEY, String.class);
            Integer index = extendedState.get(Constants.StateMachineConstants.EXPRESSION_INDEX_KEY, Integer.class);
            BaseTokens baseTokens = TokenUtils.tokenBaseType(code.charAt(index));
            if (baseTokens != null && BaseTokens.SPACE != baseTokens) {
                log.error("当前字符是有效字符不能进入初始状态");
                return false;
            }
            return true;
        };
    }

}
