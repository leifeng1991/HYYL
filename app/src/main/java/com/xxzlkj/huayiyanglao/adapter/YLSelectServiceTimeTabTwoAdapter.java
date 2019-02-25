package com.xxzlkj.huayiyanglao.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.widget.TextView;

import com.xxzlkj.huayiyanglao.R;
import com.xxzlkj.huayiyanglao.fragment.YLSelectServiceTimeTabFragment;
import com.xxzlkj.huayiyanglao.model.ProjectDetailBean1;
import com.xxzlkj.zhaolinshare.base.base.BaseAdapter;
import com.xxzlkj.zhaolinshare.base.base.BaseViewHolder;
import com.xxzlkj.zhaolinshare.base.util.NumberFormatUtils;
import com.zrq.spanbuilder.Spans;

/**
 * 描述:
 *
 * @author leifeng
 *         2018/3/9 10:22
 */


public class YLSelectServiceTimeTabTwoAdapter extends BaseAdapter<ProjectDetailBean1.DataBean.ScheduleBean.ScheduleDataBean> {
    public int selectPosition = -1;
    private YLSelectServiceTimeTabFragment.SelectServiceTimeListener selectServiceTimeListener;

    public YLSelectServiceTimeTabTwoAdapter(Context context, int itemId, YLSelectServiceTimeTabFragment.SelectServiceTimeListener selectServiceTimeListener) {
        super(context, itemId);
        this.selectServiceTimeListener = selectServiceTimeListener;
    }

    @Override
    public void convert(BaseViewHolder holder, int position, ProjectDetailBean1.DataBean.ScheduleBean.ScheduleDataBean itemBean) {
        TextView mTimeTitleTextView = holder.getView(R.id.id_time_title);// 上午 中午 下午
        TextView mTimeTextView = holder.getView(R.id.id_time);// 时间
        TextView mRemainderTextView = holder.getView(R.id.id_remainder);// 余数
        TextView mStateTextView = holder.getView(R.id.id_state);// 状态
        // 设置数据
        mTimeTitleTextView.setText(itemBean.getTitle());
        mTimeTextView.setText(String.format("%s-%s", itemBean.getStarttime(), itemBean.getEndtime()));
        String count = itemBean.getCount();
        // true：已满 false：还有余额
        boolean isAlreadyFull = TextUtils.isEmpty(count) || NumberFormatUtils.toInt(count) <= 0;
        mRemainderTextView.setText(Spans.builder().text("剩余").text(isAlreadyFull ? "0" : count).color(0xffb6e571).text("个").build());
        mStateTextView.setText(isAlreadyFull ? "已满" : "预约");
        mStateTextView.setTextColor(ContextCompat.getColor(mContext, (isAlreadyFull || selectPosition == position) ? R.color.white : R.color.text_4));
        mStateTextView.setBackgroundResource(isAlreadyFull ? com.xxzlkj.licallife.R.drawable.shape_rectangle_c8c8c8 : selectPosition == position ? R.drawable.shape_rectangle_solid_54b1f8 : R.drawable.shape_rectangle_stroke_c8);
        // 预约
        mStateTextView.setOnClickListener(v -> {
            if (!isAlreadyFull) {
                // 可以预约
                selectPosition = position;
                notifyDataSetChanged();
                if (selectServiceTimeListener != null)
                    selectServiceTimeListener.onSelectServiceTime(this, itemBean.getTimestamp(), itemBean.getTimestamp_end(), itemBean.getId());
            }
        });
    }

    public int getSelectPosition() {
        return selectPosition;
    }
}
