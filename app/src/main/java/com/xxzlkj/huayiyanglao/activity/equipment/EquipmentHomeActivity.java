package com.xxzlkj.huayiyanglao.activity.equipment;


import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.xxzlkj.huayiyanglao.R;
import com.xxzlkj.huayiyanglao.app.ZhaoLinApplication;
import com.xxzlkj.huayiyanglao.config.ZLURLConstants;
import com.xxzlkj.huayiyanglao.model.CheckWatchExistBean;
import com.xxzlkj.huayiyanglao.model.HomeBean;
import com.xxzlkj.huayiyanglao.model.TiJianIsLookBean;
import com.xxzlkj.zhaolinshare.base.base.MyBaseActivity;
import com.xxzlkj.zhaolinshare.base.model.User;
import com.xxzlkj.zhaolinshare.base.net.OnMyActivityRequestListener;
import com.xxzlkj.zhaolinshare.base.net.RequestManager;
import com.xxzlkj.zhaolinshare.base.util.NumberFormatUtils;
import com.xxzlkj.zhaolinshare.base.util.StringUtil;
import com.xxzlkj.zhaolinshare.base.util.ToastManager;

import java.util.HashMap;

/**
 * 智能设备首页
 *
 * @author zhangrq
 */
public class EquipmentHomeActivity extends MyBaseActivity {
    private TextView tv_dwjl, tv_bhsz, mHealthDataTextView, mDeviceManagementTextView;
    private TextView tv_weight, tv_cholesterol, tv_high_pressure, tv_blood_glucose, tv_low_pressure,
            tv_heart_rate;
    private ImageView iv_weight, iv_cholesterol, iv_high_pressure, iv_blood_glucose, iv_low_pressure, iv_heart_rate;
    private RoundCornerProgressBar pb_weight, pb_cholesterol, pb_high_pressure, pb_blood_glucose, pb_low_pressure, pb_heart_rate;

    public static Intent newIntent(Context context) {
        return new Intent(context, EquipmentHomeActivity.class);
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_equipment_home);
    }

    @Override
    protected void findViewById() {
        mHealthDataTextView = getView(R.id.id_health_data);// 健康数据
        tv_dwjl = getView(R.id.tv_dwjl);// 定位记录
        tv_bhsz = getView(R.id.tv_bhsz);// 拨号设置
        mDeviceManagementTextView = getView(R.id.id_device_management);// 设备管理

        // 文字进度值
        tv_weight = getView(R.id.tv_weight);
        tv_cholesterol = getView(R.id.tv_cholesterol);
        tv_high_pressure = getView(R.id.tv_high_pressure);
        tv_blood_glucose = getView(R.id.tv_blood_glucose);
        tv_low_pressure = getView(R.id.tv_low_pressure);
        tv_heart_rate = getView(R.id.tv_heart_rate);
        // 正常与否图片
        iv_weight = getView(R.id.iv_weight);
        iv_cholesterol = getView(R.id.iv_cholesterol);
        iv_high_pressure = getView(R.id.iv_high_pressure);
        iv_blood_glucose = getView(R.id.iv_blood_glucose);
        iv_low_pressure = getView(R.id.iv_low_pressure);
        iv_heart_rate = getView(R.id.iv_heart_rate);
        // 进度
        pb_weight = getView(R.id.pb_weight);
        pb_cholesterol = getView(R.id.pb_cholesterol);
        pb_high_pressure = getView(R.id.pb_high_pressure);
        pb_blood_glucose = getView(R.id.pb_blood_glucose);
        pb_low_pressure = getView(R.id.pb_low_pressure);
        pb_heart_rate = getView(R.id.pb_heart_rate);
    }

    @Override
    protected void setListener() {
        // 健康数据
        mHealthDataTextView.setOnClickListener(v -> {
            // 跳转到健康数据界面
            startActivity(HealthDataActivity.newIntent(mContext));
        });
        // 定位记录
        tv_dwjl.setOnClickListener(v -> checkUserWatch(true));
        // 拨号设置
        tv_bhsz.setOnClickListener(v -> startActivity(ContactSettingActivity.newIntent(mContext)));
        // 设备管理
        mDeviceManagementTextView.setOnClickListener(v -> {
            // 跳转到设备管理界面
            checkUserWatch(false);

        });
    }

    @Override
    protected void processLogic() {
        setTitleLeftBack();
        setTitleName("智能设备");
        // 判断是否有更新
        if (ZhaoLinApplication.getInstance().getLoginUserDoLogin(this) == null) {
            finish();
        } else {
            checkIsLook();
        }
    }

    /**
     * 体检数据是否查看
     */
    private void checkIsLook() {
        HashMap<String, String> stringStringHashMap = new HashMap<>();
        User loginUser = ZhaoLinApplication.getInstance().getLoginUser();
        if (loginUser == null) return;// 没登录，不请求
        stringStringHashMap.put("uid", loginUser.getData().getId());
        RequestManager.createRequest(ZLURLConstants.TIJIAN_IS_LOOK_URL, stringStringHashMap, new OnMyActivityRequestListener<TiJianIsLookBean>(this) {

            @Override
            public void onSuccess(TiJianIsLookBean bean) {
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        // 获取数据
        User loginUser = ZhaoLinApplication.getInstance().getLoginUser();
        if (loginUser == null) {
            // 没登录
            // 获取进度数据并设置(默认值)
            setProgressData(1, new HomeBean.DataBean("0", "0", "0", "0", "0", "0",
                    "0.5", "0", "0.5", "0", "0.5", "0", "0.5", "0", "0.5", "0", "0.5", "0"));
        } else {
            // 获取进度数据并设置
            getNetDataByProgress();
        }

    }

    private void getNetDataByProgress() {
        HashMap<String, String> stringStringHashMap = new HashMap<>();
        User loginUser = ZhaoLinApplication.getInstance().getLoginUser();
        if (loginUser == null) return;// 没登录，不请求
        stringStringHashMap.put("uid", loginUser.getData().getId());
        RequestManager.createRequest(ZLURLConstants.YL_GET_TIJIAN_DATA_URL, stringStringHashMap, new OnMyActivityRequestListener<HomeBean>(this) {

            @Override
            public void onSuccess(HomeBean bean) {
                int loginMedicalState = getLoginMedicalState(bean.getData());
                setProgressData(loginMedicalState, bean.getData());
            }
        });
    }

    private void setProgressData(int loginMedicalState, HomeBean.DataBean data) {
        // 体重
        startAnimation(loginMedicalState, tv_weight, data.getWeight(), iv_weight, data.getWeight_state(), pb_weight, data.getWeight_ratio());
        // 胆固醇
        startAnimation(loginMedicalState, tv_cholesterol, data.getChol(), iv_cholesterol, data.getChol_state(), pb_cholesterol, data.getChol_ratio());
        // 高压
        startAnimation(loginMedicalState, tv_high_pressure, data.getSbp(), iv_high_pressure, data.getSbp_state(), pb_high_pressure, data.getSbp_ratio());
        // 血糖
        startAnimation(loginMedicalState, tv_blood_glucose, data.getGlu(), iv_blood_glucose, data.getGlu_state(), pb_blood_glucose, data.getGlu_ratio());
        // 低压
        startAnimation(loginMedicalState, tv_low_pressure, data.getDbp(), iv_low_pressure, data.getDbp_state(), pb_low_pressure, data.getDbp_ratio());
        // 心率
        startAnimation(loginMedicalState, tv_heart_rate, data.getPulse(), iv_heart_rate, data.getPulse_state(), pb_heart_rate, data.getPulse_ratio());
    }

    /**
     * 返回1：未登录，2：未体检，3体检过了
     */
    public int getLoginMedicalState(HomeBean.DataBean data) {
        if (ZhaoLinApplication.getInstance().getLoginUser() == null)
            return 1;
        else {
            if (NumberFormatUtils.toDouble(data.getWeight()) == 0 &&
                    NumberFormatUtils.toDouble(data.getChol()) == 0 &&
                    NumberFormatUtils.toDouble(data.getSbp()) == 0 &&
                    NumberFormatUtils.toDouble(data.getGlu()) == 0 &&
                    NumberFormatUtils.toDouble(data.getDbp()) == 0 &&
                    NumberFormatUtils.toDouble(data.getPulse()) == 0) {
                return 2;
            } else
                return 3;
        }
    }

    private void startAnimation(final int loginMedicalState, final TextView textView, String currentValueStr, final ImageView imageView, final String stateStr, final RoundCornerProgressBar progressBar, String progressRatioStr) {
        final float currentValue = (float) NumberFormatUtils.toDouble(currentValueStr);// 当前值
        double progressRatio = NumberFormatUtils.toDouble(progressRatioStr);// 进度比例
        if (loginMedicalState == 1 || loginMedicalState == 2)// 没登录,或者体检了，回到50%，其余的都回到本来的位置
            progressRatio = 0.5;
        final int max = (int) progressBar.getMax();
        final int currentProgress = (int) (progressRatio * max);
        // 归原
        imageView.setVisibility(View.INVISIBLE);
        // 进度文字设置
        ValueAnimator tvAnimator = ValueAnimator.ofFloat(0, currentValue);
        tvAnimator.setDuration(2000);
        tvAnimator.addUpdateListener(animation -> {
            float value = (float) animation.getAnimatedValue();
            if (value > 100) {
                value = (int) (value);
            }
            String s = StringUtil.subZeroAndDot(value + "");
            textView.setText(s);
            // 文字进度值
        });
        tvAnimator.start();

        // 进度设置，先回后增
        ValueAnimator pbAnimator = ValueAnimator.ofInt(max, 0);  //定义动画
        pbAnimator.setDuration(1000);
        pbAnimator.addUpdateListener(animation -> {
            int value = (int) animation.getAnimatedValue();
            // 文字进度值
            progressBar.setProgress(value);
        });
        pbAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                // 回退完成，开始增加
                startAnimationEnd(loginMedicalState, currentProgress, progressBar, (int) currentValue, imageView, stateStr);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        pbAnimator.start();
    }

    private void startAnimationEnd(final int loginMedicalState, int currentProgress, final RoundCornerProgressBar progressBar, final int currentValue, final ImageView imageView, final String stateStr) {
        ValueAnimator pbAnimator = ValueAnimator.ofInt(0, currentProgress);  //定义动画
        pbAnimator.setDuration(1000);
        pbAnimator.addUpdateListener(animation -> {
            int value = (int) animation.getAnimatedValue();
            // 文字进度值
            progressBar.setProgress(value);
        });
        pbAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
//                返回1：未登录，2：未体检，3体检过了
                if (loginMedicalState == 1 || loginMedicalState == 2) {
                    // 没登录、2：未体检
                    imageView.setVisibility(View.GONE);
                } else {
                    // 没体检状态
                    final boolean isStateOK = "1".equals(stateStr);//状态 1 正常 2 不正常

                    Animation animation1 = AnimationUtils.loadAnimation(mContext, R.anim.anim_mine_state);
                    imageView.startAnimation(animation1);
                    animation1.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            imageView.setVisibility(View.VISIBLE);
                            imageView.setImageBitmap(null);
                            if (isStateOK) {
                                // 正常
                                imageView.setBackgroundResource(R.drawable.shape_mine_ok);
                            } else {
                                // 不正常
                                imageView.setBackgroundResource(R.drawable.shape_mine_warning);
                            }
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            imageView.setBackground(null);
                            imageView.setImageResource(isStateOK ? R.mipmap.ic_ok : R.mipmap.ic_warning);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        pbAnimator.start();
    }

    /**
     * 查询是否绑定设备
     *
     * @param isLocation true:跳转到定位记录界面 false:跳转到设备管理界面
     */
    private void checkUserWatch(boolean isLocation) {
        HashMap<String, String> stringStringHashMap = new HashMap<>();
        User loginUser = ZhaoLinApplication.getInstance().getLoginUser();
        if (loginUser == null) return;// 没登录，不请求
        stringStringHashMap.put("uid", loginUser.getData().getId());
        RequestManager.createRequest(ZLURLConstants.CHECK_USER_WATCH_URL, stringStringHashMap, new OnMyActivityRequestListener<CheckWatchExistBean>(this) {

            @Override
            public void onSuccess(CheckWatchExistBean bean) {
                // 已绑定设备
                CheckWatchExistBean.DataBean data = bean.getData();
                startActivity(isLocation ? LocationHistoryActivity.newIntent(mContext, 1, data.getImei(), data.getPhone()) : DeviceManageActivity.newIntent(mContext, 1, data.getImei(), data.getPhone()));
            }

            @Override
            public void onException(int exceptionCode, String exceptionMessage) {

            }

            @Override
            public void onError(String code, String message) {

            }

            @Override
            public void onFailed(CheckWatchExistBean bean, boolean isError, String code, String message) {
                if ("201".equals(code)) {
                    // 已申请监护
                    if (isLocation) {
                        // 检测被监护者是否绑定
                        checkWatchExist(bean.getData().getImei());
                    } else {
                        startActivity(DeviceManageActivity.newIntent(mContext, 3, bean.getData().getImei(), bean.getData().getPhone()));
                    }
                } else if ("401".equals(code)) {
                    // 已申请监护（申请中）
                    startActivity(isLocation ? LocationHistoryActivity.newIntent(mContext, 2, bean.getData().getImei(), bean.getData().getPhone()) : DeviceManageActivity.newIntent(mContext, 2, bean.getData().getImei(), bean.getData().getPhone()));
                } else {
                    // 未绑定设备、未申请监护
                    startActivity(isLocation ? LocationHistoryActivity.newIntent(mContext, 0, "", "") : DeviceManageActivity.newIntent(mContext, 0, "", ""));
                }

            }
        });

    }

    /**
     * 查询是否绑定设备
     */
    private void checkWatchExist(String imei) {
        HashMap<String, String> stringStringHashMap = new HashMap<>();
        // 设备号
        stringStringHashMap.put("imei", imei);
        RequestManager.createRequest(ZLURLConstants.CHECK_WATCH_EXIST_URL, stringStringHashMap, new OnMyActivityRequestListener<CheckWatchExistBean>(this) {

            @Override
            public void onSuccess(CheckWatchExistBean bean) {
                // 未绑定
                startActivity(LocationHistoryActivity.newIntent(mContext, 4, "", ""));
            }

            @Override
            public void onFailed(CheckWatchExistBean bean, boolean isError, String code, String message) {

            }

            @Override
            public void onFailed(boolean isError, String code, String message) {
                // code=401已经绑定
                startActivity(LocationHistoryActivity.newIntent(mContext, "401".equals(code) ? 3 : 4, "", ""));
            }
        });

    }
}
