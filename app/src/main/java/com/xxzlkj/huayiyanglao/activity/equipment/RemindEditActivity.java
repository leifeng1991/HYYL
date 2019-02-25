package com.xxzlkj.huayiyanglao.activity.equipment;

import android.content.Context;
import android.content.Intent;

import com.xxzlkj.huayiyanglao.R;
import com.xxzlkj.zhaolinshare.base.base.MyBaseActivity;


/**
 * 描述:提醒编辑
 *
 * @author zhangrq
 *         2017/3/21 14:48
 */
public class RemindEditActivity extends MyBaseActivity {


    public static Intent newIntent(Context context) {
        return new Intent(context, RemindEditActivity.class);
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_remind_edit);
    }

    @Override
    protected void findViewById() {
        // TODO 差
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void processLogic() {
        setTitleLeftBack();
        setTitleName("提醒编辑");
    }

}
