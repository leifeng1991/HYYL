package com.xxzlkj.licallife.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;

import com.xxzlkj.licallife.R;
import com.xxzlkj.licallife.model.FilterBean;
import com.xxzlkj.zhaolinshare.base.base.BaseAdapter;
import com.xxzlkj.zhaolinshare.base.base.BaseViewHolder;


/**
 * 描述: 保姆或月嫂列表adapter
 *
 * @author zhangrq
 *         2017/7/25 15:06
 */
public class DrawerItemListAdapter extends BaseAdapter<FilterBean.DataBean.DataItemBean> {
    private int selectPosition = -1;
    private int itemWidth;

    public DrawerItemListAdapter(Context context, int itemId) {
        super(context, itemId);
    }

    @Override
    public void convert(BaseViewHolder holder, int position, FilterBean.DataBean.DataItemBean itemBean) {
        if (itemWidth != 0) {
            holder.itemView.setMinimumWidth(itemWidth);
        }
        TextView tv_title = holder.getView(R.id.tv_title);// 标题
        tv_title.setText(itemBean.getTitle());
        tv_title.setTextColor(selectPosition == position ? Color.RED : Color.BLACK);
        // 设置选中
        tv_title.setBackgroundResource(selectPosition == position ? R.drawable.shape_buy_now : R.drawable.shape_rectangle_orange_ff725c);// 设置选中
        tv_title.setTextColor(selectPosition == position ?
                ContextCompat.getColor(mContext, R.color.white) :
                ContextCompat.getColor(mContext, R.color.orange_ff725c));// 设置选中
    }

    public int getSelectPosition() {
        return selectPosition;
    }

    public void setSelectPosition(int selectPosition) {
        this.selectPosition = selectPosition;
    }

    public void setItemWidth(int itemWidth) {
        this.itemWidth = itemWidth;
    }
}
