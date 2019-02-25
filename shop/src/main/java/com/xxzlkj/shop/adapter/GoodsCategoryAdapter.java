package com.xxzlkj.shop.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.xxzlkj.shop.R;
import com.xxzlkj.shop.activity.shop.GoodsDetailActivity;
import com.xxzlkj.shop.config.StringConstants;
import com.xxzlkj.shop.model.Goods;
import com.xxzlkj.shop.model.GoodsCategoryDate;
import com.xxzlkj.zhaolinshare.base.base.BaseAdapter;
import com.xxzlkj.zhaolinshare.base.base.BaseViewHolder;

import java.util.List;


/**
 * 商品分类Adapter
 * Created by Administrator on 2017/3/14.
 */

public class GoodsCategoryAdapter extends BaseAdapter<String> {
    private List<List<Goods>> mLists;
    private GoodsCategoryDate.DataBean dataBean;
    private Activity mActivity;

    public GoodsCategoryAdapter(Context context, Activity mActivity, int itemId,
                                List<List<Goods>> mLists,
                                GoodsCategoryDate.DataBean dataBean) {
        super(context, itemId);
        this.mActivity = mActivity;
        this.mLists = mLists;
        this.dataBean = dataBean;
    }

    @Override
    public void convert(BaseViewHolder holder, int position, final String itemBean) {
        TextView mType = holder.getView(R.id.id_gci_type);
        mType.setText(itemBean);
        RecyclerView mRecyclerView = holder.getView(R.id.id_gci_recyclerview);
        mRecyclerView.setLayoutManager(new GridLayoutManager(mContext,3));

        List<Goods> mGoodsList = mLists.get(position);
        GoodsTypeAdapter mAdapter;
        if ("热卖单品".equals(itemBean)){
            mAdapter = new GoodsTypeAdapter(mContext,R.layout.adapter_goods_type_item_1, StringConstants.GOODS_TYPE_1);
        }else {
            mAdapter = new GoodsTypeAdapter(mContext,R.layout.adapter_goods_type_item_2,StringConstants.GOODS_TYPE_2);
        }
        if (mGoodsList != null && mGoodsList.size() > 0){
            mType.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.VISIBLE);
            mAdapter.addList(mGoodsList);
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.setOnItemClickListener((position1, item) -> {
                if ("热卖单品".equals(itemBean)){// 商品详情
                    mActivity.startActivity(new GoodsDetailActivity().newIntent(mContext,item.getId()));
                }else {// 搜索商品列表
//                    mActivity.startActivity(new SearchGoodsListActivity().newIntent(mContext,dataBean,itemBean,item.getTitle(),item.getId()));
                }
            });
        }else {
            mType.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.GONE);
        }
    }

}
