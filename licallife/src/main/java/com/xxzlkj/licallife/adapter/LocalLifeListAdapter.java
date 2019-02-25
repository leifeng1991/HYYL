package com.xxzlkj.licallife.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xxzlkj.licallife.R;
import com.xxzlkj.licallife.model.LocalLifeListBean;
import com.xxzlkj.licallife.utils.LocalLifeUnitUtils;
import com.xxzlkj.zhaolinshare.base.base.BaseAdapter;
import com.xxzlkj.zhaolinshare.base.base.BaseViewHolder;
import com.xxzlkj.zhaolinshare.base.util.DateUtils;
import com.xxzlkj.zhaolinshare.base.util.NumberFormatUtils;
import com.xxzlkj.zhaolinshare.base.util.PicassoUtils;
import com.zrq.spanbuilder.Spans;

import java.util.List;


/**
 * 描述: 订单adapter
 *
 * @author zhangrq
 *         2016/12/12 11:06
 */

public class LocalLifeListAdapter extends BaseAdapter<LocalLifeListBean.DataBean> {

    public LocalLifeListAdapter(Context context, int itemId) {
        super(context, itemId);
    }

    @Override
    public void convert(BaseViewHolder holder, int position, final LocalLifeListBean.DataBean itemBean) {
        TextView tv_state = holder.getView(R.id.tv_state);
        ImageView iv_headImg = holder.getView(R.id.iv_headImg);
        TextView tv_title = holder.getView(R.id.tv_title);
        TextView tv_name = holder.getView(R.id.tv_name);
        TextView tv_time = holder.getView(R.id.tv_time);
        TextView tv_money = holder.getView(R.id.tv_money);
        View view_line = holder.getView(R.id.view_line);

//        状态 1待付款 2待服务 3服务中 4已完成 5已取消 6已退款
//        is_tui	1。3不显示 2申请退款中（替换掉state状态）
//        pid	（保姆，月嫂，type 3、4类型） 判断是否是预付订单 0预付订单 非0余额订单
//        shoptime	（保姆，月嫂，type3、4类型）shoptime等于0为预约中 有值则为面试中
//        type	订单类型 1保洁产品 2 预约保洁师 3月嫂 4保姆 5小时工'

        String state = itemBean.getState();
        String type = itemBean.getType();
        String pid = itemBean.getPid();
        String shoptime = itemBean.getShoptime();
        tv_state.setTextColor(ContextCompat.getColor(mContext, R.color.orange_ff725c));
        if ("2".equals(itemBean.getIs_tui())) {
            // 退款中
            tv_state.setText("退款中");
        } else if ("1".equals(state)) {
            // 1待付款
            tv_state.setText("待付款");
        } else if ("2".equals(state)) {
            // 2待服务
            if ("3".equals(type) || "4".equals(type)) {
                // 3月嫂 4保姆
                if ("0".equals(pid)) {
                    // 0预付订单
                    if (NumberFormatUtils.toDouble(shoptime) == 0) {
                        // 0为预约中
                        tv_state.setText("预约中");
                    } else {
                        //  有值则为面试中
                        tv_state.setText("面试中");
                    }
                } else {
                    // 非0余额订单
                    tv_state.setText("待服务");
                }

            } else {
                // 1保洁产品 2 预约保洁师 5小时工'
                tv_state.setText("待服务");
            }
        } else if ("3".equals(state)) {
            // 3服务中
            tv_state.setText("服务中");
        } else if ("4".equals(state)) {
            // 4已完成(判断)
//            state等于4时判断此值 等于0时为待收货 不为0时为已完成
            if ("0".equals(itemBean.getUidtime())) {
                tv_state.setText("待确认");
            } else {
                tv_state.setText("已完成");
            }
        } else if ("5".equals(state)) {
            // 5已取消
            tv_state.setText("已取消");
            tv_state.setTextColor(ContextCompat.getColor(mContext, R.color.gray_7d7d7d));
        } else if ("6".equals(state)) {
            // 6已退款
            tv_state.setText("已退款");
        }
        // 设置金额
        String moneyTitle;
        if ("3".equals(type) || "4".equals(type)) {
            // 3月嫂 4保姆
            if ("0".equals(pid)) {
                // 0预付订单
                moneyTitle = "定金：";
            } else {
                // 非0余额订单
                moneyTitle = "尾款：";
            }
        } else {
            // 1保洁产品 2 预约保洁师 5小时工'
            moneyTitle = "金额：";
        }
        String price = itemBean.getPrice();
        if (price == null)
            price = "- -";
        tv_money.setText(Spans.builder()
                .text(moneyTitle).color(ContextCompat.getColor(mContext, R.color.black_444444)).size(14)
                .text("￥" + price).color(ContextCompat.getColor(mContext, R.color.orange_ff725c)).size(17)
                .build());// 总价


        // 通用的设置
        List<LocalLifeListBean.DataBean.GoodsBean> goods = itemBean.getGoods();
        if (goods.size() > 0) {
            LocalLifeListBean.DataBean.GoodsBean goodsBean = goods.get(0);
            PicassoUtils.setImageSmall(mContext, goodsBean.getSimg(), iv_headImg);
            tv_title.setText(goodsBean.getTitle());
            tv_time.setText(LocalLifeUnitUtils.getUnitContent(goodsBean.getNum(), goodsBean.getUnit(), goodsBean.getUnit_desc()));//服务时长：2小时
        }
        tv_name.setText(String.valueOf("服务时间：" + DateUtils.getYearMonthDayHourMinute(NumberFormatUtils.toLong(itemBean.getEndtime()) * 1000)));//服务时间：XXXXXXXXXXXXXXXXXX
//        view_line.setVisibility(position == getItemCount() - 1 ? View.GONE : View.VISIBLE);
    }

}


