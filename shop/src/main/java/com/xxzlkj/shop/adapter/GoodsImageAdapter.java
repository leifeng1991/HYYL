package com.xxzlkj.shop.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.xxzlkj.shop.R;
import com.xxzlkj.shop.model.GoodsDetail;
import com.xxzlkj.zhaolinshare.base.base.BaseAdapter;
import com.xxzlkj.zhaolinshare.base.base.BaseViewHolder;
import com.xxzlkj.zhaolinshare.base.util.NumberFormatUtils;
import com.xxzlkj.zhaolinshare.base.util.PicassoUtils;
import com.xxzlkj.zhaolinshare.base.util.PixelUtil;


/**
 * 商品详情图片适配器
 * Created by Administrator on 2017/3/18.
 */

public class GoodsImageAdapter extends BaseAdapter<GoodsDetail.DataBean.ContentBean> {
    private int mWidth;

    public GoodsImageAdapter(Context context, int itemId) {
        super(context, itemId);
        mWidth = PixelUtil.getScreenWidth(context);
    }

    @Override
    public void convert(BaseViewHolder holder, int position, GoodsDetail.DataBean.ContentBean itemBean) {
        ImageView mGoodsImage = holder.getView(R.id.id_gdi_image);
        int mHeight = (int) (mWidth * (1.0 * NumberFormatUtils.toInt(itemBean.getH()) / NumberFormatUtils.toInt(itemBean.getW(),1)));
        PicassoUtils.setWithAndHeight(mContext,itemBean.getSimg(),mWidth,mHeight,mGoodsImage);
    }

}
