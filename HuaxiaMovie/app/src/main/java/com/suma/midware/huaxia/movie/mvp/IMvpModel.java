package com.suma.midware.huaxia.movie.mvp;

import com.suma.midware.huaxia.movie.mvp.model.BaseMvpModel;



/**
 * MVP架构M层接口
 */
public interface IMvpModel<M extends BaseMvpModel> {

    M getModelInstance();
}
