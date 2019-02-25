package com.xxzlkj.huayiyanglao.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.sunfusheng.marqueeview.DisplayUtil;
import com.xxzlkj.huayiyanglao.R;
import com.xxzlkj.huayiyanglao.model.Collect;
import com.xxzlkj.shop.activity.shop.GoodsDetailActivity;
import com.xxzlkj.shop.utils.GoodsUtils;
import com.xxzlkj.shop.utils.TextPriceUtil;
import com.xxzlkj.shop.weight.CustomButton;
import com.xxzlkj.zhaolinshare.base.base.BaseAdapter;
import com.xxzlkj.zhaolinshare.base.base.BaseViewHolder;
import com.xxzlkj.zhaolinshare.base.util.PicassoUtils;
import com.xxzlkj.zhaolinshare.base.util.TextViewUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 商品收藏
 * Created by Administrator on 2017/4/19.
 */

public class GoodsCollectAdapter extends BaseAdapter<Collect.DataBean> {
    private int wh;
    public static boolean isVisibelCheckbox;
    // 用来控制CheckBox的选中状况
    Map<Integer, Boolean> isSelected = new HashMap<>();
    OnIsAllCheckedChangeListener listener;
    private Activity activity;

    public GoodsCollectAdapter(Context context, Activity activity, int itemId) {
        super(context, itemId);
        this.activity = activity;
        wh = DisplayUtil.dip2px(mContext, 100);
        for (int i = 0; i < getItemCount(); i++) {
            isSelected.put(i, false);
        }
        isVisibelCheckbox = false;
    }

    @Override
    public void convert(final BaseViewHolder holder, final int position, final Collect.DataBean itemBean) {
        final CheckBox mCheckBox = holder.getView(R.id.id_bli_checkbox);// 选中按钮
        ImageView mImageView = holder.getView(R.id.id_hii_image);// 商品图片
        ImageView mCoverageImageView = holder.getView(R.id.id_coverage_image);// 商品图片图层
        TextView mGoodsSate = holder.getView(R.id.id_collect_goods_state);// 商品状态
        TextView mTitleTextView = holder.getView(R.id.id_hii_text);// 标题
        TextView mAdsTextView = holder.getView(R.id.id_hii_norms);// 广告语
        TextView mPriceTextView = holder.getView(R.id.id_hii_price);// 现价
        TextView mPricesTextView = holder.getView(R.id.id_hii_price_line);// 原价
        TextView mNowBuyTextView = holder.getView(R.id.id_hii_bn);// 立即购买
        CustomButton mYuShouButton = holder.getView(R.id.id_yushou_btn);// 预售

        if (!"2".equals(itemBean.getState())) {
            // 显示已下架
            mGoodsSate.setVisibility(View.VISIBLE);
        } else {
            // 不显示已下架
            mGoodsSate.setVisibility(View.GONE);
        }

        if (isVisibelCheckbox) {
            // 显示选中按钮
            mCheckBox.setVisibility(View.VISIBLE);
        } else {
            // 不显示选中按钮
            mCheckBox.setVisibility(View.GONE);
        }

        // 选中监听事件
        mCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                mCheckBox.setChecked(true);
                isSelected.put(position, true);
            }

            if (!isChecked) {
                mCheckBox.setChecked(false);
                isSelected.put(position, false);
            }

            isAllSelect();
        });

        if (isSelected.get(position) == null) {
            isSelected.put(position, false);
            mCheckBox.setChecked(false);
        } else {
            if (isSelected.get(position)) {
                mCheckBox.setChecked(true);
            } else {
                mCheckBox.setChecked(false);
            }
        }

        PicassoUtils.setWithAndHeight(mContext, itemBean.getSimg(), wh, wh, mImageView);

        TextViewUtils.setText(mTitleTextView, itemBean.getTitle());
        TextViewUtils.setText(mAdsTextView, itemBean.getAds());
        TextViewUtils.setText(mPriceTextView, itemBean.getPrice());
        TextPriceUtil.setTextPrices(itemBean.getPrice(), itemBean.getPrices(), mPricesTextView);// 设置原价

        mNowBuyTextView.setOnClickListener(v -> activity.startActivity(GoodsDetailActivity.newIntent(mContext, itemBean.getGoods_id())));

        // 控制预售字样的显示和隐藏
        GoodsUtils.setYuShouVisible("", mNowBuyTextView, mYuShouButton, null);

        // 设置商品图片和商品图层
        GoodsUtils.setGoodsCoverageImage(mContext, mImageView, mCoverageImageView, itemBean.getSimg(), itemBean.getActivity_icon_img());
    }

    /**
     * 全选
     */
    public void selectAll() {
        for (int i = 0; i < getItemCount(); i++) {
            isSelected.put(i, true);
        }
        notifyDataSetChanged();
    }

    /**
     * 取消选择
     */
    public void cancelSelected() {
        for (int i = 0; i < getItemCount(); i++) {
            isSelected.put(i, false);
        }
        notifyDataSetChanged();
    }

    /**
     * 反选
     */
    public void invertSelected() {
        for (int i = 0; i < getItemCount(); i++) {
            if (isSelected.get(i)) {
                isSelected.put(i, false);
            } else {
                isSelected.put(i, true);
            }
        }
        notifyDataSetChanged();
    }

    private void isAllSelect() {
        int selectCount = 0;
        for (int i = 0; i < getItemCount(); i++) {
            Boolean aBoolean = isSelected.get(i);
            if (!aBoolean) {
                break;
            } else {
                selectCount += 1;
            }
        }

        if (listener == null) {

        } else {
            if (getItemCount() == selectCount) {
                listener.isAllChecked(true);
            } else {
                listener.isAllChecked(false);
            }
        }
    }

    /**
     * 删除
     */
    public void delete(int position) {
        getList().remove(position);
    }

    public Map<Integer, Boolean> getCheckMap() {
        return this.isSelected;
    }

    public void setOnIsAllChecked(OnIsAllCheckedChangeListener listener) {
        this.listener = listener;
    }

    public interface OnIsAllCheckedChangeListener {
        void isAllChecked(boolean isAllChecked);
    }
}
