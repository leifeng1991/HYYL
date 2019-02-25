package com.xxzlkj.huayiyanglao.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.xxzlkj.huayiyanglao.R;
import com.xxzlkj.huayiyanglao.model.FoodsCartListBean;
import com.xxzlkj.zhaolinshare.base.base.BaseAdapter;
import com.xxzlkj.zhaolinshare.base.base.BaseViewHolder;
import com.xxzlkj.zhaolinshare.base.util.DateUtils;
import com.xxzlkj.zhaolinshare.base.util.NumberFormatUtils;

import java.util.List;

/**
 * 描述:确认订单商品一级列表
 *
 * @author leifeng
 *         2017/11/25 14:52
 */


public class ConfirmOderListAdapter extends BaseAdapter<FoodsCartListBean.DataBean> {

    public ConfirmOderListAdapter(Context context, int itemId) {
        super(context, itemId);
    }

    @Override
    public void convert(BaseViewHolder holder, int position, FoodsCartListBean.DataBean itemBean) {
        TextView mTitleTextView = holder.getView(R.id.id_title);// 左边标题
        TextView mTimeTextView = holder.getView(R.id.id_time);// 右边时间
        mTimeTextView.setTextColor(ContextCompat.getColor(mContext, R.color.text_4));
        RecyclerView mRecyclerView = holder.getView(R.id.id_recycler_view);// 商品列表
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        // 设置数据
        mTitleTextView.setText("配送时间");
        long startTime = NumberFormatUtils.toLong(itemBean.getStarttime()) * 1000;
        long endTime = NumberFormatUtils.toLong(itemBean.getStoptime()) * 1000;
        mTimeTextView.setText(String.format("%s-%s", DateUtils.getYearMonthDay(startTime, "MM月dd日 HH:mm"), DateUtils.getYearMonthDay(endTime, "HH:mm")));
        ConfirmOderListItemAdapter mDialogGoodsListItemAdapter = new ConfirmOderListItemAdapter(mContext, R.layout.adapter_confirm_order_item_item);
        mRecyclerView.setAdapter(mDialogGoodsListItemAdapter);
        mDialogGoodsListItemAdapter.addList(itemBean.getList());


    }


}
