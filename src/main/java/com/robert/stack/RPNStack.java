package com.robert.stack;

import java.math.BigDecimal;
import java.util.Stack;

import com.robert.util.Constant;
import com.robert.RPNException;
import com.robert.math.Operation;
import com.robert.util.StringUtil;


/**
 * a stack for RPN
 *
 * @author Robert.XU <xutao@bjnja.com>
 * @version RPN-mode, 2018/10/18 0018
 */
public class RPNStack implements Constant {

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
        } catch (RPNException e) {
            System.out.println(e.getMessage());
        }
        print();
    }

    public void print() {
        System.out.print("stack: ");
        show.forEach(number -> {
            if (number.contains(POINT + "") && number.indexOf(POINT + "") + TEN <= number.length()) {
                System.out.print(new BigDecimal(number).setScale(SHOW_PRECISION, ROUNDING_MODE));
            } else {
                System.out.print(number);
            }
            System.out.print(SPACE);
        });
        System.out.println();
    }

    private void RPN() throws RPNException {
        while (i < l) {
            nextChar();
            switch (c) {
                case ADD:
                case REDUCE:
                case MULTIPLY:
                case DIVIDE:
                    this.operationOrder();
                    break;
                case ZERO:
                case ONE:
                case TWO:
                case THREE:
                case FOUR:
                case FIVE:
                case SIX:
                case SEVEN:
                case EIGHT:
                case NINE:
                case POINT:
                    this.numberOrder();
                    break;
                case CLEAR_START:
                    this.clearOrder();
                    break;
                case UNDO_START:
                    this.undoOrder();
                    break;
                case SQRT_START:
                    this.sqrtOrder();
                    break;
                case SPACE:
                    ++i;
                    break;
                default:
                    throw new RPNException(String.format(ERROR_MESSAGE, c, i + 1));
            }
        }
    }

    private void nextChar() {
        c = raw.charAt(i);
    }

    /**
     * 队列清除
     */
    private void clearOrder() throws RPNException {
        // 若清除命令后是最后一位，或下一位是空格，则是清除命令，执行清除
        if (i + CLEAR.length() > l) {
            throw new RPNException(String.format(ERROR_MESSAGE, c, i + 1));
        } else if (checkClear()) {
            clear();
            i += CLEAR.length();
        } else {
            throw new RPNException(String.format(ERROR_MESSAGE, c, i + 1));
        }
    }

    private void clear() {
        base.clear();
        show.clear();
    }

    private boolean checkClear() {
        return raw.substring(i, i + CLEAR.length()).equals(CLEAR);
    }

    /**
     * 计算开方
     */
    private void sqrtOrder() throws RPNException {
        // 若开方命令后是最后一位，或下一位是空格，则是开方命令，执行开方运算
        if (i + SQRT.length() > l) {
            throw new RPNException(String.format(ERROR_MESSAGE, c, i + 1));
        } else if (checkSqrt()) {
            // 开方运算需要操作数，判断结果集是否满足
            if (show.empty()) {
                throw new RPNException(String.format(ERROR_MESSAGE, c, i + 1));
            }
            sqrt();
            i += SQRT.length();
        } else {
            throw new RPNException(String.format(ERROR_MESSAGE, c, i + 1));
        }

    }

    private void sqrt() {
        BigDecimal first = new BigDecimal(show.pop());
        BigDecimal result = Operation.sqrt(first);
        show.push(StringUtil.subZeroAndDot(result.toString()));
        base.push(SQRT);
    }

    private boolean checkSqrt() {
        return raw.substring(i, i + SQRT.length()).equals(SQRT);
    }

    /**
     * 回滚操作
     */
    private void undoOrder() throws RPNException {
        // 若回滚命令后是最后一位，或下一位是空格，则是回滚命令，执行回滚操作
        if (i + UNDO.length() > l) {
            throw new RPNException(String.format(ERROR_MESSAGE, c, i + 1));
        } else if (checkUndo()) {
            base.pop();
            reBuild();
            i += UNDO.length();
        } else {
            throw new RPNException(String.format(ERROR_MESSAGE, c, i + 1));
        }
    }

    private boolean checkUndo() {
        return raw.substring(i, i + UNDO.length()).equals(UNDO);
    }

    /**
     * 重算当前队列
     */
    private void reBuild() {
        Stack<String> tmp = (Stack<String>) base.clone();
        clear();
        tmp.forEach(item -> {
            if (item.equals(SQRT)) {
                this.sqrt();
            } else if (item.charAt(0) == ADD || item.charAt(0) == REDUCE || item.charAt(0) == MULTIPLY || item.charAt(0) == DIVIDE) {
                try {
                    c = item.charAt(0);
                    this.operation();
                } catch (RPNException e) {
                    e.printStackTrace();
                }
            } else {
                this.number(item);
            }
        });
    }

    /**
     * 拼接数字
     */
    private void numberOrder() throws RPNException {
        StringBuilder digitTemp = new StringBuilder();
        while (i < l) {
            // 读取当前字符
            nextChar();
            // 添加负数、小数，符号、小数点仅能添加一次
            if (isDigit()) {
                if (format(digitTemp.toString())) {
                    throw new RPNException(String.format(ERROR_MESSAGE, c, i + 1));
                }
                digitTemp.append(c);
            } else if (Character.isSpaceChar(c)) {
                break;
            } else {
                throw new RPNException(String.format(ERROR_MESSAGE, c, i + 1));
            }
            ++i;
        }
        number(digitTemp.toString());
    }

    private void number(String string) {
        base.push(string);
        show.push(StringUtil.subZeroAndDot(string));
    }

    private boolean isDigit() {
        return (c >= ZERO && c <= NINE) || c == REDUCE || c == POINT;
    }

    private boolean format(String checkString) {
        return (c == REDUCE && checkString.contains(REDUCE + "")) || (c == POINT && checkString.contains(POINT + ""));
    }

    /**
     * 确认操作符
     */
    private void operationOrder() throws RPNException {
        // 若符号后是最后一位，或下一位是空格，则是四则运算符号，执行运算
        if (i + 1 == l || Character.isSpaceChar(raw.charAt(i + 1))) {
            // 四则运算需要操作数与被操作数，判断结果集是否满足
            if (show.size() < OPERATION) {
                throw new RPNException(String.format(ERROR_MESSAGE, c, i + 1));
            }
            operation();
            ++i;
        } else {
            // 当减号后为数字、小数点时，解析为实数，其他字符与符号组合均为异常
            if (c == REDUCE && (Character.isDigit(raw.charAt(i + 1)) || raw.charAt(i + 1) == POINT)) {
                numberOrder();
            } else {
                throw new RPNException(String.format(ERROR_MESSAGE, c, i + 1));
            }
        }
    }

    private void operation() throws RPNException {
        BigDecimal second = new BigDecimal(show.pop());
        BigDecimal first = new BigDecimal(show.pop());
        BigDecimal result;
        switch (c) {
            case ADD:
                result = Operation.add(first, second);
                break;
            case REDUCE:
                result = Operation.reduce(first, second);
                break;
            case MULTIPLY:
                result = Operation.multiply(first, second);
                break;
            case DIVIDE:
                result = Operation.divide(first, second);
                break;
            default:
                throw new RPNException(String.format(ERROR_MESSAGE, c, i + 1));
        }
        show.push(StringUtil.subZeroAndDot(result.toString()));
        base.push(c + "");
    }
}
