package com.xxzlkj.huayiyanglao.activity.equipment;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.xxzlkj.huayiyanglao.R;
import com.xxzlkj.huayiyanglao.app.ZhaoLinApplication;
import com.xxzlkj.huayiyanglao.config.ZLConstants;
import com.xxzlkj.huayiyanglao.config.ZLURLConstants;
import com.xxzlkj.huayiyanglao.util.ZLDialogUtil;
import com.xxzlkj.zhaolinshare.base.base.MyBaseActivity;
import com.xxzlkj.zhaolinshare.base.model.BaseBean;
import com.xxzlkj.zhaolinshare.base.model.User;
import com.xxzlkj.zhaolinshare.base.net.OnMyActivityRequestListener;
import com.xxzlkj.zhaolinshare.base.net.RequestManager;
import com.xxzlkj.zhaolinshare.base.util.PermissionHelper;
import com.xxzlkj.zhaolinshare.base.util.PicassoUtils;
import com.xxzlkj.zhaolinshare.base.util.ToastManager;
import com.xxzlkj.zhaolinshare.base.util.UpLoadImageUtils;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import me.nereo.multi_image_selector.MultiImageSelector;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;


/**
 * 描述:绑定身份证
 *
 * @author zhangrq
 *         2017/7/12 09:48
 */
public class BindIdCardActivity extends MyBaseActivity {
    private static final int REQUEST_IMAGE = 789;
    private ImageView iv_upload_image, iv_hint_upload_ok;
    private Button btn_summit;
    private EditText et_id_card, et_name;
    private TextView tv_hint_upload;
    private PermissionHelper mHelper;
    private String idCardSimgUrl;

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_bind_id_card);
    }

    @Override
    protected void findViewById() {
        iv_upload_image = getView(R.id.iv_upload_image);// 上传后的图片
        tv_hint_upload = getView(R.id.tv_hint_upload);// 上传身份证提示
        iv_hint_upload_ok = getView(R.id.iv_hint_upload_ok);// 上传成功显示
        et_name = getView(R.id.et_name);// 姓名
        et_id_card = getView(R.id.et_id_card);// 身份证
        btn_summit = getView(R.id.btn_summit);// 提交按钮
    }

    @Override
    public void setListener() {
        // 上传图片按钮
        iv_upload_image.setOnClickListener(v -> selectUpLoadImage());

        // 下一步按钮
        btn_summit.setOnClickListener(v -> {
            // 检查
            String nameStr = et_name.getText().toString().trim();// 姓名
            String idCardStr = et_id_card.getText().toString().trim();// 身份证
            if (TextUtils.isEmpty(idCardSimgUrl)) {
                ToastManager.showShortToast(mContext, "请上传身份证(正面)");
                return;
            } else if (TextUtils.isEmpty(nameStr)) {
                ToastManager.showShortToast(mContext, "请输入您的真实姓名");
                return;
            } else if (TextUtils.isEmpty(idCardStr)) {
                ToastManager.showShortToast(mContext, "请输入您的身份证号码");
                return;
            }
            // 全部OK，提交接口
            ZLDialogUtil.showRawDialogTwoButton(this, "请确认信息无误", "取消", null,
                    "提交", (dialog, which) -> submitRequest(idCardSimgUrl, nameStr, idCardStr));
        });
    }

    @Override
    public void processLogic() {
        setTitleLeftBack();
        setTitleName("绑定身份证");
    }

    /**
     * 提交请求
     */
    private void submitRequest(String idCardSimgUrl, String nameStr, String idCardStr) {
        User loginUser = ZhaoLinApplication.getInstance().getLoginUserDoLogin(this);
        if (loginUser == null) {
            return;
        }
//        uid	用户id
//        simg	身份证照片
//        cardno	身份证号
//        name	姓名
        HashMap<String, String> map = new HashMap<>();
        map.put(ZLConstants.Params.UID, loginUser.getData().getId());
        map.put(ZLConstants.Params.SIMG, idCardSimgUrl);
        map.put(ZLConstants.Params.NAME, nameStr);
        map.put(ZLConstants.Params.CARDNO, idCardStr);
        RequestManager.createRequest(ZLURLConstants.USER_CARD_NO_URL, map, new OnMyActivityRequestListener<BaseBean>(this) {

            @Override
            public void onSuccess(BaseBean bean) {
                // 跳到绑定身份证状态页面
                startActivity(BindIdCardStateActivity.newIntent(mContext, nameStr, idCardStr));
                finish();
            }
        });
    }

    /**
     * 选择并上传图片
     */
    private void selectUpLoadImage() {
//        授权处理
        mHelper = new PermissionHelper(this);
        mHelper.requestPermissions("请授予[读写][拍照]权限，否则无法拍照和读取本地图片", new PermissionHelper.PermissionListener() {
            @Override
            public void doAfterGrand(String... permission) {
                // 请求权限成功
                MultiImageSelector.create()
                        .showCamera(true) // show camera or not. true by default
                        .single() // multiode, default mode;
                        .start(BindIdCardActivity.this, REQUEST_IMAGE);
            }

            @Override
            public void doAfterDenied(String... permission) {

            }
        }, "android.permission.READ_EXTERNAL_STORAGE", android.Manifest.permission.CAMERA);
    }

    //直接把参数交给mHelper就行了
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mHelper.handleRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE && resultCode == RESULT_OK) {
            // 选择的图片
            List<String> paths = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
            String imagePath = paths.get(0);//选择的图片
            // 长传图片
            upLoadImage(imagePath);
        }
    }

    /**
     * 增加一张图片
     */
    private void upLoadImage(String imagePath) {
        ToastManager.showLongToast(mContext, "上传图片中，请等候提示成功");
        showLoadDataAnim();
        UpLoadImageUtils.upLoadSingleImageInMainThread(this, imagePath, new UpLoadImageUtils.OnUploadListener() {
            @Override
            public void onUploadSucceed(String filePath, String url) {
                // 回调在主线程
                idCardSimgUrl = url;
                // 显示图片
                PicassoUtils.setImageRaw(mContext, new File(filePath), iv_upload_image);
                // 设置成功按钮显示
                tv_hint_upload.setVisibility(View.GONE);
                iv_hint_upload_ok.setVisibility(View.VISIBLE);
                hideLoadDataAnim();
            }

            @Override
            public void onUploadFailed(int errorCode, String errorMsg) {
                // 回调在主线程
                ToastManager.showShortToast(mContext, "上传图片失败，请重新选择后再上传");
                hideLoadDataAnim();
            }
        });
    }

    public static Intent newIntent(Context context) {
        return new Intent(context, BindIdCardActivity.class);
    }
}
