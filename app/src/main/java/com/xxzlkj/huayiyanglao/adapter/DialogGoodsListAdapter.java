package com.xxzlkj.huayiyanglao.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.xxzlkj.huayiyanglao.R;
import com.xxzlkj.huayiyanglao.model.FoodsCartListBean;
import com.xxzlkj.zhaolinshare.base.base.BaseAdapter;
import com.xxzlkj.zhaolinshare.base.base.BaseViewHolder;
import com.xxzlkj.zhaolinshare.base.util.DateUtils;
import com.xxzlkj.zhaolinshare.base.util.NumberFormatUtils;

/**
 * 描述:购物车商品一级列表
 *
 * @author leifeng
 *         2017/11/25 14:52
 */


public class DialogGoodsListAdapter extends BaseAdapter<FoodsCartListBean.DataBean> {
    private OnGoodsListChangeListener changeListener;
    private Activity activity;

    public DialogGoodsListAdapter(Context context, Activity activity, int itemId) {
        super(context, itemId);
        this.activity = activity;
    }

    @Override
    public void convert(BaseViewHolder holder, int position, FoodsCartListBean.DataBean itemBean) {
        TextView mTitleTextView = holder.getView(R.id.id_title);// 左边标题
        TextView mTimeTextView = holder.getView(R.id.id_time);// 右边时间
        RecyclerView mRecyclerView = holder.getView(R.id.id_recycler_view);// 商品列表
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));

        DialogGoodsListItemAdapter mDialogGoodsListItemAdapter = new DialogGoodsListItemAdapter(mContext, activity, itemBean, R.layout.adapter_dialog_list_item_item);
        mRecyclerView.setAdapter(mDialogGoodsListItemAdapter);
        // 设置数据
        mTitleTextView.setText("就餐时间");
        long time = NumberFormatUtils.toLong(itemBean.getStarttime()) * 1000;
        mTimeTextView.setText(DateUtils.getYearMonthDay(time, "MM月dd日 HH:mm"));

        mDialogGoodsListItemAdapter.addList(itemBean.getList());
        mDialogGoodsListItemAdapter.setOnGoodsListChangeListener(new DialogGoodsListItemAdapter.OnGoodsListChangeListener() {
            @Override
            public void onChange(boolean isNotify) {
                // 刷新
                if (isNotify)
                    removeItem(position);

                if (changeListener != null)
                    changeListener.onChange(getItemCount() == 0);

            }
        });

    }


    public void setOnGoodsListChangeListener(OnGoodsListChangeListener changeListener) {
        this.changeListener = changeListener;
    }

    public interface OnGoodsListChangeListener {
        void onChange(boolean isNotify);

    }

}
