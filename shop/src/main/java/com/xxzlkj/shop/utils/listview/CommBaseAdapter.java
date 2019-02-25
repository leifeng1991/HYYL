package com.xxzlkj.shop.utils.listview;

import android.content.Context;

import java.util.List;

/**
 * 通用适配器
 * Created by Administrator on 2017/5/15.
 */

public abstract class CommBaseAdapter<T> extends MultiItemTypeAdapter<T>{

    public CommBaseAdapter(Context context, final int layoutId, List<T> datas) {
        super(context, datas);

        addItemViewDelegate(new ItemViewDelegate<T>() {
            @Override
            public int getItemViewLayoutId() {
                return layoutId;
            }

            @Override
            public boolean isForViewType(T item, int position) {
                return true;
            }

            @Override
            public void convert(ViewHolder holder, T t, int position) {
                CommBaseAdapter.this.convert(holder, t, position);
            }
        });
    }

    protected abstract void convert(ViewHolder viewHolder, T item, int position);

    /***************************简单使用用例****************************************/
    /*CommBaseAdapter mAdapter = new CommBaseAdapter<String>(getApplicationContext(),R.layout.item_single_str, mDatas) {
        @Override
        protected void convert(ViewHolder viewHolder, String item) {
            viewHolder.setText(R.id.id_tv_title, item);
        }
    };*/
}
