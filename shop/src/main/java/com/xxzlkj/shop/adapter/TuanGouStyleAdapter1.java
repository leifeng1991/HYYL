package com.xxzlkj.shop.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xxzlkj.shop.R;
import com.xxzlkj.shop.model.Goods;
import com.xxzlkj.zhaolinshare.base.base.BaseAdapter;
import com.xxzlkj.zhaolinshare.base.base.BaseViewHolder;
import com.xxzlkj.zhaolinshare.base.util.PicassoUtils;
import com.xxzlkj.zhaolinshare.base.util.PixelUtil;


public class TuanGouStyleAdapter1 extends BaseAdapter<Goods> {

    public TuanGouStyleAdapter1(Context context, int itemId) {
        super(context, itemId);
    }

    @Override
    public void convert(BaseViewHolder holder, int position, Goods itemBean) {
        LinearLayout mParentLayout = holder.getView(R.id.id_parent_layout);
        mParentLayout.getLayoutParams().width = (int) (PixelUtil.getScreenWidth(mContext) / 2.5);
        ImageView mImageView = holder.getView(R.id.id_image);// 图片
        TextView mTitleTextView = holder.getView(R.id.id_title);// 标题
        TextView mPriceTextView = holder.getView(R.id.id_price);// 商品价格
        TextView mNumberTextView = holder.getView(R.id.id_number);// 已经抢购数量

        // 设置值
        mTitleTextView.setText(itemBean.getTitle());
        PicassoUtils.setImageBig(mContext, itemBean.getSimg(), mImageView);
        mPriceTextView.setText(String.format("￥%s", itemBean.getPrice()));
        mNumberTextView.setText(String.format("已经%s件", itemBean.getSale()));
    }
}
