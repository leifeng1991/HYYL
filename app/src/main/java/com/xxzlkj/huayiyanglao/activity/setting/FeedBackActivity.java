package com.xxzlkj.huayiyanglao.activity.setting;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.xxzlkj.huayiyanglao.R;
import com.xxzlkj.huayiyanglao.app.ZhaoLinApplication;
import com.xxzlkj.shop.config.URLConstants;
import com.xxzlkj.shop.utils.MyDrawableUtils;
import com.xxzlkj.zhaolinshare.base.base.MyBaseActivity;
import com.xxzlkj.zhaolinshare.base.model.BaseBean;
import com.xxzlkj.zhaolinshare.base.model.User;
import com.xxzlkj.zhaolinshare.base.net.OnMyActivityRequestListener;
import com.xxzlkj.zhaolinshare.base.net.RequestManager;
import com.xxzlkj.zhaolinshare.base.util.ToastManager;

import java.util.HashMap;

/**
 * 用户反馈
 */
public class FeedBackActivity extends MyBaseActivity {
    private Button btn_bottom;
    private EditText et_feedback;

    public static Intent newIntent(Context context) {
        return new Intent(context, FeedBackActivity.class);
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_feed_back);
    }

    @Override
    protected void findViewById() {
        et_feedback = getView(R.id.et_feedback);
        btn_bottom = getView(R.id.btn_bottom);
        MyDrawableUtils.setButtonShape(mContext, btn_bottom, R.color.blue_54B1F8);
    }

    @Override
    protected void setListener() {
        btn_bottom.setOnClickListener(this);
    }

    @Override
    protected void processLogic() {
        setTitleLeftBack();
        setTitleName("产品建议");
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_bottom:
                // 提交
                String feedbackString = et_feedback.getText().toString().trim();
                if (TextUtils.isEmpty(feedbackString)) {
                    ToastManager.showShortToast(mContext, "请输入反馈内容");
                    return;
                }
                summitFeedback(feedbackString);
                break;
        }
    }

    /**
     * 用户反馈
     *
     * @param feedbackString 反馈内容
     */
    private void summitFeedback(String feedbackString) {
        HashMap<String, String> stringStringHashMap = new HashMap<>();
        User user = ZhaoLinApplication.getInstance().getLoginUserDoLogin(this);
        if (user == null)
            return;
        stringStringHashMap.put("uid", user.getData().getId());
        stringStringHashMap.put("content", feedbackString);
        RequestManager.createRequest(URLConstants.FEEDBACK_URL, stringStringHashMap, new OnMyActivityRequestListener<BaseBean>(this) {
            @Override
            public void onSuccess(BaseBean bean) {
                ToastManager.showShortToast(mContext, "提交反馈成功");
                finish();
            }
        });
    }
}
