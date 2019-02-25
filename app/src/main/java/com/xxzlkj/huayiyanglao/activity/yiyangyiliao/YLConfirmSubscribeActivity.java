package com.xxzlkj.huayiyanglao.activity.yiyangyiliao;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.xxzlkj.huayiyanglao.R;
import com.xxzlkj.huayiyanglao.activity.PayActivity;
import com.xxzlkj.huayiyanglao.app.ZhaoLinApplication;
import com.xxzlkj.huayiyanglao.config.ZLURLConstants;
import com.xxzlkj.huayiyanglao.model.ConfirmSubscribeBean;
import com.xxzlkj.shop.config.GlobalParams;
import com.xxzlkj.shop.config.URLConstants;
import com.xxzlkj.shop.model.Order;
import com.xxzlkj.zhaolinshare.base.base.MyBaseActivity;
import com.xxzlkj.zhaolinshare.base.model.BaseBean;
import com.xxzlkj.zhaolinshare.base.model.User;
import com.xxzlkj.zhaolinshare.base.net.OnMyActivityRequestListener;
import com.xxzlkj.zhaolinshare.base.net.RequestManager;
import com.xxzlkj.zhaolinshare.base.util.DateUtils;
import com.xxzlkj.zhaolinshare.base.util.InputMethodUtils;
import com.xxzlkj.zhaolinshare.base.util.NumberFormatUtils;
import com.xxzlkj.zhaolinshare.base.util.StringUtil;
import com.xxzlkj.zhaolinshare.base.util.ToastManager;
import com.zrq.spanbuilder.Spans;

import java.util.HashMap;

import static com.xxzlkj.shop.config.URLConstants.REQUEST_PARAM_CONTENT;
import static com.xxzlkj.shop.config.URLConstants.REQUEST_PARAM_ENDTIME;
import static com.xxzlkj.shop.config.URLConstants.REQUEST_PARAM_IDS;
import static com.xxzlkj.shop.config.URLConstants.REQUEST_PARAM_NUM;
import static com.xxzlkj.shop.config.URLConstants.REQUEST_PARAM_PRICE;
import static com.xxzlkj.shop.config.URLConstants.REQUEST_PARAM_TYPE;
import static com.xxzlkj.shop.config.URLConstants.REQUEST_PARAM_UID;


/**
 * 医养医疗确认预约
 */
public class YLConfirmSubscribeActivity extends MyBaseActivity {
    public static final String CONFIRM_SUBSCRIBE_BEAN = "confirm_subscribe_bean";
    private TextView mServiceItemsTextView, mSubscribeTimeTextView, mServiecePointTextView, mServiceChargeTextView, mConfirmSubscribeTextView;
    private EditText mInputPhoneEditText, mLeaveWordEditText;
    private ImageView mEditPhoneImageView;
    private ConfirmSubscribeBean mConfirmSubscribeBean;

    /**
     * @param context 上下文 （必传）
     * @param bean    所需数据包装  （必传）
     */
    public static Intent newIntent(Context context, ConfirmSubscribeBean bean) {
        Intent intent = new Intent(context, YLConfirmSubscribeActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(CONFIRM_SUBSCRIBE_BEAN, bean);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_yyyl_confirm_subscribe);
    }

    @Override
    protected void findViewById() {
        mServiceItemsTextView = getView(R.id.id_service_items);// 服务项目
        mSubscribeTimeTextView = getView(R.id.id_subscribe_time);// 预约时间
        mServiecePointTextView = getView(R.id.id_service_point);// 服务点
        mServiceChargeTextView = getView(R.id.id_service_charge);// 服务费用
        mInputPhoneEditText = getView(R.id.id_input_phone);// 手机号
        mEditPhoneImageView = getView(R.id.id_edit_phone);// 编辑手机号
        mLeaveWordEditText = getView(R.id.id_leave_word);// 留言
        mConfirmSubscribeTextView = getView(R.id.id_confirm_subscribe);// 确认预约
    }

    @Override
    protected void setListener() {
        // 编辑手机号
        mEditPhoneImageView.setOnClickListener(v -> InputMethodUtils.showInputMethod(mContext, mInputPhoneEditText));
        // 确认预约
        mConfirmSubscribeTextView.setOnClickListener(v -> submitConfirmSubscribe());
    }

    @Override
    protected void processLogic() {
        setTitleLeftBack();
        setTitleName("确认预约");
        mConfirmSubscribeBean = (ConfirmSubscribeBean) getIntent().getSerializableExtra(CONFIRM_SUBSCRIBE_BEAN);
        User loginUser = ZhaoLinApplication.getInstance().getLoginUser();
        if (loginUser != null)
            // 设置默认手机号
            mInputPhoneEditText.setText(loginUser.getData().getPhone());
        setData();
    }

    private void setData() {
        String service_project = mConfirmSubscribeBean.getService_project();
        mServiceItemsTextView.setText(Spans.builder().text("服务项目：").color(0xFF746F6E).text(TextUtils.isEmpty(service_project) ? "--" : service_project).build());
        String starttime = mConfirmSubscribeBean.getStarttime();
        mSubscribeTimeTextView.setText(Spans.builder().text("预约时间：").color(0xFF746F6E).
                text(TextUtils.isEmpty(starttime) ? "--" : DateUtils.getYearMonthDayHourMinute(NumberFormatUtils.toLong(starttime) * 1000)).build());
        String service_point = mConfirmSubscribeBean.getService_point();
        mServiecePointTextView.setText(Spans.builder().text("服务点：").color(0xFF746F6E).text(TextUtils.isEmpty(service_point) ? "--" : service_point).build());
        mServiceChargeTextView.setText(Spans.builder().text("服务费用：").color(0xFF746F6E).
                text("¥" + StringUtil.saveTwoDecimal(mConfirmSubscribeBean.getService_cost()) + "元").build());
    }

    /**
     * 提交
     */
    private void submitConfirmSubscribe() {
        String phone = mInputPhoneEditText.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            ToastManager.showShortToast(mContext, "请输入手机号");
            return;
        }
        preOrderCheck();
    }


    /**
     * 下单前检测
     */
    private void preOrderCheck() {
        HashMap<String, String> map = new HashMap<>();
        User user = ZhaoLinApplication.getInstance().getLoginUserDoLogin(this);
        if (user == null) {
            return;
        }
        // 1:预约
        map.put(REQUEST_PARAM_TYPE, "1");
        // 产品id
        map.put(REQUEST_PARAM_IDS, mConfirmSubscribeBean.getId());
        // 开始时间（时间戳）
        map.put(REQUEST_PARAM_ENDTIME, mConfirmSubscribeBean.getStarttime());
        // 结束时间
        map.put(URLConstants.REQUEST_PARAM_SERVICE_ENDTIME, mConfirmSubscribeBean.getEndtime());
        // 1：按时间段 2：按人数
        String res_type = mConfirmSubscribeBean.getRes_type();
        if ("2".equals(res_type))
            // 按时间段时传number_id
            map.put(URLConstants.REQUEST_PARAM_NUMBER_ID, mConfirmSubscribeBean.getNumber_id());
        // 按时间段时传number_id
        map.put(URLConstants.REQUEST_PARAM_NUM, "1");
        RequestManager.createRequest(ZLURLConstants.HEALTH_ADD_ORDER_CHECK_URL,
                map, new OnMyActivityRequestListener<BaseBean>(this) {

                    @Override
                    public void onSuccess(BaseBean bean) {
                        summitOrder();
                    }

                    @Override
                    public void handlerStart() {

                    }
                });
    }

    /**
     * 提交订单
     */
    private void summitOrder() {
        HashMap<String, String> map = new HashMap<>();
        User user = ZhaoLinApplication.getInstance().getLoginUserDoLogin(this);
        if (user == null) {
            return;
        }

        if (TextUtils.isEmpty(GlobalParams.communityId)) {
            ToastManager.showMsgToast(mContext, getString(R.string.noLocalLifeServiceHint));
            return;
        }
        // 小区id
        map.put(URLConstants.REQUEST_PARAM_COMMUNITY_ID, GlobalParams.communityId);
        // 下单人id
        map.put(REQUEST_PARAM_UID, user.getData().getId());
        // 产品id
        map.put(REQUEST_PARAM_IDS, mConfirmSubscribeBean.getId());
        // 开始时间（时间戳）
        map.put(REQUEST_PARAM_ENDTIME, mConfirmSubscribeBean.getStarttime());
        // 结束时间
        map.put(URLConstants.REQUEST_PARAM_SERVICE_ENDTIME, mConfirmSubscribeBean.getEndtime());
        // 1：按时间段 2：按人数
        String res_type = mConfirmSubscribeBean.getRes_type();
        if ("2".equals(res_type))
            // 按时间段时传number_id
            map.put(URLConstants.REQUEST_PARAM_NUMBER_ID, mConfirmSubscribeBean.getNumber_id());
        // 数量
        map.put(REQUEST_PARAM_NUM, "1");
        // 实际支付金额（必传）
        map.put(REQUEST_PARAM_PRICE, mConfirmSubscribeBean.getService_cost());
        // 1：预约
        map.put(REQUEST_PARAM_TYPE, "1");
        String mNoteStr = mLeaveWordEditText.getText().toString().trim();
        if (!TextUtils.isEmpty(mNoteStr)) {
            // 订单备注
            map.put(REQUEST_PARAM_CONTENT, mNoteStr);
        }
        // 联系电话
        map.put(URLConstants.REQUEST_PARAM_ADDRESS_PHONE, mInputPhoneEditText.getText().toString().trim());
        RequestManager.createRequest(ZLURLConstants.HEALTH_ORDER_ADD_URL,
                map, new OnMyActivityRequestListener<Order>(this) {
                    @Override
                    public void onSuccess(Order bean) {
                        Order.DataBean data = bean.getData();
                        startActivity(PayActivity.newIntent(mContext, data.getOrderid(), data.getTitle(), 4, ""));
                        finish();
                    }

                });
    }
}
