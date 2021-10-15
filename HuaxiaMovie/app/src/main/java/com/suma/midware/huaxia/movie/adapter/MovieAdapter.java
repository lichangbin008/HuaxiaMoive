package com.suma.midware.huaxia.movie.adapter;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.suma.midware.huaxia.movie.AutoScrollTextView;
import com.suma.midware.huaxia.movie.util.ImageUtil;
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

        //处理item点击监听器
        if (onItemListener != null) {
            //给itemView设置点击事件
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                /**
                 * 点击回调事件
                 *
                 * @param v
                 */
                @Override
                public void onClick(View v) {
                    //回调监听接口
                    onItemListener.onItemClick(holder, position);
                }
            });

            holder.itemView
                    .setOnFocusChangeListener(new View.OnFocusChangeListener() {

                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            onItemListener.onFocusChannged(holder, hasFocus);
                        }
                    });

            holder.itemView.setOnKeyListener(new View.OnKeyListener() {

                @Override
                public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                    return onItemListener.onItemKey(view, keyCode, keyEvent,
                            holder.getPosition());
                }
            });
        }
        MovieInfo data = getData(position);
        holder.bindData(data);
    }

    /**
     * 电影列表的holder
     */
    public class MovieViewHolder extends BaseRecyclerViewAdapter.ViewHolder<MovieInfo> {

        /**
         * 焦点
         */
        private ImageView mFocus;

        /**
         * 海报图片
         */
        private ImageView mPosterImg;

        /**
         * 影片名称
         */
//        private AutoScrollTextView mMovieName;

        /**
         * 子视图
         */
        private View mItemView;

        /**
         * 海报名称
         */
//        private String mPosterName;

        /**
         * 构造方法
         *
         * @param itemView
         */
        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            mFocus = itemView.findViewById(R.id.iv_focus);
            mPosterImg = itemView.findViewById(R.id.iv_poster);
//            mMovieName = itemView.findViewById(R.id.tv_name);
            mItemView = itemView.findViewById(R.id.ll_movie);
        }

        @Override
        public void bindData(MovieInfo data) {
            super.bindData(data);
//            mMovieName.setText(data.getDisplayName());
//            mMovieName.setGravity(Gravity.CENTER_HORIZONTAL);
            ImageUtil.show(context, mPosterImg, data.getPosterName());
        }

        @Override
        public void dealFocusGain(View view) {
            mFocus.setVisibility(View.VISIBLE);
//            mMovieName.reset(-1);
//            mMovieName.startScroll();
            ObjectAnimator scaleXAnim = new ObjectAnimator();
            scaleXAnim = ObjectAnimator.ofFloat(view, "scaleX", 1.0f, 1.08f);

            ObjectAnimator scaleYAnim = new ObjectAnimator();
            scaleYAnim = ObjectAnimator.ofFloat(view, "scaleY", 1.0f, 1.08f);
            AnimatorSet set = new AnimatorSet();
            set.play(scaleXAnim).with(scaleYAnim);
            set.setDuration(200);
            set.start();
        }

        @Override
        public void dealFocusLost(View view) {
            mFocus.setVisibility(View.GONE);
//            mMovieName.stopScroll();
//            mMovieName.reset(-1);
            ObjectAnimator scaleXAnim = new ObjectAnimator();
            scaleXAnim = ObjectAnimator.ofFloat(view, "scaleX", 1.08f, 1.0f);

            ObjectAnimator scaleYAnim = new ObjectAnimator();
            scaleYAnim = ObjectAnimator.ofFloat(view, "scaleY", 1.08f, 1.0f);

            AnimatorSet set = new AnimatorSet();
            set.play(scaleXAnim).with(scaleYAnim);
            set.setDuration(200);
            set.start();
        }
    }


}
