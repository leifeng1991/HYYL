package com.xxzlkj.shop.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.xxzlkj.shop.R;
import com.xxzlkj.shop.model.ShopActionThemeTitleBean;
import com.xxzlkj.zhaolinshare.base.base.BaseAdapter;
import com.xxzlkj.zhaolinshare.base.base.BaseViewHolder;
import com.xxzlkj.zhaolinshare.base.util.TextViewUtils;


public class ShopActionThemTitleAdapter extends BaseAdapter<ShopActionThemeTitleBean> {
    public int selectedPosition;

    public ShopActionThemTitleAdapter(Context context, int itemId) {
        super(context, itemId);
    }

    @Override
    public void convert(BaseViewHolder holder, int position, ShopActionThemeTitleBean item) {
        View rl_title = holder.getView(R.id.rl_title);
        TextView mTextView = holder.getView(R.id.tv_title);
        // 设置值
        if (item.isClose()) {
            // 更多、关闭按钮
            // 关闭按钮
            mTextView.setText("关闭");
            rl_title.setBackgroundResource(R.drawable.shape_rectangle_order_gray);
            Drawable right = ContextCompat.getDrawable(mContext, R.mipmap.ic_shop_home_close);
            right.setBounds(0, 0, right.getIntrinsicWidth(), right.getIntrinsicHeight());
            mTextView.setCompoundDrawables(null, null, right, null);
            mTextView.setTextColor(ContextCompat.getColor(mContext, R.color.text_6));
        } else {
            // 普通的按钮
            TextViewUtils.setTextHasValue(mTextView, item.getGroupTitle());
            rl_title.setBackgroundResource(selectedPosition == position ? R.drawable.shape_buy_now : R.drawable.shape_white);// 设置选中
            mTextView.setCompoundDrawables(null, null, null, null);
            mTextView.setTextColor(selectedPosition == position ?
                    ContextCompat.getColor(mContext, R.color.white) :
                    ContextCompat.getColor(mContext, R.color.text_6));// 设置选中
        }
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }
}
