package com.robert.stack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.robert.util.RPNException;

import static com.robert.util.Constant.ERROR_MESSAGE;

/**
 * @author Robert.XU <xutao@bjnja.com>
 * @version RPN, 2018/11/5 0005
 */
@Component
public class OrderAnalysis {

    private static Logger logger = LoggerFactory.getLogger(OrderAnalysis.class);

    @Autowired
    private RPNCalculator rpnCalculator;

    /**
     * 待读取、分解的字符串
     */
    private String raw;

    /**
     * 待处理字符串长度
     */
    private int l = 0;

    /**
     * 游标，标识当前读取到字符串的位置
     */
    private int i = 0;

    /**
     * 最后一个读取到的词组
     */
    private String word;

    /**
     * 接收待处理命令
     * 分析待处理命令
     *
     * @param raw 待处理命令
     */
    public void unpackOrder(String raw) {
        logger.info("unpack input begin: {}", raw);
        this.raw = raw;
        i = 0;
        l = raw.length();
        try {
            this.decode();
        } catch (RPNException e) {
            String err = String.format(ERROR_MESSAGE, word, i == l ? i - word.length() + 1 : i - word.length());
            System.out.println(err);
            logger.error(err);
        }
        logger.info("unpack input end.");
    }

    /**
     * 解析数据，逐词处理
     *
     * @throws RPNException 解析异常
     */
    private void decode() throws RPNException {
        while (i < l) {
            // 获取下一个词
            getWord();

            if (word.isEmpty()) {
                continue;
            }

            rpnCalculator.receiveOrder(word);
        }
    }

    /**
     * 读取下一个词组
     */
    private void getWord() {
        StringBuilder wordTemp = new StringBuilder();
        while (i < l) {
            // 读取当前字符
            char c = raw.charAt(i++);
            // 遇见空格则组词完毕
            if (Character.isSpaceChar(c)) {
                break;
            }
            wordTemp.append(c);
        }
        word = wordTemp.toString();
    }

    /**
     * 打印结果集信息
     */
    public void print() {
        logger.info("print rpn stack: {}", rpnCalculator.getResult());
        System.out.println(rpnCalculator.getResult());
    }

    /**
     * get rpnCalculator
     *
     * @return rpnCalculator
     */
    public RPNCalculator getRpnCalculator() {
        return rpnCalculator;
    }
}
