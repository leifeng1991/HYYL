package com.xxzlkj.shop.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.xxzlkj.shop.R;
import com.xxzlkj.shop.activity.shop.GoodsDetailActivity;
import com.xxzlkj.shop.model.ShopActionThemeBean;
import com.xxzlkj.zhaolinshare.base.base.BaseAdapter;
import com.xxzlkj.zhaolinshare.base.base.BaseViewHolder;
import com.xxzlkj.zhaolinshare.base.util.TextViewUtils;


public class ShopActionThemAdapter extends BaseAdapter<ShopActionThemeBean.DataBean.BrandBean> {
    private Activity mActivity;

    public ShopActionThemAdapter(Context context, Activity mActivity, int itemId) {
        super(context, itemId);
        this.mActivity = mActivity;
    }

    @Override
    public void convert(BaseViewHolder holder, int position, ShopActionThemeBean.DataBean.BrandBean itemBean) {
        TextView mTitleTextView = holder.getView(R.id.id_sati_title);
        RecyclerView mRecyclerView = holder.getView(R.id.id_sati_recycler);
        // 设置头
        TextViewUtils.setText(mTitleTextView, "—  ", itemBean.getTitle(), "  —");
        // 设置内容
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 2));
        mRecyclerView.setNestedScrollingEnabled(false);
        ShopActionThemItemAdapter itemAdapter = new ShopActionThemItemAdapter(mContext, R.layout.adapter_shop_action_them_item);
        mRecyclerView.setAdapter(itemAdapter);
        itemAdapter.addList(itemBean.getGoods());
        // 跳转到商品详情
        itemAdapter.setOnItemClickListener((position1, item) -> mActivity.startActivity(GoodsDetailActivity.newIntent(mContext, item.getId())));
    }
}
