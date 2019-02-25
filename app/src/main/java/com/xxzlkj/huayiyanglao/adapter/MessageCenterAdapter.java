package com.xxzlkj.huayiyanglao.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xxzlkj.huayiyanglao.R;
import com.xxzlkj.huayiyanglao.model.MessageCenterListBean;
import com.xxzlkj.huayiyanglao.model.MessageTypeBean;
import com.xxzlkj.huayiyanglao.weight.ShapeButton;
import com.xxzlkj.zhaolinshare.base.base.BaseAdapter;
import com.xxzlkj.zhaolinshare.base.base.BaseViewHolder;
import com.xxzlkj.zhaolinshare.base.util.DateUtils;
import com.xxzlkj.zhaolinshare.base.util.NumberFormatUtils;
import com.xxzlkj.zhaolinshare.base.util.PicassoUtils;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 描述:消息中心
 *
 * @author leifeng
 *         2018/2/26 14:23
 */


public class MessageCenterAdapter extends BaseAdapter<MessageTypeBean.DataBean> {

    public MessageCenterAdapter(Context context, int itemId) {
        super(context, itemId);
    }

    @Override
    public void convert(BaseViewHolder holder, int position, MessageTypeBean.DataBean itemBean) {
        ImageView mImageView = holder.getView(R.id.id_message_center_image);
        // 新消息红点
        View mRedPointView = holder.getView(R.id.id_message_center_red);
        // 消息标题
        TextView mTitleTextView = holder.getView(R.id.id_message_center_title);
        // 消息时间
        TextView mTimeTextView = holder.getView(R.id.id_message_center_time);
        // 消息内容
        TextView mContentTextView = holder.getView(R.id.id_message_center_content);

        String simg = itemBean.getSimg();
        if (TextUtils.isEmpty(simg)) {
            mImageView.setImageResource(R.mipmap.ic_launcher);
        } else {
            PicassoUtils.setImageSmall(mContext, simg, mImageView);
        }

        String state = itemBean.getMessage_state();
        if (TextUtils.isEmpty(state)) {
            mRedPointView.setVisibility(View.GONE);
        } else {
            if ("1".equals(state)) {// 未读
                mRedPointView.setVisibility(View.VISIBLE);
            } else if ("2".equals(state)) {// 已读
                mRedPointView.setVisibility(View.GONE);
            }
        }

        String title = itemBean.getTitle();
        if (!TextUtils.isEmpty(title)) {
            mTitleTextView.setText(title);
        }

        long time = NumberFormatUtils.toLong(itemBean.getMessage_addtime()) * 1000;
        mTimeTextView.setText(DateUtils.getYearMonthDay(time, "yyyy.MM.dd"));

        String desc = itemBean.getMessage_title();
        if (!TextUtils.isEmpty(desc)) {
            mContentTextView.setText(desc);
        }
    }
}
