package com.xxzlkj.huayiyanglao.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.xxzlkj.huayiyanglao.R;
import com.xxzlkj.huayiyanglao.model.FoodsShopListBean;
import com.xxzlkj.zhaolinshare.base.base.BaseAdapter;
import com.xxzlkj.zhaolinshare.base.base.BaseViewHolder;
import com.xxzlkj.zhaolinshare.base.util.NumberFormatUtils;
import com.xxzlkj.zhaolinshare.base.util.StringUtil;

/**
 * 描述:附近食堂适配器
 *
 * @author leifeng
 *         2017/11/23 16:26
 */


public class NearDiningRoomListAdapter extends BaseAdapter<FoodsShopListBean.DataBean> {
    public int selelctPosition = 0;

    public NearDiningRoomListAdapter(Context context, int itemId) {
        super(context, itemId);
    }

    @Override
    public void convert(BaseViewHolder holder, int position, FoodsShopListBean.DataBean itemBean) {
        TextView mDiningRoomNameTextView = holder.getView(R.id.id_dining_room_name);// 食堂名字
        TextView mDistanceTextView = holder.getView(R.id.id_distance);// 距离
        ImageView mCheckedImageView = holder.getView(R.id.id_checked_image);// 选中

        mCheckedImageView.setImageResource(selelctPosition == position ? R.mipmap.ic_blue_checked : R.mipmap.ic_blue_normal_checked);

        mDiningRoomNameTextView.setText(itemBean.getTitle());
        mDistanceTextView.setText(String.format("%skm", StringUtil.saveTwoDecimal(1.0 * NumberFormatUtils.toInt(itemBean.getJuli()) / 1000)));
    }
}
