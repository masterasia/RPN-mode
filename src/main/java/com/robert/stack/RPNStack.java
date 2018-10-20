package com.robert.stack;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Stack;

import com.robert.math.Operation;

/**
 * a stack for RPN
 *
 * @author Robert.XU <xutao@bjnja.com>
 * @version RPN-mode, 2018/10/18 0018
 */
public class RPNStack {

    /**
     * save step
     */
    private Stack<String> base = new Stack<>();

    /**
     * save result
     */
    private Stack<String> show = new Stack<>();

    /**
     * 待读取、分解字符串集合
     */
    private String raw;

    /**
     * 游标
     */
    private int i = 0;

    /**
     * 待处理字符串长度
     */
    private int l = 0;

    /**
     * 读取到的字符
     */
    private char c;

    /**
     * 异常标记语句
     */
    private String errMessage = "operator <%s> (position: <%d>): insufficient parameters";

    /**
     * 小数点标记
     */
    private final static char POINT = '.';

    /**
     * 四则运算所需实数个数
     */
    private final static int OPERATION = 2;


    /**
     * 接收待处理命令
     * 分析待处理命令
     * 打印结果集信息
     *
     * @param raw 待处理命令
     */
    public void unpackVar(String raw) {
        this.raw = raw;
        i = 0;
        l = raw.length();
        try {
            this.RPN();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        System.out.println(Arrays.toString(base.toArray()));
        System.out.println(Arrays.toString(show.toArray()));
    }

    private void RPN() throws Exception {
        while (i < l) {
            nextChar();
            switch (c) {
                case '+':
                case '-':
                case '*':
                case '/':
                    this.operation();
                    break;
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                case POINT:
                    this.number();
                    break;
                case 'c':
                    this.clear();
                    break;
                case 'u':
                    this.undo();
                    break;
                case 's':
                    this.sqrt();
                    break;
                case ' ':
                    ++i;
                    break;
                default:
                    throw new Exception(String.format(errMessage, c, i + 1));
            }
        }
    }

    private void nextChar() {
        c = raw.charAt(i);
    }

    /**
     * 队列清除
     */
    private void clear() {
        base.clear();
        show.clear();
    }

    /**
     * 计算开方
     */
    private void sqrt() {
        // 若开方命令后是最后一位，或下一位是空格，则是开方符号，执行运算
        base.push("sqrt");

    }

    /**
     * 回滚操作
     */
    private void undo() {
        base.pop();
        this.reBuild();
    }

    /**
     * 重算当前队列
     */
    private void reBuild() {
        base.stream().forEach(step -> {

        });
    }

    /**
     * 拼接数字
     */
    private void number() throws Exception {
        boolean digitFlag = false;
        StringBuffer digitTemp = new StringBuffer();
        while (i < l) {
            // 读取当前字符
            nextChar();
            // 添加负数、小数，符号、小数点仅能添加一次
            if (isDigit()) {
                if (format(digitTemp.toString())) {
                    throw new Exception(String.format(errMessage, c, i + 1));
                }
                digitTemp.append(c);
            } else if (c == ' ') {
                break;
            } else {
                throw new Exception(String.format(errMessage, c, i + 1));
            }
            ++i;
        }
        base.push(digitTemp.toString());
        show.push(digitTemp.toString());
    }

    private boolean isDigit() {
        return (c >= '0' && c <= '9') || c == '-' || c == '.';
    }

    private boolean format(String checkString) {
        return (c == '-' && checkString.contains("-")) || (c == POINT && checkString.contains(POINT + ""));
    }

    /**
     * 确认操作符
     */
    private void operation() throws Exception {
        // 若符号后是最后一位，或下一位是空格，则是四则运算符号，执行运算
        if (i + 1 == l || Character.isSpaceChar(raw.charAt(i + 1))) {
            // 四则运算需要操作数与被操作数，判断结果集是否满足
            if (show.size() < OPERATION) {
                throw new Exception(String.format(errMessage, c, i));
            }
            BigDecimal second = new BigDecimal(show.pop());
            BigDecimal first = new BigDecimal(show.pop());
            BigDecimal result;
            switch (c) {
                case '+':
                    result = Operation.add(first, second);
                    break;
                case '-':
                    result = Operation.reduction(first, second);
                    break;
                case '*':
                    result = Operation.multiply(first, second);
                    break;
                case '/':
                    result = Operation.divide(first, second);
                    break;
                default:
                    throw new Exception(String.format(errMessage, c, i));
            }
            show.push(result.toString());
            ++i;
        } else {
            // 当减号后为数字、小数点时，解析为实数，其他字符与符号组合均为异常
            if (c == '-' && (Character.isDigit(raw.charAt(i + 1)) || raw.charAt(i + 1) == '.')) {
                number();
            } else {
                throw new Exception(String.format(errMessage, c, i));
            }
        }
    }
}
