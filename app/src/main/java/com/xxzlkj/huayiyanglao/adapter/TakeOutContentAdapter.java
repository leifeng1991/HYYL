package com.xxzlkj.huayiyanglao.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.xxzlkj.huayiyanglao.R;
import com.xxzlkj.huayiyanglao.activity.foodorder.TakeOutActivity;
import com.xxzlkj.huayiyanglao.model.FoodsSaleListBean;
import com.xxzlkj.zhaolinshare.base.base.BaseAdapter;
import com.xxzlkj.zhaolinshare.base.base.BaseViewHolder;

import java.util.List;

/**
 * 描述:外卖右边列表标题适配器
 *
 * @author leifeng
 *         2017/11/21 15:29
 */


public class TakeOutContentAdapter extends BaseAdapter<FoodsSaleListBean.DataBean> {
    private TakeOutActivity activity;
    private View mShopCartView;

    /**
     * @param mShopCartView 加入购物车时目标view(动画的终点view)
     */
    public TakeOutContentAdapter(Context context, TakeOutActivity activity, View mShopCartView, int itemId) {
        super(context, itemId);
        this.activity = activity;
        this.mShopCartView = mShopCartView;
    }


    @Override
    public void convert(BaseViewHolder holder, int position, FoodsSaleListBean.DataBean itemBean) {
        RecyclerView mRecyclerView = holder.getView(R.id.id_recycler_view);// 商品列表
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        TakeOutContentItemAdapter mTakeOutContentItemAdapter = new
                TakeOutContentItemAdapter(mContext, activity, mShopCartView, R.layout.adapter_takeout_right_list_item);
        mRecyclerView.setAdapter(mTakeOutContentItemAdapter);
        View mLineView = holder.getView(R.id.id_view_line);// 分割线
        // 设置最后一个分割线隐藏
        mLineView.setVisibility(position == getItemCount() - 1 ? View.GONE : View.VISIBLE);
        // 设置数据
        List<FoodsSaleListBean.DataBean.ListBeanX> list1 = itemBean.getList();

        mTakeOutContentItemAdapter.addList(list1);
    }

}
