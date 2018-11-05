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
     * 小数点符号
     */
    String POINT = ".";

    /**
     * 数字-字符
     */
    String ZERO = "0";
    String ONE = "1";
    String TWO = "2";
    String THREE = "3";
    String FOUR = "4";
    String FIVE = "5";
    String SIX = "6";
    String SEVEN = "7";
    String EIGHT = "8";
    String NINE = "9";
    /**
     * 数字-数值
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
    int HUNDRED = 100;
    double SQRT_START = 2.0;

    /**
     * 空格
     */
    String SPACE = " ";

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

    /**
     * 数学标记
     */
    String NATURAL_LOGARITHM = "e";

    String SCIENTIFIC_COUNTING = "E";
}
