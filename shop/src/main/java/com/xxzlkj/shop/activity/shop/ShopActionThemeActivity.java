package com.xxzlkj.shop.activity.shop;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xxzlkj.shop.R;
import com.xxzlkj.shop.adapter.ShopActionThemAdapter;
import com.xxzlkj.shop.adapter.ShopActionThemTitleAdapter;
import com.xxzlkj.shop.config.GlobalParams;
import com.xxzlkj.shop.config.StringConstants;
import com.xxzlkj.shop.config.URLConstants;
import com.xxzlkj.shop.event.DataChangEvent;
import com.xxzlkj.shop.event.ShopThemeAddShopCartEvent;
import com.xxzlkj.shop.model.ShopActionThemeBean;
import com.xxzlkj.shop.model.ShopActionThemeTitleBean;
import com.xxzlkj.shop.utils.AddShopCartUtil;
import com.xxzlkj.shop.utils.RecyclerViewHelperListener;
import com.xxzlkj.shop.utils.ShopCartNumberUtils;
import com.xxzlkj.zhaolinshare.base.base.MyBaseActivity;
import com.xxzlkj.zhaolinshare.base.net.OnMyActivityRequestListener;
import com.xxzlkj.zhaolinshare.base.net.RequestManager;
import com.xxzlkj.zhaolinshare.base.util.PicassoUtils;
import com.xxzlkj.zhaolinshare.base.util.PixelUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商城活动主题页
 */
public class ShopActionThemeActivity extends MyBaseActivity {
    private RecyclerView mTitleRecyclerView;
    private ImageView mBackImageView;
    private ImageView mShopCartImageView;
    private ImageView mMessageCenterImageView;
    private ImageView mArrowImageView;
    private ImageView mBannerView;
    private TextView mShopCartNumber;
    private LinearLayout mSearchLinearLayout;
    private LinearLayoutManager mContentLinearLayoutManager;
    private RecyclerView mContentRecyclerView;
    private AppBarLayout mAppBarLayout;
    private ShopActionThemTitleAdapter mTitleAdapter;
    private ShopActionThemAdapter mContentAdapter;
    private ShopActionThemeTitleBean shopActionThemeTitleCloseBean = new ShopActionThemeTitleBean("关闭", true);
    // 活动类型 1：商品活动 2:门店品牌活动详情
    private int mType;
    private boolean onScrollListenerEnable = true;
    private RecyclerViewHelperListener recyclerViewHelperListener;

    /**
     * @param type 活动类型 1：商品活动 2:门店品牌活动详情 3:主题活动详情  （必传）
     * @param id   活动id  （必传）
     */
    public static Intent newIntent(Context context, int type, String id) {
        Intent intent = new Intent(context, ShopActionThemeActivity.class);
        intent.putExtra(StringConstants.INTENT_PARAM_ID, id);
        intent.putExtra(StringConstants.INTENT_PARAM_TYPE, type);
        return intent;

    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_shop_action_theme);
    }

    @Override
    protected void findViewById() {
        EventBus.getDefault().register(this);
        // 标题头
        mShopCartNumber = getView(R.id.id_gl_number);
        mSearchLinearLayout = getView(R.id.id_gl_search);
        mBackImageView = getView(R.id.id_gl_back);
        mShopCartImageView = getView(R.id.id_gl_gouwuche);
        mMessageCenterImageView = getView(R.id.id_gl_lg);

        // banner图
        mBannerView = getView(R.id.bannerView);

        // tab头
        mAppBarLayout = getView(R.id.appBarLayout);
        mTitleRecyclerView = getView(R.id.recyclerView_title);
        mArrowImageView = getView(R.id.iv_arrow);
        // 内容
        mContentRecyclerView = getView(R.id.recyclerView_content);
        // 初始化头
        LinearLayoutManager mTitleLinearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        mTitleRecyclerView.setLayoutManager(mTitleLinearLayoutManager);
        mTitleRecyclerView.setNestedScrollingEnabled(false);
        mTitleAdapter = new ShopActionThemTitleAdapter(mContext, R.layout.item_shop_action_theme_title);
        mTitleRecyclerView.setAdapter(mTitleAdapter);
        // 初始化内容
        mContentLinearLayoutManager = new LinearLayoutManager(mContext);
        mContentRecyclerView.setLayoutManager(mContentLinearLayoutManager);
        recyclerViewHelperListener = new RecyclerViewHelperListener(mContentRecyclerView, mContentLinearLayoutManager);
        mContentRecyclerView.addOnScrollListener(recyclerViewHelperListener);
        mContentAdapter = new ShopActionThemAdapter(mContext, this, R.layout.adapter_shop_action_thme_item);
        mContentRecyclerView.setAdapter(mContentAdapter);
    }

    @Override
    protected void setListener() {
        // 点击了向右箭头
        mArrowImageView.setOnClickListener(v -> openTitles());
        // 跳转到搜索
        mSearchLinearLayout.setOnClickListener(v -> startActivity(HotSearchActivity.newIntent(mContext)));
        // 返回键
        mBackImageView.setOnClickListener(v -> finish());
        // 跳转到购物车
        mShopCartImageView.setOnClickListener(v -> startActivity(ShopCartActivity.newIntent(mContext)));
        // Title点击监听
        mTitleAdapter.setOnItemClickListener((position, item) -> {
            if (item.isClose()) {
                // 点击了关闭按钮
                closeTitles();
            } else {
                // 普通的

                // 滚动到此位置
                mTitleRecyclerView.scrollToPosition(position > mTitleAdapter.getSelectedPosition() ? position + 1 : (position == 0 ? 0 : position - 1));// 下滑预览一，上滑动预览一
                // 设置选中
                mTitleAdapter.setSelectedPosition(position);
                mTitleAdapter.notifyDataSetChanged();
                // 设置头部关闭
                mAppBarLayout.setExpanded(false);
                // 设置滚动，点击滚动的时候，不再监听滚动
                onScrollListenerEnable = false;
                recyclerViewHelperListener.scrollToPosition(position);
                mAppBarLayout.postDelayed(() -> onScrollListenerEnable = true, 500);

            }

        });
        // 滚动监听
        mContentRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int firstItem = mContentLinearLayoutManager.findFirstVisibleItemPosition();
                if (mTitleAdapter.getSelectedPosition() != firstItem && onScrollListenerEnable) {
                    // 滚动到此位置
                    mTitleRecyclerView.scrollToPosition(dy > 0 ? firstItem + 1 : (firstItem == 0 ? 0 : firstItem - 1));// 下滑预览一，上滑动预览一
                    // 设置选中
                    mTitleAdapter.setSelectedPosition(firstItem);
                    mTitleAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void closeTitles() {
        mArrowImageView.setVisibility(View.VISIBLE);
        mTitleRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        mTitleAdapter.removeItem(shopActionThemeTitleCloseBean);
        mTitleRecyclerView.scrollToPosition(mTitleAdapter.getSelectedPosition());
    }

    private void openTitles() {
        mArrowImageView.setVisibility(View.GONE);
        mTitleRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 4));
        mTitleAdapter.addItemAtLast(shopActionThemeTitleCloseBean);
    }

    @Override
    protected void processLogic() {
        Intent intent = getIntent();
        String id = intent.getStringExtra(StringConstants.INTENT_PARAM_ID);
        mType = intent.getIntExtra(StringConstants.INTENT_PARAM_TYPE, 1);
        // 设置购物车数量
        ShopCartNumberUtils.getShopCartNumber(ShopActionThemeActivity.this, mShopCartNumber);
        // 获取数据
        getNetData(id);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 获取商品列表
     */
    private void getNetData(String id) {
        Map<String, String> map = new HashMap<>();
        String url = null;
        switch (mType) {
            case 1:// 品牌活动详情
                url = URLConstants.BRAND_DETAILS_URL;
                map.put(URLConstants.REQUEST_PARAM_ID, id);
                break;
            case 2:// 门店品牌活动
                url = URLConstants.BRAND_DETAILS_STORE_URL;
                map.put(URLConstants.REQUEST_PARAM_ID, id);
                break;
            case 3:// 主题活动详情
                url = URLConstants.ADVANCE_ACT_DETAIL_URL;
                map.put(URLConstants.REQUEST_PARAM_ID, id);
                map.put(URLConstants.REQUEST_PARAM_STORE_ID, TextUtils.isEmpty(GlobalParams.storeId) ? "0" : GlobalParams.storeId);
                break;
        }

        RequestManager.createRequest(url, map, new OnMyActivityRequestListener<ShopActionThemeBean>(this) {
            @Override
            public void onSuccess(ShopActionThemeBean bean) {
                // banner图
                ShopActionThemeBean.DataBean data = bean.getData();
                int width = PixelUtil.getScreenWidth(ShopActionThemeActivity.this);
                int height = (int) ((1.0f * width / 750) * 300);
                String simg = data.getSimg();
                if (TextUtils.isEmpty(simg)) {
                    // 没有图片不显示
                    mBannerView.setVisibility(View.GONE);
                } else {
                    // 有图片显示
                    mBannerView.setVisibility(View.VISIBLE);
                    PicassoUtils.setWithAndHeight(mContext, simg, width, height, mBannerView);
                }
                List<ShopActionThemeBean.DataBean.BrandBean> brand = data.getBrand();
                // 增加标题
                ArrayList<ShopActionThemeTitleBean> titles = new ArrayList<>();
                for (ShopActionThemeBean.DataBean.BrandBean brandBean : brand) {
                    titles.add(new ShopActionThemeTitleBean(brandBean.getTitle(), false));
                }
                mTitleAdapter.setSelectedPosition(0);// 设置默认选中第一个
                mTitleAdapter.addList(titles);
                // 增加内容
                mContentAdapter.addList(brand);

            }
        });
    }

    /**
     * 添加购物车
     */
    @SuppressWarnings("unused")
    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void addShopCart(ShopThemeAddShopCartEvent event) {
        String id = event.getId();
        if (!TextUtils.isEmpty(id)) {
            AddShopCartUtil.addShopCart(ShopActionThemeActivity.this, event.getView(), mShopCartNumber, id);
        }

    }

    /**
     * 购物车数量
     */
    @SuppressWarnings("unused")
    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void updateShopCart(DataChangEvent event) {
        ShopCartNumberUtils.getShopCartNumber(this, mShopCartNumber);
    }

}
