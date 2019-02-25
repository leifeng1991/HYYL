package com.xxzlkj.huayiyanglao.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xxzlkj.huayiyanglao.R;
import com.xxzlkj.huayiyanglao.activity.address.EditHarvestAddressActivity;
import com.xxzlkj.huayiyanglao.config.ZLConstants;
import com.xxzlkj.huayiyanglao.model.AddressBean;
import com.xxzlkj.huayiyanglao.weight.ShapeButton;
import com.xxzlkj.zhaolinshare.base.base.BaseAdapter;
import com.xxzlkj.zhaolinshare.base.base.BaseViewHolder;

/**
 * 收货地址列表适配器
 * Created by Administrator on 2017/3/28.
 */

public class HarvestAddressAdapter extends BaseAdapter<AddressBean.DataBean> {
    private Activity mActivity;
    private String addressId;

    /**
     * @param addressId 地址ID 控制默认选中的地址
     */
    public HarvestAddressAdapter(Context context, Activity mActivity, String addressId, int itemId) {
        super(context, itemId);
        this.mActivity = mActivity;
        this.addressId = addressId;
    }

    @Override
    public void convert(BaseViewHolder holder, int position, final AddressBean.DataBean itemBean) {
        View mLeftLine = holder.getView(R.id.id_hai_default_line);// 坐标指示器
        View mBottomLine = holder.getView(R.id.id_hai_bottom_line);// 分割线
        TextView mNameTextView = holder.getView(R.id.id_hai_name);// 收货人姓名
        TextView mPhoneTextView = holder.getView(R.id.id_hai_phone);// 收货人手机号
        ShapeButton mDefaultTextView = holder.getView(R.id.id_hai_default_tip);// 默认地址
        TextView mAddressTextView = holder.getView(R.id.id_hai_address);// 地址
        ImageView mEditImageView = holder.getView(R.id.id_hai_edit);// 编辑按钮
        // 默认的显示和隐藏
        mDefaultTextView.setVisibility("2".equals(itemBean.getState()) ? View.VISIBLE : View.GONE);

        mLeftLine.setVisibility(TextUtils.isEmpty(addressId) ? View.GONE : addressId.equals(itemBean.getId()) ? View.VISIBLE : View.GONE);

        mNameTextView.setText(itemBean.getName());
        mPhoneTextView.setText(itemBean.getPhone());
        mAddressTextView.setText(itemBean.getAddress());
        mNameTextView.setText(itemBean.getName());

        mEditImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转到编辑地址界面
                mActivity.startActivityForResult(EditHarvestAddressActivity.newIntent(mContext, itemBean.getId()), ZLConstants.Integers.REQUEST_CODE_ADDRESS);
            }
        });
    }
}
