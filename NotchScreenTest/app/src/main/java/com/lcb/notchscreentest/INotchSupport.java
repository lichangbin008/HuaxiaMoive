package com.lcb.notchscreentest;

import android.app.Activity;
import android.view.Window;

/**
 * 刘海屏接口
 */
public interface INotchSupport {
    /**
     * 判断当前是否是刘海屏手机
     *
     * @param window
     * @return
     */
    boolean isNotchScreen(Window window);


    /**
     * 获取刘海屏的高度
     *
     * @param window
     * @return
     */
    int getNotchHeight(Window window);

//    获取的值   每个手机厂商   看 api

    /**
     * 设置刘海屏内的内容
     *
     * @param activity
     */
    void fullScreenUseStatus(Activity activity, OnNotchCallBack notchCallBack);

    /**
     * 不用刘海屏内的内容  透明的  设置颜色
     *
     * @param activity
     * @param notchCallBack
     */
    void fullScreenDontUseStatus(Activity activity, OnNotchCallBack notchCallBack);
}
