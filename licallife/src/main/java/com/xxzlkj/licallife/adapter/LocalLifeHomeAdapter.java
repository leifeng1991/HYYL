package com.xxzlkj.licallife.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xxzlkj.licallife.R;
import com.xxzlkj.licallife.model.LocalLifeHomeBean;
import com.xxzlkj.zhaolinshare.base.base.BaseAdapter;
import com.xxzlkj.zhaolinshare.base.base.BaseViewHolder;
import com.xxzlkj.zhaolinshare.base.util.PicassoUtils;


public class LocalLifeHomeAdapter extends BaseAdapter<LocalLifeHomeBean.DataBean.TypeBean> {

    public LocalLifeHomeAdapter(Context context, int itemId) {
        super(context, itemId);
    }

    @Override
    public void convert(BaseViewHolder holder, int position, LocalLifeHomeBean.DataBean.TypeBean itemBean) {
        ImageView mImageView = holder.getView(R.id.id_image);
        TextView mTitleTextView = holder.getView(R.id.id_title);// 标题
        TextView mContentTextView = holder.getView(R.id.id_content);// 内容
        // 最多三个标签
        TextView mFirstTextView = holder.getView(R.id.id_lable_first);// 标签一
        TextView mSecondTextView = holder.getView(R.id.id_lable_second);// 标签二
        TextView mThirdTextView = holder.getView(R.id.id_lable_third);// 标签三
        // 设置数据
        PicassoUtils.setImageBig(mContext,itemBean.getSimg(),mImageView);
        mTitleTextView.setText(itemBean.getTitle());
        mContentTextView.setText(itemBean.getDesc());
        // 标签
        String label = itemBean.getLabel();
        if (TextUtils.isEmpty(label)){
            mFirstTextView.setVisibility(View.GONE);
            mSecondTextView.setVisibility(View.GONE);
            mThirdTextView.setVisibility(View.GONE);
        }else {
            String[] split = label.split(",");
            if (split.length == 1){
                // 一个标签
                mFirstTextView.setVisibility(View.VISIBLE);
                mSecondTextView.setVisibility(View.GONE);
                mThirdTextView.setVisibility(View.GONE);
                mFirstTextView.setText(split[0]);
            }else if (split.length == 2){
                // 两个标签
                mFirstTextView.setVisibility(View.VISIBLE);
                mSecondTextView.setVisibility(View.VISIBLE);
                mThirdTextView.setVisibility(View.GONE);
                mFirstTextView.setText(split[0]);
                mSecondTextView.setText(split[1]);
            }else {
                // 三个以上的标签
                mFirstTextView.setVisibility(View.VISIBLE);
                mSecondTextView.setVisibility(View.VISIBLE);
                mThirdTextView.setVisibility(View.VISIBLE);
                mFirstTextView.setText(split[0]);
                mSecondTextView.setText(split[1]);
                mThirdTextView.setText(split[2]);
            }
        }


    }
}
