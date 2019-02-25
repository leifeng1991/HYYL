package com.xxzlkj.zhaolinshare.chat;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhangrq
 * 
 */
public abstract class BaseCommonAdapter<T> extends BaseAdapter {

	protected Context mContext;
	protected List<T> mList;
	protected final int mLayoutId;

	public BaseCommonAdapter(Context context, List<T> list, int layoutId) {
		mContext = context;
		if (list == null)
			list = new ArrayList<T>();
		mList = list;
		mLayoutId = layoutId;
	}

	@Override
	public int getCount() {
		return mList == null ? 0:mList.size();
	}

	@Override
	public T getItem(int position) {
		return mList == null ? null:mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		BaseCommonViewHolder holder = getViewHolder(convertView, parent,
				position);
		convert(holder, mList.get(position), position);
		return holder.getConvertView();
	}

	public abstract void convert(BaseCommonViewHolder holder, T item,
			int position);

	private BaseCommonViewHolder getViewHolder(View convertView,
											   ViewGroup parent, int position) {
		return BaseCommonViewHolder.get(mContext, convertView, parent,
				mLayoutId, position);
	}


}
