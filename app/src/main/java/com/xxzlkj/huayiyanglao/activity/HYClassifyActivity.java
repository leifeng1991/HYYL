package com.xxzlkj.huayiyanglao.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xxzlkj.huayiyanglao.R;
import com.xxzlkj.huayiyanglao.adapter.ClassifyAdapter;
import com.xxzlkj.huayiyanglao.adapter.ClassifyGridLayoutManager;
import com.xxzlkj.huayiyanglao.adapter.ClassifyMyAppAdapter;
import com.xxzlkj.huayiyanglao.adapter.ClassifyPagerAdapter;
import com.xxzlkj.huayiyanglao.app.ZhaoLinApplication;
import com.xxzlkj.huayiyanglao.config.ZLConstants;
import com.xxzlkj.huayiyanglao.event.ClassifyAnimationEvent;
import com.xxzlkj.huayiyanglao.event.ClassifyMyAppAddEvent;
import com.xxzlkj.huayiyanglao.event.OtherServersChangeEvent;
import com.xxzlkj.huayiyanglao.event.RecentAppChangeEvent;
import com.xxzlkj.huayiyanglao.model.AllAppBean;
import com.xxzlkj.huayiyanglao.model.ClassifyItemBean;
import com.xxzlkj.huayiyanglao.model.ClassifyItemItemBean;
import com.xxzlkj.huayiyanglao.util.ZLActivityUtils;
import com.xxzlkj.shop.config.GlobalParams;
import com.xxzlkj.shop.config.URLConstants;
import com.xxzlkj.zhaolinshare.base.base.BaseActivity;
import com.xxzlkj.zhaolinshare.base.base.BaseAdapter;
import com.xxzlkj.zhaolinshare.base.base.MyBaseActivity;
import com.xxzlkj.zhaolinshare.base.model.BaseBean;
import com.xxzlkj.zhaolinshare.base.model.User;
import com.xxzlkj.zhaolinshare.base.net.OnMyActivityRequestListener;
import com.xxzlkj.zhaolinshare.base.net.RequestManager;
import com.xxzlkj.zhaolinshare.base.util.BitmapUtils;
import com.xxzlkj.zhaolinshare.base.util.SystemBarUtils;
import com.xxzlkj.zhaolinshare.base.util.ToastManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * 分类
 */
public class HYClassifyActivity extends BaseActivity {
    private RecyclerView rv_my_app, rv_other_services;
    private ClassifyMyAppAdapter myAppAdapter;
    private ImageView iv_classify_left;
    private ViewPager vp_classify;
    private TextView tv_edit, tv_my_app_empty;
    private LinearLayout ll_recent_app;
    private ImageView iv_classify_right;
    private ClassifyPagerAdapter recentAppPagerAdapter;
    private ClassifyAdapter otherServicesAdapter;
    private FrameLayout fl_classify_root;
    private NestedScrollView scroll_view;
    private ArrayList<ClassifyItemItemBean> recentAppList = new ArrayList<>();
    private boolean isEditItem;

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, HYClassifyActivity.class);
        return intent;
    }

    @Override
    protected void loadViewLayout() {
        SystemBarUtils.addStatusBarTranslucentFlags(this);
        SystemBarUtils.setStatusBarLightMode(this, true);
        setContentView(R.layout.fragment_classify);
    }

    @Override
    protected void findViewById() {
        SystemBarUtils.setPaddingTopStatusBarHeight(this, getView(R.id.rl_top));
        fl_classify_root = getView(R.id.fl_classify_root);
        tv_edit = getView(R.id.tv_edit);
        scroll_view = getView(R.id.scroll_view);
        // 最近应用
        ll_recent_app = getView(R.id.ll_recent_app);
        iv_classify_left = getView(R.id.iv_classify_left);
        vp_classify = getView(R.id.vp_classify);
        iv_classify_right = getView(R.id.iv_classify_right);
//        for (int i = 0; i < 10; i++)
//            recentAppList.add(new ClassifyItemItemBean("最近应用" + i, 2));
        recentAppPagerAdapter = new ClassifyPagerAdapter(this, recentAppList);
        vp_classify.setAdapter(recentAppPagerAdapter);

        //我的应用
        rv_my_app = getView(R.id.rv_my_app);
        tv_my_app_empty = getView(R.id.tv_my_app_empty);
        rv_my_app.setLayoutManager(new ClassifyGridLayoutManager(mContext, 4));
        myAppAdapter = new ClassifyMyAppAdapter(mContext, R.layout.adapter_classify_my_app_item);
        rv_my_app.setAdapter(myAppAdapter);

//        ArrayList<ClassifyItemItemBean> list0 = new ArrayList<>();
//        for (int i = 0; i < 10; i++)
//            list0.add(new ClassifyItemItemBean("我的应用" + i, 2));
//        myAppAdapter.addList(list0);

        // 其他服务
        rv_other_services = getView(R.id.rv_other_services);
        rv_other_services.setLayoutManager(new LinearLayoutManager(mContext) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        otherServicesAdapter = new ClassifyAdapter(this, mContext, R.layout.adapter_classify_item);
        rv_other_services.setAdapter(otherServicesAdapter);
        // 假数据开始
//        List<ClassifyItemBean> otherServicesList = new ArrayList<>();
//
//        ArrayList<ClassifyItemItemBean> list1 = new ArrayList<>();
//        for (int i = 0; i < 10; i++)
//            list1.add(new ClassifyItemItemBean("保洁服务" + i, 2));
//        otherServicesList.add(new ClassifyItemBean("保洁服务", list1));
//
//        ArrayList<ClassifyItemItemBean> list2 = new ArrayList<>();
//        for (int i = 0; i < 10; i++)
//            list2.add(new ClassifyItemItemBean("家政服务" + i, 2));
//        otherServicesList.add(new ClassifyItemBean("家政服务", list2));
//
//        ArrayList<ClassifyItemItemBean> list3 = new ArrayList<>();
//        for (int i = 0; i < 10; i++)
//            list3.add(new ClassifyItemItemBean("快递服务" + i, 2));
//        otherServicesList.add(new ClassifyItemBean("快递服务", list3));
//
//        ArrayList<ClassifyItemItemBean> list4 = new ArrayList<>();
//        for (int i = 0; i < 10; i++)
//            list4.add(new ClassifyItemItemBean("养老服务" + i, 2));
//        otherServicesList.add(new ClassifyItemBean("养老服务", list4));
//
//        otherServicesAdapter.addList(otherServicesList);

        // 默认设置
        setRecentAppView(false);
    }

    @Override
    public void setListener() {
        EventBus.getDefault().register(this);

        tv_edit.setOnClickListener(this);
        iv_classify_left.setOnClickListener(this);
        iv_classify_right.setOnClickListener(this);

        myAppAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener<ClassifyItemItemBean>() {
            @Override
            public void onClick(int position, ClassifyItemItemBean item) {
                // 根据Type和to，跳转
                ZLActivityUtils.jumpToActivityByType(HYClassifyActivity.this, item.getType(), item.getTo(), item.getTitle());

            }
        });
    }

    @Override
    public void processLogic() {

    }

    @Override
    public void onResume() {
        super.onResume();
        // 获取数据
        getNetData();
    }

    @Override
    public void onPause() {
        super.onPause();
        resetRightEditText();
    }

    private void getNetData() {
        HashMap<String, String> stringStringHashMap = new HashMap<>();
        User loginUser = ZhaoLinApplication.getInstance().getLoginUser();
        // 小区id
        if (!TextUtils.isEmpty(GlobalParams.communityId))
            stringStringHashMap.put(ZLConstants.Params.COMMUNITY_ID, GlobalParams.communityId);
        // 用户id
        stringStringHashMap.put("uid", loginUser == null ? "0" : loginUser.getData().getId());
        RequestManager.createRequest(URLConstants.APP_ALL_URL, stringStringHashMap, new OnMyActivityRequestListener<AllAppBean>((BaseActivity) this) {
            @Override
            public void onSuccess(AllAppBean bean) {
                setData(bean);
            }
        });
    }

    private void setData(AllAppBean bean) {
        List<ClassifyItemBean> otherServicesList = new ArrayList<>();
        for (AllAppBean.DataBean dataBean : bean.getData()) {
//            0为我的应用 -1为最近使用
            ArrayList<ClassifyItemItemBean> appList = dataBean.getApp();
            if ("-1".equals(dataBean.getId())) {
                // 最近使用应用
                if (appList == null || appList.size() == 0) {
                    // 无最近应用
                    setRecentAppView(false);
                } else {
                    // 有最近应用设置显示，数据
                    setRecentAppView(true);
                    recentAppList.clear();
                    recentAppList.addAll(appList);
                    recentAppPagerAdapter.notifyDataSetChanged();
                }
            } else if ("0".equals(dataBean.getId())) {
                // 0为我的应用
                if (appList != null && appList.size() != 0) {
                    // 有 设置显示，数据
                    myAppAdapter.clear();
                    myAppAdapter.addList(appList);// 已经通知了
                }
                checkMyAppData();
            } else {
                // 其他服务
                otherServicesList.add(new ClassifyItemBean(dataBean.getTitle(), appList));
            }
        }
        // 其他服务
        otherServicesAdapter.clear();
        otherServicesAdapter.addList(otherServicesList);
    }


    // 设置我的APP的数据
    private void checkMyAppData() {
        if (myAppAdapter.getItemCount() == 0) {
            // 无
            tv_my_app_empty.setVisibility(View.VISIBLE);
            rv_my_app.setVisibility(View.GONE);
        } else {
            // 有 设置显示，数据
            rv_my_app.setVisibility(View.VISIBLE);
            tv_my_app_empty.setVisibility(View.GONE);
        }
    }

    /**
     * 设置最近应用是否显示
     */
    private void setRecentAppView(boolean isShow) {
        ll_recent_app.setVisibility(isShow ? View.VISIBLE : View.GONE);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) scroll_view.getLayoutParams();
        int dimensionPixelSize = getResources().getDimensionPixelSize(R.dimen.classify_margin_top);
        layoutParams.setMargins(layoutParams.leftMargin, isShow ? dimensionPixelSize : 0, layoutParams.rightMargin, layoutParams.bottomMargin);
    }


    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void notifyMyAppAddItem(ClassifyMyAppAddEvent classifyMyAppAddEvent) {
        if (myAppAdapter == null || classifyMyAppAddEvent == null || classifyMyAppAddEvent.getItemItemBean() == null) {
            return;
        }
        ClassifyItemItemBean itemItemBean = classifyMyAppAddEvent.getItemItemBean();
        if (!myAppAdapter.getList().contains(itemItemBean)) {//防止重复添加，里面判断的是title
            myAppAdapter.addItemAtLast(itemItemBean);
            otherServicesAdapter.notifyDataSetChanged();
        }

        // 检查自己是否有数据
        checkMyAppData();
        isEditItem = true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void notifyOtherServersChange(OtherServersChangeEvent classifyMyAppAddEvent) {
        if (otherServicesAdapter == null || classifyMyAppAddEvent == null || classifyMyAppAddEvent.getItemItemBean() == null) {
            return;
        }
        // 检查自己是否有数据
        checkMyAppData();
        isEditItem = true;

        ClassifyItemItemBean itemItemBean = classifyMyAppAddEvent.getItemItemBean();
        String name = itemItemBean.getTitle();
        int state = itemItemBean.getState();
        // 通知其他服务
        List<ClassifyItemBean> list = otherServicesAdapter.getList();
        for (ClassifyItemBean classifyItemBean : list) {
            for (ClassifyItemItemBean bean : classifyItemBean.getList()) {
                if (TextUtils.equals(bean.getTitle(), name)) {
                    bean.setState(state);
                    otherServicesAdapter.notifyDataSetChanged();
                    break;//只执行一次
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void notifyRecentAppChange(RecentAppChangeEvent recentAppChangeEvent) {
        if (recentAppPagerAdapter == null || recentAppChangeEvent == null || recentAppChangeEvent.getItemItemBean() == null) {
            return;
        }
        // 检查自己是否有数据
        checkMyAppData();
        isEditItem = true;

        ClassifyItemItemBean itemItemBean = recentAppChangeEvent.getItemItemBean();
        String name = itemItemBean.getTitle();
        int state = itemItemBean.getState();

        // 通知顶部
        for (ClassifyItemItemBean bean : recentAppList) {
            if (TextUtils.equals(bean.getTitle(), name)) {
                bean.setState(state);
                recentAppPagerAdapter.notifyDataSetChanged();
                break;
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void addAnimation(ClassifyAnimationEvent classifyAnimationEvent) {
        if (classifyAnimationEvent == null || classifyAnimationEvent.getClickView() == null) {
            return;
        }
        View clickView = classifyAnimationEvent.getClickView();
        int[] clickViewLocation = new int[2];
        clickView.getLocationOnScreen(clickViewLocation);
        int clickViewX = clickViewLocation[0];
        int clickViewY = clickViewLocation[1];
        final ImageView imageView = new ImageView(mContext);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(clickView.getWidth(), clickView.getHeight());
//        layoutParams.leftMargin = clickViewLocation[0];
//        layoutParams.topMargin = clickViewLocation[1];
        imageView.setLayoutParams(layoutParams);
        imageView.setImageDrawable(BitmapUtils.copyViewToDrawable(clickView));
        // 添加
        final ViewGroup rootView = (ViewGroup) this.findViewById(android.R.id.content);
        rootView.addView(imageView);
        // 动画
        int[] myAppLocation = new int[2];
        rv_my_app.getLocationOnScreen(myAppLocation);
        TranslateAnimation translateAnimation = new TranslateAnimation(clickViewX, getMyAppX(), clickViewY,
                classifyAnimationEvent.isUpDown() ? clickViewY - clickView.getHeight() : clickViewY + clickView.getHeight());
        AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
        AnimationSet animationSet = new AnimationSet(false);
        animationSet.addAnimation(translateAnimation);
        animationSet.addAnimation(alphaAnimation);
        animationSet.setDuration(250);
        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                rootView.removeView(imageView);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                fl_classify_root.removeView(imageView);
            }
        });
        imageView.startAnimation(animationSet);
    }

    private float getMyAppX() {
        int i = myAppAdapter.getItemCount() % 4;
        int width = rv_my_app.getWidth() / 4;
        int[] myAppLocation = new int[2];
        rv_my_app.getLocationOnScreen(myAppLocation);
        return myAppLocation[0] + width * i;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_edit:
                // 编辑
                String trim = tv_edit.getText().toString().trim();
                if ("编辑".equals(trim)) {
                    // 编辑功能
                    setIsEditShow(true);
                    tv_edit.setText("完成");
                } else {
                    // 完成
                    resetRightEditText();
                    summitEdit();
                }
                break;
            case R.id.iv_classify_left:
                // 上一个
                if (recentAppPagerAdapter.getCount() > 0 && vp_classify.getCurrentItem() > 0) {
                    vp_classify.setCurrentItem(vp_classify.getCurrentItem() - 1);
                }
                break;
            case R.id.iv_classify_right:
                // 下一个
                if (recentAppPagerAdapter.getCount() > 0 && vp_classify.getCurrentItem() < recentAppPagerAdapter.getCount() - 1) {
                    vp_classify.setCurrentItem(vp_classify.getCurrentItem() + 1);
                }
                break;
        }
    }

    private void resetRightEditText() {
        String trim = tv_edit.getText().toString().trim();
        if ("完成".equals(trim)) {// 只有在完成的情况下才重置，防止多次无用的重置
            // 完成
            setIsEditShow(false);
            tv_edit.setText("编辑");
        }
    }

    private void summitEdit() {
        User loginUserDoLogin = ZhaoLinApplication.getInstance().getLoginUserDoLogin(this);
        if (loginUserDoLogin == null || !isEditItem) {// 没登录，或者没编辑，都不上传提交
            return;
        }
//        uid	会员id（必传）
//        pid	应用id 多个用逗号分隔
        HashMap<String, String> stringStringHashMap = new HashMap<>();
        stringStringHashMap.put("uid", loginUserDoLogin.getData().getId());
        stringStringHashMap.put("pid", getMyAppPid());
        RequestManager.createRequest(URLConstants.EDIT_MY_APP_URL, stringStringHashMap, new OnMyActivityRequestListener<BaseBean>((BaseActivity) this) {
            @Override
            public void onSuccess(BaseBean bean) {
                ToastManager.showShortToast(mContext, "上传编辑成功");
                // 重新的获取下网络
                getNetData();
            }
        });
    }

    public String getMyAppPid() {
        List<ClassifyItemItemBean> list = myAppAdapter.getList();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            if (i != 0) {
                sb.append(",");
            }
            sb.append(list.get(i).getId());
        }
        return sb.toString();
    }

    private void setIsEditShow(boolean isEdit) {
        // 最近应用
        for (ClassifyItemItemBean bean : recentAppList) {
            bean.setEdit(isEdit);
        }
        // 我的应用
        for (ClassifyItemItemBean bean : myAppAdapter.getList()) {
            bean.setEdit(isEdit);
        }
        // 其他服务
        for (ClassifyItemBean classifyItemBean : otherServicesAdapter.getList()) {
            for (ClassifyItemItemBean classifyItemItemBean : classifyItemBean.getList()) {
                classifyItemItemBean.setEdit(isEdit);
            }
        }
        // 通知
        recentAppPagerAdapter.notifyDataSetChanged();
        myAppAdapter.notifyDataSetChanged();
        otherServicesAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
