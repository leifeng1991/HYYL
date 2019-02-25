package com.xxzlkj.huayiyanglao.activity.foodorder;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.services.core.PoiItem;
import com.xxzlkj.huayiyanglao.R;
import com.xxzlkj.huayiyanglao.activity.address.LocationActivity;
import com.xxzlkj.huayiyanglao.adapter.NearDiningRoomListAdapter;
import com.xxzlkj.huayiyanglao.config.ZLConstants;
import com.xxzlkj.huayiyanglao.config.ZLURLConstants;
import com.xxzlkj.huayiyanglao.model.FoodsShopListBean;
import com.xxzlkj.huayiyanglao.util.GaodeLocationUtil;
import com.xxzlkj.huayiyanglao.weight.ShapeButton;
import com.xxzlkj.zhaolinshare.base.base.BaseAdapter;
import com.xxzlkj.zhaolinshare.base.base.MyBaseActivity;
import com.xxzlkj.zhaolinshare.base.net.OnMyActivityRequestListener;
import com.xxzlkj.zhaolinshare.base.net.RequestManager;
import com.xxzlkj.zhaolinshare.base.util.PermissionHelper;
import com.xxzlkj.zhaolinshare.base.util.ToastManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 选择地址方式
 *
 * @author zhangrq
 */
public class SelectAddressModeActivity extends MyBaseActivity {
    private LinearLayout mHomeDeliveryLayout, mReachStoreLayout, mLocationAddressLayout;
    private ShapeButton mHomeDeliveryButton, mReachStoreButton;
    private TextView mAddressTextView;
    private RecyclerView mRecyclerView;
    private Button mOderMealButton;
    private NearDiningRoomListAdapter mNearDiningRoomListAdapter;
    private PermissionHelper mHelper;
    // 是否是外卖到家
    private boolean isHomeDelivery = true;

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, SelectAddressModeActivity.class);
        return intent;
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_select_address_mode);
    }

    @Override
    protected void findViewById() {
        mHomeDeliveryLayout = getView(R.id.id_home_delivery_layout);// 外卖到家布局
        mHomeDeliveryButton = getView(R.id.id_home_delivery);// 外卖到家按钮
        mReachStoreLayout = getView(R.id.id_reach_store_layout);// 到店食用布局
        mReachStoreButton = getView(R.id.id_reach_store);// 到店食用按钮
        mLocationAddressLayout = getView(R.id.id_location_address);// 定位地址布局
        mAddressTextView = getView(R.id.id_address);// 定位地址
        mOderMealButton = getView(R.id.id_order_meal);// 去点餐
        mRecyclerView = getView(R.id.id_recycler_view);// 附近食堂列表
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setNestedScrollingEnabled(false);
        mNearDiningRoomListAdapter = new NearDiningRoomListAdapter(mContext, R.layout.adapter_near_dining_room_list_item);
        mRecyclerView.setAdapter(mNearDiningRoomListAdapter);
    }

    @Override
    protected void setListener() {
        // 外卖到家点击事件
        mHomeDeliveryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isHomeDelivery = true;
                setCheckedState();

            }
        });
        mHomeDeliveryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isHomeDelivery = true;
                setCheckedState();

            }
        });
        // 到店食用
        mReachStoreLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isHomeDelivery = false;
                setCheckedState();
            }
        });
        mReachStoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isHomeDelivery = false;
                setCheckedState();
            }
        });
        // item点击事件
        mNearDiningRoomListAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener<FoodsShopListBean.DataBean>() {
            @Override
            public void onClick(int position, FoodsShopListBean.DataBean item) {
                // 改变选中状态
                if (mNearDiningRoomListAdapter.selelctPosition != position) {
                    mNearDiningRoomListAdapter.selelctPosition = position;
                    mNearDiningRoomListAdapter.notifyDataSetChanged();
                }
            }
        });
        // 去点餐
        mOderMealButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转到外卖列表页
                if (mNearDiningRoomListAdapter != null && mNearDiningRoomListAdapter.getItemCount() > 0) {
                    FoodsShopListBean.DataBean dataBean = mNearDiningRoomListAdapter.getList().get(mNearDiningRoomListAdapter.selelctPosition);
                    startActivity(TakeOutActivity.newIntent(mContext, isHomeDelivery,
                            mNearDiningRoomListAdapter.getList().get(mNearDiningRoomListAdapter.selelctPosition).getId(), dataBean.getTitle()));
                }

            }
        });
        mLocationAddressLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(LocationActivity.newIntent(mContext), ZLConstants.Integers.REQUEST_CODE_ADDRESS);
            }
        });
    }

    @Override
    protected void processLogic() {
        setTitleLeftBack();
        setTitleName("选择地址方式");

        mHelper = new PermissionHelper(this);
        mHelper.requestPermissions("请授予[位置]权限，否则无法获取附近食堂", new PermissionHelper.PermissionListener() {
                    @Override
                    public void doAfterGrand(String... permission) {

                        // 请求权限成功
                        final GaodeLocationUtil gaodeLocationUtil = new GaodeLocationUtil();
                        AMapLocationClientOption defaultOption = gaodeLocationUtil.getDefaultOption();
                        gaodeLocationUtil.initLocation(mContext, defaultOption, aMapLocation -> {
                            if (null != aMapLocation) {
                                // 定位成功
                                mAddressTextView.setText(aMapLocation.getAddress());
                                requestData(aMapLocation.getLatitude(), aMapLocation.getLongitude());
                                // 只定位一次
                                gaodeLocationUtil.stopLocation();
                                gaodeLocationUtil.destroyLocation();
                            } else {
                                ToastManager.showMsgToast(mContext, "定位失败");
                            }

                        });
                        //开始定位
                        gaodeLocationUtil.startLocation();
                    }

                    @Override
                    public void doAfterDenied(String... permission) {
                    }
                }, Manifest.permission.ACCESS_COARSE_LOCATION
                , Manifest.permission.ACCESS_FINE_LOCATION);

        setCheckedState();
    }

    private void setCheckedState() {
        setShapeButtonCheckedState(mHomeDeliveryButton, isHomeDelivery);
        setShapeButtonCheckedState(mReachStoreButton, !isHomeDelivery);
    }

    private void setShapeButtonCheckedState(ShapeButton shapeButton, boolean isSolid) {
        shapeButton.setBackgroundColor(isSolid ? 0xff54B1F8 : 0xffffffff);
        shapeButton.setBorderColor(isSolid ? 0xff54B1F8 : 0xff979797);
        shapeButton.setTextColor(ContextCompat.getColor(mContext, isSolid ? R.color.white : R.color.text_4));
    }

    private void requestData(double latitude, double longitude) {
        Map<String, String> map = new HashMap<>();
        map.put(ZLConstants.Params.LATITUDE, latitude + "");
        map.put(ZLConstants.Params.LONGITUDE, longitude + "");
        RequestManager.createRequest(ZLURLConstants.REQUEST_FOODS_SHOP_LIST_URL, map, new OnMyActivityRequestListener<FoodsShopListBean>(this) {
            @Override
            public void onSuccess(FoodsShopListBean bean) {
                List<FoodsShopListBean.DataBean> data = bean.getData();
                if (data != null)
                    mNearDiningRoomListAdapter.clearAndAddList(data);
            }
        });
    }

    //直接把参数交给mHelper就行了
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mHelper.handleRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            switch (requestCode) {
                case ZLConstants.Integers.REQUEST_CODE_ADDRESS:
                    PoiItem result = LocationActivity.getResult(data);
                    if (result != null) {
                        mAddressTextView.setText(String.format("%s%s%s", result.getCityName(), result.getAdName(), result.getSnippet()));
                        requestData(result.getLatLonPoint().getLatitude(), result.getLatLonPoint().getLongitude());
                    }
                    break;
            }
        }
    }
}
