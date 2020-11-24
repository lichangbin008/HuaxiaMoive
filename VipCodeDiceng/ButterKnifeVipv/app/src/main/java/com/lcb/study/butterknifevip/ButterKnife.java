package com.lcb.study.butterknifevip;

import java.lang.reflect.Constructor;

/**
 * Created by ${lichangbin} on 2020/11/24.
 */
public class ButterKnife {

    public static void bind(Object activity){
        String name=activity.getClass().getName();
        String binderName=name+"$$ViewBinder";
        try {
            Class<?> aClass = Class.forName(binderName);
            //得到构造方法
            Constructor<?> constructor =aClass.getConstructor(activity.getClass());
            constructor.newInstance(activity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
