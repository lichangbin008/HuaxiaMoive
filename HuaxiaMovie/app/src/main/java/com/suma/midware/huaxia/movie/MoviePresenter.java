package com.suma.midware.huaxia.movie;

import com.suma.midware.huaxia.movie.mvp.base.BaseMvpPresenter;

import java.util.ArrayList;
import java.util.List;

public class MoviePresenter extends BaseMvpPresenter<IMovieFilesContract.IView>
        implements IMovieFilesContract.IPrenseter {

    private static final String TAG = "MoviePresenter";

    private List<MovieInfo> mMovieList;
    public MoviePresenter(IMovieFilesContract.IView view) {
        super(view);

        mMovieList = new ArrayList<>();
    }

    @Override
    protected IMovieFilesContract.IView getEmptyView() {
        return IMovieFilesContract.emptyView;
    }

    @Override
    public void loadLocalFile() {
        //加载本地文件


        //更新影片列表
        for (int i = 0;i < 10;i++){
            MovieInfo movieInfo = new MovieInfo();
            String index = String.valueOf(i+1);
            movieInfo.setmDisplayName("边境迷雾" + index);
            mMovieList.add(movieInfo);
        }

        getView().updataMovieList(mMovieList);
    }
}
