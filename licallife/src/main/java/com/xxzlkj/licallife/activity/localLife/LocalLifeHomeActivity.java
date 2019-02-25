package com.xxzlkj.licallife.activity.localLife;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import com.sunfusheng.marqueeview.DisplayUtil;
import com.xxzlkj.licallife.R;
import com.xxzlkj.licallife.adapter.LocalLifeHomeAdapter;
import com.xxzlkj.licallife.interfac.LocalLifeLibraryInterface;
import com.xxzlkj.licallife.model.Clean;
import com.xxzlkj.licallife.model.LocalLifeHomeBean;
import com.xxzlkj.shop.config.GlobalParams;
import com.xxzlkj.shop.config.URLConstants;
import com.xxzlkj.shop.weight.BannerView;
import com.xxzlkj.zhaolinshare.base.MyRecyclerView;
import com.xxzlkj.zhaolinshare.base.app.BaseApplication;
import com.xxzlkj.zhaolinshare.base.base.BaseAdapter;
import com.xxzlkj.zhaolinshare.base.base.MyBaseActivity;
import com.xxzlkj.zhaolinshare.base.net.OnMyActivityRequestListener;
import com.xxzlkj.zhaolinshare.base.net.RequestManager;
import com.xxzlkj.zhaolinshare.base.util.NumberFormatUtils;
import com.xxzlkj.zhaolinshare.base.util.ToastManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 本地生活首页
 */
public class LocalLifeHomeActivity extends MyBaseActivity {
    private BannerView mBannerView;
    private MyRecyclerView mRecyclerView;
    private LocalLifeHomeAdapter mLocalLifeHomeAdapter;
    private int mPage = 1;
    private List<LocalLifeHomeBean.DataBean.BannerBean> banner;
    // 是否有保洁师列表
    private boolean isHasCleaningPeopleList;

    public static Intent newIntent(Context context) {
        return new Intent(context, LocalLifeHomeActivity.class);
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_local_life_home);
    }

    @Override
    protected void findViewById() {
        // 列表
        mRecyclerView = getView(R.id.id_recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setPullRefreshAndLoadingMoreEnabled(false, false);
        // 头
        View mHeaderView = LayoutInflater.from(mContext).inflate(R.layout.view_local_life_home_list_header, null);
        mBannerView = (BannerView) mHeaderView.findViewById(R.id.id_banner);
        int width = DisplayUtil.getWindowWidth(this);
        mBannerView.setWidthAndHeight(width, (int) (width * 1.0 * 416 / 750));
        mRecyclerView.addHeaderView(mHeaderView);
        // 设置数据
        mLocalLifeHomeAdapter = new LocalLifeHomeAdapter(mContext, R.layout.adapter_local_life_home_list_item);
        mRecyclerView.setAdapter(mLocalLifeHomeAdapter);

    }

    @Override
    protected void setListener() {
        // item点击事件
        mLocalLifeHomeAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener<LocalLifeHomeBean.DataBean.TypeBean>() {
            @Override
            public void onClick(int position, LocalLifeHomeBean.DataBean.TypeBean item) {
                int type = NumberFormatUtils.toInt(item.getType());
                switch (type) {
                    case 1:// 保洁项目保洁师
                        if (isHasCleaningPeopleList) {
                            // 保洁项目和保洁师都有
                            startActivity(ProjectCleaningAndHourlyWorkerActivity.newIntent(mContext, 1));
                        } else {
                            // 只有保洁项目
                            startActivity(ProjectCleaningAndHourlyWorkerActivity.newIntent(mContext, 4));
                        }
                        break;
                    case 2:// 月嫂
                        startActivity(NannyAndMaternityMatronListActivity.newIntent(mContext, false));
                        break;
                    case 3:// 保姆
                        startActivity(NannyAndMaternityMatronListActivity.newIntent(mContext, true));
                        break;
                    case 4:// 小时工
                        startActivity(ProjectCleaningAndHourlyWorkerActivity.newIntent(mContext, 2));
                        break;
                }
            }
        });
        // banner点击事件
        mBannerView.setOnHeaderViewClickListener(new BannerView.HeaderViewClickListener() {
            @Override
            public void HeaderViewClick(int position) {
                if (banner.size() > 0) {
                    LocalLifeHomeBean.DataBean.BannerBean bannerBean = banner.get(position);
                    int jumpType = NumberFormatUtils.toInt(bannerBean.getType());
                    switch (jumpType) {
                        case 0:// 不跳
                            break;
                        case 1:// 跳h5
                            if (!TextUtils.isEmpty(bannerBean.getTo()))
                                if (BaseApplication.getInstance() instanceof LocalLifeLibraryInterface) {
                                    ((LocalLifeLibraryInterface) BaseApplication.getInstance()).jumpToHasTitleWebView(LocalLifeHomeActivity.this, bannerBean.getTo(), "详情");
                                }
                            break;
                    }
                }
            }
        });
    }

    @Override
    protected void processLogic() {
        setTitleLeftBack();
        setTitleName("本地生活");
        // 小区id
        if (TextUtils.isEmpty(GlobalParams.communityId) || !GlobalParams.isOpenLocalLife) {
            ToastManager.showShortToast(mContext, getString(R.string.noLocalLifeServiceHint));
            finish();
        } else {
            getCleaningPeopleData();
            // 网络请求
            getNetData();
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
    }

    /**
     * 获取数据
     */
    private void getNetData() {
        Map<String, String> map = new HashMap<>();

        map.put(URLConstants.REQUEST_PARAM_COMMUNITY_ID, GlobalParams.communityId);
        RequestManager.createRequest(URLConstants.JZ_CLEANING_INDEX_URL, map,
                new OnMyActivityRequestListener<LocalLifeHomeBean>(this) {

                    @Override
                    public void onSuccess(LocalLifeHomeBean bean) {
                        // banner url集合
                        List<String> urlLists = new ArrayList<>();
                        // 设置banner数据
                        banner = bean.getData().getBanner();
                        if (banner.size() > 0) {
                            for (int i = 0; i < banner.size(); i++) {
                                urlLists.add(banner.get(i).getSimg());
                            }
                            mBannerView.setImgUrlData(urlLists);
                        }
                        // 设置列表数据
                        mRecyclerView.handlerSuccessHasRefreshAndLoad(mLocalLifeHomeAdapter, mPage == 1, bean.getData().getType());
                    }

                });
    }

    /**
     * 获取保洁师集合
     */
    private void getCleaningPeopleData() {
        HashMap<String, String> map = new HashMap<>();
        // 小区id
        map.put(URLConstants.REQUEST_PARAM_COMMUNITY_ID, GlobalParams.communityId);
        map.put(URLConstants.REQUEST_PARAM_PAGE, String.valueOf(mPage));
        RequestManager.createRequest(URLConstants.JZ_CLEANING_PEOPLE, map, new OnMyActivityRequestListener<Clean>(this) {
            @Override
            public void onSuccess(Clean bean) {
                List<Clean.DataBean> data = bean.getData();
                if (data.size() > 0) {
                    isHasCleaningPeopleList = true;
                }
            }

            @Override
            public void onFailed(boolean isError, String code, String message) {

            }

            @Override
            public void handlerStart() {
            }
        });
    }

}
