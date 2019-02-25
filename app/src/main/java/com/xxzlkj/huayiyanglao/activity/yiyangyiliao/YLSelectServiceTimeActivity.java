package com.xxzlkj.huayiyanglao.activity.yiyangyiliao;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import com.xxzlkj.huayiyanglao.R;
import com.xxzlkj.huayiyanglao.adapter.YLSelectServiceTimeTabOneAdapter;
import com.xxzlkj.huayiyanglao.adapter.YLSelectServiceTimeTabTwoAdapter;
import com.xxzlkj.huayiyanglao.adapter.YLSelectTimeCommonNavigatorAdapter;
import com.xxzlkj.huayiyanglao.fragment.YLSelectServiceTimeTabFragment;
import com.xxzlkj.huayiyanglao.model.ConfirmSubscribeBean;
import com.xxzlkj.huayiyanglao.model.ProjectDetailBean1;
import com.xxzlkj.zhaolinshare.base.base.BaseFragmentPagerAdapter;
import com.xxzlkj.zhaolinshare.base.base.MyBaseActivity;
import com.xxzlkj.zhaolinshare.base.util.ToastManager;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * 描述:选择服务时间
 *
 * @author zhangrq
 *         2017/3/21 14:48
 */
public class YLSelectServiceTimeActivity extends MyBaseActivity {
    public static final String DATA_BEAN = "dataBean";
    public static final String RES_TYPE = "res_type";

    private ViewPager viewPager;
    private TextView btn_config;
    private MagicIndicator magicIndicator;
    private List<Fragment> fragmentList = new ArrayList<>();
    private List<String> titleList = new ArrayList<>();
    private BaseFragmentPagerAdapter fragmentPagerAdapter;
    private YLSelectTimeCommonNavigatorAdapter indicatorAdapter;
    private String res_type;
    private ConfirmSubscribeBean mConfirmSubscribeBean = new ConfirmSubscribeBean();
    private boolean isSelected;
    private YLSelectServiceTimeTabOneAdapter mYlSelectServiceTimeTabOneAdapter;
    private YLSelectServiceTimeTabTwoAdapter mYlSelectServiceTimeTabTwoAdapter;

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_yyyl_select_service_time);
    }

    @Override
    protected void findViewById() {
        magicIndicator = getView(R.id.magic_indicator);
        viewPager = getView(R.id.viewPager);
        btn_config = getView(R.id.btn_config);
    }

    @Override
    public void setListener() {
        // 确定按钮
        btn_config.setOnClickListener(v -> {
            if (!isSelected) {
                ToastManager.showShortToast(mContext, "请选择服务时间");
                return;
            }
            // 跳转到确认预约界面
            startActivity(YLConfirmSubscribeActivity.newIntent(mContext, mConfirmSubscribeBean));
        });
    }

    @Override
    public void processLogic() {
        setTitleLeftBack();
        setTitleName("服务时间");
        // 初始化viewPager和指示器
        fragmentPagerAdapter = new BaseFragmentPagerAdapter(getSupportFragmentManager(), fragmentList);
        viewPager.setOffscreenPageLimit(fragmentList.size());// 设置预加载的数量
        viewPager.setAdapter(fragmentPagerAdapter);
        // 指示器，稳健盈、灵活投
        CommonNavigator commonNavigator = new CommonNavigator(mContext);
        commonNavigator.setScrollPivotX(1f);
        indicatorAdapter = new YLSelectTimeCommonNavigatorAdapter(viewPager, titleList);
        commonNavigator.setAdapter(indicatorAdapter);
        magicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(magicIndicator, viewPager);

        // 设置数据
        ProjectDetailBean1.DataBean dataBean = (ProjectDetailBean1.DataBean) getIntent().getSerializableExtra(DATA_BEAN);// 详情页面传过来的数据
        mConfirmSubscribeBean.setId(dataBean.getId());
        mConfirmSubscribeBean.setService_project(dataBean.getTitle());
        mConfirmSubscribeBean.setService_point(dataBean.getAddress());
        mConfirmSubscribeBean.setService_cost(dataBean.getPrice());
        mConfirmSubscribeBean.setRes_type(dataBean.getRes_type());
        res_type = getIntent().getStringExtra(RES_TYPE);
        List<ProjectDetailBean1.DataBean.ScheduleBean> schedule = dataBean.getSchedule();
        // 设置数据
        setData(schedule);
    }

    private void setData(List<ProjectDetailBean1.DataBean.ScheduleBean> schedule) {
        if (schedule == null)
            return;
        for (int i = 0; i < schedule.size(); i++) {
            ProjectDetailBean1.DataBean.ScheduleBean scheduleBean = schedule.get(i);
            // 设置头
            String week = scheduleBean.getWeek();
            if (i == 0)
                week = "今天";
            else if (i == 1)
                week = "明天";
            // 最近几天的时间段
            titleList.add(scheduleBean.getDay() + "\n" + week);
            // 设置内容
            fragmentList.add(YLSelectServiceTimeTabFragment.newInstance(scheduleBean, res_type, i, (adapter, serviceStartTime, serviceEndTime, number_id) -> {
                isSelected = true;
                // 整体做单选操作
                if (adapter instanceof YLSelectServiceTimeTabOneAdapter) {
                    if (mYlSelectServiceTimeTabOneAdapter != null && mYlSelectServiceTimeTabOneAdapter.selectPosition != -1 && mYlSelectServiceTimeTabOneAdapter != adapter) {
                        // 取消上一次选中的
                        mYlSelectServiceTimeTabOneAdapter.selectPosition = -1;
                        mYlSelectServiceTimeTabOneAdapter.notifyDataSetChanged();
                    }
                    this.mYlSelectServiceTimeTabOneAdapter = (YLSelectServiceTimeTabOneAdapter) adapter;
                } else if (adapter instanceof YLSelectServiceTimeTabTwoAdapter) {
                    if (mYlSelectServiceTimeTabTwoAdapter != null && mYlSelectServiceTimeTabTwoAdapter.selectPosition != -1 && mYlSelectServiceTimeTabTwoAdapter != adapter) {
                        // 取消上一次选中的
                        mYlSelectServiceTimeTabTwoAdapter.selectPosition = -1;
                        mYlSelectServiceTimeTabTwoAdapter.notifyDataSetChanged();
                    }
                    this.mYlSelectServiceTimeTabTwoAdapter = (YLSelectServiceTimeTabTwoAdapter) adapter;
                }
                mConfirmSubscribeBean.setStarttime(serviceStartTime);
                mConfirmSubscribeBean.setEndtime(serviceEndTime);
                mConfirmSubscribeBean.setNumber_id(number_id);
            }));// extras里面含有字段：服务时间样式
        }
        // 通知改变
        fragmentPagerAdapter.notifyDataSetChanged();
        indicatorAdapter.notifyDataSetChanged();
    }

    /**
     * @param desDataBean 必传 详情的数据，通过此数据获取到，预约时间信息
     * @param res_type    1：按时间段 2按人数
     */
    public static Intent newIntent(Context context, Serializable desDataBean, String res_type) {
        Intent intent = new Intent(context, YLSelectServiceTimeActivity.class);
        Bundle extras = new Bundle();
        intent.putExtras(extras);
        intent.putExtra(DATA_BEAN, desDataBean);
        intent.putExtra(RES_TYPE, res_type);
        return intent;
    }

}
