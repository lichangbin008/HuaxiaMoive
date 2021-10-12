/**
 * <p>
 * 标题：
 * </p>
 * <p>
 * 文件名称：BaseRecyclerViewAdapter.java
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
package com.suma.midware.huaxia.movie.adapter;

import java.util.ArrayList;
import java.util.List;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.suma.midware.huaxia.movie.listener.OnItemListener;

/**
 * <p>
 * 功能：通用RecyclerViewAdapter
 * </p>
 * <p>
 * 特点：
 * </p>
 */
public abstract class BaseRecyclerViewAdapter<D, VH extends BaseRecyclerViewAdapter.ViewHolder>
		extends RecyclerView.Adapter<VH> {

	private static final String TAG = "BaseRecyclerViewAdapter";

	/**
	 * 上下文
	 */
	protected final Context context;

	/**
	 * 布局加载器
	 */
	private final LayoutInflater inflater;

	/**
	 * 数据列表
	 */
	private List<D> datum = new ArrayList<>();

	/**
	 * item点击监听器
	 */
	protected OnItemListener onItemListener;

	/**
	 * 当前索引
	 */
	protected int curPos;

	/**
	 * 构造方法
	 * 
	 * @param context
	 */
	public BaseRecyclerViewAdapter(Context context) {
		this.context = context;

		//创建布局加载器
		inflater = LayoutInflater.from(context);
	}

	@Override
	public void onBindViewHolder(@NonNull final VH holder, final int position) {
		//处理item点击监听器
		if (onItemListener != null) {
			//给itemView设置点击事件
//			holder.itemView.setOnClickListener(new View.OnClickListener() {
//				/**
//				 * 点击回调事件
//				 * 
//				 * @param v
//				 */
//				@Override
//				public void onClick(View v) {
//					//回调监听接口
//					onItemListener.onItemClick(holder, position);
//				}
//			});

//			holder.itemView
//					.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//
//						@Override
//						public void onFocusChange(View v, boolean hasFocus) {
//							L.d(TAG, "onFocusChange hasFocus=%b", hasFocus);
//							curPos = holder.getPosition();
//							onItemListener.onFocusChannged(holder, hasFocus);
//						}
//					});
		}
	}

	@Override
	public int getItemCount() {
		return datum.size();
	}

	/**
	 * 获取当前位置数据
	 * 
	 * @param position
	 * @return
	 */
	public D getData(int position) {
		return datum.get(position);
	}

	/**
	 * 获取数据
	 * 
	 * @return
	 */
	public List<D> getDatum() {
		return datum;
	}

	/**
	 * 添加数据列表
	 * 
	 * @param datum
	 */
	public void setDatum(List<D> datum) {
		//清除原来的数据
		this.datum.clear();

		//添加数据
		this.datum.addAll(datum);

		//通知数据改变了
		//还有其他的通知方法
		//性能更好
		//详细的就不在深入讲解了
		//在《详解RecyclerView》课程中讲解了
		notifyDataSetChanged();
	}

	/**
	 * 添加数据列表
	 * 
	 * @param datum
	 */
	public void addDatum(List<D> datum) {
		//添加数据
		this.datum.addAll(datum);

		//刷新数据
		notifyDataSetChanged();
	}

	/**
	 * 添加数据
	 * 
	 * @param data
	 */
	public void addData(D data) {
		//添加数据
		this.datum.add(data);

		//刷新数据
		notifyDataSetChanged();
	}

	/**
	 * 删除指定位置的数据
	 * 
	 * @param position
	 */
	public void removeData(int position) {
		datum.remove(position);
		notifyItemRemoved(position);
	}

	/**
	 * 清除数据
	 */
	public void clearData() {
		datum.clear();
		notifyDataSetChanged();
	}

	/**
	 * 设置点击监听器
	 * 
	 * @param onItemListener
	 */
	public void setOnItemListener(OnItemListener onItemListener) {
		this.onItemListener = onItemListener;
	}

	/**
	 * 获取布局填充器
	 * 
	 * @return
	 */
	public LayoutInflater getInflater() {
		return inflater;
	}

	/**
	 * 获取当前位置
	 * 
	 * @return
	 */
	public int getCurPos() {
		return curPos;
	}

	/**
	 * 设置当前位置
	 * 
	 * @param curPos
	 */
	public void setCurPos(int curPos) {
		this.curPos = curPos;
	}

	/**
	 * 通用ViewHolder 主要是添加实现一些公共的逻辑
	 */
	public abstract static class ViewHolder<D> extends RecyclerView.ViewHolder {

		/**
		 * 构造方法
		 * 
		 * @param itemView
		 */
		public ViewHolder(@NonNull View itemView) {
			super(itemView);

		}

		/**
		 * 绑定数据
		 * 
		 * @param data
		 */
		public void bindData(D data) {

		}

		public void dealFocusGain(View view) {

		}

		public void dealFocusLost(View view) {

		}
	}
}
