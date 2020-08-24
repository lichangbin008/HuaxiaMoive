package com.lcb.study.vipcoursemainshitest.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.lcb.study.vipcoursemainshitest.R;

/**
 * Created by ${lichangbin} on 2020/8/24.
 */
public class MNView extends View {

    private Paint mPaint;
    private int color;
    private String text;
    private int mWidth;
    private int mHeight;

    // 计算字体需要的范围
    private Rect mTextBounds;  // drawText有一个基准线

    public MNView(Context context) {
        super(context);
    }

    public MNView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MNView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.MNView);
        color = ta.getInteger(R.styleable.MNView_mn_color, R.color.colorAccent);
        text = ta.getString(R.styleable.MNView_mn_text);
        ta.recycle();

        // 初始化画笔
        mPaint = new Paint();
        mTextBounds = new Rect();
        mPaint.setColor(color);
        mPaint.setTextSize(50);
        mPaint.getTextBounds(text, 0, text.length(), mTextBounds);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int specMode = MeasureSpec.getMode(widthMeasureSpec);
        int specWidth = MeasureSpec.getSize(widthMeasureSpec);
        System.out.println("onMeasure打印一下模式跟宽度" + MeasureSpec.toString(widthMeasureSpec));

        if (specMode == MeasureSpec.EXACTLY) {
            // 相当于子view指定了MatchParent或者说指定了具体的数值；
            mWidth = specWidth;
        } else {
            // 得到atmost模式  控件的大小应该跟着字体的大小来进行设定；
            mWidth = getPaddingLeft() + mTextBounds.width() + getPaddingRight();
        }

        specMode = MeasureSpec.getMode(heightMeasureSpec);
        int specHeight = MeasureSpec.getSize(heightMeasureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            mHeight = specHeight;
        } else {
            mHeight = getPaddingBottom() + mTextBounds.height() + getPaddingTop();
        }
        // 确定当前控件最终的大小
        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawText(text, getPaddingLeft() + 0, getPaddingTop() + mTextBounds.height(), mPaint);
    }
}
