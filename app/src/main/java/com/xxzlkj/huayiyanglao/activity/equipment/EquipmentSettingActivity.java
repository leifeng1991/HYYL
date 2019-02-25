package com.xxzlkj.huayiyanglao.activity.equipment;


import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.xxzlkj.huayiyanglao.R;
import com.xxzlkj.huayiyanglao.app.ZhaoLinApplication;
import com.xxzlkj.huayiyanglao.config.ZLConstants;
import com.xxzlkj.huayiyanglao.model.UserWatchBean;
import com.xxzlkj.huayiyanglao.util.HYNetRequestUtil;
import com.xxzlkj.huayiyanglao.util.ZLActivityUtils;
import com.xxzlkj.zhaolinshare.base.base.MyBaseActivity;
import com.xxzlkj.zhaolinshare.base.model.BaseBean;
import com.xxzlkj.zhaolinshare.base.model.User;
import com.xxzlkj.zhaolinshare.base.net.OnMyActivityRequestListener;
import com.xxzlkj.zhaolinshare.base.net.RequestManager;
import com.xxzlkj.zhaolinshare.base.util.ToastManager;

import java.util.HashMap;
import java.util.Map;

import static com.xxzlkj.huayiyanglao.config.ZLURLConstants.USER_WATCH;
import static com.xxzlkj.huayiyanglao.config.ZLURLConstants.USER_WATCH_EDIT;

/**
 * 智能设备-设置
 *
 * @author zhangrq
 */
public class EquipmentSettingActivity extends MyBaseActivity {

    public static final int REQUEST_CODE_SCAN = 123;
    public static final int REQUEST_CODE_DZWL = 456;
    public static final int REQUEST_CODE_JJLXR = 789;
    private ViewGroup vg_id_layout, vg_jjlxr_layout, vg_dzwl_layout, vg_update_layout;
    private TextView tv_id_value, btn_summit, tv_update_value, tv_dzwl_value;
    private EditText et_phone_value;
    private UserWatchBean.DataBean userWatchData;

    public static Intent newIntent(Context context) {
        return new Intent(context, EquipmentSettingActivity.class);
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_equipment_setting);
    }

    @Override
    protected void findViewById() {
        // 设备号
        vg_id_layout = getView(R.id.vg_id_layout);
        tv_id_value = getView(R.id.tv_id_value);
        // 定位更新频率
        vg_update_layout = getView(R.id.vg_update_layout);
        tv_update_value = getView(R.id.tv_update_value);
        // 电子围栏
        vg_dzwl_layout = getView(R.id.vg_dzwl_layout);
        tv_dzwl_value = getView(R.id.tv_dzwl_value);
        // 紧急联系人
        vg_jjlxr_layout = getView(R.id.vg_jjlxr_layout);
        // 设备手机号
        et_phone_value = getView(R.id.et_phone_value);
        // 提交按钮
        btn_summit = getView(R.id.btn_summit);
    }

    @Override
    protected void setListener() {
        // 扫描设备号
        vg_id_layout.setOnClickListener(v -> startActivityForResult(ScanCodeActivity.newIntent(mContext), REQUEST_CODE_SCAN));
        // 电子围栏
        vg_dzwl_layout.setOnClickListener(v -> startActivityForResult(ElectronicFenceActivity.newIntent(mContext), REQUEST_CODE_DZWL));
        // 紧急联系人
        vg_jjlxr_layout.setOnClickListener(v -> startActivityForResult(ContactSettingActivity.newIntent(mContext), REQUEST_CODE_JJLXR));
        // 提交按钮
        btn_summit.setOnClickListener(v -> check());
    }

    @Override
    protected void processLogic() {
        setTitleLeftBack();
        setTitleName("设置");
        getUserWatch();
        HYNetRequestUtil.getUserWatch(this, bean -> {
            // 初始化相关值
            userWatchData = bean.getData();
            tv_id_value.setText(userWatchData.getImei());
            et_phone_value.setText(userWatchData.getWatch_phone());
            tv_dzwl_value.setText("2".equals(userWatchData.getFence_state()) ? "开启" : "关闭");
        });
    }

    /**
     * 获取绑定手表信息
     */
    private void getUserWatch() {
        Map<String, String> map = new HashMap<>();
        User loginUserDoLogin = ZhaoLinApplication.getInstance().getLoginUserDoLogin(this);
        if (loginUserDoLogin == null) {
            finish();
            return;
        }
        // 用户id
        map.put(ZLConstants.Params.UID, loginUserDoLogin.getData().getId());
        RequestManager.createRequest(USER_WATCH, map, new OnMyActivityRequestListener<UserWatchBean>(this) {
            @Override
            public void onSuccess(UserWatchBean bean) {
                userWatchData = bean.getData();
                tv_id_value.setText(userWatchData.getImei());
                et_phone_value.setText(userWatchData.getWatch_phone());
                tv_dzwl_value.setText("2".equals(userWatchData.getFence_state()) ? "开启" : "关闭");
            }
        });
    }

    /**
     * 检测
     */
    private void check() {
        String imei = tv_id_value.getText().toString();
        if (TextUtils.isEmpty(imei)) {
            ToastManager.showMsgToast(mContext, "请输入设备号");
            return;
        }
        submit();
    }

    /**
     * 提交绑定
     */
    private void submit() {
       /* userWatchData.setImei(tv_id_value.getText().toString());
        String phone = et_phone_value.getText().toString().trim();
        if (!TextUtils.isEmpty(phone))
            userWatchData.setWatch_phone(phone);
        HYNetRequestUtil.userWatchEdit(this, userWatchData, new HYNetRequestUtil.OnSuccessListener() {
            @Override
            public void RequestSuccess(BaseBean bean) {
                ToastManager.showMsgToast(mContext, "绑定成功");
                finish();
            }
        });*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (requestCode == REQUEST_CODE_SCAN) {
                // 扫描
                String result = ScanCodeActivity.getResult(data);
                if (!TextUtils.isEmpty(result)) {
                    tv_id_value.setText(result);
                    // 手表imei号
                    userWatchData.setImei(result);
                }

            }

            if (requestCode == REQUEST_CODE_DZWL) {
                // 电子围栏
                UserWatchBean.DataBean intentResult = ElectronicFenceActivity.getIntentResult(data);
                if (intentResult != null) {
                    userWatchData = intentResult;
                    tv_dzwl_value.setText("2".equals(userWatchData.getFence_state()) ? "开启" : "关闭");
                }


            }
        }

    }
}
