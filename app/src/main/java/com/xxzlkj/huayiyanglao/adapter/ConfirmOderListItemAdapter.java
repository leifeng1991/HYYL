package com.xxzlkj.huayiyanglao.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.xxzlkj.huayiyanglao.R;
import com.xxzlkj.huayiyanglao.model.FoodsCartListBean;
import com.xxzlkj.zhaolinshare.base.base.BaseAdapter;
import com.xxzlkj.zhaolinshare.base.base.BaseViewHolder;
import com.xxzlkj.zhaolinshare.base.util.PicassoUtils;

/**
 * 描述:确认订单商品二级列表
 *
 * @author leifeng
 *         2017/11/25 14:52
 */


public class ConfirmOderListItemAdapter extends BaseAdapter<FoodsCartListBean.DataBean.ListBean> {

    public ConfirmOderListItemAdapter(Context context, int itemId) {
        super(context, itemId);
    }

    @Override
    public void convert(BaseViewHolder holder, int position, FoodsCartListBean.DataBean.ListBean itemBean) {
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
        mResidueNumberTextView.setText(String.format("剩余%s份", itemBean.getSale_num()));
        mGoodsNumberTextView.setText(String.format("X%s", itemBean.getNum()));
        mPriceTextView.setText("￥" + itemBean.getPrice());

    }


}
