package com.xxzlkj.shop.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xxzlkj.shop.R;
import com.xxzlkj.shop.model.SelectCouponsBean;
import com.xxzlkj.zhaolinshare.base.base.BaseAdapter;
import com.xxzlkj.zhaolinshare.base.base.BaseViewHolder;
import com.xxzlkj.zhaolinshare.base.util.DateUtils;
import com.xxzlkj.zhaolinshare.base.util.NumberFormatUtils;
import com.xxzlkj.zhaolinshare.base.util.PixelUtil;
import com.xxzlkj.zhaolinshare.base.util.StringUtil;
import com.zrq.spanbuilder.Spans;


/**
 * 描述: 选择优惠券adapter
 *
 * @author zhangrq
 *         2016/12/12 11:06
 */

public class SelectCouponsListAdapter extends BaseAdapter<SelectCouponsBean.DataBean.ItemBean> {
    public static final int ONE_DAY_SECONDS = 24 * 60 * 60;// 一天的秒数

    private final String state;

    public SelectCouponsListAdapter(Context context, int itemId, String state) {
        super(context, itemId);
        this.state = state;
    }

    @Override
    public void convert(BaseViewHolder holder, int position, final SelectCouponsBean.DataBean.ItemBean itemBean) {
        LinearLayout ll_item_coupons = holder.getView(R.id.ll_item_coupons);//适配
        TextView tv_money = holder.getView(R.id.tv_money);//优惠金额
        TextView tv_title = holder.getView(R.id.tv_title);//名称
        TextView tv_des = holder.getView(R.id.tv_des);//描述
        TextView tv_available = holder.getView(R.id.tv_available);//可用条件
        CheckBox cb_is_checked = holder.getView(R.id.cb_is_checked);//是否是选中的
        // 适配
        ViewGroup.LayoutParams layoutParams = ll_item_coupons.getLayoutParams();
        int width = PixelUtil.getScreenWidth(mContext) - mContext.getResources().getDimensionPixelSize(R.dimen.item_coupons_margin) * 2;
        layoutParams.height = width * 238 / 712;
        ll_item_coupons.setLayoutParams(layoutParams);
        // 控制布局显示
        // state	1可使用 2不可用
        long startTime = NumberFormatUtils.toLong(itemBean.getStarttime());
        long endTime = NumberFormatUtils.toLong(itemBean.getEndtime());
//        starttime	优惠券开始时间
//        endtime	优惠券结束时间
        if ("1".equals(state)) {
            // 1可使用
            // 金额背景图片设置
            if (endTime - startTime < ONE_DAY_SECONDS) {
                // 小于一天
                tv_money.setBackgroundResource(R.mipmap.ic_coupons_item_blue);
            } else if (endTime - startTime == ONE_DAY_SECONDS) {
                // 等于一天
                tv_money.setBackgroundResource(R.mipmap.ic_coupons_item_pink);
            } else if (endTime - startTime > ONE_DAY_SECONDS) {
                // 大于一天
                tv_money.setBackgroundResource(R.mipmap.ic_coupons_item_orange);
            }
            // 布局设置
            tv_title.setTextColor(ContextCompat.getColor(mContext, R.color.black_444444));
            tv_available.setTextColor(ContextCompat.getColor(mContext, R.color.orange_ff725c));
            cb_is_checked.setVisibility(View.VISIBLE);
            // 设置是否选中
            cb_is_checked.setChecked(itemBean.isChecked());
            cb_is_checked.setEnabled(false);// 防止它获取焦点
        } else {
            // 2不可用
            // 金额背景图片设置
            tv_money.setBackgroundResource(R.mipmap.ic_coupons_item_gray);
            // 布局设置
            tv_title.setTextColor(ContextCompat.getColor(mContext, R.color.black_999999));
            tv_available.setTextColor(ContextCompat.getColor(mContext, R.color.black_999999));
            cb_is_checked.setVisibility(View.GONE);
        }
        // 通用设置
        // 优惠金额或折扣
        String type = itemBean.getType();// type	1 折扣 2满减
        String discount = itemBean.getDiscount();// discount	type 为1 折扣数 type 为2  满减中 减多少钱
        if ("1".equals(type)) {
            // 1折扣
            tv_money.setText(StringUtil.discountNumber2ChineseText(discount) + "折");
        } else if ("2".equals(type)) {
            // 2满减
            tv_money.setText(Spans.builder().text("￥").size(14).text(discount).build());
        } else {
            tv_money.setText("- -");
        }
        // 标题
        tv_title.setText(itemBean.getTitle());
        // 使用时间
        if (endTime - startTime < ONE_DAY_SECONDS) {
            // 小于一天
            // 仅限2017.04.01  10:00-12:00使用
            tv_des.setText("仅限" + DateUtils.getYearMonthDayHourMinute2(startTime * 1000) + "-" + DateUtils.getYearMonthDay(endTime * 1000, "HH:mm") + "使用");
        } else if (endTime - startTime == ONE_DAY_SECONDS) {
            // 等于一天
            // 仅限2017.04.01当日使用
            tv_des.setText("仅限" + DateUtils.getYearMonthDayHourMinute2(startTime * 1000) + "当日使用");
        } else if (endTime - startTime > ONE_DAY_SECONDS) {
            // 大于一天
            // 仅限2017.04.01-2017.04.03使用
            tv_des.setText("仅限" + DateUtils.getYearMonthDay2(startTime * 1000) + "-" + DateUtils.getYearMonthDay2(endTime * 1000) + "使用");
        } else {
            // 服务器返回数据错误
            tv_des.setText("- -");
        }
        // 使用条件
        String filled = itemBean.getFilled();
        if ("0".equals(filled)) {
            tv_available.setText("无消费金额限制");
        } else {
            tv_available.setText("满" + filled + "元可用");
        }

    }

}


