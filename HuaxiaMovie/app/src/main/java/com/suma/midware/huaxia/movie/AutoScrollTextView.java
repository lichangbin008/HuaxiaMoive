package com.suma.midware.huaxia.movie;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Message;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.suma.midware.huaxia.movie.listener.IUnifyMsgDealer;


/**
 * <p>
 * 功能：单行跑马灯文本组件
 * </p>
 * <p>
 * 特点：
 * </p>
 */
public class AutoScrollTextView extends TextView implements IUnifyMsgDealer {

    /* 刷新消息 */
    private static final int MSG_REFRESH = 0xFF00;

    /* 文本长度 */
    private float textLength = 0f;

    /* 文字的横坐标 */
    private float step = 0f;

    /* 绘图样式 */
    private Paint paint = null;

    /* 绘矩形样式 */
    private Paint bgPaint = new Paint();

    /* 文本内容 */
    private String text = "";

    /* 当前动画监听器 */
    private AutoScrollViewListener mListener = null;

    /* 控件移动速度 */
    private float speed = 1.0f;

    /* 滚动次数,默认为不限次数滚动 */
    private int count = -1;

    /* 控件背景 */
    private int bgColor = Color.TRANSPARENT;

    /* 起始偏移量 */
    private int offset = 0;

    /* 滚动类型 0 滚动完再继续 1-加空格继续 */
    private int scrolType = 1;

    /* 背景显示类型 0 跟随移动 1 铺满组件 */
    private int backGroundDrawType = 0;

    /* 是否强制滚动 */
    private boolean isForthScrol = false;

    /* 是否执行滚动 */
    private boolean isScrol = true;

    /* 空格个数 */
    private String spaces = "   ";

    /* 空格长度 */
    private float spaceLength = 0;

    /* 空格模式的变换值 */
    private float spaceStep = 0f;

    /* 是否第一次绘制 */
    private boolean normalDraw = true;

    /* 第一次绘制的延迟时间，单位是毫秒 */
    private int firstDelay = 0;

    /* 状态 0 空闲 1 运行 2 停止 */
    protected int state = 0;

    /* 当前位置 */
    protected int curPosition = 0;

    /* 消息循环处理器 */
    private UnifyHandler<AutoScrollTextView> handler = new UnifyHandler<AutoScrollTextView>(
            this);

    /**
     * 单行跑马灯文本组件构造函数
     *
     * @param context
     *            上下文
     */
    public AutoScrollTextView(Context context) {
        this(context, null);
    }

    /**
     * 单行跑马灯文本组件构造函数
     *
     * @param context
     *            上下文
     * @param attrs
     *            属性集合
     */
    public AutoScrollTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * 单行跑马灯文本组件构造函数
     *
     * @param context
     *            上下文
     * @param attrs
     *            属性集合
     * @param defStyle
     *            样式
     */
    public AutoScrollTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        paint = getPaint();
        paint.setColor(getCurrentTextColor());
        paint.setTextSize(getTextSize());

        setGravity(Gravity.LEFT | Gravity.TOP);
    }

    /**
     * 开始滚动
     */
    public synchronized void startScroll() {
        startScroll(1000);
    }

    /**
     * 开始滚动
     */
    public void startScroll(int delay) {
        boolean isFirst = false;
        if (state != 1) { // 非运行
            state = 1; // 运行
            isFirst = true;
            this.firstDelay = delay;
        }
        normalDraw = false;
        handler.removeMessages(MSG_REFRESH);
        handler.sendEmptyMessageDelayed(MSG_REFRESH,
                isFirst && firstDelay > 0 ? firstDelay : 50);
        if (isFirst && mListener != null) {
            mListener.onAutoScrollViewStart(this);
        }
        firstDelay = 0;
        invalidate();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopScroll();
    }

    @Override
    public void dealMessage(Message msg) {
        if (state != 1) { // 非运行状态
            state = 0; // 空闲状态
            return;
        }
        if (isScrol) {
            postInvalidate();
        }
        handler.sendEmptyMessageDelayed(MSG_REFRESH, 50);
    }

    /**
     * 停止滚动
     */
    public synchronized void stopScroll() {
        stopScroll(true);
    }

    /**
     * 停止滚动
     *
     * @param isReset
     *            是否重置位置
     */
    public synchronized void stopScroll(boolean isReset) {
        handler.removeMessages(MSG_REFRESH);
        state = 2; // 停止状态
        normalDraw = true;
        firstDelay = 0;
        if (isReset) {
            reset(0);
        }
    }

    /**
     * 重置组件
     *
     * @param curPosition
     *            当前位置
     */
    public synchronized void reset(int curPosition) {
        spaceStep = 0f;
        this.curPosition = curPosition;
        if (curPosition == -1) {
            step = textLength - 2;
            step += offset;
        } else {
            step = curPosition;
        }
    }

    /**
     * 先绘制背景在绘制字符； 背景的长度画法 模型如下：
     *
     * <pre>
     * |<------------viewTextLen-->|
     * |<----------------------view2TextLen----->|
     * |<--textLength-->|<---- viewWidth--------->|<--textLength-->|
     *
     * |----------------||-----------------------||----------------|    --   <---getCompoundPaddingTop()
     * |                ||                       ||                |     |
     * |                ||                       ||                |    intener
     * |                ||                       ||                |     |
     * |----------------||-----------------------||----------------|    --    <--- y
     *
     * </pre>
     */

    @SuppressLint("DrawAllocation")
    public void onDraw(Canvas canvas) {
        if (null == canvas || null == bgPaint || null == paint) {
            return;
        } // 防止在特殊情况下出现错误 导致程序崩溃
        canvas.save();
        Rect clipRect = canvas.getClipBounds();
        canvas.translate(clipRect.left, clipRect.top);

        init();

        float textSize = getTextSize();
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();

        int paddingTop = getCompoundPaddingTop();
        int paddingLeft = getCompoundPaddingLeft();
        int gravity = getGravity();

        float viewTextLen = width + textLength;
        float view2TextLen = width + textLength * 2;
        float xOffset = 0;
        switch (gravity & Gravity.HORIZONTAL_GRAVITY_MASK) {
            case Gravity.LEFT:
                xOffset = paddingLeft;
                break;
            case Gravity.RIGHT:
                xOffset = width - textLength;
                break;
            case Gravity.CENTER_HORIZONTAL:
                xOffset = (width - textLength) / 2;
                break;
        }
        float yOffset = 0;

        switch (gravity & Gravity.VERTICAL_GRAVITY_MASK) {
            case Gravity.TOP:
                yOffset = paddingTop - paint.ascent();
                break;
            case Gravity.BOTTOM:
                yOffset = height - textSize - paint.ascent();
                break;
            case Gravity.CENTER_VERTICAL:
                yOffset = (height - textSize) / 2 - paint.ascent();
                break;
        }
        if (xOffset < 0) {
            xOffset = 0;
        }

        bgPaint.setColor(bgColor);
        isScrol = isForthScrol || textLength >= width;
        float rectHeight = 2 * textSize + paddingTop;

        label: {
            if (scrolType == 0) {
                if (backGroundDrawType == 0) {
                    if (viewTextLen - step > 0) {
                        canvas.drawRect(viewTextLen - step - textSize,
                                paddingTop, textLength < width ? (view2TextLen
                                        - step + textSize) : width, rectHeight,
                                bgPaint);
                        // 这个是画字幕从刚出来到字幕到控件的左边框前的背景
                    } else if ((step > textLength * 2) && step >= viewTextLen) {
                        canvas.drawRect(0, paddingTop, view2TextLen - step
                                + textSize, rectHeight, bgPaint);
                        // 画字幕结束部分 到字幕控件的右边框 到消失在屏幕那一段的 背景
                    } else {
                        if (textLength > width)
                            canvas.drawRect(0, paddingTop, width, rectHeight,
                                    bgPaint);
                    }
                } else {
                    canvas.drawRect(0, 0, width, height, bgPaint);
                }
                canvas.drawText(text, viewTextLen - step, yOffset, paint);

                if (state != 1) {
                    break label;
                }
                step += speed;
                if (step > view2TextLen + textSize) {
                    if (--count == 0) {
                        step += textSize;
                        curPosition = -1;
                        stopScroll();
                        if (mListener != null) {
                            mListener.onAutoScrollViewEnd(this);
                        }
                        break label;
                    }

                    step = textLength;
                    step += offset;
                    curPosition = (int) step;
                    if (mListener != null) {
                        mListener.onAutoScrollViewRepeat(this);
                    }
                } else {
                    curPosition = (int) step;
                }
            } else if (scrolType == 1) {
                if (isScrol && !normalDraw) {
                    if (backGroundDrawType == 0) {
                        canvas.drawRect(0, paddingTop, width, rectHeight,
                                bgPaint);
                    } else {
                        canvas.drawRect(0, 0, width, height, bgPaint);
                    }
                    canvas.drawText(text + spaces, -spaceStep, yOffset, paint);
                    canvas.drawText(text, -spaceStep + spaceLength, yOffset,
                            paint);
                    if (state != 1) {
                        if (mListener != null) {
                            mListener.onAutoScrollViewEnd(this);
                        }
                        break label;
                    }
                    spaceStep += speed;
                    if (spaceStep >= spaceLength) {
                        if (--count == 0) {
                            spaceStep = speed;
                            stopScroll();
                            if (mListener != null) {
                                mListener.onAutoScrollViewRepeat(this);
                            }
                            break label;
                        }
                        spaceStep = speed;
                        if (mListener != null) {
                            mListener.onAutoScrollViewRepeat(this);
                        }
                    }
                } else {
                    Rect rect = canvas.getClipBounds();
                    canvas.drawRect(0, paddingTop, width, rectHeight, bgPaint);
                    if (width < textLength) {
                        canvas.drawText(dealText(), xOffset, yOffset, paint);
                        canvas.clipRect(rect);
                    } else {
                        canvas.drawText(text, xOffset, yOffset, paint);
                        canvas.clipRect(rect);
                    }
                }
            }
        }
        canvas.restore();
    }

    /**
     * 文本初始化，每次更改文本内容或者文本效果等之后都需要重新初始化一下
     */
    private void init() {
        text = getText().toString();
        textLength = paint.measureText(text);
        spaceLength = paint.measureText(text + spaces);
        if (curPosition == -1) {
            step = textLength - 2;
            step += offset;
            curPosition = (int) step;
        } else {
            step = curPosition;
        }
    }

    /**
     * 截取字符串,解决显示半个字的bug
     *
     * @return
     */
    private String dealText() {
        String textTemp = "";
        if (text.length() == 0) {
            return text;
        }
        int width = getWidth();
        for (int i = 0; i < text.length(); i++) {
            textTemp = text.substring(0, i);
            if (paint.measureText(textTemp) > width) {
                if (--i > 0) {
                    textTemp = text.substring(0, i);
                }
                break;
            }

        }
        return textTemp;
    }

    /**
     * @return step
     */
    public float getStep() {
        return step;
    }

    /**
     * 设置对其方式
     *
     * @return showStyle 0 中间对齐 1 右对齐 2 左对齐 其它 偏移量
     */
    public void setShowStyle(int showStyle) {
        int gravity = getGravity() & Gravity.VERTICAL_GRAVITY_MASK;
        if (showStyle == 0) {
            setGravity(gravity | Gravity.CENTER_HORIZONTAL);
        } else if (showStyle == 1) {
            setGravity(gravity | Gravity.RIGHT);
        } else if (showStyle == 2) {
            setGravity(gravity | Gravity.LEFT);
        } else {
            setPadding(showStyle, 0, 0, 0);
        }
    }

    /**
     * 设置当前位置
     *
     * @param curPosition
     *            当前位置
     */
    public void setCurrentStep(int curPosition) {
        this.curPosition = curPosition;
    }

    /**
     * 设置监听器
     *
     * @param listener
     *            监听器
     */
    public void setAutoScrollViewListener(AutoScrollViewListener listener) {
        mListener = listener;
    }

    /**
     * 设置字幕滚动的速度 speed 必须大于0
     */
    public void setSpeed(float speed) {
        if (speed > 0) {
            this.speed = speed;
        }
    }

    /**
     * @param offset
     *            要设置的 offset
     */
    public void setOffset(int offset) {
        this.offset = offset;
    }

    @Override
    public void setBackgroundColor(int color) {
        super.setBackgroundColor(color);
        this.bgColor = color;
    }

    @Override
    public void setTextColor(int color) {
        super.setTextColor(color);
        paint.setColor(color);
    }

    @Override
    public void setTextSize(float size) {
        super.setTextSize(size);
        paint.setTextSize(getTextSize());
    }

    @Override
    public void setTextSize(int unit, float size) {
        super.setTextSize(unit, size);
        paint.setTextSize(getTextSize());
    }

    /**
     * 设置滚动次数
     *
     * @param count
     *            整数
     */
    public void setCount(int count) {
        this.count = count;
    }

    /**
     * @param scrolType
     *            要设置的 scrolType
     */
    public void setScrolType(int scrolType) {
        this.scrolType = scrolType;
    }

    /**
     * @param isForthScrol
     *            要设置的 isForthScrol
     */
    public void setForthScrol(boolean isForthScrol) {
        this.isForthScrol = isForthScrol;
    }

    /**
     * 设置背景显示类型
     *
     * @param backGroundDrawType
     *            背景显示类型
     */
    public void setBackGroundDrawType(int backGroundDrawType) {
        this.backGroundDrawType = backGroundDrawType;
    }

    @Override
    public Parcelable onSaveInstanceState() {
        try {
            Parcelable parcelable = super.onSaveInstanceState();
            if (parcelable == null) {
                return null;
            }
            SavedState saveState = new SavedState(parcelable);
            saveState.state = state;
            saveState.curPosition = curPosition;
            return saveState;
        } catch (Throwable e) {
        }
        return null;
    }

    @Override
    public void onRestoreInstanceState(Parcelable parcelable) {
        try {
            if (!(parcelable instanceof SavedState)) {
                super.onRestoreInstanceState(parcelable);
                return;
            }
            SavedState saveState = (SavedState) parcelable;
            super.onRestoreInstanceState(saveState);
            state = saveState.state;
            curPosition = saveState.curPosition;
        } catch (Throwable e) {
        }
    }

    /**
     *
     * <p>
     * 功能： 单行跑马灯文本组件保存的状态
     * </p>
     * <p>
     * 特点：
     * </p>
     */
    public static class SavedState extends View.BaseSavedState {

        /* 文本组件状态 */
        protected int state = 0;

        /* 文本组件当前显示位置 */
        public int curPosition = 0;

        /**
         * 单行跑马灯文本组件保存的状态构造函数
         *
         * @param parcel
         *            序列化对象
         */
        public SavedState(Parcelable parcel) {
            super(parcel);
        }

        /**
         * 单行跑马灯文本组件保存的状态构造函数
         *
         * @param parcel
         *            序列化接口
         */
        public SavedState(Parcel parcel) {
            super(parcel);
            try {
                state = parcel.readInt();
                curPosition = parcel.readInt();
            } catch (Throwable e) {
            }
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            try {
                super.writeToParcel(out, flags);
                out.writeInt(state);
                out.writeInt(curPosition);
            } catch (Throwable e) {
            }
        }

        /**
         * 保存状态创建者
         */
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {

            @Override
            public SavedState[] newArray(int size) {
                try {
                    return new SavedState[size];
                } catch (Throwable e) {
                }
                return null;
            }

            @Override
            public SavedState createFromParcel(Parcel in) {
                try {
                    return new SavedState(in);
                } catch (Throwable e) {
                }
                return null;
            }
        };

    }

    /**
     *
     * <p>
     * 功能：当前跑马灯文本组件的动画监听器
     * </p>
     * <p>
     * 特点：
     * </p>
     */
    public static interface AutoScrollViewListener {

        /**
         * 通知跑马灯文本组件开始运行
         *
         * @param view
         *            跑马灯文本组件
         */
        void onAutoScrollViewStart(AutoScrollTextView view);

        /**
         * 通知跑马灯文本组件运行结束
         *
         * @param view
         *            跑马灯文本组件
         */
        void onAutoScrollViewEnd(AutoScrollTextView view);

        /**
         * 通知跑马灯文本组件循环开始执行
         *
         * @param view
         *            跑马灯文本组件
         */
        void onAutoScrollViewRepeat(AutoScrollTextView view);
    }

}

