package com.xxzlkj.shop.activity.shopOrder;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.xxzlkj.shop.R;
import com.xxzlkj.shop.config.URLConstants;
import com.xxzlkj.shop.utils.CheckUtils;
import com.xxzlkj.shop.utils.MyDrawableUtils;
import com.xxzlkj.shop.utils.OrderUtils;
import com.xxzlkj.shop.weight.MyImageSelectView;
import com.xxzlkj.zhaolinshare.base.app.BaseApplication;
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
public class ApplyRefundActivity extends MyBaseActivity {
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
        setContentView(R.layout.activity_apply_refund);
    }

    @Override
    protected void findViewById() {
        myImageSelectView = getView(R.id.myImageSelectView);
        tv_reason = getView(R.id.tv_reason);
        et_max_money = getView(R.id.et_max_money);
        et_des = getView(R.id.et_des);

        btn_summit = getView(R.id.btn_summit);
        MyDrawableUtils.setButtonShapeOrange(mContext, btn_summit);

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
        OrderUtils.defRefundReason(this, tv_reason, false);
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
        if (i == R.id.tv_reason) {// 申请退款原因
            OrderUtils.selectRefundReasonHasDialog(this, tv_reason, false);

        } else if (i == R.id.btn_summit) {// 提交
            String reason = tv_reason.getText().toString().trim();
            String money = et_max_money.getText().toString().trim();
            if (!TextUtils.isEmpty(money) && NumberFormatUtils.toDouble(money) > maxMoney) {
                ToastManager.showShortToast(mContext, "退款金额超出");
                return;
            }

            String des = et_des.getText().toString().trim();// 退款原因
            if (CheckUtils.checkInputReason(reason)) {
                // 正确
                uploadImages(reason, des);
            }

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
     * 提交
     */
    private void submitData(String reason, String content, String simg) {
        User user = BaseApplication.getInstance().getLoginUserDoLogin(this);
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
        RequestManager.createRequest(isEdit ? URLConstants.EDIT_ORDER_REFUND_URL : URLConstants.ADD_ORDER_REFUND_URL, stringStringHashMap, new OnMyActivityRequestListener<BaseBean>(this) {
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
        Intent intent = new Intent(context, ApplyRefundActivity.class);
        intent.putExtra(ORDER_ID, orderId);
        intent.putExtra(MAX_MONEY, maxMoney);
        intent.putExtra(IS_EDIT, isEdit);
        return intent;
    }


}
