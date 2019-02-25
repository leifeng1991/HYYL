package com.xxzlkj.licallife.fragment;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xxzlkj.licallife.R;
import com.xxzlkj.licallife.activity.localLife.SelectServiceTimeActivity;
import com.xxzlkj.licallife.adapter.SelectServiceTimeTabAdapter;
import com.xxzlkj.licallife.model.ScheduleBean;
import com.xxzlkj.zhaolinshare.base.base.ReuseViewFragment;
import com.xxzlkj.zhaolinshare.base.util.PixelUtil;
import com.xxzlkj.zhaolinshare.base.util.ToastManager;

import java.util.List;

/**
 * 描述:选择服务时间
 *
 * @author zhangrq
 *         2017/3/20 16:37
 */
public class SelectServiceTimeTabFragment extends ReuseViewFragment {

    public static final String SERVER_TIME_STYLE = "serverTimeStyle";
    public static final String SERVICE_MINUTES_NUMBER = "serviceMinutesNumber";

    private RecyclerView mRecyclerView;
    private SelectServiceTimeTabAdapter adapter;
    private String serverTimeStyle;
    private List<ScheduleBean.TimeBean> timeList;
    private int serviceMinutesNumber;
    private ScheduleBean.TimeBean selectTimeBean;

    /**
     * @param args           里面含有字段 isPeriodOfTime 是否是时间段，true为时间段，false为时间点
     * @param time           时间的列表
     * @param selectTimeBean 上次选中的时间
     */
    public static SelectServiceTimeTabFragment newInstance(Bundle args, List<ScheduleBean.TimeBean> time, ScheduleBean.TimeBean selectTimeBean) {
        SelectServiceTimeTabFragment fragment = new SelectServiceTimeTabFragment();
        fragment.setArguments(args);
        fragment.timeList = time;
        fragment.selectTimeBean = selectTimeBean;
        return fragment;
    }

    @Override
    public View loadViewLayout(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_tab_select_service_time, container, false);
    }

    @Override
    protected void findViewById() {
        mRecyclerView = getView(R.id.recyclerView);
        mRecyclerView.setItemAnimator(null);
        if (getArguments() != null) {
            serverTimeStyle = getArguments().getString(SERVER_TIME_STYLE);// 是否是时间段,默认为false
            serviceMinutesNumber = getArguments().getInt(SERVICE_MINUTES_NUMBER);// 服务多少分钟
        }
        // 设置mRecyclerView Padding距离
        int paddingLeftOrRight = PixelUtil.dip2px(mContext, "2".equals(serverTimeStyle) ? 3.5f : 5);// 样式2（时间段）的距离为此
        mRecyclerView.setPadding(paddingLeftOrRight, mRecyclerView.getPaddingTop(), paddingLeftOrRight, mRecyclerView.getPaddingBottom());
        // 初始化
        mRecyclerView.setLayoutManager(new GridLayoutManager(mActivity, "2".equals(serverTimeStyle) ? 4 : 5));
        adapter = new SelectServiceTimeTabAdapter(mContext, R.layout.item_select_service_time, serverTimeStyle, serviceMinutesNumber);
        adapter.setSelectBean(selectTimeBean);// 设置上次选中的时间
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void setListener() {
        adapter.setOnItemClickListener((position, item) -> {
            // 可预约设置，不可预约不操作
            if ("2".equals(item.getState()) || "2".equals(item.getTimePeriodState())) {
                // 已预约
                ToastManager.showShortToast(mContext, "该时间不可约");
            } else {
                // 未预约
                // 设置当前选中
                adapter.setSelectBean(item);
                adapter.notifySelect(position);
                // 设置选中
                ((SelectServiceTimeActivity) mActivity).setSelectBean(item, position);
            }
        });
    }

    @Override
    public void processLogic() {
        // 设置数据
        adapter.addList(timeList);
    }

    /**
     * 设置选中
     */
    public void setSelectBean(ScheduleBean.TimeBean bean, int position) {
        if (adapter != null) {
            adapter.setSelectBean(bean);
            adapter.notifySelect(position);
        }
    }

}
