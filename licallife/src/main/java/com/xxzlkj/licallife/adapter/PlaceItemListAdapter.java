package com.xxzlkj.licallife.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.widget.TextView;

import com.xxzlkj.licallife.R;
import com.xxzlkj.licallife.model.PlaceListBean;
import com.xxzlkj.zhaolinshare.base.base.BaseAdapter;
import com.xxzlkj.zhaolinshare.base.base.BaseViewHolder;


/**
 * 描述: 保姆或月嫂列表adapter
 *
 * @author zhangrq
 *         2017/7/25 15:06
 */
public class PlaceItemListAdapter extends BaseAdapter<PlaceListBean.DataBean.PlaceBean> {
    private String selectPlaceId;

    public PlaceItemListAdapter(Context context, int itemId) {
        super(context, itemId);
    }

    @Override
    public void convert(BaseViewHolder holder, int position, PlaceListBean.DataBean.PlaceBean itemBean) {
        TextView tv_title = holder.getView(R.id.tv_title);// 标题
        tv_title.setText(itemBean.getTitle());
        // 设置选中
        tv_title.setBackgroundResource(TextUtils.equals(selectPlaceId, itemBean.getId()) ? R.drawable.shape_buy_now : R.drawable.shape_rectangle_orange_ff725c);// 设置选中
        tv_title.setTextColor(TextUtils.equals(selectPlaceId, itemBean.getId()) ?
                ContextCompat.getColor(mContext, R.color.white) :
                ContextCompat.getColor(mContext, R.color.orange_ff725c));// 设置选中
    }

    public void setSelectPlaceId(String selectPlaceId) {
        this.selectPlaceId = selectPlaceId;
    }
}
