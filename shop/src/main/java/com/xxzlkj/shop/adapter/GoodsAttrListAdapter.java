package com.xxzlkj.shop.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.xxzlkj.shop.R;
import com.xxzlkj.shop.event.GoodsDetailEvent;
import com.xxzlkj.shop.event.ShopCartEvent;
import com.xxzlkj.shop.model.GoodsDetail;
import com.xxzlkj.zhaolinshare.base.base.BaseAdapter;
import com.xxzlkj.zhaolinshare.base.base.BaseViewHolder;
import com.xxzlkj.zhaolinshare.base.util.TextViewUtils;

import org.greenrobot.eventbus.EventBus;

/**
 * 商品属性列表适配器
 * Created by Administrator on 2017/3/22.
 */

public class GoodsAttrListAdapter extends BaseAdapter<GoodsDetail.DataBean.AttrListBean> {
    //判断是详情页（true）/购物车(false)
    private boolean flag;

    public GoodsAttrListAdapter(Context context, int itemId, boolean flag) {
        super(context, itemId);
        this.flag = flag;
    }

    @Override
    public void convert(BaseViewHolder holder, int position, final GoodsDetail.DataBean.AttrListBean itemBean) {
        TextView mTitle = holder.getView(R.id.id_al_attr_title);
        TextViewUtils.setText(mTitle,itemBean.getTitle());
        RecyclerView mRecyclerView = holder.getView(R.id.id_al_attr_list);
        mRecyclerView.setLayoutManager(new GridLayoutManager(mContext,3));
        GoodsCatidBeanAdapter mAdapter = new GoodsCatidBeanAdapter(mContext,R.layout.adapter_attr_list_item);
        mAdapter.addList(itemBean.getCatid());
        mAdapter.setOnItemClickListener(new OnItemClickListener<GoodsDetail.DataBean.AttrListBean.CatidBean>() {
            @Override
            public void onClick(int position, GoodsDetail.DataBean.AttrListBean.CatidBean item) {
                if (flag){// 商品详情页面
                    EventBus.getDefault().postSticky(new GoodsDetailEvent(item.getGoods_id()));
                }else {// 购物车页面
                    EventBus.getDefault().postSticky(new ShopCartEvent(item.getGoods_id(),"",""));
                }

            }
        });
        mRecyclerView.setAdapter(mAdapter);
    }
}
