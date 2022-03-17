package com.bluslee.panda;

import com.bluslee.panda.lexical.analysis.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

/**
 * @author jinxuan.che
 */
@SpringBootApplication
@Slf4j
@RequiredArgsConstructor
public class PandaScriptApplication implements ApplicationRunner {

    private final Lexer lexer;

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
        lexer.parse(expressionOpt.get());
    }

}
