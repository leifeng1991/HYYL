package com.xxzlkj.huayiyanglao.activity.equipment;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xxzlkj.huayiyanglao.R;
import com.xxzlkj.huayiyanglao.app.ZhaoLinApplication;
import com.xxzlkj.huayiyanglao.config.ZLConstants;
import com.xxzlkj.huayiyanglao.config.ZLURLConstants;
import com.xxzlkj.huayiyanglao.util.ZLDialogUtil;
import com.xxzlkj.shop.weight.CustomButton;
import com.xxzlkj.zhaolinshare.base.base.MyBaseActivity;
import com.xxzlkj.zhaolinshare.base.model.BaseBean;
import com.xxzlkj.zhaolinshare.base.model.User;
import com.xxzlkj.zhaolinshare.base.net.OnMyActivityRequestListener;
import com.xxzlkj.zhaolinshare.base.net.RequestManager;

import java.util.HashMap;
import java.util.Map;


/**
 * 描述:设备管理
 *
 * @author zhangrq
 *         2017/3/21 14:48
 */
public class DeviceManageActivity extends MyBaseActivity {
    public static final int REQUEST_CODE = 500;
    private TextView mBindDeviceTextView, mApplyCustodyTextView;
    private LinearLayout mLinearLayout1, mLinearLayout2;
    private ImageView mTipImageView;
    private TextView mTipTextView1, mTipTextView2, mTipTextView3;
    private CustomButton mTipButton;

    /**
     * @param state 0：未绑定设备、未申请监护 1：已绑定设备 2：已申请监护（申请中） 3：已申请监护（申请通过）
     * @param imei  设备号（为1时传）
     * @param phone 手机号（为2、3时传）
     */
    public static Intent newIntent(Context context, int state, String imei, String phone) {
        Intent intent = new Intent(context, DeviceManageActivity.class);
        intent.putExtra(ZLConstants.Params.STATE, state);
        intent.putExtra(ZLConstants.Strings.IMEI, imei);
        intent.putExtra(ZLConstants.Strings.PHONE, phone);
        return intent;
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_device_manage);
    }

    @Override
    protected void findViewById() {
        mLinearLayout1 = getView(R.id.id_layout_1);// 0：未绑定设备、未申请监护 所展示布局
        mBindDeviceTextView = getView(R.id.id_bind_device);// 绑定设备
        mApplyCustodyTextView = getView(R.id.id_apply_custody);// 申请监护
        mLinearLayout2 = getView(R.id.id_layout_2);//  1：已绑定设备 2：已申请监护（申请中） 3：2：已申请监护（申请通过）所展示布局
        mTipImageView = getView(R.id.id_tip_image);// 图片
        mTipTextView1 = getView(R.id.id_tip_text_1);// 一级文字
        mTipTextView2 = getView(R.id.id_tip_text_2);// 二级文字
        mTipTextView3 = getView(R.id.id_tip_text_3);// 三级级文字
        mTipButton = getView(R.id.id_tip_button);// 按钮
    }

    @Override
    protected void setListener() {
        // 绑定设备
        mBindDeviceTextView.setOnClickListener(v -> {
            // 跳转到绑定设备界面
            startActivityForResult(BindDeviceActivity.newIntent(mContext), REQUEST_CODE);
        });
        // 申请监护
        mApplyCustodyTextView.setOnClickListener(v -> {
            // 跳转申请监护界面
            startActivityForResult(ApplyCustodyActivity.newIntent(mContext, 1), REQUEST_CODE);
        });
        // 按钮监听
        mTipButton.setOnClickListener(v -> {
            String buttonText = mTipButton.getText().toString();
            if ("解绑".equals(buttonText)) {
                showDialog();
            } else if ("退出监护号码".equals(buttonText)) {
                delMyGuardianship();
            }
        });
    }

    @Override
    protected void processLogic() {
        setTitleLeftBack();
        setTitleName("设备管理");
        int mState = getIntent().getIntExtra(ZLConstants.Params.STATE, 0);
        String imei = getIntent().getStringExtra(ZLConstants.Strings.IMEI);
        String phone = getIntent().getStringExtra(ZLConstants.Strings.PHONE);
        // 0：未绑定设备、未申请监护 1：已绑定设备 2：已申请监护（申请中） 3：已申请监护（申请通过）
        if (mState == 0) {
            mLinearLayout1.setVisibility(View.VISIBLE);
            // 查询自己有没有监护人
            checkIsGuardianship();
        } else {
            mLinearLayout2.setVisibility(View.VISIBLE);
            mTipImageView.setImageResource(mState == 1 ? R.mipmap.ic_device : mState == 2 ? R.mipmap.ic_checking : R.mipmap.ic_phone_device);
            mTipButton.setText(mState == 1 ? "解绑" : "退出监护号码");
            mTipButton.setVisibility(mState == 2 ? View.GONE : View.VISIBLE);
            mTipTextView1.setVisibility(mState == 2 ? View.VISIBLE : View.GONE);
            mTipTextView1.setText("请等待对方审核通过");
            mTipTextView2.setVisibility(mState == 2 ? View.VISIBLE : View.GONE);
            mTipTextView2.setText(String.format("%s：%s", TextUtils.isEmpty(imei) ? "手机号" : "监控设备号", TextUtils.isEmpty(imei) ? phone : imei));
            mTipTextView3.setText(String.format("%s", mState == 3 ? "手机号：" + phone : "设备号：" + imei));
            mTipTextView3.setVisibility(mState == 2 ? View.GONE : View.VISIBLE);
        }

    }

    private void showDialog() {
        ZLDialogUtil.showTwoButtonDialog(this, "确认解除绑定？", this::delWatch);
    }

    /**
     * 解绑设备
     */
    private void delWatch() {
        Map<String, String> map = new HashMap<>();
        User loginUserDoLogin = ZhaoLinApplication.getInstance().getLoginUserDoLogin(this);
        if (loginUserDoLogin == null) return;
        // 用户id
        map.put(ZLConstants.Params.UID, loginUserDoLogin.getData().getId());
        RequestManager.createRequest(ZLURLConstants.DEL_WATCH_URL, map, new OnMyActivityRequestListener<BaseBean>(this) {
            @Override
            public void onSuccess(BaseBean bean) {
                // 成功时显示
                mTipButton.setVisibility(View.GONE);
                mTipTextView1.setText("解绑成功");
                mTipTextView1.setVisibility(View.VISIBLE);
                mTipImageView.setImageResource(R.mipmap.ic_unbind);
                mTipTextView2.setText("您已解绑设备，并已发送相关消息给您的亲人");
                mTipTextView2.setVisibility(View.VISIBLE);
                mTipTextView3.setVisibility(View.GONE);
            }
        });
    }

    /**
     * 解绑监护人设备
     */
    private void delMyGuardianship() {
        Map<String, String> map = new HashMap<>();
        User loginUserDoLogin = ZhaoLinApplication.getInstance().getLoginUserDoLogin(this);
        if (loginUserDoLogin == null) return;
        // 用户id
        map.put(ZLConstants.Params.UID, loginUserDoLogin.getData().getId());
        RequestManager.createRequest(ZLURLConstants.DEL_MY_GUARDIANSHIP_URL, map, new OnMyActivityRequestListener<BaseBean>(this) {
            @Override
            public void onSuccess(BaseBean bean) {
                // 成功时显示
                mTipButton.setVisibility(View.GONE);
                mTipTextView1.setVisibility(View.VISIBLE);
                mTipTextView2.setVisibility(View.GONE);
                mTipTextView3.setVisibility(View.GONE);
                mTipImageView.setImageResource(R.mipmap.ic_succeed_icon);
                mTipTextView1.setText("您已退出监护号码");
            }
        });
    }

    /**
     * 查询自己有没有监护人
     */
    private void checkIsGuardianship() {
        Map<String, String> map = new HashMap<>();
        User loginUserDoLogin = ZhaoLinApplication.getInstance().getLoginUserDoLogin(this);
        if (loginUserDoLogin == null) return;
        // 用户id
        map.put(ZLConstants.Params.UID, loginUserDoLogin.getData().getId());
        RequestManager.createRequest(ZLURLConstants.CHECK_IS_GUARDIANSHIP_URL, map, new OnMyActivityRequestListener<BaseBean>(this) {
            @Override
            public void onSuccess(BaseBean bean) {
                mApplyCustodyTextView.setVisibility(View.GONE);
            }

            @Override
            public void handlerStart() {

            }

            @Override
            public void onFailed(boolean isError, String code, String message) {
                mApplyCustodyTextView.setVisibility(View.VISIBLE);
            }

            @Override
            public void handlerEnd() {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (requestCode == REQUEST_CODE) {
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        }

    }
}
