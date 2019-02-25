package com.xxzlkj.licallife.fragment;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.xxzlkj.licallife.R;
import com.xxzlkj.licallife.activity.localLife.CleanersOrHourlyWorkerDetailsActivity;
import com.xxzlkj.licallife.adapter.CleanerAdapter;
import com.xxzlkj.licallife.model.Clean;
import com.xxzlkj.licallife.utils.ZLBannerUtils;
import com.xxzlkj.shop.config.GlobalParams;
import com.xxzlkj.shop.config.StringConstants;
import com.xxzlkj.shop.config.URLConstants;
import com.xxzlkj.shop.weight.BannerView;
import com.xxzlkj.zhaolinshare.base.MyRecyclerView;
import com.xxzlkj.zhaolinshare.base.base.BaseActivity;
import com.xxzlkj.zhaolinshare.base.base.BaseAdapter;
import com.xxzlkj.zhaolinshare.base.base.ViewPageFragment;
import com.xxzlkj.zhaolinshare.base.net.OnMyActivityRequestListener;
import com.xxzlkj.zhaolinshare.base.net.RequestManager;

import java.util.HashMap;

/**
 * 保洁师/小时工
 * create an instance of this fragment.
 */
public class CleanerAndHourlyWorkerListFragment extends ViewPageFragment {
    private MyRecyclerView mCleanRecyclerView;
    private BannerView mBannerView;
    private CleanerAdapter mCleanerAdapter;
    private int mPage = 1;
    // tab
    private LinearLayout mFirstLayout;
    private LinearLayout mSecondLayout;
    private LinearLayout mThirdtLayout;
    private LinearLayout mFourLayout;
    private boolean[] isUpOrBottom = new boolean[]{false, false, false, false};
    private ImageView[] mImageViews = new ImageView[4];
    private int lastPosition = -1;
    private int mOrder = -1;
    private String mOrd = "";
    private int mJumpType;

    /**
     * @param jumpType 1/3:保洁师 2：小时工 （必传）
     * @return
     */
    public static CleanerAndHourlyWorkerListFragment newInstance(int jumpType) {
        CleanerAndHourlyWorkerListFragment fragment = new CleanerAndHourlyWorkerListFragment();
        Bundle args = new Bundle();
        args.putInt(StringConstants.INTENT_PARAM_TYPE, jumpType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View loadViewLayout(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_jz_clean, container, false);
    }

    @Override
    protected void findViewById() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            mJumpType = bundle.getInt(StringConstants.INTENT_PARAM_TYPE, 1);
        }
        //保洁师列表
        mCleanRecyclerView = getView(R.id.id_jz_clean_list);
        mCleanRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        // 禁止RecyclerView滑动解决RecyclerView不显示问题
        mCleanRecyclerView.getxRecyclerView().setNestedScrollingEnabled(false);
        mCleanRecyclerView.getxRecyclerView().setItemAnimator(new DefaultItemAnimator());
        mCleanRecyclerView.setPullRefreshAndLoadingMoreEnabled(true, true);
        mCleanerAdapter = new CleanerAdapter(getContext(), mJumpType, R.layout.adapter_jz_clean_item);
        mCleanRecyclerView.setAdapter(mCleanerAdapter);
        // 头
        View mHeaerView = LayoutInflater.from(getContext()).inflate(R.layout.view_cleaner_header, null);
        //轮播
        mBannerView = (BannerView) mHeaerView.findViewById(R.id.id_jz_clean_banner);
        // tab
        mFirstLayout = (LinearLayout) mHeaerView.findViewById(R.id.id_first_layout);
        mSecondLayout = (LinearLayout) mHeaerView.findViewById(R.id.id_second_layout);
        mThirdtLayout = (LinearLayout) mHeaerView.findViewById(R.id.id_third_layout);
        mFourLayout = (LinearLayout) mHeaerView.findViewById(R.id.id_four_layout);
        mImageViews[0] = (ImageView) mHeaerView.findViewById(R.id.id_first_image);
        mImageViews[1] = (ImageView) mHeaerView.findViewById(R.id.id_second_image);
        mImageViews[2] = (ImageView) mHeaerView.findViewById(R.id.id_third_image);
        mImageViews[3] = (ImageView) mHeaerView.findViewById(R.id.id_four_image);
        mCleanRecyclerView.addHeaderView(mHeaerView);

    }

    @Override
    public void setListener() {
        mFirstLayout.setOnClickListener(this);
        mSecondLayout.setOnClickListener(this);
        mThirdtLayout.setOnClickListener(this);
        mFourLayout.setOnClickListener(this);
        // 保洁师列表item点击事件
        mCleanerAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener<Clean.DataBean>() {
            @Override
            public void onClick(int position, Clean.DataBean item) {
                if (mJumpType == 1 || mJumpType == 3) {
                    // 跳转到保洁师详情页
                    startActivity(CleanersOrHourlyWorkerDetailsActivity.newIntent(mContext, item.getId(), true));
                } else if (mJumpType == 2) {
                    // 小时工详情
                    startActivity(CleanersOrHourlyWorkerDetailsActivity.newIntent(mContext, item.getId(), false));
                }

            }
        });
        // 刷新和加载
        mCleanRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                mPage = 1;
                getNetData();
            }

            @Override
            public void onLoadMore() {
                mPage = mCleanerAdapter.getItemCount() / mCleanRecyclerView.loadSize + 1;
                getNetData();
            }
        });

    }

    @Override
    public void processLogic() {

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int i = v.getId();
        if (i == R.id.id_first_layout) {
            mOrder = 1;
            setArrowDirection(0, lastPosition);
            lastPosition = 0;

        } else if (i == R.id.id_second_layout) {
            mOrder = 3;
            setArrowDirection(1, lastPosition);
            lastPosition = 1;

        } else if (i == R.id.id_third_layout) {
            mOrder = 2;
            setArrowDirection(2, lastPosition);
            lastPosition = 2;

        } else if (i == R.id.id_four_layout) {
            mOrder = 4;
            setArrowDirection(3, lastPosition);
            lastPosition = 3;

        }
    }

    @Override
    public void refreshOnceData() {
        if (mJumpType == 1 || mJumpType == 3) {
            // 保洁师顶部banner
            ZLBannerUtils.getBannerDataAndSetData((BaseActivity) mActivity, mBannerView, "2");
        } else if (mJumpType == 2) {
            // 小时工列表顶部banner
            ZLBannerUtils.getBannerDataAndSetData((BaseActivity) mActivity, mBannerView, "5");
        }

        getNetData();
    }

    /**
     * @param positon      当前排序位置
     * @param lastPosition 上一次排序位置
     */
    private void setArrowDirection(int positon, int lastPosition) {
        for (int i = 0; i < mImageViews.length; i++) {
            if (positon == i) {
                if (isUpOrBottom[i]) {
                    mImageViews[i].setImageResource(R.mipmap.arrow_top_red);
                    isUpOrBottom[i] = false;
                    // 低到高
                    mOrd = "asc";
                } else {
                    mImageViews[i].setImageResource(R.mipmap.bottom_arrow_red);
                    isUpOrBottom[i] = true;
                    // 高到低
                    mOrd = "desc";
                }
            }
            // 归原
            if (positon != lastPosition && lastPosition == i) {
                mImageViews[i].setImageResource(R.mipmap.top_arrow_gray);
                isUpOrBottom[i] = false;
            }

        }
        // 加载第一页
        mPage = 1;
        getNetData();
    }

    /**
     * 获取保洁师集合
     */
    private void getNetData() {
        HashMap<String, String> map = new HashMap<>();
        // 小区id
        map.put(URLConstants.REQUEST_PARAM_COMMUNITY_ID, GlobalParams.communityId);
        map.put(URLConstants.REQUEST_PARAM_PAGE, String.valueOf(mPage));
        // 按什么排序 1：价格 2：销量 3：满意度 4：星级 0和不传默认
        if (mOrder != -1) {
            map.put(URLConstants.REQUEST_PARAM_ORDER, String.valueOf(mOrder));
        }
        // desc:高到低 asc:低到高
        if (!TextUtils.isEmpty(mOrd)) {
            map.put(URLConstants.REQUEST_PARAM_ORD, mOrd);
        }

        String url = null;
        if (mJumpType == 1 || mJumpType == 3) {
            // 保洁师
            url = URLConstants.JZ_CLEANING_PEOPLE;
        } else if (mJumpType == 2) {
            // 小时工
            url = URLConstants.JZ_CLEANING_HOUR_URL;
        }
        RequestManager.createRequest(url, map, new OnMyActivityRequestListener<Clean>((BaseActivity) getActivity()) {
            @Override
            public void onSuccess(Clean bean) {
                MyRecyclerView.handlerSuccessHasRefreshAndLoad(mCleanRecyclerView.getxRecyclerView(), mCleanerAdapter, mCleanRecyclerView.loadSize, mPage == 1, bean.getData());
            }

            @Override
            public void onFailed(boolean isError, String code, String message) {
                super.onFailed(isError, code, message);
                MyRecyclerView.handlerComplete(mCleanRecyclerView.getxRecyclerView());
            }
        });
    }

}
