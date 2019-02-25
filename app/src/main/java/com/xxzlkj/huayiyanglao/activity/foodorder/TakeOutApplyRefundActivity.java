package com.xxzlkj.huayiyanglao.activity.foodorder;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.xxzlkj.huayiyanglao.R;
import com.xxzlkj.huayiyanglao.app.ZhaoLinApplication;
import com.xxzlkj.huayiyanglao.config.ZLURLConstants;
import com.xxzlkj.huayiyanglao.util.HYNetRequestUtil;
import com.xxzlkj.shop.activity.shopOrder.ApplyRefundActivity;
import com.xxzlkj.shop.config.URLConstants;
import com.xxzlkj.shop.model.RefundReasonBean;
import com.xxzlkj.shop.utils.MyDrawableUtils;
import com.xxzlkj.shop.utils.OrderUtils;
import com.xxzlkj.shop.weight.MyImageSelectView;
import com.xxzlkj.zhaolinshare.base.base.BaseActivity;
import com.xxzlkj.zhaolinshare.base.base.MyBaseActivity;
import com.xxzlkj.zhaolinshare.base.model.BaseBean;
import com.xxzlkj.zhaolinshare.base.model.User;
import com.xxzlkj.zhaolinshare.base.net.OnMyActivityRequestListener;
import com.xxzlkj.zhaolinshare.base.net.RequestManager;
import com.xxzlkj.zhaolinshare.base.util.NumberFormatUtils;
import com.xxzlkj.zhaolinshare.base.util.ToastManager;
import com.xxzlkj.zhaolinshare.base.util.UpLoadImageUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * 申请退款
 *
 * @author zhangrq
 */
public class TakeOutApplyRefundActivity extends MyBaseActivity {
    private static final String ORDER_ID = "order_id";
    private static final String MAX_MONEY = "max_money";
    public static final String IS_EDIT = "isEdit";
    private MyImageSelectView myImageSelectView;
    private TextView btn_summit, tv_reason;
    //
    private String orderId;
    private EditText et_max_money, et_des;
    private String maxMoneyStr;
    private double maxMoney;
    private boolean isEdit;

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_take_out_apply_refund);
    }

    @Override
    protected void findViewById() {
        myImageSelectView = getView(R.id.myImageSelectView);
        tv_reason = getView(R.id.tv_reason);
        et_max_money = getView(R.id.et_max_money);
        et_des = getView(R.id.et_des);

        btn_summit = getView(R.id.btn_summit);
    }

    @Override
    protected void setListener() {
        tv_reason.setOnClickListener(this);
        btn_summit.setOnClickListener(this);
    }

    @Override
    protected void processLogic() {
        setTitleLeftBack();
        setTitleName("申请退款");
        // 初始化
        myImageSelectView.setActivity(this);

        orderId = getIntent().getStringExtra(ORDER_ID);
        maxMoneyStr = getIntent().getStringExtra(MAX_MONEY);
        isEdit = getIntent().getBooleanExtra(IS_EDIT, false);//是否是编辑页面

        et_max_money.setHint("最多" + maxMoneyStr);
        et_max_money.setText(maxMoneyStr);
        et_max_money.setEnabled(false);// 目前改为了，不能修改
        maxMoney = NumberFormatUtils.toDouble(maxMoneyStr);

        // 默认显示第一个原因
        defRefundReason(tv_reason);
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
        switch (v.getId()) {
            case R.id.tv_reason:
                // 申请退款原因
                selectRefundReasonHasDialog(tv_reason);
                break;
            case R.id.btn_summit:
                // 提交
                String reason = tv_reason.getText().toString().trim();
                String money = et_max_money.getText().toString().trim();
                if (!TextUtils.isEmpty(money) && NumberFormatUtils.toDouble(money) > maxMoney) {
                    ToastManager.showShortToast(mContext, "退款金额超出");
                    return;
                }

                String des = et_des.getText().toString().trim();// 退款原因
                if (checkInputReason(reason)) {
                    // 正确
                    uploadImages(reason, des);
                }
                break;
        }
    }

    private void uploadImages(final String reason, final String des) {
        showLoadDataAnim();
        final ArrayList<String> imageList = myImageSelectView.getImageList();
        UpLoadImageUtils.upLoadMultiImageInMainThread(this, imageList, new UpLoadImageUtils.OnMultiUploadListener() {
            @Override
            public void onUploadSucceed(List<String> urls, final String simg) {
                // 图片都上传成功，提交
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        submitData(reason, des, simg);
                    }
                });
            }

            @Override
            public void onUploadFailed(int errorCode, String errorMsg) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastManager.showShortToast(mContext, "上传图片失败，请稍后再试");
                    }
                });
            }
        });
    }

    /**
     * 选择退款原因--- 带弹框
     */
    public void defRefundReason(final TextView textView) {
        HashMap<String, String> stringStringHashMap = new HashMap<>();
        RequestManager.createRequest(ZLURLConstants.FOODS_REFUND_GROUP_URL, stringStringHashMap, new OnMyActivityRequestListener<RefundReasonBean>(this) {

            @Override
            public void onSuccess(RefundReasonBean bean) {
                List<String> group = bean.getData().getGroup();
                if (group != null && group.size() > 0) {
                    textView.setText(group.get(0));
                }
            }
        });
    }

    /**
     * 选择退款原因--- 带弹框
     */
    public void selectRefundReasonHasDialog(final TextView textView) {
        HashMap<String, String> stringStringHashMap = new HashMap<>();
        RequestManager.createRequest(ZLURLConstants.FOODS_REFUND_GROUP_URL, stringStringHashMap, new OnMyActivityRequestListener<RefundReasonBean>(this) {

            @Override
            public void onSuccess(RefundReasonBean bean) {
                List<String> group = bean.getData().getGroup();
                if (group != null && group.size() > 0) {
                    OptionsPickerView picker = new OptionsPickerView.Builder(TakeOutApplyRefundActivity.this, new OptionsPickerView.OnOptionsSelectListener() {
                        @Override
                        public void onOptionsSelect(int options1, int option2, int options3, View v) {
                            textView.setText(group.get(options1));
                        }
                    })
                            .setLineSpacingMultiplier(2.0f)
                            .setDividerColor(0xffc6c9cf)
                            .setTitleSize(14)
                            .setBgColor(0xffe1e1e1)
                            .setTitleBgColor(0xfff5f5f5)
                            .setTitleText("请选择退款原因")
                            .setContentTextSize(16)
                            .build();
                    picker.setPicker(group);
                    picker.show();
                }

            }
        });
    }

    /**
     * 提交
     */
    private void submitData(String reason, String content, String simg) {
        User user = ZhaoLinApplication.getInstance().getLoginUserDoLogin(this);
        if (user == null) {
            return;
        }
        HashMap<String, String> stringStringHashMap = new HashMap<>();
        // 本地生活申请退款
//        add_order_refund	本地生活申请退款
//        uid	会员id(必传)
//                id	订单id(必传)
//        title	选择的分类名称（必传）
//        content	退款原因
//        simg	多图 图片之间用逗号隔开

        // 修改申请退款
//        edit_order_refund	修改申请退款
//        post
//        参数名称	描述
//        id	退款编号id(必传) 在详情页带过去的
//        title	选择的分类名称（必传）
//        content	退款原因
//        simg	多图 图片之间用逗号隔开

        if (!isEdit) //不是编辑页面有Uid
            stringStringHashMap.put("uid", user.getData().getId());
        stringStringHashMap.put("id", orderId);
        stringStringHashMap.put("title", reason);
        if (!TextUtils.isEmpty(content))
            stringStringHashMap.put("content", content);
        if (!TextUtils.isEmpty(simg))
            stringStringHashMap.put("simg", simg);
        RequestManager.createRequest(isEdit ? ZLURLConstants.EDIT_FOODS_REFUND_URL : ZLURLConstants.FOODS_ADD_REFUND_URL, stringStringHashMap, new OnMyActivityRequestListener<BaseBean>(this) {
            @Override
            public void onSuccess(BaseBean bean) {
                finish();
            }
        });
    }

    /**
     * @param orderId  订单id
     * @param maxMoney 最大金额，现在没有用
     * @param isEdit   是否是编辑修改申请退款
     */
    public static Intent newIntent(Context context, String orderId, String maxMoney, boolean isEdit) {
        Intent intent = new Intent(context, TakeOutApplyRefundActivity.class);
        intent.putExtra(ORDER_ID, orderId);
        intent.putExtra(MAX_MONEY, maxMoney);
        intent.putExtra(IS_EDIT, isEdit);
        return intent;
    }


    private boolean checkInputReason(String reason) {
        if (TextUtils.isEmpty(reason)) {
            ToastManager.showLongToast(mContext, "请输入申请退款原因");
            return false;
        }
        return true;
    }
}
