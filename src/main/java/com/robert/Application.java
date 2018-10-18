package com.robert;

import java.util.Scanner;

/**
 * @author Robert.XU <xutao@bjnja.com>
 * @version RPN-mode, 2018/10/18 0018
 */
public class Application {

    /**
     * 执行入口
     * @param args 输入参数
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean runnerFlag = true;
        while(runnerFlag){
            String readLine = scanner.nextLine();
            if (readLine.equals("exit")){
                runnerFlag = false;
            }else {
                System.out.println(readLine);
            }
        }
    }
}
