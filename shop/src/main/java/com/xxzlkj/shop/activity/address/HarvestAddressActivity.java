package com.xxzlkj.shop.activity.address;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.xxzlkj.shop.R;
import com.xxzlkj.shop.adapter.HarvestAddressAdapter;
import com.xxzlkj.shop.config.StringConstants;
import com.xxzlkj.shop.config.URLConstants;
import com.xxzlkj.shop.event.AddressChangEvent;
import com.xxzlkj.shop.event.InterviewEvent;
import com.xxzlkj.shop.event.JzSubscribeEvent;
import com.xxzlkj.shop.model.AddressBean;
import com.xxzlkj.shop.model.HarvestAddressList;
import com.xxzlkj.shop.utils.MyDrawableUtils;
import com.xxzlkj.zhaolinshare.base.app.BaseApplication;
import com.xxzlkj.zhaolinshare.base.base.BaseAdapter;
import com.xxzlkj.zhaolinshare.base.base.MyBaseActivity;
import com.xxzlkj.zhaolinshare.base.net.OnMyActivityRequestListener;
import com.xxzlkj.zhaolinshare.base.net.RequestManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.List;

/**
 * 收货地址
 */
public class HarvestAddressActivity extends MyBaseActivity {
    private RecyclerView mRecyclerView;
    private Button mAddButton;
    private RelativeLayout mNoAddressLayout;
    private HarvestAddressAdapter mHarvestAddressAdapter;
    // 跳转对象（从那个界面跳转过来） 1:商城确认订单 2:家政单次预约 3:保姆/月嫂面试地址选择
    private int type = 1;
    // 地址id
    private String addressId;

    /**
     * @param type      1:商城确认订单 2:家政单次预约 3：保姆/月嫂面试地址选择 4:我的 5:购物车（必传）
     * @param addressId 地址id 用于显示地址列表左侧线 没有时可以传""（必传）
     */
    public static Intent newIntent(Context context, int type, String addressId) {
        Intent intent = new Intent(context, HarvestAddressActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(StringConstants.INTENT_PARAM_TYPE, type);
        bundle.putString(StringConstants.INTENT_PARAM_ADDRESSID, addressId);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_shop_harvest_address);
    }

    @Override
    protected void findViewById() {
        EventBus.getDefault().register(this);
        mRecyclerView = getView(R.id.id_shop_list);// 收货地址列表
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mNoAddressLayout = getView(R.id.id_shop_no_address);// 没有收货地址时显示
        mAddButton = getView(R.id.id_shop_add);// 新增地址
        MyDrawableUtils.setButtonShapeOrange(this, mAddButton);
        // 上个页面传来的值
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            // 跳转对象（从那个界面跳转过来） 1:商城确认订单 2:家政单次预约
            type = bundle.getInt(StringConstants.INTENT_PARAM_TYPE);
            addressId = bundle.getString(StringConstants.INTENT_PARAM_ADDRESSID);
        }
    }

    @Override
    protected void setListener() {
        mAddButton.setOnClickListener(this);
    }

    @Override
    protected void processLogic() {
        setTitleLeftBack();
        setTitleName("地址");

        if (BaseApplication.getInstance().getLoginUserDoLogin(this) != null) {
            getMyAddress();
        } else {
            finish();
        }

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int i = v.getId();
        if (i == R.id.id_shop_add) {
            switch (type) {
                case 1:// 商城确认订单
                    startActivity(EditHarvestAddressActivity.newIntent(this, 1, null, 5));
                    break;
                case 5:// 购物车
                    startActivity(EditHarvestAddressActivity.newIntent(this, 1, null, 4));
                    break;
                default:
                    startActivity(EditHarvestAddressActivity.newIntent(this, 1, null, 1));
                    break;
            }


        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 获取我的地址
     */
    private void getMyAddress() {
        HashMap<String, String> map = new HashMap<>();
        map.put(URLConstants.REQUEST_PARAM_UID, BaseApplication.getInstance().getLoginUser().getData().getId());
        RequestManager.createRequest(URLConstants.REQUEST_MY_ADDRESS, map, new OnMyActivityRequestListener<HarvestAddressList>(this) {
            @Override
            public void onSuccess(HarvestAddressList bean) {
                if (bean == null) {
                    // 显示没有地址所显示的界面
                    mNoAddressLayout.setVisibility(View.VISIBLE);
                    mRecyclerView.setVisibility(View.GONE);
                } else {
                    // 显示地址列表
                    mNoAddressLayout.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                    List<AddressBean.DataBean> data = bean.getData();
                    if (mHarvestAddressAdapter == null) {
                        mHarvestAddressAdapter = new HarvestAddressAdapter(
                                HarvestAddressActivity.this, HarvestAddressActivity.this, type, addressId, R.layout.adapter_shop_harvest_address_item);
                        mHarvestAddressAdapter.addList(data);
                        mRecyclerView.setAdapter(mHarvestAddressAdapter);
                    } else {
                        mHarvestAddressAdapter.clear();
                        mHarvestAddressAdapter.addList(data);
                    }
                    mHarvestAddressAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener<AddressBean.DataBean>() {
                        @Override
                        public void onClick(int position, AddressBean.DataBean item) {
                            switch (type) {
                                case 1:// 商城确认订单
                                case 5:// 购物车
                                    Intent intent = getIntent();
                                    intent.putExtra(StringConstants.INTENT_PARAM_LOC, item);
                                    setResult(RESULT_OK, intent);
                                    finish();
                                    break;
                                case 2:// 家政预约界面
                                    EventBus.getDefault().postSticky(new JzSubscribeEvent(item.getId(), item.getPhone(), item.getAddress()));
                                    finish();
                                    break;
                                case 3:// 保姆/月嫂面试地址选择
                                    EventBus.getDefault().postSticky(new InterviewEvent(item.getAddress(), item.getId()));
                                    finish();
                                    break;
                            }
                        }
                    });
                }
            }

            @Override
            public void onFailed(boolean isError, String code, String message) {
                mNoAddressLayout.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(View.GONE);
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshData(AddressChangEvent event) {
        if (event.isChange()) {
            getMyAddress();
        }
    }
}
