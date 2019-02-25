package com.xxzlkj.huayiyanglao.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xxzlkj.huayiyanglao.R;
import com.xxzlkj.huayiyanglao.app.ZhaoLinApplication;
import com.xxzlkj.huayiyanglao.config.ZLConstants;
import com.xxzlkj.huayiyanglao.config.ZLGlobalParams;
import com.xxzlkj.huayiyanglao.config.ZLURLConstants;
import com.xxzlkj.huayiyanglao.util.CheckUtils;
import com.xxzlkj.huayiyanglao.util.VerCodeUtils;
import com.xxzlkj.huayiyanglao.util.ZLLooper;
import com.xxzlkj.huayiyanglao.weight.ShapeButton;
import com.xxzlkj.zhaolinshare.base.base.BaseActivity;
import com.xxzlkj.zhaolinshare.base.model.User;
import com.xxzlkj.zhaolinshare.base.net.OnMyActivityRequestListener;
import com.xxzlkj.zhaolinshare.base.net.RequestManager;
import com.xxzlkj.zhaolinshare.base.util.SystemBarUtils;
import com.xxzlkj.zhaolinshare.base.util.ToastManager;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;

import cn.jpush.android.api.JPushInterface;


/**
 * 检测登录页
 *
 * @author zhangrq
 */
public class LoginActivity extends BaseActivity {
    private EditText mPhoneEditText, mYzmEditText;
    private CheckBox mCheckBox;
    private TextView mGetVoiceTextView;
    private ShapeButton mGetYzmShapeButton, mLoginRegisterShapeButton;
    private static OnLoginListener onLoginListener;
    private boolean isLoginSuccess;

    @Override
    protected void loadViewLayout() {
        SystemBarUtils.addStatusBarTranslucentFlags(this);
        SystemBarUtils.setStatusBarLightMode(this, true);
        setContentView(R.layout.activity_login);
    }

    @Override
    protected void findViewById() {
        LinearLayout mTitleLinearLayout = getView(R.id.titleBar);
        mTitleLinearLayout.setBackgroundColor(0xff54B1F8);
        SystemBarUtils.setPaddingTopStatusBarHeight(this, getView(R.id.titleBar));
        mPhoneEditText = getView(R.id.id_phone);// 手机号
        mYzmEditText = getView(R.id.id_yzm);// 手机号
        mCheckBox = getView(R.id.id_agree_login);// 用户协议单选框
        mGetVoiceTextView = getView(R.id.id_get_voice);// 语音获取验证码
        mGetYzmShapeButton = getView(R.id.id_get_yzm);// 60秒倒计时
        mLoginRegisterShapeButton = getView(R.id.id_login_register);// 登录/注册
    }

    @Override
    protected void setListener() {
        // 用户协议单选框监听事件
        mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });
        // 点击获取验证码
        mGetYzmShapeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = mPhoneEditText.getText().toString();
                if (CheckUtils.checkPhotoNumber(phone)) {
                    // 手机号正确
                    VerCodeUtils.getVerificationCode(LoginActivity.this, phone, mGetYzmShapeButton);
                }
            }
        });
        // 语音获取验证码
        mGetVoiceTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mPhone = mPhoneEditText.getText().toString();
                // 语音获取验证码
                if (CheckUtils.checkPhotoNumber(mPhone)) {
                    // 手机号正确
                    VerCodeUtils.getVoiceVerificationCode(LoginActivity.this, mPhone, "2");
                }
            }
        });
        // 登录、注册
        mLoginRegisterShapeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check();
            }
        });

    }

    @Override
    protected void processLogic() {
        setTitleLeftBack();
    }

    private void check() {
        String phone = mPhoneEditText.getText().toString();
        if (TextUtils.isEmpty(phone)) {
            ToastManager.showMsgToast(mContext, "请输入手机号");
            return;
        }
        String yzm = mYzmEditText.getText().toString();
        if (TextUtils.isEmpty(yzm)) {
            ToastManager.showMsgToast(mContext, "请输入验证码");
            return;
        }
        if (!mCheckBox.isChecked()) {
            ToastManager.showMsgToast(mContext, "请勾选用户协议");
            return;
        }
        // 登录/注册
        login(phone, yzm);
    }

    /**
     * 登录
     */
    private void login(String phone, String yzm) {
        HashMap<String, String> stringStringHashMap = new HashMap<>();
        // 手机号（唯一登陆账号）（必传）
        stringStringHashMap.put(ZLConstants.Params.PHONE, phone);
        // 极光id
        stringStringHashMap.put(ZLConstants.Params.JPUSH_ID, JPushInterface.getRegistrationID(getApplicationContext()));
        // 验证码/密码
        stringStringHashMap.put(ZLConstants.Params.YZM, yzm);
        RequestManager.createRequest(ZLURLConstants.REGISTER_LOGIN_URL, stringStringHashMap, new OnMyActivityRequestListener<User>(this) {
            @Override
            public void onSuccess(User bean) {
                isLoginSuccess = true;
                ZhaoLinApplication.getInstance().setSuccessLoginUser(bean);
                setResult(RESULT_OK, new Intent());
                finish();
            }

        });
    }

    @Override
    public void finish() {
        if (onLoginListener != null) {
            if (isLoginSuccess)
                onLoginListener.onLoginSuccess();
            else
                onLoginListener.onLoginFailed();
            // 归原操作
            onLoginListener = null;
        }
        // 归原
        ZhaoLinApplication.getInstance().setOnLoginSuccessListener(null);
        super.finish();
    }

    interface OnLoginListener {
        /**
         * 请求登录接口成功
         */
        void onLoginSuccess();

        /**
         * 按返回按钮或者点了返回键
         */
        void onLoginFailed();
    }

    public static void setOnLoginListener(OnLoginListener onLoginListener) {
        LoginActivity.onLoginListener = onLoginListener;
    }
}
