package com.xxzlkj.huayiyanglao.fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xxzlkj.huayiyanglao.R;
import com.xxzlkj.huayiyanglao.activity.equipment.BindDeviceActivity;
import com.xxzlkj.huayiyanglao.app.ZhaoLinApplication;
import com.xxzlkj.huayiyanglao.config.ZLURLConstants;
import com.xxzlkj.huayiyanglao.model.CheckWatchExistBean;
import com.xxzlkj.huayiyanglao.model.TiJianWatchBean;
import com.xxzlkj.zhaolinshare.base.base.BaseActivity;
import com.xxzlkj.zhaolinshare.base.base.ViewPageFragment;
import com.xxzlkj.zhaolinshare.base.model.User;
import com.xxzlkj.zhaolinshare.base.net.OnMyActivityRequestListener;
import com.xxzlkj.zhaolinshare.base.net.RequestManager;
import com.xxzlkj.zhaolinshare.base.util.NumberFormatUtils;

import java.util.HashMap;


/**
 * 描述：智能手表
 *
 * @author zhangrq
 *         2017/9/2 10:58
 */
public class HeathWatchDataFragment extends ViewPageFragment {
    private View mGaoYaLineView, mDiYaLineView, mXinLvLineView, mXueTangLineView;
    private TextView mTipTextView, mHasArrowTipTextView, mGaoYaTextView, mDiYaTextView, mXinLvTextView, mXueTangTextView;

    public static HeathWatchDataFragment newInstance() {
        return new HeathWatchDataFragment();
    }

    @Override
    public View loadViewLayout(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_health_watch_data, container, false);
    }

    @Override
    protected void findViewById() {
        mTipTextView = getView(R.id.id_tip);// 提示语
        mHasArrowTipTextView = getView(R.id.id_has_arrow_tip);// 带右侧箭头提示语
        mGaoYaLineView = getView(R.id.id_gao_ya_line);// 高压左侧线
        mGaoYaTextView = getView(R.id.tv_gaoya);// 高压
        mDiYaLineView = getView(R.id.id_di_ya_line);// 低压左侧线
        mDiYaTextView = getView(R.id.tv_diya);// 低压
        mXinLvLineView = getView(R.id.id_xin_lv_line);// 心率左侧线
        mXinLvTextView = getView(R.id.tv_mailv);// 心率
        mXueTangLineView = getView(R.id.id_xue_tang_line);// 血糖左侧线
        mXueTangTextView = getView(R.id.tv_xuetang);// 血糖
    }

    @Override
    public void setListener() {
        mHasArrowTipTextView.setOnClickListener(v -> {
            // 跳转到绑定设备界面
            startActivity(BindDeviceActivity.newIntent(mContext));
        });
    }

    @Override
    public void processLogic() {

    }

    @Override
    public void refreshOnceData() {
    }

    @Override
    public void onResume() {
        super.onResume();
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
                // 已绑定设备
                getTiJianKm8020(bean.getCode());
            }

            @Override
            public void onException(int exceptionCode, String exceptionMessage) {

            }

            @Override
            public void onError(String code, String message) {

            }

            @Override
            public void onFailed(CheckWatchExistBean bean, boolean isError, String code, String message) {
                getTiJianKm8020(code);
                if ("201".equals(code)) {
                    // 已申请监护
                    mTipTextView.setVisibility(View.VISIBLE);
                    mHasArrowTipTextView.setVisibility(View.GONE);
                    mTipTextView.setText(String.format("被监护人手机号：%s", bean.getData().getPhone()));
                } else if ("401".equals(code)) {
                    // 已申请监护（申请中）
                    mTipTextView.setVisibility(View.VISIBLE);
                    mHasArrowTipTextView.setVisibility(View.GONE);
                    mTipTextView.setText("等待对方审核通过");
                } else {
                    // 未绑定设备、未申请监护
                    mTipTextView.setVisibility(View.GONE);
                    mHasArrowTipTextView.setVisibility(View.VISIBLE);
                    // 此状态点击提示跳转绑定
                    mHasArrowTipTextView.setText("暂未绑定设备，绑定后可同步数据");
                }

            }
        });

    }

    /**
     * 获取体检数据(手表)
     */
    private void getTiJianKm8020(String code) {
        HashMap<String, String> stringStringHashMap = new HashMap<>();
        User loginUser = ZhaoLinApplication.getInstance().getLoginUser();
        if (loginUser == null) return;// 没登录，不请求
        stringStringHashMap.put("uid", loginUser.getData().getId());
        RequestManager.createRequest(ZLURLConstants.TIJIAN_KM8020_URL, stringStringHashMap, new OnMyActivityRequestListener<TiJianWatchBean>((BaseActivity) mActivity) {

            @Override
            public void onSuccess(TiJianWatchBean bean) {
                TiJianWatchBean.DataBean data = bean.getData();
                if ("200".equals(code)) {
                    mTipTextView.setVisibility(View.VISIBLE);
                    mHasArrowTipTextView.setVisibility(View.GONE);
                    mTipTextView.setText(data == null ? "暂无体检数据，快去测量吧～" : "记得定期更新数据哦~");
                }
                if (data != null) {
                    setData(data);
                    getTiJianKm9020(data);
                }

            }

            @Override
            public void onFailed(boolean isError, String code, String message) {
                super.onFailed(isError, code, message);
            }
        });

    }

    /**
     * 设置数据
     */
    public void setData(TiJianWatchBean.DataBean data) {
        setTextView(mGaoYaTextView, data.getSbp(), "mmHg");
        setTextView(mDiYaTextView, data.getDbp(), "mmHg");
        setTextView(mXinLvTextView, data.getPulse(), "bpm");
        setTextView(mXueTangTextView, data.getGlu(), "mmol/L");
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
     * 获取体检数据（大型设备）
     */
    private void getTiJianKm9020(TiJianWatchBean.DataBean data) {
        HashMap<String, String> stringStringHashMap = new HashMap<>();
        User loginUser = ZhaoLinApplication.getInstance().getLoginUser();
        if (loginUser == null) return;// 没登录，不请求
        stringStringHashMap.put("uid", loginUser.getData().getId());
        RequestManager.createRequest(ZLURLConstants.TIJIAN_KM9020_URL, stringStringHashMap, new OnMyActivityRequestListener<TiJianWatchBean>((BaseActivity) mActivity) {

            @Override
            public void onSuccess(TiJianWatchBean bean) {
                mGaoYaLineView.setVisibility(NumberFormatUtils.toLong(data.getSbp_addtime()) > NumberFormatUtils.toLong(bean.getData().getSbp_addtime()) ? View.VISIBLE : View.GONE);
                mDiYaLineView.setVisibility(NumberFormatUtils.toLong(data.getDbp_addtime()) > NumberFormatUtils.toLong(bean.getData().getDbp_addtime()) ? View.VISIBLE : View.GONE);
                mXinLvLineView.setVisibility(NumberFormatUtils.toLong(data.getPulse_addtime()) > NumberFormatUtils.toLong(bean.getData().getPulse_addtime()) ? View.VISIBLE : View.GONE);
                mXueTangLineView.setVisibility(NumberFormatUtils.toLong(data.getGlu_addtime()) > NumberFormatUtils.toLong(bean.getData().getGlu_addtime()) ? View.VISIBLE : View.GONE);

            }

        });

    }
}
