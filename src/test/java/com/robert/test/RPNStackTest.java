package com.robert.test;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.robert.stack.OrderAnalysis;

import static org.junit.Assert.assertEquals;

/**
 * @author Robert.XU <xutao@bjnja.com>
 * @version RPN-mode, 2018/10/18 0018
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RPNStackTest {

    @Autowired
    private OrderAnalysis orderAnalysis;

    @Before
    public void setUp() throws Exception {
        orderAnalysis.unpackOrder("clear");
    }

    /**
     * 模拟计算π值
     */
    @Test
    public void testPi() {
        orderAnalysis.unpackOrder("2");
        int index = 1;
        int step = 3;
        StringBuilder stringBuilder = new StringBuilder(" 2 ");
        while (index < 40) {
            stringBuilder.append(index + " " + step + " / " + " * ");
            orderAnalysis.unpackOrder(stringBuilder.toString());
            if (index > 1) {
                orderAnalysis.unpackOrder(" + ");
            }
            index++;
            step += 2;
        }
        orderAnalysis.unpackOrder(" + ");
        orderAnalysis.print();
        assertEquals("stack: " + String.valueOf(Math.PI).substring(0, 12), orderAnalysis.getRpnCalculator().getResult());
    }

    /**
     * 模拟计算自然对数
     */
    @Test
    public void testE() {
        orderAnalysis.unpackOrder("1 ");
        int index = 1;
        StringBuilder stringBuilder = new StringBuilder(" 1 ");
        while (index < 14) {
            stringBuilder.append(index + " " + " * ");
            orderAnalysis.unpackOrder(" 1 " + stringBuilder.toString() + " / ");
            if (index > 1) {
                orderAnalysis.unpackOrder(" + ");
            }
            index++;
        }
        orderAnalysis.unpackOrder(" + ");
        orderAnalysis.print();
    }

    /**
     * 标准化测试
     */
    @Test
    public void ExampleOne() {
        orderAnalysis.unpackOrder("5 2");
        orderAnalysis.print();
        assertEquals("stack: 5 2", orderAnalysis.getRpnCalculator().getResult());
    }

    @Test
    public void ExampleTwo() {
        orderAnalysis.unpackOrder("2 sqrt");
        orderAnalysis.print();
        assertEquals("stack: " + String.valueOf(Math.sqrt(2.0)).substring(0, 12), orderAnalysis.getRpnCalculator().getResult());
        orderAnalysis.unpackOrder("clear 9 sqrt");
        orderAnalysis.print();
        assertEquals("stack: 3", orderAnalysis.getRpnCalculator().getResult());
    }

    @Test
    public void ExampleThree() {
        orderAnalysis.unpackOrder("5 2 -");
        orderAnalysis.print();
        assertEquals("stack: 3", orderAnalysis.getRpnCalculator().getResult());
        orderAnalysis.unpackOrder("3 -");
        orderAnalysis.print();
        assertEquals("stack: 0", orderAnalysis.getRpnCalculator().getResult());
        orderAnalysis.unpackOrder("clear");
        orderAnalysis.print();
        assertEquals("stack:", orderAnalysis.getRpnCalculator().getResult());
    }

    @Test
    public void ExampleFour() {
        orderAnalysis.unpackOrder("5 4 3 2");
        orderAnalysis.print();
        assertEquals("stack: 5 4 3 2", orderAnalysis.getRpnCalculator().getResult());
        orderAnalysis.unpackOrder("undo undo *");
        orderAnalysis.print();
        assertEquals("stack: 20", orderAnalysis.getRpnCalculator().getResult());
        orderAnalysis.unpackOrder("5 *");
        orderAnalysis.print();
        assertEquals("stack: 100", orderAnalysis.getRpnCalculator().getResult());
        orderAnalysis.unpackOrder("undo");
        orderAnalysis.print();
        assertEquals("stack: 20 5", orderAnalysis.getRpnCalculator().getResult());
    }

    @Test
    public void ExampleFive() {
        orderAnalysis.unpackOrder("7 12 2 /");
        orderAnalysis.print();
        assertEquals("stack: 7 6", orderAnalysis.getRpnCalculator().getResult());
        orderAnalysis.unpackOrder("*");
        orderAnalysis.print();
        assertEquals("stack: 42", orderAnalysis.getRpnCalculator().getResult());
        orderAnalysis.unpackOrder("4 /");
        orderAnalysis.print();
        assertEquals("stack: 10.5", orderAnalysis.getRpnCalculator().getResult());
    }

    @Test
    public void ExampleSix() {
        orderAnalysis.unpackOrder("1 2 3 4 5");
        orderAnalysis.print();
        assertEquals("stack: 1 2 3 4 5", orderAnalysis.getRpnCalculator().getResult());
        orderAnalysis.unpackOrder("*");
        orderAnalysis.print();
        assertEquals("stack: 1 2 3 20", orderAnalysis.getRpnCalculator().getResult());
        orderAnalysis.unpackOrder("clear 3 4 -");
        orderAnalysis.print();
        assertEquals("stack: -1", orderAnalysis.getRpnCalculator().getResult());
    }

    @Test
    public void ExampleSeven() {
        orderAnalysis.unpackOrder("1 2 3 4 5");
        orderAnalysis.print();
        assertEquals("stack: 1 2 3 4 5", orderAnalysis.getRpnCalculator().getResult());
        orderAnalysis.unpackOrder("* * * *");
        orderAnalysis.print();
        assertEquals("stack: 120", orderAnalysis.getRpnCalculator().getResult());
    }

    @Test
    public void ExampleEight() {
        orderAnalysis.unpackOrder("1 2 3 * 5 + * * 6 5");
        orderAnalysis.print();
        assertEquals("stack: 11", orderAnalysis.getRpnCalculator().getResult());
    }
}
