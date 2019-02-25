package com.xxzlkj.huayiyanglao.activity.equipment;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.amap.api.maps.model.LatLng;
import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;
import com.xxzlkj.huayiyanglao.R;
import com.xxzlkj.huayiyanglao.app.ZhaoLinApplication;
import com.xxzlkj.huayiyanglao.config.ZLURLConstants;
import com.xxzlkj.huayiyanglao.model.LocationRecordListBean;
import com.xxzlkj.huayiyanglao.model.UserWatchBean;
import com.xxzlkj.huayiyanglao.util.HYNetRequestUtil;
import com.xxzlkj.huayiyanglao.util.ZLDialogUtil;
import com.xxzlkj.zhaolinshare.base.base.BaseActivity;
import com.xxzlkj.zhaolinshare.base.base.MyBaseActivity;
import com.xxzlkj.zhaolinshare.base.model.BaseBean;
import com.xxzlkj.zhaolinshare.base.model.User;
import com.xxzlkj.zhaolinshare.base.net.OnMyActivityRequestListener;
import com.xxzlkj.zhaolinshare.base.net.RequestManager;
import com.xxzlkj.zhaolinshare.base.util.DateUtils;
import com.xxzlkj.zhaolinshare.base.util.LogUtil;
import com.xxzlkj.zhaolinshare.base.util.NumberFormatUtils;
import com.xxzlkj.zhaolinshare.base.util.SystemBarUtils;
import com.xxzlkj.zhaolinshare.base.util.ToastManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


/**
 * 描述:定位设置
 *
 * @author zhangrq
 *         2017/3/21 14:48
 */
public class LocationSettingActivity extends BaseActivity {
    private TextView mElectronicFenceTextView, mStartTimeTextView, mStopTimeTextView, mUpdateFrequencyTextView, mDeleteAllRecordTextView;
    private UserWatchBean.DataBean dataBean;
    private boolean isDeleteAllPosition;// 是否删除所有定位记录

    public static Intent newIntent(Context context) {
        return new Intent(context, LocationSettingActivity.class);
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_location_setting);
        // 解决使用Android-Picker控件时状态栏不变暗
        SystemBarUtils.setStatusBarTranslucent(this);
        SystemBarUtils.setStatusBarLightMode(this, true);
        SystemBarUtils.setPaddingTopStatusBarHeight(this, getView(R.id.id_parent));
    }

    @Override
    protected void findViewById() {
        mElectronicFenceTextView = getView(R.id.id_electronic_fence);// 电子围栏
        mStartTimeTextView = getView(R.id.id_start_time);// 开始时间
        mStopTimeTextView = getView(R.id.id_stop_time);// 结束时间
        mUpdateFrequencyTextView = getView(R.id.id_update_frequency);// 定位更新频率
        mDeleteAllRecordTextView = getView(R.id.id_delete_all_record);// 删除所有记录
    }

    @Override
    protected void setListener() {
        // 电子围栏
        mElectronicFenceTextView.setOnClickListener(v -> startActivity(ElectronicFenceActivity.newIntent(mContext)));
        // 开始时间
        mStartTimeTextView.setOnClickListener(v -> choiceTime(mStartTimeTextView));
        // 结束时间
        mStopTimeTextView.setOnClickListener(v -> choiceTime(mStopTimeTextView));
        // 定位更新频率
        mUpdateFrequencyTextView.setOnClickListener(v -> showServiceFrequencyDialog());
        // 删除所有记录
        mDeleteAllRecordTextView.setOnClickListener(v -> ZLDialogUtil.showTwoButtonDialog(
                LocationSettingActivity.this, "确认删除所有位置记录？", this::delWatchUltraList));
    }

    @Override
    protected void processLogic() {
        setTitleLeftBack();
        setTitleName("定位设置");
        HYNetRequestUtil.getUserWatch(LocationSettingActivity.this, userWatchBean -> {
            dataBean = userWatchBean.getData();
            mStartTimeTextView.setText(dataBean.getWatch_starttime());
            mStopTimeTextView.setText(dataBean.getWatch_endtime());
            mUpdateFrequencyTextView.setText(String.format("%s分钟/次", dataBean.getWatch_interval()));
        });
    }

    @Override
    public void onTitleRightClick(View view) {
        super.onTitleRightClick(view);
    }

    @Override
    public void onBackPressed() {
        if (isDeleteAllPosition){
            Intent intent = new Intent();
            setResult(RESULT_OK,intent);
            finish();
        }else {
            super.onBackPressed();
        }
    }

    /**
     * 选择时间
     */
    private void choiceTime(TextView textView) {
        Calendar endCalendar = Calendar.getInstance();
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.set(1900, 1, 1);
        endCalendar.setTimeInMillis(System.currentTimeMillis());
        TimePickerView pvTime = new TimePickerView.Builder(this, (date, v) -> {//选中事件回调
            int hours = date.getHours();
            int minutes = date.getMinutes();
            textView.setText(String.format("%s:%s", hours >= 10 ? hours : "0" + hours, minutes >= 10 ? minutes : "0" + minutes));
            if (dataBean != null) {
                dataBean.setWatch_starttime(mStartTimeTextView.getText().toString());
                dataBean.setWatch_endtime(mStopTimeTextView.getText().toString());
                userWatchEdit();
            }

        }).setTitleText("请选择时间")
                .setTitleSize(15)
                .setTitleColor(0xffababab)
                .setBgColor(0xffc6c9cf)
                .setRangDate(startCalendar, endCalendar)
                .setTitleBgColor(0xfff5f5f5)
                .setType(TimePickerView.Type.HOURS_MINS).build();
        pvTime.setDate(Calendar.getInstance());//注：根据需求来决定是否使用该方法（一般是精确到秒的情况），此项可以在弹出选择器的时候重新设置当前时间，避免在初始化之后由于时间已经设定，导致选中时间与当前时间不匹配的问题。
        pvTime.show();
    }

    /**
     * 修改绑定信息
     */
    private void userWatchEdit() {
        if (dataBean != null)
            HYNetRequestUtil.userWatchEdit(this, dataBean, new OnMyActivityRequestListener<BaseBean>(this) {
                @Override
                public void onSuccess(BaseBean bean) {
                }

                @Override
                public void onFailed(boolean isError, String code, String message) {
                    finish();
                }
            });
    }

    /**
     * 选择频次对话框
     */
    private void showServiceFrequencyDialog() {
        List<String> list = new ArrayList<>();
        int[] s = new int[]{5, 6, 7, 8, 9, 10, 15, 20, 25, 30, 40, 50, 60, 70, 80, 90, 100, 110, 120, 130, 140, 150, 160, 170, 180};
        for (int value : s) {
            list.add(value + "分钟");
        }
        //条件选择器
        OptionsPickerView pvOptions = new OptionsPickerView.Builder(this, (options1, option2, options3, v) -> {
            mUpdateFrequencyTextView.setText(String.format("%s/次", list.get(options1)));
            if (dataBean != null) {
                String updateFrequency = mUpdateFrequencyTextView.getText().toString();
                dataBean.setWatch_interval(updateFrequency.split("分钟/次")[0]);
                userWatchEdit();
            }
        })
                .setTitleSize(15)
                .setTitleColor(0xffababab)
                .setBgColor(0xffc6c9cf)
                .setTitleBgColor(0xfff5f5f5)
                .setTitleText("请选择定位数据更新频率")
                .build();
        pvOptions.setPicker(list);
        pvOptions.show();
    }

    /**
     * 删除手表所有位置
     */
    private void delWatchUltraList() {
        HashMap<String, String> map = new HashMap<>();
        User user = ZhaoLinApplication.getInstance().getLoginUserDoLogin(this);
        if (user == null) {
            finish();
            return;
        }
        // 用户id
        map.put("uid", user.getData().getId());
        RequestManager.createRequest(ZLURLConstants.DEL_WATCH_ULTRA_LIST_URL, map, new OnMyActivityRequestListener<BaseBean>(this) {
            @Override
            public void onSuccess(BaseBean bean) {
                isDeleteAllPosition = true;
                ToastManager.showShortToast(mContext,"删除成功");
            }
        });
    }
}
