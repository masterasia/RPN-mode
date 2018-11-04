package com.robert.math;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.robert.stack.RPNCalculator;

/**
 * 撤销操作
 *
 * @author Robert.XU <xutao@bjnja.com>
 * @version RPN, 2018/11/4 0004
 */
@Component
public class UndoOperationImpl implements OperationService {

    private static Logger logger = LoggerFactory.getLogger(UndoOperationImpl.class);

    @Autowired
    private RPNCalculator rpnCalculator;

    /**
     * 加法运算运算符号
     * 使用该符号匹配撤销操作
     */
    private static final String SYMBOL = "undo";

    /**
     * 运算需要的数据个数
     */
    private static final int NUMBERS = 0;

    /**
     * 初始化-在计算器中注册撤销操作
     */
    @PostConstruct
    private void init() {
        logger.info("register the undo operation.");
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
        rpnCalculator.moveToLast();
        return null;
    }

    /**
     * 撤销操作无需进行记录
     *
     * @return 不记录
     */
    @Override
    public boolean needCache() {
        return false;
    }
}
