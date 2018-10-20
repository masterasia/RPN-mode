package com.robert.math;

import java.math.BigDecimal;

/**
 * @author Robert.XU <xutao@bjnja.com>
 * @version RPN-mode, 2018/10/18 0018
 */
public class Operation {

    public static BigDecimal add (BigDecimal first, BigDecimal second){
        return first.add(second);
    }

    public static BigDecimal reduction(BigDecimal first, BigDecimal second) {
        return first.subtract(second);
    }

    public static BigDecimal multiply(BigDecimal first, BigDecimal second) {
        return first.multiply(second);
    }

    public static BigDecimal divide(BigDecimal first, BigDecimal second) {
        return first.divide(second);
    }

    public static BigDecimal sqrt(BigDecimal bigDecimal){
        return bigDecimal;
    }
}
