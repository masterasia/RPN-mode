package com.robert.stack;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.robert.math.OperationService;
import com.robert.util.Constant;
import com.robert.util.RPNException;


/**
 * a stack for decode
 *
 * @author Robert.XU <xutao@bjnja.com>
 * @version decode-mode, 2018/10/18 0018
 */
@Component
public class RPNCalculator implements Constant {

    private static Logger logger = LoggerFactory.getLogger(RPNCalculator.class);

    /**
     * 存储计算结果
     */
    @Autowired
    private RPNStack stack;

    /**
     * 历史记录缓存
     */
    private List<RPNMemento> mementos = new ArrayList<>();

    /**
     * 当前操作在历史记录中的标识位
     */
    private int index = -1;

    /**
     * 已注册的运算集合
     */
    private Map<String, OperationService> operationMap = new HashMap<>();

    /**
     * 运算操作标识
     */
    private Boolean changed = false;

    /**
     * 数据异常标识
     */
    private Boolean error = false;

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
            error = false;
            this.decode();
        } catch (RPNException e) {
            System.out.println(e.getMessage());
            logger.error(e.getMessage());
        }
        logger.info("unpack input end.");
    }

    /**
     * 打印结果集信息
     */
    public void print() {
        logger.info("print rpn stack: {}", stack.toString());
        System.out.println(stack.toString());
    }

    /**
     * 清除计算器
     */
    public void removeAll() {
        logger.info(" clear rpn stack.");
        stack.clear();
    }

    /**
     * 刷新数据为上一条历史记录中的数据
     */
    public void moveToLast() {
        index--;
        if (index >= 0 && index < mementos.size()) {
            stack.restore(mementos.get(index));
        } else {
            error = true;
        }
    }

    /**
     * 若当前数据为某条历史记录
     * 刷新数据为下一条历史记录的数据
     */
    public void moveToNext() {
        index++;
        if (index >= 0 && index < mementos.size()) {
            stack.restore(mementos.get(index));
        } else {
            index--;
            error = true;
        }
    }

    /**
     * 标记异常
     */
    public void isError() {
        error = true;
    }

    /**
     * 获取结果集
     *
     * @return 命令操作后的结果集-字符串数组
     */
    public String getResult() {
        return stack.toString();
    }

    /**
     * 解析数据，逐词处理
     *
     * @throws RPNException 解析异常
     */
    private void decode() throws RPNException {
        while (i < l && !error) {
            // 获取下一个词
            getWord();

            if (word.isEmpty()) {
                continue;
            }

            // 判断词是否为已注册的操作命令
            if (operationMap.containsKey(word)) {
                changed = true;
                notifyObservers(word);
            } else {
                try {
                    // 判断词是否为数字
                    BigDecimal bigDecimal = new BigDecimal(word);
                    logger.info(" find a number , {}", bigDecimal);
                    stack.push(bigDecimal.toString());
                    save();
                } catch (NumberFormatException e) {
                    // 非数字且为未注册命令，则抛出异常
                    logger.error(" wrong input : {}", word);
                    error = true;
                }
            }
        }

        if (error) {
            throw new RPNException(String.format(ERROR_MESSAGE, word, i == l ? i - word.length() + 1 : i - word.length()));
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
     * 保存历史记录
     */
    private void save() {
        // 新增操作备份，需要确认重置已有步骤
        if (index + ONE_NUMBER < mementos.size()) {
            mementos.removeAll(mementos.subList(index + 1, mementos.size()));
        }
        // 操作前缓存当前内容
        mementos.add(stack.save());
        index++;
    }

    /**
     * 获取指定个数的计算数据
     * 并按照计算顺序返回
     *
     * @param number 指定个数
     * @return 计算数据集合
     */
    public List<BigDecimal> getNumbers(int number) {
        List<BigDecimal> results = new ArrayList<>(number);
        if (stack.size() >= number) {
            for (int j = 0; j < number; j++) {
                results.add(new BigDecimal(stack.pop()));
            }
            Collections.reverse(results);
        }
        return results;
    }

    /**
     * 将获取的计算数据退回
     *
     * @param numbers 获取到的数据
     * @return 计算数据集合
     */
    public void setNumbersBack(List<BigDecimal> numbers) {
        numbers.stream().forEach(bigDecimal -> stack.push(bigDecimal.toString()));
    }

    /**
     * 注册运算方法
     *
     * @param operation 运算函数
     */
    public synchronized void addObserver(OperationService operation) {
        logger.info(" get new register from: {} ", operation.getOperator());
        operationMap.put(operation.getOperator(), operation);
    }

    /**
     * 注销运算方法
     *
     * @param operation 运算函数
     */
    public synchronized void deleteObserver(OperationService operation) {
        logger.info(" remove operation: {} ", operation.getOperator());
        operationMap.remove(operation.getOperator());
    }

    /**
     * 根据运算符号，选择注册的运算方法。
     * 运算前储存当前信息
     *
     * @param operator 运算符号
     */
    public synchronized void notifyObservers(String operator) {
        if (!changed) {
            return;
        }
        // 确认可提供运算方法所需的数据
        if (operationMap.get(operator).getNumbers() > stack.size()) {
            error = true;
        } else {
            if (operationMap.get(operator).needCache()) {
                // 操作前缓存当前内容,若回撤操作，则可使用保存的历史记录直接恢复
                save();
            }
            String results = operationMap.get(operator).operator();
            if (null != results && !results.isEmpty()) {
                // 仅存储有效结果值
                stack.push(results);
            }
        }
        changed = false;
    }
}
