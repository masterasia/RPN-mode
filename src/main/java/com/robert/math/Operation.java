package com.robert.math;

import java.math.BigDecimal;

import static com.robert.Constant.OPERATOR_PRECISION;
import static com.robert.Constant.ROUNDING_MODE;

/**
 * @author Robert.XU <xutao@bjnja.com>
 * @version RPN-mode, 2018/10/18 0018
 */
public class Operation {

    public static BigDecimal add(BigDecimal first, BigDecimal second) {
        return first.add(second);
    }

    public static BigDecimal reduce(BigDecimal first, BigDecimal second) {
        return first.subtract(second);
    }

    public static BigDecimal multiply(BigDecimal first, BigDecimal second) {
        return first.multiply(second);
    }

    public static BigDecimal divide(BigDecimal first, BigDecimal second) {
        try {
            return first.divide(second);
        } catch (ArithmeticException e) {
            return first.divide(second, OPERATOR_PRECISION, ROUNDING_MODE);
        }
    }

    public static BigDecimal sqrt(BigDecimal bigDecimal) {
        return bigDecimal;
    }
}
