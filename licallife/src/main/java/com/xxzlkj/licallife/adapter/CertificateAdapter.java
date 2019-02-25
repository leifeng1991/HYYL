package com.xxzlkj.licallife.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xxzlkj.licallife.R;
import com.xxzlkj.licallife.model.NannyAndMaternityMatronDesBean;
import com.xxzlkj.zhaolinshare.base.base.BaseAdapter;
import com.xxzlkj.zhaolinshare.base.base.BaseViewHolder;
import com.xxzlkj.zhaolinshare.base.util.PicassoUtils;
import com.xxzlkj.zhaolinshare.base.util.PixelUtil;


public class CertificateAdapter extends BaseAdapter<NannyAndMaternityMatronDesBean.DataBean.AbilityBean> {


    private final int itemWidth;

    public CertificateAdapter(Context context, int itemId) {
        super(context, itemId);
        itemWidth = (int) ((PixelUtil.getScreenWidth(mContext) - PixelUtil.dip2px(mContext, 20)) / 5.0);
    }

    @Override
    public void convert(BaseViewHolder holder, int position, NannyAndMaternityMatronDesBean.DataBean.AbilityBean itemBean) {
        ImageView iv_icon = holder.getView(R.id.iv_icon);// 图标
        TextView tv_title = holder.getView(R.id.tv_title);// 内容

        ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
        layoutParams.width = itemWidth;
        holder.itemView.setLayoutParams(layoutParams);
        // 设置值
        PicassoUtils.setImageBig(mContext,itemBean.getSimg(),iv_icon);
        tv_title.setText(itemBean.getTitle());
    }

}
