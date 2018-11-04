package com.robert.math;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.robert.stack.RPNCalculator;
import com.robert.util.StringUtil;

import static com.robert.util.Constant.OPERATOR_PRECISION;
import static com.robert.util.Constant.ROUNDING_MODE;
import static com.robert.util.Constant.ZERO_NUMBER;

/**
 * 除法运算方法
 *
 * @author Robert.XU <xutao@bjnja.com>
 * @version RPN, 2018/11/3 0003
 */
@Component
public class DivideOperationImpl implements OperationService {

    private static Logger logger = LoggerFactory.getLogger(DivideOperationImpl.class);

    @Autowired
    private RPNCalculator rpnCalculator;

    /**
     * 除法运算运算符号
     * 使用该符号匹配除法运算
     */
    private static final String SYMBOL = "/";

    /**
     * 除法运算需要的数据个数
     */
    private static final int NUMBERS = 2;

    /**
     * 初始化-在计算器中注册除法运算
     */
    @PostConstruct
    private void init() {
        logger.info("register the divide operation.");
        rpnCalculator.addObserver(this);
    }

    @Override
    public String getOperator() {
        return SYMBOL;
    }

    @Override
    public int getNumbers() {
        return NUMBERS;
    }

    @Override
    public String operator() {
        logger.info(" begin operation {}", SYMBOL);
        List<BigDecimal> number = rpnCalculator.getNumbers(NUMBERS);
        if (ZERO_NUMBER == number.get(1).signum() ){
            logger.error(" Can not divide zero .");
            rpnCalculator.isError();
            rpnCalculator.setNumbersBack(number);
            return null;
        }
        return StringUtil.subZeroAndDot(divide(number.get(0), number.get(1)).toString());
    }

    @Override
    public boolean needCache() {
        return true;
    }

    /**
     * 除法运算
     *
     * @param first  被除数
     * @param second 除数
     * @return 商
     */
    private static BigDecimal divide(BigDecimal first, BigDecimal second) {
        try {
            return first.divide(second);
        } catch (ArithmeticException e) {
            return first.divide(second, OPERATOR_PRECISION, ROUNDING_MODE);
        }
    }
}
