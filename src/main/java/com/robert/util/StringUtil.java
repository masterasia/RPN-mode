package com.robert.util;

import java.util.regex.Pattern;

/**
 * @author Robert.XU <xutao@bjnja.com>
 * @version RPN-mode, 2018/10/21 0021
 */
public class StringUtil {

    /**
     * 判断小数的正则
     */
    private static Pattern NUMBER_PATTERN = Pattern.compile("^-?[0-9]+\\.[0-9]+$") ;

    /**
     * 使用java正则表达式去掉小数多余的0和小数点
     *
     * @param number String格式的数字
     * @return 调整后的数据
     */
    public static String subZeroAndDot(String number) {
        if (NUMBER_PATTERN.matcher(number).matches()) {
            number = number.replaceAll("0+?$", "");
            number = number.replaceAll("[.]$", "");
        }
        return number;
    }
}
