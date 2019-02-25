package com.xxzlkj.huayiyanglao.activity.setting;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xxzlkj.huayiyanglao.BuildConfig;
import com.xxzlkj.huayiyanglao.R;
import com.xxzlkj.huayiyanglao.app.ZhaoLinApplication;
import com.xxzlkj.huayiyanglao.model.LoginUpBean;
import com.xxzlkj.huayiyanglao.model.UserInfo;
import com.xxzlkj.huayiyanglao.util.JumpToWebView;
import com.xxzlkj.huayiyanglao.util.ZLUpdateUtils;
import com.xxzlkj.shop.config.GlobalParams;
import com.xxzlkj.shop.config.URLConstants;
import com.xxzlkj.zhaolinshare.base.base.MyBaseActivity;
import com.xxzlkj.zhaolinshare.base.model.BaseBean;
import com.xxzlkj.zhaolinshare.base.model.User;
import com.xxzlkj.zhaolinshare.base.net.OnMyActivityRequestListener;
import com.xxzlkj.zhaolinshare.base.net.RequestManager;
import com.xxzlkj.zhaolinshare.base.util.DataCleanManager;
import com.xxzlkj.zhaolinshare.base.util.PermissionHelper;
import com.xxzlkj.zhaolinshare.base.util.PicassoUtils;
import com.xxzlkj.zhaolinshare.base.util.ToastManager;

import java.util.HashMap;

/**
 * 设置
 */
public class SettingActivity extends MyBaseActivity {

    private ImageView iv_mine_icon;
    // 当前版本号
    private TextView mCurrentVersionNameTextView;
    // 版本更新提示
    private TextView mVersionUpdataTipTextView;
    private LinearLayout mVersionUpdataLinearLayout;
    private PermissionHelper permissionHelper;

    public static Intent newIntent(Context context) {
        return new Intent(context, SettingActivity.class);
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_setting);
    }

    @Override
    protected void findViewById() {
        iv_mine_icon = getView(R.id.iv_mine_icon);
        mCurrentVersionNameTextView = getView(R.id.id_version_name);
        mVersionUpdataTipTextView = getView(R.id.id_verison_updata_tip);
        mVersionUpdataLinearLayout = getView(R.id.ll_setting_version_updata);
    }

    @Override
    protected void setListener() {
        setOnClickListener(R.id.ll_mine_info, R.id.tv_mine_clean, R.id.tv_mine_about, R.id.tv_mine_exit, R.id.id_setting_version_updata);
    }

    @Override
    protected void processLogic() {
        setTitleLeftBack();
        setTitleName("设置");

        mVersionUpdataLinearLayout.setVisibility(GlobalParams.isFromJiaYi ? View.GONE : View.VISIBLE);// 家易公司不显示更新,其余控制好了
        mCurrentVersionNameTextView.setText(BuildConfig.VERSION_NAME);
        ZLUpdateUtils.checkIsVersionUpdate(new ZLUpdateUtils.VersionUpdateListener() {
            @Override
            public void update(LoginUpBean.DataBean dataBean, boolean isForcedUpdate) {
                mVersionUpdataTipTextView.setVisibility(View.VISIBLE);
            }

            @Override
            public void notUpdate() {
                mVersionUpdataTipTextView.setVisibility(View.GONE);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        User mLoginUser = ZhaoLinApplication.getInstance().getLoginUser();
        if (mLoginUser != null) {
            getUserInfo();
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.ll_mine_info:// 用户信息
                if (ZhaoLinApplication.getInstance().getLoginUserDoLogin(this) != null) {
                    startActivity(new Intent(this, PersonInfoActivity.class));
                }
                break;
            case R.id.tv_mine_clean:// 清除缓存
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        DataCleanManager.clearAllCache(mContext);
                        iv_mine_icon.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                ToastManager.showShortToast(mContext, "清除缓存成功");
                            }
                        }, 500);
                    }
                }.start();
                break;
            case R.id.tv_mine_about:// 关于华颐
                JumpToWebView.getInstance(this).jumpToAboutZhaoLin();
                break;
            case R.id.id_setting_version_updata:// 版本更新
                permissionHelper = new PermissionHelper(this);
                ZLUpdateUtils.checkUpdateInMainThread(this, permissionHelper);
                break;
            case R.id.tv_mine_exit: // 退出登录
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("确定要退出？");
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        loginOut();
                    }
                });
                builder.show();
                break;
        }
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

                        String simg = data.getSimg();
                        if (!TextUtils.isEmpty(simg)) {
                            PicassoUtils.setImageSmall(mContext, simg, iv_mine_icon);
                        } else {// 默认头像
                            iv_mine_icon.setImageResource(R.mipmap.ic_icon_def);
                        }

                    }

                    @Override
                    public void handlerStart() {
                    }

                    @Override
                    public void handlerEnd() {
                    }
                });
    }


    /**
     * 退出登录
     */
    private void loginOut() {
        HashMap<String, String> stringStringHashMap = new HashMap<>();
        User user = ZhaoLinApplication.getInstance().getLoginUserDoLogin(this);
        if (user == null) {
            return;
        }
        stringStringHashMap.put(URLConstants.REQUEST_PARAM_UID, user.getData().getId());
        RequestManager.createRequest(URLConstants.REQUEST_LOGIN_OUT, stringStringHashMap, new OnMyActivityRequestListener<BaseBean>(this) {
            @Override
            public void onSuccess(BaseBean bean) {
                ZhaoLinApplication.getInstance().setExitLoginUser();
                finish();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] paramArrayOfInt) {
        super.onRequestPermissionsResult(requestCode, permissions, paramArrayOfInt);
        permissionHelper.handleRequestPermissionsResult(requestCode, permissions, paramArrayOfInt);
    }
}
