package com.xxzlkj.huayiyanglao.activity;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import com.xxzlkj.huayiyanglao.fragment.HealthSelectServiceTimeTabFragment;
import com.xxzlkj.licallife.R;
import com.xxzlkj.licallife.adapter.SelectTimeCommonNavigatorAdapter;
import com.xxzlkj.licallife.fragment.SelectServiceTimeTabFragment;
import com.xxzlkj.licallife.model.CleanerDetailBean;
import com.xxzlkj.licallife.model.NannyAndMaternityMatronDesBean;
import com.xxzlkj.licallife.model.ProjectDetail;
import com.xxzlkj.licallife.model.ScheduleBean;
import com.xxzlkj.shop.utils.MyDrawableUtils;
import com.xxzlkj.zhaolinshare.base.base.BaseFragmentPagerAdapter;
import com.xxzlkj.zhaolinshare.base.base.MyBaseActivity;

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
public class HealthSelectServiceTimeActivity extends MyBaseActivity {
    public static final String SELECT_ITEM = "selectItem";
    public static final String DATA_BEAN = "dataBean";
    public static final String SELECTED_TIME = "selectedTime";

    private ViewPager viewPager;
    private TextView btn_config;
    private MagicIndicator magicIndicator;
    private List<Fragment> fragmentList = new ArrayList<>();
    private List<String> titleList = new ArrayList<>();
    private BaseFragmentPagerAdapter fragmentPagerAdapter;
    private SelectTimeCommonNavigatorAdapter indicatorAdapter;
    private ScheduleBean.TimeBean selectBean;
    private Bundle extras;

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_select_service_time);
    }

    @Override
    protected void findViewById() {
        magicIndicator = getView(R.id.magic_indicator);
        viewPager = getView(R.id.viewPager);
        btn_config = getView(R.id.btn_config);
        MyDrawableUtils.setButtonShapeOrange(mContext, btn_config);
    }

    @Override
    public void setListener() {
        // 确定按钮
        btn_config.setOnClickListener(v -> {
            Intent data = new Intent();
            data.putExtra(SELECT_ITEM, selectBean);
            setResult(RESULT_OK, data);
            finish();
        });
    }

    @Override
    public void processLogic() {
        setTitleLeftBack();
        setTitleName("服务时间");
        extras = getIntent().getExtras();

        // 初始化viewPager和指示器
        fragmentPagerAdapter = new BaseFragmentPagerAdapter(getSupportFragmentManager(), fragmentList);
        viewPager.setOffscreenPageLimit(fragmentList.size());// 设置预加载的数量
        viewPager.setAdapter(fragmentPagerAdapter);
        // 指示器，稳健盈、灵活投
        CommonNavigator commonNavigator = new CommonNavigator(mContext);
        commonNavigator.setScrollPivotX(1f);
        indicatorAdapter = new SelectTimeCommonNavigatorAdapter(viewPager, titleList);
        commonNavigator.setAdapter(indicatorAdapter);
        magicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(magicIndicator, viewPager);

        // 设置数据
        String serverTimeStyle = getIntent().getStringExtra(SelectServiceTimeTabFragment.SERVER_TIME_STYLE);// 服务时间样式：1：最近几天的时间点；2：最近几天的时间段；3：最近几个月的时间点
        Serializable dataBean = getIntent().getSerializableExtra(DATA_BEAN);// 各种详情页面传过来的数据
        long selectedTime = getIntent().getLongExtra(SELECTED_TIME, 0);// 上次选中的时间，用此值设置之前的选中
        ScheduleBean.TimeBean selectTimeBean = new ScheduleBean.TimeBean();
        selectTimeBean.setTimestamp(selectedTime + "");// 里面只是通过 Timestamp 来区分是否选中的

        List<ScheduleBean> schedule = null;
        if (dataBean instanceof NannyAndMaternityMatronDesBean.DataBean) {
            // 保姆或月嫂,详情页传来的
            schedule = ((NannyAndMaternityMatronDesBean.DataBean) dataBean).getSchedule();
        } else if (dataBean instanceof ProjectDetail.DataBean) {
            // 保洁项目,详情页传来的
            schedule = ((ProjectDetail.DataBean) dataBean).getSchedule();
        } else if (dataBean instanceof CleanerDetailBean.DataBean) {
            // 保洁师、小时工,详情页传来的
            schedule = ((CleanerDetailBean.DataBean) dataBean).getSchedule();
        }
        // 设置数据
        setData(serverTimeStyle, schedule,selectTimeBean);
    }


    private void setData(String serverTimeStyle, List<ScheduleBean> schedule, ScheduleBean.TimeBean selectTimeBean) {
        if (schedule == null)
            return;
        for (int i = 0; i < schedule.size(); i++) {
            ScheduleBean scheduleBean = schedule.get(i);
            // 设置头
            String week = scheduleBean.getWeek();
            if (i == 0)
                week = "今天";
            else if (i == 1)
                week = "明天";
            if ("1".equals(serverTimeStyle)) {
                // 1：最近几天的时间点
                titleList.add(scheduleBean.getDay() + "\n" + week);
            } else if ("2".equals(serverTimeStyle)) {
                // 2：最近几天的时间段
                titleList.add(scheduleBean.getDay() + "\n" + week);
            } else if ("3".equals(serverTimeStyle)) {
                // 3：最近几个月的时间点
                titleList.add(scheduleBean.getMonth() + "月");
            }
            // 设置内容
            fragmentList.add(HealthSelectServiceTimeTabFragment.newInstance(extras, scheduleBean.getTime(),selectTimeBean));// extras里面含有字段：服务时间样式
        }
        // 通知改变
        fragmentPagerAdapter.notifyDataSetChanged();
        indicatorAdapter.notifyDataSetChanged();
    }

    /**
     * @param serverTimeStyle      必传 服务时间样式：1：最近几天的时间点；2：最近几天的时间段；3：最近几个月的时间点
     * @param desDataBean          必传 详情的数据，通过此数据获取到，预约时间信息
     * @param serviceMinutesNumber 选传 服务分钟数；serverTimeStyle = 2 时必传时间，否则传0
     * @param selectedTime         选传 上次选中的时间(单位：秒)，用此值设置之前的选中
     */
    public static Intent newIntent(Context context, String serverTimeStyle, Serializable desDataBean, int serviceMinutesNumber, long selectedTime) {
        Intent intent = new Intent(context, HealthSelectServiceTimeActivity.class);
        Bundle extras = new Bundle();
        extras.putString(SelectServiceTimeTabFragment.SERVER_TIME_STYLE, serverTimeStyle);
        intent.putExtras(extras);
        intent.putExtra(DATA_BEAN, desDataBean);
        intent.putExtra(SELECTED_TIME, selectedTime);
        intent.putExtra(SelectServiceTimeTabFragment.SERVICE_MINUTES_NUMBER, serviceMinutesNumber);
        return intent;
    }

    /**
     * 获取选中后的结果值
     */
    public static ScheduleBean.TimeBean getResult(Intent data) {
        if (data == null)
            return null;
        return (ScheduleBean.TimeBean) data.getSerializableExtra(SELECT_ITEM);
    }

    /**
     * 设置选中的Item
     */
    public void setSelectBean(ScheduleBean.TimeBean selectBean, int nowPosition) {
        this.selectBean = selectBean;
        // 通知选中
        for (Fragment fragment : fragmentList) {
            if (fragment instanceof SelectServiceTimeTabFragment) {
                ((SelectServiceTimeTabFragment) fragment).setSelectBean(selectBean, nowPosition);
            }
        }
    }
}
