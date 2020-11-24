package com.lcb.notchscreentest;

import com.lcb.notchscreentest.NotchProperty;

/**
 * 触摸回调
 */
public interface OnNotchCallBack {
    /**
     * 刘海屏属性回调
     *
     * @param notchProperty
     */
    void onNotchPropertyCallback(NotchProperty notchProperty);
}
