package com;

import java.util.StringTokenizer;

/**
 * @author lijiaming
 * @title: Demo
 * @projectName demo
 * @description: TODO
 * @date 2019/7/3020:16
 */
public class Demo {
    public static void main(String[] args) {
        String yearMonth = "2019-01";
        StringTokenizer st = new StringTokenizer(yearMonth,"-");
        System.out.println(st.nextToken());
    }

}
