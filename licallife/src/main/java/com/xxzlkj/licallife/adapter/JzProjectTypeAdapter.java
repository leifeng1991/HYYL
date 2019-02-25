package com.xxzlkj.licallife.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.xxzlkj.licallife.R;
import com.xxzlkj.licallife.model.CleanIndex;
import com.xxzlkj.zhaolinshare.base.base.BaseAdapter;
import com.xxzlkj.zhaolinshare.base.base.BaseViewHolder;
import com.xxzlkj.zhaolinshare.base.util.PicassoUtils;


/**
 * 家政项目类型
 * Created by Administrator on 2017/4/20.
 */

public class JzProjectTypeAdapter extends BaseAdapter<CleanIndex.DataBean.GroupBean> {
    public static int indexChecked;

    public JzProjectTypeAdapter(Context context, int itemId) {
        super(context, itemId);
        indexChecked = 0;
    }

    @Override
    public void convert(BaseViewHolder holder, int position, CleanIndex.DataBean.GroupBean itemBean) {
        ImageView mImageView = holder.getView(R.id.id_jpt_image);
        TextView mTitleTextView = holder.getView(R.id.id_jpt_title);
        if (indexChecked == position){
            PicassoUtils.setImageSmall(mContext,itemBean.getBg_simg(),mImageView);
        }else {
            PicassoUtils.setImageSmall(mContext,itemBean.getSimg(),mImageView);
        }

        mTitleTextView.setText(itemBean.getTitle());
    }
}
