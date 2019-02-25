package com.xxzlkj.shop.weight;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.xxzlkj.shop.adapter.RecyclerWrapAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 带有头部和尾部的recyclerview
 * Created by Administrator on 2017/3/13.
 */

public class HeaderRecyclerView extends RecyclerView {
    public List<View> mHeaderViews = new ArrayList<>();
    public List<View> mFooterViews = new ArrayList<>();
    public Adapter mAdapter;

    public HeaderRecyclerView(Context context) {
        super(context);
    }

    public HeaderRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public HeaderRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void addHeaderView(View view){
        mHeaderViews.clear();
        mHeaderViews.add(view);
        if(mAdapter!=null){
            if(!(mAdapter instanceof RecyclerWrapAdapter)){
                mAdapter = new RecyclerWrapAdapter(mHeaderViews,mFooterViews,mAdapter);
            }
        }
    }

    public void addFooterView(View view){
        mFooterViews.clear();
        mFooterViews.add(view);
        if(mAdapter!=null){
            if(!(mAdapter instanceof RecyclerWrapAdapter)){
                mAdapter = new RecyclerWrapAdapter(mHeaderViews,mFooterViews,mAdapter);
            }
        }
    }

    public void removeFooterView(View view){
        mFooterViews.clear();
        mFooterViews.remove(view);
        if(mAdapter!=null){
            if(!(mAdapter instanceof RecyclerWrapAdapter)){
                mAdapter = new RecyclerWrapAdapter(mHeaderViews,mFooterViews,mAdapter);
            }
        }
    }

    public void setAdapter(Adapter adapter){
        if (mHeaderViews.isEmpty() && mFooterViews.isEmpty()){
            super.setAdapter(adapter);
        }else {
            adapter = new RecyclerWrapAdapter(mHeaderViews,mFooterViews,adapter) ;
            super.setAdapter(adapter);
        }
        mAdapter = adapter ;
    }

    public void addDate(int position){
        if (mHeaderViews.isEmpty()){
            mAdapter.notifyItemInserted(position);
            // 加入如下代码保证position的位置正确性
            if (position != mAdapter.getItemCount() - 1) {
                mAdapter.notifyItemRangeChanged(position, mAdapter.getItemCount() - position);
            }
        }else {
            mAdapter.notifyItemInserted(position+mHeaderViews.size());
        }

    }

    public void removeDate(int position){
        if (!mFooterViews.isEmpty()){
            mAdapter.notifyItemRemoved(position);
            // 加入如下代码保证position的位置正确性
            if (position != mAdapter.getItemCount() - 1) {
                mAdapter.notifyItemRangeChanged(position, mAdapter.getItemCount() - 1 - position);
            }
        }

    }
}
