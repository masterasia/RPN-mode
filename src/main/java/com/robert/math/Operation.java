package com.robert.math;

import java.math.BigDecimal;

import static com.robert.util.Constant.OPERATOR_PRECISION;
import static com.robert.util.Constant.ROUNDING_MODE;

/**
 * @author Robert.XU <xutao@bjnja.com>
 * @version RPN-mode, 2018/10/18 0018
 */
public class Operation {
    /**
     * 不开放实例化
     */
    private Operation() {

    }

    /**
     * 加法运算
     *
     * @param first  被加数
     * @param second 加数
     * @return 和
     */
    public static BigDecimal add(BigDecimal first, BigDecimal second) {
        return first.add(second);
    }

    /**
     * 减法运算
     *
     * @param first  被减数
     * @param second 减数
     * @return 差
     */
    public static BigDecimal reduce(BigDecimal first, BigDecimal second) {
        return first.subtract(second);
    }

    /**
     * 乘法运算
     *
     * @param first  被乘数
     * @param second 乘数
     * @return 积
     */
    public static BigDecimal multiply(BigDecimal first, BigDecimal second) {
        return first.multiply(second);
    }

    /**
     * 除法运算
     *
     * @param first  被除数
     * @param second 除数
     * @return 商
     */
    public static BigDecimal divide(BigDecimal first, BigDecimal second) {
        try {
            return first.divide(second);
        } catch (ArithmeticException e) {
            return first.divide(second, OPERATOR_PRECISION, ROUNDING_MODE);
        }
    }

    /**
     * 开方运算
     *
     * @param bigDecimal 被开方数
     * @return 正根
     */
    public static BigDecimal sqrt(BigDecimal bigDecimal) {
        return BigDecimal.valueOf(Math.sqrt(bigDecimal.doubleValue())).setScale(OPERATOR_PRECISION, ROUNDING_MODE);
    }

}
