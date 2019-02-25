package com.xxzlkj.licallife.activity.localLife;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.xxzlkj.licallife.R;
import com.xxzlkj.licallife.adapter.DrawerListAdapter;
import com.xxzlkj.licallife.adapter.NannyAndMaternityMatronListAdapter;
import com.xxzlkj.licallife.adapter.PlaceListAdapter;
import com.xxzlkj.licallife.adapter.SortAdapter;
import com.xxzlkj.licallife.adapter.TreatmentListAdapter;
import com.xxzlkj.licallife.model.FilterBean;
import com.xxzlkj.licallife.model.NannyAndMaternityMatronListBean;
import com.xxzlkj.licallife.model.PlaceListBean;
import com.xxzlkj.licallife.model.SortBean;
import com.xxzlkj.licallife.utils.ZLBannerUtils;
import com.xxzlkj.shop.config.GlobalParams;
import com.xxzlkj.shop.config.URLConstants;
import com.xxzlkj.shop.weight.BannerView;
import com.xxzlkj.zhaolinshare.base.MyRecyclerView;
import com.xxzlkj.zhaolinshare.base.base.BaseActivity;
import com.xxzlkj.zhaolinshare.base.net.OnMyActivityRequestListener;
import com.xxzlkj.zhaolinshare.base.net.RequestManager;
import com.xxzlkj.zhaolinshare.base.util.BasePopupWindow;
import com.xxzlkj.zhaolinshare.base.util.PixelUtil;
import com.xxzlkj.zhaolinshare.base.util.SystemBarUtils;
import com.xxzlkj.zhaolinshare.base.util.ToastManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 保姆、月嫂列表
 */
public class NannyAndMaternityMatronListActivity extends BaseActivity {
    public static final String IS_NANNY = "isNanny";
    private MyRecyclerView mContentRecyclerView;
    private RecyclerView mDrawerRecyclerView;
    private BannerView mBannerView;
    private TextView mDrawerCancelTextView, mDrawerConfigTextView;
    private DrawerLayout mDrawerLayout;
    private AppBarLayout mAppBarLayout;
    private View mButtons;
    private NannyAndMaternityMatronListAdapter mContentAdapter;
    private boolean isNanny;
    private int pageNo = 1;
    private BasePopupWindow popupWindow;
    private TextView button1TextView, button2TextView, button3TextView, button4TextView;
    private RecyclerView recyclerView_pop;
    private SortAdapter sortAdapter;
    private FilterBean filterBean;
    private DrawerListAdapter mDrawerAdapter;
    private Map<String, String> filterRequestParams = new HashMap<>();
    private PlaceListAdapter placeListAdapter;
    private PlaceListBean placeListBean;
    private TreatmentListAdapter treatmentListAdapter;
    private FilterBean treatmentListBean;
    private View button1, button2, button3, button4;
    private TextView tv_cancel;
    private TextView tv_cancel1;
    private TextView tv_config;

    /**
     * @param isNanny 是否是保姆列表页面，true为保姆页面，false为月嫂列表
     */
    public static Intent newIntent(Context context, boolean isNanny) {
        Intent intent = new Intent(context, NannyAndMaternityMatronListActivity.class);
        intent.putExtra(IS_NANNY, isNanny);
        return intent;
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_nanny_and_maternity_matron_list);
        SystemBarUtils.setStatusBarTranslucent(this);
        SystemBarUtils.setStatusBarLightMode(this, true);
    }

    @Override
    protected void findViewById() {
        mDrawerLayout = getView(R.id.drawerLayout);// 侧滑布局
        mAppBarLayout = getView(R.id.appBarLayout);// 头布局
        mBannerView = getView(R.id.bannerView);// 轮播图
        mButtons = getView(R.id.ll_buttons);// 分类按钮集合
        button1 = getView(R.id.rl_button_1);
        button1TextView = getView(R.id.tv_button_1);
        button2 = getView(R.id.rl_button_2);
        button2TextView = getView(R.id.tv_button_2);
        button3 = getView(R.id.rl_button_3);
        button3TextView = getView(R.id.tv_button_3);
        button4 = getView(R.id.rl_button_4);
        button4TextView = getView(R.id.tv_button_4);

        mContentRecyclerView = getView(R.id.recyclerView);// 列表内容
        mDrawerRecyclerView = getView(R.id.rv_drawer);// 侧滑内容
        mDrawerCancelTextView = getView(R.id.tv_cancel);// 侧滑取消按钮
        mDrawerConfigTextView = getView(R.id.tv_config);// 侧滑确认按钮
        // 初始化
        SystemBarUtils.setPaddingTopStatusBarHeight(this, getView(R.id.titleBar));
        // 内容
        mContentRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mContentAdapter = new NannyAndMaternityMatronListAdapter(mContext, R.layout.item_nanny_and_maternity_matron_list);
        mContentRecyclerView.setAdapter(mContentAdapter);
        // 侧滑列表
        mDrawerRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mDrawerAdapter = new DrawerListAdapter(this, R.layout.item_nanny_and_maternity_matron_drawer);
        mDrawerRecyclerView.setAdapter(mDrawerAdapter);
    }

    @Override
    protected void setListener() {
        mContentRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                pageNo = 1;
                getNetData();
            }

            @Override
            public void onLoadMore() {
                pageNo = mContentAdapter.getItemCount() / mContentRecyclerView.loadSize + 1;
                getNetData();
            }
        });


        // 按钮1点击事件（综合排序）
        button1.setOnClickListener(v -> closeTitleAndShowPopupWindow(1));
        // 按钮2点击事件（籍贯）
        button2.setOnClickListener(v -> closeTitleAndShowPopupWindow(2));
        // 按钮3点击事件（待遇）
        button3.setOnClickListener(v -> closeTitleAndShowPopupWindow(3));
        // 按钮4点击事件（筛选）
        button4.setOnClickListener(v -> {
            // 设置popWindow关闭
            if (popupWindow != null) {
                popupWindow.dismiss();
            }
            setTitleSelect(4);// 设置选中
            mAppBarLayout.setExpanded(false);
            mDrawerLayout.openDrawer(Gravity.END);
            if (filterBean == null) {
                // 没获取到数据，重新获取
                getFilterData();
            } else {
                setFilterData(filterBean);
            }
        });
        // 列表的Item的点击事件,跳到详情
        mContentAdapter.setOnItemClickListener((position, item) -> startActivity(NannyAndMaternityMatronDesActivity.newIntent(mContext, isNanny, item.getId())));


        // 侧滑菜单的取消按钮点击
        mDrawerCancelTextView.setOnClickListener(v -> mDrawerLayout.closeDrawer(Gravity.END));
        // 侧滑菜单的确定按钮点击
        mDrawerConfigTextView.setOnClickListener(v -> {
            // 确定按钮，关闭侧滑，保存数据
            // 关闭
            mDrawerLayout.closeDrawer(Gravity.END);
            // 保存参数
            mDrawerAdapter.addSelectToRequestParams();
            // 拼接参数
            filterRequestParams.putAll(mDrawerAdapter.getRequestParams());
            // 获取网络
            getNetData();
        });
        // 侧滑监听
        mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                // 检查筛选
                setTitleSelect(4);
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
    }


    @Override
    protected void processLogic() {
        if (TextUtils.isEmpty(GlobalParams.communityId) || !GlobalParams.isOpenLocalLife){
            ToastManager.showShortToast(mContext, getString(R.string.noLocalLifeServiceHint));
            finish();
        }else {
            isNanny = getIntent().getBooleanExtra(IS_NANNY, false);// 是否是保姆列表
            setTitleLeftBack();
            setTitleName(isNanny ? "保姆" : "月嫂");

            // 获取轮播图数据
            ZLBannerUtils.getBannerDataAndSetData(this, mBannerView, isNanny ? "4" : "3");

            // 获取籍贯信息
            getPlaceNetData(false);
            // 获取待遇信息
            getTreatmentNetData(false);
            // 获取筛选信息
            getFilterData();

            // 获取列表数据
            // 默认选择综合排序的
            filterRequestParams.put("order", "5");
            setTitleSelect(1);
            getNetData();
        }
    }

    /**
     * 获取通用信息接口
     */
    private void getNetData() {
        Map<String, String> map = new HashMap<>();
        // 小区id
        map.put(URLConstants.REQUEST_PARAM_COMMUNITY_ID, GlobalParams.communityId);
//        page	分页 一页20条
        map.put("page", pageNo + "");
        // 侧滑筛选
        map.putAll(filterRequestParams);
        RequestManager.createRequest(isNanny ? URLConstants.JZ_CLEANING_BAOMU_URL : URLConstants.JZ_CLEANING_YUESAO_URL, map, new OnMyActivityRequestListener<NannyAndMaternityMatronListBean>(this) {
            @Override
            public void onSuccess(NannyAndMaternityMatronListBean bean) {
                mContentRecyclerView.handlerSuccessHasRefreshAndLoad(mContentAdapter, pageNo == 1, bean.getData());
            }

            @Override
            public void onFailed(boolean isError, String code, String message) {
                super.onFailed(isError, code, message);
                mContentRecyclerView.handlerError(mContentAdapter, pageNo == 1);
            }
        });
    }

    /**
     * 获取籍贯列表
     */
    private void getPlaceNetData(boolean isSetData) {
        Map<String, String> map = new HashMap<>();
        RequestManager.createRequest(isNanny ? URLConstants.JZ_CLEANING_BAOMU_PLACE_URL : URLConstants.JZ_CLEANING_YUESAO_PLACE_URL, map, new OnMyActivityRequestListener<PlaceListBean>(this) {
            @Override
            public void onSuccess(PlaceListBean bean) {
                placeListBean = bean;
                if (isSetData) {
                    // 设置数据
                    setPlaceData(placeListBean);
                }
            }

            @Override
            public void onError(String code, String message) {

            }

        });
    }

    /**
     * 获取待遇列表
     */
    private void getTreatmentNetData(boolean isSetData) {
        Map<String, String> map = new HashMap<>();
        RequestManager.createRequest(isNanny ? URLConstants.JZ_CLEANING_BAOMU_TREATMENT_URL : URLConstants.JZ_CLEANING_YUESAO_TREATMENT_URL, map, new OnMyActivityRequestListener<FilterBean>(this) {
            @Override
            public void onSuccess(FilterBean bean) {
                treatmentListBean = bean;
                if (isSetData) {
                    // 设置数据
                    setTreatmentData(bean);
                }
            }

            @Override
            public void onError(String code, String message) {

            }
        });
    }

    /**
     * 获取筛选侧滑列表
     */
    private void getFilterData() {
        Map<String, String> map = new HashMap<>();
        RequestManager.createRequest(isNanny ? URLConstants.JZ_CLEANING_BAOMU_FILTER_URL : URLConstants.JZ_CLEANING_YUESAO_FILTER_URL, map, new OnMyActivityRequestListener<FilterBean>(this) {
            @Override
            public void onSuccess(FilterBean bean) {
                filterBean = bean;
                setFilterData(bean);
            }

            @Override
            public void onError(String code, String message) {

            }
        });
    }


    /**
     * 关闭并展示PopupWindow
     */
    private void closeTitleAndShowPopupWindow(int flag) {
        setTitleSelect(flag);
        mAppBarLayout.setExpanded(false);
        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (Math.abs(verticalOffset) == appBarLayout.getTotalScrollRange()) {
                    // 关闭的
                    showPopupWindow(flag);
                    mAppBarLayout.removeOnOffsetChangedListener(this);
                }
            }
        });
//        mAppBarLayout.postDelayed(() -> showPopupWindow(flag), isClosed ? 0 : 300);// 没展开，先关闭，延时后再展示
    }

    private void setTitleSelect(int position) {
        // 按钮1 （综合排序）
        button1.setSelected(position == 1);
        // 按钮2 （籍贯）
        button2.setSelected(position == 2);
        // 按钮3 （待遇）
        button3.setSelected(position == 3);
        // 按钮4 （筛选）
        button4.setSelected(position == 4);

        // 综合排序，有两种状态：选中（红色上箭头）、未选中（灰色下箭头）
        setRightDrawable(button1TextView, position == 1 ? R.mipmap.ic_top_red : R.mipmap.ic_down_gray);
        // 籍贯，有三种状态：已选择（红色对号）、选中（红色上箭头）、未选中
        if (placeListAdapter != null && placeListAdapter.getRequestParamsCount() > 0) {
            // 已选择（都是ok）
            setRightDrawable(button2TextView, R.mipmap.ic_down_ok);
        } else {
            // 未选择（未选择选中红色，未选择未选中灰色）
            setRightDrawable(button2TextView, position == 2 ? R.mipmap.ic_top_red : R.mipmap.ic_down_gray);
        }

        // 待遇，有三种状态：已选择（红色对号）、选中（红色上箭头）、未选中
        if (treatmentListAdapter != null && treatmentListAdapter.getRequestParamsCount() > 0) {
            // 已选择（都是ok）
            setRightDrawable(button3TextView, R.mipmap.ic_down_ok);
        } else {
            // 未选择（未选择选中红色，未选择未选中灰色）
            setRightDrawable(button3TextView, position == 3 ? R.mipmap.ic_top_red : R.mipmap.ic_down_gray);
        }

        // 筛选，有三种状态：已选择（红色对号）、选中（红色上箭头）、未选中
        if (mDrawerAdapter != null && mDrawerAdapter.getRequestParamsCount() > 0) {
            // 已选择（都是ok）
            setRightDrawable(button4TextView, R.mipmap.ic_down_ok);
        } else {
            // 未选择（未选择选中红色，未选择未选中灰色）
            setRightDrawable(button4TextView, position == 4 ? R.mipmap.ic_down_red : R.mipmap.ic_down_gray);
        }
    }

    /**
     * 展示popWindow
     */
    private void showPopupWindow(int flag) {
        if (popupWindow == null) {
            View rootView = View.inflate(mContext, R.layout.popup_wiondow_nanny_and_maternity_matron, null);
            recyclerView_pop = (RecyclerView) rootView.findViewById(R.id.recyclerView_pop);
            tv_cancel1 = (TextView) rootView.findViewById(R.id.tv_cancel);// 取消按钮
            tv_config = (TextView) rootView.findViewById(R.id.tv_config);// 确定按钮
            recyclerView_pop.setLayoutManager(new LinearLayoutManager(mContext));
            popupWindow = new BasePopupWindow(rootView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
            popupWindow.setFocusable(false);
            popupWindow.setTouchable(true);
            popupWindow.setAnimationStyle(0);
            popupWindow.setOutsideTouchable(false);// 点击外面能不能关闭
            popupWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#66000000")));
            // 其他区域点击消失
            rootView.setOnClickListener(v -> popupWindow.dismiss());
            // 取消按钮
            tv_cancel1.setOnClickListener(v -> popupWindow.dismiss());
        }
        // 消失监听（状态关闭）
        popupWindow.setOnDismissListener(() -> {
            switch (flag) {
                case 1:
                    // 综合排序
                    setRightDrawable(button1TextView, R.mipmap.ic_down_red);
                    break;
                case 2:
                    // 籍贯（检查是确定选中了：选中了：红色对勾；没选中：红色向下箭头）
                    setRightDrawable(button2TextView, placeListAdapter.getRequestParamsCount() > 0 ? R.mipmap.ic_down_ok : R.mipmap.ic_down_red);
                    break;
                case 3:
                    // 待遇（规则同上）
                    setRightDrawable(button3TextView, treatmentListAdapter.getRequestParamsCount() > 0 ? R.mipmap.ic_down_ok : R.mipmap.ic_down_red);
                    break;
            }
        });
        // 确定按钮
        tv_config.setOnClickListener(v -> {
            switch (flag) {
                case 1:
                    // 综合排序
                    SortBean sortSelectItemBean = sortAdapter.getSelectItemBean();
                    filterRequestParams.put("order", sortSelectItemBean.getOrder());
                    // 设置头内容
                    button1TextView.setText(sortSelectItemBean.getTitle());

                    break;
                case 2:
                    // 籍贯（规则同上）
                    placeListAdapter.addSelectToRequestParams();// 增加到确认的请求参数里
                    Map<String, String> placeRequestParams = placeListAdapter.getRequestParams();
                    filterRequestParams.putAll(placeRequestParams);// 网络请求的时候会过滤值为null的
                    break;
                case 3:
                    // 待遇
                    treatmentListAdapter.addSelectToRequestParams();// 增加到确认的请求参数里
                    Map<String, String> treatmentRequestParams = treatmentListAdapter.getRequestParams();
                    filterRequestParams.putAll(treatmentRequestParams);// 网络请求的时候会过滤值为null的
                    break;
            }
            popupWindow.dismiss();
            // 获取网络
            getNetData();

        });

        // 归原
        ViewGroup.LayoutParams layoutParams = recyclerView_pop.getLayoutParams();
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        // 设置数据
        switch (flag) {
            case 1:
                // 综合排序
                if (sortAdapter == null) {
                    sortAdapter = new SortAdapter(mContext, R.layout.item_pop_sort);
                    // 点击
                    sortAdapter.setOnItemClickListener((position, item) -> {
                        // 设置选中
                        sortAdapter.setSelectPosition(position);
                        sortAdapter.notifyDataSetChanged();
                    });
                }
                recyclerView_pop.setAdapter(sortAdapter);
                // 设置数据，状态归原
                List<SortBean> list = new ArrayList<>();
                list.add(new SortBean("综合排序", "5"));
                list.add(new SortBean("好评率", "6"));
                list.add(new SortBean("在岗天数", "1"));
                list.add(new SortBean("期望月薪", "2"));
                list.add(new SortBean("工作经验", "3"));
                list.add(new SortBean("更新时间", "4"));
                sortAdapter.clearAndAddList(list);// 里面已经保存了已经选中的位置了，所以就会被选中
                break;
            case 2:
                // 籍贯
                layoutParams.height = PixelUtil.dip2px(mContext, 250);
                placeListAdapter = new PlaceListAdapter(mContext, R.layout.item_nanny_and_maternity_matron_drawer);
                recyclerView_pop.setAdapter(placeListAdapter);
                // 设置数据
                if (placeListBean == null) {
                    // 网络获取后设置
                    getPlaceNetData(true);
                } else {
                    // 直接设置
                    setPlaceData(placeListBean);
                }

                break;
            case 3:
                // 待遇
                treatmentListAdapter = new TreatmentListAdapter(mContext, R.layout.item_nanny_and_maternity_matron_drawer);
                recyclerView_pop.setAdapter(treatmentListAdapter);
                // 设置数据
                if (treatmentListBean == null) {
                    // 网络获取后设置
                    getTreatmentNetData(true);
                } else {
                    setTreatmentData(treatmentListBean);
                }

                break;
        }
        recyclerView_pop.setLayoutParams(layoutParams);
        // 展示
        popupWindow.showAsDropDown(mButtons);
    }

    private void setFilterData(FilterBean bean) {
        mDrawerAdapter.clearAndAddList(bean.getData());
//        // 设置默认选中
//        mDrawerAdapter.setDefaultRequestParams(bean.getData());
        // 设置之前的选中
        mDrawerAdapter.setSelectedByRequestParams(filterRequestParams);
        mDrawerAdapter.addSelectToRequestParams();// 增加到确认的请求参数里
        mDrawerAdapter.notifyDataSetChanged();
    }

    private void setTreatmentData(FilterBean treatmentListBean) {
        // 直接设置
        treatmentListAdapter.clearAndAddList(treatmentListBean.getData());
        // 设置之前的选中
        treatmentListAdapter.setSelectedByRequestParams(filterRequestParams);
        treatmentListAdapter.addSelectToRequestParams();// 增加到确认的请求参数里
        treatmentListAdapter.notifyDataSetChanged();
    }

    private void setPlaceData(PlaceListBean placeListBean) {
        // 设置全国
        List<PlaceListBean.DataBean.PlaceBean> place = placeListBean.getData().getPlace();
        PlaceListBean.DataBean.Place2Bean topBean = new PlaceListBean.DataBean.Place2Bean("全国", place);
        placeListAdapter.clear();
        placeListAdapter.addItem(0, topBean);
        // 设置其它
        placeListAdapter.addList(placeListBean.getData().getPlace2());
        // 设置之前的选中
        placeListAdapter.setSelectedByRequestParams(filterRequestParams);
        placeListAdapter.addSelectToRequestParams();// 增加到确认的请求参数里
        placeListAdapter.notifyDataSetChanged();
    }

    private void setRightDrawable(TextView textView, int resId) {
        Drawable right = ContextCompat.getDrawable(mContext, resId);
        right.setBounds(0, 0, right.getIntrinsicWidth(), right.getIntrinsicHeight());
        textView.setCompoundDrawables(null, null, right, null);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(Gravity.END))
            mDrawerLayout.closeDrawer(Gravity.END);
        else
            super.onBackPressed();

    }
}
