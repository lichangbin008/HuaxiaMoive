package com.suma.midware.huaxia.movie;

import com.suma.midware.huaxia.movie.mvp.ILifeCircle;
import com.suma.midware.huaxia.movie.mvp.IMvpView;
import com.suma.midware.huaxia.movie.mvp.MvpControler;

import java.util.List;

public interface IMovieFilesContract {

    /**
     * V层接口
     */
    interface IView extends IMvpView {

        /**
         * 更新影片列表
         *
         * @param list
         */
        void updataMovieList(List<MovieInfo> list);
    }

    /**
     * P层接口
     */
    interface IPrenseter extends ILifeCircle {

        void loadLocalFile();
    }

    IView emptyView = new IView() {
        @Override
        public void updataMovieList(List<MovieInfo> list) {

        }

        @Override
        public MvpControler getMvpControler() {
            return null;
        }

        @Override
        public void showLoadingView() {

        }

        @Override
        public void hideLoadingView() {

        }
    };
}
