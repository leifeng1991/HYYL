package com.xxzlkj.huayiyanglao.activity.setting;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.xxzlkj.huayiyanglao.R;
import com.xxzlkj.huayiyanglao.app.ZhaoLinApplication;
import com.xxzlkj.huayiyanglao.config.ZLConstants;
import com.xxzlkj.huayiyanglao.model.UserInfo;
import com.xxzlkj.shop.config.URLConstants;
import com.xxzlkj.shop.weight.CustomPopupWindow;
import com.xxzlkj.zhaolinshare.base.base.BaseActivity;
import com.xxzlkj.zhaolinshare.base.model.BaseBean;
import com.xxzlkj.zhaolinshare.base.model.User;
import com.xxzlkj.zhaolinshare.base.net.OnMyActivityRequestListener;
import com.xxzlkj.zhaolinshare.base.net.RequestManager;
import com.xxzlkj.zhaolinshare.base.util.DateUtils;
import com.xxzlkj.zhaolinshare.base.util.LogUtil;
import com.xxzlkj.zhaolinshare.base.util.NumberFormatUtils;
import com.xxzlkj.zhaolinshare.base.util.PermissionHelper;
import com.xxzlkj.zhaolinshare.base.util.PicassoUtils;
import com.xxzlkj.zhaolinshare.base.util.SystemBarUtils;
import com.xxzlkj.zhaolinshare.base.util.ToastManager;
import com.xxzlkj.zhaolinshare.base.util.UpLoadImageUtils;

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import me.nereo.multi_image_selector.MultiImageSelector;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;


/**
 * 个人资料
 */
public class PersonInfoActivity extends BaseActivity implements TextWatcher {
    private CircleImageView mUserIcon;
    private EditText mUserNick;
    private EditText mUserIntroduction;
    private RelativeLayout mSexRelativeLayout;
    private RelativeLayout mBirthdayRelativeLayout;
    private TextView mUserIdcard;
    private TextView mUserRegisterTime;
    private TextView mUserSex;
    private TextView mUserBirthday;
    private String mSimg;
    private String mUserName = "";
    private String mSex = "";
    private String mBirthday = "";
    private String mDesc = "";
    //是否需要修改
    private boolean isUpdata = false;
    //编辑框是否变化
    private boolean isEditTextChange = false;
    private List<String> sexLists;
    //编辑框的数量
    private int numEditText = 0;
    private List<String> imageList;
    //隐身
    private String mHide;
    // switch是否第一次设置
    private boolean isFirstSwitch = false;
    private PermissionHelper mHelper;

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_person_info);
        // 解决使用Android-Picker控件时状态栏不变暗
        SystemBarUtils.setStatusBarTranslucent(this);
        SystemBarUtils.setStatusBarLightMode(this, true);
        SystemBarUtils.setPaddingTopStatusBarHeight(this, getView(R.id.id_person_info));
    }

    @Override
    protected void findViewById() {
        mUserIcon = getView(R.id.id_pi_usericon);
        mUserNick = getView(R.id.id_pi_nick);
        mUserIntroduction = getView(R.id.id_pi_introduction);
        mSexRelativeLayout = getView(R.id.id_pi_sex_layout);
        mBirthdayRelativeLayout = getView(R.id.id_pi_birthday_layout);
        mUserIdcard = getView(R.id.id_pi_idcard);
        mUserRegisterTime = getView(R.id.id_pi_register_time);
        mUserSex = getView(R.id.id_pi_sex_content);
        mUserBirthday = getView(R.id.id_pi_birthday_content);
    }

    @Override
    protected void setListener() {
        mUserNick.addTextChangedListener(this);
        mSexRelativeLayout.setOnClickListener(this);
        mBirthdayRelativeLayout.setOnClickListener(this);
        mUserIcon.setOnClickListener(this);
    }

    @Override
    protected void processLogic() {
        setTitleLeftBack();
        setTitleName("个人资料");
        setTitleRightText("保存");

        getUserInfo();
    }

    @Override
    public void onTitleRightClick(View view) {
        super.onTitleRightClick(view);
        //保存
        if (imageList != null && imageList.size() > 0) {
            uploadImages();
        } else {
            checkData();
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.id_pi_sex_layout://性别选择
                choiceSex();
                break;
            case R.id.id_pi_birthday_layout://出生日期选择
                choiceBirthday();
                break;
            case R.id.id_pi_usericon://更改头像
                selectImage();
                break;
        }
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
                        .start(PersonInfoActivity.this, ZLConstants.Integers.REQUEST_IMAGE);
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
    public void onBackPressed() {
        if (isUpdata) {
            showDialog();
        } else {
            finish();
        }

    }

    /**
     * 选择性别
     */
    private void choiceSex() {
        if (sexLists == null) {
            sexLists = new ArrayList<>();
            sexLists.add("男");
            sexLists.add("女");
        }
        CustomPopupWindow popupWindow = new CustomPopupWindow(this, "请选择性别", new CustomPopupWindow.OnMyClickListener() {
            @Override
            public void sureClickListener(String item) {
                isUpdata = true;
                mUserSex.setText(item);
            }

            @Override
            public void cancelClickListener() {

            }
        }, sexLists);
        popupWindow.showAtLocationBottom(this, R.id.id_person_info);
    }

    /**
     * 选择生日
     */
    private void choiceBirthday() {
        Calendar endCalendar = Calendar.getInstance();
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.set(1900, 1, 1);
        endCalendar.setTimeInMillis(System.currentTimeMillis());
        TimePickerView pvTime = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                LogUtil.e("date", date.getTime() + "");
                isUpdata = true;
                mUserBirthday.setText(DateUtils.getYearMonthDay(date.getTime(), "yyyy-MM-dd"));
            }
        }).setTitleText("请选择时间")
                .setTitleSize(15)
                .setTitleColor(0xffababab)
                .setBgColor(0xffc6c9cf)
                .setRangDate(startCalendar, endCalendar)
                .setTitleBgColor(0xfff5f5f5)
                .setType(TimePickerView.Type.YEAR_MONTH_DAY).build();
        pvTime.setDate(Calendar.getInstance());//注：根据需求来决定是否使用该方法（一般是精确到秒的情况），此项可以在弹出选择器的时候重新设置当前时间，避免在初始化之后由于时间已经设定，导致选中时间与当前时间不匹配的问题。
        pvTime.show();
    }

    /**
     * 提示框
     */
    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("个人资料已修改，是否保存？");
        builder.setNegativeButton("不保存", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });
        builder.setPositiveButton("保存", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (imageList != null && imageList.size() > 0) {
                    uploadImages();
                } else {
                    checkData();
                }
            }
        });
        builder.show();
    }

    /**
     * 个人资料
     */
    private void getUserInfo() {
        HashMap<String, String> map = new HashMap<>();
        User user = ZhaoLinApplication.getInstance().getLoginUser();
        if (user == null) {
            return;
        }
        map.put(URLConstants.REQUEST_PARAM_ID, user.getData().getId());
        RequestManager.createRequest(URLConstants.REQUEST_USER_INFO,
                map, new OnMyActivityRequestListener<UserInfo>(this) {
                    @Override
                    public void onSuccess(UserInfo bean) {
                        UserInfo.DataBean data = bean.getData();
                        mSimg = data.getSimg();
                        if (TextUtils.isEmpty(mSimg)) {// 默认头像
                            mUserIcon.setImageResource(R.mipmap.ic_icon_def);
                        } else {
                            PicassoUtils.setImageSmall(PersonInfoActivity.this, mSimg, mUserIcon);
                        }
                        // 昵称
                        mUserNick.setText(data.getUsername());
                        // 将光标移至最后
                        mUserNick.setSelection(mUserNick.getText().toString().length());
                        // 描述
                        mUserIntroduction.setText(data.getDesc());
                        // Id
                        mUserIdcard.setText(data.getId());
                        // 注册日期
                        mUserRegisterTime.setText(DateUtils.getYearMonthDay(NumberFormatUtils.toLong(data.getAddtime()) * 1000, "yyyy-MM-dd"));
                        int sexType = NumberFormatUtils.toInt(data.getSex());
                        String sexStr = "";
                        switch (sexType) {
                            case 0:// 保密
                                break;
                            case 1:// 男
                                sexStr = "男";
                                break;
                            case 2:// 女
                                sexStr = "女";
                                break;
                        }
                        mUserSex.setText(sexStr);
                        // 生日
                        mBirthday = data.getBirthday();
                        if (!TextUtils.isEmpty(mBirthday)) {
                            mUserBirthday.setText(DateUtils.getYearMonthDay(NumberFormatUtils.toLong(mBirthday) * 1000, "yyyy-MM-dd"));
                        }

                    }

                });
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
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (urls != null && urls.size() > 0) {
                            mSimg = urls.get(0);
                        }
                        checkData();
                    }
                });
            }

            @Override
            public void onUploadFailed(int errorCode, String errorMsg) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastManager.showShortToast(mContext, "上传图片失败，请稍后再试");
                    }
                });
            }
        });
    }

    /**
     * 检查数据
     */
    private void checkData() {
        if (TextUtils.isEmpty(mSimg)) {
            ToastManager.showShortToast(mContext, "用户头像不能为空");
            return;
        }
        mUserName = mUserNick.getText().toString().trim();
        if (TextUtils.isEmpty(mUserName)) {
            ToastManager.showShortToast(mContext, "用户昵称不能为空");
            return;
        }
        if ("男".equals(mUserSex.getText().toString())) {
            mSex = "1";
        } else {
            mSex = "2";
        }
        if (TextUtils.isEmpty(mSex)) {
            ToastManager.showShortToast(mContext, "用户性别不能为空");
            return;
        }
        try {
            mBirthday = DateUtils.getTimeInMillis(mUserBirthday.getText().toString()) + "";
            if (TextUtils.isEmpty(mBirthday)) {
                ToastManager.showShortToast(mContext, "用户生日不能为空");
                return;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        updataPersonInfo();
    }

    /**
     * 修改个人资料
     */
    private void updataPersonInfo() {
        HashMap<String, String> map = new HashMap<>();
        User user = ZhaoLinApplication.getInstance().getLoginUser();
        if (user == null) {
            return;
        }
        map.put(URLConstants.REQUEST_PARAM_ID, user.getData().getId());
        map.put(URLConstants.REQUEST_PARAM_SIMG, mSimg);
        map.put(URLConstants.REQUEST_PARAM_USERNAME, mUserName);
        map.put(URLConstants.REQUEST_PARAM_SEX, mSex);
        map.put(URLConstants.REQUEST_PARAM_BIRTHDAY, mBirthday);
        map.put(URLConstants.REQUEST_PARAM_HIDE, mHide);
        mDesc = mUserIntroduction.getText().toString().trim();
        if (!TextUtils.isEmpty(mDesc)) {
            map.put(URLConstants.REQUEST_PARAM_DESC, mDesc);
        }

        RequestManager.createRequest(URLConstants.REQUEST_EDIT_USER,
                map, new OnMyActivityRequestListener<BaseBean>(this) {
                    @Override
                    public void onSuccess(BaseBean bean) {
                        finish();
                    }

                    @Override
                    public void onFailed(boolean isError, String code, String message) {
                        super.onFailed(isError, code, message);
                        finish();
                    }
                });
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (!isEditTextChange) {
            numEditText += 1;
            if (numEditText == 2) {
                isEditTextChange = true;
            }
        } else {
            isUpdata = true;
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

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
                    PicassoUtils.setImageRaw(mContext, new File(imageList.get(0)), mUserIcon);

                    isUpdata = true;
                }
            }
        }
    }
}
