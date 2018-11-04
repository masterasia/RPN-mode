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
 * 减法运算方法
 *
 * @author Robert.XU <xutao@bjnja.com>
 * @version RPN, 2018/11/3 0003
 */
@Component
public class ReduceOperationImpl implements OperationService {

    private static Logger logger = LoggerFactory.getLogger(ReduceOperationImpl.class);

    @Autowired
    private RPNCalculator rpnCalculator;

    /**
     * 减法运算运算符号
     * 使用该符号匹配减法运算
     */
    private static final String SYMBOL = "-";

    /**
     * 运算需要的数据个数
     */
    private static final int NUMBERS = 2;

    /**
     * 初始化-在计算器中注册减法运算
     */
    @PostConstruct
    private void init() {
        logger.info("register the reduce operation.");
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
        return reduce(number.get(0), number.get(1)).toString();
    }

    @Override
    public boolean needCache() {
        return true;
    }

    /**
     * 减法运算
     *
     * @param first  被减数
     * @param second 减数
     * @return 差
     */
    private static BigDecimal reduce(BigDecimal first, BigDecimal second) {
        return first.subtract(second);
    }
}
