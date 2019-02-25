package com.xxzlkj.huayiyanglao.util;

import android.app.Activity;
import android.app.ProgressDialog;

/**
 * 描述:
 *
 * @author zhangrq
 *         2017/9/8 15:45
 */

public abstract class OnUploadVideoAndImageListener {
    private Activity activity;
    private ProgressDialog mDialog;

    public OnUploadVideoAndImageListener(Activity activity) {
        this.activity = activity;
    }

    public void onStart() {
        showProgressDialog(activity);
    }

    public void onUploadSucceed(String videoUrl, String imageUrl) {
        onSucceed(videoUrl, imageUrl);
        onEnd();
    }


    public void onUploadFailed(int errorCode, String errorMsg) {
        onFailed(errorCode, errorMsg);
        onEnd();
    }

    public void onUploadProgress(int progress) {
        // 设置Dialog进度
        setDialogProgress(progress);
    }

    public abstract void onSucceed(String videoUrl, String imageUrl);

    public abstract void onFailed(int errorCode, String errorMsg);


    public void onEnd() {
        hideProgressDialog();
    }

    private void showProgressDialog(Activity activity) {
        mDialog = new ProgressDialog(activity);
        // 设置进度条风格，风格为圆形，旋转的
        mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        // 设置ProgressDialog 的进度条是否不明确
        mDialog.setIndeterminate(false);
        mDialog.setMax(100);
        // 设置ProgressDialog 是否可以按退回按键取消
        mDialog.setCancelable(false);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setTitle("视频上传中");
        mDialog.show();

    }

    private void hideProgressDialog() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    private void setDialogProgress(int progress) {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.setProgress(progress);
        }
    }
}
