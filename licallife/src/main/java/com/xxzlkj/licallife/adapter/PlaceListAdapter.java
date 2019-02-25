package com.xxzlkj.licallife.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xxzlkj.licallife.R;
import com.xxzlkj.licallife.model.PlaceListBean;
import com.xxzlkj.zhaolinshare.base.base.BaseAdapter;
import com.xxzlkj.zhaolinshare.base.base.BaseViewHolder;
import com.xxzlkj.zhaolinshare.base.util.PixelUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 描述: 籍贯
 *
 * @author zhangrq
 *         2017/7/25 15:06
 */
public class PlaceListAdapter extends BaseAdapter<PlaceListBean.DataBean.Place2Bean> {
    private String selectPlaceId;
    private String selectPlace2Id;
    private final String NO_ID = "asdfdffjkldakdl";
    private Map<String, String> requestParams = new HashMap<>();

    public PlaceListAdapter(Context context, int itemId) {
        super(context, itemId);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void convert(BaseViewHolder holder, int position, PlaceListBean.DataBean.Place2Bean itemBean) {
        TextView tv_title = holder.getView(R.id.tv_title);// 标题
        RecyclerView recyclerView = holder.getView(R.id.recyclerView);// 内容
        // 设置值
        tv_title.setText(itemBean.getTitle());
        // 设置内容
        recyclerView.setLayoutManager(new GridLayoutManager(mContext, 4));
        recyclerView.setNestedScrollingEnabled(false);
        PlaceItemListAdapter itemListAdapter = new PlaceItemListAdapter(mContext, R.layout.item_nanny_and_maternity_matron_drawer_item);
        recyclerView.setAdapter(itemListAdapter);
        itemListAdapter.clearAndAddList(itemBean.getPlace());
        // 设置点击
        itemListAdapter.setOnItemClickListener((itemPosition, item) -> {
            // 设置选中
            if (position == 0) {
                // 第一个（全国）
                selectPlaceId = item.getId();
                selectPlace2Id = null;
            } else {
                selectPlaceId = null;
                selectPlace2Id = item.getId();
            }
            // 通知全部刷新
            notifyDataSetChanged();
        });
        // 设置内部Item默认选中
        if (selectPlaceId != null && selectPlace2Id == null) {
            // 之前选了，全国模块（全国模块选中，地区模块传NO_ID，即不选中）
            itemListAdapter.setSelectPlaceId(position == 0 ? selectPlaceId : NO_ID);
        } else if (selectPlaceId == null && selectPlace2Id != null) {
            // 之前选了，地区模块（地区模块选中，全国模块传NO_ID，即不选中）
            itemListAdapter.setSelectPlaceId(position == 0 ? NO_ID : selectPlace2Id);
        } else if (selectPlaceId == null && selectPlace2Id == null) {
            // 之前什么都没选，默认选中：全国第一个（不限）
            itemListAdapter.setSelectPlaceId(position == 0 && itemBean.getPlace().size() > 0 ? itemBean.getPlace().get(0).getId() : NO_ID);
        } else {
            // 之前都选了（数据错误）
            itemListAdapter.setSelectPlaceId(position == 0 && itemBean.getPlace().size() > 0 ? itemBean.getPlace().get(0).getId() : NO_ID);
            selectPlaceId = null;
            selectPlace2Id = null;
        }
        itemListAdapter.notifyDataSetChanged();
        // 设置最后一个距下有距离
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) recyclerView.getLayoutParams();
        layoutParams.bottomMargin = position == getItemCount() - 1 ? PixelUtil.dip2px(mContext, 11) : 0;
        recyclerView.setLayoutParams(layoutParams);
    }

    /**
     * 点击了确定后，增加到里面
     */
    public void addSelectToRequestParams() {
        requestParams.put("place", selectPlaceId);
        requestParams.put("place2", selectPlace2Id);
    }

    public Map<String, String> getRequestParams() {
        return requestParams;
    }

    /**
     * 获取请求参数非空数量
     */
    public int getRequestParamsCount() {
        int count = 0;
        for (Map.Entry<String, String> stringStringEntry : requestParams.entrySet()) {
            if (!TextUtils.isEmpty(stringStringEntry.getValue())) {
                count++;
            }
        }
        return count;
    }

    /**
     * 设置选中
     */
    public void setSelectedByRequestParams(Map<String, String> requestParams) {
        if (requestParams != null && requestParams.containsKey("place")) {
            this.selectPlaceId = requestParams.get("place");
        }
        if (requestParams != null && requestParams.containsKey("place2")) {
            this.selectPlace2Id = requestParams.get("place2");
        }
    }
}
