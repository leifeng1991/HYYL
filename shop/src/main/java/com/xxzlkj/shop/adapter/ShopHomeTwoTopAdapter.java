package com.xxzlkj.shop.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;
import android.widget.TextView;

import com.xxzlkj.shop.R;
import com.xxzlkj.shop.model.ShopAppBean;
import com.xxzlkj.zhaolinshare.base.base.BaseAdapter;
import com.xxzlkj.zhaolinshare.base.base.BaseViewHolder;
import com.xxzlkj.zhaolinshare.base.util.PicassoUtils;
import com.xxzlkj.zhaolinshare.base.util.TextViewUtils;


/**
 * 描述: 商城首页adapter
 *
 * @author zhangrq
 *         2016/12/12 11:06
 */

public class ShopHomeTwoTopAdapter extends BaseAdapter<ShopAppBean.DataBean> {

    public ShopHomeTwoTopAdapter(Context context, int itemId) {
        super(context, itemId);
    }

    @Override
    public void convert(BaseViewHolder holder, int position, ShopAppBean.DataBean itemBean) {
        ImageView iv_icon = holder.getView(R.id.iv_icon);
        TextView tv_title = holder.getView(R.id.tv_title);
        // 设置值
        PicassoUtils.setImageSmall(mContext, itemBean.getSimg(), iv_icon);
        TextViewUtils.setTextHasValue(tv_title, itemBean.getTitle());
        tv_title.setTextColor(itemBean.isChecked() ?
                ContextCompat.getColor(mContext, R.color.orange_ff725c) :
                ContextCompat.getColor(mContext, R.color.black_888888));

    }
}


