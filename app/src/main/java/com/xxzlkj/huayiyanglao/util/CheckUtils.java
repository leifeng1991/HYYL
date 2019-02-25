package com.xxzlkj.huayiyanglao.util;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

import com.xxzlkj.huayiyanglao.app.ZhaoLinApplication;
import com.xxzlkj.zhaolinshare.base.util.ToastManager;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 检查内容
 *
 * @author zhangrq
 */
public class CheckUtils {
    /**
     * 正则表达式：验证手机号
     */
    public static final String REGEX_MOBILE = "1\\d{10}";
    /**
     * 正则表达式：验证纯数字
     */
    public static final String REGEX_NUM = "\\d+";
    /**
     * 正则表达式：验证身份证号
     */
    public static final String REGEX_ID_CARD = "\\d{17}(\\d|x|X)";
    /**
     * 正则表达式：验证密码
     */
    public static final String PASS_WORD = "[a-zA-Z0-9]{6,12}";

    private static Context getContext() {
        return ZhaoLinApplication.getInstance().getApplicationContext();
    }

    /**
     * 检查手机号
     */

    public static boolean checkPhotoNumber(String photoNumber) {

        if (TextUtils.isEmpty(photoNumber)) {
            ToastManager.showLongToast(getContext(), "手机号不能为空");
            return false;
        } else if (!Pattern.matches(REGEX_MOBILE, photoNumber)) {
            ToastManager.showLongToast(getContext(), "请输入正确手机号");
            return false;
        }
        return true;
    }

    /**
     * 检查注册密码
     */
    public static boolean checkPassWord(String password) {

        if (TextUtils.isEmpty(password)) {
            ToastManager.showLongToast(getContext(), "密码不能为空");
            return false;
        } else if (!Pattern.matches(PASS_WORD, password)) {
            ToastManager.showLongToast(getContext(), "密码为6-12位，数字和字母组合，不可含有符号");
            return false;
        }
        return true;
    }

    /**
     * 检查手机号和密码
     */
    public static boolean checkPhotoNumberAndPassword(String photoNumber, String password) {
        if (!checkPhotoNumber(photoNumber)) {
            // 手机号有问题
            return false;
        } else if (!checkPassWord(password)) {
            return false;
        }
        return true;
    }

    /**
     * 检查身份证号
     *
     * @param idCard 身份证号
     */
    public static boolean checkIdCard(String idCard) {
        if (TextUtils.isEmpty(idCard)) {
            ToastManager.showLongToast(getContext(), "请输入正确身份证号");
            return false;
        } else if (!Pattern.matches(REGEX_ID_CARD, idCard)) {
            ToastManager.showLongToast(getContext(), "请输入正确身份证号");
            return false;
        }
        return true;
    }

    /**
     * 检查网络数据
     */
    public static boolean checkNetData(Object data) {
        if (data == null) {
            ToastManager.showLongToast(getContext(), "获取网络数据错误，请稍后再试");
            return false;
        }
        return true;
    }

    /**
     * 检查验证码
     *
     * @param verCode 验证码
     */
    public static boolean checkInput(String verCode) {
        // 检测密码
        if (TextUtils.isEmpty(verCode)) {
            ToastManager.showLongToast(getContext(), "验证码输入错误");
            return false;
        }
        return true;
    }

    /**
     * 检查密码
     *
     * @param password 验证码
     */
    public static boolean checkInputPassword(String password) {
        // 检测密码
        if (TextUtils.isEmpty(password)) {
            ToastManager.showLongToast(getContext(), "请输入密码");
            return false;
        }
        if (password.length() < 6) {
            ToastManager.showLongToast(getContext(), "密码长度不小于六位");
            return false;
        }
        return true;
    }

    /**
     * 检查手机号、验证码
     *
     * @param photoNumber 手机号
     * @param verCode     验证码/密码
     * @param falg        true:验证码 false:密码
     */
    public static boolean checkInput(String photoNumber, String verCode, boolean falg) {
        // 检测密码
        if (!checkPhotoNumber(photoNumber)) {
            // 手机号有问题
            return false;
        } else if (falg ? !checkInput(verCode) : !checkPassWord(verCode)) {
            // 验证码/密码有问题
            return false;
        }
        return true;
    }

    /**
     * 检查手机号、验证码、邀请码
     *
     * @param photoNumber      手机号
     * @param verificationCode 验证码
     * @param password         密码
     */
    public static boolean checkInput(String photoNumber,
                                     String verificationCode, String password) {
        if (!checkInput(photoNumber, verificationCode, true)) {
            // 手机号、验证码有问题
            return false;
        } else if (!checkPassWord(password)) {
            // 密码有问题
            return false;
        }
        return true;
    }

    /**
     * 检查身份证、验证码
     *
     * @param idNumber 身份证
     * @param verCode  验证码
     */
    public static boolean checkInputs(String idNumber, String verCode) {
        // 检测密码
        if (!checkIdCard(idNumber)) {
            // 身份证号有问题
            return false;
        } else if (!checkInput(verCode)) {
            // 验证码有问题
            return false;
        }
        return true;
    }

    public static boolean checkInputReason(String reason) {
        if (TextUtils.isEmpty(reason)) {
            ToastManager.showLongToast(getContext(), "请输入申请退款原因");
            return false;
        }
        return true;
    }

    /**
     * 设置值，并手机号设置高亮
     */
    public static void setTextAndCheckPhoneHighLight(TextView textView, String rawContent, OnPhoneClickListener listener) {
        if (TextUtils.isEmpty(rawContent)) {
            return;
        }
        // 加载文章内容高亮多个关键字，只高亮一个关键字去掉循环
        SpannableString spannableString = new SpannableString(rawContent);
        Pattern p = Pattern.compile(REGEX_MOBILE);
        Matcher m = p.matcher(spannableString);
        while (m.find()) {
            int start = m.start();
            int end = m.end();
            spannableString.setSpan(new PhoneClickableSpan(rawContent.substring(start, end), listener), start, end,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        textView.setText(spannableString);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setHighlightColor(Color.TRANSPARENT);
    }

    public static class PhoneClickableSpan extends ClickableSpan {

        private final String phoneNum;
        private final OnPhoneClickListener listener;

        public PhoneClickableSpan(String phoneNum, OnPhoneClickListener listener) {
            this.phoneNum = phoneNum;
            this.listener = listener;
        }

        @Override
        public void onClick(View widget) {
            if (listener != null) {
                listener.onPhoneClick(phoneNum);
            }
        }

        public void updateDrawState(TextPaint ds) {
            ds.setColor(Color.parseColor("#4976ff"));
            ds.setUnderlineText(true);
        }
    }

    public interface OnPhoneClickListener {
        void onPhoneClick(String phoneNum);
    }
}
