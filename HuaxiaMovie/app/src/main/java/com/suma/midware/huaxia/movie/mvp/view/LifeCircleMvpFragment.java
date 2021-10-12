/**
 * <p>
 * 标题：
 * </p>
 * <p>
 * 文件名称：LifeCircleMvpFragment.java
 * </p>
 * <p>
 * 说明：
 * </p>
 * <p>
 * 版权申明：
 *
 * Copyright  1990-2011 SumaVision, Inc. All Rights Reserved.
 * 
 * Without prior written consent by SumaVision,any reproduction,modification,
 * storage in a retrieval system or retransmission,or tied sale with other products 
 * in any form or by any means,is illegal and strictly prohibited. 
 *
 * </p>
 * <p>
 * 创建时间：2021年1月4日
 * </p>
 * <p>
 * 创建者：Administrator
 * </p>
 * <p>
 * 公司：北京数码视讯软件技术发展有限公司
 * </p>
 */
package com.suma.midware.huaxia.movie.mvp.view;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.suma.midware.huaxia.movie.mvp.IMvpView;
import com.suma.midware.huaxia.movie.mvp.MvpControler;

/**
 * <p>
 * 功能：Fragment生命周期MPV界面
 * </p>
 * <p>
 * 特点：
 * </p>
 */
public class LifeCircleMvpFragment extends Fragment implements IMvpView{

	//MVP控制者对象
    private MvpControler mvpControler;

    @Override
    public MvpControler getMvpControler() {
        if (this.mvpControler == null) {
            this.mvpControler = new MvpControler();
        }
        return mvpControler;
    }

    @Override
    public void showLoadingView() {

    }

    @Override
    public void hideLoadingView() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle == null) {
            bundle = new Bundle();
        }
        MvpControler mvpControler = this.getMvpControler();
        if (mvpControler != null) {
            mvpControler.onCreate(savedInstanceState, null, bundle);
            mvpControler.onActivityCreated(savedInstanceState, null, bundle);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle == null) {
            bundle = new Bundle();
        }
        MvpControler mvpControler = this.getMvpControler();
        if (mvpControler != null) {
            mvpControler.onActivityCreated(savedInstanceState, null, bundle);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        MvpControler mvpControler = this.getMvpControler();
        if (mvpControler != null) {
            mvpControler.onDestroyView();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        MvpControler mvpControler = this.getMvpControler();
        if (mvpControler != null) {
            mvpControler.onStart();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        MvpControler mvpControler = this.getMvpControler();
        if (mvpControler != null) {
            mvpControler.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        MvpControler mvpControler = this.getMvpControler();
        if (mvpControler != null) {
            mvpControler.onPause();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        MvpControler mvpControler = this.getMvpControler();
        if (mvpControler != null) {
            mvpControler.onStop();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MvpControler mvpControler = this.getMvpControler();
        if (mvpControler != null) {
            mvpControler.onDestroy();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        MvpControler mvpControler = this.getMvpControler();
        if (mvpControler != null) {
            mvpControler.onSaveInstanceState(outState);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        MvpControler mvpControler = this.getMvpControler();
        if (mvpControler != null) {
            mvpControler.onActivityResult(requestCode, resultCode, data);
        }
    }
}
