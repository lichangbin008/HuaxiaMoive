package com.suma.midware.huaxia.movie.mvp;

/**
 * MVP架构的View接口
 */
public interface IMvpView {

    MvpControler getMvpControler();

    /**
     * 显示加载视图
     */
    void showLoadingView();

    /**
     * 隐藏加载视图
     */
    void hideLoadingView();
}
