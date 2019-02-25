package com.xxzlkj.huayiyanglao.activity;


import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.TextView;

import com.xxzlkj.huayiyanglao.R;
import com.xxzlkj.huayiyanglao.app.ZhaoLinApplication;
import com.xxzlkj.huayiyanglao.config.ZLConstants;
import com.xxzlkj.huayiyanglao.config.ZLURLConstants;
import com.xxzlkj.huayiyanglao.model.WatchGuardianshipInfoBean;
import com.xxzlkj.huayiyanglao.weight.ShapeButton;
import com.xxzlkj.zhaolinshare.base.base.MyBaseActivity;
import com.xxzlkj.zhaolinshare.base.model.BaseBean;
import com.xxzlkj.zhaolinshare.base.model.User;
import com.xxzlkj.zhaolinshare.base.net.OnMyActivityRequestListener;
import com.xxzlkj.zhaolinshare.base.net.RequestManager;
import com.xxzlkj.zhaolinshare.base.util.PicassoUtils;
import com.xxzlkj.zhaolinshare.base.util.ToastManager;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 查看监护请求
 */
public class CheckGuardianshipRequestActivity extends MyBaseActivity {
    private CircleImageView mUserAvatarCircleImageView;
    private TextView mNickTextView, mRealNameTextView, mContactWayTextView;
    private ShapeButton mRefunseShapeButton, mReceiverShapeButton;
    private String id;
    private WatchGuardianshipInfoBean.DataBean data;

    public static Intent newIntent(Context context, String id) {
        Intent intent = new Intent(context, CheckGuardianshipRequestActivity.class);
        intent.putExtra(ZLConstants.Strings.ID, id);
        return intent;
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_check_guardianship_request_info);
    }

    @Override
    protected void findViewById() {
        mUserAvatarCircleImageView = getView(R.id.id_user_avatar);// 用户头像
        mNickTextView = getView(R.id.id_nick);// 用户昵称
        mRealNameTextView = getView(R.id.id_real_name);// 用户真实姓名
        mContactWayTextView = getView(R.id.id_contact_way);// 用户联系方式
        mRefunseShapeButton = getView(R.id.id_refuse);// 拒绝
        mReceiverShapeButton = getView(R.id.id_receiver);// 同意
    }

    @Override
    protected void setListener() {
        // 拒绝
        mRefunseShapeButton.setOnClickListener(v -> auditWatchGuardianship("3"));
        // 同意
        mReceiverShapeButton.setOnClickListener(v -> auditWatchGuardianship("2"));
    }

    @Override
    protected void processLogic() {
        setTitleLeftBack();
        setTitleName("查看监护请求");
        id = getIntent().getStringExtra(ZLConstants.Strings.ID);
        getWatchGuardianshipInfo();
    }


    /**
     * 申请监护详情
     */
    private void getWatchGuardianshipInfo() {
        Map<String, String> map = new HashMap<>();
        User loginUserDoLogin = ZhaoLinApplication.getInstance().getLoginUserDoLogin(this);
        if (loginUserDoLogin == null) {
            finish();
            return;
        }
        // 用户id
        map.put(ZLConstants.Params.UID, loginUserDoLogin.getData().getId());
        // 数据id
        map.put(ZLConstants.Params.ID, id);
        RequestManager.createRequest(ZLURLConstants.WATCH_GUARDIANSHIP_INFO_URL, map, new OnMyActivityRequestListener<WatchGuardianshipInfoBean>(this) {
            @Override
            public void onSuccess(WatchGuardianshipInfoBean bean) {
                data = bean.getData();
                setData(data);
            }

            @Override
            public void onFailed(boolean isError, String code, String message) {

            }
        });
    }

    /**
     * 设置数据
     */
    public void setData(WatchGuardianshipInfoBean.DataBean data) {
        String simg = data.getSimg();
        if (TextUtils.isEmpty(simg)) {
            // 用户头像为空显示默认
            mUserAvatarCircleImageView.setImageResource(R.mipmap.default_icon);
        } else {
            // 用户头像不为空加载用户头像
            PicassoUtils.setImageSmall(mContext, simg, mUserAvatarCircleImageView);
        }
        String username = data.getUsername();
        mNickTextView.setText(TextUtils.isEmpty(username) ? "华颐用户" : username);
        mRealNameTextView.setText(String.format("真实姓名：%s", data.getName()));
        mContactWayTextView.setText(String.format("联系方式：%s", data.getPhone()));
    }

    /**
     * 申请监护
     *
     * @param state 2同意 3拒绝
     */
    private void auditWatchGuardianship(String state) {
        if (data == null) {
            ToastManager.showShortToast(mContext, "网络加载错误");
            return;
        }
        Map<String, String> map = new HashMap<>();
        User loginUserDoLogin = ZhaoLinApplication.getInstance().getLoginUserDoLogin(this);
        if (loginUserDoLogin == null) {
            finish();
            return;
        }
        // 用户id
        map.put(ZLConstants.Params.UID, loginUserDoLogin.getData().getId());
        // 数据id
        map.put(ZLConstants.Params.ID, id);
        // 2同意 3拒绝
        map.put(ZLConstants.Params.STATE, state);
        RequestManager.createRequest(ZLURLConstants.AUDIT_WATCH_GUARDIANSHIP_URL, map, new OnMyActivityRequestListener<BaseBean>(this) {
            @Override
            public void onSuccess(BaseBean bean) {
                Intent intent = new Intent();
                intent.putExtra(ZLConstants.Params.STATE, state);
                setResult(RESULT_OK, intent);
                finish();
            }

            @Override
            public void onFailed(boolean isError, String code, String message) {

            }
        });
    }

    public static String getIntentResult(Intent intent) {
        return intent.getStringExtra(ZLConstants.Params.STATE);
    }
}
