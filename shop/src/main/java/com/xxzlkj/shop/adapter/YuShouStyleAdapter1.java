package com.xxzlkj.shop.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xxzlkj.shop.R;
import com.xxzlkj.shop.model.Goods;
import com.xxzlkj.zhaolinshare.base.base.BaseAdapter;
import com.xxzlkj.zhaolinshare.base.base.BaseViewHolder;
import com.xxzlkj.zhaolinshare.base.util.PicassoUtils;
import com.xxzlkj.zhaolinshare.base.util.PixelUtil;


/**
 * Created by Administrator on 2017/6/26.
 */

public class YuShouStyleAdapter1 extends BaseAdapter<Goods> {
    private double widthScale;

    public YuShouStyleAdapter1(Context context, int itemId) {
        super(context, itemId);
        widthScale = 1.0 * PixelUtil.getScreenWidth(mContext) / 750;
    }

    @Override
    public void convert(final BaseViewHolder holder, int position, final Goods itemBean) {
        View mLine = holder.getView(R.id.id_line);// 分割线
        ImageView mImageView = holder.getView(R.id.id_image);// 图片
        TextView mTitleTextView = holder.getView(R.id.id_title);// 标题
        // 控制分割线显示和隐藏
        mLine.setVisibility(position == 0 ? View.GONE : View.VISIBLE);

        mImageView.getLayoutParams().height = (int) (widthScale * 280);
        PicassoUtils.setImageBig(mContext, itemBean.getSimg(), mImageView);
        mTitleTextView.setText(itemBean.getTitle());

    }

}
