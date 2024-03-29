package com.suma.midware.huaxia.movie;


import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.suma.midware.huaxia.movie.adapter.BaseRecyclerViewAdapter;
import com.suma.midware.huaxia.movie.adapter.MovieAdapter;
import com.suma.midware.huaxia.movie.listener.OnItemListener;
import com.suma.midware.huaxia.movie.mvp.view.LifeCircleMvpActivity;

import java.util.List;

public class MovieActivity extends LifeCircleMvpActivity implements IMovieFilesContract.IView {

    private static final String TAG = "MovieActivity";

    /**
     * 影片列表P层对象
     */
    private IMovieFilesContract.IPrenseter mPresenter;

    /**
     * 列表组件对象
     */
    private MyRecyclerView mMovieList;

    /**
     * 布局管理器
     */
    private GridLayoutManager mLayoutManager;

    /**
     * 影片列表适配器
     */
    private MovieAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        mPresenter = new MoviePresenter(this);

        initView();
        initData();
        initListener();

        mPresenter.loadLocalFile();
    }

    private void initView() {

        mMovieList = findViewById(R.id.rv_movie);
        mLayoutManager = new GridLayoutManager(
                this, 5, LinearLayoutManager.VERTICAL, false);
        mMovieList.setLayoutManager(mLayoutManager);
    }

    private void initData() {
        mAdapter = new MovieAdapter(MovieActivity.this);
        mMovieList.setAdapter(mAdapter);
    }

    private void initListener() {
        mAdapter.setOnItemListener(new OnItemListener() {
            @Override
            public void onItemClick(BaseRecyclerViewAdapter.ViewHolder holder, int position) {
                mPresenter.getFileUri(position);
                mPresenter.getFileType(position);
            }

            @Override
            public void onFocusChannged(BaseRecyclerViewAdapter.ViewHolder holder, boolean hasFocus) {
                if (hasFocus) {
                    holder.dealFocusGain(holder.itemView);
                } else {
                    holder.dealFocusLost(holder.itemView);
                }
            }

            @Override
            public boolean onItemKey(View view, int keyCode, KeyEvent keyEvent, int position) {
                return false;
            }
        });
    }

    @Override
    public void updataMovieList(List<MovieInfo> list) {
        mAdapter.clearData();
        mAdapter.addDatum(list);
        mMovieList.setSelection(0);
    }

    @Override
    public void startPlay(Uri uri, String type) {
        ComponentName toActivity = new ComponentName(
                "com.suma.midware.player.demo",
                "com.suma.midware.player.demo.MainActivity");
        Intent intentVlc = new Intent();
        intentVlc.setComponent(toActivity);
        intentVlc.setDataAndType(uri, type);
        intentVlc.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intentVlc);
    }

    @Override
    public void showNoList() {
        Toast.makeText(MovieActivity.this,"没有加载到影片！",Toast.LENGTH_SHORT).show();
    }
}