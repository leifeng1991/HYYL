package com.xxzlkj.huayiyanglao.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.xxzlkj.huayiyanglao.R;
import com.xxzlkj.huayiyanglao.model.OrderListBean;
import com.xxzlkj.zhaolinshare.base.base.BaseAdapter;
import com.xxzlkj.zhaolinshare.base.base.BaseViewHolder;
import com.xxzlkj.zhaolinshare.base.util.PicassoUtils;
import com.xxzlkj.zhaolinshare.base.util.TextViewUtils;


/**
 * 描述: 订单详情adapter
 *
 * @author zhangrq
 *         2016/12/12 11:06
 */

public class FoodsOrderListAdapterAdapter extends BaseAdapter<OrderListBean.DataBean.GoodsBean> {

    public FoodsOrderListAdapterAdapter(Context context, int itemId) {
        super(context, itemId);
    }

    @Override
    public void convert(BaseViewHolder holder, int position, OrderListBean.DataBean.GoodsBean itemBean) {
        ImageView mImageView = holder.getView(R.id.id_image);// 商品图片
        TextView mTitleTextView = holder.getView(R.id.id_title);// 标题
        TextView mContentTextView = holder.getView(R.id.id_content);// 内容
        TextView mResidueNumberTextView = holder.getView(R.id.id_residue_number);// 剩余数量
        TextView mGoodsNumberTextView = holder.getView(R.id.id_goods_number);// 购买商品数量
        TextView mPriceTextView = holder.getView(R.id.id_price);// 购买商品数量
        // 设置数据
        PicassoUtils.setImageBig(mContext, itemBean.getSimg(), mImageView);
        mTitleTextView.setText(itemBean.getTitle());
        mContentTextView.setText(itemBean.getAds());
//        mResidueNumberTextView.setText(String.format("剩余%s份", itemBean.getSale_num()));
        mGoodsNumberTextView.setText(String.format("X%s", itemBean.getNum()));
        mPriceTextView.setText("￥" + itemBean.getPrice());
    }

}
