package com.xxzlkj.huayiyanglao.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.xxzlkj.huayiyanglao.R;
import com.xxzlkj.zhaolinshare.base.base.MyBaseActivity;
import com.xxzlkj.zhaolinshare.base.util.LogUtil;


/**
 * 搜索
 */
public class SearchActivity extends MyBaseActivity {
    private EditText mSearchEditText;
    private TextView mCancelTextView;

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, SearchActivity.class);
        Bundle bundle = new Bundle();
        intent.putExtras(bundle);
        return intent;
    }


    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_search);
    }

    @Override
    protected void findViewById() {
        mCancelTextView = getView(R.id.id_hs_cancel);// 取消

        mSearchEditText = getView(R.id.id_search);
        mSearchEditText.setHint("请输入关键字…");
        mSearchEditText.setFocusable(true);
        mSearchEditText.setFocusableInTouchMode(true);
        mSearchEditText.requestFocus();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

    }

    @Override
    protected void setListener() {
        mCancelTextView.setOnClickListener(this);
        mSearchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    LogUtil.e(TAG, "======onQueryTextSubmit============" + v.getText().toString());
                    return true;
                }
                return false;
            }
        });

    }


    @Override
    protected void processLogic() {

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.id_hs_cancel:
                finish();
                break;
        }
    }

}
