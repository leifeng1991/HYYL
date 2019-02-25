package com.xxzlkj.huayiyanglao.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xxzlkj.huayiyanglao.R;
import com.xxzlkj.huayiyanglao.model.HealthGoodsListBean;
import com.xxzlkj.zhaolinshare.base.base.BaseAdapter;
import com.xxzlkj.zhaolinshare.base.base.BaseViewHolder;
import com.xxzlkj.zhaolinshare.base.util.NumberFormatUtils;
import com.xxzlkj.zhaolinshare.base.util.PicassoUtils;
import com.xxzlkj.zhaolinshare.base.util.StringUtil;

/**
 * 描述:
 *
 * @author leifeng
 *         2017/11/24 11:31
 */


public class TwoLevelListAdapter extends BaseAdapter<HealthGoodsListBean.DataBean> {
    public TwoLevelListAdapter(Context context, int itemId) {
        super(context, itemId);
    }

    @Override
    public void convert(BaseViewHolder holder, int position, HealthGoodsListBean.DataBean itemBean) {
        ImageView mImageView = holder.getView(R.id.id_image);// 图片
        TextView mPriceTextView = holder.getView(R.id.id_price);// 类型
        TextView mTitleTextView = holder.getView(R.id.id_title);// 标题
        TextView mYyylTimeTextView = holder.getView(R.id.id_yyyl_time);// 时间(医养医疗特有)
        TextView mYyylLocationTextView = holder.getView(R.id.id_yyyl_location);// 地址(医养医疗特有)
        TextView mContentTextView = holder.getView(R.id.id_content);// 描述
        TextView mNoteTextView = holder.getView(R.id.id_time);// 备注
        // 设置数据
        PicassoUtils.setImageBig(mContext, itemBean.getSimg(), mImageView);
        // 价格
        String price = itemBean.getPrice();
        String type = itemBean.getType();
        if (TextUtils.isEmpty(price) || NumberFormatUtils.toDouble(price) <= 0) {
            if ("1".equals(type) && NumberFormatUtils.toDouble(price) == 0) {
                // 是预约模板并且价格为零 显示免费
                mPriceTextView.setVisibility(View.VISIBLE);
                mPriceTextView.setText("免费");
            } else {
                // 隐藏价格
                mPriceTextView.setVisibility(View.GONE);
            }
        } else {
            mPriceTextView.setVisibility(View.VISIBLE);
            String unit_desc = itemBean.getUnit_desc();
            mPriceTextView.setText(String.format("¥%s%s", price, TextUtils.isEmpty(unit_desc) ? "" : "/" + unit_desc));
        }

        mTitleTextView.setText(itemBean.getTitle());
        if ("1".equals(type)) {
            // 预约模板
            mYyylTimeTextView.setVisibility(View.VISIBLE);
            mYyylLocationTextView.setVisibility(View.VISIBLE);
            mYyylTimeTextView.setText(String.format("时长：%s", itemBean.getShow_time()));
            mYyylLocationTextView.setText(String.format("地点：%s", itemBean.getAddress()));
        } else {
            // 其他
            mYyylTimeTextView.setVisibility(View.GONE);
            mYyylLocationTextView.setVisibility(View.GONE);
        }
        mContentTextView.setText(itemBean.getAds());

        if (TextUtils.isEmpty(itemBean.getRemark())) {
            mNoteTextView.setVisibility(View.GONE);
        } else {
            mNoteTextView.setVisibility(View.VISIBLE);
            mNoteTextView.setText(itemBean.getRemark());
        }
    }
}
