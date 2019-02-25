package com.xxzlkj.licallife.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sunfusheng.marqueeview.DisplayUtil;
import com.xxzlkj.licallife.R;
import com.xxzlkj.licallife.model.CleanGoods;
import com.xxzlkj.zhaolinshare.base.base.BaseAdapter;
import com.xxzlkj.zhaolinshare.base.base.BaseViewHolder;
import com.xxzlkj.zhaolinshare.base.util.PicassoUtils;
import com.xxzlkj.zhaolinshare.base.util.TextViewUtils;

/**
 * 保洁产品
 * Created by Administrator on 2017/4/20.
 */

public class CleanGoodsAdapter extends BaseAdapter<CleanGoods.DataBean> {
    private int wh;
    public CleanGoodsAdapter(Context context, int itemId) {
        super(context, itemId);
        int windowWidth = DisplayUtil.getWindowWidth((Activity) context);
        wh = (int)  (1.0f * windowWidth / 2) - DisplayUtil.dip2px(context,20);
    }

    @Override
    public void convert(BaseViewHolder holder, int position, CleanGoods.DataBean itemBean) {
        ImageView mImageView = holder.getView(R.id.id_jpgi_image);
        TextView mTitleTextView = holder.getView(R.id.id_jpgi_title);
        TextView mPriceTextView = holder.getView(R.id.id_jpgi_price);
        TextView mNumberTextView = holder.getView(R.id.id_jpgi_num);
        TextView mContentTextView = holder.getView(R.id.id_jpgi_content);
        //右边线
        View rightLine = holder.getView(R.id.id_jpgi_right);
        if (position % 2 == 0){
            rightLine.setVisibility(View.VISIBLE);
        }else {
            rightLine.setVisibility(View.GONE);
        }

        PicassoUtils.setWithAndHeight(mContext,itemBean.getSimg(),wh,wh,mImageView);
        TextViewUtils.setText(mTitleTextView,itemBean.getTitle());
        TextViewUtils.setText(mPriceTextView,itemBean.getUnitContent());
        TextViewUtils.setText(mNumberTextView,"服务单数" + itemBean.getSale());
        TextViewUtils.setText(mContentTextView,itemBean.getShop_title());
    }
}
