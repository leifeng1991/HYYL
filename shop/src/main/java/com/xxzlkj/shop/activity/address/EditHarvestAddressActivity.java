package com.xxzlkj.shop.activity.address;

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

import com.xxzlkj.shop.R;
import com.xxzlkj.shop.config.StringConstants;
import com.xxzlkj.shop.config.URLConstants;
import com.xxzlkj.shop.event.AddressChangEvent;
import com.xxzlkj.shop.event.EditHarvestAddressEvent;
import com.xxzlkj.shop.event.Home4AddressEvent;
import com.xxzlkj.shop.event.MakeSureAddressEvent;
import com.xxzlkj.shop.event.ShopCartAddressEvent;
import com.xxzlkj.shop.event.ShopHomeAddressEvent;
import com.xxzlkj.shop.model.AddressBean;
import com.xxzlkj.shop.utils.MyDrawableUtils;
import com.xxzlkj.shop.utils.ZLPreferencesUtils;
import com.xxzlkj.zhaolinshare.base.app.BaseApplication;
import com.xxzlkj.zhaolinshare.base.base.MyBaseActivity;
import com.xxzlkj.zhaolinshare.base.model.BaseBean;
import com.xxzlkj.zhaolinshare.base.model.User;
import com.xxzlkj.zhaolinshare.base.net.OnMyActivityRequestListener;
import com.xxzlkj.zhaolinshare.base.net.RequestManager;
import com.xxzlkj.zhaolinshare.base.util.NumberFormatUtils;
import com.xxzlkj.zhaolinshare.base.util.PreferencesSaver;
import com.xxzlkj.zhaolinshare.base.util.ToastManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;

/**
 * 编辑收货地址
 */
@SuppressWarnings("ALL")
public class EditHarvestAddressActivity extends MyBaseActivity implements TextWatcher {
    private Button mSaveButton;
    private EditText mNameEditText;
    private EditText mPhoneEditText;
    private EditText mDetailAddressEditText;
    private EditText mIdCardEditText;
    private TextView mLocationTextView;
    private CheckBox mCheckBox;
    private RelativeLayout mLocationLayout;
    private User mLogUser;
    private double mLatitude;
    private double mLongitude;
    //1:添加 2：编辑
    private int type;
    private String id = "";
    //信息是否改变
    private boolean isChange = false;
    //用户身份判断
    private boolean idCardIsEmpty = false;
    // 跳转类型
    private int jumpType;

    /**
     * @param context 上下文
     * @param type    1：添加新地址 2：修改地址 （必传）
     * @param id      添加新地址可以传null 修改地址必须传id（选传）
     * @param jumpTyp 跳转类型 1：地址列表 2：app首页 3：商城首页 4：购物车 5:确认订单（必传）
     * @return
     */
    public static Intent newIntent(Context context, int type, String id, int jumpTyp) {
        Intent intent = new Intent(context, EditHarvestAddressActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(StringConstants.INTENT_PARAM_TYPE, type);
        bundle.putInt(StringConstants.INTENT_PARAM_JUMP_TYPE, jumpTyp);
        bundle.putString(StringConstants.INTENT_PARAM_ID, id);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_edit_harvest_address);
    }

    @Override
    protected void findViewById() {
        EventBus.getDefault().register(this);
        mNameEditText = getView(R.id.id_eha_name);// 收货人名字
        mPhoneEditText = getView(R.id.id_eha_phone);// 手机号
        mLocationLayout = getView(R.id.id_eha_location_layout);// 坐标布局
        mLocationTextView = getView(R.id.id_eha_location);// 坐标
        mDetailAddressEditText = getView(R.id.id_eha_detail_address);// 详细地址
        mIdCardEditText = getView(R.id.id_eha_idcard);// 身份
        mCheckBox = getView(R.id.id_eha_cb);// 默认地址
        mSaveButton = getView(R.id.id_eha_save);// 保存
        MyDrawableUtils.setButtonShapeOrange(this, mSaveButton);
        // 上个页面传来的值
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            type = bundle.getInt(StringConstants.INTENT_PARAM_TYPE);
            id = bundle.getString(StringConstants.INTENT_PARAM_ID);
            jumpType = bundle.getInt(StringConstants.INTENT_PARAM_JUMP_TYPE);
        }

        tvTitleRight = getView(R.id.tv_title_right);
        // 1:添加 2：编辑
        if (type == 2) {
            setTitleRightText("删除");
        }
        // 登录用户
        mLogUser = BaseApplication.getInstance().getLoginUserDoLogin(this);
        if (mLogUser == null) {
            finish();
        }


    }

    @Override
    protected void setListener() {
        mSaveButton.setOnClickListener(this);
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
        if (!TextUtils.isEmpty(id)) {
            getAddressDetail();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.iv_title_left) {
            if (isChange) {
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
                finish();
            }
        } else if (id == R.id.tv_title_right) {
            // 删除
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
        } else if (id == R.id.id_eha_save) {
            // 保存
            checkData();
        } else if (id == R.id.id_eha_location_layout) {
            //获取地理位置
            Intent intent = new Intent(this, LocationActivity.class);
            startActivityForResult(intent, StringConstants.REQUSTCODE_LOCATION);
        }

    }

    /**
     * 获取地址详细信息
     */
    private void getAddressDetail() {
        HashMap<String, String> map = new HashMap<>();
        map.put(URLConstants.REQUEST_PARAM_ID, id);
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
            map.put(URLConstants.REQUEST_PARAM_UID, mLogUser.getData().getId());
            map.put(URLConstants.REQUEST_PARAM_NAME, mNameEditText.getText().toString());
            map.put(URLConstants.REQUEST_PARAM_PHONE, mPhoneEditText.getText().toString());
            if (mLatitude == 0 || mLongitude == 0) {//经纬度
                return;
            }
            map.put(URLConstants.REQUEST_PARAM_LONGITUDE, String.valueOf(mLongitude));
            map.put(URLConstants.REQUEST_PARAM_LATITUDE, String.valueOf(mLatitude));
            map.put(URLConstants.REQUEST_PARAM_POSITION, mLocationTextView.getText().toString());
            map.put(URLConstants.REQUEST_PARAM_ADDRESS, mDetailAddressEditText.getText().toString());
            if (!TextUtils.isEmpty(mIdCardEditText.getText().toString())) {
                map.put(URLConstants.REQUEST_PARAM_IDENTITY, mIdCardEditText.getText().toString());
            }
            if (mCheckBox.isChecked()) {
                map.put(URLConstants.REQUEST_PARAM_STATE, "2");
            }

            String url;
            if (type == 1) {
                url = URLConstants.REQUEST_ADDRESS;
            } else {
                map.put(URLConstants.REQUEST_PARAM_ID, id);
                url = URLConstants.REQUEST_EDIT_ADDRESS;
            }

            RequestManager.createRequest(url, map, new OnMyActivityRequestListener<AddressBean>(this) {
                @Override
                public void onSuccess(AddressBean bean) {
                    if (bean.getData() != null) {
                        Intent intent = getIntent();
                        intent.putExtra(StringConstants.INTENT_PARAM_LOC, bean.getData());
                        setResult(RESULT_OK, intent);
                        switch (jumpType) {
                            case 1:// 地址列表
                                EventBus.getDefault().postSticky(new AddressChangEvent(true, bean.getData().getId(), bean.getData()));
                                break;
                            case 4:// 购物车
                                EventBus.getDefault().postSticky(new AddressChangEvent(true, bean.getData().getId(), bean.getData()));
                                if (type == 1) {
                                    // 添加新地址
                                    EventBus.getDefault().postSticky(new ShopCartAddressEvent(1, id));
                                } else {
                                    // 修改地址
                                    EventBus.getDefault().postSticky(new ShopCartAddressEvent(2, id));
                                }
                                break;
                            case 5:// 确认订单
                                EventBus.getDefault().postSticky(new AddressChangEvent(true, bean.getData().getId(), bean.getData()));
                                if (type == 1) {
                                    // 添加新地址
                                    EventBus.getDefault().postSticky(new MakeSureAddressEvent(1, bean.getData().getId(), bean.getData()));
                                } else {
                                    // 修改地址
                                    EventBus.getDefault().postSticky(new MakeSureAddressEvent(2, bean.getData().getId(), bean.getData()));
                                }
                                break;
                            case 2:// app首页
                                EventBus.getDefault().postSticky(new Home4AddressEvent(true, bean.getData()));
                                break;
                            case 3:// 商城首页
                                EventBus.getDefault().postSticky(new ShopHomeAddressEvent(true, bean.getData()));
                                EventBus.getDefault().postSticky(new Home4AddressEvent(true, bean.getData()));
                                break;

                        }

                        if ((jumpType != 2 || jumpType != 3) && type == 2) {
                            String stringAttr = PreferencesSaver.getStringAttr(mContext, StringConstants.PREFERENCES_MY_ADDRESS_ID);
                            if (stringAttr.equals(id)) {
                                // 修改时判断是否是自己本地保存的地址id
                                EventBus.getDefault().postSticky(new ShopHomeAddressEvent(true, bean.getData()));
                                EventBus.getDefault().postSticky(new Home4AddressEvent(true, bean.getData()));
                            }
                        }

                    }
                    finish();
                }
            });
        }
    }

    /**
     * 删除收货地址
     */
    private void deleteAddress() {
        HashMap<String, String> map = new HashMap<>();
        map.put(URLConstants.REQUEST_PARAM_ID, id);
        RequestManager.createRequest(URLConstants.REQUEST_DEL_ADDRESS, map, new OnMyActivityRequestListener<BaseBean>(this) {
            @Override
            public void onSuccess(BaseBean bean) {

                String stringAttr = PreferencesSaver.getStringAttr(mContext, StringConstants.PREFERENCES_MY_ADDRESS_ID);
                if (stringAttr.equals(id)) {// 删除时判断是否是自己本地保存的地址id
                    ZLPreferencesUtils.removeAddressInfo(mContext);
                    switch (jumpType) {
                        case 2:// app首页
                        case 3:// 商城首页
                            if (jumpType == 3 || jumpType == 4) {
                                // 商城首页
                                EventBus.getDefault().postSticky(new ShopHomeAddressEvent(false, null));
                            }
                            // app首页
                            EventBus.getDefault().postSticky(new Home4AddressEvent(false, null));
                            break;
                    }

                } else {
                    switch (jumpType) {
                        case 2:// app首页
                        case 3:// 商城首页
                            if (jumpType == 3) {
                                // 商城首页
                                EventBus.getDefault().postSticky(new ShopHomeAddressEvent(true, null));
                            }
                            // app首页
                            EventBus.getDefault().postSticky(new Home4AddressEvent(true, null));
                            break;
                    }

                }

                EventBus.getDefault().postSticky(new AddressChangEvent(true, id, null));

                switch (jumpType) {
                    case 4:// 购物车
                    case 5:// 确认订单
                        EventBus.getDefault().postSticky(new ShopCartAddressEvent(3, id));
                        if (jumpType == 5) {
                            EventBus.getDefault().postSticky(new MakeSureAddressEvent(3, id, null));
                        }
                        break;
                }
                finish();
            }
        });
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
        if (resultCode == RESULT_OK) {
            if (data != null) {// 得到经纬度
                mLocationTextView.setText(data.getStringExtra(StringConstants.INTENT_PARAM_LOCATION));
                mLatitude = data.getDoubleExtra(StringConstants.INTENT_PARAM_LATITUDE, 0);
                mLongitude = data.getDoubleExtra(StringConstants.INTENT_PARAM_LONGITUDE, 0);
                isChange = true;
            }
        }
    }

    /**
     * 坐标位置变化
     */
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = false)
    public void updateLocation(EditHarvestAddressEvent event) {
        mLocationTextView.setText(event.getAddress());
        mLatitude = event.getLatitude();
        mLongitude = event.getLongitude();
        isChange = true;
    }
}
