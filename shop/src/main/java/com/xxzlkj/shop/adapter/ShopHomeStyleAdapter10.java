package com.xxzlkj.shop.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xxzlkj.shop.R;
import com.xxzlkj.shop.activity.shop.GoodsDetailActivity;
import com.xxzlkj.shop.model.Goods;
import com.xxzlkj.zhaolinshare.base.base.BaseAdapter;
import com.xxzlkj.zhaolinshare.base.base.BaseViewHolder;
import com.xxzlkj.zhaolinshare.base.util.StringUtil;


public class ShopHomeStyleAdapter10 extends BaseAdapter<Goods> {
    private Activity mActivity;

    public ShopHomeStyleAdapter10(Context context, Activity mActivity, int itemId) {
        super(context, itemId);
        this.mActivity = mActivity;
    }

    @Override
    public void convert(BaseViewHolder holder, int position, Goods itemBean) {
        ViewGroup vg_title_layout = holder.getView(R.id.vg_title_layout);// title布局
        TextView tv_day_top = holder.getView(R.id.tv_day_top);// 顶部时间
        TextView tv_day_bottom = holder.getView(R.id.tv_day_bottom);// 底部时间
        RecyclerView rv_content = holder.getView(R.id.rv_content);// 内容
        // 初始化RecyclerView
        rv_content.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        rv_content.setNestedScrollingEnabled(false);
        TuanGouStyleAdapter1 mContentAdapter = new TuanGouStyleAdapter1(mContext, R.layout.adapter_shop_home_style_10_item_item);
        rv_content.setAdapter(mContentAdapter);

        // 设置数据
        if (position == 0) {
            // 第一个不显示，title
            vg_title_layout.setVisibility(View.GONE);
        } else {
            vg_title_layout.setVisibility(View.VISIBLE);
            String[] substring = StringUtil.getSubstring(itemBean.getTitle());
            tv_day_top.setText(substring[0]);// 顶部时间
            tv_day_bottom.setText(substring[1]);// 底部时间
        }
        mContentAdapter.clearAndAddList(itemBean.getGoods());
        mContentAdapter.setOnItemClickListener(new OnItemClickListener<Goods>() {
            @Override
            public void onClick(int position, Goods item) {
                // 跳转到商品详情
                mActivity.startActivity(GoodsDetailActivity.newIntent(mContext, item.getId()));
            }
        });
    }
}
