package com.xxzlkj.licallife.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xxzlkj.licallife.R;
import com.xxzlkj.licallife.model.Clean;
import com.xxzlkj.licallife.weight.RatingBar;
import com.xxzlkj.zhaolinshare.base.base.BaseAdapter;
import com.xxzlkj.zhaolinshare.base.base.BaseViewHolder;
import com.xxzlkj.zhaolinshare.base.util.NumberFormatUtils;
import com.xxzlkj.zhaolinshare.base.util.PicassoUtils;
import com.xxzlkj.zhaolinshare.base.util.TextViewUtils;


/**
 * 保洁师
 * Created by Administrator on 2017/4/21.
 */

public class CleanerAdapter extends BaseAdapter<Clean.DataBean> {
    private int jumpType;

    public CleanerAdapter(Context context, int jumpType,int itemId) {
        super(context, itemId);
        this.jumpType = jumpType;
    }

    @Override
    public void convert(BaseViewHolder holder, int position, Clean.DataBean itemBean) {
        ImageView mImageView = holder.getView(R.id.id_jci_image);
        TextView mNameTextView = holder.getView(R.id.id_jci_name);
        TextView mSatisfactionTextView = holder.getView(R.id.id_jci_satisfaction);
        TextView mNumberTextView = holder.getView(R.id.id_jci_num);
        TextView mTypeTextView = holder.getView(R.id.id_jci_type);
        LinearLayout mLableLinearLayout = holder.getView(R.id.id_lable_layout);
        TextView mFirstLableTextView = holder.getView(R.id.id_lable_first);
        TextView mSecondLablelTextView = holder.getView(R.id.id_lable_second);
        TextView mThirdTextView = holder.getView(R.id.id_lable_third);
        RatingBar mRatingBar = holder.getView(R.id.id_jci_rb);
        mRatingBar.setEnabled(false);
        // 设置数据
        // 图片
        PicassoUtils.setImageSmall(mContext, itemBean.getSimg(), mImageView);
        // 名字
        TextViewUtils.setText(mNameTextView, itemBean.getName());
        // 星级
        String star = itemBean.getStar();
        if (TextUtils.isEmpty(star)){
            mRatingBar.setRating(0);
        }else {
            mRatingBar.setRating(NumberFormatUtils.toFloat(star));
        }

        // 满意度
        mSatisfactionTextView.setText(String.format("满意度：%s%%", itemBean.getSatisfaction()));
        // 接单数量
        mNumberTextView.setText(String.format("接单量：%s", itemBean.getSale()));
        // 分类
        String group;
        if (jumpType == 2){
            // 2 小时工
            group = itemBean.getSpecialty();
        }else {
            // 保洁师
            group = itemBean.getGroup();
        }

        if (TextUtils.isEmpty(group)){
            mTypeTextView.setVisibility(View.GONE);
        }else {
            mTypeTextView.setVisibility(View.VISIBLE);
            mTypeTextView.setText(group);
        }

        // 标签
        String label = itemBean.getLabel();

        String[] split = label.split(",");
        if (!TextUtils.isEmpty(label)) {
            if (split.length == 1) {
                // 一个标签
                mLableLinearLayout.setVisibility(View.VISIBLE);
                mFirstLableTextView.setVisibility(View.VISIBLE);
                mSecondLablelTextView.setVisibility(View.GONE);
                mThirdTextView.setVisibility(View.GONE);
                mFirstLableTextView.setText(split[0]);
            } else if (split.length == 2) {
                // 两个标签
                mLableLinearLayout.setVisibility(View.VISIBLE);
                mFirstLableTextView.setVisibility(View.VISIBLE);
                mSecondLablelTextView.setVisibility(View.VISIBLE);
                mThirdTextView.setVisibility(View.GONE);
                mFirstLableTextView.setText(split[0]);
                mSecondLablelTextView.setText(split[1]);
            } else if (split.length > 2){
                // 三个、三个以上的标签
                mLableLinearLayout.setVisibility(View.VISIBLE);
                mFirstLableTextView.setVisibility(View.VISIBLE);
                mSecondLablelTextView.setVisibility(View.VISIBLE);
                mThirdTextView.setVisibility(View.VISIBLE);
                mFirstLableTextView.setText(split[0]);
                mSecondLablelTextView.setText(split[1]);
                mThirdTextView.setText(split[2]);
            }else {
                // 没有标签
                mLableLinearLayout.setVisibility(View.GONE);
            }
        } else {
            // 没有标签
            mLableLinearLayout.setVisibility(View.GONE);
        }
    }
}
