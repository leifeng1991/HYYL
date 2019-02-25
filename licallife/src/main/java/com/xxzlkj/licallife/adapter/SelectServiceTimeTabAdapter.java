package com.xxzlkj.licallife.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.xxzlkj.licallife.R;
import com.xxzlkj.licallife.model.ScheduleBean;
import com.xxzlkj.zhaolinshare.base.base.BaseAdapter;
import com.xxzlkj.zhaolinshare.base.base.BaseViewHolder;
import com.xxzlkj.zhaolinshare.base.util.DateUtils;
import com.xxzlkj.zhaolinshare.base.util.NumberFormatUtils;
import com.xxzlkj.zhaolinshare.base.util.PixelUtil;
import com.zrq.spanbuilder.Spans;


/**
 * 描述: 订单adapter
 *
 * @author zhangrq
 *         2016/12/12 11:06
 */

public class SelectServiceTimeTabAdapter extends BaseAdapter<ScheduleBean.TimeBean> {

    private final String serverTimeStyle;
    private ScheduleBean.TimeBean selectBean = new ScheduleBean.TimeBean();
    private final int paddingLeftOrRight;
    private final int serviceSecondsNumber;
    private final int serviceHalfHourNumber;
    private int oldPosition;

    /**
     * @param serverTimeStyle      服务时间样式：1：最近几天的时间点；2：最近几天的时间段；3：最近几个月的时间点
     * @param serviceMinutesNumber 服务分钟数；serverTimeStyle = 2 时必传时间，否则传0
     */
    public SelectServiceTimeTabAdapter(Context context, int itemId, String serverTimeStyle, int serviceMinutesNumber) {
        super(context, itemId);
        this.serverTimeStyle = serverTimeStyle;
        // 以半小时为单位向上取整，获取多少个半小时，不足半小时以半小时为单位
        serviceHalfHourNumber = (int) Math.ceil(serviceMinutesNumber / 30.0);
        // 服务的秒数
        serviceSecondsNumber = serviceHalfHourNumber * 30 * 60;
        paddingLeftOrRight = PixelUtil.dip2px(mContext, "2".equals(serverTimeStyle) ? 2.5f : 10);// 样式2（时间段）的距离为此
    }

    @Override
    public void convert(BaseViewHolder holder, int position, ScheduleBean.TimeBean itemBean) {
        // 归原
        holder.itemView.setVisibility(View.VISIBLE);
        // 找控件
        TextView tv_time = holder.getView(R.id.tv_time);
        // 特殊设置
        // 设置默认布局Padding显示
        holder.itemView.setPadding(paddingLeftOrRight, holder.itemView.getPaddingTop(), paddingLeftOrRight, holder.itemView.getPaddingBottom());
        // 设置背景和颜色
//        String state = itemBean.getState();// 1 未预定 ；2 已预订
        if ("2".equals(serverTimeStyle)) {
            // 时间段
            // 2：最近几天的时间段(11:00-13:00)
            // 判断 （当前点）——(当前点 + 服务时间) 中是否有已预订的，即向后看 serviceHalfHourNumber 个数据，如果向后没有了，则隐藏自己
            int endPosition = position + serviceHalfHourNumber;
            if (endPosition >= getItemCount()) {
                // 向后计算超过了，隐藏自己
                holder.itemView.setVisibility(View.GONE);
            } else {
                // 没有超过，判断此时间范围内是否已有预定
                if (hasSubscribe(position, endPosition)) {
                    // 有预约了
                    // 已预订，白字体，灰背景
                    tv_time.setTextColor(Color.WHITE);
                    tv_time.setBackgroundResource(R.drawable.shape_rectangle_c8c8c8);
                    // 设置时间段状态为2 已预订
                    itemBean.setTimePeriodState("2");
                } else {
                    // 没有预约
                    // 未预定，黑字体，白背景
                    tv_time.setTextColor(Color.BLACK);
                    tv_time.setBackgroundResource(R.drawable.shape_rectangle_stroke_c8c8c8);
                    // 设置时间段状态为1 未预定
                    itemBean.setTimePeriodState("1");
                }
            }
        } else {
            // 时间点
            // 1：最近几天的时间点；
            // 3：最近几个月的时间点
            if ("1".equals(itemBean.getState())) {
                // 未预定，黑字体，白背景
                tv_time.setTextColor(Color.BLACK);
                tv_time.setBackgroundResource(R.drawable.shape_rectangle_stroke_c8c8c8);
            } else {
                // 已预订，白字体，灰背景
                tv_time.setTextColor(Color.WHITE);
                tv_time.setBackgroundResource(R.drawable.shape_rectangle_c8c8c8);
            }
        }

        // 设置字体大小及内容
        String showTime = itemBean.getTime();
        if (showTime == null)
            showTime = "";
        if ("1".equals(serverTimeStyle)) {
            // 1：最近几天的时间点；
            tv_time.setText(Spans.builder().text(showTime).size(14)// 时间点
                    .text("1".equals(itemBean.getState()) ? "" : "\n已预约").size(11).build());// 是否显示已预约
        } else if ("2".equals(serverTimeStyle)) {
            // 2：最近几天的时间段(11:00-13:00)
            String endTime = DateUtils.getYearMonthDay((NumberFormatUtils.toLong(itemBean.getTimestamp()) + serviceSecondsNumber) * 1000, "HH:mm");
            String stateStr;
            if ("2".equals(itemBean.getState()) || "2".equals(itemBean.getTimePeriodState()))
                // 时间点的状态为已预约 或者 时间段的状态为已预约 显示为已预约
                stateStr = "\n已预约";
            else
                stateStr = "";
            tv_time.setText(Spans.builder().text(showTime + "-" + endTime).size(11)// 时间点
                    .text(stateStr).size(11).build());// 是否显示已预约
        } else {
            // 3：最近几个月的时间点
            tv_time.setText(Spans.builder().text(showTime + "号").size(15)// 时间点
                    .text("1".equals(itemBean.getState()) ? "" : "\n已预约").size(11).build());// 是否显示已预约
        }

        // 设置选中
        if (selectBean != null && selectBean.getTimestamp() != null && selectBean.getTimestamp().equals(itemBean.getTimestamp())) {
            // 选中的，强制替换
            oldPosition = position;
            tv_time.setTextColor(Color.WHITE);
            tv_time.setBackgroundResource(R.drawable.shape_rectangle_solid_ff725c);
        }

    }

    /**
     * 此startPosition 到 endPosition 时间内是否有某个时间点预约了
     *
     * @return true 为此时间内有预约
     */
    private boolean hasSubscribe(int startPosition, int endPosition) {
        for (int i = startPosition; i <= endPosition; i++) {
            if (i < getItemCount()) {
                // 小于总的
                ScheduleBean.TimeBean timeBean = getList().get(i);
                String state = timeBean.getState();// 1 未预定 ；2 已预订
                if ("2".equals(state))
                    return true;
            }
        }
        return false;
    }

    public void setSelectBean(ScheduleBean.TimeBean selectBean) {
        this.selectBean = selectBean;
    }

    public void notifySelect(int nowPosition) {
        notifyItemChanged(oldPosition);
        notifyItemChanged(nowPosition);
        oldPosition = nowPosition;
    }
}


