package com.xxzlkj.shop.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xxzlkj.shop.R;
import com.xxzlkj.shop.activity.shop.GoodsDetailActivity;
import com.xxzlkj.shop.activity.shop.SearchGoodsListActivity;
import com.xxzlkj.shop.config.StringConstants;
import com.xxzlkj.shop.event.AddShopCartActionEvent;
import com.xxzlkj.shop.interfac.ShopLibraryInterface;
import com.xxzlkj.shop.model.Goods;
import com.xxzlkj.shop.model.Market;
import com.xxzlkj.zhaolinshare.base.app.BaseApplication;
import com.xxzlkj.zhaolinshare.base.base.BaseAdapter;
import com.xxzlkj.zhaolinshare.base.base.BaseViewHolder;
import com.xxzlkj.zhaolinshare.base.util.NumberFormatUtils;
import com.xxzlkj.zhaolinshare.base.util.PicassoUtils;
import com.xxzlkj.zhaolinshare.base.util.PixelUtil;

import org.greenrobot.eventbus.EventBus;


/**
 * 商城适配器
 * Created by Administrator on 2017/3/13.
 */

public class MarketAdapter extends BaseAdapter<Market> {
    private int mWidth;
    private int mHeight;
    private Activity mActivity;

    public MarketAdapter(Context context, Activity mActivity, int itemId) {
        super(context, itemId);
        this.mActivity = mActivity;
        mWidth = PixelUtil.getScreenWidth(context);
        mHeight = mWidth / 2;
    }

    @Override
    public void convert(BaseViewHolder holder, int position, final Market itemBean) {
        LinearLayout mHeaderLayout = holder.getView(R.id.id_hi_header);// 头
        ImageView mLeftThemeImage = holder.getView(R.id.id_hh_image);// 头--图片
        TextView mLeftThemeText = holder.getView(R.id.id_hh_theme);// 头--名称
        LinearLayout mMoreLayout = holder.getView(R.id.id_hi_ll);// 头--更多
        ImageView mHeaderImage = holder.getView(R.id.id_hi_image);// banner

        mMoreLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.startActivity(SearchGoodsListActivity.newIntent(mContext, 4, itemBean.getId(), "", ""));
            }
        });
        mHeaderImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(itemBean.getTo())) {
                    switch (NumberFormatUtils.toInt(itemBean.getType())) {
                        case StringConstants.INTENT_TO_NO:// 不跳转
                            break;
                        case StringConstants.INTENT_TO_H5:// 跳转H5
                            if (BaseApplication.getInstance() instanceof ShopLibraryInterface) {
                                // 让主项目处理
                                ((ShopLibraryInterface) BaseApplication.getInstance()).jumpToHasTitleWebView(mActivity, itemBean.getTo(), "详情");
                            }
                            break;
                        case StringConstants.INTENT_TO_GOODS:// 跳转商品详情
                            mActivity.startActivity(GoodsDetailActivity.newIntent(mContext, itemBean.getTo()));
                            break;
                        case StringConstants.INTENT_TO_GOODSTYPE:// 跳转搜索商品列表
                            mActivity.startActivity(SearchGoodsListActivity.newIntent(mContext, -1, itemBean.getTo(), "", ""));
                            break;
                        case 11:// 本身
                            mActivity.startActivity(SearchGoodsListActivity.newIntent(mContext, 4, itemBean.getId(), "", ""));
                            break;
                    }
                }
            }
        });

        if (TextUtils.isEmpty(itemBean.getSimg())) {
            mHeaderImage.setVisibility(View.GONE);
        } else {
            mHeaderImage.setVisibility(View.VISIBLE);
            PicassoUtils.setWithAndHeight(mContext, itemBean.getSimg(), mWidth, mHeight, mHeaderImage);
        }

        RecyclerView mRecyclerView = holder.getView(R.id.id_hi_recyclerview);

        MarketItemAdapter itemAdapter = null;
        switch (itemBean.getPid()) {
            case StringConstants.HOT_RECOMMEND_TYPE_1:// 样式一
                mHeaderLayout.setVisibility(View.VISIBLE);
                mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
                PicassoUtils.setImageSmall(mContext, itemBean.getIcon(), mLeftThemeImage);
                mLeftThemeText.setText(itemBean.getTitle());
                itemAdapter = new MarketItemAdapter(mContext, R.layout.adapter_hr_item_item_1, StringConstants.HOT_RECOMMEND_TYPE_1, new MarketItemAdapter.AddShopCallback() {
                    @Override
                    public void addAction(View view, Goods goods) {
                        if (BaseApplication.getInstance().getLoginUserDoLogin(mActivity) != null) {
                            EventBus.getDefault().postSticky(new AddShopCartActionEvent(view, goods, true));
                        }
                    }
                });
                break;
            case StringConstants.HOT_RECOMMEND_TYPE_2:// 样式二
                mHeaderLayout.setVisibility(View.VISIBLE);
                LinearLayoutManager mLayoutManager = new LinearLayoutManager(mContext);
                mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                mRecyclerView.setLayoutManager(mLayoutManager);
                PicassoUtils.setImageSmall(mContext, itemBean.getIcon(), mLeftThemeImage);
                mLeftThemeText.setText(itemBean.getTitle());
                itemAdapter = new MarketItemAdapter(mContext, mActivity, R.layout.adapter_hr_item_item_2, StringConstants.HOT_RECOMMEND_TYPE_2);
                itemAdapter.setOnItemClickListener(new OnItemClickListener<Goods>() {
                    @Override
                    public void onClick(int position, Goods item) {
                        // 跳转到商品详情
                        mActivity.startActivity(GoodsDetailActivity.newIntent(mContext, item.getId()));
                    }
                });
                break;
            case StringConstants.HOT_RECOMMEND_TYPE_3:// 样式三
                mHeaderLayout.setVisibility(View.GONE);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
                itemAdapter = new MarketItemAdapter(mContext, mActivity, R.layout.adapter_shop_home_style_4_item, StringConstants.HOT_RECOMMEND_TYPE_3);
                break;
        }

        itemAdapter.addList(itemBean.getGoods());
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setAdapter(itemAdapter);
        itemAdapter.setOnItemClickListener((position1, item) -> mActivity.startActivity(GoodsDetailActivity.newIntent(mContext, item.getId())));
    }

}
