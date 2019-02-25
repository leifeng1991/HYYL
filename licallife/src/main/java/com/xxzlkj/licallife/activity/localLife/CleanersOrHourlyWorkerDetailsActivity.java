package com.xxzlkj.licallife.activity.localLife;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sunfusheng.marqueeview.DisplayUtil;
import com.xxzlkj.licallife.R;
import com.xxzlkj.licallife.adapter.ProjecImageAdapter;
import com.xxzlkj.licallife.adapter.ScheduleAdapter;
import com.xxzlkj.licallife.model.CleanerDetailBean;
import com.xxzlkj.licallife.model.ProjectDetail;
import com.xxzlkj.licallife.model.ScheduleBean;
import com.xxzlkj.licallife.utils.ScheduleUtil;
import com.xxzlkj.licallife.weight.CustomPopupWindow;
import com.xxzlkj.licallife.weight.RatingBar;
import com.xxzlkj.shop.config.GlobalParams;
import com.xxzlkj.shop.config.StringConstants;
import com.xxzlkj.shop.config.URLConstants;
import com.xxzlkj.shop.weight.CustomButton;
import com.xxzlkj.zhaolinshare.base.app.BaseApplication;
import com.xxzlkj.zhaolinshare.base.base.MyBaseActivity;
import com.xxzlkj.zhaolinshare.base.model.User;
import com.xxzlkj.zhaolinshare.base.net.OnMyActivityRequestListener;
import com.xxzlkj.zhaolinshare.base.net.RequestManager;
import com.xxzlkj.zhaolinshare.base.util.CallPhoneUtils;
import com.xxzlkj.zhaolinshare.base.util.NumberFormatUtils;
import com.xxzlkj.zhaolinshare.base.util.PicassoUtils;
import com.xxzlkj.zhaolinshare.base.util.StringUtil;
import com.xxzlkj.zhaolinshare.base.util.TextViewUtils;
import com.xxzlkj.zhaolinshare.base.util.ToastManager;
import com.zrq.spanbuilder.Spans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * 保洁师/小时工详情
 */
public class CleanersOrHourlyWorkerDetailsActivity extends MyBaseActivity {
    public static final String CLEANERS_OR_HOURLY = "cleaners_or_hourly";
    private ImageView mBannerView;
    private TextView mNameTextView;
    private TextView mServiceTimeTextView;
    private TextView mOrderQuantityTextView;
    private TextView mSatisfactionTextView;
    private TextView mSaturationTextView;
    private TextView mPriceTextView;
    private RatingBar mRatingBar;
    private CustomButton mNowBuyTextView;
    private CustomButton mPhoneLinearLayout;
    private TextView mContentTextView;
    private TextView mSkillsLabelTextView;
    private LinearLayout ll_bottom_button;
    private RecyclerView mImagesRecyclerView;
    private LinearLayout mTableLinearLayout;
    private LinearLayout mDetailLinearLayout;
    private LinearLayout mFirstLinearLayout;
    private RelativeLayout mSecondLinearLayout;
    private LinearLayout mTimeLinearLayout;
    private RecyclerView mScheduleRecyclerView;
    private ScheduleAdapter mScheduleAdapter;
    // 商家
    private LinearLayout mMerchantLinearLayout;
    private ImageView mMerchantImageView;
    private TextView mMerchantNameTextView;
    private TextView mMerchantContentTextView;
    //标签
    private CustomButton mFirstTextView;
    private CustomButton mSecondTextView;
    private CustomButton mThirdTextView;
    // 技能标签/耗时参考
    private TextView mTitleTextView;
    private CleanerDetailBean.DataBean data;
    // 保洁师id
    private String mCleanerId;
    private boolean isNowBottomShow;
    private int[] outLocation = new int[2];
    private boolean isCleanersOrHourly;

    /**
     * @param context            上下文 （必传）
     * @param id                 保洁师/小时工id (必传)
     * @param isCleanersOrHourly 用于区分小时工和保洁师 true:保洁师详情 false:小时工详情 （必传）
     * @return
     */
    public static Intent newIntent(Context context, String id, boolean isCleanersOrHourly) {
        Intent intent = new Intent(context, CleanersOrHourlyWorkerDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(StringConstants.INTENT_PARAM_ID, id);
        bundle.putBoolean(CLEANERS_OR_HOURLY, isCleanersOrHourly);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_cleaners_detail);
    }

    @Override
    protected void findViewById() {
        // 顶部图片
        mBannerView = getView(R.id.id_banner);
        // 名字
        mNameTextView = getView(R.id.id_name);
        // 服务时间
        mServiceTimeTextView = getView(R.id.id_service_time);
        // 接单量
        mOrderQuantityTextView = getView(R.id.id_order_quantity);
        // 满意度
        mSatisfactionTextView = getView(R.id.id_satisfaction);
        // 饱和度
        mSaturationTextView = getView(R.id.id_saturation);
        // 价格
        mPriceTextView = getView(R.id.id_price);
        // 评价
        mRatingBar = getView(R.id.id_star);
        mRatingBar.setEnabled(false);
        // 立即购买
        mNowBuyTextView = getView(R.id.id_now);
        // 打电话
        mPhoneLinearLayout = getView(R.id.id_phone);
        // 表格布局（保洁师特有的）
        mTableLinearLayout = getView(R.id.id_table_layout);
        // 技能标签（小时工特有的）
        mSkillsLabelTextView = getView(R.id.id_skills_label);
        // 商家列表布局
        mMerchantLinearLayout = getView(R.id.id_merchant_layout);
        mMerchantImageView = getView(R.id.id_merchant_image);
        mMerchantNameTextView = getView(R.id.id_merchant_name);
        mMerchantContentTextView = getView(R.id.id_merchant_content);
        mContentTextView = getView(R.id.id_content);
        // 图片列表
        mImagesRecyclerView = getView(R.id.id_recyclerview);
        mImagesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        // 禁止RecyclerView滑动解决RecyclerView不显示问题
        mImagesRecyclerView.setNestedScrollingEnabled(false);
        // 底部按钮
        ll_bottom_button = getView(R.id.ll_bottom_button);
        // 详情布局
        mDetailLinearLayout = getView(R.id.id_detail_layout);
        mFirstLinearLayout = getView(R.id.id_first_layout);
        mSecondLinearLayout = getView(R.id.id_second_layout);
        // 标签
        mFirstTextView = getView(R.id.id_lable_first);
        mSecondTextView = getView(R.id.id_lable_second);
        mThirdTextView = getView(R.id.id_lable_third);
        // 技能标签/耗时参考
        mTitleTextView = getView(R.id.id_title);
        // 可约时间
        mTimeLinearLayout = getView(R.id.id_time_layout);
        mScheduleRecyclerView = getView(R.id.id_schedule_recyclerview);
        mScheduleRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mScheduleRecyclerView.setNestedScrollingEnabled(false);
        mScheduleAdapter = new ScheduleAdapter(mContext, R.layout.adapter_schedule_list_item, 1);
        mScheduleRecyclerView.setAdapter(mScheduleAdapter);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mCleanerId = bundle.getString(StringConstants.INTENT_PARAM_ID);
            isCleanersOrHourly = bundle.getBoolean(CLEANERS_OR_HOURLY);
        }

        ScheduleUtil.drawingScale(mContext, mTimeLinearLayout, 1);
    }

    @Override
    protected void setListener() {
        mPhoneLinearLayout.setOnClickListener(this);
        mNowBuyTextView.setOnClickListener(this);
        getView(R.id.id_about_time_layout).setOnClickListener(this);
        setOnClickListener(R.id.id_bottom_now, R.id.id_bottom_phone);
        //标题栏背景变化
        NestedScrollView mNestedScrollView = getView(R.id.id_gd_scroll);
        mNestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY < mBannerView.getHeight() + mFirstLinearLayout.getHeight() + mPhoneLinearLayout.getHeight() + mSecondLinearLayout.getPaddingTop()) {// 此方法不会逻辑深入获取
                    // 隐藏
                    if (isNowBottomShow) {// 防止执行多次
                        hideBottom();
                        isNowBottomShow = false;
                    }
                } else {
                    // 打开
                    if (!isNowBottomShow) {// 防止执行多次
                        openBottom();
                        isNowBottomShow = true;
                    }
                }
            }
        });

    }

    private void openBottom() {
        ll_bottom_button.setVisibility(View.VISIBLE);
        ll_bottom_button.setTranslationY(ll_bottom_button.getHeight());
        ll_bottom_button.animate().translationY(0).setDuration(500).start();// animate 会先取消，后执行
    }

    private void hideBottom() {
        ll_bottom_button.setVisibility(View.VISIBLE);
        ll_bottom_button.setTranslationY(0);
        ll_bottom_button.animate().translationY(ll_bottom_button.getHeight()).setDuration(500).start();
    }

    @Override
    protected void processLogic() {
        if (!GlobalParams.isOpenLocalLife) {
            ToastManager.showShortToast(mContext, getString(R.string.noLocalLifeServiceHint));
            finish();
        } else {
            setTitleLeftBack();
            if (isCleanersOrHourly) {
                setTitleName("保洁师");
                mTitleTextView.setText("耗时参考");
                mTableLinearLayout.setVisibility(View.VISIBLE);
            } else {
                setTitleName("小时工");
                mTitleTextView.setText("技能标签");
                mSkillsLabelTextView.setVisibility(View.VISIBLE);
            }
            setTitleRightImage(R.mipmap.share_icon);
            getCleanerDetail(mCleanerId);
            // 初始化底部布局
            ViewTreeObserver viewTreeObserver1 = ll_bottom_button.getViewTreeObserver();
            viewTreeObserver1.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    ll_bottom_button.setTranslationY(ll_bottom_button.getHeight());
                    //noinspection deprecation
                    ll_bottom_button.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
            });
            // 获取按钮位置
            mPhoneLinearLayout.getLocationOnScreen(outLocation);
        }

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int i = v.getId();
        if (i == R.id.id_now || i == R.id.id_bottom_now) {
            User loginUserDoLogin = BaseApplication.getInstance().getLoginUserDoLogin(this);
            if (loginUserDoLogin != null && data != null) {
                if (isCleanersOrHourly) {
                    // 保洁师
                    startActivity(SubscribeActivity.newIntent(this, mCleanerId, 2, data));
                } else {
                    // 小时工
                    startActivity(SubscribeActivity.newIntent(this, mCleanerId, 3, data));
                }

            }

        } else if (i == R.id.id_phone || i == R.id.id_bottom_phone) {
            if (data != null) {
                CleanerDetailBean.DataBean.ShopBean shop = data.getShop();
                if (shop != null) {
                    CallPhoneUtils.callPhoneDialog(CleanersOrHourlyWorkerDetailsActivity.this, shop.getPhone());
                }
            }

        } else if (i == R.id.iv_title_right) {
        } else if (i == R.id.id_about_time_layout) {
            CustomPopupWindow popupWindow = new CustomPopupWindow(this, 1, data.getSchedule());
            popupWindow.showAtLocationBottom(this, R.id.id_parent_layout);

        }
    }


    /**
     * 添加表格
     *
     * @param key
     * @param value
     */
    private void addTableView(String key, String value) {
        View tableView = LayoutInflater.from(this).inflate(R.layout.view_local_life_table_layout, null);
        TextView mKeyTextView = (TextView) tableView.findViewById(R.id.id_table_key);
        TextView mValueTextView = (TextView) tableView.findViewById(R.id.id_table_value);
        TextViewUtils.setText(mKeyTextView, key);
        TextViewUtils.setText(mValueTextView, value);
        mTableLinearLayout.addView(tableView);
    }

    /**
     * 保洁师/小时工详情
     */
    private void getCleanerDetail(String id) {
        HashMap<String, String> map = new HashMap<>();
        map.put(URLConstants.REQUEST_PARAM_ID, id);
        String url;
        if (isCleanersOrHourly) {
            // 保洁师详情
            url = URLConstants.JZ_CLEANING_PEOPLE_INFO_URL;

        } else {
            // 小时工详情
            url = URLConstants.JZ_cleaning_Hour_Info_URL;
        }
        RequestManager.createRequest(url, map, new OnMyActivityRequestListener<CleanerDetailBean>(this) {
            @Override
            public void onSuccess(CleanerDetailBean bean) {
                data = bean.getData();
                setData(data);
            }

        });
    }

    private void setData(CleanerDetailBean.DataBean data) {
        // 详情
        List<ProjectDetail.DataBean.ImgBean> img = data.getImg();
        if (img != null && img.size() > 0) {
            // 有图片
            mDetailLinearLayout.setVisibility(View.VISIBLE);
            ProjecImageAdapter mImageAdapter = new
                    ProjecImageAdapter(CleanersOrHourlyWorkerDetailsActivity.this, R.layout.adapter_goods_detail_image);
            mImageAdapter.addList(data.getImg());
            mImagesRecyclerView.setAdapter(mImageAdapter);
        } else {
            // 无图片
            mDetailLinearLayout.setVisibility(View.GONE);
        }

        // 顶部图片
        int wh = DisplayUtil.getWindowWidth(CleanersOrHourlyWorkerDetailsActivity.this);
        mBannerView.getLayoutParams().width = wh;
        mBannerView.getLayoutParams().height = wh;
        PicassoUtils.setWithAndHeight(CleanersOrHourlyWorkerDetailsActivity.this, data.getSimg(), wh, wh, mBannerView);
        // 广告语
        String ads = data.getAds();
        if (TextUtils.isEmpty(ads)) {
            mContentTextView.setVisibility(View.GONE);
        } else {
            mContentTextView.setVisibility(View.VISIBLE);
            mContentTextView.setText(ads);
        }
        // 标签
        String label = data.getLabel();
        // 标签个数
        if (TextUtils.isEmpty(label)) {
            // 无标签
            mFirstTextView.setVisibility(View.GONE);
            mSecondTextView.setVisibility(View.GONE);
            mThirdTextView.setVisibility(View.GONE);
        } else {
            String[] split = label.split(",");
            if (split.length == 1) {
                // 一个标签
                mFirstTextView.setVisibility(View.VISIBLE);
                mSecondTextView.setVisibility(View.GONE);
                mThirdTextView.setVisibility(View.GONE);
                mFirstTextView.setText(split[0]);
            } else if (split.length == 2) {
                // 两个标签
                mFirstTextView.setVisibility(View.VISIBLE);
                mSecondTextView.setVisibility(View.VISIBLE);
                mThirdTextView.setVisibility(View.GONE);
                mFirstTextView.setText(split[0]);
                mSecondTextView.setText(split[1]);
            } else {
                // 三个及三个以上标签
                mFirstTextView.setVisibility(View.VISIBLE);
                mSecondTextView.setVisibility(View.VISIBLE);
                mThirdTextView.setVisibility(View.VISIBLE);
                mFirstTextView.setText(split[0]);
                mSecondTextView.setText(split[1]);
                mThirdTextView.setText(split[2]);
            }
        }

        if (isCleanersOrHourly) {
            // 保洁师
            // 价格
            double price = NumberFormatUtils.toDouble(data.getPrice()) * 2;
            mPriceTextView.setText(Spans.builder().text("￥" + StringUtil.saveTwoDecimal(String.valueOf(price))).text("/" + "小时", 15, 0xffff725c).build());
            // 表格
            addTableView("承接业务", "收费标准");
            List<CleanerDetailBean.DataBean.GrouptitleBean> grouptitle = data.getGrouptitle();
            if (grouptitle != null && grouptitle.size() > 0) {
                for (int i = 0; i < grouptitle.size(); i++) {
                    CleanerDetailBean.DataBean.GrouptitleBean grouptitleBean = grouptitle.get(i);
                    // 添加表格
                    String unit_desc = grouptitleBean.getUnit_desc();
                    if (TextUtils.isEmpty(unit_desc)) {
                        // 单位为空
                        addTableView(grouptitleBean.getTitle(), grouptitleBean.getPrice());
                    } else {
                        // 单位不为空
                        addTableView(grouptitleBean.getTitle(), grouptitleBean.getPrice() + "/" + unit_desc);
                    }

                }
            }
        } else {
            // 小时工
            // 技能
            mSkillsLabelTextView.setText(data.getSpecialty());
            // 价格
            double price = NumberFormatUtils.toDouble(data.getPrice()) * 2;
            mPriceTextView.setText(Spans.builder().text("￥" + StringUtil.saveTwoDecimal(String.valueOf(price))).text("/" + "小时", 15, 0xffff725c).build());
        }

        // 获取按钮位置
        mPhoneLinearLayout.postDelayed(() -> mPhoneLinearLayout.getLocationOnScreen(outLocation), 1000);// 防止不能及时获取

        // 姓名
        mNameTextView.setText(data.getName());
        // 评价
        mRatingBar.setRating(NumberFormatUtils.toFloat(data.getStar()));
        // 服务时间
        mServiceTimeTextView.setText(String.format("服务时间：%s-%s", data.getService_starttime(), data.getService_endtime()));
        // 接单量
        mOrderQuantityTextView.setText(String.format("接单量：%s", data.getSale()));
        // 满意度
        mSatisfactionTextView.setText(String.format("满意度：%s%%", data.getSatisfaction()));
        // 饱和度
        mSaturationTextView.setText(String.format("饱和度：%s%%", data.getSaturation()));
        // 店铺状态
        String cleaning_shop_state = data.getCleaning_shop_state();
        if ("0".equals(cleaning_shop_state)) {
            // 隐藏店铺
            mMerchantLinearLayout.setVisibility(View.GONE);
        } else if ("1".equals(cleaning_shop_state)) {
            // 商家
            CleanerDetailBean.DataBean.ShopBean shopBean = data.getShop();
            if (shopBean != null) {
                // 设置店铺内容
                PicassoUtils.setImageBig(mContext, shopBean.getSimg(), mMerchantImageView);
                mMerchantNameTextView.setText(shopBean.getTitle());
                mMerchantContentTextView.setText(shopBean.getDesc());
                // 显示店铺
                mMerchantLinearLayout.setVisibility(View.VISIBLE);
            } else {
                // 隐藏店铺
                mMerchantLinearLayout.setVisibility(View.GONE);
            }
        }

        List<ScheduleBean> schedule = data.getSchedule();
        // 新的列表 三个
        List<ScheduleBean> newScheduleBeanList = new ArrayList<>();
        if (null != schedule && schedule.size() >= 3) {
            for (int i = 0; i < 3; i++) {
                newScheduleBeanList.add(schedule.get(i));
            }
        }
        mScheduleAdapter.addList(newScheduleBeanList);
    }
}
