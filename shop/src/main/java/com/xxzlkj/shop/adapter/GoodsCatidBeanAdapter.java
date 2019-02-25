package com.xxzlkj.shop.adapter;

import android.content.Context;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xxzlkj.shop.R;
import com.xxzlkj.shop.model.GoodsDetail;
import com.xxzlkj.zhaolinshare.base.base.BaseAdapter;
import com.xxzlkj.zhaolinshare.base.base.BaseViewHolder;
import com.xxzlkj.zhaolinshare.base.util.NumberFormatUtils;


public class GoodsCatidBeanAdapter extends BaseAdapter<GoodsDetail.DataBean.AttrListBean.CatidBean> {

    public GoodsCatidBeanAdapter(Context context, int itemId) {
        super(context, itemId);
    }

    @Override
    public void convert(BaseViewHolder holder, int position, final GoodsDetail.DataBean.AttrListBean.CatidBean itemBean) {
        TextView mTitle = holder.getView(R.id.id_ali_attr_title);
        RelativeLayout mLayout = holder.getView(R.id.id_ali_attr_layout);

        if (NumberFormatUtils.toInt(itemBean.getIs_state()) == 2){// 2上架状态
            mLayout.setClickable(true);
        }else {// 非2为不可选状态
            mLayout.setClickable(false);
        }

        if (itemBean.getIs_select() == 1){// 1未选中
            if (NumberFormatUtils.toInt(itemBean.getIs_state()) == 2){// 2上架状态
                mLayout.setBackgroundResource(R.drawable.shape_gray_stroke_5);
                mTitle.setTextColor(0xff555555);
            }else {// 非2为不可选状态
                mLayout.setBackgroundResource(R.drawable.shape_gray_stroke_b4);
                mTitle.setTextColor(0xffb4b4b4);
            }

        }else {// 2为选中
            mLayout.setBackgroundResource(R.drawable.shape_orange_stroke_30);
            mTitle.setTextColor(0xffff725c);
        }

        mTitle.setText(itemBean.getTitle());
    }

}
