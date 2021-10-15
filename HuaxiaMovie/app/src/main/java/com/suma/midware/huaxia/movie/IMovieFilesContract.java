package com.suma.midware.huaxia.movie;

import android.net.Uri;

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

        /**
         * 启动播放
         *
         * @param uri  uri
         * @param type 类型
         */
        void startPlay(Uri uri, String type);

        /**
         * 显示无列表
         */
        void showNoList();
    }

    /**
     * P层接口
     */
    interface IPrenseter extends ILifeCircle {

        /**
         * 加载本地文件
         */
        void loadLocalFile();

        /**
         * 获取文件URI
         *
         * @param position 索引
         */
        void getFileUri(int position);

        /**
         * 获取文件类型
         *
         * @param position 索引
         * @return
         */
        void getFileType(int position);
    }

    IView emptyView = new IView() {
        @Override
        public void updataMovieList(List<MovieInfo> list) {

        }

        @Override
        public void startPlay(Uri uri, String type) {

        }

        @Override
        public void showNoList() {

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
