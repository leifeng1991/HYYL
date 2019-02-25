package com.xxzlkj.huayiyanglao.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.tencent.mm.opensdk.constants.Build;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.xxzlkj.huayiyanglao.R;
import com.xxzlkj.huayiyanglao.activity.yiyangyiliao.YLSubscribeSuccessActivity;
import com.xxzlkj.huayiyanglao.adapter.PayTypeAdapter;
import com.xxzlkj.huayiyanglao.config.ZLURLConstants;
import com.xxzlkj.huayiyanglao.event.WeChatPayEvent;
import com.xxzlkj.huayiyanglao.model.AlipaySign;
import com.xxzlkj.huayiyanglao.model.PayResult;
import com.xxzlkj.huayiyanglao.model.PayType;
import com.xxzlkj.huayiyanglao.model.WeChatPay;
import com.xxzlkj.shop.config.StringConstants;
import com.xxzlkj.shop.config.URLConstants;
import com.xxzlkj.zhaolinshare.base.base.MyBaseActivity;
import com.xxzlkj.zhaolinshare.base.model.BaseBean;
import com.xxzlkj.zhaolinshare.base.net.OnMyActivityRequestListener;
import com.xxzlkj.zhaolinshare.base.net.RequestManager;
import com.xxzlkj.zhaolinshare.base.util.ToastManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.xxzlkj.huayiyanglao.config.ZLConstants.Strings.WECHAT_APPID;

/**
 * 支付方式选择
 */
public class PayActivity extends MyBaseActivity {
    public static final String ORDER_TITLE = "orderTitle";
    public static final String ORDER_ID = "orderId";
    public static final String ORDER_TYPE = "type";
    public static final int ORDER = 1;// 兆邻商城订单
    public static final int CLEANING_ORDER = 2;// 本地生活订单
    public static final int FOODS_ORDER = 3;// 健康餐桌
    public static final int HEALTH_ORDER = 4;// 医养医疗
    private int[] mIds = new int[]{R.mipmap.pay_weixin, R.mipmap.pay_zhifubao, R.mipmap.pay_yinlian};
    private String[] mNames = new String[]{"微信", "支付宝", "银联"};
    private String[] mContents = new String[]{"微信付款安全放心", "选用支付宝付款帮你立减", "银联支付随机立减20-99", "方便为您"};
    private Button mSureButton;
    private PayTypeAdapter mPayTypeAdapter;
    private static final int SDK_PAY_FLAG = 1;
    //支付宝签名
    private String mSign;
    //订单id
    private String orderId;
    //订单title
    private String orderTitle;
    //类型
    private int type;
    /**
     * 微信支付
     */
    private IWXAPI api;
    //  团购组id
    private String grouponTeamId;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /*
                      对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        Toast.makeText(PayActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                        intentOrderDetail();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(PayActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
                default:
                    break;
            }
        }

    };

    /**
     * @param orderId       订单id 2017开头 支付时使用 （必传）
     * @param orderTitle    订单title  100开头 跳转到详情时使用（必传）
     * @param type          1:商城订单 2：本地生活订单 3:外卖订单 4:医养医疗（必传）
     * @param grouponTeamId 团购组id 当type = 1并且商品是团购商品时必传
     */
    public static Intent newIntent(Context context, String orderId, String orderTitle, int type, String grouponTeamId) {
        Intent intent = new Intent(context, PayActivity.class);
        intent.putExtra(ORDER_ID, orderId);
        intent.putExtra(ORDER_TITLE, orderTitle);
        intent.putExtra(ORDER_TYPE, type);
        intent.putExtra(StringConstants.INTENT_GROUPON_TEAM_ID, grouponTeamId);
        return intent;
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_pay);
    }

    @Override
    protected void findViewById() {
        RecyclerView mPayRecyclerView = getView(R.id.id_recycler_view);
        mPayRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        // 支付类型列表适配器
        mPayTypeAdapter = new PayTypeAdapter(mContext, R.layout.adapter_pay_type_item);
        mPayRecyclerView.setAdapter(mPayTypeAdapter);
        mSureButton = getView(R.id.id_pad_sure);// 确定支付

    }

    @Override
    protected void setListener() {
        EventBus.getDefault().register(this);
        mSureButton.setOnClickListener(this);
        // item点击事件
        mPayTypeAdapter.setOnItemClickListener((position, item) -> {
            if (mPayTypeAdapter.selectPosition != position) {
                // 设置选中
                mPayTypeAdapter.selectPosition = position;
                mPayTypeAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void processLogic() {
        setTitleLeftBack();
        setTitleName("选择支付配送方式");
        // 通过WXAPIFactory工厂，获取IWXAPI的实例
        api = WXAPIFactory.createWXAPI(this, WECHAT_APPID, true);
        // 获取上个界面传过来的值
        if (getIntent() != null) {
            orderId = getIntent().getStringExtra(ORDER_ID);
            orderTitle = getIntent().getStringExtra(ORDER_TITLE);
            type = getIntent().getIntExtra(ORDER_TYPE, 1);
            grouponTeamId = getIntent().getStringExtra(StringConstants.INTENT_GROUPON_TEAM_ID);
        }
        // 支付方式列表
        List<PayType> mPayTypes = new ArrayList<>();
        for (int i = 0; i < mIds.length; i++) {
            PayType mPayType = new PayType();
            mPayType.setIcon(mIds[i]);
            mPayType.setName(mNames[i]);
            mPayType.setContent(mContents[i]);
            mPayTypes.add(mPayType);
        }
        mPayTypeAdapter.addList(mPayTypes);
        // 获取支付宝签名
        alipaySign();

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.id_pad_sure:
                checkPay(orderId);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(" 确认取消支付？");
        builder.setNegativeButton("继续支付", (dialog, which) -> dialog.dismiss());
        builder.setPositiveButton("仍要离开", (dialog, which) -> {
            dialog.dismiss();
            finish();
        });
        builder.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 获取支付宝签名
     */
    private void alipaySign() {
        HashMap<String, String> map = new HashMap<>();
        map.put(URLConstants.REQUEST_PARAM_ORDERID, orderId);
        String typeStr = "order";
        String url = null;
        switch (type) {
            case ORDER:// 兆邻商城订单
                typeStr = "order";
                url = URLConstants.REQUEST_AOP_SIGN;
                break;
            case CLEANING_ORDER:// 本地生活订单
                typeStr = "cleaning_order";
                url = URLConstants.JZ_AOP_SIGN;
                break;
            case FOODS_ORDER:// 健康餐桌
                typeStr = "foods_order";
                url = URLConstants.REQUEST_AOP_SIGN;
                break;
            case HEALTH_ORDER:// 医养医疗
                typeStr = "health_order";
                url = URLConstants.JZ_AOP_SIGN;
                break;
        }
        map.put(URLConstants.REQUEST_PARAM_TYPE, typeStr);
        RequestManager.createRequest(url, map, new OnMyActivityRequestListener<AlipaySign>(this) {
            @Override
            public void onSuccess(AlipaySign bean) {
                mSign = bean.getData().getSign();
            }

        });
    }

    /**
     * 支付前检测
     *
     * @param id 订单id
     */
    private void checkPay(String id) {
        HashMap<String, String> map = new HashMap<>();
        // 订单id
        map.put(URLConstants.REQUEST_PARAM_ID, id);
        String url = null;
        switch (type) {
            case ORDER:// 商城
                url = URLConstants.REQUEST_CHECK_PAY;
                // 团购小组id
                if (!TextUtils.isEmpty(grouponTeamId))
                    map.put(URLConstants.REQUEST_PARAM_GROUPON_TEAM_ID, grouponTeamId);
                break;
            case CLEANING_ORDER:// 本地生活
                url = URLConstants.JZ_CHECK_PAY;
                break;
            case FOODS_ORDER:// 健康餐桌
                url = ZLURLConstants.FOODS_CHECK_PAY_URL;
                break;
            case HEALTH_ORDER:// 医养医疗
                url = URLConstants.HEALTH_CHECK_PAY;
                break;
        }
        RequestManager.createRequest(url, map, new OnMyActivityRequestListener<BaseBean>(this) {
            @Override
            public void onSuccess(BaseBean bean) {
                switch (mPayTypeAdapter.selectPosition) {
                    case 0://微信支付
                        boolean isPaySupported = api.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
                        if (isPaySupported) {
                            // 获取微信预订单
                            getWechat(orderId);
                        } else {
                            ToastManager.showShortToast(mContext, "您的微信版本不支持支付");
                        }
                        break;
                    case 1://支付宝支付
                        alipayPay();
                        break;
                    case 2://银联支付
                        ToastManager.showShortToast(mContext, "暂未开放");
                        break;
                    case 3://货到付款
                        addCod(orderId);
                        break;
                }
            }
        });

    }

    /**
     * 获取微信预订单
     *
     * @param orderId 订单id
     */
    private void getWechat(final String orderId) {
        HashMap<String, String> map = new HashMap<>();
        map.put(URLConstants.REQUEST_PARAM_ORDERID, orderId);
        String typeStr = "order";
        String url = "";
        switch (type) {
            case ORDER:// 兆邻商城订单
                typeStr = "order";
                url = URLConstants.REQUEST_WX_SIGN;
                break;
            case CLEANING_ORDER:// 本地生活订单
                typeStr = "cleaning_order";
                url = URLConstants.JZ_WX_SIGN;
                break;
            case FOODS_ORDER:// 健康餐桌
                typeStr = "foods_order";
                url = URLConstants.REQUEST_WX_SIGN;
                break;
            case HEALTH_ORDER:// 医养医疗
                typeStr = "health_order";
                url = URLConstants.JZ_WX_SIGN;
                break;
        }
        map.put(URLConstants.REQUEST_PARAM_TYPE, typeStr);
        RequestManager.createRequest(url, map, new OnMyActivityRequestListener<WeChatPay>(this) {
            @Override
            public void onSuccess(WeChatPay bean) {
                WeChatPay.DataBean data = bean.getData();
                //拉取微信支付
                PayReq req = new PayReq();
                req.appId = data.getAppid();
                req.partnerId = data.getPartnerid();
                req.prepayId = data.getPrepayid();
                req.nonceStr = data.getNoncestr();
                req.timeStamp = data.getTimestamp();
                req.packageValue = "Sign=WXPay";
                req.sign = data.getSign();
                api.registerApp(WECHAT_APPID);
                // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
                api.sendReq(req);
            }

            @Override
            public void onFailed(boolean isError, String code, String message) {
                super.onFailed(isError, code, message);
            }
        });
    }

    /**
     * 支付宝回调
     */
    private void alipayPay() {
        Runnable payRunnable = () -> {
            PayTask alipay = new PayTask(PayActivity.this);
            Map<String, String> result = alipay.payV2(mSign, true);
            Log.i("msp", result.toString());

            Message msg = new Message();
            msg.what = SDK_PAY_FLAG;
            msg.obj = result;
            mHandler.sendMessage(msg);
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     * 货到付款
     *
     * @param orderId 订单id
     */
    private void addCod(final String orderId) {
        HashMap<String, String> map = new HashMap<>();
        // TODO ""
        map.put("", orderId);
        // TODO ""
        RequestManager.createRequest("",
                map, new OnMyActivityRequestListener<BaseBean>(this) {
                    @Override
                    public void onSuccess(BaseBean bean) {
                        ToastManager.showShortToast(mContext, "支付成功");
                        finish();
//                        startActivity(PaySuccessOrderDetailActivity.newIntent(mContext, orderId));

                    }

                    @Override
                    public void onFailed(boolean isError, String code, String message) {
                        super.onFailed(isError, code, message);
                        ToastManager.showShortToast(mContext, "支付失败");
                    }
                });
    }

    /**
     * 跳转到详情
     */
    private void intentOrderDetail() {
        switch (type) {
            case ORDER:// 兆邻商城订单

                break;
            case CLEANING_ORDER:// 本地生活订单

                break;
            case FOODS_ORDER:// 健康餐桌

                break;
            case HEALTH_ORDER:// 医养医疗
                // 跳转到预约成功界面
                startActivity(YLSubscribeSuccessActivity.newIntent(mContext, orderTitle));
                break;
        }
        finish();
    }

    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void updataWechatPay(WeChatPayEvent event) {
        String msg;
        switch (event.getState()) {
            case 0:
                msg = "支付成功！";
                intentOrderDetail();
                break;
            case -1:
                msg = "支付失败！";
                finish();
                break;
            case -2:
                msg = "您取消了支付！";
                break;
            default:
                msg = "支付失败！";
                break;
        }

        ToastManager.showShortToast(mContext, msg);
    }
}
