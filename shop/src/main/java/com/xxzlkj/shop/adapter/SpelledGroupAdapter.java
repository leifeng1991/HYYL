package com.xxzlkj.shop.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.widget.TextView;

import com.xxzlkj.shop.R;
import com.xxzlkj.shop.activity.shop.SpelledGroupDesActivity;
import com.xxzlkj.shop.model.TeamBean;
import com.xxzlkj.shop.weight.CustomButton;
import com.xxzlkj.zhaolinshare.base.base.BaseAdapter;
import com.xxzlkj.zhaolinshare.base.base.BaseViewHolder;
import com.xxzlkj.zhaolinshare.base.util.PicassoUtils;
import com.zrq.spanbuilder.Spans;

import de.hdodenhof.circleimageview.CircleImageView;


public class SpelledGroupAdapter extends BaseAdapter<TeamBean> {
    private Activity activity;

    public SpelledGroupAdapter(Context context, Activity activity, int itemId) {
        super(context, itemId);
        this.activity = activity;
    }

    @Override
    public void convert(BaseViewHolder holder, int position, TeamBean itemBean) {
        CircleImageView mUserAvatarCircleImageView = holder.getView(R.id.id_user_avatar);// 用户头像
        TextView mUserNameTextView = holder.getView(R.id.id_user_name);// 用户名
        TextView mTuanMessageTextView = holder.getView(R.id.id_number);// 参团信息
        CustomButton mOfferedButton = holder.getView(R.id.id_offered);// 去参团

        PicassoUtils.setImageSmall(mContext, itemBean.getSimg(), mUserAvatarCircleImageView);

        // 有昵称优先显示昵称 没昵称显示电话号码
        if (TextUtils.isEmpty(itemBean.getUsername())) {
            mUserNameTextView.setText(itemBean.getPhone());
        } else {
            mUserNameTextView.setText(itemBean.getUsername());
        }

        // 跳转到参与拼团(详情)
        mOfferedButton.setOnClickListener(v -> activity.startActivity(SpelledGroupDesActivity.newIntent(mContext, itemBean.getId())));


        mTuanMessageTextView.setText(Spans.builder().text("还差").size(11).color(0xff222833).
                text(itemBean.getNumber() + "人").size(11).color(0xffff725c).text("成团").size(11).color(0xff222833).
                text("\n¥" + itemBean.getPrice() + "  " + itemBean.getNum() + "人团").size(10).color(0xff746F6E).build());
    }
}
