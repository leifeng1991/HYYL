package com.xxzlkj.shop.activity.shopOrder;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xxzlkj.shop.R;
import com.xxzlkj.shop.config.StringConstants;
import com.xxzlkj.shop.config.URLConstants;
import com.xxzlkj.shop.event.AfterSaleDetailEvent;
import com.xxzlkj.shop.event.AfterSaleEvent;
import com.xxzlkj.shop.model.RefundDetail;
import com.xxzlkj.shop.model.RefundReasonBean;
import com.xxzlkj.shop.utils.MyDrawableUtils;
import com.xxzlkj.shop.weight.CustomPopupWindow;
import com.xxzlkj.shop.weight.MyImageSelectView;
import com.xxzlkj.zhaolinshare.base.app.BaseApplication;
import com.xxzlkj.zhaolinshare.base.base.MyBaseActivity;
import com.xxzlkj.zhaolinshare.base.model.BaseBean;
import com.xxzlkj.zhaolinshare.base.model.User;
import com.xxzlkj.zhaolinshare.base.net.OnMyActivityRequestListener;
import com.xxzlkj.zhaolinshare.base.net.RequestManager;
import com.xxzlkj.zhaolinshare.base.util.NumberFormatUtils;
import com.xxzlkj.zhaolinshare.base.util.StringUtil;
import com.xxzlkj.zhaolinshare.base.util.ToastManager;
import com.xxzlkj.zhaolinshare.base.util.UpLoadImageUtils;
import com.zrq.spanbuilder.Spans;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 申请售后填写页面
 */
public class ApplySalesActivity extends MyBaseActivity {
    private static final String REFUNDDETAIL_REFUNDDATABEAN = "refunddetail_refunddatabean";
    private static final String FALG = "falg";
    //订单id
    private String mOrderId;
    //商品id
    private String mPid;
    //数量
    private int mNumber;
    //总价
    private double mTotalPrice;
    //实际总价
    private double mTotalRealPrice;
    //单价
    private double mPrice;
    private RelativeLayout mTypeRelativeLayout;
    private RelativeLayout mYuanYinRelativeLayout;
    private TextView mTypeTextView;
    private TextView mNumberTextView;
    private TextView mYuanYinTextView;
    private Button mSummitBtn;
    private ImageView mReduceImageView;
    private ImageView mAddImageView;
    private EditText mPriceEditText;
    private EditText mInstructionsEditText;
    //退货类型
    private CustomPopupWindow mTypePopupWindow;
    //退货原因
    private CustomPopupWindow mReasonPopupWindow;
    private MyImageSelectView myImageSelectView;
    //false:修改申请售后 true：填写申请售后
    private boolean flag = true;
    private List<String> mTypeList;
    // 折扣系数
    private double discount = 1;
    // 优惠价格
    private double mCouponPrice = 0;

    /**
     * @param context
     * @param orderId     可申请售后订单id
     * @param pid         商品id
     * @param price       价格
     * @param num         数量
     * @param discount    折扣系数
     * @param couponPrice 优惠价格
     * @return
     */
    public static Intent newIntent(Context context, String orderId, String pid, String price, String num, String discount, String couponPrice) {
        Intent intent = new Intent(context, ApplySalesActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(StringConstants.INTENT_PARAM_ORDERID, orderId);
        bundle.putString(StringConstants.INTENT_PARAM_PID, pid);
        bundle.putString(StringConstants.INTENT_PARAM_PRICE, price);
        bundle.putString(StringConstants.INTENT_PARAM_NUM, num);
        bundle.putString(StringConstants.INTENT_PARAM_DISCOUNT, discount);
        bundle.putString(StringConstants.INTENT_PARAM_COUPON_PRICE, couponPrice);
        intent.putExtras(bundle);
        return intent;
    }

    /**
     * @param context
     * @param dataBean
     * @param price
     * @param num
     * @return
     */
    public static Intent newIntent(Context context, RefundDetail.RefundDataBean dataBean, String price, String num) {
        Intent intent = new Intent(context, ApplySalesActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean(FALG, false);
        bundle.putSerializable(REFUNDDETAIL_REFUNDDATABEAN, dataBean);
        bundle.putString(StringConstants.INTENT_PARAM_PRICE, price);
        bundle.putString(StringConstants.INTENT_PARAM_NUM, num);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_apply_sales);
    }

    @Override
    protected void findViewById() {
        mTypeRelativeLayout = getView(R.id.id_as_apply_type_layout);
        mYuanYinRelativeLayout = getView(R.id.id_as_yy_layout);
        mTypeTextView = getView(R.id.id_as_type);
        mNumberTextView = getView(R.id.id_as_number);
        mYuanYinTextView = getView(R.id.id_as_yy);
        TextView mTip1 = getView(R.id.id_as_type_tip);
        mTip1.setText(Spans.builder().text("退款类型").text("*", 14, 0xffff725c).text("：").build());
        TextView mTip2 = getView(R.id.id_as_yy_tip);
        mTip2.setText(Spans.builder().text("退款原因").text("*", 14, 0xffff725c).text("：").build());
        TextView mTip3 = getView(R.id.id_as_price_tip);
        mTip3.setText(Spans.builder().text("退款金额").text("*", 14, 0xffff725c).text("：").build());
        mReduceImageView = getView(R.id.id_as_reduce);
        mAddImageView = getView(R.id.id_as_add);
        mPriceEditText = getView(R.id.id_as_price);
        mInstructionsEditText = getView(R.id.id_sm_instructions);
        myImageSelectView = getView(R.id.myImageSelectView);
        mSummitBtn = getView(R.id.btn_summit);
        MyDrawableUtils.setButtonShapeOrange(mContext, mSummitBtn);

        mTypeList = new ArrayList<>();
        mTypeList.add("退款退货");
        mTypeList.add("退款不退货");

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            flag = bundle.getBoolean(FALG, true);
            // 单价
            mPrice = NumberFormatUtils.toDouble(bundle.getString(StringConstants.INTENT_PARAM_PRICE));
            // 最大数量
            mNumber = NumberFormatUtils.toInt(bundle.getString(StringConstants.INTENT_PARAM_NUM));
            if (flag) {// 填写申请售后
                mOrderId = bundle.getString(StringConstants.INTENT_PARAM_ORDERID);
                mPid = bundle.getString(StringConstants.INTENT_PARAM_PID);
                String discountStr = bundle.getString(StringConstants.INTENT_PARAM_DISCOUNT);
                // 优惠价格
                mCouponPrice = NumberFormatUtils.toDouble(bundle.getString(StringConstants.INTENT_PARAM_COUPON_PRICE));
                // 折扣系数
                if (NumberFormatUtils.toDouble(discountStr) != 0) {
                    discount = NumberFormatUtils.toDouble(discountStr);
                }

                // 赋初始值
                mTypeTextView.setText(mTypeList.get(0));

            } else {// 修改申请售后
                RefundDetail.RefundDataBean dataBean = (RefundDetail.RefundDataBean) bundle.getSerializable(REFUNDDETAIL_REFUNDDATABEAN);
                if (dataBean != null) {
                    mOrderId = dataBean.getId();
                }
                mPid = dataBean.getPid();
                // 退款类型
                if ("1".equals(dataBean.getTui_mode())) {
                    mTypeTextView.setText("退款退货");
                } else {
                    mTypeTextView.setText("退款不退货");
                }
                // 打折
                String discountStr = dataBean.getDiscount();
                if (NumberFormatUtils.toDouble(discountStr) != 0) {
                    discount = NumberFormatUtils.toDouble(discountStr);
                }
                mCouponPrice = NumberFormatUtils.toDouble(dataBean.getCoupon_price());

                mYuanYinTextView.setText(dataBean.getReason());
                mInstructionsEditText.setText(dataBean.getContent());
            }

            mTotalPrice = mPrice * mNumber * discount - mCouponPrice;
            mTotalRealPrice = mTotalPrice;
            mPriceEditText.setHint("最多" + StringUtil.saveTwoDecimal(String.valueOf(mTotalRealPrice)));
            mNumberTextView.setText(mNumber + "");

        }

        intPopupWindow();

    }

    @Override
    protected void setListener() {
        mTypeRelativeLayout.setOnClickListener(this);
        mYuanYinRelativeLayout.setOnClickListener(this);
        mReduceImageView.setOnClickListener(this);
        mAddImageView.setOnClickListener(this);
        mSummitBtn.setOnClickListener(this);
    }

    @Override
    protected void processLogic() {
        setTitleLeftBack();
        setTitleName("申请售后");
        // 初始化
        myImageSelectView.setActivity(this);

        getTypeData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        myImageSelectView.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        myImageSelectView.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int i = v.getId();
        if (i == R.id.id_left_image) {
            finish();

        } else if (i == R.id.id_as_apply_type_layout) {
            mTypePopupWindow.showAtLocationBottom(this, R.id.id_mso_main);

        } else if (i == R.id.id_as_yy_layout) {
            if (mReasonPopupWindow != null) {
                mReasonPopupWindow.showAtLocationBottom(ApplySalesActivity.this, R.id.id_mso_main);
            }

        } else if (i == R.id.id_as_reduce) {
            int num = NumberFormatUtils.toInt(mNumberTextView.getText().toString());
            if (num > 1) {
                num--;
            }
            mTotalRealPrice = mTotalPrice / mNumber * num;
            mPriceEditText.setHint("最多" + StringUtil.saveTwoDecimal(String.valueOf(mTotalRealPrice)));
            mNumberTextView.setText(String.valueOf(num));

        } else if (i == R.id.id_as_add) {
            int numAdd = NumberFormatUtils.toInt(mNumberTextView.getText().toString());
            if (NumberFormatUtils.toInt(mNumberTextView.getText().toString()) < mNumber) {
                numAdd++;
                mTotalRealPrice = mTotalPrice / mNumber * numAdd;
                mPriceEditText.setHint("最多" + StringUtil.saveTwoDecimal(String.valueOf(mTotalRealPrice)));
                mNumberTextView.setText(String.valueOf(numAdd));
            }

        } else if (i == R.id.btn_summit) {
            String typeStr = mTypeTextView.getText().toString().trim();
            String yuanYinStr = mYuanYinTextView.getText().toString().trim();
            String priceStr = mPriceEditText.getText().toString().trim();
            checkText(typeStr, yuanYinStr, priceStr);

        }
    }

    /**
     * 提交前检测
     *
     * @param typeStr
     * @param yuanYinStr
     * @param priceStr
     */
    private void checkText(String typeStr, String yuanYinStr, String priceStr) {
        if (TextUtils.isEmpty(typeStr)) {
            ToastManager.showShortToast(mContext, "退款类型不能为空");
            return;
        }
        if (TextUtils.isEmpty(yuanYinStr)) {
            ToastManager.showShortToast(mContext, "退款原因不能为空");
            return;
        }
        if (TextUtils.isEmpty(priceStr)) {
            ToastManager.showShortToast(mContext, "退款金额不能为空");
            return;
        }
        if (NumberFormatUtils.toDouble(priceStr) > NumberFormatUtils.toDouble(StringUtil.saveTwoDecimal(String.valueOf(mTotalRealPrice)))) {
            ToastManager.showShortToast(mContext, "退款金额不能超出最大退款金额");
            return;
        }
        String numberStr = mNumberTextView.getText().toString();
        String strInstructions = mInstructionsEditText.getText().toString().trim();
        uploadImages(numberStr, typeStr, yuanYinStr, strInstructions, priceStr);
    }

    /**
     * 上传图片
     *
     * @param number
     * @param mTuiMode
     * @param title
     * @param content
     * @param price
     */
    private void uploadImages(final String number, final String mTuiMode, final String title, final String content, String price) {
        showLoadDataAnim();
        final ArrayList<String> imageList = myImageSelectView.getImageList();
        UpLoadImageUtils.upLoadMultiImageInMainThread(this, imageList, new UpLoadImageUtils.OnMultiUploadListener() {
            @Override
            public void onUploadSucceed(List<String> urls, final String simg) {
                // 图片都上传成功，提交
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        submitData(number, mTuiMode, title, content, simg, price);
                    }
                });
            }

            @Override
            public void onUploadFailed(int errorCode, String errorMsg) {
                ToastManager.showShortToast(mContext, "上传图片失败，请稍后再试");
            }
        });
    }

    /**
     * PopupWindow
     */
    private void intPopupWindow() {
        mTypePopupWindow = new CustomPopupWindow(this, "请选择退款类型", new CustomPopupWindow.OnMyClickListener() {
            @Override
            public void sureClickListener(String item) {
                mTypeTextView.setText(item);
            }

            @Override
            public void cancelClickListener() {

            }
        }, mTypeList);

    }

    /**
     * 申请售后网络请求
     *
     * @param number   数量（必传）最大值从前面带过来
     * @param mTuiMode 退货方式（必传）1退款退货 2退款不退货
     * @param title    选择的分类名称（必传）
     * @param content  退款原因
     * @param simg     多图 图片之间用逗号隔开
     * @param price    退款金额
     */
    private void submitData(String number, String mTuiMode, String title, String content, String simg, String price) {
        Map<String, String> map = new HashMap<>();
        User user = BaseApplication.getInstance().getLoginUser();
        if (user == null) {
            return;
        }
        map.put(URLConstants.REQUEST_PARAM_UID, user.getData().getId());
        map.put(URLConstants.REQUEST_PARAM_ID, mOrderId);
        map.put(URLConstants.REQUEST_PARAM_PID, mPid);
        map.put(URLConstants.REQUEST_PARAM_PRICE, price);
        map.put(URLConstants.REQUEST_PARAM_NUM, number);
        String tuiMode;
        if ("退款退货".equals(mTuiMode)) {
            tuiMode = "1";
        } else {
            tuiMode = "2";
        }
        map.put(URLConstants.REQUEST_PARAM_TUI_MODE, tuiMode);
        map.put(URLConstants.REQUEST_PARAM_TITLE, title);
        if (!TextUtils.isEmpty(content))
            map.put(URLConstants.REQUEST_PARAM_CONTENT, content);
        if (!TextUtils.isEmpty(simg))
            map.put(URLConstants.REQUEST_PARAM_SIMG, simg);
        String url;
        if (flag) {
            url = URLConstants.REQUEST_ADD_REFUND;
        } else {
            url = URLConstants.REQUEST_EDIT_REFUND_GOODS;
        }
        RequestManager.createRequest(url, map,
                new OnMyActivityRequestListener<BaseBean>(this) {
                    @Override
                    public void onSuccess(BaseBean bean) {
                        if (flag) {
                            ToastManager.showShortToast(mContext, "申请成功");
                        } else {
                            ToastManager.showShortToast(mContext, "修改成功");
                            EventBus.getDefault().postSticky(new AfterSaleDetailEvent(true));
                        }

                        EventBus.getDefault().postSticky(new AfterSaleEvent(true));
                        finish();
                    }

                    @Override
                    public void onFailed(boolean isError, String code, String message) {
                        super.onFailed(isError, code, message);
                        if (flag) {
                            ToastManager.showShortToast(mContext, "申请失败");
                        } else {
                            ToastManager.showShortToast(mContext, "修改失败");
                        }

                    }
                });
    }

    /**
     * 申请售后分类
     */
    private void getTypeData() {
        Map<String, String> map = new HashMap<>();
        RequestManager.createRequest(URLConstants.REQUEST_REFUND_GROUP, map,
                new OnMyActivityRequestListener<RefundReasonBean>(this) {
                    @Override
                    public void onSuccess(RefundReasonBean bean) {
                        List<String> group = bean.getData().getGroup();
                        if (group != null) {
                            if (group.size() > 0 && flag) {// 赋初始值
                                mYuanYinTextView.setText(group.get(0));
                            }
                            mReasonPopupWindow = new CustomPopupWindow(ApplySalesActivity.this, "请选择退款原因", new CustomPopupWindow.OnMyClickListener() {
                                @Override
                                public void sureClickListener(String item) {
                                    mYuanYinTextView.setText(item);
                                }

                                @Override
                                public void cancelClickListener() {

                                }
                            }, group);
                        }
                    }
                });
    }

}
