package com.xxzlkj.huayiyanglao.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.xxzlkj.huayiyanglao.R;
import com.xxzlkj.huayiyanglao.model.MessageBean;
import com.xxzlkj.zhaolinshare.base.base.BaseAdapter;
import com.xxzlkj.zhaolinshare.base.base.BaseViewHolder;
import com.xxzlkj.zhaolinshare.base.util.DateUtils;
import com.xxzlkj.zhaolinshare.base.util.NumberFormatUtils;
import com.xxzlkj.zhaolinshare.base.util.PicassoUtils;

/**
 * Created by Administrator on 2017/7/26.
 */

public class NotificationMessageAdapter extends BaseAdapter<MessageBean.DataBean> {
    public NotificationMessageAdapter(Context context, int itemId) {
        super(context, itemId);
    }

    @Override
    public void convert(BaseViewHolder holder, int position, MessageBean.DataBean itemBean) {
        TextView mTimeTextView = holder.getView(R.id.id_time);
        TextView mTitleTextView = holder.getView(R.id.id_title);
        // 样式一 小图
        LinearLayout mFirstLinearLayout = holder.getView(R.id.id_style_first);
        ImageView mFirstImageView = holder.getView(R.id.id_style_first_image);
        TextView mFirstContentTextView = holder.getView(R.id.id_style_first_content);
        // 样式二 大图
        LinearLayout mSecondLinearLayout = holder.getView(R.id.id_style_second);
        RoundedImageView mSecondImageView = holder.getView(R.id.id_style_second_image);
        TextView mSecondContentTextView = holder.getView(R.id.id_style_second_content);
        //设置数据
        // 时间
        long time = NumberFormatUtils.toLong(itemBean.getAddtime()) * 1000;
        mTimeTextView.setText(DateUtils.getMonthDayHourMinute(time));
        // 标题
        mTitleTextView.setText(itemBean.getTitle());
        // 样式
        String style = itemBean.getStyle();
        if ("2".equals(style)) {
            mFirstLinearLayout.setVisibility(View.VISIBLE);
            mSecondLinearLayout.setVisibility(View.GONE);
            mFirstContentTextView.setText(itemBean.getDesc());
            PicassoUtils.setImageBig(mContext, itemBean.getSimg(), mFirstImageView);
        } else {
            mSecondLinearLayout.setVisibility(View.VISIBLE);
            mFirstLinearLayout.setVisibility(View.GONE);
            mSecondContentTextView.setText(itemBean.getDesc());
            PicassoUtils.setImageBig(mContext, itemBean.getSimg(), mSecondImageView);
        }
    }
}
