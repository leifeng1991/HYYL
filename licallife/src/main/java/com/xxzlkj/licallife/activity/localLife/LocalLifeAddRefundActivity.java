package com.xxzlkj.licallife.activity.localLife;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.xxzlkj.licallife.R;
import com.xxzlkj.licallife.event.AddRefundEvent;
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

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * 本地生活---申请售后
 *
 * @author zhangrq
 */
public class LocalLifeAddRefundActivity extends MyBaseActivity {
    private static final String ORDER_ID = "order_id";
    private static final String MAX_MONEY = "max_money";
    public static final String IS_EDIT = "isEdit";
    public static final String LOCAL_PID = "local_pid";
    public static final String LOCAL_NUM = "local_num";
    private MyImageSelectView myImageSelectView;
    private TextView btn_summit, tv_reason;
    //
    private String orderId;
    private EditText et_max_money, et_des;
    private String maxMoneyStr;
    private double maxMoney;
    private boolean isEdit;
    // 商品id
    private String pid;

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_localife_apply_after_sales);
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
        setTitleName("申请售后");
        // 初始化
        myImageSelectView.setActivity(this);

        orderId = getIntent().getStringExtra(ORDER_ID);
        maxMoneyStr = getIntent().getStringExtra(MAX_MONEY);
        isEdit = getIntent().getBooleanExtra(IS_EDIT, false);//是否是编辑页面
        pid = getIntent().getStringExtra(LOCAL_PID);

        et_max_money.setHint("最多" + maxMoneyStr);
        maxMoney = NumberFormatUtils.toDouble(maxMoneyStr);

        // 默认显示第一个原因
        OrderUtils.defRefundGroup(this, tv_reason, true);
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
            OrderUtils.selectRefundGroupHasDialog(this, tv_reason, true);

        } else if (i == R.id.btn_summit) {// 提交
            et_max_money = getView(R.id.et_max_money);
            et_des = getView(R.id.et_des);
            String reason = tv_reason.getText().toString().trim();
            String money = et_max_money.getText().toString().trim();
            if (!TextUtils.isEmpty(money) && NumberFormatUtils.toDouble(money) > maxMoney) {
                ToastManager.showShortToast(mContext, "退款金额超出");
                return;
            }

            if (!isEdit) {
                if (TextUtils.isEmpty(pid)) {// 商品id 申请售后用
                    ToastManager.showShortToast(mContext, "商品id为空");
                    return;
                }

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

        stringStringHashMap.put(URLConstants.REQUEST_PARAM_ID, orderId);

        if (!isEdit) {// 非编辑状态时传 pid num uid
            stringStringHashMap.put(URLConstants.REQUEST_PARAM_NUM, "1");
            stringStringHashMap.put(URLConstants.REQUEST_PARAM_PID, pid);
            stringStringHashMap.put(URLConstants.REQUEST_PARAM_UID, user.getData().getId());
        }
        stringStringHashMap.put(URLConstants.REQUEST_PARAM_PRICE, et_max_money.getText().toString().trim());
        stringStringHashMap.put(URLConstants.REQUEST_PARAM_TITLE, reason);
        if (!TextUtils.isEmpty(content))
            stringStringHashMap.put(URLConstants.REQUEST_PARAM_CONTENT, content);
        if (!TextUtils.isEmpty(simg))
            stringStringHashMap.put(URLConstants.REQUEST_PARAM_SIMG, simg);
        RequestManager.createRequest(isEdit ? URLConstants.JZ_AEDIT_REFUND_GOODS : URLConstants.JZ_ADD_REFUND, stringStringHashMap, new OnMyActivityRequestListener<BaseBean>(this) {
            @Override
            public void onSuccess(BaseBean bean) {
                EventBus.getDefault().postSticky(new AddRefundEvent(true));
                finish();
            }
        });
    }

    /**
     * @param context
     * @param orderId  订单id
     * @param maxMoney 最大金额
     * @param pid      商品id
     * @param isEdit   是否是编辑修改申请退款
     * @return
     */
    public static Intent newIntent(Context context, String orderId, String maxMoney, String pid, boolean isEdit) {
        Intent intent = new Intent(context, LocalLifeAddRefundActivity.class);
        intent.putExtra(ORDER_ID, orderId);
        intent.putExtra(MAX_MONEY, maxMoney);
        intent.putExtra(LOCAL_PID, pid);
        intent.putExtra(IS_EDIT, isEdit);
        return intent;
    }

}
