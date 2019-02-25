package com.xxzlkj.shop.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.xxzlkj.shop.R;
import com.xxzlkj.shop.model.GoodsDetail;
import com.xxzlkj.shop.weight.CustomButton;
import com.xxzlkj.zhaolinshare.base.base.BaseAdapter;
import com.xxzlkj.zhaolinshare.base.base.BaseViewHolder;


/**
 * Created by Administrator on 2017/11/14.
 */

public class TuanGouTypeAdapter extends BaseAdapter<GoodsDetail.DataBean.GroupPonsBean> {
    OnClickOpenMassListener listener;

    public TuanGouTypeAdapter(Context context, int itemId) {
        super(context, itemId);
    }

    @Override
    public void convert(BaseViewHolder holder, int position, GoodsDetail.DataBean.GroupPonsBean itemBean) {
        TextView mTuanGouPeopleTextView = holder.getView(R.id.id_tuangou_people);// 共需要参团人数
        TextView mTuanGouPriceTextView = holder.getView(R.id.id_tuangou_price);// 团购价格
        CustomButton mOpenMassButton = holder.getView(R.id.id_open_mass);// 开团

        mTuanGouPeopleTextView.setText(String.format("%s人团", itemBean.getNum()));
        mTuanGouPriceTextView.setText(String.format("￥%s", itemBean.getPrice()));

        mOpenMassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null)
                    listener.clicOpenMass(itemBean);
            }
        });
    }

    public void setClickOpenMassListener(OnClickOpenMassListener listener){
        this.listener = listener;
    }

    public interface OnClickOpenMassListener {
        void clicOpenMass(GoodsDetail.DataBean.GroupPonsBean itemBean);
    }
}
