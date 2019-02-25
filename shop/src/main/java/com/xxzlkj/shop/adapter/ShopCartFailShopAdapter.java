package com.xxzlkj.shop.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xxzlkj.shop.R;
import com.xxzlkj.shop.activity.shop.SearchGoodsListActivity;
import com.xxzlkj.shop.config.URLConstants;
import com.xxzlkj.shop.model.ShopCartList;
import com.xxzlkj.shop.utils.TextPriceUtil;
import com.xxzlkj.zhaolinshare.base.base.BaseActivity;
import com.xxzlkj.zhaolinshare.base.model.BaseBean;
import com.xxzlkj.zhaolinshare.base.net.OnMyActivityRequestListener;
import com.xxzlkj.zhaolinshare.base.net.RequestManager;
import com.xxzlkj.zhaolinshare.base.util.PicassoUtils;
import com.xxzlkj.zhaolinshare.base.util.ToastManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 商品失效
 * Created by Administrator on 2017/3/22.
 */

public class ShopCartFailShopAdapter extends BaseAdapter {
    private Context context;
    private Activity mActivity;
    private List<ShopCartList.DataBean.GBean> mLists;

    public ShopCartFailShopAdapter(Context context, Activity mActivity, List<ShopCartList.DataBean.GBean> mLists) {
        this.context = context;
        this.mLists = mLists;
        this.mActivity = mActivity;
    }

    @Override
    public int getCount() {
        return mLists.size();
    }

    @Override
    public Object getItem(int i) {
        return mLists.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        final CheckBoxViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.adapter_sc_list_item_2, null);
            holder = new CheckBoxViewHolder();
            holder.mGoodsImage = (ImageView) view.findViewById(R.id.id_sli_image);
            holder.mGoodsTitle = (TextView) view.findViewById(R.id.id_sli_title);
            holder.mGoodsGg = (TextView) view.findViewById(R.id.id_sli_guige);
            holder.mBottomImage = (ImageView) view.findViewById(R.id.id_sli_detail);
            holder.mPrice = (TextView) view.findViewById(R.id.id_sli_price);
            holder.mPrices = (TextView) view.findViewById(R.id.id_sli_prices);
            holder.mNumber = (TextView) view.findViewById(R.id.id_sli_number);
            holder.mDeleteText = (TextView) view.findViewById(R.id.id_sli_delete);
            holder.mSameText = (TextView) view.findViewById(R.id.id_sli_same);
            holder.mGgLayout = (LinearLayout) view.findViewById(R.id.id_sli_layout);
            view.setTag(holder);
        } else {
            holder = (CheckBoxViewHolder) view.getTag();
        }

        final ShopCartList.DataBean.GBean gBean = mLists.get(i);
        PicassoUtils.setImageBig(context, gBean.getSimg(), holder.mGoodsImage);
        holder.mGoodsTitle.setText(gBean.getTitle());
        String ads = gBean.getAds();
        if (!TextUtils.isEmpty(ads)) {
            holder.mGgLayout.setVisibility(View.VISIBLE);
            holder.mGoodsGg.setText(gBean.getAds());
        } else {
            holder.mGgLayout.setVisibility(View.INVISIBLE);
        }

        String price = gBean.getPrice();
        holder.mPrice.setText("￥" + price);
        String prices = gBean.getPrices();
        TextPriceUtil.setTextPrices(price, prices, holder.mPrices);
        holder.mDeleteText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteGoods(i, gBean.getId());
            }
        });
        holder.mSameText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.startActivity(SearchGoodsListActivity.newIntent(context, -1, gBean.getGroupid(), "", ""));
            }
        });
        return view;
    }

    public static class CheckBoxViewHolder {
        private ImageView mGoodsImage;
        private TextView mGoodsTitle;
        private TextView mGoodsGg;
        private ImageView mBottomImage;
        private TextView mPrice;
        private TextView mPrices;
        private TextView mNumber;
        private TextView mShiXiao;
        private TextView mDeleteText;
        private TextView mSameText;
        private LinearLayout mGgLayout;
    }

    /**
     * 删除失效商品
     */
    private void deleteGoods(final int position, String id) {
        Map<String, String> map = new HashMap<>();
        map.put(URLConstants.REQUEST_PARAM_ID, id);
        RequestManager.createRequest(URLConstants.REQUEST_DELCART, map,
                new OnMyActivityRequestListener<BaseBean>((BaseActivity) context) {
                    @Override
                    public void onSuccess(BaseBean bean) {
                        mLists.remove(position);
                        notifyDataSetChanged();
                    }

                    @Override
                    public void onFailed(boolean isError, String code, String message) {
                        super.onFailed(isError, code, message);
                        ToastManager.showShortToast(context, "删除失败");

                    }
                });
    }
}
