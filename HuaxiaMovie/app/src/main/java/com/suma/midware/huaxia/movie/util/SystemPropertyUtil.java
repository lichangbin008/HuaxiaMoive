package com.suma.midware.huaxia.movie.util;

import android.text.TextUtils;

import java.lang.reflect.Method;

public class SystemPropertyUtil {

    /**
     * 获取系统属性
     *
     * @param key 键名
     * @return
     */
    public static String getSystemPropertie(String key) {
        String value = "";
        try {
            Method method = Class.forName("android.os.SystemProperties").getMethod("get", String.class, String.class);
            value = ((String) method.invoke(null, key, "")).toString();
            // 对部分非法数据进行处理
            if (value == null || value.equalsIgnoreCase("unknown")) {
                value = "";
            } else {
                value = value.trim();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }


    /**
     * 设置系统属性
     *
     * @param key
     * @param value
     */
    public static void setSystemPropertie(String key, String value) {
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method set = c.getMethod("set", String.class, String.class);
            set.invoke(null, key, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
