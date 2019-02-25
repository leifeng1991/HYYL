package com.xxzlkj.huayiyanglao.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.xxzlkj.huayiyanglao.R;
import com.xxzlkj.huayiyanglao.activity.found.FoundDetailActivity;
import com.xxzlkj.huayiyanglao.activity.yiyangyiliao.YLDetailActivity;
import com.xxzlkj.huayiyanglao.adapter.TwoLevelListAdapter;
import com.xxzlkj.huayiyanglao.config.ZLConstants;
import com.xxzlkj.huayiyanglao.config.ZLURLConstants;
import com.xxzlkj.huayiyanglao.model.HealthGoodsListBean;
import com.xxzlkj.huayiyanglao.webView.WebViewActivity;
import com.xxzlkj.shop.config.GlobalParams;
import com.xxzlkj.zhaolinshare.base.MyRecyclerView;
import com.xxzlkj.zhaolinshare.base.base.MyBaseActivity;
import com.xxzlkj.zhaolinshare.base.net.OnMyActivityRequestListener;
import com.xxzlkj.zhaolinshare.base.net.RequestManager;
import com.xxzlkj.zhaolinshare.base.util.NumberFormatUtils;

import java.util.HashMap;


/**
 * 二级列表
 *
 * @author zhangrq
 */
public class TwoLevelListActivity extends MyBaseActivity {
    private MyRecyclerView mRecyclerView;
    private TwoLevelListAdapter mTwoLevelListAdapter;
    private int mPage = 1;
    private String mGroupId;

    /**
     * @param context
     * @param groupid 一级分类id(必传)
     * @param title   标题 用于二级标题的显示
     * @return
     */
    public static Intent newIntent(Context context, String groupid, String title) {
        Intent intent = new Intent(context, TwoLevelListActivity.class);
        intent.putExtra(ZLConstants.Strings.GROUPID, groupid);
        intent.putExtra(ZLConstants.Strings.TITLE, title);
        return intent;
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_my_recycler_view);
    }

    @Override
    protected void findViewById() {
        mRecyclerView = getView(R.id.id_recycler_view);// 分类列表
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setPullRefreshAndLoadingMoreEnabled(true, true);
        mTwoLevelListAdapter = new TwoLevelListAdapter(mContext, R.layout.adapter_two_level_list_item);
        mRecyclerView.setAdapter(mTwoLevelListAdapter);
    }

    @Override
    protected void setListener() {
        // 刷新和分页加载
        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                mPage = 1;
                healthGoods();
            }

            @Override
            public void onLoadMore() {
                mPage = mTwoLevelListAdapter.getItemCount() / 20 + 1;
                healthGoods();
            }
        });
        // item列表点击事件
        mTwoLevelListAdapter.setOnItemClickListener((position, item) -> {
            // 跳转到对应的详情模板
            String id = item.getId();
            switch (NumberFormatUtils.toInt(item.getType(), 1)) {
                case 1:// 预约模板
                    startActivity(YLDetailActivity.newIntent(mContext, id));
                    break;
                case 2:// 报名付费模板
                    startActivity(FoundDetailActivity.newIntent(mContext, id));
                    break;
                case 3:// 报名不付费模板
                    startActivity(FoundDetailActivity.newIntent(mContext, id));
                    break;
                case 4:// 活动展示模板
                    startActivity(FoundDetailActivity.newIntent(mContext, id));
                    break;
                case 5:// 图文展示模板
                    startActivity(WebViewActivity.newIntent(mContext, item.getUrl(), item.getTitle(), true));
                    break;
            }

        });
    }

    @Override
    protected void processLogic() {
        setTitleLeftBack();
        mGroupId = getIntent().getStringExtra(ZLConstants.Strings.GROUPID);
        setTitleName(getIntent().getStringExtra(ZLConstants.Strings.TITLE));
        healthGoods();
    }


    /**
     * 医养医疗产品列表
     */
    private void healthGoods() {
        HashMap<String, String> map = new HashMap<>();
        // 小区id
        if (TextUtils.isEmpty(GlobalParams.communityId)) {
            finish();
            return;
        }
        map.put(ZLConstants.Params.COMMUNITY_ID, GlobalParams.communityId);
        // 分类id(一级或二级)
        map.put(ZLConstants.Params.GROUPID, mGroupId);
        // 分页 一页20条
        map.put(ZLConstants.Params.PAGE, mPage + "");
        RequestManager.createRequest(ZLURLConstants.HEALTH_GOODS_URL, map, new OnMyActivityRequestListener<HealthGoodsListBean>(this) {

            @Override
            public void onSuccess(HealthGoodsListBean bean) {
                mRecyclerView.handlerSuccessHasRefreshAndLoad(mTwoLevelListAdapter, mPage == 1, bean.getData());
            }

            @Override
            public void onFailed(boolean isError, String code, String message) {
                super.onFailed(isError, code, message);
                mRecyclerView.handlerError(mTwoLevelListAdapter, mPage == 1);
            }
        });
    }
}
