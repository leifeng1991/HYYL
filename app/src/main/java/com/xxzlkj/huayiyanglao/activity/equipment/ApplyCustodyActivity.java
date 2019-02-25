package com.xxzlkj.huayiyanglao.activity.equipment;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xxzlkj.huayiyanglao.R;
import com.xxzlkj.huayiyanglao.app.ZhaoLinApplication;
import com.xxzlkj.huayiyanglao.config.ZLConstants;
import com.xxzlkj.huayiyanglao.config.ZLURLConstants;
import com.xxzlkj.huayiyanglao.model.CheckWatchExistBean;
import com.xxzlkj.zhaolinshare.base.base.MyBaseActivity;
import com.xxzlkj.zhaolinshare.base.model.BaseBean;
import com.xxzlkj.zhaolinshare.base.model.User;
import com.xxzlkj.zhaolinshare.base.net.OnMyActivityRequestListener;
import com.xxzlkj.zhaolinshare.base.net.RequestManager;
import com.xxzlkj.zhaolinshare.base.util.TextViewUtils;
import com.xxzlkj.zhaolinshare.base.util.ToastManager;

import java.util.HashMap;
import java.util.Map;


/**
 * 描述:申请监护
 *
 * @author zhangrq
 *         2017/3/21 14:48
 */
public class ApplyCustodyActivity extends MyBaseActivity {
    public static final int REQUEST_CODE_SCAN = 123;
    public static final String JUMP_TYPE = "jump_type";
    public static final String STRINGS = "strings";
    private TextView mSearchTextView, mTipTextView, mTipTextView1, mTipTextView2, mJhDeviceNumberTextView, mJhDevicePhoneTextView;
    private Button mBottomButton, mApplyButton;
    private EditText mInputDeviceNumberEditText, mJhNameEditText;
    private LinearLayout linearLayout1, linearLayout2, linearLayout3;
    private ImageView mTipImageView, mScanCodeImageView;
    private View mLineView;
    private int jumpType;
    private String[] list;
    private boolean isBackEHomeActivity;// 是否返回到智能设备首页

    /**
     * @param jumpType 1：设备管理 2：绑定设备
     * @param strings  为2时传 strings[0]：设备号 strings[1]：手机号
     */
    public static Intent newIntent(Context context, int jumpType, String... strings) {
        Intent intent = new Intent(context, ApplyCustodyActivity.class);
        intent.putExtra(JUMP_TYPE, jumpType);
        intent.putExtra(STRINGS, strings);
        return intent;
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_bind_device);
    }

    @Override
    protected void findViewById() {
        linearLayout1 = getView(R.id.id_layout_1);// 布局1
        mInputDeviceNumberEditText = getView(R.id.id_input_device_number);// 设备码
        mInputDeviceNumberEditText.setHint("请输入设备号/手机号");
        mScanCodeImageView = getView(R.id.id_scan_code);
        mSearchTextView = getView(R.id.id_search);// 搜索
        mTipTextView = getView(R.id.id_tip);// 提示
        mLineView = getView(R.id.id_line);
        linearLayout2 = getView(R.id.id_layout_2);// 布局2
        mTipImageView = getView(R.id.id_tip_image);
        mTipTextView1 = getView(R.id.id_tip_text_1);
        mTipTextView2 = getView(R.id.id_tip_text_2);
        linearLayout3 = getView(R.id.id_layout_3);// 布局3
        mJhDeviceNumberTextView = getView(R.id.id_jh_device_number);// 监护申请设备号
        mJhDevicePhoneTextView = getView(R.id.id_jh_device_phone);// 被监护手机号
        mJhNameEditText = getView(R.id.id_input_name);// 监护申请姓名
        mApplyButton = getView(R.id.id_apply);// 申请监护
        mBottomButton = getView(R.id.id_bottom_button);// 底部按钮
    }

    @Override
    protected void setListener() {
        // 扫码
        mScanCodeImageView.setOnClickListener(v -> startActivityForResult(ScanCodeActivity.newIntent(mContext), REQUEST_CODE_SCAN));
        // 搜索
        mSearchTextView.setOnClickListener(v -> checkWatchGuardianship());
        // 申请监护
        mApplyButton.setOnClickListener(v -> bindWatchGuardianship());
        // 顶部按钮
        mBottomButton.setOnClickListener(v -> {
            String s = mBottomButton.getText().toString();
            if ("申请绑定".equals(s)) {
                // 申请绑定
                bindWatch();
            }
        });

    }

    @Override
    protected void processLogic() {
        setTitleLeftBack();
        setTitleName("申请监护");
        jumpType = getIntent().getIntExtra(JUMP_TYPE, 1);
        list = getIntent().getStringArrayExtra(STRINGS);
        if (jumpType == 1) {
            linearLayout1.setVisibility(View.VISIBLE);
        } else {
            linearLayout3.setVisibility(View.VISIBLE);
            if (list != null && list.length > 0)
                mJhDeviceNumberTextView.setText(String.format("监护设备号：%s", list[0]));
            if (list != null && list.length > 1)
                mJhDevicePhoneTextView.setText(String.format("被监护号码：%s", list[1]));
        }

    }

    @Override
    public void onBackPressed() {
        if (isBackEHomeActivity) {
            // 销毁上一界面
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
        } else {
            super.onBackPressed();
        }
    }

    /**
     * 查询是否可以绑定监护设备
     */
    private void checkWatchGuardianship() {
        Map<String, String> map = new HashMap<>();
        User loginUserDoLogin = ZhaoLinApplication.getInstance().getLoginUserDoLogin(this);
        if (loginUserDoLogin == null) return;
        // 用户id
        map.put(ZLConstants.Params.UID, loginUserDoLogin.getData().getId());
        String text = mInputDeviceNumberEditText.getText().toString();
        if (TextUtils.isEmpty(text)) {
            ToastManager.showShortToast(mContext, "请输入手机号或设备号");
            return;
        }
        // 手机或设备号
        map.put(ZLConstants.Params.KEYWORD, text);
        RequestManager.createRequest(ZLURLConstants.CHECK_WATCH_GUARDIANSHIP_URL, map, new OnMyActivityRequestListener<CheckWatchExistBean>(this) {
            @Override
            public void onSuccess(CheckWatchExistBean bean) {
                setLinearLayoutVisible(View.VISIBLE, View.GONE, View.VISIBLE, View.GONE, View.GONE, View.GONE);
                mJhDeviceNumberTextView.setText(String.format("监护设备号：%s", bean.getData().getImei()));
                mJhDevicePhoneTextView.setText(String.format("被监护号码：%s", bean.getData().getPhone()));
            }

            @Override
            public void onFailed(CheckWatchExistBean bean, boolean isError, String code, String message) {
                if ("400".equals(code)) {
                    // 设备不存在
                    setLinearLayoutVisible(View.VISIBLE, View.GONE, View.GONE, View.VISIBLE, View.GONE, View.GONE);
                    setTipTextAndButtonState(R.mipmap.ic_exclamation_point, R.color.orange_ff725c, "此设备/手机号不存在", "", View.GONE);
                } else if ("401".equals(code)) {
                    // 此设备未绑定，可申请绑定
                    setLinearLayoutVisible(View.VISIBLE, View.GONE, View.GONE, View.VISIBLE, View.VISIBLE, View.VISIBLE);
                    setTipTextAndButtonState(R.drawable.shape_white, R.color.color_82ce2e, "该设备号尚未绑定，可绑定设备", "申请绑定", View.VISIBLE);
                }
            }
        });
    }

    private void setLinearLayoutVisible(int... visible) {
        if (visible.length > 0)
            linearLayout1.setVisibility(visible[0]);
        if (visible.length > 1)
            linearLayout2.setVisibility(visible[1]);
        if (visible.length > 2)
            linearLayout3.setVisibility(visible[2]);
        if (visible.length > 3)
            mTipTextView.setVisibility(visible[3]);
        if (visible.length > 4)
            mLineView.setVisibility(visible[4]);
        if (visible.length > 5)
            mBottomButton.setVisibility(visible[5]);
    }


    /**
     * @param resId         提示图片
     * @param textColor     提示文本颜色
     * @param text          提示文本
     * @param buttonText    按钮文本内容
     * @param buttonVisible 按钮显示和隐藏
     */
    private void setTipTextAndButtonState(int resId, int textColor, String text, String buttonText, int buttonVisible) {
        mTipTextView.setVisibility(View.VISIBLE);
        TextViewUtils.setImageResources(mContext, resId, Gravity.LEFT, mTipTextView);
        mTipTextView.setText(text);
        mTipTextView.setTextColor(ContextCompat.getColor(mContext, textColor));
        mBottomButton.setText(buttonText);
        mBottomButton.setVisibility(buttonVisible);
    }

    /**
     * 绑定设备
     */
    private void bindWatch() {
        HashMap<String, String> stringStringHashMap = new HashMap<>();
        String deviceNumber = mInputDeviceNumberEditText.getText().toString();
        if (TextUtils.isEmpty(deviceNumber)) {
            ToastManager.showShortToast(mContext, "请输入设备编号");
            return;
        }
        // 设备号
        stringStringHashMap.put("imei", deviceNumber);
        // 用户id
        stringStringHashMap.put("uid", ZhaoLinApplication.getInstance().getLoginUser().getData().getId());
        RequestManager.createRequest(ZLURLConstants.BIND_WATCH_URL, stringStringHashMap, new OnMyActivityRequestListener<BaseBean>(this) {

            @Override
            public void onSuccess(BaseBean bean) {
                setLinearLayoutVisible(View.GONE, View.VISIBLE, View.GONE, View.GONE, View.GONE, View.VISIBLE);
                mTipImageView.setImageResource(R.mipmap.ic_succeed_icon);
                mTipTextView1.setText("申请成功");
                mTipTextView2.setText(String.format("设备号：%s", deviceNumber));
                mTipTextView1.setVisibility(View.VISIBLE);
                mTipTextView2.setVisibility(View.VISIBLE);
                isBackEHomeActivity = true;
            }

        });

    }

    /**
     * 绑定监护设备
     */
    private void bindWatchGuardianship() {
        String deviceNumber = mInputDeviceNumberEditText.getText().toString().trim();
        Map<String, String> map = new HashMap<>();
        User loginUserDoLogin = ZhaoLinApplication.getInstance().getLoginUserDoLogin(this);
        if (loginUserDoLogin == null) return;
        // 用户id
        map.put(ZLConstants.Params.UID, loginUserDoLogin.getData().getId());
        // 手机或设备号
        map.put(ZLConstants.Params.KEYWORD, jumpType == 2 ? list[0] : deviceNumber);
        String name = mJhNameEditText.getText().toString().trim();
        // 真实姓名
        map.put(ZLConstants.Params.NAME, name);
        RequestManager.createRequest(ZLURLConstants.BIND_WATCH_GUARDIANSHIP_URL, map, new OnMyActivityRequestListener<BaseBean>(this) {
            @Override
            public void onSuccess(BaseBean bean) {
                setLinearLayoutVisible(View.GONE, View.VISIBLE, View.GONE, View.GONE, View.GONE, View.GONE);
                mTipImageView.setImageResource(R.mipmap.ic_checking);
                mTipTextView1.setText("请等待对方审核通过");
                mTipTextView1.setVisibility(View.VISIBLE);
                mTipTextView2.setVisibility(View.VISIBLE);
                mTipTextView2.setText(String.format("监护设备号：%s", jumpType == 2 ? list[0] : deviceNumber));
                isBackEHomeActivity = true;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (requestCode == REQUEST_CODE_SCAN) {
                // 扫描
                String result = ScanCodeActivity.getResult(data);
                if (!TextUtils.isEmpty(result)) {
                    mInputDeviceNumberEditText.setText(result);
                    checkWatchGuardianship();
                }

            }

        }
    }
}
