package com.lcb.mnbigviewtest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.Scroller;

import androidx.annotation.Nullable;

import java.io.IOException;
import java.io.InputStream;

public class MNBigViewOld extends View implements GestureDetector.OnGestureListener,
        View.OnTouchListener {

    private static final String TAG = "MNBigView";

    /**
     * 位图配置
     */
    private BitmapFactory.Options mOptions;

    /**
     * 手势对象
     */
    private GestureDetector mGestureDetector;

    /**
     * 滚动对象
     */
    private Scroller mScroller;

    /**
     * 缩放手势对象
     */
    private ScaleGestureDetector mScaleGestureDetector;

    /**
     * 矩形区域
     */
    private Rect mRect;

    /**
     * 图片宽度
     */
    private int mImageWidth;

    /**
     * 图片高度
     */
    private int mImageHeight;
    /**
     * 区域解码器
     */
    private BitmapRegionDecoder mDecoder;

    /**
     * 视图宽度
     */
    private int mViewWidth;

    /**
     * 视图高度
     */
    private int mViewHeight;

    /**
     * 缩放比例
     */
    private float mScale;

    /**
     * 位图对象
     */
    private Bitmap mBitmap;

    /**
     * 原始缩放比例
     */
    private float originalScale;

    public MNBigViewOld(Context context) {
        this(context, null);
    }

    public MNBigViewOld(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MNBigViewOld(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        // 设置bigView的成员变量
        mRect = new Rect();
        mOptions = new BitmapFactory.Options();
        mGestureDetector = new GestureDetector(context, this);
        mScroller = new Scroller(context);
        setOnTouchListener(this);
    }

    public void setImage(InputStream is) {
        // 不传path mipmap url Bitmap
        // 获取图片的宽和高；
        mOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(is, null, mOptions);
        mImageWidth = mOptions.outWidth;
        mImageHeight = mOptions.outHeight;
        mOptions.inJustDecodeBounds = false;

        //开始复用
        mOptions.inMutable = true;

        //设置图片格式
        mOptions.inPreferredConfig = Bitmap.Config.RGB_565;
        // 图片由像素点组成；像素点由什么组成；argb 透明通道 红色 绿色 蓝色；
        // 创建区域解码器
        try {
            mDecoder = BitmapRegionDecoder.newInstance(is, false);
        } catch (IOException e) {
            e.printStackTrace();
        }

        requestLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mViewWidth = getMeasuredWidth();
        mViewHeight = getMeasuredHeight();

        // 确定图片的加载区域；
        mRect.left = 0;
        mRect.top = 0;
        mRect.right = mViewWidth;
        mScale = mViewWidth / (float) mImageWidth;
        mRect.bottom = (int) (mViewHeight / mScale);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mDecoder == null) {
            return;
        }
        //复用内存，谷歌提供的方法
        mOptions.inBitmap = mBitmap;
        mBitmap = mDecoder.decodeRegion(mRect, mOptions);
        Matrix matrix = new Matrix();
        matrix.setScale(mScale, mScale);
        canvas.drawBitmap(mBitmap, matrix, null);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return mGestureDetector.onTouchEvent(motionEvent);
//        return true;
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        //按下去的时候如果还在滚动则强制停止
        if (!mScroller.isFinished()) {
            mScroller.forceFinished(true);
        }
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float distanceX, float distanceY) {
        mRect.offset(0, (int) distanceY);
        //滑到底部
        if (mRect.bottom > mImageHeight) {
            mRect.top = mImageHeight - (int) (mViewHeight / mScale);
            mRect.bottom = mImageHeight;
        }

        //滑到顶部
        if (mRect.top < 0) {
            mRect.top = 0;
            mRect.bottom = (int) (mViewHeight / mScale);
        }

        invalidate();
        return false;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        //惯性处理
        mScroller.fling(0, mRect.top, 0, (int) -velocityY,
                0, 0, 0, mImageHeight - (int) (mViewHeight / mScale));
        return false;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        //滑动停止了，直接返回
        if (mScroller.isFinished()) {
            return;
        }
        if (mScroller.computeScrollOffset()) {
            mRect.top = mScroller.getCurrY();
            mRect.bottom = mRect.top + (int) (mViewHeight / mScale);
            invalidate();
        }
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }


    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }


}
