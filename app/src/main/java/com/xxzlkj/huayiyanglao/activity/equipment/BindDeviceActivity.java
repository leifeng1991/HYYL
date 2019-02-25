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
 * 描述:绑定设备
 *
 * @author zhangrq
 *         2017/3/21 14:48
 */
public class BindDeviceActivity extends MyBaseActivity {
    public static final int REQUEST_CODE_SCAN = 123;
    public static final int REQUEST_CODE = 500;
    private TextView mSearchTextView, mTipTextView, mTipTextView1, mTipTextView2;
    private Button mBottomButton;
    private EditText mInputDeviceNumberEditText;
    private LinearLayout linearLayout1, linearLayout2, linearLayout3;
    private ImageView mTipImageView, mScanCodeImageView;
    private String phone;
    private boolean isBackEHomeActivity;// 是否返回到智能设备首页

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, BindDeviceActivity.class);
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
        mScanCodeImageView = getView(R.id.id_scan_code);// 扫码按钮
        mSearchTextView = getView(R.id.id_search);// 搜索
        mTipTextView = getView(R.id.id_tip);// 提示
        View mLineView = getView(R.id.id_line);
        mLineView.setVisibility(View.VISIBLE);
        linearLayout2 = getView(R.id.id_layout_2);// 布局2
        mTipImageView = getView(R.id.id_tip_image);
        mTipTextView1 = getView(R.id.id_tip_text_1);
        mTipTextView2 = getView(R.id.id_tip_text_2);
        linearLayout3 = getView(R.id.id_layout_3);// 布局3
        mBottomButton = getView(R.id.id_bottom_button);// 底部按钮

    }

    @Override
    protected void setListener() {
        // 扫描
        mScanCodeImageView.setOnClickListener(v -> startActivityForResult(ScanCodeActivity.newIntent(mContext), REQUEST_CODE_SCAN));
        // 搜索
        mSearchTextView.setOnClickListener(v -> checkWatchExist());
        // 底部按钮
        mBottomButton.setOnClickListener(v -> {
            String text = mBottomButton.getText().toString();
            if ("申请绑定".equals(text)) {
                // 申请绑定
                bindWatch();
            } else if ("去申请".equals(text)) {
                // 去申请
                checkIsGuardianship();
            }
        });
    }

    @Override
    protected void processLogic() {
        setTitleLeftBack();
        setTitleName("绑定设备");
        linearLayout1.setVisibility(View.VISIBLE);

    }

    @Override
    public void onBackPressed() {
        if (isBackEHomeActivity) {
            // 带回返回值用于销毁前一个界面
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
        } else {
            super.onBackPressed();
        }
    }

    /**
     * 查询是否绑定设备
     */
    private void checkWatchExist() {
        HashMap<String, String> stringStringHashMap = new HashMap<>();
        String deviceNumber = mInputDeviceNumberEditText.getText().toString();
        if (TextUtils.isEmpty(deviceNumber)) {
            ToastManager.showShortToast(mContext, "请输入设备编号");
            return;
        }
        // 设备号
        stringStringHashMap.put("imei", deviceNumber);
        RequestManager.createRequest(ZLURLConstants.CHECK_WATCH_EXIST_URL, stringStringHashMap, new OnMyActivityRequestListener<CheckWatchExistBean>(this) {

            @Override
            public void onSuccess(CheckWatchExistBean bean) {
                setTipTextAndButtonByCode(bean.getCode());
            }

            @Override
            public void onFailed(CheckWatchExistBean bean, boolean isError, String code, String message) {
                setTipTextAndButtonByCode(code);
                if ("401".equals(code))
                    phone = bean.getData().getPhone();
            }

        });

    }

    /**
     * 设置提示文本和按钮文字以及按钮显示和隐藏
     *
     * @param code 返回码
     */
    private void setTipTextAndButtonByCode(String code) {
        if ("200".equals(code)) {
            // 此设备未绑定，可申请绑定
            setTipTextAndButtonState(R.drawable.shape_white, R.color.color_82ce2e, "此设备未绑定，可申请绑定", "申请绑定", View.VISIBLE);
        } else if ("400".equals(code)) {
            // 此设备不存在
            setTipTextAndButtonState(R.mipmap.ic_exclamation_point, R.color.orange_ff725c, "此设备不存在", "", View.GONE);
        } else if ("401".equals(code)) {
            // 该设备已绑定，可申请成为该设备的监护号
            setTipTextAndButtonState(R.drawable.shape_white, R.color.color_82ce2e, "该设备已绑定，可申请成为该设备的监护号", "去申请", View.VISIBLE);
        }

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
                linearLayout1.setVisibility(View.GONE);
                linearLayout2.setVisibility(View.VISIBLE);
                linearLayout3.setVisibility(View.GONE);
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
     * 查询自己有没有监护人
     */
    private void checkIsGuardianship() {
        Map<String, String> map = new HashMap<>();
        User loginUserDoLogin = ZhaoLinApplication.getInstance().getLoginUserDoLogin(this);
        if (loginUserDoLogin == null) return;
        // 用户id
        map.put(ZLConstants.Params.UID, loginUserDoLogin.getData().getId());
        RequestManager.createRequest(ZLURLConstants.CHECK_IS_GUARDIANSHIP_URL, map, new OnMyActivityRequestListener<BaseBean>(this) {
            @Override
            public void onSuccess(BaseBean bean) {
                ToastManager.showShortToast(mContext, "该设备已绑定且您有监护人，不可对该设备继续操作");
            }

            @Override
            public void onFailed(boolean isError, String code, String message) {
                // 跳转到申请监护界面
                startActivityForResult(ApplyCustodyActivity.newIntent(mContext, 2, mInputDeviceNumberEditText.getText().toString(), phone), REQUEST_CODE);
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
                    checkWatchExist();
                }

            } else if (requestCode == REQUEST_CODE) {
                // 销毁当前界面
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }

        }

    }

}
