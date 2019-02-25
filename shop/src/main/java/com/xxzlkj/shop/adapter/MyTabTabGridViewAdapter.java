package com.xxzlkj.shop.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.xxzlkj.shop.R;
import com.xxzlkj.shop.model.ShopAppBean;
import com.xxzlkj.zhaolinshare.base.base.BaseAdapter;
import com.xxzlkj.zhaolinshare.base.base.BaseViewHolder;
import com.xxzlkj.zhaolinshare.base.util.TextViewUtils;


/**
 * 描述:
 *
 * @author zhangrq
 *         2017/7/5 15:59
 */
public class MyTabTabGridViewAdapter extends BaseAdapter<ShopAppBean.DataBean.GroupBean> {
    public int selectedPosition;

    public MyTabTabGridViewAdapter(Context context, int itemId) {
        super(context, itemId);
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
    }

    @Override
    public void convert(BaseViewHolder viewHolder, int position, ShopAppBean.DataBean.GroupBean item) {
        View rl_title = viewHolder.getView(R.id.rl_title);
        TextView mTextView = viewHolder.getView(R.id.id_shs111_title);
        // 设置值
        if (item.isClose() || item.isMore()) {
            // 更多、关闭按钮
            if (item.isClose()) {
                // 关闭按钮
                mTextView.setText("关闭");
                mTextView.setTextColor(ContextCompat.getColor(mContext, R.color.text_6));
                rl_title.setBackgroundResource(R.drawable.shape_rectangle_order_gray);
                Drawable right = mContext.getResources().getDrawable(R.mipmap.ic_shop_home_close);
                right.setBounds(0, 0, right.getIntrinsicWidth(), right.getIntrinsicHeight());
                mTextView.setCompoundDrawables(null, null, right, null);
            } else {
                // 更多按钮
                mTextView.setText("更多");
                mTextView.setTextColor(ContextCompat.getColor(mContext, R.color.text_6));
                rl_title.setBackground(null);
                Drawable right = mContext.getResources().getDrawable(R.mipmap.ic_shop_home_arrow);
                right.setBounds(0, 0, right.getIntrinsicWidth(), right.getIntrinsicHeight());
                mTextView.setCompoundDrawables(null, null, right, null);
            }
        } else {
            // 普通的按钮
            TextViewUtils.setTextHasValue(mTextView, item.getGroup_title());
            rl_title.setBackgroundResource(position == selectedPosition ? R.drawable.shape_buy_now : R.drawable.shape_white);// 设置选中
            mTextView.setTextColor(position == selectedPosition ?
                    ContextCompat.getColor(mContext, R.color.white) :
                    ContextCompat.getColor(mContext, R.color.text_6));// 设置选中
            mTextView.setCompoundDrawables(null, null, null, null);
        }
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }
}
