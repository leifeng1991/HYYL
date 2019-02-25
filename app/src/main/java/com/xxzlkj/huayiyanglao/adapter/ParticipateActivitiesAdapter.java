package com.xxzlkj.huayiyanglao.adapter;

import android.content.Context;
import android.widget.TextView;

import com.xxzlkj.huayiyanglao.R;
import com.xxzlkj.zhaolinshare.base.base.BaseAdapter;
import com.xxzlkj.zhaolinshare.base.base.BaseViewHolder;

/**
 * 描述:
 *
 * @author leifeng
 *         2017/12/2 14:34
 */


public class ParticipateActivitiesAdapter extends BaseAdapter<String> {
    public ParticipateActivitiesAdapter(Context context, int itemId) {
        super(context, itemId);
    }

    @Override
    public void convert(BaseViewHolder holder, int position, String itemBean) {
        TextView mTitleTextView = holder.getView(R.id.id_title);// 标题
        TextView mTimeTextView = holder.getView(R.id.id_time);// 时间
        TextView mBorwsingNumberTextView = holder.getView(R.id.id_browsing_number);// 浏览数量
        TextView mCollectNumberTextView = holder.getView(R.id.id_collect_number);// 收藏数量
        TextView mApplyNumberTextView = holder.getView(R.id.id_apply_number);// 报名数量
    }
}
