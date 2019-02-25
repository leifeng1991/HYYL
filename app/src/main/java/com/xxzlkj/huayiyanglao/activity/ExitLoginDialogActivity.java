package com.xxzlkj.huayiyanglao.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.xxzlkj.huayiyanglao.R;
import com.xxzlkj.huayiyanglao.app.ZhaoLinApplication;
import com.xxzlkj.huayiyanglao.util.ActivityUtils;


/**
 * 描述:退出登录的DialogActivity
 *
 * @author zhangrq
 *         2017/1/7 13:16
 */

public class ExitLoginDialogActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exit_login_dialog);

        TextView tv_message = (TextView) findViewById(R.id.tv_message);
        Button btn_config = (Button) findViewById(R.id.btn_config);
        // 设置值
        tv_message.setText(getIntent().getStringExtra("message"));
        // 接收到退出消息就代表退出登录
        ZhaoLinApplication.getInstance().setExitLoginUser();
        // 设置监听
        btn_config.setOnClickListener(v -> {
            // 退出登录，回到首页
            ActivityUtils.finishToMainTabActivityTab(this, 0);
        });
    }

    public static Intent newIntent(Context context, String message) {
        Intent intent = new Intent(context, ExitLoginDialogActivity.class);
        intent.putExtra("message", message);
        return intent;
    }

    @Override
    public void onBackPressed() {
        // 不可返回
    }
}
