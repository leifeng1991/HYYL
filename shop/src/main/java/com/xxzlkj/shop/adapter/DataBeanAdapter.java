package com.xxzlkj.shop.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xxzlkj.shop.R;
import com.xxzlkj.shop.model.CofirmOrderBean;
import com.xxzlkj.shop.model.ShopCartList;
import com.xxzlkj.shop.utils.GoodsUtils;
import com.xxzlkj.shop.utils.TextPriceUtil;
import com.xxzlkj.shop.weight.CustomButton;
import com.xxzlkj.zhaolinshare.base.base.BaseAdapter;
import com.xxzlkj.zhaolinshare.base.base.BaseViewHolder;
import com.xxzlkj.zhaolinshare.base.util.NumberFormatUtils;
import com.xxzlkj.zhaolinshare.base.util.TextViewUtils;

import java.util.List;

/**
 * 确认订单商品适配器
 * Created by Administrator on 2017/3/27.
 */

public class DataBeanAdapter extends BaseAdapter<ShopCartList.DataBean.GBean> {
    private List<CofirmOrderBean.DataBean> dataBeanList;

    public DataBeanAdapter(Context context, List<CofirmOrderBean.DataBean> dataBeanList, int itemId) {
        super(context, itemId);
        this.dataBeanList = dataBeanList;
    }

    @Override
    public void convert(BaseViewHolder holder, int position, ShopCartList.DataBean.GBean itemBean) {
        ImageView mImageView = holder.getView(R.id.id_hii_image);
        ImageView mCoverageImageView = holder.getView(R.id.id_coverage_image);
        TextView mTitleTextView = holder.getView(R.id.id_hii_text);
        TextView mGuiGeTextView = holder.getView(R.id.id_hii_norms);
        TextView mPriceTextView = holder.getView(R.id.id_hii_price);
        TextView mPricesTextView = holder.getView(R.id.id_hii_price_line);
        TextView mNumberTextView = holder.getView(R.id.id_hii_number);
        TextView mTipTextView = holder.getView(R.id.id_hii_tip);
        LinearLayout mAdvanceLayout = holder.getView(R.id.id_advance_layout);// 预售/团购布局
        CustomButton mAdvanceButton = holder.getView(R.id.id_advance);// 预售/团购
        TextView mAdvanceTextView = holder.getView(R.id.id_advance_desc);// 预售/团购描述

        // 设置商品图片和商品图层
        GoodsUtils.setGoodsCoverageImage(mContext, mImageView, mCoverageImageView, itemBean.getSimg(), itemBean.getActivity_icon_img());

        TextViewUtils.setText(mTitleTextView, itemBean.getTitle());
        // 广告语 如果没有不显示
        if (TextUtils.isEmpty(itemBean.getAds())) {
            mGuiGeTextView.setVisibility(View.INVISIBLE);
        } else {
            mGuiGeTextView.setVisibility(View.VISIBLE);
            mGuiGeTextView.setText(itemBean.getAds());
        }
        TextViewUtils.setText(mNumberTextView, "X" + itemBean.getNum());
        TextViewUtils.setText(mPriceTextView, "￥" + itemBean.getPrice());
        TextPriceUtil.setTextPrices(itemBean.getPrice(), itemBean.getPrices(), mPricesTextView);// 设置原价

        if (dataBeanList != null && dataBeanList.size() > 0 && dataBeanList.get(position) != null) {
            CofirmOrderBean.DataBean dataBean = dataBeanList.get(position);
            // 当前库存数
            double stock = NumberFormatUtils.toDouble(dataBean.getStock());
            // 购物车中该商品数量
            double num = NumberFormatUtils.toDouble(itemBean.getNum());
            if (num > stock) {
                mTipTextView.setVisibility(View.VISIBLE);
            } else {
                mTipTextView.setVisibility(View.GONE);
            }

            String activitys = dataBean.getActivitys();
            if ("1".equals(activitys)) {
                // 预售
                mAdvanceLayout.setVisibility(View.VISIBLE);
                mAdvanceButton.setText("预售");
                mAdvanceTextView.setText(dataBean.getActivity_desc());
            } else if ("2".equals(activitys)) {
                // 团购
                mAdvanceLayout.setVisibility(View.VISIBLE);
                mAdvanceButton.setText("团购");
                mAdvanceTextView.setText(dataBean.getActivity_desc());
            } else {
                // 其他
                mAdvanceLayout.setVisibility(View.GONE);
            }

        } else {
            mAdvanceLayout.setVisibility(View.GONE);
        }


    }
}
