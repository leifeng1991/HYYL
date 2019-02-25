package com.xxzlkj.shop.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xxzlkj.shop.R;
import com.xxzlkj.shop.activity.shop.GoodsDetailActivity;
import com.xxzlkj.shop.activity.shop.SpelledGroupDesActivity;
import com.xxzlkj.shop.model.Goods;
import com.xxzlkj.shop.weight.CustomButton;
import com.xxzlkj.zhaolinshare.base.base.BaseAdapter;
import com.xxzlkj.zhaolinshare.base.base.BaseViewHolder;
import com.xxzlkj.zhaolinshare.base.util.PicassoUtils;
import com.xxzlkj.zhaolinshare.base.util.PixelUtil;
import com.zrq.spanbuilder.Spans;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * by Administrator on 2017/11/14.
 */

public class TuanGouStyleAdapter2 extends BaseAdapter<Goods> {
    private boolean isShopHome;
    private Activity activity;

    /**
     * @param isShopHome 是否是商城首页
     */
    public TuanGouStyleAdapter2(Context context, Activity activity, boolean isShopHome, int itemId) {
        super(context, itemId);
        this.isShopHome = isShopHome;
        this.activity = activity;
    }

    @Override
    public void convert(BaseViewHolder holder, int position, Goods itemBean) {
        if (isShopHome) {
            LinearLayout mParentLayout = holder.getView(R.id.id_parent_layout);
            View mLeftLineView = holder.getView(R.id.id_left_line);
            mLeftLineView.setVisibility(position == 0 ? View.VISIBLE : View.GONE);
            mParentLayout.getLayoutParams().width = (int) (PixelUtil.getScreenWidth(mContext) / 2.5);
        }

        ImageView mImageView = holder.getView(R.id.id_image);// 图片
        TextView mTitleTextView = holder.getView(R.id.id_title);// 标题
        CircleImageView mUserAvatarCircleImageView = holder.getView(R.id.id_user_avatar);// 用户头像
        TextView mUserNameTextView = holder.getView(R.id.id_user_name);// 用户名
        TextView mOpenMassStateTextView = holder.getView(R.id.id_open_mass_state);// 团购状态
        TextView mPriceTextView = holder.getView(R.id.id_price);// 商品价格
        TextView mTuanPeopleTextView = holder.getView(R.id.id_tuan_people);// 团购人数
        CustomButton mOfferedButton = holder.getView(R.id.id_offered);// 参团

        // 团购小组ID 如果为空则暂时还没有人开团 隐藏用户名和用户头像 以及相应的状态
        String id = itemBean.getId();
        if (TextUtils.isEmpty(id)) {
            mUserAvatarCircleImageView.setVisibility(View.GONE);
            mUserNameTextView.setVisibility(View.GONE);
            mOpenMassStateTextView.setText("尚未开团");
            mOfferedButton.setText("开团");
            mTuanPeopleTextView.setText(String.format("%s人团", itemBean.getNum()));
        } else {
            mUserAvatarCircleImageView.setVisibility(View.VISIBLE);
            mUserNameTextView.setVisibility(View.VISIBLE);
            PicassoUtils.setImageBig(mContext, itemBean.getImg(), mUserAvatarCircleImageView);
            // 如果用户名为空显示手机号
            String username = itemBean.getUsername();
            mUserNameTextView.setText(TextUtils.isEmpty(username) ? itemBean.getPhone() : username);
            mOpenMassStateTextView.setText("已开团");
            mOfferedButton.setText("参团");
            mTuanPeopleTextView.setText(Spans.builder().text("差").color(0xff969696).
                    text(itemBean.getNumber() + "人").color(0xffff725c).text("·" + itemBean.getNum() + "人团").build());
        }

        mPriceTextView.setText(String.format("￥%s", itemBean.getPrice()));

        PicassoUtils.setImageBig(mContext, itemBean.getSimg(), mImageView);
        mTitleTextView.setText(itemBean.getTitle());

        // 参团/开团点击事件
        mOfferedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = mOfferedButton.getText().toString();
                if ("参团".equals(s)) {
                    // 跳转到参与拼团(详情)
                    activity.startActivity(SpelledGroupDesActivity.newIntent(mContext, itemBean.getId()));
                } else {
                    // 跳转到商品详情
                    activity.startActivity(GoodsDetailActivity.newIntent(mContext, itemBean.getGoods_id()));
                }
            }
        });

    }
}
