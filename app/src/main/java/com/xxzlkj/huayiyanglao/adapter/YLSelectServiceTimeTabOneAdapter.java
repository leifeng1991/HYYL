package com.xxzlkj.huayiyanglao.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;

import com.xxzlkj.huayiyanglao.R;
import com.xxzlkj.huayiyanglao.model.ProjectDetailBean1;
import com.xxzlkj.zhaolinshare.base.base.BaseAdapter;
import com.xxzlkj.zhaolinshare.base.base.BaseViewHolder;

/**
 * 描述:
 *
 * @author leifeng
 *         2018/3/9 10:22
 */


public class YLSelectServiceTimeTabOneAdapter extends BaseAdapter<ProjectDetailBean1.DataBean.ScheduleBean.TimeBean> {
    public int selectPosition = -1;

    public YLSelectServiceTimeTabOneAdapter(Context context, int itemId) {
        super(context, itemId);
    }

    @Override
    public void convert(BaseViewHolder holder, int position, ProjectDetailBean1.DataBean.ScheduleBean.TimeBean itemBean) {
        TextView mTimeTextView = holder.getView(R.id.tv_time);// 时间
        // 1：未预定 2：已预订
        String state = itemBean.getState();
        mTimeTextView.setTextColor(ContextCompat.getColor(mContext, ("2".equals(state) || selectPosition == position) ? R.color.white : R.color.text_4));
        mTimeTextView.setBackgroundResource("2".equals(state) ? com.xxzlkj.licallife.R.drawable.shape_rectangle_c8c8c8 : selectPosition == position ? R.drawable.shape_rectangle_solid_54b1f8 : R.drawable.shape_rectangle_stroke_c8);
        mTimeTextView.setText(String.format("%s-%s%s", itemBean.getTime(), itemBean.getEndtime(), "2".equals(state) ? "\n已预订" : ""));
    }
}
