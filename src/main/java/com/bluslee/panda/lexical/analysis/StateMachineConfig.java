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
            List<Token> tokenList = extendedState.get(Constants.StateMachineConstants.TOKENS_KEY, List.class);
            if (tokenList == null) {
                tokenList = new LinkedList();
                extendedState.getVariables().put(Constants.StateMachineConstants.TOKENS_KEY, tokenList);
            }
            Integer tokenIndex = extendedState.get(Constants.StateMachineConstants.TOKEN_INDEX_KEY, Integer.class);
            String tokenContext = code.substring(tokenIndex, index);
            States currentStates = context.getStateMachine().getState().getId();
            DefaultToken defaultToken = new DefaultToken(currentStates.getTokens(), tokenContext);
            tokenList.add(defaultToken);
            log.info("tokenAction {} add element {}", Constants.StateMachineConstants.TOKENS_KEY, defaultToken);
            extendedState.getVariables().put(Constants.StateMachineConstants.TOKEN_INDEX_KEY, index);
            log.info("tokenAction {} from {} to {}", Constants.StateMachineConstants.TOKEN_INDEX_KEY, tokenIndex, index);
        };
    }

    @Bean
    public Guard<States, Events> toNumberGuard() {
        return context -> {
            ExtendedState extendedState = context.getExtendedState();
            CharType charType = extendedState.get(Constants.StateMachineConstants.BASE_TOKEN_KEY, CharType.class);
            if (CharType.NUMBER != charType) {
                log.info("toNumberGuard：下一个字符不是数字，不能进入数字状态");
                return false;
            }
            //当前状态
            State<States, Events> currentState = context.getStateMachine().getState();
            States currentStateId = currentState.getId();
            if (States.NUMBER == currentStateId || States.INITIAL == currentStateId || States.OPERATOR == currentStateId) {
                log.info("state machine 进入数字状态");
                return true;
            } else {
                log.info("当前状态不属于数字,初始化,操作符 无法进入数字状态");
                return false;
            }
        };
    }

    //需要将不同的操作符拆成不同的状态，否则不同的操作符会粘在一起 todo
    @Bean
    public Guard<States, Events> toOperatorGuard() {
        List<CharType> operatorList = Arrays.asList(CharType.EQUALS, CharType.ADD,
                CharType.SUB, CharType.MULTI, CharType.DIV,
                CharType.MORE, CharType.LESS, CharType.POINT,
                CharType.LEFT_BRACKETS, CharType.RIGHT_BRACKETS);
        return context -> {
            ExtendedState extendedState = context.getExtendedState();
            CharType charType = extendedState.get(Constants.StateMachineConstants.BASE_TOKEN_KEY, CharType.class);
            if (!operatorList.contains(charType)) {
                log.info("toOperatorGuard下一个字符不是操作符不能进入操作符状态");
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
            CharType charType = extendedState.get(Constants.StateMachineConstants.BASE_TOKEN_KEY, CharType.class);
            List<CharType> idTokens = Arrays.asList(CharType.LETTER_LOWER, CharType.LETTER_UPPER, CharType.NUMBER);
            if (!idTokens.contains(charType)) {
                log.info("toIdGuard下一个字符不是字母或数字，不能进入ID状态");
                return false;
            }
            State<States, Events> currentState = context.getStateMachine().getState();
            States currentStateId = currentState.getId();
            if (States.ID == currentStateId || (States.INITIAL == currentStateId && !charType.equals(CharType.NUMBER))
                    || (States.OPERATOR == currentStateId && !charType.equals(CharType.NUMBER))) {
                log.info("state machine 进入ID状态");
                return true;
            } else {
                log.info("当前状态不属于 ID, 初始化, 操作符 无法进入ID状态");
                return false;
            }
        };
    }

    @Bean
    public Guard<States, Events> toInitGuard() {
        return context -> {
            ExtendedState extendedState = context.getExtendedState();
            CharType charType = extendedState.get(Constants.StateMachineConstants.BASE_TOKEN_KEY, CharType.class);
            if (charType != null && CharType.SPACE != charType) {
                log.error("当前字符是有效字符不能进入初始状态");
                return false;
            }
            log.info("state machine 进入初始化状态");
            return true;
        };
    }

}
