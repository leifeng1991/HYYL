package com.xxzlkj.shop.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.xxzlkj.shop.R;
import com.xxzlkj.shop.activity.shop.SearchGoodsListActivity;
import com.xxzlkj.shop.config.StringConstants;
import com.xxzlkj.shop.model.Goods;
import com.xxzlkj.shop.model.GoodsGroupBean;
import com.xxzlkj.shop.model.SpeedGoodsCategoryBean;
import com.xxzlkj.zhaolinshare.base.base.BaseAdapter;
import com.xxzlkj.zhaolinshare.base.base.BaseViewHolder;


/**
 * 时速达商品分类Adapter
 * Created by Administrator on 2017/3/14.
 */

public class SpeedGoodsCategoryAdapter extends BaseAdapter<GoodsGroupBean> {
    private SpeedGoodsCategoryBean.DataBean dataBean;
    private Activity mActivity;
    private String mStoreId;
    private String mTitleId;

    /**
     *
     * @param dataBean 二级类bean
     * @param mTitleId 左侧标题id
     * @param mStoreId 门店id
     */
    public SpeedGoodsCategoryAdapter(Context context, Activity mActivity, SpeedGoodsCategoryBean.DataBean dataBean, String mTitleId, String mStoreId, int itemId) {
        super(context, itemId);
        this.mActivity = mActivity;
        this.dataBean = dataBean;
        this.mTitleId = mTitleId;
        this.mStoreId = mStoreId;
    }

    @Override
    public void convert(BaseViewHolder holder, int position, final GoodsGroupBean itemBean) {
        TextView mType = holder.getView(R.id.id_gci_type);
        // 设置分类标题
        mType.setText(itemBean.getTitle());
        RecyclerView mRecyclerView = holder.getView(R.id.id_gci_recyclerview);
        mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
        GoodsTypeAdapter mAdapter = new GoodsTypeAdapter(mContext, R.layout.adapter_goods_type_item_2, StringConstants.GOODS_TYPE_2);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.addList(itemBean.getChild());
        // 跳转到商品列表
        mAdapter.setOnItemClickListener(new OnItemClickListener<Goods>() {
            @Override
            public void onClick(int position, Goods item) {
                // 跳转到商品列表
                if ("-1".equals(mTitleId)) {
                    // 预售
                    mActivity.startActivity(SearchGoodsListActivity.newIntent(mContext, dataBean, 5, itemBean.getTitle(), item.getTitle(), item.getId(), mStoreId));
                } else if ("-2".equals(mTitleId)) {
                    // 团购
                    mActivity.startActivity(SearchGoodsListActivity.newIntent(mContext, dataBean, 6, itemBean.getTitle(), item.getTitle(), item.getId(), mStoreId));
                } else {
                    // 其他
                    mActivity.startActivity(SearchGoodsListActivity.newIntent(mContext, dataBean, 1, itemBean.getTitle(), item.getTitle(), item.getId(), mStoreId));
                }

            }
        });
    }

}
