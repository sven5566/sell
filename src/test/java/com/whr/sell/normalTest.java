package com.whr.sell;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class normalTest {
    @Test
    public void test(){
        List features = Arrays.asList("Lambdas", "Default Method", "Stream API", "Date and Time API");
        features.forEach(n -> System.out.println(n));

// 使用Java 8的方法引用更方便，方法引用由::双冒号操作符标示，
// 看起来像C++的作用域解析运算符
        features.forEach(System.out::println);
    }

}
