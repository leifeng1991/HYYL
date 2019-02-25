package com.xxzlkj.huayiyanglao.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.xxzlkj.huayiyanglao.R;
import com.xxzlkj.huayiyanglao.model.LocationHistoryTabBean;
import com.xxzlkj.huayiyanglao.model.LocationRecordListBean;
import com.xxzlkj.zhaolinshare.base.base.BaseAdapter;
import com.xxzlkj.zhaolinshare.base.base.BaseViewHolder;
import com.xxzlkj.zhaolinshare.base.util.DateUtils;
import com.xxzlkj.zhaolinshare.base.util.NumberFormatUtils;


/**
 * 描述:
 *
 * @author zhangrq
 *         2016/12/12 11:06
 */

public class LocationRecordAdapter extends BaseAdapter<LocationRecordListBean.DataBean> {
    public int selectionPosition = -1;

    public LocationRecordAdapter(Context context, int itemId) {
        super(context, itemId);
    }

    @Override
    public void convert(BaseViewHolder holder, int position, final LocationRecordListBean.DataBean itemBean) {
        TextView tv_left = holder.getView(R.id.tv_left);
        TextView tv_right = holder.getView(R.id.tv_right);
        View view_line = holder.getView(R.id.view_line);
        // 设置值
        holder.itemView.setBackgroundColor(ContextCompat.getColor(mContext, selectionPosition == position ? R.color.blue_54B1F8 : R.color.white));
        long addTime = NumberFormatUtils.toLong(itemBean.getAddtime()) * 1000;
        tv_left.setText(DateUtils.getYearMonthDay(addTime, "HH:mm"));
        tv_right.setText(itemBean.getAddress());
        // 设置线
        view_line.setVisibility(position == getItemCount() - 1 ? View.GONE : View.VISIBLE);
    }

}


