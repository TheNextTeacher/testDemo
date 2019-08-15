package com.validate.demo;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author lijiaming
 * @title: Person
 * @projectName demo
 * @description: TODO
 * @date 2019/7/1715:49
 */
class Person {

    @NotNull(message = "用户ID不能为空")
    private Integer id;     //应为包装类型，否则不能检测到

    @NotNull(message = "test不能为空")
    private String test;

    @NumberVlidator(message = "体重必须为数字")  //该注解为自定义注解
    private String weight;

    @NotNull(message = "用户姓名不能为空dd")
    @Size(min = 1, max = 10, message = "用户姓名必须是1-10位之间")
    private String username;
    //省略setter和getter方法
}
