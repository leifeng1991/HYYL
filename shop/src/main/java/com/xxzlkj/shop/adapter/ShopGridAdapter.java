package com.xxzlkj.shop.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.xxzlkj.shop.R;
import com.xxzlkj.shop.model.ShopIndexBean;
import com.xxzlkj.zhaolinshare.base.base.BaseAdapter;
import com.xxzlkj.zhaolinshare.base.base.BaseViewHolder;
import com.xxzlkj.zhaolinshare.base.util.PicassoUtils;
import com.xxzlkj.zhaolinshare.base.util.TextViewUtils;


public class ShopGridAdapter extends BaseAdapter<ShopIndexBean.DataBean.AppBean> {
    public ShopGridAdapter(Context context, int itemId) {
        super(context, itemId);
    }

    @Override
    public void convert(BaseViewHolder holder, int position, ShopIndexBean.DataBean.AppBean itemBean) {
        ImageView mImageView = holder.getView(R.id.id_grid_list_image);
        TextView mTextView = holder.getView(R.id.id_grid_list_title);
        // 设置值
        PicassoUtils.setImageSmall(mContext, itemBean.getSimg(), mImageView);
        TextViewUtils.setTextHasValue(mTextView,itemBean.getTitle());
    }

}