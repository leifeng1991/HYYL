package com.xxzlkj.shop.adapter;

import android.app.Activity;
import android.content.Context;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.xxzlkj.shop.R;
import com.xxzlkj.shop.model.AddressList;
import com.xxzlkj.zhaolinshare.base.base.BaseAdapter;
import com.xxzlkj.zhaolinshare.base.base.BaseViewHolder;
import com.xxzlkj.zhaolinshare.base.util.CallPhoneUtils;
import com.xxzlkj.zhaolinshare.base.util.NumberFormatUtils;
import com.xxzlkj.zhaolinshare.base.util.StringUtil;


/**
 * 自提地址adapter
 * Created by Administrator on 2017/4/8.
 */

public class StoreAdressAdapter extends BaseAdapter<AddressList.DataBean> {
    public static int selectedIndex = 0;
    public StoreAdressAdapter(Context context, int itemId) {
        super(context, itemId);
    }

    @Override
    public void convert(BaseViewHolder holder, int position, final AddressList.DataBean itemBean) {
        final CheckBox mCheckBox = holder.getView(R.id.id_sli_checkbox);
        mCheckBox.setClickable(false);
        TextView mNameTextView = holder.getView(R.id.id_sli_store_name);
        TextView mJuLiTextView = holder.getView(R.id.id_sli_juli);
        TextView mContentTextView = holder.getView(R.id.id_sli_content);
        TextView mTimeTextView = holder.getView(R.id.id_sli_time);
        ImageView mPhoneImageView = holder.getView(R.id.id_sli_phone);

        if (position == selectedIndex){
            mCheckBox.setChecked(true);
        }else {
            mCheckBox.setChecked(false);
        }

        mNameTextView.setText(itemBean.getTitle());
        int juli = NumberFormatUtils.toInt(itemBean.getJuli());
        if (juli < 1000){
            mJuLiTextView.setText(juli + "m");
        }else {
            float newJuli = 1.0f * juli / 1000;
            mJuLiTextView.setText(StringUtil.saveTwoDecimal(newJuli) + "km");
        }

        mContentTextView.setText(itemBean.getAddress());

        mTimeTextView.setText("营业时间：暂无营业时间");

        mPhoneImageView.setOnClickListener(v -> CallPhoneUtils.callPhoneDialog((Activity) mContext,itemBean.getPhone()));
    }
}
