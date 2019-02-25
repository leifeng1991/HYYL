package com.xxzlkj.shop.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xxzlkj.shop.R;
import com.xxzlkj.shop.activity.shop.BrowsingHistoryActivity;
import com.xxzlkj.shop.activity.shop.GoodsDetailActivity;
import com.xxzlkj.shop.adapter.BrowsingHistoryAdapter;
import com.xxzlkj.shop.config.StringConstants;
import com.xxzlkj.shop.config.URLConstants;
import com.xxzlkj.shop.event.BrowserHistoryEvent;
import com.xxzlkj.shop.model.BrowserHistory;
import com.xxzlkj.shop.model.BrowsingHistoryModel;
import com.xxzlkj.shop.weight.CustomExpandableListView;
import com.xxzlkj.zhaolinshare.base.app.BaseApplication;
import com.xxzlkj.zhaolinshare.base.base.BaseActivity;
import com.xxzlkj.zhaolinshare.base.base.ViewPageFragment;
import com.xxzlkj.zhaolinshare.base.model.User;
import com.xxzlkj.zhaolinshare.base.net.OnMyActivityRequestListener;
import com.xxzlkj.zhaolinshare.base.net.RequestManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 浏览记录
 * create an instance of this fragment.
 */
public class BrowsingHistoryFragment extends ViewPageFragment {
    // 浏览商品
    public static final int BROWSING_GOODS = 0;
    // 浏览商铺
    public static final int BROWSING_STORE = 1;
    // 浏览活动
    public static final int BROWSING_ACTIVITY = 2;
    private BrowsingHistoryAdapter mBrowsingHistoryAdapter;
    private CustomExpandableListView mCustomExpandableListView;
    private int type;
    BrowsingHistoryActivity activity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (BrowsingHistoryActivity) activity;
    }

    /**
     *
     * @param type 0:浏览商品列表 1：浏览商铺列表 2：浏览活动列表 （必传）
     * @return
     */
    public static BrowsingHistoryFragment newInstance(int type) {
        BrowsingHistoryFragment fragment = new BrowsingHistoryFragment();
        Bundle args = new Bundle();
        args.putInt(StringConstants.INTENT_PARAM_TYPE,type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View loadViewLayout(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_browsing_history, container, false);
    }

    @Override
    protected void findViewById() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        mCustomExpandableListView = getView(R.id.id_bh_list);

        Bundle bundle = getArguments();
        if (bundle != null) {
            type = bundle.getInt(StringConstants.INTENT_PARAM_TYPE);
        }
    }

    @Override
    public void setListener() {
        mCustomExpandableListView.setOnChildClickListener((parent, v, groupPosition, childPosition, id) -> {
            HashMap<String, Object> hashMap = (HashMap<String, Object>) mBrowsingHistoryAdapter.getChild(groupPosition, childPosition);
            BrowserHistory.DataBean.ListBean listBean = (BrowserHistory.DataBean.ListBean) hashMap.get("childName");
            mActivity.startActivity(GoodsDetailActivity.newIntent(mContext, listBean.getGoods_id()));
//                ToastManager.showShortToast(mContext, "点击了");
            return true;
        });
    }

    @Override
    public void processLogic() {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 设置刷新
     */
    private void setRefresh() {
        switch (type) {
            case BROWSING_GOODS:// 商品
                getGoodsBrowsingHistory();
                break;
            case BROWSING_STORE:// 店铺
                break;
            case BROWSING_ACTIVITY:// 活动
                break;
        }
    }

    /**
     * 获取浏览记录数据
     */
    private void getGoodsBrowsingHistory() {
        HashMap<String, String> map = new HashMap<>();
        User user = BaseApplication.getInstance().getLoginUser();
        if (user == null) {
            return;
        }
        map.put(URLConstants.REQUEST_PARAM_UID, user.getData().getId());
        RequestManager.createRequest(URLConstants.REQUEST_TRACE_GOODS, map, new OnMyActivityRequestListener<BrowserHistory>((BaseActivity) getActivity()) {
            @Override
            public void onSuccess(BrowserHistory bean) {
                setData(bean);
            }

        });
    }

    /**
     * 设置数据
     */
    private void setData(BrowserHistory bean) {
        List<BrowserHistory.DataBean> mGoods = bean.getData();
        //定义父列表项List数据集合
        List<Map<String, Object>> mParentMapList = new ArrayList<>();
        //定义子列表项List数据集合
        List<List<Map<String, Object>>> mChildMapList = new ArrayList<>();

        for (int i = 0; i < mGoods.size(); i++) {
            List<BrowserHistory.DataBean.ListBean> listBeen = mGoods.get(i).getList();
            //提供当前父列的子列数据
            List<Map<String, Object>> childMapList = new ArrayList<>();
            for (int j = 0; j < listBeen.size(); j++) {
                Map<String, Object> childMap = new HashMap<>();
                BrowserHistory.DataBean.ListBean goods = listBeen.get(j);
                childMap.put("childName", goods);
                childMapList.add(childMap);
            }
            mChildMapList.add(childMapList);

            if (childMapList.size() > 0) {
                //提供父列表的数据
                Map<String, Object> parentMap = new HashMap<>();
                BrowsingHistoryModel model = new BrowsingHistoryModel();
                String[] splitTime = mGoods.get(i).getTime().split("-");
                model.setAdd_time(splitTime[1] + "月" + splitTime[2] + "日");
                parentMap.put("parentName", model);
                mParentMapList.add(parentMap);
            }
        }

        if (mParentMapList.size() > 0) {
            mBrowsingHistoryAdapter = new BrowsingHistoryAdapter(mContext, getActivity(),mParentMapList, mChildMapList);
            mCustomExpandableListView.setAdapter(mBrowsingHistoryAdapter);

            for (int i = 0; i < mParentMapList.size(); i++) {
                mCustomExpandableListView.expandGroup(i);
            }

            mBrowsingHistoryAdapter.setOnAllCheckedBoxNeedChangeListener(new BrowsingHistoryAdapter.OnAllCheckedBoxNeedChangeListener() {
                @Override
                public void onCheckedBoxNeedChange(boolean allParentIsChecked) {
                    activity.setCheckboxSate(allParentIsChecked);
                }
            });

            mBrowsingHistoryAdapter.setOnCheckHasGoodsListener(new BrowsingHistoryAdapter.OnCheckHasGoodsListener() {
                @Override
                public void onCheckHasGoods(boolean isHasGoods) {
                    if (!isHasGoods) {// 有商品
                        mCustomExpandableListView.setVisibility(View.GONE);
                        activity.setBottomLayoutVisible(false);
                    } else {// 没有商品（已经删除完）
                        mCustomExpandableListView.setVisibility(View.VISIBLE);
                        activity.setBottomLayoutVisible(true);
                    }
                }
            });
        }

    }

    /**
     * 单选按钮的显示
     *
     * @param isVisible
     */
    private void setCheckboxIsVisible(boolean isVisible) {
        if (mBrowsingHistoryAdapter != null && mBrowsingHistoryAdapter.getGroupCount() > 0) {
            BrowsingHistoryAdapter.isVisibelFlag = isVisible;
            mBrowsingHistoryAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 是否全选
     *
     * @param isAllCheck
     */
    private void setIsAllCheck(boolean isAllCheck) {
        if (mBrowsingHistoryAdapter != null && mBrowsingHistoryAdapter.getGroupCount() > 0) {
            mBrowsingHistoryAdapter.setupAllChecked(isAllCheck);
        }
    }

    /**
     * 删除选中的
     */
    private void delete() {
        if (mBrowsingHistoryAdapter != null && mBrowsingHistoryAdapter.getGroupCount() > 0) {
            mBrowsingHistoryAdapter.removeGoods();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = false)
    public void updataBrowserHistory(BrowserHistoryEvent event) {
        if (!TextUtils.isEmpty(event.getIsAllFlag())) {
            setIsAllCheck(event.isAllCheckbox());
        }
        if (!TextUtils.isEmpty(event.getIsVisibleFlag())) {
            setCheckboxIsVisible(event.isCheckboxVisible());
        }
        if (event.isDelete()) {
            delete();
        }
    }

    @Override
    public void refreshOnceData() {
        setRefresh();
    }
}
