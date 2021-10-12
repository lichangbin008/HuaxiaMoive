package com.suma.midware.huaxia.movie.mvp.model;

import com.suma.midware.huaxia.movie.mvp.base.BaseMvpPresenter;


/**
 * 基础M层
 */
public abstract class BaseMvpModel<P extends BaseMvpPresenter> {

    protected P mPresenter;

    /**
     * 构造方法
     * @param mPresenter
     */
    public BaseMvpModel(P mPresenter) {
        this.mPresenter = mPresenter;
    }
}
