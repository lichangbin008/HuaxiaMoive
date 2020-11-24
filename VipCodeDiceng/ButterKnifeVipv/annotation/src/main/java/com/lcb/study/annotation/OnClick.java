package com.lcb.study.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD) //声明这个注解的在方法上，这个注解标记在什么上面
@Retention(RetentionPolicy.CLASS) //声明注解的生命周期 存在周期 源码期 编译期 运行期
public @interface OnClick {
    int[] value();
}
