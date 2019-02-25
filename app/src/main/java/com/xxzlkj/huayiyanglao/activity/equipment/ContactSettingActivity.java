package com.xxzlkj.huayiyanglao.activity.equipment;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.xxzlkj.huayiyanglao.R;
import com.xxzlkj.huayiyanglao.adapter.UserWatchAdapter;
import com.xxzlkj.huayiyanglao.app.ZhaoLinApplication;
import com.xxzlkj.huayiyanglao.config.ZLConstants;
import com.xxzlkj.huayiyanglao.config.ZLURLConstants;
import com.xxzlkj.huayiyanglao.model.CheckWatchExistBean;
import com.xxzlkj.huayiyanglao.model.WatchFastListBean;
import com.xxzlkj.shop.weight.CustomButton;
import com.xxzlkj.zhaolinshare.base.MyRecyclerView;
import com.xxzlkj.zhaolinshare.base.base.MyBaseActivity;
import com.xxzlkj.zhaolinshare.base.model.User;
import com.xxzlkj.zhaolinshare.base.net.OnMyActivityRequestListener;
import com.xxzlkj.zhaolinshare.base.net.RequestManager;
import com.xxzlkj.zhaolinshare.base.util.LogUtil;
import com.xxzlkj.zhaolinshare.base.util.ToastManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 描述:拨号设置
 *
 * @author zhangrq
 *         2017/3/21 14:48
 */
public class ContactSettingActivity extends MyBaseActivity {
    private MyRecyclerView mMonitoringRecyclerView;
    private TextView mDevicePhoneTextView, mPupillusTextView, mApplyingTextView1, mApplyingTextView2, mMonitoringTextView;
    private CustomButton mSpeedDialButton, mUrgentMessageButton, mMonitoringButton;
    private RecyclerView mSpeedDialRecyclerView, mUrgentMessageRecyclerView;
    private LinearLayout mDeviceInfoLayout, mSpeedDialLayout, mUrgentMessageLayout, mMonitoringLayout, mApplyingLayout;
    private RelativeLayout mNoBindDeviceLayout;
    private View headerView;
    private ImageView mApplyingImageView;
    private UserWatchAdapter mSpeedDialAdapter, mUrgentMessageAdapter, mMonitoringAdapter;
    private int page = 1;

    public static Intent newIntent(Context context) {
        return new Intent(context, ContactSettingActivity.class);
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_contact_setting);
    }

    @Override
    protected void findViewById() {
        mMonitoringRecyclerView = getView(R.id.id_recycler_view);// 监护号码列表
        headerView = LayoutInflater.from(mContext).inflate(R.layout.layout_contact_header, null);
        mDeviceInfoLayout = headerView.findViewById(R.id.id_device_info);// 设备信息布局
        mNoBindDeviceLayout = headerView.findViewById(R.id.id_no_bind_device);// 没有绑定设备和没有添加监护布局
        mPupillusTextView = headerView.findViewById(R.id.id_pupillus);// 被监护人
        mDevicePhoneTextView = headerView.findViewById(R.id.id_device_phone);// 设备手机号
        mSpeedDialLayout = headerView.findViewById(R.id.id_speed_dial_layout);// 快捷拨号布局
        mSpeedDialButton = headerView.findViewById(R.id.id_speed_dial_btn);// 快捷拨号添加按钮
        mSpeedDialRecyclerView = headerView.findViewById(R.id.id_speed_dial_list);// 快捷拨号列表
        mUrgentMessageLayout = headerView.findViewById(R.id.id_unread_message_layout);// 紧急通知布局
        mUrgentMessageButton = headerView.findViewById(R.id.id_urgent_message_btn);// 紧急通知添加按钮
        mUrgentMessageRecyclerView = headerView.findViewById(R.id.id_urgent_message_list);// 紧急通知列表
        mMonitoringLayout = headerView.findViewById(R.id.id_monitoring_layout);// 监护号码布局
        mMonitoringTextView = headerView.findViewById(R.id.id_monitoring_tip);// 监护号码提示
        mMonitoringButton = headerView.findViewById(R.id.id_monitoring_btn);// 监护号码添加按钮
        mApplyingLayout = getView(R.id.id_layout_2);// 申请中显示布局
        mApplyingImageView = getView(R.id.id_tip_image);
        mApplyingTextView1 = getView(R.id.id_tip_text_1);
        mApplyingTextView2 = getView(R.id.id_tip_text_2);

        init();
    }

    private void init() {
        // 监控号码列表
        mMonitoringRecyclerView.setPullRefreshAndLoadingMoreEnabled(false, true);
        mMonitoringAdapter = new UserWatchAdapter(mContext, R.layout.item_contact_setting);
        mMonitoringRecyclerView.setAdapter(mMonitoringAdapter);
        mMonitoringRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mMonitoringRecyclerView.addHeaderView(headerView);
        // 快捷拨号列表
        mSpeedDialRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mSpeedDialRecyclerView.setNestedScrollingEnabled(false);
        mSpeedDialAdapter = new UserWatchAdapter(mContext, R.layout.item_contact_setting);
        mSpeedDialRecyclerView.setAdapter(mSpeedDialAdapter);
        // 紧急通知列表
        mUrgentMessageRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mUrgentMessageRecyclerView.setNestedScrollingEnabled(false);
        mUrgentMessageAdapter = new UserWatchAdapter(mContext, R.layout.item_contact_setting);
        mUrgentMessageRecyclerView.setAdapter(mUrgentMessageAdapter);
        // 等待审核
        mApplyingImageView.setImageResource(R.mipmap.ic_checking);
        mApplyingTextView1.setText("请等待对方审核通过");
        mApplyingTextView1.setVisibility(View.VISIBLE);
        mApplyingTextView2.setVisibility(View.VISIBLE);

    }

    @Override
    protected void setListener() {
        // 加载监听
        mMonitoringRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadMore() {

                page = mMonitoringAdapter.getItemCount() / 20 + 1;
            }
        });
        //  没有绑定设备
        mNoBindDeviceLayout.setOnClickListener(v -> {
            // 跳转到绑定界面
            startActivity(BindDeviceActivity.newIntent(mContext));
        });
        // 添加快捷拨号
        mSpeedDialButton.setOnClickListener(v -> {
            int itemCount = mSpeedDialAdapter.getItemCount();
            if (itemCount == 4) {
                ToastManager.showShortToast(mContext, "最多可以添加4个");
                return;
            }
            startActivity(AddEditContactActivity.newIntent(mContext, false, 1, null));
        });
        // 添加紧急通知
        mUrgentMessageButton.setOnClickListener(v -> {
            int itemCount = mUrgentMessageAdapter.getItemCount();
            if (itemCount == 3) {
                ToastManager.showShortToast(mContext, "最多可以添加3个");
                return;
            }
            startActivity(AddEditContactActivity.newIntent(mContext, false, 2, null));
        });
        // 添加监护号
        mMonitoringButton.setOnClickListener(v -> startActivity(AddEditContactActivity.newIntent(mContext, false, 3, null)));
        // 添加快捷拨号列表监听
        mSpeedDialAdapter.setOnItemClickListener((position, item) -> startActivity(AddEditContactActivity.newIntent(mContext, true, 1, item)));
        // 添加紧急通知列表监听
        mUrgentMessageAdapter.setOnItemClickListener((position, item) -> startActivity(AddEditContactActivity.newIntent(mContext, true, 2, item)));
        // 监护人列表监听
        mMonitoringAdapter.setOnItemClickListener((position, item) -> startActivity(AddEditContactActivity.newIntent(mContext, true, 3, item)));
    }

    @Override
    protected void processLogic() {
        setTitleLeftBack();
        setTitleName("拨号设置");

    }

    @Override
    protected void onResume() {
        super.onResume();
        // 归原
        mDeviceInfoLayout.setVisibility(View.GONE);
        mNoBindDeviceLayout.setVisibility(View.GONE);
        mPupillusTextView.setVisibility(View.GONE);
        mSpeedDialLayout.setVisibility(View.GONE);
        mUrgentMessageLayout.setVisibility(View.GONE);
        checkUserWatch();
        getWatchFastList();
    }

    /**
     * 查询是否绑定设备
     */
    private void checkUserWatch() {
        HashMap<String, String> stringStringHashMap = new HashMap<>();
        User loginUser = ZhaoLinApplication.getInstance().getLoginUser();
        if (loginUser == null) return;// 没登录，不请求
        stringStringHashMap.put("uid", loginUser.getData().getId());
        RequestManager.createRequest(ZLURLConstants.CHECK_USER_WATCH_URL, stringStringHashMap, new OnMyActivityRequestListener<CheckWatchExistBean>(this) {

            @Override
            public void onSuccess(CheckWatchExistBean bean) {
                // 已绑定设备
                CheckWatchExistBean.DataBean data = bean.getData();
                mDeviceInfoLayout.setVisibility(View.VISIBLE);
                mSpeedDialLayout.setVisibility(View.VISIBLE);
                mUrgentMessageLayout.setVisibility(View.VISIBLE);
                mMonitoringLayout.setVisibility(View.VISIBLE);
                mDevicePhoneTextView.setText(data.getPhone());
                getWatchFastList("1");
                getWatchFastList("2");
                mMonitoringTextView.setText("我的监护人");
            }

            @Override
            public void onException(int exceptionCode, String exceptionMessage) {

            }

            @Override
            public void onError(String code, String message) {

            }

            @Override
            public void onFailed(CheckWatchExistBean bean, boolean isError, String code, String message) {
                if ("201".equals(code)) {
                    // 已申请监护
                    mPupillusTextView.setVisibility(View.VISIBLE);
                    mMonitoringButton.setVisibility(View.GONE);
                    mPupillusTextView.setText(String.format("被监护人    %s", bean.getData().getPhone()));
                    mMonitoringTextView.setText("共同监护人");
                } else if ("401".equals(code)) {
                    // 已申请监护（申请中）
                    mMonitoringRecyclerView.setVisibility(View.GONE);
                    mApplyingLayout.setVisibility(View.VISIBLE);
                    String imei = bean.getData().getImei();
                    mApplyingTextView2.setText(TextUtils.isEmpty(imei) ? "手机号：" + bean.getData().getPhone() : "监护设备号：" + imei);
                } else {
                    // 未绑定设备、未申请监护
                    mNoBindDeviceLayout.setVisibility(View.VISIBLE);
                    mMonitoringTextView.setText("我的监护人");

                }
            }
        });

    }

    /**
     * 快速拨号 和紧急通知 列表
     *
     * @param type 1 快速拨号 2紧急联系
     */
    private void getWatchFastList(String type) {
        Map<String, String> map = new HashMap<>();
        User loginUserDoLogin = ZhaoLinApplication.getInstance().getLoginUserDoLogin(this);
        if (loginUserDoLogin == null) {
            finish();
            return;
        }
        // 用户id
        map.put(ZLConstants.Params.UID, loginUserDoLogin.getData().getId());
        // 1 快速拨号 2紧急联系
        map.put(ZLConstants.Params.TYPE, type);
        RequestManager.createRequest(ZLURLConstants.WATCH_FAST_LIST_URL, map, new OnMyActivityRequestListener<WatchFastListBean>(this) {
            @Override
            public void onSuccess(WatchFastListBean bean) {
                List<WatchFastListBean.DataBean> data = bean.getData();
                if ("1".equals(type)) {
                    mSpeedDialAdapter.clearAndAddList(data);
                } else {
                    mUrgentMessageAdapter.clearAndAddList(data);
                }
            }

            @Override
            public void onFailed(boolean isError, String code, String message) {

            }
        });
    }

    /**
     * 监护人列表
     */
    private void getWatchFastList() {
        Map<String, String> map = new HashMap<>();
        User loginUserDoLogin = ZhaoLinApplication.getInstance().getLoginUserDoLogin(this);
        if (loginUserDoLogin == null) {
            finish();
            return;
        }
        // 用户id
        map.put(ZLConstants.Params.UID, loginUserDoLogin.getData().getId());
        // 分页
        map.put(ZLConstants.Params.PAGE, page + "");
        RequestManager.createRequest(ZLURLConstants.WATCH_GUARDIANSHIP_LIST_URL, map, new OnMyActivityRequestListener<WatchFastListBean>(this) {
            @Override
            public void onSuccess(WatchFastListBean bean) {
                mMonitoringRecyclerView.handlerSuccessHasRefreshAndLoad(mMonitoringAdapter, page == 1, bean.getData());
                LogUtil.e("===============" + mMonitoringAdapter.getItemCount());
            }

            @Override
            public void onFailed(boolean isError, String code, String message) {
                if (page == 1)
                    mMonitoringAdapter.clear();
            }
        });
    }
}
