package com.robert.math;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.robert.stack.RPNCalculator;

/**
 * 清除方法
 *
 * @author Robert.XU <xutao@bjnja.com>
 * @version RPN, 2018/11/3 0003
 */
@Component
public class ClearOperationImpl implements OperationService {

    private static Logger logger = LoggerFactory.getLogger(ClearOperationImpl.class);

    @Autowired
    private RPNCalculator rpnCalculator;

    /**
     * 清除方法标识
     * 使用该标识匹配清除操作
     */
    private static final String SYMBOL = "clear";

    /**
     * 运算需要的数据个数
     */
    private static final int NUMBERS = 0;

    /**
     * 初始化-在计算器中注册清除运算
     */
    @PostConstruct
    private void init() {
        logger.info("register the clear operation.");
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
        rpnCalculator.removeAll();
        return null;
    }

    @Override
    public boolean needCache() {
        return true;
    }
}
