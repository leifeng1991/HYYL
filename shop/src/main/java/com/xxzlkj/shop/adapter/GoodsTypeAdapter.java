package com.xxzlkj.shop.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.xxzlkj.shop.R;
import com.xxzlkj.shop.config.StringConstants;
import com.xxzlkj.shop.model.Goods;
import com.xxzlkj.shop.utils.TextPriceUtil;
import com.xxzlkj.zhaolinshare.base.base.BaseAdapter;
import com.xxzlkj.zhaolinshare.base.base.BaseViewHolder;
import com.xxzlkj.zhaolinshare.base.util.PicassoUtils;
import com.xxzlkj.zhaolinshare.base.util.TextViewUtils;



public class GoodsTypeAdapter extends BaseAdapter<Goods> {
    private int mType;

    public GoodsTypeAdapter(Context context, int itemId, int mType) {
        super(context, itemId);
        this.mType = mType;
    }

    @Override
    public void convert(BaseViewHolder holder, int position, Goods itemBean) {
        ImageView mGoodsImage = holder.getView(R.id.id_gti_image);
        PicassoUtils.setImageBig(mContext,itemBean.getSimg(),mGoodsImage);
        TextView mGoodsName = holder.getView(R.id.id_gti_name);
        mGoodsName.setText(itemBean.getTitle());

        if (mType == StringConstants.GOODS_TYPE_1){
            TextView mGoodsPrice = holder.getView(R.id.id_gti_price);
            TextView mGoodsLinePrice = holder.getView(R.id.id_gti_line_price);
            String price = itemBean.getPrice();
            String prices = itemBean.getPrices();
            TextViewUtils.setText(mGoodsPrice,"￥"+price);
            TextPriceUtil.setTextPrices(price,prices,mGoodsLinePrice);
        }
    }

}
