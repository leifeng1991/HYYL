package com.xxzlkj.licallife.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager;
import com.xxzlkj.licallife.R;
import com.xxzlkj.licallife.model.FilterBean;
import com.xxzlkj.zhaolinshare.base.base.BaseAdapter;
import com.xxzlkj.zhaolinshare.base.base.BaseViewHolder;
import com.xxzlkj.zhaolinshare.base.util.PixelUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述: 保姆或月嫂列表adapter
 *
 * @author zhangrq
 *         2017/7/25 15:06
 */
public class DrawerListAdapter extends BaseAdapter<FilterBean.DataBean> {
    private Map<String, String> tempRequestParams = new HashMap<>();
    private Map<String, String> requestParams = new HashMap<>();
    private Map<String, String> selectedRequestParams;
    private int measuredWidth;

    public DrawerListAdapter(Context context, int itemId) {
        super(context, itemId);
    }

    @Override
    public void convert(BaseViewHolder holder, int position, FilterBean.DataBean itemBean) {
        TextView tv_title = holder.getView(R.id.tv_title);// 标题
        RecyclerView recyclerView = holder.getView(R.id.recyclerView);// 内容
        String type = itemBean.getType();
        // 设置值
        tv_title.setText(itemBean.getTitle());
        // 设置内容
        ChipsLayoutManager spanLayoutManager = ChipsLayoutManager.newBuilder(mContext)
                .setOrientation(ChipsLayoutManager.HORIZONTAL)
                .setScrollingEnabled(false)
                .build();
        recyclerView.setLayoutManager("3".equals(type) ? spanLayoutManager : new GridLayoutManager(mContext, 3));
        DrawerItemListAdapter drawerItemListAdapter = new DrawerItemListAdapter(mContext, "3".equals(type) ? R.layout.item_nanny_and_maternity_matron_drawer_item_wrap : R.layout.item_nanny_and_maternity_matron_drawer_item);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(drawerItemListAdapter);
        // 根据类型获取对应的内部RecyclerView的集合
        List<FilterBean.DataBean.DataItemBean> itemBeanList = getItemBeanListByType(itemBean, type);
        drawerItemListAdapter.clearAndAddList(itemBeanList);

        // 设置ItemItem点击
        drawerItemListAdapter.setOnItemClickListener((itemPosition, item) -> {
            // 维护一个 map
            addParamsByType(type, item);
            // 内部RecyclerView设置选中
            drawerItemListAdapter.setSelectPosition(itemPosition);
            drawerItemListAdapter.notifyDataSetChanged();
        });

        // 设置当前item的内部RecyclerView，默认选中
        drawerItemListAdapter.setSelectPosition(getSelectPosition(itemBeanList, type));// 通过类型for循环找出是否有匹配的对象，有则返回，没有则返回0
        drawerItemListAdapter.notifyDataSetChanged();

        // 设置最后一个距下有距离
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) recyclerView.getLayoutParams();
        layoutParams.bottomMargin = position == getItemCount() - 1 ? PixelUtil.dip2px(mContext, 11) : 0;
        recyclerView.setLayoutParams(layoutParams);
        if (measuredWidth == 0) {
            ViewTreeObserver viewTreeObserver = recyclerView.getViewTreeObserver();
            viewTreeObserver.addOnPreDrawListener(() -> {
                if (measuredWidth == 0) {
                    //获取的宽
                    measuredWidth = recyclerView.getMeasuredWidth();
                    drawerItemListAdapter.setItemWidth(measuredWidth / 3);
                    drawerItemListAdapter.notifyDataSetChanged();
                }
                return true;
            });
        } else {
            drawerItemListAdapter.setItemWidth(measuredWidth / 3);
            drawerItemListAdapter.notifyDataSetChanged();
        }
    }

    private List<FilterBean.DataBean.DataItemBean> getItemBeanListByType(FilterBean.DataBean itemBean, String type) {
        List<FilterBean.DataBean.DataItemBean> itemBeanList = null;
        if ("1".equals(type)) {
            // 经验
            itemBeanList = itemBean.getExperience();
        } else if ("2".equals(type)) {
            // 年龄
            itemBeanList = itemBean.getAge();
        } else if ("3".equals(type)) {
            // 职务
            itemBeanList = itemBean.getDuties();
        } else if ("4".equals(type)) {
            // 能力
            itemBeanList = itemBean.getAbility();
        } else if ("5".equals(type)) {
            // 学历
            itemBeanList = itemBean.getEducation();
        } else if ("6".equals(type)) {
            // 在岗天数
            itemBeanList = itemBean.getOnduty();
        } else if ("7".equals(type)) {
            // 培训经历
            itemBeanList = itemBean.getTraining();
        } else if ("8".equals(type)) {
            // 证件照
            itemBeanList = itemBean.getPaperwork();
        } else if ("9".equals(type)) {
            // 保险
            itemBeanList = itemBean.getInsurance();
        }
        return itemBeanList;
    }

    private void addParamsByType(String type, FilterBean.DataBean.DataItemBean item) {
        if ("1".equals(type)) {
            // 经验
            addParams("experienceBig", item.getBig());
            addParams("experienceSmall", item.getSmall());
        } else if ("2".equals(type)) {
            // 年龄
            addParams("ageBig", item.getBig());
            addParams("ageSmall", item.getSmall());
        } else if ("3".equals(type)) {
            // 职务
            addParams("duties", item.getId());
        } else if ("4".equals(type)) {
            // 能力
            addParams("ability", item.getId());
        } else if ("5".equals(type)) {
            // 学历
            addParams("education", item.getId());
        } else if ("6".equals(type)) {
            // 在岗天数
            addParams("ondutyBig", item.getBig());
            addParams("ondutySmall", item.getSmall());
        } else if ("7".equals(type)) {
            // 培训经历
            addParams("training", item.getIs());
        } else if ("8".equals(type)) {
            // 证件照
            addParams("cardno_img", item.getIs());
        } else if ("9".equals(type)) {
            // 保险
            addParams("insurance", item.getIs());
        }
    }

    private int getSelectPosition(List<FilterBean.DataBean.DataItemBean> itemBeanList, String type) {
        if (itemBeanList == null || selectedRequestParams == null || selectedRequestParams.size() == 0) {
            return 0;
        }
        for (int i = 0; i < itemBeanList.size(); i++) {
            FilterBean.DataBean.DataItemBean dataItemBean = itemBeanList.get(i);
            if ("1".equals(type)
                    && selectedRequestParams.containsKey("experienceBig")
                    && selectedRequestParams.containsKey("experienceSmall")
                    && TextUtils.equals(selectedRequestParams.get("experienceBig"), dataItemBean.getBig())
                    && TextUtils.equals(selectedRequestParams.get("experienceSmall"), dataItemBean.getSmall())) {
                // 经验
                return i;
            } else if ("2".equals(type)
                    && selectedRequestParams.containsKey("ageBig")
                    && selectedRequestParams.containsKey("ageSmall")
                    && TextUtils.equals(selectedRequestParams.get("ageBig"), dataItemBean.getBig())
                    && TextUtils.equals(selectedRequestParams.get("ageSmall"), dataItemBean.getSmall())) {
                // 年龄
                return i;
            } else if ("3".equals(type)
                    && selectedRequestParams.containsKey("duties")
                    && TextUtils.equals(selectedRequestParams.get("duties"), dataItemBean.getId())) {
                // 职务
                return i;
            } else if ("4".equals(type)
                    && selectedRequestParams.containsKey("ability")
                    && TextUtils.equals(selectedRequestParams.get("ability"), dataItemBean.getId())) {
                // 能力
                return i;
            } else if ("5".equals(type)
                    && selectedRequestParams.containsKey("education")
                    && TextUtils.equals(selectedRequestParams.get("education"), dataItemBean.getId())) {
                // 学历
                return i;
            } else if ("6".equals(type)
                    && selectedRequestParams.containsKey("ondutyBig")
                    && selectedRequestParams.containsKey("ondutySmall")
                    && TextUtils.equals(selectedRequestParams.get("ondutyBig"), dataItemBean.getBig())
                    && TextUtils.equals(selectedRequestParams.get("ondutySmall"), dataItemBean.getSmall())) {
                // 在岗天数
                return i;

            } else if ("7".equals(type)
                    && selectedRequestParams.containsKey("training")
                    && TextUtils.equals(selectedRequestParams.get("training"), dataItemBean.getIs())) {
                // 培训经历
                return i;
            } else if ("8".equals(type)
                    && selectedRequestParams.containsKey("cardno_img")
                    && TextUtils.equals(selectedRequestParams.get("cardno_img"), dataItemBean.getIs())) {
                // 证件照
                return i;
            } else if ("9".equals(type)
                    && selectedRequestParams.containsKey("insurance")
                    && TextUtils.equals(selectedRequestParams.get("insurance"), dataItemBean.getIs())) {
                // 保险
                return i;
            }
        }
        return 0;
    }

    private void addParams(String key, String value) {
        if (!TextUtils.isEmpty(key)) {
            tempRequestParams.put(key, value);
        }
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

    /**
     * 点击确定按钮的时候都保存起来了
     */
    public void addSelectToRequestParams() {
        if (tempRequestParams.size() > 0) {
            requestParams.putAll(tempRequestParams);
        }
    }

    /**
     * 每次点击筛选按钮的时候都传过来了
     */
    public void setSelectedByRequestParams(Map<String, String> selectedRequestParams) {
        if (selectedRequestParams == null)
            selectedRequestParams = new HashMap<>();
        this.selectedRequestParams = selectedRequestParams;
    }

    /**
     * 设置默认的请求参数
     */
    public void setDefaultRequestParams(List<FilterBean.DataBean> data) {
        if (data == null || data.size() == 0) {
            return;
        }
        for (FilterBean.DataBean dataBean : data) {
            String type = dataBean.getType();
            List<FilterBean.DataBean.DataItemBean> itemBeanList = getItemBeanListByType(dataBean, type);
            if (itemBeanList != null && itemBeanList.size() > 0)
                addParamsByType(type, itemBeanList.get(0));// 默认的增加第一个为默认值
        }
    }
}
