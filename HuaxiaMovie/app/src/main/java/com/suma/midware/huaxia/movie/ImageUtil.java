package com.suma.midware.huaxia.movie;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

/**
 *
 * <p>
 * 功能：图片显示工具类
 * </p>
 * <p>
 * 特点：
 * </p>
 */
public class ImageUtil {
    /**
     * 显示图片
     *
     * @param context
     *            上下文
     * @param view
     *            图片控件
     * @param uri
     *            地址
     */
    public static void show(Context context, ImageView view, String uri) {
        //获取公共配置
        RequestOptions options = getCommonRequestOptions();

        Glide.with(context).load(uri).apply(options).into(view);
    }

    /**
     * 获取公共配置
     *
     * @return
     */
    public static RequestOptions getCommonRequestOptions() {
        //创建配置选项
        RequestOptions options = new RequestOptions();

        // 站位符
        options.placeholder(R.mipmap.vod_posters);

        //出错后显示的图片
        //包括：图片不存在等情况
        options.error(R.mipmap.vod_posters);

        return options;
    }
}