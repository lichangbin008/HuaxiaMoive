package com.lcb.notchscreentest;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import java.lang.reflect.Field;

/**
 * 刘海屏状态栏工具类
 */
public class NotchStatusBarUtils {

    /**
     * 状态栏高度
     */
    private static int statusBarHeight = -1;
    //

    /**
     * 设置系统全面屏，oppo +vivo  采用google官方的方法
     *
     * @param window
     */
    public static void setFullScreenWithSystemUi(final Window window) {
        int systemUiVisibility = 0;
        WindowManager.LayoutParams attrs = window.getAttributes();
        attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        window.setAttributes(attrs);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;

        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            systemUiVisibility |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        }
        window.getDecorView().setSystemUiVisibility(systemUiVisibility);
    }

    /**
     * 获取状态栏高度
     *
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        int resourceId = context.getResources().getIdentifier(
                "status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = context.getResources().getDimensionPixelSize(resourceId);
        }
        if (statusBarHeight < 0) {
            int result = 0;
            try {
                Class<?> clazz = Class.forName("com.android.internal.R$dimen");
                Object obj = clazz.newInstance();
                Field field = clazz.getField("status_bar_height");
                int resourceId1 = Integer.parseInt(field.get(obj).toString());
                result = context.getResources().getDimensionPixelSize(resourceId1);
            } catch (Exception e) {
            } finally {
                statusBarHeight = result;
            }
        }
        return statusBarHeight;
    }

    /**
     * 显示危险区域
     *
     * @param window
     * @param height
     */
    public static void showFakeNotchView(Window window, int height) {
        View decorView = window.getDecorView();
        ViewGroup notchContainer = decorView.findViewWithTag("notch_container");
        if (notchContainer == null) {
            return;
        }

        if (notchContainer.getChildCount() == 0) {
            View view = new View(window.getContext());
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    height));
            view.setBackgroundColor(Color.BLACK);
            notchContainer.addView(view);
        }
        notchContainer.setVisibility(View.VISIBLE);
    }
}
