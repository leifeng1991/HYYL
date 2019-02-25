package com.xxzlkj.huayiyanglao.fragment;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.xxzlkj.huayiyanglao.R;
import com.xxzlkj.huayiyanglao.activity.address.HarvestAddressActivity;
import com.xxzlkj.huayiyanglao.activity.equipment.ElectronicFenceActivity;
import com.xxzlkj.huayiyanglao.activity.equipment.HealthDataActivity;
import com.xxzlkj.huayiyanglao.activity.mine.CollectionActivity;
import com.xxzlkj.huayiyanglao.activity.setting.FeedBackActivity;
import com.xxzlkj.huayiyanglao.activity.setting.PersonInfoActivity;
import com.xxzlkj.huayiyanglao.activity.setting.SettingActivity;
import com.xxzlkj.huayiyanglao.app.ZhaoLinApplication;
import com.xxzlkj.huayiyanglao.config.ZLURLConstants;
import com.xxzlkj.huayiyanglao.model.HomeBean;
import com.xxzlkj.shop.activity.shop.BrowsingHistoryActivity;
import com.xxzlkj.zhaolinshare.base.base.BaseActivity;
import com.xxzlkj.zhaolinshare.base.base.ReuseViewFragment;
import com.xxzlkj.zhaolinshare.base.model.User;
import com.xxzlkj.zhaolinshare.base.net.OnMyActivityRequestListener;
import com.xxzlkj.zhaolinshare.base.net.RequestManager;
import com.xxzlkj.zhaolinshare.base.util.NumberFormatUtils;
import com.xxzlkj.zhaolinshare.base.util.PicassoUtils;
import com.xxzlkj.zhaolinshare.base.util.StringUtil;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 描述:
 *
 * @author zhangrq
 *         2017/11/22 9:31
 */
public class MineFragment extends ReuseViewFragment {
    private TextView mSettingTextView, mUserNameTextView, mMyFavoriteTextView, mBrowsingHistoryTextView,
            mAddressAdministrationTextView, mInvitationRegisterTextView, mMovementListTextView, mServiceTextView;
    private CircleImageView mUserAvatarCircleImageView;
    private LinearLayout mHealthExaminationLinearLayout;
    private TextView tv_weight, tv_cholesterol, tv_high_pressure, tv_blood_glucose, tv_low_pressure,
            tv_heart_rate;
    private ImageView iv_weight, iv_cholesterol, iv_high_pressure, iv_blood_glucose, iv_low_pressure, iv_heart_rate;
    private RoundCornerProgressBar pb_weight, pb_cholesterol, pb_high_pressure, pb_blood_glucose, pb_low_pressure, pb_heart_rate;

    @Override
    public View loadViewLayout(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_mine, container, false);
    }

    @Override
    protected void findViewById() {
        mSettingTextView = getView(R.id.id_setting);// 设置
        mUserAvatarCircleImageView = getView(R.id.id_user_avatar);// 用户头像
        mUserNameTextView = getView(R.id.id_user_name);// 用户名
        mHealthExaminationLinearLayout = getView(R.id.id_health_examination);// 体检记录
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

        mMyFavoriteTextView = getView(R.id.id_my_favorite);// 我的收藏
        mBrowsingHistoryTextView = getView(R.id.id_browsing_history);// 浏览记录
        mAddressAdministrationTextView = getView(R.id.id_address_administration);// 地址管理
        mInvitationRegisterTextView = getView(R.id.id_invitation_register);// 电子围栏
        mMovementListTextView = getView(R.id.id_movement_list);// 意见反馈
        mServiceTextView = getView(R.id.id_service);// 华颐客服
    }

    @Override
    public void setListener() {
        mSettingTextView.setOnClickListener(this);
        mUserNameTextView.setOnClickListener(this);
        mUserAvatarCircleImageView.setOnClickListener(this);
        mHealthExaminationLinearLayout.setOnClickListener(this);
        mMyFavoriteTextView.setOnClickListener(this);
        mBrowsingHistoryTextView.setOnClickListener(this);
        mAddressAdministrationTextView.setOnClickListener(this);
        mInvitationRegisterTextView.setOnClickListener(this);
        mMovementListTextView.setOnClickListener(this);
        mServiceTextView.setOnClickListener(this);
    }

    @Override
    public void processLogic() {

    }

    @Override
    public void onResume() {
        super.onResume();
        // 获取数据
        User loginUser = ZhaoLinApplication.getInstance().getLoginUser();
        if (loginUser == null) {
            // 没登录
            mUserAvatarCircleImageView.setImageResource(R.mipmap.ic_default_avatar);
            mUserNameTextView.setText("点击登录");
            // 获取进度数据并设置(默认值)
            setProgressData(1, new HomeBean.DataBean("0", "0", "0", "0", "0", "0",
                    "0.5", "0", "0.5", "0", "0.5", "0", "0.5", "0", "0.5", "0", "0.5", "0"));
        } else {
            // 用户头像地址
            String simg = loginUser.getData().getSimg();
            if (TextUtils.isEmpty(simg)) {
                // 头像为空
                mUserAvatarCircleImageView.setImageResource(R.mipmap.ic_default_avatar);
            } else {
                PicassoUtils.setImageBig(mContext, simg, mUserAvatarCircleImageView);
            }
            // 用户名
            String username = loginUser.getData().getUsername();
            mUserNameTextView.setText(TextUtils.isEmpty(username) ? "华颐用户" : username);
            // 获取进度数据并设置
            getNetDataByProgress();
        }

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        User loginUserDoLogin = ZhaoLinApplication.getInstance().getLoginUserDoLogin(getActivity());
        if (loginUserDoLogin == null)
            return;
        switch (v.getId()) {
            case R.id.id_setting:// 设置
                if (ZhaoLinApplication.getInstance().getLoginUserDoLogin(mActivity) != null)
                    startActivity(SettingActivity.newIntent(mContext));
                break;
            case R.id.id_user_avatar:// 用户头像
            case R.id.id_user_name:// 用户名
                if (ZhaoLinApplication.getInstance().getLoginUserDoLogin(mActivity) != null) {
                    startActivity(new Intent(mContext, PersonInfoActivity.class));
                }
                break;
            case R.id.id_health_examination:// 体检记录
                if (ZhaoLinApplication.getInstance().getLoginUserDoLogin(mActivity) != null)
                    startActivity(HealthDataActivity.newIntent(mContext));
                break;
            case R.id.id_my_favorite:// 我的收藏
                if (ZhaoLinApplication.getInstance().getLoginUserDoLogin(mActivity) != null)
                    startActivity(new Intent(mContext, CollectionActivity.class));
                break;
            case R.id.id_browsing_history:// 浏览记录
                if (ZhaoLinApplication.getInstance().getLoginUserDoLogin(mActivity) != null)
                    startActivity(new Intent(mContext, BrowsingHistoryActivity.class));
                break;
            case R.id.id_address_administration:// 地址管理
                if (ZhaoLinApplication.getInstance().getLoginUserDoLogin(mActivity) != null)
                    mActivity.startActivity(HarvestAddressActivity.newIntent(mContext, ""));
                break;
            case R.id.id_invitation_register:// 电子围栏
                if (ZhaoLinApplication.getInstance().getLoginUserDoLogin(mActivity) != null)
                    startActivity(ElectronicFenceActivity.newIntent(mContext));
                break;
            case R.id.id_movement_list:// 意见反馈
                if (ZhaoLinApplication.getInstance().getLoginUserDoLogin(mActivity) != null)
                    startActivity(FeedBackActivity.newIntent(mContext));
                break;
            case R.id.id_service:// 华颐客服
                break;
        }
    }

    private void getNetDataByProgress() {
        HashMap<String, String> stringStringHashMap = new HashMap<>();
        User loginUser = ZhaoLinApplication.getInstance().getLoginUser();
        if (loginUser == null) return;// 没登录，不请求
        stringStringHashMap.put("uid", loginUser.getData().getId());
        RequestManager.createRequest(ZLURLConstants.YL_GET_TIJIAN_DATA_URL, stringStringHashMap, new OnMyActivityRequestListener<HomeBean>((BaseActivity) mActivity) {

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
        tvAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                if (value > 100) {
                    value = (int) (value);
                }
                String s = StringUtil.subZeroAndDot(value + "");
                textView.setText(s);
                // 文字进度值
            }
        });
        tvAnimator.start();

        // 进度设置，先回后增
        ValueAnimator pbAnimator = ValueAnimator.ofInt(max, 0);  //定义动画
        pbAnimator.setDuration(1000);
        pbAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                // 文字进度值
                progressBar.setProgress(value);
            }
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
        pbAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                // 文字进度值
                progressBar.setProgress(value);
            }
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
                            imageView.setBackgroundDrawable(null);
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

}
