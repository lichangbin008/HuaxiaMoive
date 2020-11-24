package com.lcb.notchscreentest;

import android.app.Activity;
import android.util.Log;
import android.view.View;

/**
 * 刘海屏工具类
 * 策略模式
 */
public class NotchTools {

    private INotchSupport notchScreenSupport;

    /**
     * 使用刘海屏
     *
     * @param activity
     * @param notchCallBack
     */
    public void fullScreenUseNotch(final Activity activity, final OnNotchCallBack notchCallBack) {
        fullScreenUseStatus(activity, notchCallBack);
    }

    /**
     * 使用刘海屏
     *
     * @param activity
     * @param notchCallBack
     */
    public void fullScreenUseStatus(final Activity activity, final OnNotchCallBack notchCallBack) {
        checkNotch();
        //等到摆放完成后再设置
        activity.getWindow().getDecorView().
                addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
                    @Override
                    public void onViewAttachedToWindow(View view) {
                        notchScreenSupport.fullScreenUseStatus(activity, notchCallBack);
                    }

                    @Override
                    public void onViewDetachedFromWindow(View view) {
                    }
                });
    }

    /**
     * 不使用刘海屏
     *
     * @param activity
     */
    public void fullScreenDontUseNotch(final Activity activity) {
        checkNotch();
        activity.getWindow().getDecorView().
                addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
                    @Override
                    public void onViewAttachedToWindow(View view) {
                        notchScreenSupport.fullScreenDontUseStatus(activity, null);
                    }

                    @Override
                    public void onViewDetachedFromWindow(View view) {
                    }
                });
    }

    /**
     * 检查刘海屏
     */
    public void checkNotch() {
        if (notchScreenSupport == null) {
            if (DeviceTools.isMiui()) {
                notchScreenSupport = new MiUiNotchScreen();
                Log.d("NotchTools","小米手机");
            } else if (DeviceTools.isHuaWei()) {
                notchScreenSupport = new HuaWeiNotchScreen();
                Log.d("NotchTools","华为手机");
            } else if (DeviceTools.isVivo()) {
                notchScreenSupport = new VivoNotchScreen();
                Log.d("NotchTools","Vivo手机");
            } else if (DeviceTools.isOppo()) {
                notchScreenSupport = new OppoNotchScreen();
                Log.d("NotchTools","OPPO手机");
            } else {
                notchScreenSupport = new CommonNotchScreen();
                Log.d("NotchTools","普通手机");
            }
        }
    }
}
