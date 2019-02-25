package com.xxzlkj.huayiyanglao.activity.equipment;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.xxzlkj.huayiyanglao.R;
import com.xxzlkj.zhaolinshare.base.base.MyBaseActivity;


/**
 * 描述:提醒设置
 *
 * @author zhangrq
 *         2017/3/21 14:48
 */
public class RemindSettingActivity extends MyBaseActivity {


    public static Intent newIntent(Context context) {
        return new Intent(context, RemindSettingActivity.class);
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_remind_setting);
    }

    @Override
    protected void findViewById() {
        // TODO 差,后期换成列表
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void processLogic() {
        setTitleLeftBack();
        setTitleName("提醒设置");
        setTitleRightText("添加");
    }

    @Override
    public void onTitleRightClick(View view) {
        super.onTitleRightClick(view);
        startActivity(RemindEditActivity.newIntent(mContext));
    }
}
