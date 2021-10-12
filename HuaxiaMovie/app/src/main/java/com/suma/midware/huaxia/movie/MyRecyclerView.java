package com.suma.midware.huaxia.movie;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * 
 * <p>
 * 功能：自定义RecyclerView
 * </p>
 * <p>
 * 特点：
 * </p>
 */
public class MyRecyclerView extends RecyclerView {
	
	private int mSelectedPosition = 0;
	
	public MyRecyclerView(Context context) {

		super(context);
	}

	public MyRecyclerView(Context context, AttributeSet attrs) {

		super(context, attrs);
	}

	public MyRecyclerView(Context context, AttributeSet attrs, int defStyle) {

		super(context, attrs, defStyle);

		init();
	}

	/**
	 * 初始化
	 */
	private void init() {
		setChildrenDrawingOrderEnabled(true);
		setWillNotDraw(true); // 自身不作onDraw处理
		setHasFixedSize(true);
		setFocusable(true);
		setFocusableInTouchMode(true);
		setClipChildren(false);
		setClipToPadding(false);
	}
	@Override
	public void onDraw(Canvas arg0) {
		mSelectedPosition = getChildPosition(getFocusedChild());
		super.onDraw(arg0);
	}
	@Override
	protected int getChildDrawingOrder(int childCount, int i) {
		int position = mSelectedPosition;
        if (position < 0) {
            return i;
        } else {
            if (i == childCount - 1) {
                if (position > i) {
                    position = i;
                }
                return position;
            }
            if (i == position) {
                return childCount - 1;
            }
        }
        return i;

	}
	
	public void setSelection(int position) {
		if (null == getAdapter() || position < 0
				|| position >= getAdapter().getItemCount()) {
			return;
		}
		View view = getChildAt(position - getFirstVisiblePosition());
		if (null != view) {
			if (!hasFocus()) {
				onFocusChanged(true, FOCUS_DOWN, null);
			}
			view.requestFocus();
		}
	}
	
	public int getFirstVisiblePosition() {
		if (getChildCount() == 0)
			return 0;
		else
			return getChildPosition(getChildAt(0));
		
	}
	


}
