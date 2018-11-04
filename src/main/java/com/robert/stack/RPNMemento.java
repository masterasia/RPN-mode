package com.robert.stack;

import java.util.ArrayDeque;

/**
 * 历史记录-计算记录的存储对象
 *
 * @author Robert.XU <xutao@bjnja.com>
 * @version RPN, 2018/11/3 0003
 */
class RPNMemento {

    /**
     * save result
     */
    private ArrayDeque<String> show = new ArrayDeque<>();

    public RPNMemento(ArrayDeque<String> show) {
        this.show = show;
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
    public void setShow(ArrayDeque<String> show) {
        this.show = show;
    }
}
