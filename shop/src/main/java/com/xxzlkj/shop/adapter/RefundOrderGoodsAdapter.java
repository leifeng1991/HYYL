package com.xxzlkj.shop.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.xxzlkj.shop.R;
import com.xxzlkj.shop.model.RefundOrderGoods;
import com.xxzlkj.zhaolinshare.base.base.BaseAdapter;
import com.xxzlkj.zhaolinshare.base.base.BaseViewHolder;
import com.zrq.spanbuilder.Spans;

/**
 * 可申请售后商品订单列表适配器
 * Created by Administrator on 2017/4/7.
 */

public class RefundOrderGoodsAdapter extends BaseAdapter<RefundOrderGoods.DataBean> {
    private Activity mActivity;

    public RefundOrderGoodsAdapter(Context context, Activity mActivity,int itemId) {
        super(context, itemId);
        this.mActivity = mActivity;
    }

    @Override
    public void convert(BaseViewHolder holder, int position, RefundOrderGoods.DataBean itemBean) {
        TextView mThemeTextView = holder.getView(R.id.id_ali_theme);
        TextView mStateTextView = holder.getView(R.id.id_ali_state);
        TextView mDetailTextView = holder.getView(R.id.id_ali_detail);
        View mLineView = holder.getView(R.id.id_ali_line);
        RecyclerView mRecyclerView = holder.getView(R.id.id_ali_recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        RefundOrderGoodsItemAdapter mRefundOrderGoodsItemAdapter =
                new RefundOrderGoodsItemAdapter(mContext,mActivity,R.layout.adapter_ali_list_item,itemBean.getId(),itemBean.getDiscount());
        mRefundOrderGoodsItemAdapter.addList(itemBean.getGoods());
        mRecyclerView.setAdapter(mRefundOrderGoodsItemAdapter);

        if (position == getItemCount() - 1){
            mLineView.setVisibility(View.GONE);
        }else {
            mLineView.setVisibility(View.VISIBLE);
        }

        mDetailTextView.setText(Spans.builder()
                .text("共计")
                .text(itemBean.getCount(),14,0xff000000)
                .text("件商品        ")
                .text("合计：")
                .text("￥" + itemBean.getPrice(),14,0xff000000)
                .text("(含运费￥0.00)").build());
    }
}
