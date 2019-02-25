package com.xxzlkj.huayiyanglao.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.xxzlkj.huayiyanglao.R;
import com.xxzlkj.zhaolinshare.base.util.DialogBuilder;


/**
 * 乐行样式弹框
 *
 * @author zhangrq
 */
public class ZLDialogUtil {


    public static Dialog showRawDialog(Activity activity, String message, final OnClickConfirmListener listener) {
        return new AlertDialog.Builder(activity)
                .setTitle("提示")
                .setMessage(message)
                .setNegativeButton("取消", (dialog, which) -> {
                })
                .setPositiveButton("确定", (dialog, which) -> {
                    if (listener != null)
                        listener.onClickConfirm();
                })
                .show();
    }

    public static AlertDialog showRawDialogOneButton(Activity activity, String message, final OnClickConfirmListener listener) {
        return new AlertDialog.Builder(activity)
                .setTitle("提示")
                .setMessage(message)
                .setPositiveButton("确定", (dialog, which) -> {
                    if (listener != null)
                        listener.onClickConfirm();
                })
                .show();
    }

    public static Dialog showRawDialogTwoButton(Activity activity, String message, String buttonOneStr, DialogInterface.OnClickListener oneButtonListener, String buttonTwoStr, DialogInterface.OnClickListener twoButtonListener) {
        return new AlertDialog.Builder(activity)
                .setTitle("提示")
                .setMessage(message)
                .setNegativeButton(buttonOneStr, oneButtonListener)
                .setPositiveButton(buttonTwoStr, twoButtonListener)
                .show();
    }


    /**
     * 检查更新的Dialog
     *
     * @param isForcedUpdate 是否是强制更新
     */
    public static Dialog showUpdateDialog(Activity activity, boolean isForcedUpdate, String title, String message, final OnClickConfirmListener listener) {
        ViewGroup rootView = (ViewGroup) View.inflate(activity, R.layout.dialog_update, null);
        TextView tv_title = (TextView) rootView.findViewById(R.id.tv_title);// 标题
        TextView tv_des = (TextView) rootView.findViewById(R.id.tv_des);// 更新内容
        View rl_no_forced_update = rootView.findViewById(R.id.rl_no_forced_update);// 不强制更新布局
        TextView tv_cancel = (TextView) rootView.findViewById(R.id.tv_cancel);// 取消
        TextView tv_config = (TextView) rootView.findViewById(R.id.tv_config);// 更新
        TextView tv_forced_update = (TextView) rootView.findViewById(R.id.tv_forced_update);// 强制更新
        final DialogBuilder dialogBuilder = DialogBuilder
                .create(activity)
                .setView(rootView)
                .setWidthScale(0.77)
                .setCancelable(!isForcedUpdate)
                .setCanceledOnTouchOutside(!isForcedUpdate)
                .show();
        // 通用设置
        tv_title.setText(title);
        tv_des.setText(message);
        // 设置点击
        // 非强制更新
        tv_cancel.setOnClickListener(v -> dialogBuilder.dismiss());
        tv_config.setOnClickListener(v -> {
            if (listener != null) {
                listener.onClickConfirm();
            }
            dialogBuilder.dismiss();
        });
        // 强制更新
        tv_forced_update.setOnClickListener(v -> {
            if (listener != null) {
                tv_forced_update.setEnabled(false);
                listener.onClickConfirm();
            }
        });
        // 设置是否强制更新布局
        rl_no_forced_update.setVisibility(isForcedUpdate ? View.GONE : View.VISIBLE);
        tv_forced_update.setVisibility(isForcedUpdate ? View.VISIBLE : View.GONE);

        return dialogBuilder.getDialog();
    }


    public static Dialog showTwoButtonDialog(Activity activity, String content, OnClickConfirmListener listener) {
        ViewGroup rootView = (ViewGroup) View.inflate(activity, R.layout.dialog_two_button, null);
        TextView mContentTextView = rootView.findViewById(R.id.id_content);// 提示内容
        mContentTextView.setText(content);
        TextView mCancelButton = rootView.findViewById(R.id.id_left_button);// 取消
        TextView mSureButton = rootView.findViewById(R.id.id_right_button);// 确定

        final DialogBuilder dialogBuilder = DialogBuilder
                .create(activity)
                .setView(rootView)
                .setWidthScale(0.77)
                .show();
        // 取消
        mCancelButton.setOnClickListener(v -> dialogBuilder.dismiss());
        // 确定
        mSureButton.setOnClickListener(v -> {
            dialogBuilder.dismiss();
            if (listener != null)
                listener.onClickConfirm();
        });
        return dialogBuilder.getDialog();
    }

    /**
     * @param isOneBtn  true:一个按钮 false：两个按钮
     * @param leftText  左边按钮文本
     * @param rightText 右边按钮文本
     * @param title     提示标题
     * @param message   提示内容
     * @param listener  左右按钮点击回调
     */
    public static Dialog showTwoButtonDialog(Activity activity, boolean isOneBtn, String leftText, String rightText, String title, String message, OnClickCancelConfirmListener listener) {
        ViewGroup rootView = (ViewGroup) View.inflate(activity, R.layout.dialog_order_two_button, null);
        TextView mContentTextView = rootView.findViewById(R.id.id_title);// 提示标题
        TextView mMessageTextView = rootView.findViewById(R.id.id_message);// 提示内容
        TextView mCancelButton = rootView.findViewById(R.id.id_left_button);// 取消
        View mLineView = rootView.findViewById(R.id.id_line);
        TextView mSureButton = rootView.findViewById(R.id.id_right_button);// 确定
        mContentTextView.setText(title);
        mContentTextView.setVisibility(TextUtils.isEmpty(title) ? View.GONE : View.VISIBLE);
        mMessageTextView.setText(message);
        mMessageTextView.setVisibility(TextUtils.isEmpty(message) ? View.GONE : View.VISIBLE);
        mCancelButton.setText(leftText);
        mCancelButton.setVisibility(isOneBtn ? View.GONE : View.VISIBLE);
        mLineView.setVisibility(isOneBtn ? View.GONE : View.VISIBLE);
        mSureButton.setText(rightText);


        final DialogBuilder dialogBuilder = DialogBuilder
                .create(activity)
                .setView(rootView)
                .setWidthScale(0.77)
                .show();
        // 取消
        mCancelButton.setOnClickListener(v -> {
            dialogBuilder.dismiss();
            if (listener != null)
                listener.onLeftClick();
        });
        // 确定
        mSureButton.setOnClickListener(v -> {
            dialogBuilder.dismiss();
            if (listener != null)
                listener.onRightClick();
        });
        return dialogBuilder.getDialog();
    }


    public interface OnClickConfirmListener {
        void onClickConfirm();
    }

    public interface OnClickCancelConfirmListener {
        void onLeftClick();

        void onRightClick();
    }


}
