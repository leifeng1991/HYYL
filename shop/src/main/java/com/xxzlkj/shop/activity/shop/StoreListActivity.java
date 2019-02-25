package com.xxzlkj.shop.activity.shop;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.xxzlkj.shop.R;
import com.xxzlkj.shop.adapter.StoreAdressAdapter;
import com.xxzlkj.shop.config.StringConstants;
import com.xxzlkj.shop.config.URLConstants;
import com.xxzlkj.shop.model.AddressList;
import com.xxzlkj.zhaolinshare.base.base.MyBaseActivity;
import com.xxzlkj.zhaolinshare.base.net.OnMyActivityRequestListener;
import com.xxzlkj.zhaolinshare.base.net.RequestManager;

import java.util.HashMap;
import java.util.List;


/**
 * 自提地址列表
 */
public class StoreListActivity extends MyBaseActivity {
    private RecyclerView mRecyclerView;
    private int position;
    private String mAddressId;

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_store_list);
    }

    @Override
    protected void findViewById() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            mAddressId = bundle.getString(StringConstants.INTENT_PARAM_ADDRESSID);
            position = bundle.getInt(StringConstants.INTENT_PARAM_POSITION);
        }
        mRecyclerView = getView(R.id.id_sl_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void setListener() {
    }

    @Override
    protected void processLogic() {
        setTitleLeftBack();
        setTitleName("自提地址");
        getStoreList(mAddressId);
    }

    /**
     * 获取线下门店地址列表
     * @param addressId
     */
    private void getStoreList(String addressId){
        HashMap<String, String> map = new HashMap<>();
        map.put(URLConstants.REQUEST_PARAM_ID,addressId);
        RequestManager.createRequest(URLConstants.REQUEST_STORE_LIST,
                map, new OnMyActivityRequestListener<AddressList>(this) {
                    @Override
                    public void onSuccess(AddressList bean) {
                        List<AddressList.DataBean> data = bean.getData();
                        final StoreAdressAdapter mStoreAdressAdapter = new StoreAdressAdapter(StoreListActivity.this,R.layout.adapter_store_list_item);
                        mStoreAdressAdapter.addList(data);
                        StoreAdressAdapter.selectedIndex = position;
                        mRecyclerView.setAdapter(mStoreAdressAdapter);
                        mStoreAdressAdapter.setOnItemClickListener((position1, item) -> {
                            StoreAdressAdapter.selectedIndex = position1;
                            Intent intent = getIntent();
                            intent.putExtra(StringConstants.INTENT_PARAM_POSITION, position1);
                            intent.putExtra(StringConstants.INTENT_PARAM_NAME,item.getTitle());
                            intent.putExtra(StringConstants.INTENT_PARAM_STOREID,item.getId());
                            setResult(RESULT_OK,intent);
                            finish();
                        });
                    }
                });

    }
}
