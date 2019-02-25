package com.xxzlkj.huayiyanglao.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.xxzlkj.huayiyanglao.R;
import com.xxzlkj.huayiyanglao.model.WatchFastListBean;
import com.xxzlkj.shop.weight.CustomButton;
import com.xxzlkj.zhaolinshare.base.base.BaseAdapter;
import com.xxzlkj.zhaolinshare.base.base.BaseViewHolder;
import com.xxzlkj.zhaolinshare.base.util.PicassoUtils;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 描述:
 *
 * @author leifeng
 *         2018/1/29 9:23
 */


public class UserWatchAdapter extends BaseAdapter<WatchFastListBean.DataBean> {
    public UserWatchAdapter(Context context, int itemId) {
        super(context, itemId);
    }

    @Override
    public void convert(BaseViewHolder holder, int position, WatchFastListBean.DataBean itemBean) {
        CustomButton mPositionButton = holder.getView(R.id.id_position);// 数量
        CircleImageView mUserAvatarImageView = holder.getView(R.id.id_user_avatar);// 用户头像
        TextView mTitleTextView = holder.getView(R.id.id_title);// 用户名
        TextView mPhoneTextView = holder.getView(R.id.id_phone);// 用户手机号
        // 设置数据
        mPositionButton.setText((position + 1) + "");
        String simg = itemBean.getSimg();
        if (TextUtils.isEmpty(simg)) {
            mUserAvatarImageView.setImageResource(R.mipmap.default_icon);
        } else {
            PicassoUtils.setImageSmall(mContext, simg, mUserAvatarImageView);
        }
        mTitleTextView.setText(itemBean.getName());
        mPhoneTextView.setText(itemBean.getPhone());
    }
}
