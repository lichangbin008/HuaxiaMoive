package com.suma.midware.huaxia.movie;

import android.text.TextUtils;
import android.util.Log;

import java.lang.reflect.Method;

public class ClassUtil {

    private static final String TAG = "ClassUtil";

    /**
     * 加载指定类名的类
     *
     * @param clsName 指定类名
     * @return 类
     */
    public static final Class<?> loadClass(String clsName) {
        if (TextUtils.isEmpty(clsName)) {
            Log.e(TAG, "loadClass illegal clsName=" + clsName);
            return null;
        }
        Log.v(TAG, "loadClass clsName=" + clsName);
        try {
            return Class.forName(clsName);
        } catch (Throwable e) {
            Log.e(TAG, "loadClass clsName=" + clsName + "," + e.getClass().getName());
        }
        return null;
    }

    /**
     * 调用方法
     *
     * @param method    方法对象
     * @param methodObj 方法所属对象,如果此对象为null表示是静态方法
     * @param paramObjs 方法参数对象序列
     * @return 方法返回对象
     */
    public static final Object callMethod(Method method, Object methodObj,
                                          Object... paramObjs) {
        if (method == null) {
            Log.e(TAG, "callMethod method null");
            return null;
        }
        try {
            return method.invoke(methodObj, paramObjs);
        } catch (Throwable e) {
            Log.e(TAG, "callMethod=" + e.getClass().getName());
        }
        return null;
    }

    /**
     * 获取指定类名的指定方法
     *
     * @param cls          类
     * @param methodName   方法名称
     * @param paramClasses 方法的参数类型序列
     * @return 方法
     */
    public static final Method getMethod(Class<?> cls, String methodName,
                                         Class<?>... paramClasses) {
        return getMethod(cls, methodName, false, paramClasses);
    }

    /**
     * 获取指定类名的指定方法
     *
     * @param cls          类
     * @param methodName   方法名称
     * @param isDeclared   是否是公开声明的
     * @param paramClasses 方法的参数类型序列
     * @return 方法
     */
    public static final Method getMethod(Class<?> cls, String methodName,
                                         boolean isDeclared, Class<?>... paramClasses) {
        if (cls == null || TextUtils.isEmpty(methodName)) {
            Log.e(TAG, "getMethod illegal methodName=" + methodName + ",cls=" + cls);
            return null;
        }
        try {
            return isDeclared ? cls.getDeclaredMethod(methodName, paramClasses)
                    : cls.getMethod(methodName, paramClasses);
        } catch (Throwable e) {
            Log.e(TAG, "getMethod methodName=" + methodName + "," + e.getClass().getName());
        }
        return null;
    }
}
