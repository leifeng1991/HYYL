package com.xxzlkj.shop.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.xxzlkj.shop.R;
import com.xxzlkj.zhaolinshare.base.base.BaseAdapter;
import com.xxzlkj.zhaolinshare.base.base.BaseViewHolder;
import com.xxzlkj.zhaolinshare.base.util.PicassoUtils;
import com.xxzlkj.zhaolinshare.base.util.PixelUtil;


/**
 * 售后详情中图片适配器
 * Created by Administrator on 2017/4/24.
 */

public class AfterSaleImageAdapter extends BaseAdapter<String> {

    public AfterSaleImageAdapter(Context context, int itemId) {
        super(context, itemId);
    }

    @Override
    public void convert(BaseViewHolder holder, int position, String itemBean) {
        ImageView mGoodsImage = holder.getView(R.id.id_gdi_image);
        PicassoUtils.setWithAndHeight(mContext, itemBean, PixelUtil.dip2px(mContext,67),PixelUtil.dip2px(mContext,67),mGoodsImage);
    }
}
