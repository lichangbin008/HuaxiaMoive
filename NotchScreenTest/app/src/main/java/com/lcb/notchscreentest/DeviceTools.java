package com.lcb.notchscreentest;

import android.os.Build;
import android.text.TextUtils;

/**
 * 设备工具类
 */
public class DeviceTools {

    /**
     * 是否是小米
     *
     * @return
     */
    public static final boolean isMiui() {
        String manufacturer = SystemProperties.getInstance().get("ro.miui.version.name");
        if (!TextUtils.isEmpty(manufacturer)) {
            return true;
        }
        return false;
    }

    /**
     * 是否是VIVO
     *
     * @return
     */
    public static final boolean isVivo() {
        String manufacturer = SystemProperties.getInstance().get("ro.vivo.os.name");
        if (!TextUtils.isEmpty(manufacturer)) {
            return true;
        }
        return false;
    }
//    oppo   华为   大部分

    /**
     * 是否是华为
     *
     * @return
     */
    public static boolean isHuaWei() {
        String manufacturer = Build.MANUFACTURER;
        if (!TextUtils.isEmpty(manufacturer)) {
            if (manufacturer.contains("HUAWEI")) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否是OPPO
     *
     * @return
     */
    public static final boolean isOppo() {
        String manufacturer = Build.MANUFACTURER;
        if ("oppo".equalsIgnoreCase(manufacturer)) {
            return true;
        }
        return false;
    }

    /**
     * 是否是三星
     *
     * @return
     */
    public static final boolean isSamsung() {
        String manufacturer = Build.MANUFACTURER;
        if ("samsung".equalsIgnoreCase(manufacturer)) {
            return true;
        }
        return false;
    }
}
