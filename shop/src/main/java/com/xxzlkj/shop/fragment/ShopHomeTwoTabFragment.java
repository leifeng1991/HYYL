package com.xxzlkj.shop.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xxzlkj.shop.R;
import com.xxzlkj.shop.activity.ShopHomeActivity;
import com.xxzlkj.shop.adapter.ShopHomeTwoTabFragmentNavigatorAdapter;
import com.xxzlkj.shop.config.GlobalParams;
import com.xxzlkj.shop.config.URLConstants;
import com.xxzlkj.shop.model.ShopAppBean;
import com.xxzlkj.zhaolinshare.base.base.BaseActivity;
import com.xxzlkj.zhaolinshare.base.base.BaseFragmentPagerAdapter;
import com.xxzlkj.zhaolinshare.base.base.ReuseViewFragment;
import com.xxzlkj.zhaolinshare.base.net.OnMyActivityRequestListener;
import com.xxzlkj.zhaolinshare.base.net.RequestManager;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * 描述:商城首页Two栏目
 *
 * @author zhangrq
 *         2017/6/26 10:17
 */
public class ShopHomeTwoTabFragment extends ReuseViewFragment implements IOnFragmentBackPressed {

    private ViewPager viewPager;
    private ShopHomeTabFragment parentFragment;
    private String twoTabId;
    private ImageView iv_icon;
    private TextView tv_title;
    private MagicIndicator magicIndicator;
    private String type;

    /**
     * @param parentArgs 父类里面的参数，详细请看父类 ShopHomeTabFragment 接收的值
     * @param twoTabId   第二级列表id
     */
    public static ShopHomeTwoTabFragment newInstance(ShopHomeTabFragment parentFragment, Bundle parentArgs, String twoTabId) {
        ShopHomeTwoTabFragment fragment = new ShopHomeTwoTabFragment();
        fragment.setArguments(parentArgs);
        fragment.parentFragment = parentFragment;
        fragment.twoTabId = twoTabId;
        return fragment;
    }

    @Override
    public View loadViewLayout(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_tab_shop_home_two, container, false);
    }

    @Override
    protected void findViewById() {
        // 返回按钮
        iv_icon = getView(R.id.iv_icon);
        tv_title = getView(R.id.tv_title);
        // 顶部的tab
        magicIndicator = getView(R.id.magic_indicator);
        // 内容体
        viewPager = getView(R.id.viewPager);
        // 绑定ViewPager
        ViewPagerHelper.bind(magicIndicator, viewPager);
    }

    @Override
    public void setListener() {
        // 顶部返回按钮点击
        getView(R.id.ll_back).setOnClickListener(v -> mActivity.onBackPressed());
    }


    @Override
    public void processLogic() {
        // 设置返回按钮
        iv_icon.setImageResource(R.mipmap.ic_btn_back);
        tv_title.setText("返回首页");
        // 设置当前的页面
        ((ShopHomeActivity) mActivity).setCurrentFragment(false);
        // 增加到返回栈中，用于返回键的时候控制此页面
        parentFragment.addFragmentBackPressed(this);
        // 获取参数
        type = getArguments().getString(ShopHomeTabFragment.TYPE);//  1商城首页 2 火爆预售;3 兆邻团购 ;

        // 刷新获取网络数据
        viewPager.postDelayed(this::getNetData, 500);
    }

    public void setRefresh() {
        getNetData();
    }

    /**
     * 获取所有的二级Tabs 然后设置对应的页面
     */
    private void getNetData() {
        HashMap<String, String> map = new HashMap<>();
        String urlStr = "";
//        5商城首页 2 火爆预售;3 兆邻团购 ;
        if ("5".equals(type)) {
            // 1.商城首页（时速达）
            urlStr = URLConstants.SHOP_APP_STORE_URL;
            map.put("store_id", GlobalParams.storeId);
        } else if ("2".equals(type)) {
            // 2 火爆预售
            urlStr = URLConstants.ADVANCE_GROUP_URL;
        } else if ("3".equals(type)) {
            // 3 兆邻团购
            urlStr = URLConstants.GROUPON_GROUP_URL;
        }
        RequestManager.createRequest(urlStr, map, new OnMyActivityRequestListener<ShopAppBean>((BaseActivity) mActivity) {

            @Override
            public void onSuccess(ShopAppBean bean) {
                // ShopAppBean 内部包含了二级Tabs、三级tabs
                List<ShopAppBean.DataBean> data = bean.getData();
                // 设置二级Tabs列表
                // 指示器
                CommonNavigator commonNavigator = new CommonNavigator(mContext);
                ShopHomeTwoTabFragmentNavigatorAdapter indicatorAdapter = new ShopHomeTwoTabFragmentNavigatorAdapter(viewPager, data);
                commonNavigator.setAdapter(indicatorAdapter);
                magicIndicator.setNavigator(commonNavigator);

                // 设置ViewPager、Fragment设置内容
                ArrayList<Fragment> fragments = new ArrayList<>();
                for (ShopAppBean.DataBean appBean : data) {
                    fragments.add(ShopHomeTwoTabTabFragment.newInstance(appBean, getArguments()));
                }
                viewPager.setAdapter(new BaseFragmentPagerAdapter(getChildFragmentManager(), fragments));
                // 设置选中
                setChecked(data);

            }
        });
    }

    private void setChecked(List<ShopAppBean.DataBean> data) {
        int selectPosition = 0;
        for (int i = 0; i < data.size(); i++) {
            ShopAppBean.DataBean dataBean = data.get(i);
            if (TextUtils.equals(dataBean.getId(), twoTabId)) {
                // 设置被选中，viewpager设置页面
                dataBean.setChecked(true);
                selectPosition = i;
            } else {
                dataBean.setChecked(false);
            }
        }
        // 设置viewPager切换
        viewPager.setCurrentItem(selectPosition);
    }

    @Override
    public boolean onBackPressed() {
        // 按返回键，先移除自己，再展示OneFragment，此方法在ShopHomeActivity中调用
        getFragmentManager().popBackStack();
        parentFragment.showOneFragment();
        return false;
    }


}
