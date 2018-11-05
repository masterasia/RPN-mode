package com.robert;

import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import com.robert.stack.OrderAnalysis;

/**
 * RPN 计算器
 * 启动后可持续进行运算输入，计算器会自动进行RPN规则操作。
 * 计算器允许输入 整数、小数 四则运算符号
 * 使用命令 sqrt 进行开方运算，仅返回正根
 * 使用命令 undo 进行回滚操作，取消上一次命令
 * 使用命令 clear 清空结果集
 * 使用命令 exit 可退出计算器
 * 运算过程如遇小数，则保留15位
 * 结果集展示，如小数部分超出10位，仅保留10位
 * 每轮输入一行命令，计算器逐词解析、执行，若遇见错误命令，则该轮执行结束
 * 重新输入可继续紧接上轮错误命令前内容执行
 *
 * @author Robert.XU <xutao@bjnja.com>
 * @version RPN-mode, 2018/10/18 0018
 */
@SpringBootApplication
public class Application {

    @Autowired
    private OrderAnalysis orderAnalysis;

    /**
     * 执行入口
     *
     * @param args 输入参数
     */
    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(Application.class);
        Application application = (Application) applicationContext.getBean("application");
        application.run(args);
    }

    public void run(String... args) {
        Scanner scanner = new Scanner(System.in);
        boolean runnerFlag = true;
        while (runnerFlag) {
            String readLine = scanner.nextLine();
            if (readLine.equals("exit")) {
                runnerFlag = false;
            } else {
                orderAnalysis.unpackOrder(readLine);
                orderAnalysis.print();
            }
        }
    }
}
