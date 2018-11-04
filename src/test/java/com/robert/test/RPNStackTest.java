package com.robert.test;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.robert.stack.RPNCalculator;

/**
 * @author Robert.XU <xutao@bjnja.com>
 * @version RPN-mode, 2018/10/18 0018
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RPNStackTest {

    @Autowired
    private RPNCalculator rpnStack;

    @Before
    public void setUp() throws Exception {
        rpnStack.unpackOrder("clear");
    }

    /**
     * 模拟计算π值
     */
    @Test
    public void testPi() {
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
    @Test
    public void testE() {
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
    @Test
    public void ExampleOne() {
        rpnStack.unpackOrder("5 2");
        rpnStack.print();
    }

    @Test
    public void ExampleTwo() {
        rpnStack.unpackOrder("2 sqrt");
        rpnStack.print();
        rpnStack.unpackOrder("clear 9 sqrt");
        rpnStack.print();
    }

    @Test
    public void ExampleThree() {
        rpnStack.unpackOrder("5 2 -");
        rpnStack.print();
        rpnStack.unpackOrder("3 -");
        rpnStack.print();
        rpnStack.unpackOrder("clear");
        rpnStack.print();
    }

    @Test
    public void ExampleFour() {
        rpnStack.unpackOrder("5 4 3 2");
        rpnStack.print();
        rpnStack.unpackOrder("undo undo *");
        rpnStack.print();
        rpnStack.unpackOrder("5 *");
        rpnStack.print();
        rpnStack.unpackOrder("undo");
        rpnStack.print();
    }

    @Test
    public void ExampleFive() {
        rpnStack.unpackOrder("7 12 2 /");
        rpnStack.print();
        rpnStack.unpackOrder("*");
        rpnStack.print();
        rpnStack.unpackOrder("4 /");
        rpnStack.print();
    }

    @Test
    public void ExampleSix() {
        rpnStack.unpackOrder("1 2 3 4 5");
        rpnStack.print();
        rpnStack.unpackOrder("*");
        rpnStack.print();
        rpnStack.unpackOrder("clear 3 4 -");
        rpnStack.print();
    }

    @Test
    public void ExampleSeven() {
        rpnStack.unpackOrder("1 2 3 4 5");
        rpnStack.print();
        rpnStack.unpackOrder("* * * *");
        rpnStack.print();
    }

    @Test
    public void ExampleEight() {
        rpnStack.unpackOrder("1 2 3 * 5 + * * 6 5");
        rpnStack.print();
    }
}
