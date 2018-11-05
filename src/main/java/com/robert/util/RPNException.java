package com.robert.util;

/**
 * @author Robert.XU <xutao@bjnja.com>
 * @version RPN-mode, 2018/10/20 0020
 */
public class RPNException extends Exception {
    private static final long serialVersionUID = 1L;

    /**
     * 异常代码
     */
    private int code;

    public RPNException() {
    }

    public RPNException(String message, int code) {
        super(message);
    }

    public RPNException(String message) {
        super(message);
    }
}
