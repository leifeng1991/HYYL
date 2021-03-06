package com.xxzlkj.huayiyanglao.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.xxzlkj.huayiyanglao.R;
import com.xxzlkj.huayiyanglao.activity.foodorder.TakeOutActivity;
import com.xxzlkj.huayiyanglao.model.FoodsBean;
import com.xxzlkj.huayiyanglao.model.FoodsSaleListBean;
import com.xxzlkj.zhaolinshare.base.base.BaseAdapter;
import com.xxzlkj.zhaolinshare.base.base.BaseViewHolder;

import java.util.List;

/**
 * 描述:外卖右边列表内容适配器
 *
 * @author leifeng
 *         2017/11/21 15:29
 */


public class TakeOutContentItemAdapter extends BaseAdapter<FoodsSaleListBean.DataBean.ListBeanX> {
    private View mShopCartView;
    private TakeOutActivity activity;

    /**
     * @param mShopCartView 加入购物车时目标view
     */
    public TakeOutContentItemAdapter(Context context, TakeOutActivity activity, View mShopCartView, int itemId) {
        super(context, itemId);
        this.mShopCartView = mShopCartView;
        this.activity = activity;
    }

    @Override
    public void convert(BaseViewHolder holder, int position, FoodsSaleListBean.DataBean.ListBeanX itemBean) {
        TextView mTitleTextView = holder.getView(R.id.id_title);// 标题
        RecyclerView mRecyclerView = holder.getView(R.id.id_recycler_view);// 商品列表
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        TakeOutContentItemItemAdapter mTakeOutContentItemAdapter = new
                TakeOutContentItemItemAdapter(mContext, activity, mShopCartView, R.layout.adapter_takeout_right_list_item_item);
        mRecyclerView.setAdapter(mTakeOutContentItemAdapter);
        View mLineView = holder.getView(R.id.id_view_line);// 分割线
        mLineView.setVisibility(position == getItemCount() - 1 ? View.GONE : View.VISIBLE);

        // 设置数据
        mTitleTextView.setText(itemBean.getTitle());
        List<FoodsBean> list = itemBean.getList();
        mTakeOutContentItemAdapter.addList(list);

    }


}
