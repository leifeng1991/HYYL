package com.xxzlkj.huayiyanglao.fragment;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xxzlkj.huayiyanglao.R;
import com.xxzlkj.huayiyanglao.adapter.YLSelectServiceTimeTabOneAdapter;
import com.xxzlkj.huayiyanglao.adapter.YLSelectServiceTimeTabTwoAdapter;
import com.xxzlkj.huayiyanglao.event.SelectServiceTimeEvent;
import com.xxzlkj.huayiyanglao.model.ProjectDetailBean1;
import com.xxzlkj.zhaolinshare.base.base.BaseAdapter;
import com.xxzlkj.zhaolinshare.base.base.ReuseViewFragment;
import com.xxzlkj.zhaolinshare.base.util.PixelUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述:选择服务时间
 *
 * @author zhangrq
 *         2017/3/20 16:37
 */
public class YLSelectServiceTimeTabFragment extends ReuseViewFragment {
    public static final String PROJECT_DETAIL_BEAN = "project_detail_bean";
    public static final String RES_TYPE = "res_type";
    public static final String CURRENT_ITEM = "current_item";
    private RecyclerView mRecyclerView;
    private YLSelectServiceTimeTabOneAdapter mYlSelectServiceTimeTabOneAdapter;
    private YLSelectServiceTimeTabTwoAdapter mYlSelectServiceTimeTabTwoAdapter;
    private String res_type;
    private SelectServiceTimeListener selectServiceTimeListener;
    private int currentItem = -1;

    /**
     * @param bean     档期数据
     * @param res_type 1：按时间段 2按人数
     */
    public static YLSelectServiceTimeTabFragment newInstance(ProjectDetailBean1.DataBean.ScheduleBean bean, String res_type, int currentItem, SelectServiceTimeListener selectServiceTimeListener) {
        YLSelectServiceTimeTabFragment fragment = new YLSelectServiceTimeTabFragment();
        fragment.selectServiceTimeListener = selectServiceTimeListener;
        Bundle args = new Bundle();
        args.putSerializable(PROJECT_DETAIL_BEAN, bean);
        args.putString(RES_TYPE, res_type);
        args.putInt(CURRENT_ITEM, currentItem);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View loadViewLayout(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_select_service_time, container, false);
    }

    @Override
    protected void findViewById() {
        mRecyclerView = getView(R.id.recyclerView);
        init();
    }

    private void init() {
        res_type = getArguments().getString(RES_TYPE);
        mRecyclerView.setItemAnimator(null);
        mRecyclerView.setNestedScrollingEnabled(false);
        // 设置mRecyclerView Padding距离
        int paddingLeftOrRight = PixelUtil.dip2px(mContext, 7.5f);
        mRecyclerView.setPadding(paddingLeftOrRight * 2, mRecyclerView.getPaddingTop(), "1".equals(res_type) ? paddingLeftOrRight : paddingLeftOrRight * 2, mRecyclerView.getPaddingBottom());
        // 初始化
        currentItem = getArguments().getInt(CURRENT_ITEM);
        mRecyclerView.setLayoutManager("1".equals(res_type) ? new GridLayoutManager(mActivity, 3) : new LinearLayoutManager(mContext));
        mYlSelectServiceTimeTabOneAdapter = new YLSelectServiceTimeTabOneAdapter(mContext, R.layout.adapter_select_service_time_one);
        mYlSelectServiceTimeTabTwoAdapter = new YLSelectServiceTimeTabTwoAdapter(mContext, R.layout.adapter_select_service_time_two, selectServiceTimeListener);
        mRecyclerView.setAdapter("1".equals(res_type) ? mYlSelectServiceTimeTabOneAdapter : mYlSelectServiceTimeTabTwoAdapter);
    }

    @Override
    public void setListener() {
        // 列表点击事件
        mYlSelectServiceTimeTabOneAdapter.setOnItemClickListener((position, item) -> {
            if ("1".equals(item.getState())) {
                EventBus.getDefault().postSticky(new SelectServiceTimeEvent(currentItem));
                // 可以预约
                mYlSelectServiceTimeTabOneAdapter.selectPosition = position;
                mYlSelectServiceTimeTabOneAdapter.notifyDataSetChanged();
                if (selectServiceTimeListener != null)
                    selectServiceTimeListener.onSelectServiceTime(mYlSelectServiceTimeTabOneAdapter, item.getTimestamp(), item.getTimestamp_end(), "");
            }
        });
    }

    @Override
    public void processLogic() {
        // 设置数据
        ProjectDetailBean1.DataBean.ScheduleBean scheduleBean = (ProjectDetailBean1.DataBean.ScheduleBean) getArguments().getSerializable(PROJECT_DETAIL_BEAN);
        if ("1".equals(res_type)) {
            // 按时间段
            List<ProjectDetailBean1.DataBean.ScheduleBean.TimeBean> time = scheduleBean == null ? new ArrayList<>() : scheduleBean.getTime();
            mYlSelectServiceTimeTabOneAdapter.clearAndAddList(time);
        } else {
            // 按人数
            if (scheduleBean != null){
                List<ProjectDetailBean1.DataBean.ScheduleBean.ScheduleDataBean> data = scheduleBean.getData();
                mYlSelectServiceTimeTabTwoAdapter.clearAndAddList(data);
            }

        }

    }

    public interface SelectServiceTimeListener {
        void onSelectServiceTime(BaseAdapter adapter, String serviceStartTime, String serviceEndTime, String number_id);
    }

}
