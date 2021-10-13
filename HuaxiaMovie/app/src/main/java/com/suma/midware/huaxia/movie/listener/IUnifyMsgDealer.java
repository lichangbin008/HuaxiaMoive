package com.suma.midware.huaxia.movie.listener;

import android.os.Message;

/**
 * <p>
 * 功能：统一消息处理接口
 * </p>
 * <p>
 * 特点：
 * </p>
 */
public interface IUnifyMsgDealer {

    /**
     * 处理消息
     *
     * @param msg
     *            消息
     */
    public void dealMessage(Message msg);
}
