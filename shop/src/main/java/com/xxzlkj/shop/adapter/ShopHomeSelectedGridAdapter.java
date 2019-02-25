package com.xxzlkj.shop.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;

import com.xxzlkj.shop.R;
import com.xxzlkj.shop.utils.listview.CommBaseAdapter;
import com.xxzlkj.shop.utils.listview.ViewHolder;

import java.util.List;

/**
 * 描述:
 *
 * @author zhangrq
 *         2017/7/3 14:45
 */
public class ShopHomeSelectedGridAdapter extends CommBaseAdapter<String> {
    public int selectedPosition;

    public ShopHomeSelectedGridAdapter(Context context, int layoutId, List<String> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder viewHolder, String item, int position) {
        TextView mTitleTextView = viewHolder.getView(R.id.id_shs111_title);
        mTitleTextView.setText(item);
        // 新增设置选中
        viewHolder.mConvertView.setBackgroundResource(selectedPosition == position ? R.drawable.shape_buy_now : R.drawable.shape_white);// 设置选中
        mTitleTextView.setTextColor(selectedPosition == position ?
                ContextCompat.getColor(mContext, R.color.white) :
                ContextCompat.getColor(mContext, R.color.text_6));// 设置选中

    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
    }
}
