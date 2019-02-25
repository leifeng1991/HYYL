package com.xxzlkj.huayiyanglao.util;

import android.os.CountDownTimer;
import android.text.TextUtils;

import com.xxzlkj.huayiyanglao.config.ZLURLConstants;
import com.xxzlkj.huayiyanglao.weight.ShapeButton;
import com.xxzlkj.zhaolinshare.base.base.BaseActivity;
import com.xxzlkj.zhaolinshare.base.model.BaseBean;
import com.xxzlkj.zhaolinshare.base.net.OnMyActivityRequestListener;
import com.xxzlkj.zhaolinshare.base.net.RequestManager;
import com.xxzlkj.zhaolinshare.base.util.ToastManager;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 获取 验证码的工具类
 *
 * @author zhangrq
 */
public class VerCodeUtils {
    /**
     * 获取验证码
     *
     * @param photoNumber 手机号
     */
    public static void getVerificationCode(BaseActivity activity,
                                           String photoNumber, final ShapeButton verCodeBtn) {
        CountDownTimer countDownTimer = new CountDownTimer(60000, 1000) {
            // 30000(30秒)倒计时，每1000(1秒)调用一下onTick();

            @Override
            public void onTick(long millisUntilFinished) {
                verCodeBtn.setEnabled(false);
                verCodeBtn.setText(millisUntilFinished / 1000 + "秒后重发");
            }

            @Override
            public void onFinish() {
                verCodeBtn.setEnabled(true);
                verCodeBtn.setText("获取验证码");
            }

        };
        countDownTimer.start(); // 开始执行
        Map<String, String> requestParams = new HashMap<>();
        requestParams.put("phone", photoNumber);
//        requestParams.put("type", type);
        RequestManager.createRequest(ZLURLConstants.GET_VER_CODE_URL, requestParams, new OnMyActivityRequestListener<BaseBean>(activity) {

            @Override
            public void onSuccess(BaseBean bean) {
                ToastManager.showShortToast(getContext(), "验证码已发送，请注意查收");
            }
        });
    }

    /**
     * 获取语音验证码
     *
     * @param photoNumber 手机号
     * @param type        1 注册；2 登录
     */
    public static void getVoiceVerificationCode(BaseActivity activity, String photoNumber, String type) {
        Map<String, String> requestParams = new HashMap<>();
        requestParams.put("phone", photoNumber);
        requestParams.put("type", type);
        RequestManager.createRequest(ZLURLConstants.GET_VOICE_VER_CODE_URL, requestParams, new OnMyActivityRequestListener<BaseBean>(activity) {

            @Override
            public void onSuccess(BaseBean bean) {
                ToastManager.showShortToast(getContext(), "语音验证码已发送，请注意查收");
            }
        });
    }

    /**
     * 短信内容解析成6位验证码
     */
    public static String parseCode(String message) {
        if (!TextUtils.isEmpty(message)) {
            Pattern pattern = Pattern.compile("[0-9]{6}");
            //↑包含六位数字或字母的正则表达式
            Matcher matcher = pattern.matcher(message);
            if (matcher.find()) {
                return matcher.group();
            }
        }
        return "";
    }
}
