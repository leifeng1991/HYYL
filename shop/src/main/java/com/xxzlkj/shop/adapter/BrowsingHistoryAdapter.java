package com.xxzlkj.shop.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.xxzlkj.shop.R;
import com.xxzlkj.shop.activity.shop.GoodsDetailActivity;
import com.xxzlkj.shop.config.URLConstants;
import com.xxzlkj.shop.model.BrowserHistory;
import com.xxzlkj.shop.model.BrowsingHistoryModel;
import com.xxzlkj.shop.utils.GoodsUtils;
import com.xxzlkj.shop.utils.TextPriceUtil;
import com.xxzlkj.shop.weight.CustomButton;
import com.xxzlkj.zhaolinshare.base.base.BaseActivity;
import com.xxzlkj.zhaolinshare.base.model.BaseBean;
import com.xxzlkj.zhaolinshare.base.net.OnMyActivityRequestListener;
import com.xxzlkj.zhaolinshare.base.net.RequestManager;
import com.xxzlkj.zhaolinshare.base.util.TextViewUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 浏览记录
 * Created by Administrator on 2017/4/18.
 */

public class BrowsingHistoryAdapter extends BaseExpandableListAdapter {
    public static boolean isVisibelFlag;
    List<Map<String, Object>> parentMapList;
    List<List<Map<String, Object>>> childMapList_list;
    Context context;
    //全选/取消全选监听
    OnAllCheckedBoxNeedChangeListener onAllCheckedBoxNeedChangeListener;
    //检查
    OnCheckHasGoodsListener onCheckHasGoodsListener;
    private Activity activity;

    public void setOnCheckHasGoodsListener(OnCheckHasGoodsListener onCheckHasGoodsListener) {
        this.onCheckHasGoodsListener = onCheckHasGoodsListener;
    }

    public void setOnAllCheckedBoxNeedChangeListener(OnAllCheckedBoxNeedChangeListener onAllCheckedBoxNeedChangeListener) {
        this.onAllCheckedBoxNeedChangeListener = onAllCheckedBoxNeedChangeListener;
    }

    public BrowsingHistoryAdapter(Context context, Activity activity, List<Map<String, Object>> parentMapList, List<List<Map<String, Object>>> childMapList_list) {
        this.parentMapList = parentMapList;
        this.childMapList_list = childMapList_list;
        this.context = context;
        this.activity = activity;
        isVisibelFlag = false;
    }


    //获取当前父item的数据数量
    @Override
    public int getGroupCount() {
        return parentMapList.size();
    }

    //获取当前父item下的子item的个数
    @Override
    public int getChildrenCount(int groupPosition) {
        return childMapList_list.get(groupPosition).size();
    }

    //获取当前父item的数据
    @Override
    public Object getGroup(int groupPosition) {
        return parentMapList.get(groupPosition);
    }

    //得到子item需要关联的数据
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childMapList_list.get(groupPosition).get(childPosition);
    }

    //得到父item的ID
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    //得到子item的ID
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        //return false;
        return true;
    }

    //设置父item组件
    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder groupViewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_bh_list_item, null);
            groupViewHolder = new GroupViewHolder();
            groupViewHolder.mAddTime = (TextView) convertView
                    .findViewById(R.id.id_bh_add_time);
            groupViewHolder.mParentCheckBox = (CheckBox) convertView
                    .findViewById(R.id.id_bh_parent_checkbox);
            convertView.setTag(groupViewHolder);
        } else {
            groupViewHolder = (GroupViewHolder) convertView.getTag();
        }

        BrowsingHistoryModel storeBean = (BrowsingHistoryModel) parentMapList.get(groupPosition).get("parentName");
        final String parentName = storeBean.getAdd_time();
        groupViewHolder.mAddTime.setText(parentName);

        //覆盖原有收起展开事件
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });


        groupViewHolder.mParentCheckBox.setChecked(storeBean.isChecked());
        final boolean nowBeanChecked = storeBean.isChecked();
        groupViewHolder.mParentCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupOneParentAllChildChecked(!nowBeanChecked, groupPosition);
                //控制总checkedbox 接口
                onAllCheckedBoxNeedChangeListener.onCheckedBoxNeedChange(dealAllParentIsChecked());
            }
        });
        return convertView;
    }

    //设置子item的组件
    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder childViewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_bh_list_item_item, null);
            childViewHolder = new ChildViewHolder();
            childViewHolder.mCheckBox = (CheckBox) convertView
                    .findViewById(R.id.id_bli_checkbox);
            childViewHolder.mGoodsImage = (ImageView) convertView
                    .findViewById(R.id.id_hii_image);
            childViewHolder.mCoverageImageView = (ImageView) convertView
                    .findViewById(R.id.id_coverage_image);
            convertView.setTag(childViewHolder);
            childViewHolder.mTitleTextView = (TextView) convertView
                    .findViewById(R.id.id_hii_text);
            childViewHolder.mPriceTextView = (TextView) convertView
                    .findViewById(R.id.id_hii_price);
            childViewHolder.mPricesTextView = (TextView) convertView
                    .findViewById(R.id.id_hii_price_line);
            childViewHolder.mGuiGeTextView = (TextView) convertView
                    .findViewById(R.id.id_hii_norms);
            childViewHolder.mNowBuyTextView = (TextView) convertView
                    .findViewById(R.id.id_hii_bn);
            childViewHolder.mYuShouButton = (CustomButton) convertView
                    .findViewById(R.id.id_yushou_btn);
        } else {
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }

        final BrowserHistory.DataBean.ListBean goods =
                (BrowserHistory.DataBean.ListBean) childMapList_list.get(groupPosition).get(childPosition).get("childName");

        if (isVisibelFlag) {
            childViewHolder.mCheckBox.setVisibility(View.VISIBLE);
            childViewHolder.mNowBuyTextView.setVisibility(View.GONE);
        } else {
            // 控制预售字样的显示和隐藏
            GoodsUtils.setYuShouVisible("", childViewHolder.mNowBuyTextView, childViewHolder.mYuShouButton, null);

            childViewHolder.mCheckBox.setVisibility(View.GONE);
        }

        childViewHolder.mNowBuyTextView.setOnClickListener(v -> activity.startActivity(GoodsDetailActivity.newIntent(context, goods.getGoods_id())));
        // 设置商品图片和商品图层
        GoodsUtils.setGoodsCoverageImage(context,childViewHolder.mGoodsImage,childViewHolder.mCoverageImageView,goods.getSimg(),goods.getActivity_icon_img());

        TextViewUtils.setText(childViewHolder.mTitleTextView, goods.getTitle());
        TextViewUtils.setText(childViewHolder.mGuiGeTextView, goods.getAds());
        TextViewUtils.setText(childViewHolder.mPriceTextView, "￥" + goods.getPrice());
        TextPriceUtil.setTextPrices(goods.getPrice(), goods.getPrices(), childViewHolder.mPricesTextView);// 设置原价


        childViewHolder.mCheckBox.setChecked(goods.isChecked());
        childViewHolder.mCheckBox.setOnClickListener(v -> {
            final boolean nowBeanChecked = goods.isChecked();
            //更新数据
            goods.setIsChecked(!nowBeanChecked);

            boolean isOneParentAllChildIsChecked = dealOneParentAllChildIsChecked(groupPosition);

            BrowsingHistoryModel storeBean = (BrowsingHistoryModel) parentMapList.get(groupPosition).get("parentName");
            storeBean.setIsChecked(isOneParentAllChildIsChecked);

            notifyDataSetChanged();
            //控制总checkedbox 接口
            onAllCheckedBoxNeedChangeListener.onCheckedBoxNeedChange(dealAllParentIsChecked());
        });

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        // return false;
        return true;
    }

    //供全选按钮调用
    public void setupAllChecked(boolean isChecked) {
        for (int i = 0; i < parentMapList.size(); i++) {
            BrowsingHistoryModel storeBean = (BrowsingHistoryModel) parentMapList.get(i).get("parentName");
            storeBean.setIsChecked(isChecked);

            List<Map<String, Object>> childMapList = childMapList_list.get(i);
            for (int j = 0; j < childMapList.size(); j++) {
                BrowserHistory.DataBean.ListBean goodsBean =
                        (BrowserHistory.DataBean.ListBean) childMapList.get(j).get("childName");
                goodsBean.setIsChecked(isChecked);
            }
        }
        notifyDataSetChanged();
    }

    private void setupOneParentAllChildChecked(boolean isChecked, int groupPosition) {
        BrowsingHistoryModel storeBean = (BrowsingHistoryModel) parentMapList.get(groupPosition).get("parentName");
        storeBean.setIsChecked(isChecked);

        List<Map<String, Object>> childMapList = childMapList_list.get(groupPosition);
        for (int j = 0; j < childMapList.size(); j++) {
            BrowserHistory.DataBean.ListBean goodsBean = (BrowserHistory.DataBean.ListBean) childMapList.get(j).get("childName");
            goodsBean.setIsChecked(isChecked);
        }
        notifyDataSetChanged();
    }

    public boolean dealOneParentAllChildIsChecked(int groupPosition) {
        // StoreBean storeBean= (StoreBean) parentMapList.get(groupPosition).get("parentName");
        List<Map<String, Object>> childMapList = childMapList_list.get(groupPosition);
        for (int j = 0; j < childMapList.size(); j++) {
            BrowserHistory.DataBean.ListBean goodsBean = (BrowserHistory.DataBean.ListBean) childMapList.get(j).get("childName");
            if (!goodsBean.isChecked()) {
                return false;//如果有一个没选择  就false
            }
        }
        return true;
    }

    public boolean dealAllParentIsChecked() {
        for (int i = 0; i < parentMapList.size(); i++) {
            BrowsingHistoryModel storeBean = (BrowsingHistoryModel) parentMapList.get(i).get("parentName");
            if (!storeBean.isChecked()) {
                return false;//如果有一个没选择  就false
            }
        }
        return true;
    }


    /**
     * 删除单个商品
     *
     * @param groupPosition
     * @param childPosition
     */
    public void removeOneGood(int groupPosition, int childPosition) {
        List<Map<String, Object>> childMapList = childMapList_list.get(groupPosition);
        childMapList.remove(childPosition);

        //通过子项
        if (childMapList != null && childMapList.size() > 0) {

        } else {
            parentMapList.remove(groupPosition);
            childMapList_list.remove(groupPosition);//！！！！因为parentMapList和childMapList_list是pos关联的  得保持一致
        }
        if (parentMapList != null && parentMapList.size() > 0) {
            onCheckHasGoodsListener.onCheckHasGoods(true);//
        } else {
            onCheckHasGoodsListener.onCheckHasGoods(false);//
        }
        notifyDataSetChanged();
    }


    public interface OnAllCheckedBoxNeedChangeListener {
        void onCheckedBoxNeedChange(boolean allParentIsChecked);
    }

    public interface OnCheckHasGoodsListener {
        void onCheckHasGoods(boolean isHasGoods);
    }

    /**
     * 删除选中的全部商品
     */
    public void removeGoods() {
        String ids = "";
        for (int i = parentMapList.size() - 1; i >= 0; i--) {//倒过来遍历  remove
            BrowsingHistoryModel storeBean = (BrowsingHistoryModel) parentMapList.get(i).get("parentName");
            if (storeBean.isChecked()) {
                parentMapList.remove(i);
                childMapList_list.remove(i);
            } else {
                List<Map<String, Object>> childMapList = childMapList_list.get(i);
                for (int j = childMapList.size() - 1; j >= 0; j--) {//倒过来遍历  remove
                    BrowserHistory.DataBean.ListBean goodsBean =
                            (BrowserHistory.DataBean.ListBean) childMapList.get(j).get("childName");
                    if (goodsBean.isChecked()) {
                        ids = ids + goodsBean.getId() + ",";
                        childMapList.remove(j);
                    }
                }
            }

        }
        if (parentMapList != null && parentMapList.size() > 0) {
            onCheckHasGoodsListener.onCheckHasGoods(true);
        } else {
            onCheckHasGoodsListener.onCheckHasGoods(false);
        }
        if (!TextUtils.isEmpty(ids)) {
            deleteBrowserHistory(ids);
        }
        notifyDataSetChanged();
    }

    class GroupViewHolder {
        private TextView mAddTime;
        private CheckBox mParentCheckBox;
    }

    class ChildViewHolder {
        private CheckBox mCheckBox;
        private TextView mTitleTextView;
        private TextView mPriceTextView;
        private TextView mPricesTextView;
        private TextView mGuiGeTextView;
        private TextView mNowBuyTextView;
        private ImageView mGoodsImage;
        private ImageView mCoverageImageView;
        private CustomButton mYuShouButton;

    }

    /**
     * 删除浏览记录
     *
     * @param ids id 多个用英文逗号隔开
     */
    private void deleteBrowserHistory(String ids) {
        Map<String, String> map = new HashMap<>();
        map.put(URLConstants.REQUEST_PARAM_IDS, ids.substring(0, ids.length() - 1).trim());
        RequestManager.createRequest(URLConstants.REQUEST_TRACE_DEL, map,
                new OnMyActivityRequestListener<BaseBean>((BaseActivity) context) {
                    @Override
                    public void onSuccess(BaseBean bean) {
                    }
                });
    }
}