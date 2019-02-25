package com.xxzlkj.huayiyanglao.activity.equipment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

import com.xxzlkj.huayiyanglao.R;
import com.xxzlkj.zhaolinshare.base.base.BaseActivity;
import com.xxzlkj.zhaolinshare.base.util.PermissionHelper;
import com.xxzlkj.zhaolinshare.base.util.SystemBarUtils;
import com.xxzlkj.zhaolinshare.base.util.ToastManager;

import cn.bingoogolapple.qrcode.core.QRCodeView;


/**
 * 描述:
 *
 * @author zhangrq
 *         2017/4/18 17:44
 */
public class ScanCodeActivity extends BaseActivity {
    private static final String TAG = ScanCodeActivity.class.getSimpleName();
    public static final String RESULT = "result";

    private QRCodeView mQRCodeView;
    private boolean isOpenFlashLight;
    private PermissionHelper permissionHelper;

    public static Intent newIntent(Context context) {
        return new Intent(context, ScanCodeActivity.class);
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_scan_code);
        SystemBarUtils.setStatusBarTranslucent(this);
    }

    @Override
    protected void findViewById() {
        View titleBar = getView(R.id.titleBar);
        titleBar.setBackgroundColor(Color.TRANSPARENT);
        SystemBarUtils.setPaddingTopStatusBarHeight(this, titleBar);

        mQRCodeView = getView(R.id.zxingview);
        mQRCodeView.setDelegate(new QRCodeView.Delegate() {
            @Override
            public void onScanQRCodeSuccess(String result) {
                Intent data = new Intent();
                data.putExtra(RESULT, result);
                setResult(RESULT_OK, data);
                vibrate();
                finish();
//                Log.i(TAG, "result:" + result);
//                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
//                mQRCodeView.startSpot();
            }

            @Override
            public void onScanQRCodeOpenCameraError() {
                Log.e(TAG, "打开相机出错");
                ToastManager.showShortToast(mContext, "打开相机出错");
            }
        });
    }

    /**
     * 获取扫描后的结果值
     */
    public static String getResult(Intent data) {
        if (data == null)
            return null;
        return data.getStringExtra(RESULT);
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void processLogic() {
        setTitleLeft(R.mipmap.ic_left_arrow_white);
        setTitleName("扫一扫");
        tvTitleName.setTextColor(Color.WHITE);
        setTitleRightImage(R.mipmap.ic_light_close);
    }

    @Override
    protected void onStart() {
        super.onStart();
        permissionHelper = new PermissionHelper(this);
        permissionHelper.requestPermissions("请授予[相机]权限，否则无法扫描", new PermissionHelper.PermissionListener() {
            @Override
            public void doAfterGrand(String... permission) {
                // 请求权限成功
                mQRCodeView.startCamera();

                mQRCodeView.showScanRect();

                mQRCodeView.startSpot();// 开始扫描
            }

            @Override
            public void doAfterDenied(String... permission) {
                finish();
            }
        }, Manifest.permission.CAMERA);


    }

    @Override
    protected void onStop() {
        mQRCodeView.stopCamera();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mQRCodeView.onDestroy();
        super.onDestroy();
    }

    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }


    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_title_right:
                // 开关闭灯
                if (isOpenFlashLight = !isOpenFlashLight) {
                    // 关灯
                    mQRCodeView.closeFlashlight();
                    ivTitleRight.setImageResource(R.mipmap.ic_light_close);
                } else {
                    mQRCodeView.openFlashlight();
                    ivTitleRight.setImageResource(R.mipmap.ic_light_open);
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionHelper.handleRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
