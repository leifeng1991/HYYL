package com.xxzlkj.huayiyanglao.activity.equipment;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.xxzlkj.huayiyanglao.R;
import com.xxzlkj.huayiyanglao.app.ZhaoLinApplication;
import com.xxzlkj.huayiyanglao.config.ZLConstants;
import com.xxzlkj.huayiyanglao.config.ZLURLConstants;
import com.xxzlkj.zhaolinshare.base.base.MyBaseActivity;
import com.xxzlkj.zhaolinshare.base.model.BaseBean;
import com.xxzlkj.zhaolinshare.base.model.User;
import com.xxzlkj.zhaolinshare.base.net.OnMyActivityRequestListener;
import com.xxzlkj.zhaolinshare.base.net.RequestManager;
import com.xxzlkj.zhaolinshare.base.util.PermissionHelper;
import com.xxzlkj.zhaolinshare.base.util.PicassoUtils;
import com.xxzlkj.zhaolinshare.base.util.ToastManager;
import com.xxzlkj.zhaolinshare.base.util.UpLoadImageUtils;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import me.nereo.multi_image_selector.MultiImageSelector;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;


/**
 * 描述:绑定身份证-审核中
 *
 * @author zhangrq
 *         2017/7/12 09:48
 */
public class BindIdCardStateActivity extends MyBaseActivity {
    public static final String USER_NAME = "user_name";
    public static final String USER_IDCARD = "user_idcard";
    private TextView tv_name, tv_id_card;

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_bind_id_card_state);
    }

    @Override
    protected void findViewById() {
        tv_name = getView(R.id.tv_name);// 真实姓名
        tv_id_card = getView(R.id.tv_id_card);// 身份证
    }

    @Override
    public void setListener() {

    }

    @Override
    public void processLogic() {
        setTitleLeftBack();
        setTitleName("绑定身份证");
        Intent intent = getIntent();
        tv_name.setText(intent.getStringExtra(USER_NAME));
        tv_id_card.setText(intent.getStringExtra(USER_IDCARD));
    }

    /**
     * @param nameStr   姓名
     * @param idCardStr 身份证号
     */
    public static Intent newIntent(Context context, String nameStr, String idCardStr) {
        Intent intent = new Intent(context, BindIdCardStateActivity.class);
        intent.putExtra(USER_NAME,nameStr);
        intent.putExtra(USER_IDCARD,idCardStr);
        return intent;
    }
}
