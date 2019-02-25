package com.xxzlkj.huayiyanglao.activity.address;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.xxzlkj.huayiyanglao.R;
import com.xxzlkj.huayiyanglao.adapter.HarvestAddressAdapter;
import com.xxzlkj.huayiyanglao.app.ZhaoLinApplication;
import com.xxzlkj.huayiyanglao.config.ZLConstants;
import com.xxzlkj.huayiyanglao.model.AddressBean;
import com.xxzlkj.huayiyanglao.model.HarvestAddressListBean;
import com.xxzlkj.huayiyanglao.weight.ShapeButton;
import com.xxzlkj.shop.config.URLConstants;
import com.xxzlkj.zhaolinshare.base.MyRecyclerView;
import com.xxzlkj.zhaolinshare.base.base.BaseAdapter;
import com.xxzlkj.zhaolinshare.base.base.MyBaseActivity;
import com.xxzlkj.zhaolinshare.base.net.OnMyActivityRequestListener;
import com.xxzlkj.zhaolinshare.base.net.RequestManager;

import java.util.HashMap;

/**
 * 收货地址
 */
public class HarvestAddressActivity extends MyBaseActivity {
    private MyRecyclerView mRecyclerView;
    private ShapeButton mAddButton;
    private HarvestAddressAdapter mHarvestAddressAdapter;
    private View mNoDataView;
    private String mAddressId;

    public static Intent newIntent(Context context, String mAddressId) {
        Intent intent = new Intent(context, HarvestAddressActivity.class);
        intent.putExtra(ZLConstants.Strings.ADDRESS_ID, mAddressId);
        return intent;
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_harvest_address);
    }

    @Override
    protected void findViewById() {
        mRecyclerView = getView(R.id.id_ha_list);// 收货地址列表
        // 没有数据时布局
        mNoDataView = getView(R.id.id_no_data);
        // 替换空view
        mRecyclerView.setEmptyView(mNoDataView);
        mRecyclerView.setPullRefreshAndLoadingMoreEnabled(true, true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAddButton = getView(R.id.id_add_address);// 新增地址
        mAddressId = getIntent().getStringExtra(ZLConstants.Strings.ADDRESS_ID);
        mHarvestAddressAdapter = new HarvestAddressAdapter(mContext, this, mAddressId, R.layout.adapter_harvest_address_item);
        mRecyclerView.setAdapter(mHarvestAddressAdapter);
    }

    @Override
    protected void setListener() {
        // 刷新和分页加载
        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                getMyAddress();
            }

            @Override
            public void onLoadMore() {

            }
        });
        // 添加地址点击事件
        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转到添加地址界面
                startActivityForResult(EditHarvestAddressActivity.newIntent(mContext, ""), ZLConstants.Integers.REQUEST_CODE_ADDRESS);
            }
        });
        // 没有收获地址点击事件
        mNoDataView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转到添加地址界面
                startActivityForResult(EditHarvestAddressActivity.newIntent(mContext, ""), ZLConstants.Integers.REQUEST_CODE_ADDRESS);
            }
        });
        // 地址列表item点击事件
        mHarvestAddressAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener<AddressBean.DataBean>() {
            @Override
            public void onClick(int position, AddressBean.DataBean item) {
                Intent intent = new Intent();
                intent.putExtra(ZLConstants.Strings.ADDRESSBEAN_DATABEAN, item);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    @Override
    protected void processLogic() {
        setTitleLeftBack();
        setTitleName("收货地址");

        if (ZhaoLinApplication.getInstance().getLoginUserDoLogin(this) != null) {
            getMyAddress();
        } else {
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            switch (requestCode) {
                case ZLConstants.Integers.REQUEST_CODE_ADDRESS:// 更新列表
                    getMyAddress();
                    break;
            }
        }
    }

    /**
     * 获取我的地址
     */
    private void getMyAddress() {
        HashMap<String, String> map = new HashMap<>();
        map.put(ZLConstants.Params.UID, ZhaoLinApplication.getInstance().getLoginUser().getData().getId());
        RequestManager.createRequest(URLConstants.REQUEST_MY_ADDRESS, map, new OnMyActivityRequestListener<HarvestAddressListBean>(this) {
            @Override
            public void onSuccess(HarvestAddressListBean bean) {
                mRecyclerView.handlerSuccessHasRefreshAndLoad(mHarvestAddressAdapter, true, bean.getData());
            }

            @Override
            public void onFailed(boolean isError, String code, String message) {
                mRecyclerView.handlerError(mHarvestAddressAdapter, true);
            }
        });
    }

    public static AddressBean.DataBean getResult(Intent data) {
        return (AddressBean.DataBean) data.getSerializableExtra(ZLConstants.Strings.ADDRESSBEAN_DATABEAN);
    }
}
