package com.xxzlkj.huayiyanglao.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xxzlkj.huayiyanglao.R;
import com.xxzlkj.huayiyanglao.event.FoodsSaleListEvent;
import com.xxzlkj.huayiyanglao.event.FoodsShopCartEvent;
import com.xxzlkj.huayiyanglao.model.FoodsAddCartBean;
import com.xxzlkj.huayiyanglao.model.FoodsCartListBean;
import com.xxzlkj.huayiyanglao.util.HYNetRequestUtil;
import com.xxzlkj.zhaolinshare.base.base.BaseAdapter;
import com.xxzlkj.zhaolinshare.base.base.BaseViewHolder;
import com.xxzlkj.zhaolinshare.base.util.NumberFormatUtils;

import org.greenrobot.eventbus.EventBus;

/**
 * 描述:购物车商品二级列表
 *
 * @author leifeng
 *         2017/11/25 14:52
 */


public class DialogGoodsListItemAdapter extends BaseAdapter<FoodsCartListBean.DataBean.ListBean> {
    private OnGoodsListChangeListener changeListener;
    private FoodsCartListBean.DataBean parentiItemBean;
    private Activity activity;

    /**
     * @param parentiItemBean 对应该适配器bean
     */
    public DialogGoodsListItemAdapter(Context context, Activity activity, FoodsCartListBean.DataBean parentiItemBean, int itemId) {
        super(context, itemId);
        this.parentiItemBean = parentiItemBean;
        this.activity = activity;
    }

    @Override
    public void convert(BaseViewHolder holder, int position, FoodsCartListBean.DataBean.ListBean itemBean) {
        TextView mTitleTextView = holder.getView(R.id.id_title);// 左边标题
        TextView mPriceTextView = holder.getView(R.id.id_price);// 价格
        ImageView mAddImageView = holder.getView(R.id.id_add);// 加
        ImageView mReduceImageView = holder.getView(R.id.id_reduce);// 减
        TextView mGoodsNumberTextView = holder.getView(R.id.id_goods_number);// 单个商品数量
        // 设置数据
        mTitleTextView.setText(itemBean.getTitle());
        mPriceTextView.setText(String.format("￥%s", itemBean.getPrice()));
        mGoodsNumberTextView.setText(itemBean.getNum());
        // 加一
        mAddImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HYNetRequestUtil.foodsAddCart(activity, itemBean.getPid(), new HYNetRequestUtil.OnRequestSuccessListener() {
                    @Override
                    public void RequestSuccess(FoodsAddCartBean bean) {
                        // 刷新购物车列表
                        EventBus.getDefault().postSticky(new FoodsShopCartEvent());

                        int number = NumberFormatUtils.toInt(itemBean.getNum());
                        number++;
                        itemBean.setNum(number + "");
                        EventBus.getDefault().postSticky(new FoodsSaleListEvent(itemBean.getPid(), itemBean.getNum()));
                        notifyDataSetChanged();
                    }
                });

            }
        });
        // 减一
        mReduceImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int number = NumberFormatUtils.toInt(itemBean.getNum());
                if (number == 1) {
                    // 删除购物车
                    HYNetRequestUtil.foodsDelCart(activity, itemBean.getId(), new HYNetRequestUtil.OnRequestSuccessListener() {
                        @Override
                        public void RequestSuccess(FoodsAddCartBean bean) {
                            // 刷新购物车列表
                            EventBus.getDefault().postSticky(new FoodsShopCartEvent());
                            getList().remove(position);
                            parentiItemBean.setList(getList());
                            EventBus.getDefault().postSticky(new FoodsSaleListEvent(itemBean.getPid(), "0"));
                            notifyDataSetChanged();

                            if (changeListener != null)
                                changeListener.onChange(getItemCount() == 0);
                        }
                    });

                } else {

                    number--;
                    int finalNumber = number;
                    // 编辑购物车
                    HYNetRequestUtil.foodsEditCart(activity, itemBean.getId(), number, new HYNetRequestUtil.OnRequestSuccessListener() {
                        @Override
                        public void RequestSuccess(FoodsAddCartBean bean) {
                            // 刷新购物车列表
                            EventBus.getDefault().postSticky(new FoodsShopCartEvent());
                            itemBean.setNum(finalNumber + "");
                            EventBus.getDefault().postSticky(new FoodsSaleListEvent(itemBean.getPid(), itemBean.getNum()));
                            notifyDataSetChanged();
                        }
                    });

                }


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
