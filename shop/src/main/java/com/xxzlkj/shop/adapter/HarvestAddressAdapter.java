package com.xxzlkj.shop.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xxzlkj.shop.R;
import com.xxzlkj.shop.activity.address.EditHarvestAddressActivity;
import com.xxzlkj.shop.model.AddressBean;
import com.xxzlkj.zhaolinshare.base.base.BaseAdapter;
import com.xxzlkj.zhaolinshare.base.base.BaseViewHolder;


/**
 * 收货地址列表适配器
 * Created by Administrator on 2017/3/28.
 */

public class HarvestAddressAdapter extends BaseAdapter<AddressBean.DataBean> {
    private Activity mActivity;
    private int jumpType;
    private String addressId;

    /**
     *
     * @param jumpType 1:商城确认订单 2:家政单次预约 3：保姆/月嫂面试地址选择 4:我的 5:购物车（必传）
     */
    public HarvestAddressAdapter(Context context, Activity mActivity, int jumpType, String addressId, int itemId) {
        super(context, itemId);
        this.mActivity = mActivity;
        this.jumpType = jumpType;
        this.addressId = addressId;
    }

    @Override
    public void convert(BaseViewHolder holder, int position, final AddressBean.DataBean itemBean) {
        View mLeftLine = holder.getView(R.id.id_shop_default_line);
        View mBottomLine = holder.getView(R.id.id_shop_bottom_line);
        TextView mNameTextView = holder.getView(R.id.id_shop_name);
        TextView mPhoneTextView = holder.getView(R.id.id_shop_phone);
        TextView mDefaultTextView = holder.getView(R.id.id_shop_default_tip);
        TextView mAddressTextView = holder.getView(R.id.id_shop_address);
        ImageView mEditImageView = holder.getView(R.id.id_shop_edit);
        // 默认的显示和隐藏
        if ("2".equals(itemBean.getState())) {
            // 显示
            mDefaultTextView.setVisibility(View.VISIBLE);
        } else {
            // 隐藏
            mDefaultTextView.setVisibility(View.GONE);
        }

        if (TextUtils.isEmpty(addressId)){
            // 地址id为空
            mLeftLine.setVisibility(View.GONE);
        }else {
            if (addressId.equals(itemBean.getId())) {
                mLeftLine.setVisibility(View.VISIBLE);
            } else {
                mLeftLine.setVisibility(View.GONE);
            }
        }

        mNameTextView.setText(itemBean.getName());
        mPhoneTextView.setText(itemBean.getPhone());
        mAddressTextView.setText(itemBean.getAddress());
        mNameTextView.setText(itemBean.getName());

        mEditImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (jumpType) {
                    case 1:// 商城确认订单
                        mActivity.startActivity(EditHarvestAddressActivity.newIntent(mContext, 2, itemBean.getId(), 5));
                        break;
                    case 5:// 购物车
                        mActivity.startActivity(EditHarvestAddressActivity.newIntent(mContext, 2, itemBean.getId(), 4));
                        break;
                    default:
                        mActivity.startActivity(EditHarvestAddressActivity.newIntent(mContext, 2, itemBean.getId(), 1));
                        break;
                }
            }
        });
    }
}
