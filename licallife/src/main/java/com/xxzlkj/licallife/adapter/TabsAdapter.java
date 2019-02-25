package com.xxzlkj.licallife.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;

import com.xxzlkj.licallife.R;
import com.xxzlkj.zhaolinshare.base.base.BaseAdapter;
import com.xxzlkj.zhaolinshare.base.base.BaseViewHolder;


public class TabsAdapter extends BaseAdapter<String> {



    public TabsAdapter(Context context, int itemId) {
        super(context, itemId);
    }

    @Override
    public void convert(BaseViewHolder holder, int position, String itemBean) {
        TextView tv_tab = holder.getView(R.id.tv_tab);// 内容
        // 设置值
        tv_tab.setText(itemBean);
        // 设置背景、颜色值
        // 红、橙、绿
        int index = position % 3;
        tv_tab.setTextColor(ContextCompat.getColor(mContext, index == 0 ? R.color.orange_ff725c : (index == 1 ? R.color.orange_f6a623 : R.color.green_7BCC36)));
        tv_tab.setBackgroundResource(index == 0 ? R.drawable.shape_rectangle_orange_ff725c : (index == 1 ? R.drawable.shape_rectangle_yellow_f6a623 : R.drawable.shape_rectangle_green_7bcc36));
    }

}
