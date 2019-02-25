package com.xxzlkj.huayiyanglao.fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xxzlkj.huayiyanglao.R;
import com.xxzlkj.huayiyanglao.activity.equipment.BindIdCardActivity;
import com.xxzlkj.huayiyanglao.activity.equipment.BindIdCardStateActivity;
import com.xxzlkj.huayiyanglao.app.ZhaoLinApplication;
import com.xxzlkj.huayiyanglao.config.ZLConstants;
import com.xxzlkj.huayiyanglao.config.ZLURLConstants;
import com.xxzlkj.huayiyanglao.model.CheckWatchExistBean;
import com.xxzlkj.huayiyanglao.model.TiJianWatchBean;
import com.xxzlkj.huayiyanglao.model.UserCardNoStateBean;
import com.xxzlkj.zhaolinshare.base.base.BaseActivity;
import com.xxzlkj.zhaolinshare.base.base.ViewPageFragment;
import com.xxzlkj.zhaolinshare.base.model.User;
import com.xxzlkj.zhaolinshare.base.net.OnMyActivityRequestListener;
import com.xxzlkj.zhaolinshare.base.net.RequestManager;
import com.xxzlkj.zhaolinshare.base.util.NumberFormatUtils;

import java.util.HashMap;


/**
 * 描述：健康一体机
 *
 * @author zhangrq
 *         2017/9/2 10:58
 */
public class HeathAllInOneDataFragment extends ViewPageFragment {
    private View mTiZhongLineView, mGaoYaLineView, mDiYaLineView, mXinLvLineView, mXueTangLineView, mDanGuChunLineView;
    private TextView mTipTextView, mHasArrowTipTextView, mShenGaoTextView, mTiZhongTextView,
            mTiWenTextView, mGaoYaTextView, mDiYaTextView, mXueYangTextView, mXueTangTextView,
            mNiaoSuanTextView, mXinLvTextView, mXinDianTextView, mZhiFangTextView, mYaoTunTextView, mDanGuChunTextView;
    private UserCardNoStateBean.DataBean data;

    public static HeathAllInOneDataFragment newInstance() {
        return new HeathAllInOneDataFragment();
    }

    @Override
    public View loadViewLayout(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_health_all_in_one_data, container, false);
    }

    @Override
    protected void findViewById() {
        mTipTextView = getView(R.id.id_tip);// 提示语
        mHasArrowTipTextView = getView(R.id.id_has_arrow_tip);// 带右侧箭头提示语
        mShenGaoTextView = getView(R.id.id_shen_gao);// 身高
        mTiZhongLineView = getView(R.id.id_ti_zhong_line);// 体重左侧线
        mTiZhongTextView = getView(R.id.id_ti_zhong);// 体重
        mTiWenTextView = getView(R.id.id_ti_wen);// 体温
        mGaoYaLineView = getView(R.id.id_gao_ya_line);// 高压左侧线
        mGaoYaTextView = getView(R.id.id_gao_ya);// 高压
        mDiYaLineView = getView(R.id.id_di_ya_line);// 低压左侧线
        mDiYaTextView = getView(R.id.id_di_ya);// 低压
        mXueYangTextView = getView(R.id.id_xue_yang);// 血养
        mXueTangLineView = getView(R.id.id_xue_tang_line);// 血糖左侧线
        mXueTangTextView = getView(R.id.id_xue_tang);// 血糖
        mNiaoSuanTextView = getView(R.id.id_niao_suan);// 尿酸
        mXinLvLineView = getView(R.id.id_xin_lv_line);// 心率左侧线
        mXinLvTextView = getView(R.id.id_xin_lv);// 心率
        mXinDianTextView = getView(R.id.id_xin_dian);// 心电
        mZhiFangTextView = getView(R.id.id_zhi_fang);// 脂肪
        mYaoTunTextView = getView(R.id.id_yao_tun);// 腰臀比
        mDanGuChunLineView = getView(R.id.id_dan_gu_chui_line);//总胆固醇左侧线
        mDanGuChunTextView = getView(R.id.id_dan_gu_chui);// 总胆固醇
    }

    @Override
    public void setListener() {
        mHasArrowTipTextView.setOnClickListener(v -> {
            String s = mHasArrowTipTextView.getText().toString();
            if ("暂未绑定身份证，绑定后可同步数据".equals(s)) {
                // 跳转到身份证认证界面
                startActivity(BindIdCardActivity.newIntent(mContext));
            } else if ("身份证正在审核中".equals(s) && data != null) {
                // 跳转到身份证正在审核中界面
                startActivity(BindIdCardStateActivity.newIntent(mContext, data.getName(), data.getCardno()));
            }
        });
    }

    @Override
    public void processLogic() {

    }

    @Override
    public void refreshOnceData() {
        checkUserWatch();

    }

    @Override
    public void onResume() {
        super.onResume();
        if (isDataInitiated)
            checkUserWatch();
    }

    /**
     * 查询是否绑定设备
     */
    private void checkUserWatch() {
        HashMap<String, String> stringStringHashMap = new HashMap<>();
        User loginUser = ZhaoLinApplication.getInstance().getLoginUser();
        if (loginUser == null) return;// 没登录，不请求
        stringStringHashMap.put("uid", loginUser.getData().getId());
        RequestManager.createRequest(ZLURLConstants.CHECK_USER_WATCH_URL, stringStringHashMap, new OnMyActivityRequestListener<CheckWatchExistBean>((BaseActivity) mActivity) {

            @Override
            public void onSuccess(CheckWatchExistBean bean) {
                getUserCardnoState(bean.getCode(), bean.getData().getPhone());
            }

            @Override
            public void onException(int exceptionCode, String exceptionMessage) {

            }

            @Override
            public void onError(String code, String message) {

            }

            @Override
            public void onFailed(CheckWatchExistBean bean, boolean isError, String code, String message) {
                getUserCardnoState(code, "201".equals(code) ? bean.getData().getPhone() : "");
            }
        });

    }

    /**
     * 认证身份证号状态
     */
    private void getUserCardnoState(String mCode, String phone) {
        User loginUser = ZhaoLinApplication.getInstance().getLoginUserDoLogin(mActivity);
        if (loginUser == null) {
            return;
        }
        HashMap<String, String> map = new HashMap<>();
        // uid	用户id
        map.put(ZLConstants.Params.UID, loginUser.getData().getId());
        RequestManager.createRequest(ZLURLConstants.USER_CARDNO_STATE_URL, map, new OnMyActivityRequestListener<UserCardNoStateBean>((BaseActivity) mActivity) {

            @Override
            public void onSuccess(UserCardNoStateBean bean) {
                data = bean.getData();
                String state = data.getState();
                if ("1".equals(state)) {
                    // 审核中
                    mTipTextView.setVisibility(View.GONE);
                    mHasArrowTipTextView.setVisibility(View.VISIBLE);
                    mHasArrowTipTextView.setText("身份证正在审核中");
                } else if ("2".equals(state)) {
                    // 审核成功
                    getTiJianKm9020();
                }

            }

            @Override
            public void onException(int exceptionCode, String exceptionMessage) {

            }

            @Override
            public void onError(String code, String message) {

            }

            @Override
            public void onFailed(boolean isError, String code, String message) {
                if ("201".equals(mCode)) {
                    // 已申请监护
                    mTipTextView.setVisibility(View.VISIBLE);
                    mHasArrowTipTextView.setVisibility(View.GONE);
                    mTipTextView.setText(String.format("被监护人手机号：%s", phone));
                } else if ("401".equals(code)) {
                    // 已申请监护（申请中）
                    mTipTextView.setVisibility(View.VISIBLE);
                    mHasArrowTipTextView.setVisibility(View.GONE);
                    mTipTextView.setText("等待对方审核通过");
                } else {
                    mTipTextView.setVisibility(View.GONE);
                    mHasArrowTipTextView.setVisibility(View.VISIBLE);
                    mHasArrowTipTextView.setText("暂未绑定身份证，绑定后可同步数据");
                }


            }
        });
    }

    /**
     * 获取体检数据（大型设备）
     */
    private void getTiJianKm9020() {
        HashMap<String, String> stringStringHashMap = new HashMap<>();
        User loginUser = ZhaoLinApplication.getInstance().getLoginUser();
        if (loginUser == null) return;// 没登录，不请求
        stringStringHashMap.put("uid", loginUser.getData().getId());
        RequestManager.createRequest(ZLURLConstants.TIJIAN_KM9020_URL, stringStringHashMap, new OnMyActivityRequestListener<TiJianWatchBean>((BaseActivity) mActivity) {

            @Override
            public void onSuccess(TiJianWatchBean bean) {
                TiJianWatchBean.DataBean data = bean.getData();
                mTipTextView.setVisibility(View.VISIBLE);
                mHasArrowTipTextView.setVisibility(View.GONE);
                mTipTextView.setText(data == null ? "暂无体检数据，快去测量吧～" : "记得定期更新数据哦~");
                if (data != null) {
                    setData(data);
                    getTiJianKm8020(data);
                }

            }

            @Override
            public void onFailed(boolean isError, String code, String message) {
                super.onFailed(isError, code, message);
                mTipTextView.setVisibility(View.VISIBLE);
                mHasArrowTipTextView.setVisibility(View.GONE);
                mTipTextView.setText("暂无体检数据，快去测量吧～");
            }
        });

    }

    /**
     * 设置数据
     */
    public void setData(TiJianWatchBean.DataBean data) {
        setTextView(mShenGaoTextView, data.getHeight(), "cm");
        setTextView(mTiZhongTextView, data.getWeight(), "kg");
        setTextView(mTiWenTextView, data.getTemperature(), "℃");
        setTextView(mGaoYaTextView, data.getSbp(), "mmHg");
        setTextView(mDiYaTextView, data.getDbp(), "mmHg");
        setTextView(mXueYangTextView, data.getBo(), "%");
        setTextView(mXueTangTextView, data.getGlu(), "mmol/L");
        setTextView(mNiaoSuanTextView, data.getPulse(), "μmol/L");
        setTextView(mXinLvTextView, data.getPulse(), "bpm");
        setTextView(mXinDianTextView, data.getEcg(), "");
        setTextView(mZhiFangTextView, data.getFat(), "%");
        setTextView(mYaoTunTextView, data.getWhr(), "%");
        setTextView(mDanGuChunTextView, data.getChol(), "mmol/L");
    }

    /**
     * @param value 数值
     * @param unit  单位
     */
    private void setTextView(TextView textView, String value, String unit) {
        if (!TextUtils.isEmpty(value) && NumberFormatUtils.toDouble(value) > 0)
            textView.setText(String.format("%s%s", value, unit));
    }


    /**
     * 获取体检数据(手表)
     */
    private void getTiJianKm8020(TiJianWatchBean.DataBean dataBean) {
        HashMap<String, String> stringStringHashMap = new HashMap<>();
        User loginUser = ZhaoLinApplication.getInstance().getLoginUser();
        if (loginUser == null) return;// 没登录，不请求
        stringStringHashMap.put("uid", loginUser.getData().getId());
        RequestManager.createRequest(ZLURLConstants.TIJIAN_KM8020_URL, stringStringHashMap, new OnMyActivityRequestListener<TiJianWatchBean>((BaseActivity) mActivity) {

            @Override
            public void onSuccess(TiJianWatchBean bean) {
                mTiZhongLineView.setVisibility(View.VISIBLE);
                mDanGuChunLineView.setVisibility(View.VISIBLE);
                mGaoYaLineView.setVisibility(NumberFormatUtils.toLong(dataBean.getSbp_addtime()) > NumberFormatUtils.toLong(bean.getData().getSbp_addtime()) ? View.VISIBLE : View.GONE);
                mDiYaLineView.setVisibility(NumberFormatUtils.toLong(dataBean.getDbp_addtime()) > NumberFormatUtils.toLong(bean.getData().getDbp_addtime()) ? View.VISIBLE : View.GONE);
                mXinLvLineView.setVisibility(NumberFormatUtils.toLong(dataBean.getPulse_addtime()) > NumberFormatUtils.toLong(bean.getData().getPulse_addtime()) ? View.VISIBLE : View.GONE);
                mXueTangLineView.setVisibility(NumberFormatUtils.toLong(dataBean.getGlu_addtime()) > NumberFormatUtils.toLong(bean.getData().getGlu_addtime()) ? View.VISIBLE : View.GONE);

            }

        });

    }
}
