package com.xxzlkj.shop.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xxzlkj.shop.R;
import com.xxzlkj.shop.activity.shop.GoodsDetailActivity;
import com.xxzlkj.shop.activity.shop.ShopCartActivity;
import com.xxzlkj.shop.config.URLConstants;
import com.xxzlkj.shop.event.DataChangEvent;
import com.xxzlkj.shop.event.ShopCartEvent;
import com.xxzlkj.shop.model.ShopCartList;
import com.xxzlkj.shop.model.ShopCartModel;
import com.xxzlkj.shop.utils.GoodsUtils;
import com.xxzlkj.shop.utils.TextPriceUtil;
import com.xxzlkj.shop.weight.SwipeMenuLayout;
import com.xxzlkj.zhaolinshare.base.app.BaseApplication;
import com.xxzlkj.zhaolinshare.base.base.BaseActivity;
import com.xxzlkj.zhaolinshare.base.model.BaseBean;
import com.xxzlkj.zhaolinshare.base.model.User;
import com.xxzlkj.zhaolinshare.base.net.OnMyActivityRequestListener;
import com.xxzlkj.zhaolinshare.base.net.RequestManager;
import com.xxzlkj.zhaolinshare.base.util.NumberFormatUtils;
import com.xxzlkj.zhaolinshare.base.util.StringUtil;
import com.xxzlkj.zhaolinshare.base.util.TextViewUtils;
import com.xxzlkj.zhaolinshare.base.util.ToastManager;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 购物车
 * Created by louisgeek on 2016/4/27.
 */
public class ShopCartListAdapter extends BaseExpandableListAdapter {
    public static boolean flag = false;
    private List<Map<String, Object>> parentMapList;
    private List<List<Map<String, Object>>> childMapList_list;
    Context context;
    ShopCartActivity mActivity;
    int totalCount = 0;
    double totalPrice = 0.00;
    private OnAllCheckedBoxNeedChangeListener onAllCheckedBoxNeedChangeListener;
    private OnGoodsCheckedChangeListener onGoodsCheckedChangeListener;
    private OnCheckHasGoodsListener onCheckHasGoodsListener;

    public void setOnCheckHasGoodsListener(OnCheckHasGoodsListener onCheckHasGoodsListener) {
        this.onCheckHasGoodsListener = onCheckHasGoodsListener;
    }

    public void setOnGoodsCheckedChangeListener(OnGoodsCheckedChangeListener onGoodsCheckedChangeListener) {
        this.onGoodsCheckedChangeListener = onGoodsCheckedChangeListener;
    }

    public void setOnAllCheckedBoxNeedChangeListener(OnAllCheckedBoxNeedChangeListener onAllCheckedBoxNeedChangeListener) {
        this.onAllCheckedBoxNeedChangeListener = onAllCheckedBoxNeedChangeListener;
    }

    public ShopCartListAdapter(Context context, ShopCartActivity mActivity, List<Map<String, Object>> parentMapList, List<List<Map<String, Object>>> childMapList_list) {
        this.parentMapList = parentMapList;
        this.childMapList_list = childMapList_list;
        this.context = context;
        this.mActivity = mActivity;
        flag = false;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_shop_cart_list, null);
            groupViewHolder = new GroupViewHolder();
            groupViewHolder.mTitle = (TextView) convertView
                    .findViewById(R.id.id_scl_title);
            groupViewHolder.mParentCheckBox = (CheckBox) convertView
                    .findViewById(R.id.id_scl_cb);
            groupViewHolder.mTopLineView = convertView
                    .findViewById(R.id.id_sli_top_line);

            convertView.setTag(groupViewHolder);
        } else {
            groupViewHolder = (GroupViewHolder) convertView.getTag();
        }

        if (groupPosition == 0) {
            groupViewHolder.mTopLineView.setVisibility(View.GONE);
        } else {
            groupViewHolder.mTopLineView.setVisibility(View.VISIBLE);
        }

        ShopCartModel storeBean = (ShopCartModel) parentMapList.get(groupPosition).get("parentName");
        final String parentName = storeBean.getName();
        groupViewHolder.mTitle.setText(parentName);

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
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_sc_list_item_1, null);
            childViewHolder = new ChildViewHolder();
            childViewHolder.mCheckBox = (CheckBox) convertView
                    .findViewById(R.id.id_sli_cb);
            childViewHolder.mAddTextView = (ImageView) convertView
                    .findViewById(R.id.id_sli_add);
            childViewHolder.mGoodsImage = (ImageView) convertView
                    .findViewById(R.id.id_sli_image);
            childViewHolder.mCoverageImageView = (ImageView) convertView
                    .findViewById(R.id.id_coverage_image);
            childViewHolder.mReduceTextView = (ImageView) convertView
                    .findViewById(R.id.id_sli_jian);
            childViewHolder.mCountTextView = (TextView) convertView
                    .findViewById(R.id.id_sli_number);
            convertView.setTag(childViewHolder);
            childViewHolder.mTitleTextView = (TextView) convertView
                    .findViewById(R.id.id_sli_title);
            childViewHolder.mPriceTextView = (TextView) convertView
                    .findViewById(R.id.id_sli_price);
            childViewHolder.mPricesTextView = (TextView) convertView
                    .findViewById(R.id.id_sli_prices);
            childViewHolder.mGuiGeTextView = (TextView) convertView
                    .findViewById(R.id.id_sli_guige);
            childViewHolder.mLineView = convertView
                    .findViewById(R.id.id_sli_line);
            childViewHolder.mLinearLayout = (LinearLayout) convertView
                    .findViewById(R.id.id_sli_layout);
            childViewHolder.mDetailImageView = (ImageView) convertView
                    .findViewById(R.id.id_sli_detail);
            childViewHolder.mDeleteLayout = (RelativeLayout) convertView
                    .findViewById(R.id.id_sli_delete);
            childViewHolder.mImageLayout = (TextView) convertView
                    .findViewById(R.id.id_image_layout_tv);
            childViewHolder.mInventoryTextView = (TextView) convertView
                    .findViewById(R.id.id_sli_tip);
            childViewHolder.mItemLayout = (RelativeLayout) convertView
                    .findViewById(R.id.id_list_itemt);
            childViewHolder.mAdvanceLayout = (LinearLayout) convertView
                    .findViewById(R.id.id_advance_layout);
            childViewHolder.mAdvanceDescTextView = convertView
                    .findViewById(R.id.id_advance_desc);
        } else {
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }

        if (childPosition == getChildrenCount(groupPosition) - 1) {
            // 隐藏分割线
            childViewHolder.mLineView.setVisibility(View.GONE);
        } else {
            childViewHolder.mLineView.setVisibility(View.VISIBLE);
        }

        final ShopCartList.DataBean.GBean goodsBean = (ShopCartList.DataBean.GBean) childMapList_list.get(groupPosition).get(childPosition).get("childName");
        String ads = goodsBean.getAds();
        childViewHolder.mItemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.startActivity(GoodsDetailActivity.newIntent(context, goodsBean.getGoods_id(), mActivity.getStoreId()));
            }
        });
        if (flag) {
            if (!TextUtils.isEmpty(ads)) {
                childViewHolder.mDetailImageView.setVisibility(View.VISIBLE);
                childViewHolder.mLinearLayout.setBackgroundResource(R.drawable.shape_gray_radius);
                childViewHolder.mLinearLayout.setClickable(true);
                childViewHolder.mLinearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                    context.startActivity(GoodsDetailActivity.newIntent(context,goodsBean.getId()));
                        EventBus.getDefault().postSticky(new ShopCartEvent(goodsBean.getGoods_id(), goodsBean.getId(), goodsBean.getNum()));
                    }
                });
            } else {
                childViewHolder.mDetailImageView.setVisibility(View.GONE);
                childViewHolder.mLinearLayout.setBackgroundResource(R.drawable.shape_white);
                childViewHolder.mLinearLayout.setClickable(false);
            }

        } else {
            childViewHolder.mDetailImageView.setVisibility(View.GONE);
            childViewHolder.mLinearLayout.setBackgroundResource(R.drawable.shape_white);
            childViewHolder.mLinearLayout.setClickable(false);
        }
        // 设置商品图片和商品图层
        GoodsUtils.setGoodsCoverageImage(context, childViewHolder.mGoodsImage, childViewHolder.mCoverageImageView, goodsBean.getSimg(), goodsBean.getActivity_icon_img());

        childViewHolder.mCountTextView.setText(goodsBean.getNum());
        TextViewUtils.setText(childViewHolder.mTitleTextView, goodsBean.getTitle());

        if (!TextUtils.isEmpty(ads)) {
            childViewHolder.mGuiGeTextView.setVisibility(View.VISIBLE);
            childViewHolder.mGuiGeTextView.setText(ads);
        } else {
            if ("1".equals(goodsBean.getActivitys())) {
                // 预售商品
                childViewHolder.mGuiGeTextView.setVisibility(View.GONE);
            } else {
                childViewHolder.mGuiGeTextView.setVisibility(View.INVISIBLE);
            }

        }
        TextViewUtils.setText(childViewHolder.mGuiGeTextView, goodsBean.getAds());
        String price = goodsBean.getPrice();
        String prices = goodsBean.getPrices();
        TextPriceUtil.setTextPrices(price, prices, childViewHolder.mPricesTextView);
        TextViewUtils.setText(childViewHolder.mPriceTextView, "￥" + price);

        childViewHolder.mCheckBox.setChecked(goodsBean.isChecked());
        childViewHolder.mCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final boolean nowBeanChecked = goodsBean.isChecked();
                //更新数据
                goodsBean.setIsChecked(!nowBeanChecked);

                boolean isOneParentAllChildIsChecked = dealOneParentAllChildIsChecked(groupPosition);

                ShopCartModel storeBean = (ShopCartModel) parentMapList.get(groupPosition).get("parentName");
                storeBean.setIsChecked(isOneParentAllChildIsChecked);

                notifyDataSetChanged();
                //控制总checkedbox 接口
                onAllCheckedBoxNeedChangeListener.onCheckedBoxNeedChange(dealAllParentIsChecked());
                dealPrice();
            }
        });

        // 当前商品库存数
        double stock = NumberFormatUtils.toDouble(goodsBean.getStock());
        // 安全库存
        double minQt = NumberFormatUtils.toDouble(goodsBean.getMin_qty());
        if (stock > minQt) {// 当前商品库存数 > 安全库存
            childViewHolder.mInventoryTextView.setVisibility(View.GONE);
            childViewHolder.mImageLayout.setAlpha(0);
        } else {// 当前商品库存数 <= 安全库存
            childViewHolder.mInventoryTextView.setVisibility(View.VISIBLE);
            if (stock > 0 && stock <= minQt) {// 当前商品库存数 <= 安全库存 && 大于零
                childViewHolder.mInventoryTextView.setText("仅剩" + goodsBean.getStock() + "件");
                childViewHolder.mImageLayout.setAlpha(0);
            } else {
                childViewHolder.mInventoryTextView.setText("补货中");
                childViewHolder.mImageLayout.setAlpha(0.7f);
            }
        }
        // 加一
        childViewHolder.mAddTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NumberFormatUtils.toDouble(goodsBean.getNum()) < stock) {// 当前商品数 < 当前商品库存数
                    dealAdd(goodsBean);
                    updateShopCart(goodsBean.getId(), goodsBean.getNum());
                } else {
                    if (NumberFormatUtils.toDouble(goodsBean.getNum()) >= stock) {
                        ToastManager.showShortToast(context, "已达库存上限");
                    }
                }
            }
        });
        // 减一
        childViewHolder.mReduceTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dealReduce(goodsBean);
                if (NumberFormatUtils.toInt(goodsBean.getNum()) >= 1) {
                    updateShopCart(goodsBean.getId(), goodsBean.getNum());
                }
            }
        });
        childViewHolder.mDetailImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        final View finalConvertView = convertView;
        childViewHolder.mDeleteLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SwipeMenuLayout swipeMenuLayout = (SwipeMenuLayout) finalConvertView;
                swipeMenuLayout.smoothClose();
                removeOneGood(groupPosition, childPosition, goodsBean.getId());
            }
        });

        if ("1".equals(goodsBean.getActivitys())) {
            // 预售商品
            childViewHolder.mAdvanceLayout.setVisibility(View.VISIBLE);
            childViewHolder.mAdvanceDescTextView.setText(goodsBean.getActivity_desc());
        } else {
            // 其他
            childViewHolder.mAdvanceLayout.setVisibility(View.GONE);
        }


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
            ShopCartModel storeBean = (ShopCartModel) parentMapList.get(i).get("parentName");
            storeBean.setIsChecked(isChecked);

            List<Map<String, Object>> childMapList = childMapList_list.get(i);
            for (int j = 0; j < childMapList.size(); j++) {
                ShopCartList.DataBean.GBean goodsBean = (ShopCartList.DataBean.GBean) childMapList.get(j).get("childName");
                goodsBean.setIsChecked(isChecked);
            }
        }
        notifyDataSetChanged();
        dealPrice();
    }

    private void setupOneParentAllChildChecked(boolean isChecked, int groupPosition) {
        ShopCartModel storeBean = (ShopCartModel) parentMapList.get(groupPosition).get("parentName");
        storeBean.setIsChecked(isChecked);

        List<Map<String, Object>> childMapList = childMapList_list.get(groupPosition);
        for (int j = 0; j < childMapList.size(); j++) {
            ShopCartList.DataBean.GBean goodsBean = (ShopCartList.DataBean.GBean) childMapList.get(j).get("childName");
            goodsBean.setIsChecked(isChecked);
        }
        notifyDataSetChanged();
        dealPrice();
    }

    public boolean dealOneParentAllChildIsChecked(int groupPosition) {
        // StoreBean storeBean= (StoreBean) parentMapList.get(groupPosition).get("parentName");
        List<Map<String, Object>> childMapList = childMapList_list.get(groupPosition);
        for (int j = 0; j < childMapList.size(); j++) {
            ShopCartList.DataBean.GBean goodsBean = (ShopCartList.DataBean.GBean) childMapList.get(j).get("childName");
            if (!goodsBean.isChecked()) {
                return false;//如果有一个没选择  就false
            }
        }
        return true;
    }

    public boolean dealAllParentIsChecked() {
        for (int i = 0; i < parentMapList.size(); i++) {
            ShopCartModel storeBean = (ShopCartModel) parentMapList.get(i).get("parentName");
            if (!storeBean.isChecked()) {
                return false;//如果有一个没选择  就false
            }
        }
        return true;
    }

    /**
     * 处理总价
     */
    public void dealPrice() {
        totalCount = 0;
        totalPrice = 0.00;
        for (int i = 0; i < parentMapList.size(); i++) {
            List<Map<String, Object>> childMapList = childMapList_list.get(i);
            for (int j = 0; j < childMapList.size(); j++) {
                ShopCartList.DataBean.GBean goodsBean = (ShopCartList.DataBean.GBean) childMapList.get(j).get("childName");
                int count = NumberFormatUtils.toInt(goodsBean.getNum());
                double discountPrice = NumberFormatUtils.toDouble(goodsBean.getPrice());
                if (goodsBean.isChecked()) {
                    totalCount += count;//单品多数量只记1
                    totalPrice += discountPrice * count;
                }

            }
        }
        //计算回调
        onGoodsCheckedChangeListener.onGoodsCheckedChange(totalCount, NumberFormatUtils.toDouble(StringUtil.saveTwoDecimal(String.valueOf(totalPrice))));
    }


    /**
     * 商品加一
     *
     * @param goodsBean
     */
    public void dealAdd(ShopCartList.DataBean.GBean goodsBean) {
        int count = NumberFormatUtils.toInt(goodsBean.getNum());
        count++;
        goodsBean.setNum(count + "");
        notifyDataSetChanged();
        dealPrice();
    }

    /**
     * 商品减一
     *
     * @param goodsBean
     */
    public void dealReduce(ShopCartList.DataBean.GBean goodsBean) {
        int count = NumberFormatUtils.toInt(goodsBean.getNum());
        if (count == 1) {
            return;
        }
        count--;
        goodsBean.setNum(count + "");
        notifyDataSetChanged();
        dealPrice();
    }

    /**
     * 删除单个商品
     *
     * @param groupPosition
     * @param childPosition
     */
    public void removeOneGood(int groupPosition, int childPosition, String id) {
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
        deleteShopCart(id + ",");
        notifyDataSetChanged();
        dealPrice();
    }

    public interface OnAllCheckedBoxNeedChangeListener {
        void onCheckedBoxNeedChange(boolean allParentIsChecked);
    }

    public interface OnGoodsCheckedChangeListener {
        void onGoodsCheckedChange(int totalCount, double totalPrice);
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
            ShopCartModel storeBean = (ShopCartModel) parentMapList.get(i).get("parentName");
            if (storeBean.isChecked()) {
                clearShopCart();
                parentMapList.remove(i);
                childMapList_list.remove(i);
            } else {
                List<Map<String, Object>> childMapList = childMapList_list.get(i);
                for (int j = childMapList.size() - 1; j >= 0; j--) {//倒过来遍历  remove
                    ShopCartList.DataBean.GBean goodsBean = (ShopCartList.DataBean.GBean) childMapList.get(j).get("childName");
                    if (goodsBean.isChecked()) {
                        ids = ids + goodsBean.getId() + ",";
                        childMapList.remove(j);
                    }
                }
            }

        }
        if (parentMapList != null && parentMapList.size() > 0) {
            onCheckHasGoodsListener.onCheckHasGoods(true);//
        } else {
            onCheckHasGoodsListener.onCheckHasGoods(false);//
        }
        if (!TextUtils.isEmpty(ids)) {
            deleteShopCart(ids);
        }
        notifyDataSetChanged();//
        dealPrice();//重新计算
    }

    class GroupViewHolder {
        private TextView mTitle;
        private CheckBox mParentCheckBox;
        private View mTopLineView;
    }

    class ChildViewHolder {
        private CheckBox mCheckBox;
        private TextView mTitleTextView;
        private TextView mPriceTextView;
        private TextView mPricesTextView;
        private TextView mGuiGeTextView;
        private TextView mCountTextView;
        private TextView mInventoryTextView;
        private ImageView mGoodsImage;
        private ImageView mCoverageImageView;
        private ImageView mReduceTextView;
        private ImageView mAddTextView;
        private ImageView mDetailImageView;
        private View mLineView;
        private LinearLayout mLinearLayout;
        private LinearLayout mAdvanceLayout;
        private RelativeLayout mDeleteLayout;
        private RelativeLayout mItemLayout;
        private TextView mImageLayout;
        private TextView mAdvanceDescTextView;
    }

    /**
     * 修改数量
     *
     * @param id
     * @param num
     */
    private void updateShopCart(String id, String num) {
        Map<String, String> map = new HashMap<>();
        map.put(URLConstants.REQUEST_PARAM_ID, id);
        map.put(URLConstants.REQUEST_PARAM_NUM, num);
        RequestManager.createRequest(URLConstants.REQUEST_EDITCART, map,
                new OnMyActivityRequestListener<BaseBean>((BaseActivity) context) {
                    @Override
                    public void onSuccess(BaseBean bean) {
                        EventBus.getDefault().postSticky(new DataChangEvent(0, true));
                    }
                });
    }

    /**
     * 删除购物车
     *
     * @param ids 购物车id 多个用英文逗号隔开
     */
    private void deleteShopCart(String ids) {
        Map<String, String> map = new HashMap<>();
        map.put(URLConstants.REQUEST_PARAM_ID, ids.substring(0, ids.length() - 1).trim());
        RequestManager.createRequest(URLConstants.REQUEST_DELCART, map,
                new OnMyActivityRequestListener<BaseBean>((BaseActivity) context) {
                    @Override
                    public void onSuccess(BaseBean bean) {
                        EventBus.getDefault().postSticky(new DataChangEvent(0, true));
                    }
                });
    }

    /**
     * 清空购物车
     */
    private void clearShopCart() {
        Map<String, String> map = new HashMap<>();
        User user = BaseApplication.getInstance().getLoginUser();
        if (user == null) {
            return;
        }
        map.put(URLConstants.REQUEST_PARAM_UID, user.getData().getId());
        RequestManager.createRequest(URLConstants.REQUEST_CLEARCART, map,
                new OnMyActivityRequestListener<BaseBean>((BaseActivity) context) {
                    @Override
                    public void onSuccess(BaseBean bean) {
                        EventBus.getDefault().postSticky(new DataChangEvent(0, true));
                    }
                });
    }

    public ShopCartList.DataBean getGoodsList() {
        ShopCartList.DataBean dataBean = new ShopCartList.DataBean();
        List<ShopCartList.DataBean.GBean> goodses = new ArrayList<>();
        for (int i = 0; i < parentMapList.size(); i++) {
            List<Map<String, Object>> childMapList = childMapList_list.get(i);
            for (int j = 0; j < childMapList.size(); j++) {
                ShopCartList.DataBean.GBean goodsBean = (ShopCartList.DataBean.GBean) childMapList.get(j).get("childName");
                if (goodsBean.isChecked()) {
                    goodses.add(goodsBean);
                }
            }
        }
        dataBean.setGoods(goodses);
        return dataBean;
    }

}
