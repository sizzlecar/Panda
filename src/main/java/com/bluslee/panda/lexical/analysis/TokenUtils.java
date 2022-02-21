package com.bluslee.panda.lexical.analysis;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

/**
 * @author jinxuan.che
 */
public class TokenUtils {

    private final static char CHAR_A = 'a';

    private final static char CHAR_Z = 'z';

    private final static char CHAR_A_UPPER = 'A';

    private final static char CHAR_Z_UPPER = 'Z';

    public final static List<Function<Character, BaseTokens>> FUNCTION_LIST = new LinkedList<>();

    static {
        //大写字母
        Function<Character, BaseTokens> letterUpper = character -> character >= CHAR_A_UPPER && character <= CHAR_Z_UPPER ? BaseTokens.LETTER_UPPER : null;
        FUNCTION_LIST.add(letterUpper);

        //小写字母
        Function<Character, BaseTokens> letterLower = character -> character >= CHAR_A && character <= CHAR_Z ? BaseTokens.LETTER_LOWER : null;
        FUNCTION_LIST.add(letterLower);

        //数字
        Function<Character, BaseTokens> number = character -> {
            try {
                Integer.parseInt(character.toString());
            } catch (NumberFormatException e) {
                return null;
            }
            return BaseTokens.NUMBER;
        };
        FUNCTION_LIST.add(number);

        //等于
        Function<Character, BaseTokens> equals = character -> character.equals('=') ? BaseTokens.EQUALS : null;
        FUNCTION_LIST.add(equals);

        //加
        Function<Character, BaseTokens> add = character -> character.equals('+') ? BaseTokens.ADD : null;
        FUNCTION_LIST.add(add);

        //减
        Function<Character, BaseTokens> sub = character -> character.equals('-') ? BaseTokens.SUB : null;
        FUNCTION_LIST.add(sub);

        //乘
        Function<Character, BaseTokens> multi = character -> character.equals('*') ? BaseTokens.MULTI : null;
        FUNCTION_LIST.add(multi);

        //除
        Function<Character, BaseTokens> div = character -> character.equals('/') ? BaseTokens.DIV : null;
        FUNCTION_LIST.add(div);

        //大于
        Function<Character, BaseTokens> more = character -> character.equals('>') ? BaseTokens.MORE : null;
        FUNCTION_LIST.add(more);

        //小于
        Function<Character, BaseTokens> less = character -> character.equals('<') ? BaseTokens.LESS : null;
        FUNCTION_LIST.add(less);

        //点
        Function<Character, BaseTokens> point = character -> character.equals('.') ? BaseTokens.POINT : null;
        FUNCTION_LIST.add(point);

        //空格
        Function<Character, BaseTokens> space = character -> character.equals(' ') ? BaseTokens.SPACE : null;
        FUNCTION_LIST.add(space);

    }

    /**
     * 获取字符对应的base token
     * @param ch 单个字符
     * @return 对应的单个字符，没有则返回空
     */
    public static BaseTokens tokenBaseType(char ch) {
        for (Function<Character, BaseTokens> fun : FUNCTION_LIST) {
            BaseTokens baseToken = fun.apply(ch);
            if (baseToken != null) {
                return baseToken;
            }
        }
        return null;
    }

}
