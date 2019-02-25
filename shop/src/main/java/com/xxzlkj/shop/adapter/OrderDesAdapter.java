package com.xxzlkj.shop.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xxzlkj.shop.R;
import com.xxzlkj.shop.model.OrderDesBean;
import com.xxzlkj.shop.utils.TextPriceUtil;
import com.xxzlkj.shop.weight.CustomButton;
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

public class OrderDesAdapter extends BaseAdapter<OrderDesBean.DataBean.GoodsBean> {

    public OrderDesAdapter(Context context, int itemId) {
        super(context, itemId);
    }

    @Override
    public void convert(BaseViewHolder holder, int position, OrderDesBean.DataBean.GoodsBean itemBean) {
        ImageView iv_order_des_icon = holder.getView(R.id.iv_order_des_icon);
        TextView tv_order_des_name = holder.getView(R.id.tv_order_des_name);
        TextView tv_order_des_specification = holder.getView(R.id.tv_order_des_specification);//规格
        ViewGroup vg_pre_sale_layout = holder.getView(R.id.vg_pre_sale_layout);// 预售/团购布局
        CustomButton id_pre_sale = holder.getView(R.id.id_pre_sale);// 预售/团购布局
        TextView tv_pre_sale_hint = holder.getView(R.id.tv_pre_sale_hint);// 预售/团购提示
        TextView tv_order_des_money = holder.getView(R.id.tv_order_des_money);
        TextView tv_order_des_money_old = holder.getView(R.id.tv_order_des_money_old);
        TextView tv_order_des_number = holder.getView(R.id.tv_order_des_number);
        View view_line = holder.getView(R.id.view_line);
        // 特殊的设置
        view_line.setVisibility(position == getItemCount() - 1 ? View.GONE : View.VISIBLE);
        // 设置值
        PicassoUtils.setImageSmall(mContext, itemBean.getSimg(), iv_order_des_icon);
        TextViewUtils.setText(tv_order_des_name, itemBean.getTitle());
        TextViewUtils.setText(tv_order_des_specification, itemBean.getAds());
        // 设置预售
        String activity_type = itemBean.getActivity_type();// 0普通,1预售，2团购
        String activity_desc = itemBean.getActivity_desc();// 活动描述，只有activity_type不等于0时显示
        if ("1".equals(activity_type)) {
            // 预售
            vg_pre_sale_layout.setVisibility(View.VISIBLE);
            id_pre_sale.setText("预售");
            tv_pre_sale_hint.setText(activity_desc);
        } else if ("2".equals(activity_type)) {
            // 团购
            vg_pre_sale_layout.setVisibility(View.VISIBLE);
            id_pre_sale.setText("团购");
            tv_pre_sale_hint.setText(activity_desc);
        } else {
            // 其它（0普通,2团购）
            vg_pre_sale_layout.setVisibility(View.GONE);
        }
        TextViewUtils.setText(tv_order_des_money, "¥ ", itemBean.getPrice());// 现价
        TextPriceUtil.setTextPrices(itemBean.getPrice(), itemBean.getPrices(), tv_order_des_money_old);// 设置原价

        TextViewUtils.setText(tv_order_des_number, "x ", itemBean.getNum());
    }

}
