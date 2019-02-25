package com.xxzlkj.licallife.activity.localLife;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.wonderkiln.blurkit.BlurLayout;
import com.xxzlkj.licallife.R;
import com.xxzlkj.licallife.adapter.CertificateAdapter;
import com.xxzlkj.licallife.adapter.IdPhotoAdapter;
import com.xxzlkj.licallife.adapter.NannyAndMaternityMatronListAdapter;
import com.xxzlkj.licallife.adapter.ScheduleAdapter;
import com.xxzlkj.licallife.adapter.TabsAdapter;
import com.xxzlkj.licallife.fragment.NannyAndMaternityMatronDesTabFragment;
import com.xxzlkj.licallife.model.NannyAndMaternityMatronDesBean;
import com.xxzlkj.licallife.model.NannyAndMaternityMatronListBean;
import com.xxzlkj.licallife.model.ScheduleBean;
import com.xxzlkj.licallife.utils.LocalLifePicassoUtils;
import com.xxzlkj.licallife.utils.ScheduleUtil;
import com.xxzlkj.licallife.weight.CustomPopupWindow;
import com.xxzlkj.shop.config.GlobalParams;
import com.xxzlkj.shop.config.URLConstants;
import com.xxzlkj.shop.utils.MyDrawableUtils;
import com.xxzlkj.zhaolinshare.base.app.BaseApplication;
import com.xxzlkj.zhaolinshare.base.base.BaseActivity;
import com.xxzlkj.zhaolinshare.base.model.BaseBean;
import com.xxzlkj.zhaolinshare.base.model.User;
import com.xxzlkj.zhaolinshare.base.net.OnMyActivityRequestListener;
import com.xxzlkj.zhaolinshare.base.net.RequestManager;
import com.xxzlkj.zhaolinshare.base.util.NumberFormatUtils;
import com.xxzlkj.zhaolinshare.base.util.PixelUtil;
import com.xxzlkj.zhaolinshare.base.util.StringUtil;
import com.xxzlkj.zhaolinshare.base.util.SystemBarUtils;
import com.xxzlkj.zhaolinshare.base.util.ToastManager;
import com.zrq.spanbuilder.Spans;
import com.zrq.spanbuilder.TextStyle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 本地生活--保姆、月嫂详情
 *
 * @author zhangrq
 */
public class NannyAndMaternityMatronDesActivity extends BaseActivity {
    public static final String IS_NANNY = "isNanny";
    public static final String ID = "id";
    private String id;
    private boolean isNanny;
    private ImageView iv_icon;
    private RelativeLayout mToolbar;
    private RecyclerView recyclerView_tabs, recyclerView_certificate, recyclerView_recommend, recyclerView_id_photo, mScheduleRecyclerView;
    private TextView tv_name_id, tv_pv_num, btn_subscribe, tv_service, tv_des, tv_sale_num, tv_all_days, tv_monthly_salary, tv_experience, tv_high_praise, mTipTextView, mJuLiTextView;
    private CertificateAdapter certificateAdapter;
    private TabsAdapter tabsAdapter;
    private IdPhotoAdapter idPhotoAdapter;
    private NannyAndMaternityMatronListAdapter recommendAdapter;
    private NannyAndMaternityMatronDesTabFragment nannyAndMaternityMatronDesTabFragment;
    private TabLayout tabLayout;
    private NestedScrollView nev_content;
    private View ll_recommend_layout,ll_id_photo, ll_certificate, ll_tabs;
    private NannyAndMaternityMatronDesBean.DataBean data;
    private ImageView leftBackImage;
    private ImageView leftBackImages;
    private ImageView mShareImageView;
    private ImageView mCollectImageView;
    private ImageView mShareImageViews;
    private ImageView mCollectImageViews;
    private LinearLayout mTimeLinearLayout;
    private ScheduleAdapter mScheduleAdapter;
    //心 样式
    private boolean isHeartFlag = true;
    // true:透明度从1-0 false:透明度从0-1
    private boolean isAlphaShow = true;
    private BlurLayout blurLayout;

    /**
     * @param isNanny isNanny 是否是保姆详情页面，true为保姆详情页面，false为月嫂详情页面
     * @param id      订单id
     */
    public static Intent newIntent(Context context, boolean isNanny, String id) {
        Intent intent = new Intent(context, NannyAndMaternityMatronDesActivity.class);
        intent.putExtra(IS_NANNY, isNanny);
        intent.putExtra(ID, id);
        return intent;
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_nanny_and_maternity_matron_des);
        SystemBarUtils.setStatusBarTranslucent(this);
        SystemBarUtils.setStatusBarLightMode(this, true);
    }

    @Override
    protected void findViewById() {
        nev_content = getView(R.id.nev_content);// 内容列表
        // 标题头
        mToolbar = getView(R.id.id_mtl_toolbar);
        mToolbar.setBackgroundColor(Color.argb(0,250,251,253));// fafbfd
        mToolbar.setPadding(0, PixelUtil.dip2px(mContext, 26), 0, PixelUtil.dip2px(mContext, 10));
        leftBackImage = getView(R.id.id_left_image);
        leftBackImages = getView(R.id.id_left_images);
        mShareImageView = getView(R.id.id_right_image_1);
        mCollectImageView = getView(R.id.id_right_image_2);
        mShareImageViews = getView(R.id.id_right_images_1);
        mCollectImageViews = getView(R.id.id_right_images_2);

        iv_icon = getView(R.id.iv_icon);// 头像
        blurLayout = getView(R.id.blurLayout);// 模糊布局

        tv_name_id = getView(R.id.tv_name_id);// 名字 + id
        tv_des = getView(R.id.tv_des);// 详细描述

        tv_high_praise = getView(R.id.tv_high_praise);// 好评
        tv_experience = getView(R.id.tv_experience);// 经验
        tv_monthly_salary = getView(R.id.tv_monthly_salary);// 月薪
        // 可预约时间
        TextView mTextView = getView(R.id.id_time);
        mTextView.setText("日期");
        mTimeLinearLayout = getView(R.id.id_time_layout);
        mTipTextView = getView(R.id.id_tip);// 查看7个月
        mTipTextView.setText("查看7个月");
        mScheduleRecyclerView = getView(R.id.id_schedule_recyclerview);// 可约时间列表
        mScheduleRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mScheduleRecyclerView.setNestedScrollingEnabled(false);
        mScheduleAdapter = new ScheduleAdapter(mContext, R.layout.adapter_schedule_list_item, 2);
        mScheduleRecyclerView.setAdapter(mScheduleAdapter);
        ScheduleUtil.drawingScale(mContext, mTimeLinearLayout, 2);

        tv_all_days = getView(R.id.tv_all_days);// 总在岗X天
        tv_sale_num = getView(R.id.tv_sale_num);// 预约X次
        tv_pv_num = getView(R.id.tv_pv_num);// 浏览X次
        mJuLiTextView = getView(R.id.id_juli);//距离

        ll_tabs = getView(R.id.ll_tabs);// 标签布局
        recyclerView_tabs = getView(R.id.recyclerView_tabs);// 标签

        ll_certificate = getView(R.id.ll_certificate);// 职业技能证布局
        recyclerView_certificate = getView(R.id.recyclerView_certificate);// 职业技能证

        tabLayout = getView(R.id.tabLayout); // tab布局

        ll_id_photo = getView(R.id.ll_id_photo);// 证件照片布局
        recyclerView_id_photo = getView(R.id.recyclerView_id_photo);// 证件照片
        ll_recommend_layout = getView(R.id.ll_recommend_layout);// 类似月嫂推荐布局
        recyclerView_recommend = getView(R.id.recyclerView_recommend);// 类似月嫂推荐

        tv_service = getView(R.id.tv_service);// 客服
        btn_subscribe = getView(R.id.btn_subscribe);// 按钮立即预约
        MyDrawableUtils.setButtonShapeOrange(mContext, btn_subscribe);

        // 初始化标签
        recyclerView_tabs.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        tabsAdapter = new TabsAdapter(mContext, R.layout.item_tabs);
        recyclerView_tabs.setAdapter(tabsAdapter);
        // 初始化职业技能证
        recyclerView_certificate.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        certificateAdapter = new CertificateAdapter(mContext, R.layout.item_certificate);
        recyclerView_certificate.setAdapter(certificateAdapter);
        // 初始化tab内容(基本信息，工作信息，培训信息)
        nannyAndMaternityMatronDesTabFragment = NannyAndMaternityMatronDesTabFragment.newInstance();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.tabContent, nannyAndMaternityMatronDesTabFragment)
                .commitAllowingStateLoss();

        // 初始化证件照片
        recyclerView_id_photo.setLayoutManager(new GridLayoutManager(mContext, 4));
        recyclerView_id_photo.setNestedScrollingEnabled(false);
        idPhotoAdapter = new IdPhotoAdapter(mContext, R.layout.item_id_photo);
        recyclerView_id_photo.setAdapter(idPhotoAdapter);
        // 初始化类似月嫂推荐
        recyclerView_recommend.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView_recommend.setNestedScrollingEnabled(false);
        recommendAdapter = new NannyAndMaternityMatronListAdapter(mContext, R.layout.item_nanny_and_maternity_matron_list);
        recyclerView_recommend.setAdapter(recommendAdapter);
    }

    @Override
    protected void setListener() {
        // 返回按钮
        leftBackImage.setOnClickListener(v -> onBackPressed());
        // 收藏
        mCollectImageView.setOnClickListener(v -> {
            if (data != null) {
                addCollect(data.getId());
            } else {
                ToastManager.showShortToast(mContext, "请检查网络或稍后再试");
            }
        });
        // 栏目的点击事件
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                nannyAndMaternityMatronDesTabFragment.setData(tab.getPosition() + "", data);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        // 滚动监听
        nev_content.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY <= 0) {
                    mToolbar.setBackgroundColor(Color.argb(0,250,251,253));// fafbfd
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
                    mToolbar.setBackgroundColor(Color.argb((int) (y * 255),250,251,253));// fafbfd

                    if (isAlphaShow) {// 透明度值变化
                        y = 1 - y;
                    }
                    setTitleAlpha(y);
                } else {
                    mToolbar.setBackgroundColor(Color.argb(255,250,251,253));// fafbfd
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
        // 客服
        tv_service.setOnClickListener(v -> ToastManager.showShortToast(mContext, "客服"));
        // 按钮立即预约
        btn_subscribe.setOnClickListener(v -> {
            if (data == null) {
                ToastManager.showShortToast(mContext, "获取网络数据错误，请检查网络后稍后再试");
                return;
            }

            startActivity(NannyAndMaternityMatronSubscribeActivity.newIntent(mContext, isNanny, data));
        });
        // 最近几个月可预约 弹框
        getView(R.id.id_about_time_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (data != null) {
                    CustomPopupWindow popupWindow = new CustomPopupWindow(NannyAndMaternityMatronDesActivity.this, 2, data.getSchedule());
                    popupWindow.showAtLocationBottom(NannyAndMaternityMatronDesActivity.this, R.id.id_parent_layout);
                }
            }
        });
    }

    @Override
    protected void processLogic() {
        if (TextUtils.isEmpty(GlobalParams.communityId) || !GlobalParams.isOpenLocalLife){
            ToastManager.showShortToast(mContext, getString(R.string.noLocalLifeServiceHint));
            finish();
        }else {
            id = getIntent().getStringExtra(ID);
            isNanny = getIntent().getBooleanExtra(IS_NANNY, false);// 是否是保姆列表
            // 获取网络数据
            getNetData();
        }

    }

    private void getNetData() {
        Map<String, String> map = new HashMap<>();
        // id	月嫂id
        map.put("id", id);
        User user = BaseApplication.getInstance().getLoginUser();
        map.put("uid", user == null ? "" : user.getData().getId());
        // 小区id
        map.put(URLConstants.REQUEST_PARAM_COMMUNITY_ID, GlobalParams.communityId);
        // 经度
        map.put(URLConstants.REQUEST_PARAM_LONGITUDE, String.valueOf(GlobalParams.longitude));
        // 纬度
        map.put(URLConstants.REQUEST_PARAM_LATITUDE, String.valueOf(GlobalParams.latitude));
        RequestManager.createRequest(isNanny ? URLConstants.JZ_CLEANING_BAOMU_DETAILS_URL : URLConstants.JZ_CLEANING_YUESAO_DETAILS_URL, map, new OnMyActivityRequestListener<NannyAndMaternityMatronDesBean>(this) {
            @Override
            public void onSuccess(NannyAndMaternityMatronDesBean bean) {
                // 设置数据
                setData(bean);
            }
        });
    }

    private void setData(NannyAndMaternityMatronDesBean bean) {
        data = bean.getData();
        // 头像
        LocalLifePicassoUtils.setImageBigHasCallBack(mContext, data.getSimg(), iv_icon, new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                // 下载车成功，刷新
                blurLayout.postDelayed(() -> blurLayout.invalidate(), 200);
                return false;
            }
        });
        // 姓名+id
        String name = data.getName();
        if (TextUtils.isEmpty(name))
            name = "";
        tv_name_id.setText(Spans.builder()
                .text(name).color(ContextCompat.getColor(mContext, R.color.gray_555555)).size(18).style(TextStyle.BOLD)
//                .text("（" + data.getId() + "）").color(ContextCompat.getColor(mContext, R.color.black_252525)).size(12)
                .build());
        // 详细描述
        tv_des.setText(data.getAds());
        // 好评
        String praise = data.getPraise();
        if (TextUtils.isEmpty(praise))
            praise = "- -";
        tv_high_praise.setText(Spans.builder()
                .text(praise + "%")
                .text("\n好评").color(ContextCompat.getColor(mContext, R.color.gray_555555)).size(12)
                .build());
        // 经验
        String experience = data.getExperience();
        if (TextUtils.isEmpty(experience))
            experience = "- -";
        tv_experience.setText(Spans.builder()
                .text(experience + "个月")
                .text("\n经验").color(ContextCompat.getColor(mContext, R.color.gray_555555)).size(12)
                .build());
        // 月薪
        String price = data.getPrice();
        tv_monthly_salary.setText(Spans.builder()
                .text("￥" + StringUtil.subZeroAndDot(NumberFormatUtils.toDouble(price) * 2 + ""))
                .text("\n月薪").color(ContextCompat.getColor(mContext, R.color.gray_555555)).size(12)
                .build());
        // 档期（可约时间）
        List<ScheduleBean> schedule = data.getSchedule();
        // 展示三个
        List<ScheduleBean> scheduleBeanList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            scheduleBeanList.add(schedule.get(i));
        }
        mScheduleAdapter.addList(scheduleBeanList);
        // 总在岗X天
        String onduty = data.getOnduty();
        if (TextUtils.isEmpty(onduty))
            onduty = "";
        tv_all_days.setText(Spans.builder()
                .text("总在岗")
                .text(onduty).color(ContextCompat.getColor(mContext, R.color.orange_ff725c))
                .text("天")
                .build());
        // 预约X次
        String sale = data.getSale();
        if (TextUtils.isEmpty(sale))
            sale = "";
        tv_sale_num.setText(Spans.builder()
                .text("预约")
                .text(sale).color(ContextCompat.getColor(mContext, R.color.orange_ff725c))
                .text("次")
                .build());
        // 浏览X次
        String pv = data.getPv();
        if (TextUtils.isEmpty(pv))
            pv = "";
        tv_pv_num.setText(Spans.builder()
                .text("浏览")
                .text(pv).color(ContextCompat.getColor(mContext, R.color.orange_ff725c))
                .text("次")
                .build());
        // 距离
        String juli = data.getJuli();
        if (!TextUtils.isEmpty(juli)) {
            mJuLiTextView.setText(String.format("%skm", StringUtil.saveTwoDecimal(NumberFormatUtils.toDouble(juli) / 1000)));
        }

        // 标签
        List<String> duties = data.getDuties();
        if (duties == null || duties.size() == 0) {
            // 没有标签
            ll_tabs.setVisibility(View.GONE);
        } else {
            // 有标签
            ll_tabs.setVisibility(View.VISIBLE);
            tabsAdapter.clearAndAddList(duties);
        }
        // 各种证
        List<NannyAndMaternityMatronDesBean.DataBean.AbilityBean> ability = data.getAbility();
        if (ability == null || ability.size() == 0) {
            // 没有证
            ll_certificate.setVisibility(View.GONE);
        } else {
            // 有证
            ll_certificate.setVisibility(View.VISIBLE);
            certificateAdapter.clearAndAddList(ability);
        }
        // 基本信息，工作信息，培训信息
        nannyAndMaternityMatronDesTabFragment.setData("0", data);
        // 证件图片
        List<String> paperwork = data.getPaperwork();
        if (paperwork == null || paperwork.size() == 0) {
            // 没有证件
            ll_id_photo.setVisibility(View.GONE);
        } else {
            // 有证件
            ll_id_photo.setVisibility(View.VISIBLE);
            idPhotoAdapter.clearAndAddList(paperwork);
        }
        // 类似月嫂推荐
        List<NannyAndMaternityMatronListBean.DataBean> recommend = data.getRecommend();
        if (recommend == null || recommend.size() == 0) {
            // 没有月嫂推荐
            ll_recommend_layout.setVisibility(View.GONE);
        } else {
            // 有月嫂推荐
            ll_recommend_layout.setVisibility(View.VISIBLE);
            recommendAdapter.clearAndAddList(recommend);
        }

        if (data.getIs_cell() == 1) {// 未收藏
            mCollectImageView.setImageResource(R.mipmap.collect_gray_icon);
            mCollectImageViews.setImageResource(R.mipmap.collect_normal);
        } else {// 收藏
            mCollectImageView.setImageResource(R.mipmap.collect_red_icon);
            mCollectImageViews.setImageResource(R.mipmap.red_heart_stroke);
        }

    }

    /**
     * 设置标题栏按钮透明度
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
     * 添加收藏
     */
    private void addCollect(String id) {
        User loginUserDoLogin = BaseApplication.getInstance().getLoginUserDoLogin(this);
        if (loginUserDoLogin == null)
            return;
        Map<String, String> map = new HashMap<>();
        map.put(URLConstants.REQUEST_PARAM_UID, loginUserDoLogin.getData().getId());
        map.put(URLConstants.REQUEST_PARAM_PID, id);
        map.put(URLConstants.REQUEST_PARAM_TYPE, isNanny ? "baomu" : "yuesao");
        RequestManager.createRequest(URLConstants.REQUEST_CELL_JZ, map, new OnMyActivityRequestListener<BaseBean>(this) {
            @Override
            public void onSuccess(BaseBean bean) {
                if (data.getIs_cell() == 1) {// 收藏商品
                    data.setIs_cell(2);
                    if (isHeartFlag) {
                        mCollectImageView.setImageResource(R.mipmap.collect_red_icon);
                        mCollectImageViews.setImageResource(R.mipmap.red_heart_stroke);
                    } else {
                        mCollectImageView.setImageResource(R.mipmap.red_heart_stroke);
                        mCollectImageViews.setImageResource(R.mipmap.collect_red_icon);
                    }
                    ToastManager.showShortToast(mContext, "收藏成功");
                } else {// 取消商品收藏
                    data.setIs_cell(1);
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
     * 设置收藏按钮 状态
     */
    private void setCollectIcon(boolean flag) {
        if (data != null) {
            if (flag) {
                if (data.getIs_cell() == 1) {
                    // 未收藏
                    mCollectImageView.setImageResource(R.mipmap.collect_gray_icon);
                    mCollectImageViews.setImageResource(R.mipmap.collect_normal);
                } else {
                    // 收藏
                    mCollectImageView.setImageResource(R.mipmap.collect_red_icon);
                    mCollectImageViews.setImageResource(R.mipmap.red_heart_stroke);
                }
            } else {
                if (data.getIs_cell() == 1) {
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
}
