package com.xxzlkj.huayiyanglao.activity.equipment;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.xxzlkj.huayiyanglao.R;
import com.xxzlkj.huayiyanglao.app.ZhaoLinApplication;
import com.xxzlkj.huayiyanglao.config.ZLConstants;
import com.xxzlkj.huayiyanglao.config.ZLURLConstants;
import com.xxzlkj.huayiyanglao.model.SimgBean;
import com.xxzlkj.huayiyanglao.model.WatchFastListBean;
import com.xxzlkj.huayiyanglao.util.ZLDialogUtil;
import com.xxzlkj.zhaolinshare.base.base.MyBaseActivity;
import com.xxzlkj.zhaolinshare.base.model.BaseBean;
import com.xxzlkj.zhaolinshare.base.model.User;
import com.xxzlkj.zhaolinshare.base.net.OnMyActivityRequestListener;
import com.xxzlkj.zhaolinshare.base.net.RequestManager;
import com.xxzlkj.zhaolinshare.base.util.LogUtil;
import com.xxzlkj.zhaolinshare.base.util.PermissionHelper;
import com.xxzlkj.zhaolinshare.base.util.PicassoUtils;
import com.xxzlkj.zhaolinshare.base.util.ToastManager;
import com.xxzlkj.zhaolinshare.base.util.UpLoadImageUtils;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import me.nereo.multi_image_selector.MultiImageSelector;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;


/**
 * 描述:添加、编辑号码
 *
 * @author zhangrq
 *         2017/3/21 14:48
 */
public class AddEditContactActivity extends MyBaseActivity {
    public static final String IS_EDIT = "is_edit";
    public static final String TYPE = "type";
    public static final String BEAN = "bean";
    private CircleImageView mAvatarCircleImageView;
    private EditText mNameEditText, mAccountEditText;
    private TextView mAccountTipTextView;
    private Button mBottomButton;
    private boolean isEdit;// 是否是编辑
    private int type;
    private PermissionHelper mHelper;
    private List<String> imageList;
    private String mSimg;// 头像
    private WatchFastListBean.DataBean dataBean;

    /**
     * @param isEdit true:编辑 false：添加
     * @param type   1 快速拨号 2紧急联系 3监护号码
     * @param bean   isEdit为true时必传
     */
    public static Intent newIntent(Context context, boolean isEdit, int type, WatchFastListBean.DataBean bean) {
        Intent intent = new Intent(context, AddEditContactActivity.class);
        intent.putExtra(IS_EDIT, isEdit);
        intent.putExtra(TYPE, type);
        intent.putExtra(BEAN, bean);
        return intent;
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_add_edit_contact);
    }

    @Override
    protected void findViewById() {
        mAvatarCircleImageView = getView(R.id.id_avatar);// 头像
        mNameEditText = getView(R.id.id_name);// 名字
        mAccountTipTextView = getView(R.id.id_account_tip);// 账号/手机
        mAccountEditText = getView(R.id.id_account);// 账号/手机
        mBottomButton = getView(R.id.id_bottom_btn);// 底部按钮
        type = getIntent().getIntExtra(TYPE, 1);
    }

    @Override
    protected void setListener() {
        mBottomButton.setOnClickListener(v -> {
            if (type == 3) {
                // 监护号码
                addEditGuardianship();
            } else {
                if (imageList != null && imageList.size() > 0) {
                    // 上传图片
                    uploadImages();
                } else {
                    addEditWatchFast();
                }
            }

        });

        if (type == 3) {
            // 手机号监听事件
            mAccountEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    String phone = s.toString();
                    if (!isEdit && phone.length() == 11) {
                        checkAddWatchGuardianship();
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        } else {
            // 头像点击事件
            mAvatarCircleImageView.setOnClickListener(v -> selectImage());
        }

    }

    @Override
    protected void processLogic() {
        setTitleLeftBack();
        isEdit = getIntent().getBooleanExtra(IS_EDIT, false);
        setTitleName(isEdit ? "编辑信息" : "添加号码");
        mBottomButton.setText(isEdit ? "保存" : "确认添加");
        mAccountTipTextView.setText(type == 3 ? "账号" : "电话");
        mAccountEditText.setHint(type == 3 ? "请输入手机账号" : "请输入手机号码");
        if (isEdit) {
            // 编辑
            setTitleRightText("删除");
            dataBean = (WatchFastListBean.DataBean) getIntent().getSerializableExtra(BEAN);
            // 初始化
            mSimg = dataBean.getSimg();
            PicassoUtils.setImageSmall(mContext, mSimg, mAvatarCircleImageView);
            mNameEditText.setText(dataBean.getName());
            mAccountEditText.setText(dataBean.getPhone());
            // 监护人
            if (type == 3) {
                mAccountEditText.setEnabled(false);
            }

        } else {
            // 添加
            User loginUser = ZhaoLinApplication.getInstance().getLoginUser();
            if (loginUser != null && !TextUtils.isEmpty(loginUser.getData().getSimg())) {
                // 设置头像
                PicassoUtils.setImageSmall(mContext, loginUser.getData().getSimg(), mAvatarCircleImageView);
            } else {
                // 设置默认头像
                mAvatarCircleImageView.setImageResource(R.mipmap.default_icon);
            }
        }

    }

    @Override
    public void onTitleRightClick(View view) {
        super.onTitleRightClick(view);
        ZLDialogUtil.showTwoButtonDialog(this, "确认删除？", () -> {
            if (type == 3) {
                // 删除监护人
                delGuardianship();
            } else {
                //删除速拨号 和紧急通知
                delWatchFast();
            }
        });
    }

    private void selectImage() {
//        授权处理
        mHelper = new PermissionHelper(this);
        mHelper.requestPermissions("请授予[读写][拍照]权限，否则无法拍照和读取本地图片", new PermissionHelper.PermissionListener() {
            @Override
            public void doAfterGrand(String... permission) {
                // 请求权限成功
                MultiImageSelector.create()
                        .showCamera(true) // show camera or not. true by default
                        .single() // multi mode, default mode;
                        .start(AddEditContactActivity.this, ZLConstants.Integers.REQUEST_IMAGE);
            }

            @Override
            public void doAfterDenied(String... permission) {

            }
        }, "android.permission.READ_EXTERNAL_STORAGE", android.Manifest.permission.CAMERA);
    }

    /**
     * 上传图片
     */
    private void uploadImages() {
        showLoadDataAnim();
        UpLoadImageUtils.upLoadMultiImageInMainThread(this, imageList, new UpLoadImageUtils.OnMultiUploadListener() {
            @Override
            public void onUploadSucceed(final List<String> urls, final String simg) {
                // 图片都上传成功，提交
                runOnUiThread(() -> {
                    if (urls != null && urls.size() > 0) {
                        mSimg = urls.get(0);
                    }
                    addEditWatchFast();
                });
            }

            @Override
            public void onUploadFailed(int errorCode, String errorMsg) {
                runOnUiThread(() -> ToastManager.showShortToast(mContext, "上传图片失败，请稍后再试"));
            }
        });
    }

    //直接把参数交给mHelper就行了
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mHelper.handleRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     * 删除速拨号 和紧急通知
     */
    private void delWatchFast() {
        Map<String, String> map = new HashMap<>();
        User loginUserDoLogin = ZhaoLinApplication.getInstance().getLoginUserDoLogin(this);
        if (loginUserDoLogin == null) {
            finish();
            return;
        }
        // 用户id
        map.put(ZLConstants.Params.UID, loginUserDoLogin.getData().getId());
        if (dataBean == null) {
            ToastManager.showMsgToast(mContext, "网络加载错误");
            return;
        }
        // 数据id
        map.put(ZLConstants.Params.ID, dataBean.getId());
        RequestManager.createRequest(ZLURLConstants.DEL_WATCH_FAST_URL, map, new OnMyActivityRequestListener<BaseBean>(this) {
            @Override
            public void onSuccess(BaseBean bean) {
                finish();
            }
        });
    }

    /**
     * 删除监护人
     */
    private void delGuardianship() {
        Map<String, String> map = new HashMap<>();
        if (dataBean == null) {
            ToastManager.showMsgToast(mContext, "网络加载错误");
            return;
        }
        // 用户id
        map.put(ZLConstants.Params.UID, dataBean.getUid());
        // 数据id
        map.put(ZLConstants.Params.ID, dataBean.getId());
        RequestManager.createRequest(ZLURLConstants.DEL_GUARDIANSHIP_URL, map, new OnMyActivityRequestListener<BaseBean>(this) {
            @Override
            public void onSuccess(BaseBean bean) {
                finish();
            }
        });
    }

    /**
     * 添加快速拨号 和紧急通知
     */
    private void addEditWatchFast() {
        Map<String, String> map = new HashMap<>();
        User loginUserDoLogin = ZhaoLinApplication.getInstance().getLoginUserDoLogin(this);
        if (loginUserDoLogin == null) {
            finish();
            return;
        }
        String name = mNameEditText.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            ToastManager.showShortToast(mContext, "请输入姓名");
            return;
        }
        String phone = mAccountEditText.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            ToastManager.showShortToast(mContext, "请输入手机账号");
            return;
        }

        // 用户id
        map.put(ZLConstants.Params.UID, loginUserDoLogin.getData().getId());
        // 备注姓名
        map.put(ZLConstants.Params.NAME, name);
        // 1 快速拨号 2紧急联系
        map.put(ZLConstants.Params.TYPE, type + "");
        // 数据id
        if (dataBean != null)
            map.put(ZLConstants.Params.ID, dataBean.getId());
        // 手机号
        map.put(ZLConstants.Params.PHONE, phone);
        // 头像
        String simg = !TextUtils.isEmpty(mSimg) ? mSimg : loginUserDoLogin.getData().getSimg();
        if (TextUtils.isEmpty(simg)) {
            ToastManager.showShortToast(mContext, "请添加用户头像");
            return;
        }
        map.put(ZLConstants.Params.SIMG, simg);
        RequestManager.createRequest(isEdit ? ZLURLConstants.EDIT_WATCH_FAST_URL : ZLURLConstants.ADD_WATCH_FAST_URL, map, new OnMyActivityRequestListener<BaseBean>(this) {
            @Override
            public void onSuccess(BaseBean bean) {
                finish();
            }
        });
    }

    /**
     * 添加监护人/修改监护人备注
     */
    private void addEditGuardianship() {
        Map<String, String> map = new HashMap<>();
        User loginUserDoLogin = ZhaoLinApplication.getInstance().getLoginUserDoLogin(this);
        if (loginUserDoLogin == null) {
            finish();
            return;
        }
        String name = mNameEditText.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            ToastManager.showShortToast(mContext, "请输入备注姓名");
            return;
        }
        String phone = mAccountEditText.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            ToastManager.showShortToast(mContext, "请输入手机账号");
            return;
        }

        if (isEdit) {
            // 编辑
            // 用户id
            if (dataBean == null) {
                ToastManager.showMsgToast(mContext, "网络加载错误");
                return;
            }
            map.put(ZLConstants.Params.ID, dataBean.getId());
        } else {
            // 添加
            // 用户id
            map.put(ZLConstants.Params.UID, loginUserDoLogin.getData().getId());
            // 手机号
            map.put(ZLConstants.Params.PHONE, phone);
        }
        // 备注姓名
        map.put(ZLConstants.Params.NAME, name);

        RequestManager.createRequest(isEdit ? ZLURLConstants.EDIT_GUARDIANSHIP_URL : ZLURLConstants.ADD_WATCH_GUARDIANSHIP_URL, map, new OnMyActivityRequestListener<BaseBean>(this) {
            @Override
            public void onSuccess(BaseBean bean) {
                finish();
            }
        });
    }

    /**
     * 查询监护人
     */
    private void checkAddWatchGuardianship() {
        Map<String, String> map = new HashMap<>();
        String phone = mAccountEditText.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            ToastManager.showShortToast(mContext, "请输入手机账号");
            return;
        }
        // 手机号
        map.put(ZLConstants.Params.PHONE, phone);
        RequestManager.createRequest(ZLURLConstants.CHECK_ADD_WATCH_GUARDIANSHIP_URL, map, new OnMyActivityRequestListener<SimgBean>(this) {
            @Override
            public void onSuccess(SimgBean bean) {
                PicassoUtils.setImageSmall(mContext, bean.getData().getSimg(), mAvatarCircleImageView);
            }

            @Override
            public void onError(String code, String message) {
                super.onError(code, message);
                mAvatarCircleImageView.setImageResource(R.mipmap.default_icon);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ZLConstants.Integers.REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
                // Get the result list of select image paths
                imageList = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                if (imageList != null && imageList.size() > 0) {
                    LogUtil.e("uri", imageList.get(0));
                    PicassoUtils.setImageRaw(mContext, new File(imageList.get(0)), mAvatarCircleImageView);

                }
            }
        }
    }
}
