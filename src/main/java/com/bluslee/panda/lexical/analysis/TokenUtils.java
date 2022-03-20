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

    public final static List<Function<Character, CharType>> FUNCTION_LIST = new LinkedList<>();

    static {
        //大写字母
        Function<Character, CharType> letterUpper = character -> character >= CHAR_A_UPPER && character <= CHAR_Z_UPPER ? CharType.LETTER_UPPER : null;
        FUNCTION_LIST.add(letterUpper);

        //小写字母
        Function<Character, CharType> letterLower = character -> character >= CHAR_A && character <= CHAR_Z ? CharType.LETTER_LOWER : null;
        FUNCTION_LIST.add(letterLower);

        //数字
        Function<Character, CharType> number = character -> {
            try {
                Integer.parseInt(character.toString());
            } catch (NumberFormatException e) {
                return null;
            }
            return CharType.NUMBER;
        };
        FUNCTION_LIST.add(number);

        //等于
        Function<Character, CharType> equals = character -> character.equals('=') ? CharType.EQUALS : null;
        FUNCTION_LIST.add(equals);

        //加
        Function<Character, CharType> add = character -> character.equals('+') ? CharType.ADD : null;
        FUNCTION_LIST.add(add);

        //减
        Function<Character, CharType> sub = character -> character.equals('-') ? CharType.SUB : null;
        FUNCTION_LIST.add(sub);

        //乘
        Function<Character, CharType> multi = character -> character.equals('*') ? CharType.MULTI : null;
        FUNCTION_LIST.add(multi);

        //除
        Function<Character, CharType> div = character -> character.equals('/') ? CharType.DIV : null;
        FUNCTION_LIST.add(div);

        //大于
        Function<Character, CharType> more = character -> character.equals('>') ? CharType.MORE : null;
        FUNCTION_LIST.add(more);

        //小于
        Function<Character, CharType> less = character -> character.equals('<') ? CharType.LESS : null;
        FUNCTION_LIST.add(less);

        //点
        Function<Character, CharType> point = character -> character.equals('.') ? CharType.POINT : null;
        FUNCTION_LIST.add(point);

        //空格
        Function<Character, CharType> space = character -> character.equals(' ') ? CharType.SPACE : null;
        FUNCTION_LIST.add(space);

        //换行
        Function<Character, CharType> newLine = character -> character.equals('\n') ? CharType.NEW_LINE_POSIX : null;
        FUNCTION_LIST.add(newLine);

        //(
        Function<Character, CharType> left = character -> character.equals('(') ? CharType.NEW_LINE_POSIX : null;
        FUNCTION_LIST.add(left);

        //)
        Function<Character, CharType> right = character -> character.equals(')') ? CharType.NEW_LINE_POSIX : null;
        FUNCTION_LIST.add(right);

    }

    /**
     * 获取字符对应的base token
     * @param ch 单个字符
     * @return 对应的单个字符，没有则返回空
     */
    public static CharType charType(char ch) {
        for (Function<Character, CharType> fun : FUNCTION_LIST) {
            CharType baseToken = fun.apply(ch);
            if (baseToken != null) {
                return baseToken;
            }
        }
        return null;
    }

}
