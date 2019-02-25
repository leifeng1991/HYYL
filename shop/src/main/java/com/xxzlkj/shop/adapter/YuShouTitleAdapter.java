package com.xxzlkj.shop.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.xxzlkj.shop.R;
import com.xxzlkj.shop.model.Goods;
import com.xxzlkj.zhaolinshare.base.base.BaseAdapter;
import com.xxzlkj.zhaolinshare.base.base.BaseViewHolder;
import com.xxzlkj.zhaolinshare.base.util.TextViewUtils;

/**
 * 描述:
 *
 * @author zhangrq
 *         2017/7/5 15:59
 */
public class YuShouTitleAdapter extends BaseAdapter<Goods> {
    public int selectedPosition = 0;

    public YuShouTitleAdapter(Context context, int itemId) {
        super(context, itemId);
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
    }

    @Override
    public void convert(BaseViewHolder viewHolder, int position, Goods item) {
        View rl_title = viewHolder.getView(R.id.rl_title);
        TextView mTextView = viewHolder.getView(R.id.id_shs111_title);
        // 普通的按钮
        TextViewUtils.setTextHasValue(mTextView, item.getTitle());
        rl_title.setBackgroundResource(position == selectedPosition ? R.drawable.shape_buy_now : R.drawable.shape_white);// 设置选中
        mTextView.setTextColor(position == selectedPosition ?
                ContextCompat.getColor(mContext, R.color.white) :
                ContextCompat.getColor(mContext, R.color.text_6));// 设置选中
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }
}
