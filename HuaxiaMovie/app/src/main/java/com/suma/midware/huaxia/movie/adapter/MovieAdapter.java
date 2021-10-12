package com.suma.midware.huaxia.movie.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.suma.midware.huaxia.movie.MovieInfo;
import com.suma.midware.huaxia.movie.R;

public class MovieAdapter extends BaseRecyclerViewAdapter<MovieInfo, BaseRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "MovieAdapter";

    /**
     * 构造方法
     *
     * @param context
     */
    public MovieAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MovieAdapter.MovieViewHolder(
                getInflater().inflate(R.layout.item_movie_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
    }

    /**
     * 电影列表的holder
     */
    public class MovieViewHolder extends BaseRecyclerViewAdapter.ViewHolder<MovieInfo> {


        /**
         * 构造方法
         *
         * @param itemView
         */
        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        public void bindData(MovieInfo data) {
            super.bindData(data);
        }

        @Override
        public void dealFocusGain(View view) {
            super.dealFocusGain(view);
        }

        @Override
        public void dealFocusLost(View view) {
            super.dealFocusLost(view);
        }
    }
}
