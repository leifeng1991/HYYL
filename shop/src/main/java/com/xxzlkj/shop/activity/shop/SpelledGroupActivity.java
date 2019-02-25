package com.xxzlkj.shop.activity.shop;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.xxzlkj.shop.R;
import com.xxzlkj.shop.adapter.SpelledGroupAdapter;
import com.xxzlkj.shop.config.StringConstants;
import com.xxzlkj.shop.config.URLConstants;
import com.xxzlkj.shop.model.SpelledGroupListBean;
import com.xxzlkj.shop.model.TeamBean;
import com.xxzlkj.zhaolinshare.base.MyRecyclerView;
import com.xxzlkj.zhaolinshare.base.app.BaseApplication;
import com.xxzlkj.zhaolinshare.base.base.BaseAdapter;
import com.xxzlkj.zhaolinshare.base.base.MyBaseActivity;
import com.xxzlkj.zhaolinshare.base.net.OnMyActivityRequestListener;
import com.xxzlkj.zhaolinshare.base.net.RequestManager;
import com.xxzlkj.zhaolinshare.base.util.SystemBarUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 正在拼团列表
 */
public class SpelledGroupActivity extends MyBaseActivity {
    private MyRecyclerView myRecyclerView;
    private SpelledGroupAdapter mSpelledGroupAdapter;
    private String grouponId;
    private int mPage = 1;

    /**
     * @param grouponId 团购活动id(必传)
     * @return
     */
    public static Intent newIntent(Context context, String grouponId) {
        Intent intent = new Intent(context, SpelledGroupActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(StringConstants.INTENT_PARAM_ID, grouponId);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_spelled_group);
    }

    @Override
    protected void findViewById() {
        SystemBarUtils.setStatusBarLightMode(this, true);
        myRecyclerView = getView(R.id.id_my_recycler);// 列表
        myRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        myRecyclerView.setPullRefreshAndLoadingMoreEnabled(true, true);
        mSpelledGroupAdapter = new SpelledGroupAdapter(mContext, this, R.layout.adapter_spelled_group_list_item);
        myRecyclerView.setAdapter(mSpelledGroupAdapter);
    }

    @Override
    protected void setListener() {
        // item点击事件
        mSpelledGroupAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener<TeamBean>() {
            @Override
            public void onClick(int position, TeamBean item) {
                // 跳转到参与拼团(详情)
                startActivity(SpelledGroupDesActivity.newIntent(mContext, item.getId()));
            }
        });
    }

    @Override
    protected void processLogic() {
        setTitleLeftBack();
        setTitleName("正在拼团");

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            grouponId = bundle.getString(StringConstants.INTENT_PARAM_ID);
            getGrouponTeamList();
        }
    }

    /**
     * 团购组列表
     */
    private void getGrouponTeamList() {
        if (BaseApplication.getInstance().getLoginUserDoLogin(this) != null) {
            Map<String, String> map = new HashMap<>();
            // 团购活动id（必传）
            map.put(URLConstants.REQUEST_PARAM_GROUPON_ID, grouponId);
            // 分页默认为1
            map.put(URLConstants.REQUEST_PARAM_PAGE, String.valueOf(mPage));
            RequestManager.createRequest(URLConstants.GROUPON_TEAM_LIST_URL, map, new OnMyActivityRequestListener<SpelledGroupListBean>(this) {
                @Override
                public void onSuccess(SpelledGroupListBean bean) {
                    myRecyclerView.handlerSuccessHasRefreshAndLoad(mSpelledGroupAdapter, mPage == 1, bean.getData());
                }

                @Override
                public void onFailed(boolean isError, String code, String message) {
                    super.onFailed(isError, code, message);
                    myRecyclerView.handlerError(mSpelledGroupAdapter, mPage == 1);
                }
            });
        }
    }

}
