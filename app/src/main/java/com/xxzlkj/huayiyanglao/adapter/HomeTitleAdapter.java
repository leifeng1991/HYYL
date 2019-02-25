package com.xxzlkj.huayiyanglao.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.xxzlkj.huayiyanglao.R;
import com.xxzlkj.huayiyanglao.model.HomeIndexBean;
import com.xxzlkj.huayiyanglao.model.TitleBean;
import com.xxzlkj.zhaolinshare.base.base.BaseAdapter;
import com.xxzlkj.zhaolinshare.base.base.BaseViewHolder;

/**
 * 描述:首页左边列表标题适配器
 *
 * @author leifeng
 *         2017/11/21 15:29
 */


public class HomeTitleAdapter extends BaseAdapter<HomeIndexBean.DataBean.IndexBean> {
    public int selectPosition = 0;

    public HomeTitleAdapter(Context context, int itemId) {
        super(context, itemId);
    }

    @Override
    public void convert(BaseViewHolder holder, int position, HomeIndexBean.DataBean.IndexBean itemBean) {
        TextView mTitleTextView = holder.getView(R.id.id_title);// 标题
        View mIndicatorView = holder.getView(R.id.id_indicator);// 指示器

        mTitleTextView.setText(itemBean.getTitle());

        if (selectPosition == position){
            mTitleTextView.setTextColor(0xff54B1F8);
            mIndicatorView.setVisibility(View.VISIBLE);
        }else {
            mTitleTextView.setTextColor(0xff888888);
            mIndicatorView.setVisibility(View.GONE);
        }

        mTitleTextView.setText(itemBean.getTitle());
    }
}
