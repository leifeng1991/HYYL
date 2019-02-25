package com.xxzlkj.huayiyanglao.activity.foodorder;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.xxzlkj.huayiyanglao.R;
import com.xxzlkj.huayiyanglao.app.ZhaoLinApplication;
import com.xxzlkj.huayiyanglao.config.ZLConstants;
import com.xxzlkj.huayiyanglao.config.ZLURLConstants;
import com.xxzlkj.huayiyanglao.model.TakeOutComplainGroupBean;
import com.xxzlkj.shop.model.complainShopGroupBean;
import com.xxzlkj.shop.weight.MyImageSelectView;
import com.xxzlkj.zhaolinshare.base.base.MyBaseActivity;
import com.xxzlkj.zhaolinshare.base.model.BaseBean;
import com.xxzlkj.zhaolinshare.base.model.User;
import com.xxzlkj.zhaolinshare.base.net.OnMyActivityRequestListener;
import com.xxzlkj.zhaolinshare.base.net.RequestManager;
import com.xxzlkj.zhaolinshare.base.util.ToastManager;
import com.xxzlkj.zhaolinshare.base.util.UpLoadImageUtils;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;


/**
 * 申请投诉
 *
 * @author zhangrq
 */
public class TakeOutApplyComplainActivity extends MyBaseActivity {
    private static final String ORDER_ID = "order_id";
    private MyImageSelectView myImageSelectView;
    private TextView btn_summit;
    private String orderId;
    private EditText et_des;
    private TagFlowLayout mFlowLayout;
    private TagAdapter<String> tagAdapter;
    private List<String> mValues = new ArrayList<>();
    List<TakeOutComplainGroupBean.DataBean> data;

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_take_out_apply_complain);
    }

    @Override
    protected void findViewById() {
        mFlowLayout = getView(R.id.id_flowlayout);
        et_des = getView(R.id.et_des);
        myImageSelectView = getView(R.id.myImageSelectView);

        btn_summit = getView(R.id.btn_summit);
    }

    @Override
    protected void setListener() {
        btn_summit.setOnClickListener(this);
    }

    @Override
    protected void processLogic() {
        setTitleLeftBack();
        setTitleName("投诉");
        // 初始化
        myImageSelectView.setActivity(this);

        tagAdapter = new TagAdapter<String>(mValues) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) View.inflate(mContext, R.layout.item_take_out_apply_complain, null);
                tv.setText(s);
                return tv;
            }
        };
        mFlowLayout.setAdapter(tagAdapter);

        orderId = getIntent().getStringExtra(ORDER_ID);
        // 获取网络数据
        getNetData();
    }

    private void getNetData() {
        HashMap<String, String> stringStringHashMap = new HashMap<>();
        stringStringHashMap.put(ZLConstants.Params.ORDERID, orderId);
        RequestManager.createRequest(ZLURLConstants.FOODS_COMPLAIN_URL, stringStringHashMap, new OnMyActivityRequestListener<TakeOutComplainGroupBean>(this) {
            @Override
            public void onSuccess(TakeOutComplainGroupBean bean) {
                mValues.clear();
                data = bean.getData();
                for (int i = 0; i < data.size(); i++) {
                    mValues.add(data.get(i).getTitle());
                }
                tagAdapter.notifyDataChanged();
            }
        });
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
            case R.id.btn_summit:
                // 提交
                Set<Integer> selectedList = mFlowLayout.getSelectedList();
                if (selectedList == null || selectedList.size() == 0) {
                    ToastManager.showShortToast(mContext, "请选择投诉原因");
                    return;
                }
                String des = et_des.getText().toString().trim();// 退款原因
                // 正确
                uploadImages(getReason(selectedList), des);
                break;
        }
    }

    private String getReason(Set<Integer> selectedList) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < selectedList.size(); i++) {
            if (i < mValues.size()) {
                if (i != 0) {
                    sb.append(",");
                }
                sb.append(data.get(i).getId());
            }
        }
        return sb.toString();
    }

    private void uploadImages(final String reason, final String des) {
        showLoadDataAnim();
        final ArrayList<String> imageList = myImageSelectView.getImageList();
        UpLoadImageUtils.upLoadMultiImageInMainThread(this, imageList, new UpLoadImageUtils.OnMultiUploadListener() {
            @Override
            public void onUploadSucceed(List<String> urls, final String simg) {
                // 图片都上传成功，提交
                submitData(reason, des, simg);
            }

            @Override
            public void onUploadFailed(int errorCode, String errorMsg) {
                ToastManager.showShortToast(mContext, "上传图片失败，请稍后再试");
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
//        uid	会员id(必传)
//                id	订单id(必传)
//        title	选择的原因名称（必传）
//        content	备注
//        simg	多图 图片之间用逗号隔开


        stringStringHashMap.put("uid", user.getData().getId());
        stringStringHashMap.put("orderid", orderId);
        stringStringHashMap.put("complain_id", reason);
        if (!TextUtils.isEmpty(content))
            stringStringHashMap.put("remark", content);
        if (!TextUtils.isEmpty(simg))
            stringStringHashMap.put("img", simg);
        RequestManager.createRequest(ZLURLConstants.SUB_FOODS_COMPLAIN_URL, stringStringHashMap, new OnMyActivityRequestListener<BaseBean>(this) {
            @Override
            public void onSuccess(BaseBean bean) {
                ToastManager.showShortToast(mContext, "订单投诉提交成功");
                finish();
            }
        });
    }

    /**
     * @param orderId 订单id
     */
    public static Intent newIntent(Context context, String orderId) {
        Intent intent = new Intent(context, TakeOutApplyComplainActivity.class);
        intent.putExtra(ORDER_ID, orderId);
        return intent;
    }


}
