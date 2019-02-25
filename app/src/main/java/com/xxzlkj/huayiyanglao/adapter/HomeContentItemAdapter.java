package com.xxzlkj.huayiyanglao.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.xxzlkj.huayiyanglao.R;
import com.xxzlkj.huayiyanglao.model.HomeIndexBean;
import com.xxzlkj.zhaolinshare.base.base.BaseAdapter;
import com.xxzlkj.zhaolinshare.base.base.BaseViewHolder;
import com.xxzlkj.zhaolinshare.base.util.PicassoUtils;

/**
 * 描述:首页右边列表内容适配器
 *
 * @author leifeng
 *         2017/11/21 17:33
 */


public class HomeContentItemAdapter extends BaseAdapter<HomeIndexBean.DataBean.IndexBean.ChildBean> {

    public HomeContentItemAdapter(Context context, int itemId) {
        super(context, itemId);
    }

    @Override
    public void convert(BaseViewHolder holder, int position, HomeIndexBean.DataBean.IndexBean.ChildBean itemBean) {
        ImageView mImageView = holder.getView(R.id.id_image);// 图片
        TextView mTitleTextView = holder.getView(R.id.id_title);// 标题
        TextView mContentTextView = holder.getView(R.id.id_content);// 内容
        // 设置数据
        PicassoUtils.setImageBig(mContext,itemBean.getSimg(),mImageView);
        mTitleTextView.setText(itemBean.getTitle());
        mContentTextView.setText(itemBean.getDesc());

    }
}
