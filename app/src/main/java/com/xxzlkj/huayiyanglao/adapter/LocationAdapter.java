package com.xxzlkj.huayiyanglao.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.services.core.PoiItem;
import com.xxzlkj.huayiyanglao.R;
import com.xxzlkj.zhaolinshare.base.base.BaseAdapter;
import com.xxzlkj.zhaolinshare.base.base.BaseViewHolder;

/**
 * by Administrator on 2017/9/12.
 */

public class LocationAdapter extends BaseAdapter<PoiItem> {
    private int lastSelection = 0;

    public LocationAdapter(Context context, int itemId) {
        super(context, itemId);
    }

    @Override
    public void convert(BaseViewHolder holder, int position, PoiItem itemBean) {
        TextView mLocationTitle = holder.getTextView(R.id.id_li_location);
        TextView mLocationDetail = holder.getTextView(R.id.id_li_detail_location);
        ImageView mCheckedImageView = holder.getImageView(R.id.id_li_image);
        if (lastSelection == position) {
            mCheckedImageView.setImageResource(R.mipmap.location_rb);
        } else {
            mCheckedImageView.setImageResource(R.drawable.shape_white_bg);
        }
        if (TextUtils.isEmpty(itemBean.getTitle())) {
            mLocationTitle.setVisibility(View.GONE);
        } else {
            mLocationTitle.setVisibility(View.VISIBLE);
            mLocationTitle.setText(itemBean.getTitle());
        }

        if (TextUtils.isEmpty(itemBean.getSnippet())) {
            mLocationDetail.setVisibility(View.GONE);
        } else {
            mLocationDetail.setVisibility(View.VISIBLE);
            mLocationDetail.setText(itemBean.getCityName() + itemBean.getAdName() + itemBean.getSnippet());
        }
    }

    public void setLastSelection(int postion) {
        this.lastSelection = postion;
    }

    public int getSelection() {
        return lastSelection;
    }

    public PoiItem getPoiItem() {
        return getList().get(lastSelection);
    }
}
