package com.suma.midware.huaxia.movie;

import java.lang.ref.WeakReference;

import com.suma.midware.huaxia.movie.listener.IUnifyMsgDealer;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

/**
 * <p>
 * 功能：统一消息循环处理器
 * </p>
 * <p>
 * 特点：
 * </p>
 */
public final class UnifyHandler<T extends IUnifyMsgDealer> extends Handler {

    /* 类标记 */
    private static final String TAG = "UnifyHandler";

    /* 消息处理器 */
    private final WeakReference<T> msgDealer;

    /**
     * 统一消息循环处理器构造函数
     *
     * @param msgDealer
     *            消息处理器
     */
    public UnifyHandler(T msgDealer) {
        super();
        this.msgDealer = new WeakReference<T>(msgDealer);
    }

    /**
     * 统一消息循环处理器构造函数
     *
     * @param callBack
     *            回调
     * @param msgDealer
     *            消息处理器
     */
    public UnifyHandler(Callback callBack, T msgDealer) {
        super(callBack);
        this.msgDealer = new WeakReference<T>(msgDealer);
    }

    /**
     * 统一消息循环处理器构造函数
     *
     * @param looper
     *            循环处理器
     * @param msgDealer
     *            消息处理器
     */
    public UnifyHandler(Looper looper, T msgDealer) {
        super(looper);
        this.msgDealer = new WeakReference<T>(msgDealer);
    }

    @Override
    public void handleMessage(Message msg) {
        T dealer = msgDealer.get();
        if (dealer == null) {
//            L.w(TAG, "handleMessage dealer already release,msg=%s", msg);
            return;
        }
        try {
            dealer.dealMessage(msg);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}

