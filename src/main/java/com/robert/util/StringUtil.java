package com.robert.util;

import static com.robert.util.Constant.POINT;
import static com.robert.util.Constant.SCIENTIFIC_COUNTING;

/**
 * @author Robert.XU <xutao@bjnja.com>
 * @version RPN-mode, 2018/10/21 0021
 */
public class StringUtil {

    /**
     * 使用java正则表达式去掉多余的0
     * 科学计数法无需处理
     *
     * @param number String格式的数字
     * @return 调整后的数据
     */
    public static String subZeroAndDot(String number) {
        if (number.indexOf(POINT) > 0 && !number.contains(SCIENTIFIC_COUNTING)) {
            number = number.replaceAll("0+?$", "");
            number = number.replaceAll("[.]$", "");
        }
        return number;
    }
}
