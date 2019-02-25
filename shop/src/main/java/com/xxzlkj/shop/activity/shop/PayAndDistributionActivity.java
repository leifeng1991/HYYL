package com.xxzlkj.shop.activity.shop;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xxzlkj.shop.R;
import com.xxzlkj.shop.config.StringConstants;
import com.xxzlkj.shop.config.URLConstants;
import com.xxzlkj.shop.model.AddressList;
import com.xxzlkj.shop.model.Pay;
import com.xxzlkj.shop.weight.CustomPopupWindow;
import com.xxzlkj.zhaolinshare.base.base.MyBaseActivity;
import com.xxzlkj.zhaolinshare.base.net.OnMyActivityRequestListener;
import com.xxzlkj.zhaolinshare.base.net.RequestManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * 支付配送方式
 */
public class PayAndDistributionActivity extends MyBaseActivity {
    private TextView mOnlineTextView;
    private TextView mDeliveryTextView;
    private TextView mZlTextView;
    private TextView mZqTextView;
    private TextView mKdTextView;
    private TextView mTimeTextView;
    // 送货时间布局
    private RelativeLayout mTimeLayout;
    // 自提布局
    private LinearLayout mZtLinearLayout;
    // 自提地点
    private LinearLayout mZtddRelativeLayout;
    private TextView mZtddTextView;
    // 自提时间
    private LinearLayout mZtsjRelativeLayout;
    // 支付方式
    private String zhiFuFlag;
    // 配送方式
    private String peiSongFlag;
    // 商品id
    private String mIds;
    // 地址id
    private String mAddressId;
    // 记录店铺位置
    private int position = 0;
    // 店铺id
    private String storeId;
    // 配送类型 1:兆邻配送 2：门店自提
    private int psType;
    // 支付类型 1:在线支付 2：货到付款
    private int zfType;
    // 送货时间
    private List<String> faPiaoList;
    private Button mSureButton;

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_pay_and_distribution);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mIds = bundle.getString(StringConstants.INTENT_PARAM_IDS);
            mAddressId = bundle.getString(StringConstants.INTENT_PARAM_ADDRESSID);
            psType = bundle.getInt(StringConstants.INTENT_PARAM_PSTYPE);
            zfType = bundle.getInt(StringConstants.INTENT_PARAM_ZFTYPE);
            storeId = bundle.getString(StringConstants.INTENT_PARAM_STOREID);
        }
    }

    @Override
    protected void findViewById() {
        mOnlineTextView = getView(R.id.id_pay_online);// 在线支付
        mDeliveryTextView = getView(R.id.id_pay_delivery);// 货到付款
        mZlTextView = getView(R.id.id_pay_zl);// 兆邻配送
        mZqTextView = getView(R.id.id_pay_zq);// 门店自提
        mKdTextView = getView(R.id.id_pay_kd);// 快递配送
        mTimeLayout = getView(R.id.id_pad_time);// 送货时间布局
        mTimeTextView = getView(R.id.id_pad_time_text);// 送货时间
        mZtLinearLayout = getView(R.id.id_zt_layout);// 自提地点布局
        mZtddRelativeLayout = getView(R.id.id_ztdd_time);// 自提地点布局
        mZtddTextView = getView(R.id.id_ztdd_text);// 自提地点
        mZtsjRelativeLayout = getView(R.id.id_ztsj_time);// 自提时间布局
        mZtLinearLayout = getView(R.id.id_zt_layout);// 自提时间布局
        mSureButton = getView(R.id.id_pad_sure);// 确定

        if (1 == psType) {// 兆邻配送
            setTextWhiteColor(mZlTextView);
            setTextGrayColor(mZqTextView);
            setTextGrayColor(mKdTextView);
            peiSongFlag = mZlTextView.getText().toString();
//                mTimeLayout.setVisibility(View.VISIBLE);
            mZtLinearLayout.setVisibility(View.GONE);
        } else {// 门店自提
            setTextWhiteColor(mZqTextView);
            setTextGrayColor(mZlTextView);
            setTextGrayColor(mKdTextView);
            peiSongFlag = mZqTextView.getText().toString();
//                mTimeLayout.setVisibility(View.GONE);
            mZtLinearLayout.setVisibility(View.VISIBLE);
        }

        if (1 == zfType) {// 在线支付
            setTextWhiteColor(mOnlineTextView);
            setTextGrayColor(mDeliveryTextView);
            zhiFuFlag = mOnlineTextView.getText().toString();
        } else {// 货到付款
            setTextWhiteColor(mDeliveryTextView);
            setTextGrayColor(mOnlineTextView);
            zhiFuFlag = mDeliveryTextView.getText().toString();
        }
    }

    @Override
    protected void setListener() {
        mOnlineTextView.setOnClickListener(this);
        mDeliveryTextView.setOnClickListener(this);
        mZlTextView.setOnClickListener(this);
        mZqTextView.setOnClickListener(this);
        mKdTextView.setOnClickListener(this);
        mTimeLayout.setOnClickListener(this);
        mZtddRelativeLayout.setOnClickListener(this);
        mZtsjRelativeLayout.setOnClickListener(this);
        mSureButton.setOnClickListener(this);

    }

    @Override
    protected void processLogic() {
        setTitleLeftBack();
        setTitleName("选择支付配送方式");
        getStoreList(mAddressId);
        checkDelivery(mIds);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int i = v.getId();
        if (i == R.id.id_pay_online) {
            setTextWhiteColor(mOnlineTextView);
            setTextGrayColor(mDeliveryTextView);
            zhiFuFlag = mOnlineTextView.getText().toString();

        } else if (i == R.id.id_pay_delivery) {
            setTextWhiteColor(mDeliveryTextView);
            setTextGrayColor(mOnlineTextView);
            zhiFuFlag = mDeliveryTextView.getText().toString();

        } else if (i == R.id.id_pay_zl) {
            setTextWhiteColor(mZlTextView);
            setTextGrayColor(mZqTextView);
            setTextGrayColor(mKdTextView);
            peiSongFlag = mZlTextView.getText().toString();
//                mTimeLayout.setVisibility(View.VISIBLE);
            mZtLinearLayout.setVisibility(View.GONE);

        } else if (i == R.id.id_pay_zq) {
            setTextWhiteColor(mZqTextView);
            setTextGrayColor(mZlTextView);
            setTextGrayColor(mKdTextView);
            peiSongFlag = mZqTextView.getText().toString();
//                mTimeLayout.setVisibility(View.GONE);
            mZtLinearLayout.setVisibility(View.VISIBLE);

        } else if (i == R.id.id_pay_kd) {
            setTextWhiteColor(mKdTextView);
            setTextGrayColor(mZlTextView);
            setTextGrayColor(mZqTextView);
            peiSongFlag = mKdTextView.getText().toString();

        } else if (i == R.id.id_pad_time) {
            choiceSongHuoTime();

        } else if (i == R.id.id_ztdd_time) {
            Intent storeIntent = new Intent(this, StoreListActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString(StringConstants.INTENT_PARAM_ADDRESSID, mAddressId);
            bundle.putInt(StringConstants.INTENT_PARAM_POSITION, position);
            storeIntent.putExtras(bundle);
            startActivityForResult(storeIntent, StringConstants.REQUSTCODE_STORE);

        } else if (i == R.id.id_ztsj_time) {
        } else if (i == R.id.id_pad_sure) {
            intentBack();

        }
    }

    public void intentBack() {
        Intent intent = getIntent();
        intent.putExtra(StringConstants.INTENT_PARAM_PAYTYPE, zhiFuFlag);
        intent.putExtra(StringConstants.INTENT_PARAM_PEISONGTYPE, peiSongFlag);
        intent.putExtra(StringConstants.INTENT_PARAM_PS_TIME, mTimeTextView.getText().toString());
        if (!TextUtils.isEmpty(storeId)) {
            intent.putExtra(StringConstants.INTENT_PARAM_STOREID, storeId);
        }
        setResult(RESULT_OK, intent);
        finish();
    }

    /**
     * 设置白色字体颜色和对应背景
     *
     * @param textView
     */
    private void setTextWhiteColor(TextView textView) {
        textView.setTextColor(0xffffffff);
        textView.setBackgroundResource(R.drawable.shape_buy_now);
    }

    /**
     * 设置灰色字体颜色和对应背景
     *
     * @param textView
     */
    private void setTextGrayColor(TextView textView) {
        textView.setTextColor(0xff898989);
        textView.setBackgroundResource(R.drawable.shape_gray_stroke);
    }

    /**
     * 选择送货时间
     */
    private void choiceSongHuoTime() {
        if (faPiaoList == null) {
            faPiaoList = new ArrayList<>();
            faPiaoList.add("即刻送货上门");
            faPiaoList.add("工作日、节假日均可送货");
            faPiaoList.add("工作日");
            faPiaoList.add("双休日及节假日");
        }
        CustomPopupWindow popupWindow = new CustomPopupWindow(this, "请选择送货时间", new CustomPopupWindow.OnMyClickListener() {
            @Override
            public void sureClickListener(String item) {
                mTimeTextView.setText(item);
            }

            @Override
            public void cancelClickListener() {

            }
        }, faPiaoList);
        popupWindow.showAtLocationBottom(this, R.id.id_mso_main);
    }

    /**
     * 选择支付配送（选择支付配送时检测下内部控件）
     *
     * @param id
     */
    private void checkDelivery(String id) {
        HashMap<String, String> map = new HashMap<>();
        map.put(URLConstants.REQUEST_PARAM_ID, id);
        RequestManager.createRequest(URLConstants.REQUEST_CHOOSE_DISTRIBUTION,
                map, new OnMyActivityRequestListener<Pay>(this) {
                    @Override
                    public void onSuccess(Pay bean) {
                        int pay = bean.getData().getPay();
                        if (pay == 2) {
                            mDeliveryTextView.setVisibility(View.VISIBLE);
                        }
                    }
                });

    }

    /**
     * 获取线下门店地址列表
     *
     * @param addressId
     */
    private void getStoreList(String addressId) {
        HashMap<String, String> map = new HashMap<>();
        map.put(URLConstants.REQUEST_PARAM_ID, addressId);
        RequestManager.createRequest(URLConstants.REQUEST_STORE_LIST,
                map, new OnMyActivityRequestListener<AddressList>(this) {
                    @Override
                    public void onSuccess(AddressList bean) {
                        List<AddressList.DataBean> data = bean.getData();
                        if (data != null && data.size() > 0) {
                            if (!TextUtils.isEmpty(storeId)) {
                                for (int i = 0; i < data.size(); i++) {
                                    AddressList.DataBean dataBean = data.get(i);
                                    if (dataBean != null) {
                                        if (storeId.equals(dataBean.getId())) {
                                            position = i;
                                            mZtddTextView.setText(data.get(i).getTitle());
                                            return;
                                        }
                                    }
                                }
                            }
                        }
                    }
                });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (data != null) {
                position = data.getIntExtra(StringConstants.INTENT_PARAM_POSITION, 1);
                String mNameString = data.getStringExtra(StringConstants.INTENT_PARAM_NAME);
                mZtddTextView.setText(mNameString);
                storeId = data.getStringExtra(StringConstants.INTENT_PARAM_STOREID);
            }
        }
    }

}
