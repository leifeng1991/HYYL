package com.xxzlkj.huayiyanglao.activity.address;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.services.core.PoiItem;
import com.xxzlkj.huayiyanglao.R;
import com.xxzlkj.huayiyanglao.app.ZhaoLinApplication;
import com.xxzlkj.huayiyanglao.config.ZLConstants;
import com.xxzlkj.huayiyanglao.event.DeleteAddressEvent;
import com.xxzlkj.huayiyanglao.model.AddressBean;
import com.xxzlkj.shop.config.URLConstants;
import com.xxzlkj.zhaolinshare.base.base.MyBaseActivity;
import com.xxzlkj.zhaolinshare.base.model.BaseBean;
import com.xxzlkj.zhaolinshare.base.model.User;
import com.xxzlkj.zhaolinshare.base.net.OnMyActivityRequestListener;
import com.xxzlkj.zhaolinshare.base.net.RequestManager;
import com.xxzlkj.zhaolinshare.base.util.NumberFormatUtils;
import com.xxzlkj.zhaolinshare.base.util.ToastManager;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;

/**
 * 编辑收货地址
 */
public class EditHarvestAddressActivity extends MyBaseActivity implements TextWatcher {
    public static final int REQUSTCODE_LOCATION = 300;
    private Button mSaveButton;
    private EditText mNameEditText, mPhoneEditText, mDetailAddressEditText, mIdCardEditText;
    private TextView mLocationTextView;
    private CheckBox mCheckBox;
    private RelativeLayout mLocationLayout;
    private User mLogUser;
    private double mLatitude, mLongitude;
    private String mAddressId = "";
    //信息是否改变
    private boolean isChange = false;
    //用户身份判断
    private boolean idCardIsEmpty = false;


    /**
     * @param addressId 地址id 当是编辑地址时必传 添加地址传""
     * @return
     */
    public static Intent newIntent(Context context, String addressId) {
        Intent intent = new Intent(context, EditHarvestAddressActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(ZLConstants.Strings.ADDRESS_ID, addressId);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_editharvest_address);
    }

    @Override
    protected void findViewById() {
        mNameEditText = getView(R.id.id_eha_name);// 收货人名字
        mPhoneEditText = getView(R.id.id_eha_phone);// 手机号
        mLocationLayout = getView(R.id.id_eha_location_layout);// 坐标布局
        mLocationTextView = getView(R.id.id_eha_location);// 坐标
        mDetailAddressEditText = getView(R.id.id_eha_detail_address);// 详细地址
        mIdCardEditText = getView(R.id.id_eha_idcard);// 身份
        mCheckBox = getView(R.id.id_eha_cb);// 默认地址
        mSaveButton = getView(R.id.id_eha_save);// 保存
        tvTitleRight = getView(R.id.tv_title_right);

    }

    @Override
    protected void setListener() {
        mSaveButton.setOnClickListener(this);
        tvTitleRight.setOnClickListener(this);
        mLocationLayout.setOnClickListener(this);
        mNameEditText.addTextChangedListener(this);
        mPhoneEditText.addTextChangedListener(this);
        mDetailAddressEditText.addTextChangedListener(this);
        mIdCardEditText.addTextChangedListener(this);
        mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isChange = true;
            }
        });
    }

    @Override
    protected void processLogic() {
        setTitleLeftBack();
        setTitleName("编辑地址");
        // 登录用户
        mLogUser = ZhaoLinApplication.getInstance().getLoginUserDoLogin(this);
        if (mLogUser == null) {
            finish();
        }
        // 上个页面传来的值
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mAddressId = bundle.getString(ZLConstants.Strings.ADDRESS_ID);
            if (!TextUtils.isEmpty(mAddressId)) {
                setTitleRightText("删除");
                getAddressDetail();
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (isChange && !TextUtils.isEmpty(mAddressId)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("地址已修改，是否保存？");
            builder.setNegativeButton("不保存", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    finish();
                }
            });
            builder.setPositiveButton("保存", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    checkData();
                }
            });
            builder.show();
        } else {
            super.onBackPressed();
        }

    }

    @Override
    public void onTitleRightClick(View view) {
        super.onTitleRightClick(view);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("确定要删除收货地址？");
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                deleteAddress();
                tvTitleRight.setText("编辑");
            }
        });
        builder.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_eha_save:// 保存
                checkData();
                break;
            case R.id.id_eha_location_layout://获取地理位置
                Intent intent = new Intent(this, LocationActivity.class);
                startActivityForResult(intent, REQUSTCODE_LOCATION);
                break;
        }
    }

    /**
     * 获取地址详细信息
     */
    private void getAddressDetail() {
        HashMap<String, String> map = new HashMap<>();
        map.put(ZLConstants.Params.ID, mAddressId);
        RequestManager.createRequest(URLConstants.REQUEST_ADDRESS_INFO, map, new OnMyActivityRequestListener<AddressBean>(this) {
            @Override
            public void onSuccess(AddressBean bean) {
                AddressBean.DataBean data = bean.getData();
                mNameEditText.setText(data.getName());
                // 将光标移至最后
                int length = data.getName().length();
                if (length > 10) {
                    length = 10;
                }
                mNameEditText.setSelection(length);
                // 手机号
                mPhoneEditText.setText(data.getPhone());
                // 位置
                mLocationTextView.setText(data.getPosition());
                // 地址
                mDetailAddressEditText.setText(data.getAddress());
                // 经纬度
                mLatitude = NumberFormatUtils.toDouble(data.getLatitude());
                mLongitude = NumberFormatUtils.toDouble(data.getLongitude());
                if ("2".equals(data.getState())) {// 是否是默认地址
                    mCheckBox.setChecked(true);
                }

                if (!TextUtils.isEmpty(data.getIdentity())) {
                    idCardIsEmpty = true;
                    mIdCardEditText.setText(data.getIdentity());
                }

                isChange = false;
            }
        });
    }

    /**
     * 保存前检查
     */
    private void checkData() {
        if (TextUtils.isEmpty(mNameEditText.getText().toString())) {
            ToastManager.showShortToast(mContext, "收货人不能为空");
            return;
        }
        if (TextUtils.isEmpty(mPhoneEditText.getText().toString())) {
            ToastManager.showShortToast(mContext, "手机号不能为空");
            return;
        }
        if (TextUtils.isEmpty(mLocationTextView.getText().toString())) {
            ToastManager.showShortToast(mContext, "坐标不能为空");
            return;
        }
        if (TextUtils.isEmpty(mDetailAddressEditText.getText().toString())) {
            ToastManager.showShortToast(mContext, "详细地址不能为空");
            return;
        }

        addAddress();
    }

    /**
     * 添加/编辑收货地址
     */
    private void addAddress() {
        if (mLogUser != null) {
            HashMap<String, String> map = new HashMap<>();
            map.put(ZLConstants.Params.UID, mLogUser.getData().getId());
            map.put(ZLConstants.Params.NAME, mNameEditText.getText().toString());
            map.put(ZLConstants.Params.PHONE, mPhoneEditText.getText().toString());
            if (mLatitude == 0 || mLongitude == 0) {//经纬度
                return;
            }
            map.put(ZLConstants.Params.LONGITUDE, String.valueOf(mLongitude));
            map.put(ZLConstants.Params.LATITUDE, String.valueOf(mLatitude));
            map.put(ZLConstants.Params.POSITION, mLocationTextView.getText().toString());
            map.put(ZLConstants.Params.ADDRESS, mDetailAddressEditText.getText().toString());
            if (!TextUtils.isEmpty(mIdCardEditText.getText().toString())) {
                map.put(ZLConstants.Params.IDENTITY, mIdCardEditText.getText().toString());
            }
            if (mCheckBox.isChecked()) {
                map.put(ZLConstants.Params.STATE, "2");
            }
            String url;
            if (TextUtils.isEmpty(mAddressId)) {
                url = URLConstants.REQUEST_ADDRESS;
            } else {
                map.put(ZLConstants.Params.ID, mAddressId);
                url = URLConstants.REQUEST_EDIT_ADDRESS;
            }

            RequestManager.createRequest(url, map, new OnMyActivityRequestListener<AddressBean>(this) {
                @Override
                public void onSuccess(AddressBean bean) {
                    setResultIntent(bean.getData());
                }
            });
        }
    }

    /**
     * 删除收货地址
     */
    private void deleteAddress() {
        HashMap<String, String> map = new HashMap<>();
        map.put(ZLConstants.Params.ID, mAddressId);
        RequestManager.createRequest(URLConstants.REQUEST_DEL_ADDRESS, map, new OnMyActivityRequestListener<BaseBean>(this) {
            @Override
            public void onSuccess(BaseBean bean) {
                setResultIntent(null);
                EventBus.getDefault().postSticky(new DeleteAddressEvent(mAddressId));
            }
        });
    }

    private void setResultIntent(AddressBean.DataBean bean) {
        Intent intent = new Intent();
        intent.putExtra(ZLConstants.Strings.ADDRESSBEAN_DATABEAN, bean);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        isChange = true;

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUSTCODE_LOCATION:
                PoiItem mPoiItem = LocationActivity.getResult(data);
                if (mPoiItem != null) {
                    mLocationTextView.setText(String.format("%s%s%s", mPoiItem.getCityName(), mPoiItem.getAdName(), mPoiItem.getSnippet()));
                    mLatitude = mPoiItem.getLatLonPoint().getLatitude();
                    mLongitude = mPoiItem.getLatLonPoint().getLongitude();
                    isChange = true;
                }
                break;
        }

    }

    public static AddressBean.DataBean getResult(Intent data) {
        return (AddressBean.DataBean) data.getSerializableExtra(ZLConstants.Strings.ADDRESSBEAN_DATABEAN);
    }
}
