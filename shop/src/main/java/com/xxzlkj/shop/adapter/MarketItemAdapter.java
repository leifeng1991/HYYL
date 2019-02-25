package com.xxzlkj.shop.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sunfusheng.marqueeview.DisplayUtil;
import com.xxzlkj.shop.R;
import com.xxzlkj.shop.model.Goods;
import com.xxzlkj.shop.utils.GoodsUtils;
import com.xxzlkj.shop.utils.TextPriceUtil;
import com.xxzlkj.shop.weight.CustomButton;
import com.xxzlkj.zhaolinshare.base.base.BaseAdapter;
import com.xxzlkj.zhaolinshare.base.base.BaseViewHolder;
import com.xxzlkj.zhaolinshare.base.util.PicassoUtils;
import com.xxzlkj.zhaolinshare.base.util.TextViewUtils;


/**
 * 商城Item适配器
 * Created by Administrator on 2017/3/13.
 */

public class MarketItemAdapter extends BaseAdapter<Goods> {
    private int type;
    private AddShopCallback callback;
    private int widthAndHeight;

    public MarketItemAdapter(Context context, Activity mActivity, int itemId, int type) {
        super(context, itemId);
        this.type = type;
        widthAndHeight = (int) (DisplayUtil.getWindowWidth(mActivity) / 3.5);
    }

    public MarketItemAdapter(Context context, int itemId, int type, AddShopCallback callback) {
        super(context, itemId);
        this.type = type;
        this.callback = callback;
    }

    @Override
    public void convert(BaseViewHolder holder, int position, final Goods itemBean) {
        ImageView mImageView = holder.getView(R.id.id_hii_image);
        ImageView mCoverageImageView = holder.getView(R.id.id_coverage_image);
        TextView mDetailTextView = holder.getView(R.id.id_hii_text);
        TextView mPriceTextView = holder.getView(R.id.id_hii_price);// 现价
        CustomButton mYuShouButton = holder.getView(R.id.id_yushou_btn);// 預售

        if (type == 1) {
            final ImageView mAddImageView = holder.getView(R.id.id_hii_add);
            mAddImageView.setOnClickListener(v -> {
                if (callback == null) return;
                callback.addAction(v, itemBean);
            });

            GoodsUtils.setYuShouVisible(itemBean.getActivitys(), mAddImageView, mYuShouButton, null);
        }

        if (type == 3) {
            TextView mNormsTextView = holder.getView(R.id.id_hii_norms);
            TextView mLineTextView = holder.getView(R.id.id_hii_price_line);
            TextView mBuyNowTextView = holder.getView(R.id.id_hii_bn);
            TextViewUtils.setText(mNormsTextView, itemBean.getAds());
            TextPriceUtil.setTextPrices(itemBean.getPrice(), itemBean.getPrices(), mLineTextView);// 设置原价
            GoodsUtils.setYuShouVisible(itemBean.getActivitys(), mBuyNowTextView, mYuShouButton, null);
        }

        if (type == 2) {
            RelativeLayout layout = holder.getView(R.id.id_hii_parent);
            layout.setLayoutParams(new RelativeLayout.LayoutParams(widthAndHeight, RelativeLayout.LayoutParams.MATCH_PARENT));
            PicassoUtils.setWithAndHeight(mContext, itemBean.getSimg(), widthAndHeight, widthAndHeight, mImageView);
            // 图片图层为空不显示
            if (TextUtils.isEmpty(itemBean.getActivity_icon_img())){
                mCoverageImageView.setVisibility(View.GONE);
            }else {
                mCoverageImageView.setVisibility(View.VISIBLE);
                PicassoUtils.setWithAndHeight(mContext, itemBean.getSimg(), widthAndHeight, widthAndHeight, mCoverageImageView);
            }

            GoodsUtils.setYuShouVisible(itemBean.getActivitys(), null, mYuShouButton, null);
        } else {
            // 设置商品图片和商品图层
            GoodsUtils.setGoodsCoverageImage(mContext, mImageView, mCoverageImageView, itemBean.getSimg(), itemBean.getActivity_icon_img());
        }
        TextViewUtils.setText(mDetailTextView, itemBean.getTitle());
        TextViewUtils.setText(mPriceTextView, "￥" + itemBean.getPrice());
    }

    public interface AddShopCallback {
        void addAction(View view, Goods goods);
    }

}
