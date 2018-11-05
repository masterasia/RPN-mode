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
        if (index >= 0 && index < mementos.size()) {
            stack.restore(mementos.get(index));
            index--;
        } else {
            error = true;
        }
    }

    /**
     * 若当前数据为某条历史记录
     * 刷新数据为下一条历史记录的数据
     */
    public void moveToNext() {
        if (index >= 0 && index < mementos.size()) {
            stack.restore(mementos.get(index));
            index++;
        } else {
            error = true;
        }
    }

    /**
     * 标记异常-操作步骤有误
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
    public void receiveOrder(String order) throws RPNException {
        error = false;
        // 判断词是否为已注册的操作命令
        if (operationMap.containsKey(order)) {
            changed = true;
            notifyObservers(order);
        } else {
            try {
                // 判断词是否为数字
                BigDecimal bigDecimal = new BigDecimal(order);
                logger.info(" find a number , {}", bigDecimal);
                save();
                stack.push(bigDecimal.toString());
            } catch (NumberFormatException e) {
                // 非数字且为未注册命令，则抛出异常
                logger.error(" wrong input : {}", order);
                throw new RPNException();
            }
        }

        if (error) {
            // 运算中出现异常，将由操作执行标记，标识当前操作异常，该操作若为需要缓存的操作，则会导致缓存异常操作一次，需要进行自动回退
            if (operationMap.get(order).needCache()) {
                // 自动回退
                this.moveToLast();
            }
            throw new RPNException();
        }
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
    public synchronized void notifyObservers(String operator) throws RPNException {
        if (!changed) {
            return;
        }
        // 确认可提供运算方法所需的数据
        if (operationMap.get(operator).getNumbers() > stack.size()) {
            throw new RPNException();
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
