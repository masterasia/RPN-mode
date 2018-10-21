package com.robert.test;

import com.robert.stack.RPNStack;

/**
 * @author Robert.XU <xutao@bjnja.com>
 * @version RPN-mode, 2018/10/18 0018
 */
public class RPNStackTest {

    public static void main(String[] args) {
        testPi();
        testE();
        ExampleOne();
        ExampleTwo();
        ExampleThree();
        ExampleFour();
        ExampleFive();
        ExampleSix();
        ExampleSeven();
        ExampleEight();
    }

    /**
     * 模拟计算π值
     */
    public static void testPi() {
        RPNStack rpnStack = new RPNStack();

        rpnStack.unpackOrder("2");
        int index = 1;
        int step = 3;
        StringBuilder stringBuilder = new StringBuilder(" 2 ");
        while (index < 40) {
            stringBuilder.append(index + " " + step + " / " + " * ");
            rpnStack.unpackOrder(stringBuilder.toString());
            if (index > 1) {
                rpnStack.unpackOrder(" + ");
            }
            index++;
            step += 2;
        }
        rpnStack.unpackOrder(" + ");
        rpnStack.print();
    }

    /**
     * 模拟计算自然对数
     */
    public static void testE() {
        RPNStack rpnStack = new RPNStack();

        rpnStack.unpackOrder("1 ");
        int index = 1;
        StringBuilder stringBuilder = new StringBuilder(" 1 ");
        while (index < 14) {
            stringBuilder.append(index + " " + " * ");
            rpnStack.unpackOrder(" 1 " + stringBuilder.toString() + " / ");
            if (index > 1) {
                rpnStack.unpackOrder(" + ");
            }
            index++;
        }
        rpnStack.unpackOrder(" + ");
        rpnStack.print();
    }

    /**
     * 标准化测试
     */
    public static void ExampleOne() {
        RPNStack rpnStack = new RPNStack();
        rpnStack.unpackOrder("5 2");
        rpnStack.print();
    }

    public static void ExampleTwo() {
        RPNStack rpnStack = new RPNStack();
        rpnStack.unpackOrder("2 sqrt");
        rpnStack.unpackOrder("clear 9 sqrt");
        rpnStack.print();
    }

    public static void ExampleThree() {
        RPNStack rpnStack = new RPNStack();
        rpnStack.unpackOrder("5 2 -");
        rpnStack.unpackOrder("3 -");
        rpnStack.unpackOrder("clear");
        rpnStack.print();
    }

    public static void ExampleFour() {
        RPNStack rpnStack = new RPNStack();
        rpnStack.unpackOrder("5 4 3 2");
        rpnStack.unpackOrder("undo undo *");
        rpnStack.unpackOrder("5 *");
        rpnStack.unpackOrder("undo");
        rpnStack.print();
    }

    public static void ExampleFive() {
        RPNStack rpnStack = new RPNStack();
        rpnStack.unpackOrder("7 12 2 /");
        rpnStack.unpackOrder("*");
        rpnStack.unpackOrder("4 /");
        rpnStack.print();
    }

    public static void ExampleSix() {
        RPNStack rpnStack = new RPNStack();
        rpnStack.unpackOrder("1 2 3 4 5");
        rpnStack.unpackOrder("*");
        rpnStack.unpackOrder("clear 3 4 -");
        rpnStack.print();
    }

    public static void ExampleSeven() {
        RPNStack rpnStack = new RPNStack();
        rpnStack.unpackOrder("1 2 3 4 5");
        rpnStack.unpackOrder("* * * *");
        rpnStack.print();
    }

    public static void ExampleEight() {
        RPNStack rpnStack = new RPNStack();
        rpnStack.unpackOrder("1 2 3 * 5 + * * 6 5");
        rpnStack.print();
    }
}
