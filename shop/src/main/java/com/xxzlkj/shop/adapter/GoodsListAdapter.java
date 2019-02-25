package com.xxzlkj.shop.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xxzlkj.shop.R;
import com.xxzlkj.shop.activity.shop.GoodsDetailActivity;
import com.xxzlkj.shop.config.URLConstants;
import com.xxzlkj.shop.model.Goods;
import com.xxzlkj.shop.utils.GoodsUtils;
import com.xxzlkj.shop.utils.TextPriceUtil;
import com.xxzlkj.shop.weight.CustomButton;
import com.xxzlkj.zhaolinshare.base.app.BaseApplication;
import com.xxzlkj.zhaolinshare.base.base.BaseAdapter;
import com.xxzlkj.zhaolinshare.base.base.BaseViewHolder;
import com.xxzlkj.zhaolinshare.base.model.BaseBean;
import com.xxzlkj.zhaolinshare.base.model.User;
import com.xxzlkj.zhaolinshare.base.net.OnBaseRequestListener;
import com.xxzlkj.zhaolinshare.base.net.RequestManager;
import com.xxzlkj.zhaolinshare.base.util.NumberFormatUtils;
import com.xxzlkj.zhaolinshare.base.util.PixelUtil;
import com.xxzlkj.zhaolinshare.base.util.TextViewUtils;

import java.util.HashMap;

/**
 * 商品列表适配器
 * Created by Administrator on 2017/3/20.
 */

public class GoodsListAdapter extends BaseAdapter<Goods> {
    public static boolean isGirdView = true;
    private AddShopCallback callback;
    // 宫格
    private int mGirdTop;
    private int mGirdBottom;
    // 列表
    private int mListTop;
    private Activity mActivity;
    private User mUser;

    public GoodsListAdapter(Context context, Activity mActivity, int itemId, AddShopCallback callback) {
        super(context, itemId);
        this.callback = callback;
        this.mActivity = mActivity;
        mGirdTop = PixelUtil.dip2px(mContext, 8);
        mGirdBottom = PixelUtil.dip2px(mContext, 10);
        mListTop = PixelUtil.dip2px(mContext, 13);
        mUser = BaseApplication.getInstance().getLoginUser();
    }

    @Override
    public void convert(BaseViewHolder holder, int position, final Goods itemBean) {
        // 宫格
        RelativeLayout mGirdRelativeLayout = holder.getView(R.id.id_grid_layout);
        // 图片
        ImageView mGirdGoodsImage = holder.getView(R.id.id_hii_top_image);
        ImageView mCoverageTopImageView = holder.getView(R.id.id_coverage_top_image);// 商品图层
        // 宫格里的下架
        CustomButton mGirdSoldOutCustomButton = holder.getView(R.id.id_grid_sold_out);
        CustomButton mGirdNumCustomButton = holder.getView(R.id.id_grid_goods_number);
        // 列表
        RelativeLayout mListRelativeLayout = holder.getView(R.id.id_list_layout);
        // 图片
        ImageView mListGoodsImage = holder.getView(R.id.id_hii_image);
        ImageView mCoverageImageView = holder.getView(R.id.id_coverage_image);// 商品图层
        // 列表里的下架
        CustomButton mListSoldOutCustomButton = holder.getView(R.id.id_list_sold_out);
        CustomButton mListNumCustomButton = holder.getView(R.id.id_list_goods_number);

        ImageView mAddImage = holder.getView(R.id.id_hii_add);// 加入购物车
        TextView mTitle = holder.getView(R.id.id_hii_text);// 标题
        TextView mGuige = holder.getView(R.id.id_hii_norms);// 规格
        RelativeLayout mLayout = holder.getView(R.id.id_hii_layout);// 底部价格、加入购物车、立即购物布局
        LinearLayout mPriceLayout = holder.getView(R.id.id_price_layout);// 价格布局
        TextView mPrice = holder.getView(R.id.id_hii_price);// 现价
        TextView mPriceLine = holder.getView(R.id.id_hii_price_line);// 原价
        TextView mNowBuy = holder.getView(R.id.id_hii_bn);// 立即购买
        View line = holder.getView(R.id.id_hii_line);
        CustomButton mYuShouButton = holder.getView(R.id.id_yushou);// 预售

        // 分割线
        View lineTop = holder.getView(R.id.id_hii_grid_top);
        View lineRight = holder.getView(R.id.id_hii_grid_right);
        // 库存
        String stockString = itemBean.getStock();
        if (TextUtils.isEmpty(stockString)) {
            // 隐藏库存数和下架按钮的显示和隐藏
            setVisible(View.GONE, mGirdSoldOutCustomButton, mGirdNumCustomButton, mListSoldOutCustomButton, mListNumCustomButton);
        } else {
            double stock = NumberFormatUtils.toDouble(stockString);
            if (mUser != null && stock >= 0) {
                // 是店员 显示库存数和下架按钮的显示和隐藏
                setVisible(View.VISIBLE, mGirdSoldOutCustomButton, mGirdNumCustomButton, mListSoldOutCustomButton, mListNumCustomButton);
                mGirdNumCustomButton.setText(itemBean.getStock());
                mListNumCustomButton.setText(itemBean.getStock());
            } else {
                // 不是店员 隐藏库存数和下架按钮的显示和隐藏
                setVisible(View.GONE, mGirdSoldOutCustomButton, mGirdNumCustomButton, mListSoldOutCustomButton, mListNumCustomButton);
            }
        }

        if (isGirdView) {
            // 宫格
            mGirdRelativeLayout.setVisibility(View.VISIBLE);
            mListRelativeLayout.setVisibility(View.GONE);
            mAddImage.setVisibility(View.VISIBLE);
            mNowBuy.setVisibility(View.GONE);
            line.setVisibility(View.GONE);
            lineTop.setVisibility(View.VISIBLE);

            if (position % 2 == 1) {// 显示左边分割线
                lineRight.setVisibility(View.GONE);
            } else if (position % 2 == 0) {// 显示右边分割线
                lineRight.setVisibility(View.VISIBLE);
            }
            // 重新设置价格的字体大小
            mPrice.setTextSize(15f);
            mPriceLine.setTextSize(10f);
            // 添加购物车
            mAddImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (callback == null) return;
                    callback.addAction(v, itemBean);
                }
            });
            // 动态设置价格的距离顶部和底部的距离
            mLayout.setPadding(0, mGirdTop, 0, mGirdBottom);
            // 下架
            mGirdSoldOutCustomButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialog(itemBean);
                }
            });

        } else {// 列表形式
            mGirdRelativeLayout.setVisibility(View.GONE);
            mListRelativeLayout.setVisibility(View.VISIBLE);
            mAddImage.setVisibility(View.GONE);
            mNowBuy.setVisibility(View.VISIBLE);
            line.setVisibility(View.VISIBLE);
            lineTop.setVisibility(View.GONE);
            lineRight.setVisibility(View.GONE);
            // 重新设置价格的字体大小
            mPrice.setTextSize(16f);
            mPriceLine.setTextSize(11f);
            // 添加购物车
            mNowBuy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (BaseApplication.getInstance().getLoginUserDoLogin(mActivity) != null) {
                        mActivity.startActivity(GoodsDetailActivity.newIntent(mContext, itemBean.getId()));
                    }
                }
            });
            // 动态设置价格的距离顶部和底部的距离
            mLayout.setPadding(0, mListTop, 0, 0);
            // 下架
            mListSoldOutCustomButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialog(itemBean);
                }
            });


        }

        // 设置价格方向
        mPriceLayout.setOrientation(isGirdView ? LinearLayout.VERTICAL : LinearLayout.HORIZONTAL);
        GoodsUtils.setYuShouVisible(itemBean.getActivitys(), isGirdView ? mAddImage : mNowBuy, mYuShouButton, null);
        TextViewUtils.setText(mTitle, itemBean.getTitle());
        TextViewUtils.setText(mGuige, itemBean.getAds());
        String price = itemBean.getPrice();
        String prices = itemBean.getPrices();
        TextPriceUtil.setTextPrices(price, prices, mPriceLine);
        TextViewUtils.setText(mPrice, "￥" + price);

        String simg = itemBean.getSimg();
        if (!TextUtils.isEmpty(simg)) {
            // 设置商品图片和商品图层
            GoodsUtils.setGoodsCoverageImage(mContext, mGirdGoodsImage, mCoverageTopImageView, itemBean.getSimg(), itemBean.getActivity_icon_img());
            GoodsUtils.setGoodsCoverageImage(mContext, mListGoodsImage, mCoverageImageView, itemBean.getSimg(), itemBean.getActivity_icon_img());
        }
    }

    public interface AddShopCallback {
        void addAction(View view, Goods goods);
    }

    /**
     * 设置显示和隐藏
     *
     * @param visible
     * @param customButtons
     */
    private void setVisible(int visible, CustomButton... customButtons) {
        for (int i = 0; i < customButtons.length; i++) {
            customButtons[i].setVisibility(visible);
        }

    }

    /**
     * 提示框
     */
    private void showDialog(Goods goods) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage("是否下架该商品");
        builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                goodsSoldOut(goods);
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    /**
     * 商品下架
     *
     * @param goods 删除商品
     */
    private void goodsSoldOut(Goods goods) {
        HashMap<String, String> stringStringHashMap = new HashMap<>();
        User loginUser = BaseApplication.getInstance().getLoginUser();
        if (loginUser == null) return;
        // 会员id(必传)
        stringStringHashMap.put(URLConstants.REQUEST_PARAM_UID, loginUser.getData().getId());
        // 商品id (必传)
        stringStringHashMap.put(URLConstants.REQUEST_PARAM_GOODS_ID, goods.getId());
        RequestManager.createRequest(URLConstants.GOOD_DOWN_URL, stringStringHashMap, new OnBaseRequestListener<BaseBean>() {
            @Override
            public void handlerSuccess(BaseBean bean) {
                getList().remove(goods);
                notifyDataSetChanged();
            }

            @Override
            public void handlerError(int errorCode, String errorMessage) {

            }

        });
    }

}
