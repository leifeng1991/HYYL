package com.xxzlkj.huayiyanglao.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.xxzlkj.huayiyanglao.R;
import com.xxzlkj.huayiyanglao.model.PayType;
import com.xxzlkj.zhaolinshare.base.base.BaseAdapter;
import com.xxzlkj.zhaolinshare.base.base.BaseViewHolder;

/**
 * 描述:
 *
 * @author leifeng
 *         2017/11/25 17:47
 */


public class PayTypeAdapter extends BaseAdapter<PayType> {
    public int selectPosition = 0;

    public PayTypeAdapter(Context context, int itemId) {
        super(context, itemId);
    }

    @Override
    public void convert(BaseViewHolder holder, int position, PayType itemBean) {
        ImageView mImageView = holder.getView(R.id.id_pti_icon);
        ImageView mCheckedImageView = holder.getView(R.id.id_checked_image);
        TextView mNameTextView = holder.getView(R.id.id_pti_name);
        TextView mContentTextView = holder.getView(R.id.id_pti_content);
        // 设置数据
        mImageView.setImageResource(itemBean.getIcon());
        mNameTextView.setText(itemBean.getName());
        mContentTextView.setText(itemBean.getContent());
        // 设置选中
        mCheckedImageView.setImageResource(selectPosition == position ? R.mipmap.ic_blue_checked : R.mipmap.ic_blue_normal_checked);
    }
}
