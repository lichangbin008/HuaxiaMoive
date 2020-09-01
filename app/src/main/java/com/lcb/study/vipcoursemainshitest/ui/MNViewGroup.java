package com.lcb.study.vipcoursemainshitest.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * 自定义viewgroup
 */
public class MNViewGroup extends ViewGroup {

    private int mWidth;
    private int mHeight;

    public MNViewGroup(Context context) {
        super(context);
    }

    public MNViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MNViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        /**
         * 自定义ViewGroup时，应该从哪些方面进行考虑？
         * 以下代码，虽然都是在计算自定义viewgroup中的子view，但是这些子view的计算都是为了确定viewgroup的大小
         * **/
        int childCount = getChildCount();
        if (childCount == 0) {
            // viewgroup里没有任何子view，那么你设置多大，就显示多大；
            // 但是当你是wrap_content的时候，就直接设置为0
            setMeasuredDimension(measureWidthOrHeight(widthMeasureSpec), measureWidthOrHeight(heightMeasureSpec));
        } else {
            // 如果说，viewgroup指定的具体的值，resultSize就是你指定的值；
            // 如果viewGroup是wrap_content，resultSize就应该等于子view相加；
            int childViewsWidth = 0;
            int childViewsHeight = 0;

            int childViewsMarginLeft = 0;
            int childViewsMarginRight = 0;
            int childViewsMarginTop = 0;
            int childViewsMarginBottom = 0;

            for (int i = 0; i < childCount; i++) {
                View childView = getChildAt(i);
                // 循环子view，并且测量一下；
                measureChild(childView, widthMeasureSpec, heightMeasureSpec);
                MarginLayoutParams lp = (MarginLayoutParams) childView.getLayoutParams();
                childViewsWidth = Math.max(childViewsWidth, childView.getMeasuredWidth());// 得到子view中宽度最大的那个
                childViewsHeight += childView.getMeasuredHeight();// 得到所有子view的高度的和

                childViewsMarginTop += lp.topMargin;
                childViewsMarginBottom += lp.bottomMargin;
                // 计算最大的marginLeft的值
                childViewsMarginLeft = Math.max(childViewsMarginLeft, lp.leftMargin);
                childViewsMarginRight = Math.max(childViewsMarginRight, lp.rightMargin);
            }

            mWidth = childViewsWidth + childViewsMarginLeft + childViewsMarginRight;
            mHeight = childViewsHeight + childViewsMarginTop + childViewsMarginBottom;
            setMeasuredDimension(measureWidthOrHeight(widthMeasureSpec, mWidth),
                    measureWidthOrHeight(heightMeasureSpec, mHeight));
        }
    }

    private int measureWidthOrHeight(int measureSpec, int widthOrHeight) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = widthOrHeight;
        }
        return result;
    }


    private int measureWidthOrHeight(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = 0;
        }
        return result;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
//        int childCount = getChildCount();
//        for(int i=0;i<childCount;i++){
//            View childView = getChildAt(i);
//            childView.getWidth();
//            System.out.println("=====>"+childView.getMeasuredHeight()+" "+childView.getMeasuredWidth());
//            childView.layout(l,i*childView.getMeasuredHeight(),childView.getMeasuredWidth(),
//                    (i+1)*childView.getMeasuredHeight());
//        }
        // getMeasuredWidth 和 getWidth区别
        // 仅仅只是时机不同而已；
        int left, top, right, bottom;

        int countTop = 0;

        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            MarginLayoutParams lp = (MarginLayoutParams) childView.getLayoutParams();
            left = lp.leftMargin;
            top = countTop + lp.topMargin;
            right = left + childView.getMeasuredWidth();
            bottom = top + childView.getMeasuredHeight();
            childView.layout(left, top, right, bottom);
            // 下一个控件绘制的高度就应该是加上上一个控件的高度以及上一个控件它涉及的margin高度；
            countTop += ((bottom - top) + lp.topMargin + lp.bottomMargin);
        }
    }
}
