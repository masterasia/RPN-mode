package com.robert.util;

import java.math.BigDecimal;

/**
 * @author Robert.XU <xutao@bjnja.com>
 * @version RPN-mode, 2018/10/20 0020
 */
public interface Constant {
    /**
     * 异常标记语句
     */
    String ERROR_MESSAGE = "operator %s (position: %d): insufficient parameters";

    /**
     * 小数点标记
     */
    char POINT = '.';

    /**
     * 运算符号
     */
    char ADD = '+';
    char REDUCE = '-';
    char MULTIPLY = '*';
    char DIVIDE = '/';

    /**
     * 数字
     */
    char ZERO = '0';
    char ONE = '1';
    char TWO = '2';
    char THREE = '3';
    char FOUR = '4';
    char FIVE = '5';
    char SIX = '6';
    char SEVEN = '7';
    char EIGHT = '8';
    char NINE = '9';
    /**
     * 数字
     */
    int ZERO_NUMBER = 0;
    int ONE_NUMBER = 1;
    int TWO_NUMBER = 2;
    int THREE_NUMBER = 3;
    int FOUR_NUMBER = 4;
    int FIVE_NUMBER = 5;
    int SIX_NUMBER = 6;
    int SEVEN_NUMBER = 7;
    int EIGHT_NUMBER = 8;
    int NINE_NUMBER = 9;
    int TEN = 10;
    int ELEVEN = 11;
    int TWELVE = 12;
    int THIRTEEN = 13;
    int FOURTEEN = 14;
    int FIFTEEN = 15;

    /**
     * 空格
     */
    char SPACE = ' ';

    /**
     * 清除命令
     */
    char CLEAR_START = 'c';
    String CLEAR = "clear";

    /**
     * 开方命令
     */
    char SQRT_START = 's';
    String SQRT = "sqrt";

    /**
     * 回滚命令
     */
    char UNDO_START = 'u';
    String UNDO = "undo";
    /**
     * 四则运算所需实数个数
     */
    int OPERATION = 2;

    /**
     * 显示精度
     */
    int SHOW_PRECISION = TEN;

    /**
     * 计算精度
     */
    int OPERATOR_PRECISION = FIFTEEN;

    /**
     * 进位方式
     */
    int ROUNDING_MODE = BigDecimal.ROUND_DOWN;
}
