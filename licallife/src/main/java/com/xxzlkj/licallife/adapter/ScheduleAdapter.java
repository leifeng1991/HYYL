package com.xxzlkj.licallife.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xxzlkj.licallife.R;
import com.xxzlkj.licallife.model.ScheduleBean;
import com.xxzlkj.zhaolinshare.base.base.BaseAdapter;
import com.xxzlkj.zhaolinshare.base.base.BaseViewHolder;
import com.xxzlkj.zhaolinshare.base.util.NumberFormatUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 可约时间
 * Created by Administrator on 2017/4/19.
 */

public class ScheduleAdapter extends BaseAdapter<ScheduleBean> {
    // 半格大小（第一个和第二个保持一致）
    private int semiLattice;
    // 总数(平均分成多少份)
    private int totalNumber;
    private int type;

    /**
     * @param type 1：每天 2：每月
     */
    public ScheduleAdapter(Context context, int itemId, int type) {
        super(context, itemId);
        this.type = type;
        switch (type) {
            case 1:// 每天
                semiLattice = 2;
                totalNumber = 36;
                break;
            case 2:// 每月
                semiLattice = 1;
                totalNumber = 32;
                break;
        }
    }

    @Override
    public void convert(final BaseViewHolder holder, final int position, final ScheduleBean itemBean) {
        TextView mDayTextView = holder.getView(R.id.id_day);
        LinearLayout mRecyclerView = holder.getView(R.id.id_recyclerview);

        List<ScheduleBean.TimeBean> timeBeanList = new ArrayList<>();


        switch (type) {
            case 1:// 每天
                // 添加空白
                for (int i = 0; i < semiLattice; i++) {
                    ScheduleBean.TimeBean timeBean = new ScheduleBean.TimeBean();
                    timeBeanList.add(timeBean);
                }

                List<ScheduleBean.TimeBean> beanList = itemBean.getTime();
                if (beanList != null && beanList.size() > 0) {
                    // 获取第一个时间
                    String time = itemBean.getTime().get(0).getTime();
                    int hours = NumberFormatUtils.toInt(time.split(":")[0]);
                    int number;
                    if (hours >= 8) {
                        // 大于8点 计算从第几个开始
                        number = (hours - 8) * 2;
                        if (NumberFormatUtils.toInt(time.split(":")[0]) == 0) {
                            // 不加
                        } else {
                            // 加一
                            number += 1;
                        }

                        // 添加灰色（不可选状态）
                        for (int i = timeBeanList.size(); i < number; i++) {
                            ScheduleBean.TimeBean timeBean = new ScheduleBean.TimeBean();
                            // 设置灰色状态值
                            timeBean.setState("2");
                            timeBeanList.add(timeBean);
                        }
                        // 添加数据
                        timeBeanList.addAll(itemBean.getTime());
                    } else {
                        // 添加数据
                        for (int i = 0; i < beanList.size(); i++) {
                            String mTime = beanList.get(i).getTime();
                            int mHours = NumberFormatUtils.toInt(mTime.split(":")[0]);
                            if (mHours >= 8) {
                                if (mHours == 8) {
                                    if (NumberFormatUtils.toInt(time.split(":")[0]) == 0) {

                                    } else {
                                        timeBeanList.add(beanList.get(i));
                                    }
                                } else {
                                    timeBeanList.add(beanList.get(i));
                                }

                            }
                        }
                    }
                }

                // 添加灰色（不可选状态）
                for (int i = timeBeanList.size(); i < totalNumber; i++) {
                    ScheduleBean.TimeBean timeBean = new ScheduleBean.TimeBean();
                    if (i < totalNumber - semiLattice) {
                        // 设置灰色状态值
                        timeBean.setState("2");
                    }
                    timeBeanList.add(timeBean);
                }
                break;
            case 2:// 每月
                // 添加空白
                for (int i = 0; i < 1; i++) {
                    ScheduleBean.TimeBean timeBean = new ScheduleBean.TimeBean();
                    timeBeanList.add(timeBean);
                }
                timeBeanList.addAll(itemBean.getTime());
                for (int i = timeBeanList.size(); i < 32; i++) {
                    ScheduleBean.TimeBean timeBean = new ScheduleBean.TimeBean();
                    timeBeanList.add(timeBean);
                }
                break;
        }

        // 根据状态设置相应颜色
        for (int i = 0; i < timeBeanList.size(); i++) {
            View v = LayoutInflater.from(mContext).inflate(R.layout.adapter_schedule_list_item_item, null);
            v.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
            View viewById = v.findViewById(R.id.id_view);
            mRecyclerView.addView(v);
            // 1：未预定 2：已预订
            String state = timeBeanList.get(i).getState();
            if (TextUtils.isEmpty(state)) {
                // 为空时设置白色
                viewById.setBackgroundResource(R.color.white);
            } else {
                if ("2".equals(state)) {
                    // 已预订
                    viewById.setBackgroundResource(R.color.text_e5);
                } else if ("1".equals(state)) {
                    //未预定
                    viewById.setBackgroundResource(R.color.text_ffc668);

                } else {
                    // 其他
                    viewById.setBackgroundResource(R.color.white);
                }
            }
        }

        switch (type) {
            case 1:// 每天
                String week = itemBean.getWeek();
                switch (position) {
                    case 0:// 今天
                        week = "今天";
                        break;
                    case 1:// 明天
                        week = "明天";
                        break;
                    case 2:// 后天
                        week = "后天";
                        break;
                }
                mDayTextView.setText(week);
                break;
            case 2:// 每月
                String month = itemBean.getMonth();
                switch (position) {
                    case 0:// 本月
                        month = "本";
                        break;
                    case 1:// 下月
                        month = "下";
                        break;
                }
                mDayTextView.setText(month + "月");
                break;
        }

    }

}
