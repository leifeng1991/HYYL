package com.xxzlkj.huayiyanglao.activity.found;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.xxzlkj.huayiyanglao.R;
import com.xxzlkj.huayiyanglao.adapter.FoundSignUpUserAdapter;
import com.xxzlkj.huayiyanglao.model.FoundSignUp;
import com.xxzlkj.shop.config.StringConstants;
import com.xxzlkj.shop.config.URLConstants;
import com.xxzlkj.zhaolinshare.base.MyRecyclerView;
import com.xxzlkj.zhaolinshare.base.base.MyBaseActivity;
import com.xxzlkj.zhaolinshare.base.net.OnMyActivityRequestListener;
import com.xxzlkj.zhaolinshare.base.net.RequestManager;

import java.util.HashMap;

/**
 * 已报名用户列表
 */
public class FoundSignUpActivity extends MyBaseActivity {
    private MyRecyclerView mRecyclerView;
    private FoundSignUpUserAdapter mFoundSignUpUserAdapter;
    //活动id
    private String id;
    //页数
    private int page = 1;

    /**
     * @param context 上下文 （必传）
     * @param id      活动id （必传）
     * @return
     */
    public static Intent newIntent(Context context, String id) {
        Intent intent = new Intent(context, FoundSignUpActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(StringConstants.INTENT_PARAM_ID, id);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_found_sign_up);
    }

    @Override
    protected void findViewById() {
        mRecyclerView = getView(R.id.id_fsu_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setPullRefreshAndLoadingMoreEnabled(true, true);
        mFoundSignUpUserAdapter = new FoundSignUpUserAdapter(this, R.layout.adapter_fsuu_list_item_1, true);
        mRecyclerView.setAdapter(mFoundSignUpUserAdapter);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            id = bundle.getString(StringConstants.INTENT_PARAM_ID);
        }

        setTitleLeftBack();
        setTitleName("报名列表");
    }

    @Override
    protected void setListener() {
        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                page = 1;
                getFoundSignUpUser();

            }

            @Override
            public void onLoadMore() {
                page = mFoundSignUpUserAdapter.getItemCount() / mRecyclerView.loadSize + 1;
                getFoundSignUpUser();
            }
        });

//        mFoundSignUpUserAdapter.setOnItemClickListener((position, item) -> startActivity(PersonMainActivity.newIntent(mContext, 1, item.getId())));
    }

    @Override
    protected void processLogic() {
        getFoundSignUpUser();
    }

    /**
     * 活动报名会员
     */
    private void getFoundSignUpUser() {
        HashMap<String, String> map = new HashMap<>();
        // 会员ID 必传
        map.put(URLConstants.REQUEST_PARAM_ID, id);
        map.put(URLConstants.REQUEST_PARAM_PAGE, String.valueOf(page));
        RequestManager.createRequest(URLConstants.REQUEST_ACTIVITY_USER,
                map, new OnMyActivityRequestListener<FoundSignUp>(this) {
                    @Override
                    public void onSuccess(FoundSignUp bean) {
                        mRecyclerView.handlerSuccessHasRefreshAndLoad(mFoundSignUpUserAdapter, page == 1, bean.getData());
                    }

                    @Override
                    public void onFailed(boolean isError, String code, String message) {
                        super.onFailed(isError, code, message);
                        mRecyclerView.handlerError(mFoundSignUpUserAdapter, page == 1);
                    }

                });
    }
}
