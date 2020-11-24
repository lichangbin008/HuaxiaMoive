package com.lcb.notchscreentest;

import android.util.Log;

import java.lang.reflect.Method;

/**
 * 系统属性单例类
 */
public class SystemProperties {

    /**
     * 方法
     */
    private static Method getStringProperty;

    /**
     * 单例对象
     */
    private static SystemProperties sSystemProperties;

    /**
     * 获取单例对象
     *
     * @return
     */
    public static SystemProperties getInstance() {
        if (sSystemProperties == null) {
            synchronized (SystemProperties.class) {
                if (sSystemProperties == null) {
                    sSystemProperties = new SystemProperties();
                }
            }
        }
        return sSystemProperties;
    }

    /**
     * 方法
     */
    private SystemProperties() {
        getStringProperty = getMethod(getClass("android.os.SystemProperties"));
    }

    /**
     * 获取类
     *
     * @param name
     * @return
     */
    private Class getClass(String name) {
        try {
            return Class.forName(name);
        } catch (ClassNotFoundException e) {
        }
        return null;
    }

    /**
     * 过去方法
     *
     * @param clz
     * @return
     */
    private Method getMethod(Class clz) {
        Method method;
        try {
            method = clz != null ? clz.getMethod("get", String.class) : null;
        } catch (Exception e) {
            Log.e("tuch", e.getMessage());
            method = null;
        }
        return method;
    }

    /**
     * get方法
     *
     * @param key
     * @return
     */
    public final String get(String key) {
        String value;
        try {
            value = (String) getStringProperty.invoke(null, key);
            if (value != null) {
                return value.trim();
            } else {
                return "";
            }
        } catch (Exception var4) {
            return "";
        }
    }
}
