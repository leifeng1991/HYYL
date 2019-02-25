package com.xxzlkj.licallife.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xxzlkj.licallife.R;
import com.xxzlkj.licallife.model.LocalLifeDesBean;
import com.xxzlkj.licallife.utils.LocalLifeUnitUtils;
import com.xxzlkj.zhaolinshare.base.base.BaseAdapter;
import com.xxzlkj.zhaolinshare.base.base.BaseViewHolder;
import com.xxzlkj.zhaolinshare.base.util.DateUtils;
import com.xxzlkj.zhaolinshare.base.util.NumberFormatUtils;
import com.xxzlkj.zhaolinshare.base.util.PicassoUtils;
import com.xxzlkj.zhaolinshare.base.util.StringUtil;
import com.zrq.spanbuilder.Spans;


/**
 * 描述: 本地生活详情adapter
 *
 * @author zhangrq
 *         2016/12/12 11:06
 */

public class LocalLifeDesAdapter extends BaseAdapter<LocalLifeDesBean.DataBean.GoodsBean> {
    private final boolean isHalfMoney;

    /**
     * @param isHalfMoney 是否是半个月的钱
     */
    public LocalLifeDesAdapter(Context context, int itemId, boolean isHalfMoney) {
        super(context, itemId);
        this.isHalfMoney = isHalfMoney;
    }

    @Override
    public void convert(BaseViewHolder holder, int position, LocalLifeDesBean.DataBean.GoodsBean itemBean) {
        ImageView iv_order_des_icon = holder.getView(R.id.iv_order_des_icon);
        TextView tv_order_des_date = holder.getView(R.id.tv_order_des_date);//服务时间：2017-02-22  15:30
        TextView tv_order_des_time = holder.getView(R.id.tv_order_des_time);//服务时长：2小时
        TextView tv_order_des_shop = holder.getView(R.id.tv_order_des_shop);//所属商家：58同城
        TextView tv_order_des_project = holder.getView(R.id.tv_order_des_project);//服务项目：58同城
        TextView tv_order_des_money = holder.getView(R.id.tv_order_des_money);//¥ 52
        View view_line = holder.getView(R.id.view_line);
        // 特殊的设置
        view_line.setVisibility(position == getItemCount() - 1 ? View.GONE : View.VISIBLE);
        // 设置值
        PicassoUtils.setImageSmall(mContext, itemBean.getSimg(), iv_order_des_icon);

        setItemValue(tv_order_des_date, "服务时间：", DateUtils.getYearMonthDayHourMinute(NumberFormatUtils.toLong(itemBean.getEndtime()) * 1000));
        setItemValue(tv_order_des_time,
                LocalLifeUnitUtils.getUnitLeft(itemBean.getUnit()),
                LocalLifeUnitUtils.getUnitRight(itemBean.getNum(), itemBean.getUnit(), itemBean.getUnit_desc()));// 服务时长 或 数量
        setItemValue(tv_order_des_shop, "所属商家：", itemBean.getShop_title());
        setItemValue(tv_order_des_project, "服务项目：", itemBean.getTitle());

        double price = NumberFormatUtils.toDouble(itemBean.getPrice());
        String priceStr = StringUtil.saveTwoDecimal(isHalfMoney ? price * 2 : price);
        tv_order_des_money.setText(String.valueOf("¥ " + priceStr));
    }

    private void setItemValue(TextView textView, String title, String value) {
        if (TextUtils.isEmpty(value))
            value = "——";
        textView.setText(Spans.builder().text(title).color(ContextCompat.getColor(mContext, R.color.black_746f6e))
                .text(value + "").color(ContextCompat.getColor(mContext, R.color.text_3a)).build());
    }

}
