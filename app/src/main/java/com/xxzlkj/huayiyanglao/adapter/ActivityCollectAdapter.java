package com.xxzlkj.huayiyanglao.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.sunfusheng.marqueeview.DisplayUtil;
import com.xxzlkj.huayiyanglao.R;
import com.xxzlkj.huayiyanglao.model.ActivityCollect;
import com.xxzlkj.zhaolinshare.base.base.BaseAdapter;
import com.xxzlkj.zhaolinshare.base.base.BaseViewHolder;
import com.xxzlkj.zhaolinshare.base.util.PicassoUtils;

/**
 * 活动收藏
 * Created by Administrator on 2017/4/19.
 */

public class ActivityCollectAdapter extends BaseAdapter<ActivityCollect.DataBean> {
    private int wh;

    public ActivityCollectAdapter(Context context, int itemId) {
        super(context, itemId);
        wh = DisplayUtil.dip2px(mContext,81);
    }

    @Override
    public void convert(final BaseViewHolder holder, final int position, final ActivityCollect.DataBean itemBean) {
        ImageView mImageView = holder.getView(R.id.id_cgli_image);
        TextView mTitleTextView = holder.getView(R.id.id_cgli_title);
        TextView mContentTextView = holder.getView(R.id.id_cgli_content);
        TextView mBroserNumTextView = holder.getView(R.id.id_cgli_browsering_number);
        TextView mCommentNumTextView = holder.getView(R.id.id_cgli_comment_number);
        TextView mCollectNumTextView = holder.getView(R.id.id_cgli_collect_number);

        PicassoUtils.setWithAndHeight(mContext,itemBean.getSimg(), wh,wh,mImageView);

        mTitleTextView.setText(itemBean.getTitle());
        mContentTextView.setText(itemBean.getAddress());
        mBroserNumTextView.setText(itemBean.getPv());
        mCommentNumTextView.setText(itemBean.getCount());
        mCollectNumTextView.setText(itemBean.getCell());

    }

}
