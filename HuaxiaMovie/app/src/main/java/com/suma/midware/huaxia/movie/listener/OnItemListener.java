/**
 * <p>
 * 标题：
 * </p>
 * <p>
 * 文件名称：OnItemClickListener.java
 * </p>
 * <p>
 * 说明：
 * </p>
 * <p>
 * 版权申明：
 *
 * Copyright  1990-2011 SumaVision, Inc. All Rights Reserved.
 * 
 * Without prior written consent by SumaVision,any reproduction,modification,
 * storage in a retrieval system or retransmission,or tied sale with other products 
 * in any form or by any means,is illegal and strictly prohibited. 
 *
 * </p>
 * <p>
 * 创建时间：2021年1月8日
 * </p>
 * <p>
 * 创建者：Administrator
 * </p>
 * <p>
 * 公司：北京数码视讯软件技术发展有限公司
 * </p>
 */
package com.suma.midware.huaxia.movie.listener;

import android.view.KeyEvent;
import android.view.View;

import com.suma.midware.huaxia.movie.adapter.BaseRecyclerViewAdapter;


/**
 * <p>
 * 功能：Adapter的item点击事件监听器
 * </p>
 * <p>
 * 特点：
 * </p>
 */
public interface OnItemListener {
	/**
	 * item点击事件
	 * 
	 * @param holder
	 *            点击的ViewHolder
	 * @param position
	 *            点击的位置
	 */
	void onItemClick(BaseRecyclerViewAdapter.ViewHolder holder, int position);

	/**
	 * 焦点改变
	 * 
	 * @param holder
	 * @param hasFocus
	 */
	void onFocusChannged(BaseRecyclerViewAdapter.ViewHolder holder,
			boolean hasFocus);
	
	boolean onItemKey(View view, int keyCode, KeyEvent keyEvent, int position);

}
