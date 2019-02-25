package com.xxzlkj.licallife.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xxzlkj.licallife.R;
import com.xxzlkj.licallife.model.FilterBean;
import com.xxzlkj.zhaolinshare.base.base.BaseAdapter;
import com.xxzlkj.zhaolinshare.base.base.BaseViewHolder;
import com.xxzlkj.zhaolinshare.base.util.PixelUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述: 待遇
 *
 * @author zhangrq
 *         2017/7/25 15:06
 */
public class TreatmentListAdapter extends BaseAdapter<FilterBean.DataBean> {
    private Map<String, String> requestParams = new HashMap<>();
    private String salary, restBig, restSmall;

    public TreatmentListAdapter(Context context, int itemId) {
        super(context, itemId);
    }

    @Override
    public void convert(BaseViewHolder holder, int position, FilterBean.DataBean itemBean) {
        TextView tv_title = holder.getView(R.id.tv_title);// 标题
        RecyclerView recyclerView = holder.getView(R.id.recyclerView);// 内容

        String type = itemBean.getType();
        List<FilterBean.DataBean.DataItemBean> itemBeanList = null;
        if ("1".equals(type)) {
            // 薪水
            itemBeanList = itemBean.getSalary();
        } else if ("2".equals(type)) {
            // 月休
            itemBeanList = itemBean.getHugh();
        }
        // 设置值
        tv_title.setText(itemBean.getTitle());
        // 设置内容
        recyclerView.setLayoutManager(new GridLayoutManager(mContext, 4));
        recyclerView.setNestedScrollingEnabled(false);
        DrawerItemListAdapter itemListAdapter = new DrawerItemListAdapter(mContext, R.layout.item_nanny_and_maternity_matron_drawer_item);
        recyclerView.setAdapter(itemListAdapter);
        itemListAdapter.clearAndAddList(itemBeanList);
        // 设置点击
        itemListAdapter.setOnItemClickListener((itemPosition, item) -> {
            // 维护一个 map
            if ("1".equals(type)) {
                // 薪水
                salary = item.getId();
            } else if ("2".equals(type)) {
                // 月休
                restBig = item.getBig();
                restSmall = item.getSmall();
            }
            // 设置选中
            itemBean.setSelectPosition(itemPosition);
            itemListAdapter.setSelectPosition(itemPosition);
            itemListAdapter.notifyDataSetChanged();
        });
        // 设置默认选中
        itemListAdapter.setSelectPosition(getSelectPosition(itemBeanList, type));
        itemListAdapter.notifyDataSetChanged();
        // 设置最后一个距下有距离
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) recyclerView.getLayoutParams();
        layoutParams.bottomMargin = position == getItemCount() - 1 ? PixelUtil.dip2px(mContext, 11) : 0;
        recyclerView.setLayoutParams(layoutParams);
    }

    private int getSelectPosition(List<FilterBean.DataBean.DataItemBean> itemBeanList, String type) {
        if (itemBeanList == null) {
            return 0;
        }
        for (int i = 0; i < itemBeanList.size(); i++) {
            FilterBean.DataBean.DataItemBean dataItemBean = itemBeanList.get(i);
            if ("1".equals(type)) {
                // 薪水
                if (TextUtils.equals(salary, dataItemBean.getId())) {
                    return i;
                }
            } else if ("2".equals(type)) {
                // 月休
                if (TextUtils.equals(restBig, dataItemBean.getBig()) && TextUtils.equals(restSmall, dataItemBean.getSmall())) {
                    return i;
                }
            }
        }
        return 0;
    }

    public Map<String, String> getRequestParams() {
        return requestParams;
    }

    public int getRequestParamsCount() {
        int count = 0;
        for (Map.Entry<String, String> stringStringEntry : requestParams.entrySet()) {
            if (!TextUtils.isEmpty(stringStringEntry.getValue())) {
                count++;
            }
        }
        return count;
    }

    public void addSelectToRequestParams() {
        requestParams.put("salary", salary);
        requestParams.put("restBig", restBig);
        requestParams.put("restSmall", restSmall);
    }

    public void setSelectedByRequestParams(Map<String, String> requestParams) {
        if (requestParams != null && requestParams.containsKey("salary")) {
            salary = requestParams.get("salary");
        }
        if (requestParams != null && requestParams.containsKey("restBig")) {
            restBig = requestParams.get("restBig");
        }
        if (requestParams != null && requestParams.containsKey("restSmall")) {
            restSmall = requestParams.get("restSmall");
        }

    }
}
