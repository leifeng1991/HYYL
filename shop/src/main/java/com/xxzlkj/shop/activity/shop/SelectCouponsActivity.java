package com.xxzlkj.shop.activity.shop;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;

import com.xxzlkj.shop.R;
import com.xxzlkj.shop.adapter.MyCommonNavigatorAdapter;
import com.xxzlkj.shop.config.GlobalParams;
import com.xxzlkj.shop.config.URLConstants;
import com.xxzlkj.shop.fragment.SelectCouponsTabFragment;
import com.xxzlkj.shop.interfac.ShopLibraryInterface;
import com.xxzlkj.shop.model.SelectCouponsBean;
import com.xxzlkj.zhaolinshare.base.app.BaseApplication;
import com.xxzlkj.zhaolinshare.base.base.BaseFragmentPagerAdapter;
import com.xxzlkj.zhaolinshare.base.base.MyBaseActivity;
import com.xxzlkj.zhaolinshare.base.model.User;
import com.xxzlkj.zhaolinshare.base.net.OnMyActivityRequestListener;
import com.xxzlkj.zhaolinshare.base.net.OnRequestListener;
import com.xxzlkj.zhaolinshare.base.net.RequestManager;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * 描述:选择优惠券
 *
 * @author zhangrq
 *         2017/3/21 14:48
 */
public class SelectCouponsActivity extends MyBaseActivity {
    public static final String CHECKED_ITEM = "checked_item";
    public static final String IDS = "ids";
    public static final String NUM = "num";
    public static final String GROUPON_TEAM_ID = "grouponTeamId";
    private ViewPager viewPager;
    private MagicIndicator magicIndicator;
    private List<Fragment> fragmentList = new ArrayList<>();
    private List<String> titleList = new ArrayList<>();
    private CommonNavigator commonNavigator;
    private String ids;
    private String num;
    private SelectCouponsTabFragment fragment0;
    private SelectCouponsTabFragment fragment1;
    private String grouponTeamId;

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_coupons_select);
    }

    @Override
    protected void findViewById() {
        magicIndicator = getView(R.id.magic_indicator);
        viewPager = getView(R.id.viewPager);
    }

    @Override
    public void setListener() {

    }

    @Override
    public void processLogic() {
        setTitleLeftBack();
        setTitleName("优惠券");
        setTitleRightImage(R.mipmap.ic_coupons_help);
        Bundle extras = getIntent().getExtras();

        titleList.add("可使用");
        fragment0 = SelectCouponsTabFragment.newInstance(extras, this, "1");
        fragmentList.add(fragment0);

        titleList.add("不可用");
        fragment1 = SelectCouponsTabFragment.newInstance(extras, this, "2");
        fragmentList.add(fragment1);

        // 初始化viewPager和指示器
        BaseFragmentPagerAdapter fragmentPagerAdapter = new BaseFragmentPagerAdapter(getSupportFragmentManager(), fragmentList);
        viewPager.setOffscreenPageLimit(fragmentList.size());// 设置预加载的数量
        viewPager.setAdapter(fragmentPagerAdapter);
        // 指示器，稳健盈、灵活投
        commonNavigator = new CommonNavigator(mContext);
        commonNavigator.setScrollPivotX(1f);
        MyCommonNavigatorAdapter indicatorAdapter = new MyCommonNavigatorAdapter(viewPager, titleList);
        commonNavigator.setAdapter(indicatorAdapter);
        magicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(magicIndicator, viewPager);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            ids = bundle.getString(IDS);
            num = bundle.getString(NUM);
            grouponTeamId = bundle.getString(GROUPON_TEAM_ID);
        }

        getNetData();
    }

    public void getNetData() {
        // 获取网络数据
        getNetDataByPublic(ids, num, grouponTeamId, new OnMyActivityRequestListener<SelectCouponsBean>(this) {

            @Override
            public void onSuccess(SelectCouponsBean bean) {
                // 有数据
                SelectCouponsBean.DataBean data = bean.getData();
                // 设置title 数量
                setTitleNum(0, data.getUsable().size() + "");
                setTitleNum(1, data.getDisable().size() + "");
                // 给fragment传值
                fragment0.setListData(data.getUsable());
                fragment1.setListData(data.getDisable());
            }

            @Override
            public void onFailed(boolean isError, String code, String message) {
                super.onFailed(isError, code, message);
                fragment0.setFailed();
                fragment1.setFailed();
            }
        });
    }

    /**
     * 通用的获取网络数据
     *
     * @param ids           必传，商品id，用逗号隔开
     * @param num           必传，商品id对应的数量，用逗号隔开
     * @param grouponTeamId 选传，团购小组id，如果为团购时必传
     */
    public static void getNetDataByPublic(String ids, String num, String grouponTeamId, OnRequestListener<SelectCouponsBean> listener) {
        User user = BaseApplication.getInstance().getLoginUser();
        if (user == null || TextUtils.isEmpty(ids) || TextUtils.isEmpty(num) || TextUtils.isEmpty(GlobalParams.storeId)) {
            return;
        }
        String uid = user.getData().getId();
        HashMap<String, String> stringStringHashMap = new HashMap<>();
//        uid	用户id
//        ids	商品id用逗号隔开
//        num	商品数量
//        grouponTeamId（团购小组id） 如果为团购时传

        stringStringHashMap.put("uid", uid);
        stringStringHashMap.put("ids", ids);
        stringStringHashMap.put("num", num);
        if (!TextUtils.isEmpty(grouponTeamId))
            stringStringHashMap.put("grouponTeamId", grouponTeamId);

        stringStringHashMap.put(URLConstants.REQUEST_PARAM_STORE_ID, GlobalParams.storeId);

        RequestManager.createRequest(URLConstants.USABLE_COUPON_URL, stringStringHashMap, listener);
    }

    /**
     * 设置title的数量
     */
    private void setTitleNum(int titleIndex, String messageNum) {
        SimplePagerTitleView pagerTitleView = (SimplePagerTitleView) commonNavigator.getPagerTitleView(titleIndex);
        pagerTitleView.setText(titleList.get(titleIndex) + "(" + messageNum + ")");
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int i = v.getId();
        if (i == R.id.iv_title_right) {// 帮助
            if (BaseApplication.getInstance() instanceof ShopLibraryInterface) {
                // 让主项目处理
                ((ShopLibraryInterface) BaseApplication.getInstance()).jumpToHasTitleWebView(this, URLConstants.COUPON_URL, "使用说明");
            }
        }
    }

    /**
     * @param checkedBean   选择的bean
     * @param ids           商品id，用逗号隔开
     * @param num           商品id对应的数量，用逗号隔开
     * @param grouponTeamId 选传，团购小组id，如果为团购时必传
     */
    public static Intent newIntent(Context context, SelectCouponsBean.DataBean.ItemBean checkedBean, String ids, String num, String grouponTeamId) {
        Intent intent = new Intent(context, SelectCouponsActivity.class);
        Bundle extras = new Bundle();
        extras.putSerializable(CHECKED_ITEM, checkedBean);
        extras.putString(IDS, ids);
        extras.putString(NUM, num);
        extras.putString(GROUPON_TEAM_ID, grouponTeamId);
        intent.putExtras(extras);
        return intent;
    }

    /**
     * 获取选中后的结果值,为空代表没有选
     */
    public static SelectCouponsBean.DataBean.ItemBean getResult(Intent data) {
        if (data == null)
            return null;
        return (SelectCouponsBean.DataBean.ItemBean) data.getSerializableExtra(CHECKED_ITEM);
    }

}
