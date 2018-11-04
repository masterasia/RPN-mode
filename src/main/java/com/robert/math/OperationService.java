package com.robert.math;

/**
 * 运算函数接口，所有运算方法应实现此接口
 *
 * @author Robert.XU <xutao@bjnja.com>
 * @version RPN, 2018/11/3 0003
 */
public interface OperationService {

    /**
     * 获取当前运算方法的运算符号
     * 计算器将通过匹配该符号来判断该由哪个运算方法执行运算
     *
     * @return 运算符号
     */
    String getOperator();

    /**
     * 获取当前运算方法的所需参数数量
     *
     * @return 参数数量
     */
    int getNumbers();

    /**
     * 执行运算
     *
     * @return 运算结果
     */
    String operator();

    /**
     * 运算是否需要缓存
     * 可缓存的运算将存储操作记录
     * 不可缓存的运算将不做存储
     *
     * @return 判断结果
     */
    boolean needCache();
}
