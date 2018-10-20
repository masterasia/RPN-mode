package com.robert.util;

/**
 * @author Robert.XU <xutao@bjnja.com>
 * @version RPN-mode, 2018/10/21 0021
 */
public class StringUtil {

    /**
     * 使用java正则表达式去掉多余的.与0
     *
     * @param number String格式的数字
     * @return 调整后的数据
     */
    public static String subZeroAndDot(String number) {
        if (number.indexOf(".") > 0) {
            number = number.replaceAll("0+?$", "");
            number = number.replaceAll("[.]$", "");
        }
        return number;
    }
}
