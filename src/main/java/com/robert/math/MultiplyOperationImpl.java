package com.robert.math;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.robert.stack.RPNCalculator;

/**
 * 乘法运算方法
 *
 * @author Robert.XU <xutao@bjnja.com>
 * @version RPN, 2018/11/3 0003
 */
@Component
public class MultiplyOperationImpl implements OperationService {

    private static Logger logger = LoggerFactory.getLogger(MultiplyOperationImpl.class);

    @Autowired
    private RPNCalculator rpnCalculator;

    /**
     * 乘法运算运算符号
     * 使用该符号匹配乘法运算
     */
    private static final String SYMBOL = "*";

    /**
     * 运算需要的数据个数
     */
    private static final int NUMBERS = 2;

    /**
     * 初始化-在计算器中注册乘法运算
     */
    @PostConstruct
    private void init() {
        logger.info("register the multiply operation.");
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
        return multiply(number.get(0), number.get(1)).toString();
    }

    @Override
    public boolean needCache() {
        return true;
    }

    /**
     * 乘法运算
     *
     * @param first  被乘数
     * @param second 乘数
     * @return 积
     */
    private static BigDecimal multiply(BigDecimal first, BigDecimal second) {
        return first.multiply(second);
    }
}
