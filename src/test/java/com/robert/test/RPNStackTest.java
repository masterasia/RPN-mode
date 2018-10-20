package com.robert.test;

import com.robert.stack.RPNStack;

/**
 * @author Robert.XU <xutao@bjnja.com>
 * @version RPN-mode, 2018/10/18 0018
 */
public class RPNStackTest {

    public static void testPushNumber(){
        RPNStack rpnStack  = new RPNStack();
        rpnStack.unpackVar("-23.23");
        rpnStack.unpackVar("-23.23 0.23123 3123");
        rpnStack.unpackVar("-23.23 2312313123 3213131.312313123");
        rpnStack.unpackVar("-23.23 23131313 -12313 12313");
        rpnStack.unpackVar("-23.23 0. 123131 ");
        rpnStack.unpackVar("-23.23");
    }

    public static void main(String[] args) {
        testPushNumber();
    }
}
