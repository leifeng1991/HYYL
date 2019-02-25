package com.xxzlkj.huayiyanglao.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xxzlkj.huayiyanglao.R;
import com.xxzlkj.huayiyanglao.model.SignUpUser;
import com.xxzlkj.zhaolinshare.base.base.BaseAdapter;
import com.xxzlkj.zhaolinshare.base.base.BaseViewHolder;
import com.xxzlkj.zhaolinshare.base.util.DateUtils;
import com.xxzlkj.zhaolinshare.base.util.NumberFormatUtils;
import com.xxzlkj.zhaolinshare.base.util.PicassoUtils;
import com.xxzlkj.zhaolinshare.base.util.TextViewUtils;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 活动报名用户
 * Created by Administrator on 2017/4/24.
 */

public class FoundSignUpUserAdapter extends BaseAdapter<SignUpUser> {
    // true:用户列表页 false:活动详情页
    private boolean flag;
    public FoundSignUpUserAdapter(Context context, int itemId,boolean flag) {
        super(context, itemId);
        this.flag = flag;
    }

    @Override
    public void convert(BaseViewHolder holder, int position, SignUpUser itemBean) {
        CircleImageView mCircleImageView = holder.getView(R.id.id_fli_usericon);
        String simg = itemBean.getSimg();
        if(!TextUtils.isEmpty(simg)){
            PicassoUtils.setImageSmall(mContext,itemBean.getSimg(),mCircleImageView);
        }else {// 默认头像
            mCircleImageView.setImageResource(R.mipmap.ic_icon_def);
        }

        if (flag){
            TextView mNameTextView = holder.getView(R.id.id_fli_name);
            TextView mTimeTextView = holder.getView(R.id.id_fli_time);
            TextView mSexOldTextView = holder.getView(R.id.id_fli_sex_year);
            LinearLayout mSexOldLinearLayout = holder.getView(R.id.id_fli_sex_layout);
            ImageView mSexOldImageView = holder.getView(R.id.id_fli_sex_icon);
            TextView mContentTextView = holder.getView(R.id.id_fli_content);

            TextViewUtils.setText(mNameTextView,itemBean.getUsername());
            mContentTextView.setText(itemBean.getDesc());
            TextViewUtils.setText(mSexOldTextView,itemBean.getAge());

            String time = null;
            if ("0".equals(itemBean.getHide())){// 隐身
                time = "隐身";
            }else {// 正常
                time = DateUtils.getTimeType(System.currentTimeMillis() - NumberFormatUtils.toLong(itemBean.getLogintime()) * 1000);
            }
            TextViewUtils.setText(mTimeTextView,time);

            int sex = NumberFormatUtils.toInt(itemBean.getSex());
            switch (sex){
                case 0:// 保密
                    break;
                case 1:// 男
                    mSexOldImageView.setImageResource(R.mipmap.sex_man);
                    mSexOldLinearLayout.setBackgroundResource(R.drawable.shape_man_corners);
                    break;
                case 2:// 女
                    mSexOldImageView.setImageResource(R.mipmap.sex_woman);
                    mSexOldLinearLayout.setBackgroundResource(R.drawable.shape_woman_corners);
                    break;
            }
        }
    }
}
