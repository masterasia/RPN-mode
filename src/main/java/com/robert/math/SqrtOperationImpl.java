package com.robert.math;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.robert.stack.RPNCalculator;
import com.robert.util.Constant;
import com.robert.util.StringUtil;

/**
 * 开方运算方法
 *
 * @author Robert.XU <xutao@bjnja.com>
 * @version RPN, 2018/11/3 0003
 */
@Component
public class SqrtOperationImpl implements OperationService, Constant {

    private static Logger logger = LoggerFactory.getLogger(SqrtOperationImpl.class);

    @Autowired
    private RPNCalculator rpnCalculator;

    /**
     * 开方运算运算符号
     * 使用该符号匹配开方运算
     */
    private static final String SYMBOL = "sqrt";

    /**
     * 运算需要的数据个数
     */
    private static final int NUMBERS = 1;

    /**
     * 初始化-在计算器中注册开方运算
     */
    @PostConstruct
    private void init() {
        logger.info("register the sqrt operation.");
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
        BigDecimal base = number.get(0);
        if (ONE_NUMBER == base.signum()) {
            return StringUtil.subZeroAndDot(sqrt(base, OPERATOR_PRECISION).toString());
        } else {
            logger.error("");
            rpnCalculator.isError();
            rpnCalculator.setNumbersBack(number);
            return null;
        }
    }

    @Override
    public boolean needCache() {
        return true;
    }

    /**
     * 开方运算
     *
     * @param sourceNum 被开方数
     * @return 正根
     */
    private static BigDecimal sqrt(BigDecimal sourceNum, int accuracy) {
        BigDecimal temp = BigDecimal.valueOf(SQRT_START);
        int precision = HUNDRED;
        MathContext mathContext = new MathContext(precision, RoundingMode.HALF_UP);
        int count = ZERO;
        BigDecimal deviation = sourceNum;
        while (count < HUNDRED) {
            deviation = (deviation.add(sourceNum.divide(deviation, mathContext))).divide(temp, mathContext);
            count++;
        }
        deviation = deviation.setScale(accuracy, ROUNDING_MODE);
        return deviation;
    }
}
