package com.validate.demo;

import javax.validation.ValidationException;

/**
 * @author lijiaming
 * @title: Validation
 * @projectName demo
 * @description: TODO
 * @date 2019/7/1715:13
 */
public class ValidationDemo {
    public static void main(String[] args) {
        Person person = new Person();
        try {
            VlidationUtil.validate(person);
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
        }
    }
}
