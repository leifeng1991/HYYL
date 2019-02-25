package com.xxzlkj.licallife.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.sunfusheng.marqueeview.DisplayUtil;
import com.xxzlkj.licallife.R;
import com.xxzlkj.licallife.model.ProjectDetail;
import com.xxzlkj.zhaolinshare.base.base.BaseAdapter;
import com.xxzlkj.zhaolinshare.base.base.BaseViewHolder;
import com.xxzlkj.zhaolinshare.base.util.NumberFormatUtils;
import com.xxzlkj.zhaolinshare.base.util.PicassoUtils;
import com.xxzlkj.zhaolinshare.base.util.PixelUtil;


/**
 * 保洁详情图片适配器
 * Created by Administrator on 2017/3/18.
 */

public class ProjecImageAdapter extends BaseAdapter<ProjectDetail.DataBean.ImgBean> {
    private int mWidth;
    private int mHeight;

    public ProjecImageAdapter(Context context, int itemId) {
        super(context, itemId);
        mWidth = PixelUtil.getScreenWidth(context) - DisplayUtil.dip2px(context, 22);
    }

    @Override
    public void convert(BaseViewHolder holder, int position, ProjectDetail.DataBean.ImgBean itemBean) {
        ImageView mGoodsImage = holder.getView(R.id.id_gdi_image);
        mHeight = (int) (mWidth * (1.0 * NumberFormatUtils.toInt(itemBean.getH()) / NumberFormatUtils.toInt(itemBean.getW(),1)));
        PicassoUtils.setWithAndHeight(mContext,itemBean.getSimg(),mWidth,mHeight,mGoodsImage);
    }

}
