package com.xxzlkj.huayiyanglao.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sunfusheng.marqueeview.DisplayUtil;
import com.xxzlkj.huayiyanglao.R;
import com.xxzlkj.huayiyanglao.model.Found;
import com.xxzlkj.zhaolinshare.base.base.BaseAdapter;
import com.xxzlkj.zhaolinshare.base.base.BaseViewHolder;
import com.xxzlkj.zhaolinshare.base.util.DateUtils;
import com.xxzlkj.zhaolinshare.base.util.NumberFormatUtils;
import com.xxzlkj.zhaolinshare.base.util.PicassoUtils;
import com.xxzlkj.zhaolinshare.base.util.TextViewUtils;

/**
 * 发现适配器
 * Created by Administrator on 2017/4/10.
 */

public class FoundActivityAdapter extends BaseAdapter<Found.DataBean> {
    private int mWidth;

    public FoundActivityAdapter(Context context, Activity activity, int itemId) {
        super(context, itemId);
        mWidth = DisplayUtil.getWindowWidth(activity) - DisplayUtil.dip2px(context, 20);
    }

    @Override
    public void convert(BaseViewHolder holder, int position, Found.DataBean itemBean) {
        ImageView mImageView = holder.getView(R.id.id_found_image);
        TextView mTitleTextView = holder.getView(R.id.id_found_title);
        TextView mLocationTextView = holder.getView(R.id.id_found_location_text);
        TextView mBrowserTextView = holder.getView(R.id.id_found_browser);
        TextView mCountTextView = holder.getView(R.id.id_found_count);
        TextView mCellionTextView = holder.getView(R.id.id_found_cell);
        TextView mSignUpNumberTextView = holder.getView(R.id.id_found_sign_up);
        View mLine = holder.getView(R.id.id_found_view);
        RelativeLayout mLayout1 = holder.getView(R.id.id_layout_1);
        LinearLayout mLayout2 = holder.getView(R.id.id_layout_2);
        LinearLayout mLayout3 = holder.getView(R.id.id_found_layout);

        // 最后一条线不显示
        if (position == getItemCount() - 1) {
            mLine.setVisibility(View.GONE);
        } else {
            mLine.setVisibility(View.VISIBLE);
        }

        if ("3".equals(itemBean.getType())){
            // 类型为url 跳转H5
            mLayout1.setVisibility(View.GONE);
            mLayout2.setVisibility(View.GONE);
            mLayout3.setVisibility(View.GONE);
        }else {
            mLayout1.setVisibility(View.VISIBLE);
            mLayout2.setVisibility(View.VISIBLE);

            if ("1".equals(itemBean.getSign())) {// 1 开启报名
                mLayout3.setVisibility(View.VISIBLE);
            } else {// 0 关闭报名
                mLayout3.setVisibility(View.GONE);
            }
            TextViewUtils.setText(mLocationTextView, itemBean.getAddress());
            TextViewUtils.setText(mCountTextView, itemBean.getPv());
            TextViewUtils.setText(mBrowserTextView, DateUtils.getMonthDay(NumberFormatUtils.toLong(itemBean.getStarttime()) * 1000));
            TextViewUtils.setText(mSignUpNumberTextView, itemBean.getCount());
            TextViewUtils.setText(mCellionTextView, itemBean.getCell());
        }

        PicassoUtils.setWithAndHeight(mContext, itemBean.getSimg(), mWidth, (int) (mWidth * (1.0 * 400 / 730)), mImageView);
        TextViewUtils.setText(mTitleTextView, itemBean.getTitle());


    }
}
