package com.robert.stack;

import java.math.BigDecimal;
import java.util.ArrayDeque;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.robert.util.Constant;

/**
 * 计算记录-用于记录计算结果
 *
 * @author Robert.XU <xutao@bjnja.com>
 * @version RPN, 2018/11/3 0003
 */
@Component
public class RPNStack implements Constant {

    private static Logger logger = LoggerFactory.getLogger(RPNStack.class);

    /**
     * 计算结果集
     */
    private ArrayDeque<String> show = new ArrayDeque<>();

    /**
     * 将当前结果集保存为历史记录
     *
     * @return 历史记录
     */
    public RPNMemento save() {
        return new RPNMemento(show.clone());
    }

    /**
     * 通过历史记录恢复结果集
     *
     * @param memento 历史记录
     */
    public void restore(RPNMemento memento) {
        this.show = memento.getShow().clone();
    }

    /**
     * get show
     *
     * @return show
     */
    public ArrayDeque<String> getShow() {
        return show;
    }

    /**
     * set show
     *
     * @param show show
     */
    public RPNStack setShow(ArrayDeque<String> show) {
        this.show = show;
        return this;
    }

    /**
     * 获取结果集最后一条记录
     * 并清除该记录
     *
     * @return 记录内容
     */
    public String pop() {
        return show.removeLast();
    }

    /**
     * 清除整个结果集
     */
    public void clear() {
        show.clear();
    }

    /**
     * 向结果集中存储记录
     *
     * @param s 待存储的记录
     */
    public void push(String s) {
        show.addLast(s);
    }

    /**
     * 获取结果集的大小
     *
     * @return 结果集的大小
     */
    public int size() {
        return show.size();
    }

    /**
     * 打印结果集内数据
     *
     * @return
     */
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("stack: ");
        show.forEach(number -> {
            if (number.contains(POINT + "") && number.indexOf(POINT + "") + TEN <= number.length()) {
                stringBuilder.append(new BigDecimal(number).setScale(SHOW_PRECISION, ROUNDING_MODE));
            } else {
                stringBuilder.append(number);
            }
            stringBuilder.append(SPACE);
        });
        return stringBuilder.toString().trim();
    }
}
