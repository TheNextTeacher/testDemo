package com.analysisdata.demo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author lijiaming
 * @title: TimeDemo
 * @projectName demo
 * @description: TODO
 * @date 2019/7/19:37
 */
public class TimeDemo {

    public static void main(String[] args) throws InterruptedException {
//
//        Thread t1 = new Thread(new MyThread());
//        Thread t2 = new Thread(new MyThread());
//        t1.start();
//        Thread.sleep(4000);
//        t2.start();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        System.out.println(LocalDate.parse("2019-11-01", formatter));
//        LocalDateTime date = LocalDateTime.now();
//        System.out.println(LocalDateTime.now());
//        System.out.println(TimeUtils.getLastMonth());
//        System.out.println(TimeUtils.getNowMonth());
    }


}

class TimeUtils {
    private static LocalDateTime localDate;
    private static DateTimeFormatter formatter;

    static {
        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    }

    public static String getNowMonth() {
        return formatter.format(LocalDateTime.now());
    }

    public static String getLastMonth() {
        return formatter.format(LocalDateTime.now().minusMonths(1));
    }

}

class MyThread extends Thread {

    @Override
    public void run() {

        try {
            System.out.println(TimeUtils.getNowMonth());
            System.out.println(TimeUtils.getLastMonth());
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}