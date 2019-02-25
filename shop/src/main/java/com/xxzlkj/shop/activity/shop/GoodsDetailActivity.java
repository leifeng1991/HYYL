package com.xxzlkj.shop.activity.shop;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xxzlkj.shop.R;
import com.xxzlkj.shop.adapter.GoodsImageAdapter;
import com.xxzlkj.shop.adapter.SpelledGroupAdapter;
import com.xxzlkj.shop.config.GlobalParams;
import com.xxzlkj.shop.config.StringConstants;
import com.xxzlkj.shop.config.URLConstants;
import com.xxzlkj.shop.event.DataChangEvent;
import com.xxzlkj.shop.event.GoodsDetailEvent;
import com.xxzlkj.shop.model.AddGrouponItemBean;
import com.xxzlkj.shop.model.GoodsDetail;
import com.xxzlkj.shop.model.ShopCartList;
import com.xxzlkj.shop.model.TeamBean;
import com.xxzlkj.shop.model.TimeBean;
import com.xxzlkj.shop.utils.ShopCartNumberUtils;
import com.xxzlkj.shop.utils.TextPriceUtil;
import com.xxzlkj.shop.utils.ZLDialogUtil;
import com.xxzlkj.shop.weight.BannerView;
import com.xxzlkj.shop.weight.CustomButton;
import com.xxzlkj.shop.weight.CustomPopupWindow;
import com.xxzlkj.zhaolinshare.base.app.BaseApplication;
import com.xxzlkj.zhaolinshare.base.base.BaseActivity;
import com.xxzlkj.zhaolinshare.base.base.BaseAdapter;
import com.xxzlkj.zhaolinshare.base.model.BaseBean;
import com.xxzlkj.zhaolinshare.base.model.User;
import com.xxzlkj.zhaolinshare.base.net.OnBaseRequestListener;
import com.xxzlkj.zhaolinshare.base.net.OnMyActivityRequestListener;
import com.xxzlkj.zhaolinshare.base.net.RequestManager;
import com.xxzlkj.zhaolinshare.base.util.LogUtil;
import com.xxzlkj.zhaolinshare.base.util.NumberFormatUtils;
import com.xxzlkj.zhaolinshare.base.util.PixelUtil;
import com.xxzlkj.zhaolinshare.base.util.TextViewUtils;
import com.xxzlkj.zhaolinshare.base.util.TimeUtil;
import com.xxzlkj.zhaolinshare.base.util.ToastManager;
import com.zrq.spanbuilder.Spans;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 商品详情
 */
public class GoodsDetailActivity extends BaseActivity {
    private RelativeLayout mToolbar;
    private ImageView leftBackImage, leftBackImages, mShopCartImageView, mShareImageView,
            mCollectImageView, mShareImageViews, mCollectImageViews, mChatImageView;
    private String mId;
    private BannerView mBannder;
    private TextView mTitle, mAds, mPrice, mGouWuCheNumber, mNowBuy, mAddGouwuche, mKown, arrivalNotice,
            mAdvancePriceTextView, mAdvancePricesTextView, mAdvanceDayTextView, mAdvanceDescTextView,
            mTuanGouTextView, mPricesTextView, mTuanGouNumberTextView, mPurchaseSeparatelyTextView,
            mGroupPurchaseTextView, mPurchaseSeparatelyTextView1, mGroupPurchaseTextView1;
    private RecyclerView mImagesList, mTuanGouRecyclerView;
    private CustomButton mAdvanceHourTextView, mAdvanceMinuteTextView, mAdvanceSecondTextView;
    // 表格
    private LinearLayout mTableLayout, mTableLayoutTip, mKownLayout, mAdvanceLayout, mPriceLayout, mTuanGouLinearLayout, mTuanGouMoreLinearLayout;
    private GoodsDetail mGoodsDetail;
    private User mLoginUser;
    //距离top高度
    private int statusHeight;
    //心 样式
    private boolean isHeartFlag = true;
    // true:透明度从1-0 false:透明度从0-1
    private boolean isAlphaShow = true;
    // 属性选择
    private CustomPopupWindow mCustomPopupWindow;
    // 库存数
    private String stock;
    // 门店id
    private String mStoreId;
    private CountDownTimer countDownTimer;
    private SpelledGroupAdapter mSpelledGroupAdapter;

    /**
     * @param context 上下文 （必传）
     * @param id      商品id （必传）
     * @return
     */
    public static Intent newIntent(Context context, String id) {
        Intent intent = new Intent(context, GoodsDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(StringConstants.INTENT_PARAM_ID, id);
        bundle.putString(StringConstants.INTENT_PARAM_STOREID, GlobalParams.storeId);
        intent.putExtras(bundle);
        return intent;
    }

    /**
     * @param context 上下文 （必传）
     * @param id      商品id （必传）
     * @param storeId 门店id （必传）
     * @return
     */
    public static Intent newIntent(Context context, String id, String storeId) {
        Intent intent = new Intent(context, GoodsDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(StringConstants.INTENT_PARAM_ID, id);
        bundle.putString(StringConstants.INTENT_PARAM_STOREID, storeId);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void loadViewLayout() {
        //设置无标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_goods_detail);
        statusHeight = PixelUtil.dip2px(this, 25);
    }

    @Override
    protected void findViewById() {
        mLoginUser = BaseApplication.getInstance().getLoginUser();
        if (getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            mId = bundle.getString(StringConstants.INTENT_PARAM_ID);
//            mId = "9508193";
            mStoreId = bundle.getString(StringConstants.INTENT_PARAM_STOREID);
            LogUtil.e(TAG, mId);
        }
        // 标题栏
        mToolbar = getView(R.id.id_mtl_toolbar);
        mToolbar.setBackgroundColor(Color.argb(0, 250, 251, 253));// fafbfd

        mToolbar.setPadding(0, statusHeight / 2, 0, statusHeight / 2);

        leftBackImage = getView(R.id.id_left_image);
        leftBackImages = getView(R.id.id_left_images);

        // 轮播
        mBannder = getView(R.id.id_gd_banner);
        int width = PixelUtil.getScreenWidth(this);
        mBannder.setWidthAndHeight(width, width);

        mTitle = getView(R.id.id_gd_title);// 标题
        mAds = getView(R.id.id_gd_ads);// 广告语
        mPrice = getView(R.id.id_gd_price);// 商品现价
        mGouWuCheNumber = getView(R.id.id_gd_number);// 购物车数量
        mNowBuy = getView(R.id.id_gd_buy_now);// 立即购买
        mAddGouwuche = getView(R.id.id_gd_add_gwc);// 加入购物车
        mShopCartImageView = getView(R.id.id_gd_gwc);// 购物车
        mChatImageView = getView(R.id.id_gd_chat);// 聊天
        mShareImageView = getView(R.id.id_right_image_1);// 分享
        mShareImageViews = getView(R.id.id_right_images_1);
        mCollectImageView = getView(R.id.id_right_image_2);// 收藏
        mCollectImageViews = getView(R.id.id_right_images_2);
        mTableLayout = getView(R.id.id_gd_table);// 规格表
        mKownLayout = getView(R.id.id_gd_konw_layout);
        mTableLayoutTip = getView(R.id.id_gd_table_tip);
        mKown = getView(R.id.id_gd_konw_text);
        arrivalNotice = getView(R.id.id_gd_arrival_notice);
        // 图片列表
        mImagesList = getView(R.id.id_gd_image_list);
        mImagesList.setLayoutManager(new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }

            @Override
            public boolean canScrollHorizontally() {
                return false;
            }
        });
        // 禁止RecyclerView滑动，解决ScrollView嵌套RecyclerView不显示问题
        mImagesList.setNestedScrollingEnabled(false);

        mAdvanceLayout = getView(R.id.id_advance_layout);// 预售布局
        mAdvancePriceTextView = getView(R.id.id_advance_price);// 预售价格
        mAdvancePricesTextView = getView(R.id.id_advance_prices);// 预售原价格
        mAdvanceDayTextView = getView(R.id.id_advance_day);// 预售剩余天
        mAdvanceHourTextView = getView(R.id.id_advance_hour);// 预售剩余时
        mAdvanceMinuteTextView = getView(R.id.id_advance_minute);// 预售剩余分
        mAdvanceSecondTextView = getView(R.id.id_advance_second);// 预售剩余秒
        mAdvanceDescTextView = getView(R.id.id_advance_desc);// 预售描述
        mPriceLayout = getView(R.id.id_price_layout);// 价格布局
        mTuanGouTextView = getView(R.id.id_tuangou);// 团购
        mPricesTextView = getView(R.id.id_prices);// 团购
        mTuanGouLinearLayout = getView(R.id.id_tuangou_layout);// 团购布局
        mTuanGouMoreLinearLayout = getView(R.id.id_tuangou_more);// 查看更多
        mTuanGouNumberTextView = getView(R.id.id_tuangou_number);// 正在参团人数
        mTuanGouRecyclerView = getView(R.id.id_tuangou_recycler);// 参团列表 最多显示两个
        mTuanGouRecyclerView.setLayoutManager(new LinearLayoutManager(mContext) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }

            @Override
            public boolean canScrollHorizontally() {
                return false;
            }
        });
        mSpelledGroupAdapter = new SpelledGroupAdapter(mContext, this, R.layout.adapter_spelled_group_list_item);
        mTuanGouRecyclerView.setAdapter(mSpelledGroupAdapter);
        mPurchaseSeparatelyTextView = getView(R.id.id_purchase_separately);// 单独购买
        mGroupPurchaseTextView = getView(R.id.id_group_purchase);// 发起团购
        mPurchaseSeparatelyTextView1 = getView(R.id.id_purchase_separately1);// 单独购买
        mGroupPurchaseTextView1 = getView(R.id.id_group_purchase1);// 发起团购


        //标题栏背景变化
        NestedScrollView mNestedScrollView = getView(R.id.id_gd_scroll);
        mNestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY <= 0) {
                    // 改变标题栏透明度
                    mToolbar.setBackgroundColor(Color.argb(0, 250, 251, 253));// fafbfd
                    // 设置标题栏 左右图片
                    leftBackImage.setImageResource(R.mipmap.back_slod);
                    leftBackImages.setImageResource(R.mipmap.back_arrow);
                    mShareImageView.setImageResource(R.mipmap.share_gray_icon);
                    mShareImageViews.setImageResource(R.mipmap.share_icon);
                    setCollectIcon(true);
                    isHeartFlag = true;
                    isAlphaShow = true;
                    setTitleAlpha(1);
                } else if (scrollY > 0 && scrollY <= mToolbar.getHeight()) {
                    // 透明度值
                    float y = 1.0f * scrollY / mToolbar.getHeight();
                    // 改变标题栏透明度
                    mToolbar.setBackgroundColor(Color.argb((int) (y * 255), 250, 251, 253));// fafbfd

                    if (isAlphaShow) {// 透明度值变化
                        y = 1 - y;
                    }
                    setTitleAlpha(y);
                } else {
                    // 改变标题栏透明度
                    mToolbar.setBackgroundColor(Color.argb(255, 250, 251, 253));// fafbfd
                    // 设置标题栏 左右图片
                    leftBackImage.setImageResource(R.mipmap.back_arrow);
                    leftBackImages.setImageResource(R.mipmap.back_slod);
                    mShareImageView.setImageResource(R.mipmap.share_icon);
                    mShareImageViews.setImageResource(R.mipmap.share_gray_icon);
                    setCollectIcon(false);
                    isHeartFlag = false;
                    isAlphaShow = false;
                    setTitleAlpha(1);
                }
            }
        });
    }

    @Override
    protected void setListener() {
        EventBus.getDefault().register(this);

        leftBackImage.setOnClickListener(this);
        // 立即购买
        mNowBuy.setOnClickListener(this);
        // 购物车
        mAddGouwuche.setOnClickListener(this);
        mShopCartImageView.setOnClickListener(this);
        // 分享
        mShareImageView.setOnClickListener(this);
        // 收藏
        mCollectImageView.setOnClickListener(this);
        mChatImageView.setOnClickListener(this);
        // 到货通知
        arrivalNotice.setOnClickListener(this);
        mPurchaseSeparatelyTextView.setOnClickListener(this);
        mGroupPurchaseTextView.setOnClickListener(this);
        mTuanGouMoreLinearLayout.setOnClickListener(this);
        mGroupPurchaseTextView1.setOnClickListener(this);
        mPurchaseSeparatelyTextView1.setOnClickListener(this);

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
        intPopupWindow();
        getGoodsDetail(mId);
        ShopCartNumberUtils.getShopCartNumber(this, mGouWuCheNumber);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View v) {
        super.onClick(v);
        int i = v.getId();
        if (i == R.id.id_left_image) {
            finish();
        } else if (i == R.id.id_gd_buy_now) {
            jumpToMakeSureOrderActivity("", "");
        } else if (i == R.id.id_gd_add_gwc) {
            if (mGoodsDetail == null) {
                ToastManager.showShortToast(mContext, "请检查网络或稍后再试");
                return;
            }
            if (BaseApplication.getInstance().getLoginUserDoLogin(this) != null && mCustomPopupWindow != null) {
                mCustomPopupWindow.showAtLocationBottom(this, R.id.id_gd_main);
            }
        } else if (i == R.id.id_gd_gwc) {
            startActivity(ShopCartActivity.newIntent(this));
        } else if (i == R.id.id_right_image_1) {
        } else if (i == R.id.id_right_image_2) {
            if (mGoodsDetail != null) {
                addCollect(mGoodsDetail.getData().getId());
            } else {
                ToastManager.showShortToast(mContext, "请检查网络或稍后再试");
            }
        } else if (i == R.id.id_gd_chat) {
            ToastManager.showShortToast(mContext, "暂未开放");
        } else if (i == R.id.id_gd_arrival_notice) {
            if (!TextUtils.isEmpty(mId)) {
                arrialNotice(mId);
            }
        } else if (i == R.id.id_tuangou_more) {
            if (mGoodsDetail != null)
                // 跳转到正在参团列表
                startActivity(SpelledGroupActivity.newIntent(mContext, mGoodsDetail.getData().getGroupon_id()));
        } else if (i == R.id.id_purchase_separately || i == R.id.id_purchase_separately1) {
            jumpToMakeSureOrderActivity("", "");
        } else if (i == R.id.id_group_purchase) {
            if (mGoodsDetail != null && mGoodsDetail.getData().getGroupons().size() > 0) {
                // 等于时点击直接跳转到确认订单界面 大于1时弹对话框
                if (mGoodsDetail.getData().getGroupons().size() == 1) {
                    GoodsDetail.DataBean.GroupPonsBean groupPonsBean = mGoodsDetail.getData().getGroupons().get(0);
                    jumpToMakeSureOrderActivity(groupPonsBean.getNum(), groupPonsBean.getPrice());
                } else {
                    showSelectGrouponDialog();
                }
            }
        } else if (i == R.id.id_group_purchase1) {
            ToastManager.showShortToast(mContext, "请等待团购开启");
        }
    }

    private void showSelectGrouponDialog() {
        ZLDialogUtil.tuanGouDialog(mContext, this, mGoodsDetail.getData().getGroupons(), new ZLDialogUtil.OnTuanGouItemClickListener() {
            @Override
            public void onItemClick(GoodsDetail.DataBean.GroupPonsBean item) {
                // 跳转到确认订单
                jumpToMakeSureOrderActivity(item.getNum(), item.getPrice());
            }
        });
    }

    private void jumpToMakeSureOrderActivity(String num, String price) {
        if (mGoodsDetail == null) {
            ToastManager.showShortToast(mContext, "请检查网络或稍后再试");
            return;
        }

        if (mLoginUser != null) {
            ShopCartList.DataBean dataBean = new ShopCartList.DataBean();
            List<ShopCartList.DataBean.GBean> goodses = new ArrayList<>();
            ShopCartList.DataBean.GBean gBean = new ShopCartList.DataBean.GBean();
            GoodsDetail.DataBean data = mGoodsDetail.getData();
            gBean.setId(data.getId());
            gBean.setGoods_id(data.getId());
            gBean.setSimg(data.getImg());
            gBean.setNum("1");
            gBean.setPrice(data.getPrice());
            gBean.setPrices(data.getPrices());
            gBean.setAds(data.getAds());
            gBean.setTitle(data.getTitle());
            goodses.add(gBean);
            dataBean.setGoods(goodses);
            if (!TextUtils.isEmpty(num) && !TextUtils.isEmpty(price)) {
                // 发起团购组网络请求
                addGrouponItem(dataBean, NumberFormatUtils.toDouble(data.getPrice()), num, price);
            } else {
                // 跳转到确认订单界面
                startActivity(MakeSureOrderActivity.newIntent(this, dataBean, null, NumberFormatUtils.toDouble(data.getPrice()), ""));
            }
        } else {
            mLoginUser = BaseApplication.getInstance().getLoginUserDoLogin(this);
        }
    }

    /**
     * 设置标题栏按钮透明度
     *
     * @param alpha
     */
    private void setTitleAlpha(float alpha) {
        leftBackImage.setAlpha(alpha);
        mShareImageView.setAlpha(alpha);
        mCollectImageView.setAlpha(alpha);
        leftBackImages.setAlpha(1 - alpha);
        mShareImageViews.setAlpha(1 - alpha);
        mCollectImageViews.setAlpha(1 - alpha);
    }

    /**
     * 获取商品详情数据
     */
    private void getGoodsDetail(String id) {
        Map<String, String> map = new HashMap<>();

        map.put(URLConstants.REQUEST_PARAM_ID, id);
        // 会员id (必传)
        if (mLoginUser == null) {// 没有登录默认为0
            map.put(URLConstants.REQUEST_PARAM_UID, "0");
        } else {
            map.put(URLConstants.REQUEST_PARAM_UID, mLoginUser.getData().getId());
        }

        // 店铺id 为空时不传
        if (!TextUtils.isEmpty(mStoreId)) {
            // 门店id不为空
            map.put(URLConstants.REQUEST_PARAM_STORE_ID, mStoreId);
        }
        RequestManager.createRequest(URLConstants.REQUEST_GOODS_INFO, map, new OnMyActivityRequestListener<GoodsDetail>(this) {
            @Override
            public void onSuccess(GoodsDetail bean) {
                mGoodsDetail = bean;
                setDate(mGoodsDetail);
            }
        });
    }

    /**
     * 设置数据
     *
     * @param bean
     */
    private void setDate(GoodsDetail bean) {
        GoodsDetail.DataBean data = bean.getData();
        // 商品顶部轮播图
        mBannder.setImgUrlData(data.getActivity_icon_img(), data.getSimg());
        // 标题
        TextViewUtils.setText(mTitle, data.getTitle());
        // 广告语
        TextViewUtils.setText(mAds, data.getAds());
//                mPrice.setText(Spans.builder().text("按实计价").size(14).text("￥" + data.getPrice()).size(40).build());

        // 移除所有view
        if (mTableLayout.getChildCount() > 0) {
            mTableLayout.removeAllViews();
            mTableLayout.addView(LayoutInflater.from(mContext).inflate(R.layout.view_table_top_line, null));
        }

        // 表格自定义属性值
        List<GoodsDetail.DataBean.AttrBean> attr = data.getAttr();
        if (attr != null && attr.size() >= 1) {
            for (int i = 0; i < attr.size(); i++) {
                addTableView(attr.get(i).getK(), attr.get(i).getV());
            }
        }

        // 包装
        String packing = data.getPacking();
        if (!TextUtils.isEmpty(packing)) {
            addTableView("包装", packing);
        }

        // 保质期
        String term = data.getTerm();
        if (!TextUtils.isEmpty(term)) {
            addTableView("保质期", term);
        }

        // 储存方式
        String storage = data.getStorage();
        if (!TextUtils.isEmpty(storage)) {
            addTableView("储存方式", storage);
        }

        // 购买须知
        String desc = data.getDesc();
        if (!TextUtils.isEmpty(desc)) {
            mKownLayout.setVisibility(View.VISIBLE);
            mKown.setText(desc);
        }
        // 商品详情图片
        GoodsImageAdapter mImageAdapter = new
                GoodsImageAdapter(GoodsDetailActivity.this, R.layout.adapter_goods_detail_image);
        mImageAdapter.addList(data.getContent());
        mImagesList.setAdapter(mImageAdapter);
        // 设置PopupWindow数据
        mCustomPopupWindow.setPopupWindow(this, mGoodsDetail);

        if (mGoodsDetail.getData().getIs_cell() == 1) {// 未收藏
            mCollectImageView.setImageResource(R.mipmap.collect_gray_icon);
            mCollectImageViews.setImageResource(R.mipmap.collect_normal);
        } else {// 收藏
            mCollectImageView.setImageResource(R.mipmap.collect_red_icon);
            mCollectImageViews.setImageResource(R.mipmap.red_heart_stroke);
        }

        String activitys = data.getActivitys();
        if ("1".equals(activitys)) {
            // 预售
            mAdvanceLayout.setVisibility(View.VISIBLE);
            mAddGouwuche.setVisibility(View.GONE);
            mPriceLayout.setVisibility(View.GONE);
            mNowBuy.setVisibility(View.VISIBLE);
            mAdvancePriceTextView.setText(String.format("￥%s", data.getPrice()));
            mAdvancePricesTextView.setText(Spans.builder().text("￥" + data.getPrices()).deleteLine().build());
            mAdvanceDescTextView.setText(data.getActivity_desc());
            long time = NumberFormatUtils.toLong(data.getStoptime()) * 1000;
            startTime(data, time);

        } else if ("2".equals(activitys)) {
            // 团购
            mPriceLayout.setVisibility(View.VISIBLE);
            mTuanGouTextView.setVisibility(View.VISIBLE);

            TextPriceUtil.setTextPrices(data.getPrice(), data.getPrices(), mPricesTextView);
            // 价格
            mPrice.setText(Spans.builder().text("￥" + data.getPrice()).size(40).build());

            List<TeamBean> team = data.getTeam();
            // 如果没有人正在参团不显示
            if (team != null && team.size() > 0) {
                mTuanGouLinearLayout.setVisibility(View.VISIBLE);
                mTuanGouNumberTextView.setText(String.format("%s人正在拼团，直接参团", data.getTeam_count()));
                mSpelledGroupAdapter.addList(team);
            } else {
                mTuanGouLinearLayout.setVisibility(View.GONE);
            }

            startTime(data, NumberFormatUtils.toLong(data.getStarttime()) * 1000);
        } else {
            // 其他
            mPriceLayout.setVisibility(View.VISIBLE);
            mAdvanceLayout.setVisibility(View.GONE);
            // 价格
            mPrice.setText(Spans.builder().text("￥" + data.getPrice()).size(40).build());

            // 库存数为0显示到货通知
            stock = data.getStock();
            if ("0".equals(stock) || NumberFormatUtils.toDouble(stock) == 0) {
                arrivalNotice.setVisibility(View.VISIBLE);
                mAddGouwuche.setVisibility(View.GONE);
                mNowBuy.setVisibility(View.GONE);
            } else {
                mNowBuy.setVisibility(View.VISIBLE);
                mAddGouwuche.setVisibility(View.VISIBLE);
                arrivalNotice.setVisibility(View.GONE);
            }


        }
    }

    /**
     * 获取网络时间戳
     *
     * @param time 为1时是预售的结束时间 为2时是团购的开始时间
     */
    private void startTime(GoodsDetail.DataBean data, long time) {
        Map<String, String> map = new HashMap<>();
        RequestManager.createRequest(URLConstants.REQUEST_GET_TIME, map, new OnBaseRequestListener<TimeBean>() {
            @Override
            public void handlerSuccess(TimeBean bean) {
                setIsStartTimer(data, bean.getData().getTime() * 1000, time);

            }

            @Override
            public void handlerError(int errorCode, String errorMessage) {
                setIsStartTimer(data, System.currentTimeMillis(), time);
            }
        });
    }

    private void setIsStartTimer(GoodsDetail.DataBean data, long currentTime, long time) {
        String activitys = data.getActivitys();
        if ("1".equals(activitys)) {
            // 预售
            startTimer(data, time - currentTime);
        } else if ("2".equals(activitys)) {
            // 团购


            if (time > currentTime) {
                // 还没到开始时间 显示对应的按钮
                startTimer(data, time - currentTime);
                mPurchaseSeparatelyTextView1.setVisibility(View.VISIBLE);
                mGroupPurchaseTextView1.setVisibility(View.VISIBLE);
                mGroupPurchaseTextView1.setText(new SimpleDateFormat("MM月dd日 HH:mm").format(new Date(time)) + "\n开抢");
                mPurchaseSeparatelyTextView1.setText(String.format("￥%s\n单独购买", data.getPrices()));
            } else {
                // 团购已经开始
                mPurchaseSeparatelyTextView.setVisibility(View.VISIBLE);
                mGroupPurchaseTextView.setVisibility(View.VISIBLE);

            }

            // 团购形式等于1右边直接显示几人团 大于1 点击选择团购类型
            List<GoodsDetail.DataBean.GroupPonsBean> grouppons = data.getGroupons();
            if (grouppons != null && grouppons.size() > 0)
                mGroupPurchaseTextView.setText(grouppons.size() == 1 ? "￥" + grouppons.get(0).getPrice() + "\n" + grouppons.get(0).getNum() + "人团" : data.getPrice() + "起\n发起团购");

            mPurchaseSeparatelyTextView.setText(String.format("￥%s\n单独购买", data.getPrices()));

        }
    }

    private void startTimer(GoodsDetail.DataBean data, long countTime) {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        countDownTimer = new CountDownTimer(countTime, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // 转换为秒
                String countTimeByLong = TimeUtil.getCountTimeByLong(millisUntilFinished);
                String[] split = countTimeByLong.split(":");
                int hour = NumberFormatUtils.toInt(split[0]);
                int day = hour / 24;
                if (day > 0) {
                    mAdvanceDayTextView.setText("距结束还剩: (" + day + "天)");
                } else {
                    mAdvanceDayTextView.setText("距结束还剩: ");
                }
                mAdvanceHourTextView.setText(String.valueOf(hour % 24));
                mAdvanceMinuteTextView.setText(split[1]);
                mAdvanceSecondTextView.setText(split[2]);
                LogUtil.e(split[0] + "==" + split[1] + "==" + split[2] + "==" + split[3]);

            }

            @Override
            public void onFinish() {
                cancel();
                String activitys = data.getActivitys();
                if ("1".equals(activitys)) {
                    // 预售
                    mPrice.setVisibility(View.VISIBLE);
                    mAdvanceLayout.setVisibility(View.GONE);
                } else if ("2".equals(activitys)) {
                    // 团购
                    mPurchaseSeparatelyTextView.setVisibility(View.VISIBLE);
                    mGroupPurchaseTextView.setVisibility(View.VISIBLE);
                }

            }
        };

        countDownTimer.start();
    }

    /**
     * 添加表格
     *
     * @param key
     * @param value
     */
    private void addTableView(String key, String value) {
        mTableLayout.setVisibility(View.VISIBLE);
        mTableLayoutTip.setVisibility(View.VISIBLE);
        View tableView = LayoutInflater.from(this).inflate(R.layout.view_table_layout, null);
        TextView mKeyTextView = (TextView) tableView.findViewById(R.id.id_table_key);
        TextView mValueTextView = (TextView) tableView.findViewById(R.id.id_table_value);
        TextViewUtils.setText(mKeyTextView, key);
        TextViewUtils.setText(mValueTextView, value);
        mTableLayout.addView(tableView);
    }


    /**
     * 初始化PopupWindow
     */
    private void intPopupWindow() {
        mCustomPopupWindow = new CustomPopupWindow(this, new CustomPopupWindow.AddShopCartListener() {
            @Override
            public void sureClick(TextView numberText) {
                addShopCart(mId, numberText);
            }

            @Override
            public void addClick(TextView numberText) {
                if (!TextUtils.isEmpty(stock)) {
                    Integer number = Integer.valueOf(numberText.getText().toString());
                    number++;
                    numberText.setText(String.valueOf(number));
                }


            }

            @Override
            public void reduceClick(TextView numberText) {
                Integer numberLast = Integer.valueOf(numberText.getText().toString());
                if (numberLast > 1) {
                    numberLast--;
                    numberText.setText(String.valueOf(numberLast));
                }
            }
        }, true);
    }

    /**
     * 加入购物车
     *
     * @param id
     */
    private void addShopCart(String id, TextView mPopupNumberTextView) {
        if (BaseApplication.getInstance().getLoginUserDoLogin(this) != null) {
            User loginUserDoLogin = BaseApplication.getInstance().getLoginUserDoLogin(this);
            Map<String, String> map = new HashMap<>();
            map.put(URLConstants.REQUEST_PARAM_ID, id);
            map.put(URLConstants.REQUEST_PARAM_UID, loginUserDoLogin.getData().getId());
            map.put(URLConstants.REQUEST_PARAM_NUM, mPopupNumberTextView.getText().toString());
            RequestManager.createRequest(URLConstants.REQUEST_ADDCART, map, new OnMyActivityRequestListener<BaseBean>(this) {
                @Override
                public void onSuccess(BaseBean bean) {
                    ToastManager.showShortToast(mContext, "加入购物车");
                    EventBus.getDefault().postSticky(new DataChangEvent(0, true));
                }

                @Override
                public void handlerStart() {
                }

                @Override
                public void handlerEnd() {

                }
            });
        }
    }

    /**
     * 到货通知
     *
     * @param goodId
     */
    private void arrialNotice(String goodId) {
        if (BaseApplication.getInstance().getLoginUserDoLogin(this) != null) {
            User loginUserDoLogin = BaseApplication.getInstance().getLoginUserDoLogin(this);
            Map<String, String> map = new HashMap<>();
            map.put(URLConstants.REQUEST_PARAM_GOODS_ID, goodId);
            map.put(URLConstants.REQUEST_PARAM_UID, loginUserDoLogin.getData().getId());
            RequestManager.createRequest(URLConstants.ARRIVAL_NOTICE_URL, map, new OnMyActivityRequestListener<BaseBean>(this) {
                @Override
                public void onSuccess(BaseBean bean) {
                    ToastManager.showShortToast(mContext, "设置到货通知成功，请注意查看消息中心");
                }

                @Override
                public void handlerStart() {
                }

                @Override
                public void handlerEnd() {

                }
            });
        }
    }

    /**
     * 添加收藏
     *
     * @param id
     */
    private void addCollect(String id) {
        User loginUserDoLogin = BaseApplication.getInstance().getLoginUserDoLogin(this);
        if (loginUserDoLogin == null)
            return;
        Map<String, String> map = new HashMap<>();
        map.put(URLConstants.REQUEST_PARAM_UID, loginUserDoLogin.getData().getId());
        map.put(URLConstants.REQUEST_PARAM_PID, id);
        map.put(URLConstants.REQUEST_PARAM_TYPE, "goods");
        RequestManager.createRequest(URLConstants.REQUEST_CELL, map, new OnMyActivityRequestListener<BaseBean>(this) {
            @Override
            public void onSuccess(BaseBean bean) {
                if (mGoodsDetail.getData().getIs_cell() == 1) {// 收藏商品
                    mGoodsDetail.getData().setIs_cell(2);
                    if (isHeartFlag) {
                        mCollectImageView.setImageResource(R.mipmap.collect_red_icon);
                        mCollectImageViews.setImageResource(R.mipmap.red_heart_stroke);
                    } else {
                        mCollectImageView.setImageResource(R.mipmap.red_heart_stroke);
                        mCollectImageViews.setImageResource(R.mipmap.collect_red_icon);
                    }
                    ToastManager.showShortToast(mContext, "收藏成功");
                } else {// 取消商品收藏
                    mGoodsDetail.getData().setIs_cell(1);
                    if (isHeartFlag) {
                        mCollectImageView.setImageResource(R.mipmap.collect_gray_icon);
                        mCollectImageViews.setImageResource(R.mipmap.collect_normal);
                    } else {
                        mCollectImageView.setImageResource(R.mipmap.collect_normal);
                        mCollectImageViews.setImageResource(R.mipmap.collect_gray_icon);
                    }

                    ToastManager.showShortToast(mContext, "取消收藏成功");
                }
            }
        });
    }

    /**
     * 发起团购组
     *
     * @param goodsPrice 商品价格(必传)
     * @param num        发起数组的人数(必传)
     * @param price      发起数组的价格(必传)
     */
    private void addGrouponItem(ShopCartList.DataBean dataBean, double goodsPrice, String num, String price) {
        if (BaseApplication.getInstance().getLoginUserDoLogin(this) != null) {
            User loginUserDoLogin = BaseApplication.getInstance().getLoginUserDoLogin(this);
            Map<String, String> map = new HashMap<>();
            // 商品id(必传)
            map.put(URLConstants.REQUEST_PARAM_GOODS_ID, mId);
            // 发起人id(必传)
            map.put(URLConstants.REQUEST_PARAM_UID, loginUserDoLogin.getData().getId());
            // 发起数组的人数(必传)
            map.put(URLConstants.REQUEST_PARAM_NUM, num);
            // 发起数组的价格(必传)
            map.put(URLConstants.REQUEST_PARAM_PRICE, price);
            // 团购活动id(必传)
            map.put(URLConstants.REQUEST_PARAM_GROUPON_ID, mGoodsDetail.getData().getGroupon_id());
            RequestManager.createRequest(URLConstants.ADD_GROUPON_ITEM_URL, map, new OnMyActivityRequestListener<AddGrouponItemBean>(this) {
                @Override
                public void onSuccess(AddGrouponItemBean bean) {
                    // 跳转到确认订单界面
                    startActivity(MakeSureOrderActivity.newIntent(GoodsDetailActivity.this, dataBean, null, goodsPrice, bean.getData().getGroupon_team_id()));
                }

            });
        }
    }


    /**
     * 设置收藏按钮 状态
     *
     * @param flag
     */
    private void setCollectIcon(boolean flag) {
        if (mGoodsDetail != null) {
            if (flag) {
                if (mGoodsDetail.getData().getIs_cell() == 1) {
                    mCollectImageView.setImageResource(R.mipmap.collect_gray_icon);
                    mCollectImageViews.setImageResource(R.mipmap.collect_normal);
                } else {
                    mCollectImageView.setImageResource(R.mipmap.collect_red_icon);
                    mCollectImageViews.setImageResource(R.mipmap.red_heart_stroke);
                }
            } else {
                if (mGoodsDetail.getData().getIs_cell() == 1) {
                    mCollectImageView.setImageResource(R.mipmap.collect_normal);
                    mCollectImageViews.setImageResource(R.mipmap.collect_gray_icon);
                } else {
                    mCollectImageView.setImageResource(R.mipmap.red_heart_stroke);
                    mCollectImageViews.setImageResource(R.mipmap.collect_red_icon);
                }
            }
        } else {
            if (flag) {
                mCollectImageView.setImageResource(R.mipmap.collect_gray_icon);
                mCollectImageViews.setImageResource(R.mipmap.collect_normal);
            } else {
                mCollectImageView.setImageResource(R.mipmap.collect_normal);
                mCollectImageViews.setImageResource(R.mipmap.collect_gray_icon);
            }

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true) //在ui线程执行
    public void updataShopCart(DataChangEvent event) {
        ShopCartNumberUtils.getShopCartNumber(this, mGouWuCheNumber);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = false) //在ui线程执行
    public void updataDetail(GoodsDetailEvent event) {
        mId = event.getId();
        getGoodsDetail(event.getId());

    }
}
