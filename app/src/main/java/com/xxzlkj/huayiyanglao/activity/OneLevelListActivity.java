package com.xxzlkj.huayiyanglao.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.xxzlkj.huayiyanglao.R;
import com.xxzlkj.huayiyanglao.adapter.OneLevelListAdapter;
import com.xxzlkj.huayiyanglao.adapter.TwoLevelListAdapter;
import com.xxzlkj.huayiyanglao.config.ZLConstants;
import com.xxzlkj.huayiyanglao.config.ZLURLConstants;
import com.xxzlkj.huayiyanglao.model.HealthGoodsListBean;
import com.xxzlkj.huayiyanglao.util.HYNetRequestUtil;
import com.xxzlkj.huayiyanglao.util.ZLActivityUtils;
import com.xxzlkj.shop.config.GlobalParams;
import com.xxzlkj.zhaolinshare.base.MyRecyclerView;
import com.xxzlkj.zhaolinshare.base.base.BaseActivity;
import com.xxzlkj.zhaolinshare.base.base.BaseAdapter;
import com.xxzlkj.zhaolinshare.base.base.MyBaseActivity;
import com.xxzlkj.zhaolinshare.base.model.BaseBean;
import com.xxzlkj.zhaolinshare.base.net.OnMyActivityRequestListener;
import com.xxzlkj.zhaolinshare.base.net.RequestManager;

import java.util.HashMap;
import java.util.List;


/**
 * 一级列表
 *
 * @author zhangrq
 */
public class OneLevelListActivity extends MyBaseActivity {
    private MyRecyclerView mRecyclerView;
    private OneLevelListAdapter mOneLevelListAdapter;
    private int mPage = 1;
    private String mGroupId;


    /**
     * @param groupid 一级分类id(必传)
     * @param title   标题 用于一级标题的显示
     * @return
     */
    public static Intent newIntent(Context context, String groupid, String title) {
        Intent intent = new Intent(context, OneLevelListActivity.class);
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
        mOneLevelListAdapter = new OneLevelListAdapter(mContext, R.layout.adapter_one_level_list_item);
        mRecyclerView.setAdapter(mOneLevelListAdapter);
    }

    @Override
    protected void setListener() {
        // 刷新和分页加载
        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                mPage = 1;

            }

            @Override
            public void onLoadMore() {
                mPage = mOneLevelListAdapter.getItemCount() / 20 + 1;
                healthGoods();
            }
        });
        // 列表Item点击事件
        mOneLevelListAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener<HealthGoodsListBean.DataBean>() {
            @Override
            public void onClick(int position, HealthGoodsListBean.DataBean item) {
                // 跳转到二级列表界面
//                startActivity(TwoLevelListActivity.newIntent(mContext, item.getId(), item.getToptitle()));
                ZLActivityUtils.jumpToActivityByType(OneLevelListActivity.this, item.getType(), item.getTo(), item.getTitle());
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
     * 二级分类列表
     */
    private void healthGoods() {
        HashMap<String, String> map = new HashMap<>();
        // 分类id
        map.put(ZLConstants.Params.GROUPID, mGroupId);
        // 分页 一页20条
        map.put(ZLConstants.Params.PAGE, mPage + "");
        RequestManager.createRequest(ZLURLConstants.HEALTH_TWO_GROUP_URL, map, new OnMyActivityRequestListener<HealthGoodsListBean>(this) {

            @Override
            public void onSuccess(HealthGoodsListBean bean) {
                mRecyclerView.handlerSuccessHasRefreshAndLoad(mOneLevelListAdapter, mPage == 1, bean.getData());
            }

            @Override
            public void onFailed(boolean isError, String code, String message) {
                super.onFailed(isError, code, message);
                mRecyclerView.handlerError(mOneLevelListAdapter, mPage == 1);
            }
        });
    }
}
