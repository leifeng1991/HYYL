package com.xxzlkj.licallife.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.xxzlkj.licallife.R;
import com.xxzlkj.zhaolinshare.base.base.BaseAdapter;
import com.xxzlkj.zhaolinshare.base.base.BaseViewHolder;
import com.xxzlkj.zhaolinshare.base.util.PicassoUtils;


public class IdPhotoAdapter extends BaseAdapter<String> {


    public IdPhotoAdapter(Context context, int itemId) {
        super(context, itemId);
    }

    @Override
    public void convert(BaseViewHolder holder, int position, String itemBean) {
        ImageView iv_icon = holder.getView(R.id.iv_icon);// 图标
        PicassoUtils.setImageBig(mContext, itemBean, iv_icon);
    }

}
