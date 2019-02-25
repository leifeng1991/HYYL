package com.xxzlkj.huayiyanglao.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.xxzlkj.huayiyanglao.R;
import com.xxzlkj.huayiyanglao.activity.foodorder.TakeOutActivity;
import com.xxzlkj.huayiyanglao.event.FoodsSaleListEvent;
import com.xxzlkj.huayiyanglao.event.FoodsShopCartEvent;
import com.xxzlkj.huayiyanglao.model.FoodsAddCartBean;
import com.xxzlkj.huayiyanglao.model.FoodsBean;
import com.xxzlkj.huayiyanglao.model.FoodsCartListBean;
import com.xxzlkj.huayiyanglao.util.AddShopCartUtil;
import com.xxzlkj.huayiyanglao.util.HYNetRequestUtil;
import com.xxzlkj.zhaolinshare.base.base.BaseAdapter;
import com.xxzlkj.zhaolinshare.base.base.BaseViewHolder;
import com.xxzlkj.zhaolinshare.base.util.NumberFormatUtils;
import com.xxzlkj.zhaolinshare.base.util.PicassoUtils;
import com.xxzlkj.zhaolinshare.base.util.ToastManager;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * 描述:外卖右边列表内容适配器
 *
 * @author leifeng
 *         2017/11/21 15:29
 */


public class TakeOutContentItemItemAdapter extends BaseAdapter<FoodsBean> {
    private View mShopCartView;
    private TakeOutActivity activity;

    /**
     * @param mShopCartView 加入购物车时目标view
     */
    public TakeOutContentItemItemAdapter(Context context, TakeOutActivity activity, View mShopCartView, int itemId) {
        super(context, itemId);
        this.mShopCartView = mShopCartView;
        this.activity = activity;
    }

    @Override
    public void convert(BaseViewHolder holder, int position, final FoodsBean itemBean) {
        ImageView mImageView = holder.getView(R.id.id_image);// 图片
        TextView mTitleTextView = holder.getView(R.id.id_title);// 标题
        TextView mContentTextView = holder.getView(R.id.id_content);// 内容
        TextView mResidueNumberTextView = holder.getView(R.id.id_residue_number);// 剩余数量
        TextView mPriceTextView = holder.getView(R.id.id_price);// 价格
        ImageView mReduceImageView = holder.getView(R.id.id_reduce);// 减
        TextView mGoodsNumberTextView = holder.getView(R.id.id_goods_number);// 商品数量
        ImageView mAddImageView = holder.getView(R.id.id_add);// 加
        // 设置数据
        PicassoUtils.setImageBig(mContext, itemBean.getSimg(), mImageView);
        mTitleTextView.setText(itemBean.getTitle());
        mContentTextView.setText(itemBean.getAds());
        mResidueNumberTextView.setText(String.format("剩余%s份", itemBean.getNum()));
        mPriceTextView.setText(String.format("￥%s", itemBean.getPrice()));
        // 1可售状态 0不可售状态
        String stoptime = itemBean.getStoptime();
        // 选择数量 未选择为0
        int count = NumberFormatUtils.toInt(itemBean.getCount());
        if (count <= 0) {
            mReduceImageView.setVisibility(View.GONE);
            mGoodsNumberTextView.setVisibility(View.GONE);
        } else {
            mReduceImageView.setVisibility(View.VISIBLE);
            mGoodsNumberTextView.setVisibility(View.VISIBLE);
            mGoodsNumberTextView.setText(itemBean.getCount());
        }
        // 加一
        mAddImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if ("0".equals(stoptime)) {
                // 可售
                if (mShopCartView != null) {
                    HYNetRequestUtil.foodsAddCart(activity, itemBean.getId(), new HYNetRequestUtil.OnRequestSuccessListener() {
                        @Override
                        public void RequestSuccess(FoodsAddCartBean bean) {
                            ToastManager.showShortToast(activity.getApplicationContext(), "加入购物车成功");
                            // 加入购物车
                            AddShopCartUtil.addShopCartAnimal(activity, v, mShopCartView);
                            // 减号和数量显示
                            mReduceImageView.setVisibility(View.VISIBLE);
                            mGoodsNumberTextView.setVisibility(View.VISIBLE);
                            // 减号和数量动画
                            mReduceImageView.setAnimation(getShowAnimation());
                            mGoodsNumberTextView.setAnimation(getShowAnimation());
                            // 刷新数据
                            FoodsBean data = bean.getData();
                            EventBus.getDefault().postSticky(new FoodsSaleListEvent(itemBean.getId(), data.getNum()));
                            // 刷新购物车列表
                            EventBus.getDefault().postSticky(new FoodsShopCartEvent());
                        }
                    });
                }
//                } else {
                // 不可售
//                    ToastManager.showMsgToast(mContext, "当前商品不可售");
//                }

            }
        });
        // 减一
        mReduceImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 购物车id
                String shopCartId = getShopCartId(itemBean.getId());
                if (!TextUtils.isEmpty(shopCartId)){
                    int mCount = NumberFormatUtils.toInt(itemBean.getCount());

                    if (mCount == 1){
                        // 删除购物车
                        HYNetRequestUtil.foodsDelCart(activity, shopCartId, new HYNetRequestUtil.OnRequestSuccessListener() {
                            @Override
                            public void RequestSuccess(FoodsAddCartBean bean) {
                                // 减号和数量动画
                                mReduceImageView.setAnimation(getHiddenAnimation());
                                mGoodsNumberTextView.setAnimation(getHiddenAnimation());
                                // 减号和数量隐藏
                                mReduceImageView.setVisibility(View.GONE);
                                mGoodsNumberTextView.setVisibility(View.GONE);
                                // 刷新购物车列表
                                EventBus.getDefault().postSticky(new FoodsShopCartEvent());
                                // 刷新数据
                                EventBus.getDefault().postSticky(new FoodsSaleListEvent(itemBean.getId(), "0"));
                            }
                        });
                    }else {
                        mCount -= 1;
                        // 编辑购物车
                        int finalMCount = mCount;
                        HYNetRequestUtil.foodsEditCart(activity, shopCartId, mCount, new HYNetRequestUtil.OnRequestSuccessListener() {
                            @Override
                            public void RequestSuccess(FoodsAddCartBean bean) {
                                // 刷新购物车列表
                                EventBus.getDefault().postSticky(new FoodsShopCartEvent());
                                // 刷新数据
                                EventBus.getDefault().postSticky(new FoodsSaleListEvent(itemBean.getId(), finalMCount+""));
                            }
                        });
                    }

                }

            }
        });

    }

    /**
     * 遍历获取购物车id
     *
     * @param id
     * @return
     */
    private String getShopCartId(String id) {
        String shopCartId = "";
        if (activity.mDialogGoodsListAdapter != null && activity.mDialogGoodsListAdapter.getList().size() > 0) {
            List<FoodsCartListBean.DataBean> list = activity.mDialogGoodsListAdapter.getList();
            for (int i = 0; i < list.size(); i++) {
                List<FoodsCartListBean.DataBean.ListBean> list1 = list.get(i).getList();
                for (int j = 0; j < list1.size(); j++) {
                    String pid = list1.get(j).getPid();
                    if (pid.equals(id)) {
                        shopCartId = list1.get(j).getId();
                    }
                }
            }
        }

        return shopCartId;
    }


    /**
     * 显示减号的动画
     *
     * @return
     */
    private Animation getShowAnimation() {
        AnimationSet set = new AnimationSet(true);
        RotateAnimation rotate = new RotateAnimation(0, 720, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        set.addAnimation(rotate);
        TranslateAnimation translate = new TranslateAnimation(
                TranslateAnimation.RELATIVE_TO_SELF, 2f
                , TranslateAnimation.RELATIVE_TO_SELF, 0
                , TranslateAnimation.RELATIVE_TO_SELF, 0
                , TranslateAnimation.RELATIVE_TO_SELF, 0);
        set.addAnimation(translate);
        AlphaAnimation alpha = new AlphaAnimation(0, 1);
        set.addAnimation(alpha);
        set.setDuration(500);
        return set;
    }


    /**
     * 隐藏减号的动画
     *
     * @return
     */
    private Animation getHiddenAnimation() {
        AnimationSet set = new AnimationSet(true);
        RotateAnimation rotate = new RotateAnimation(0, 720, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        set.addAnimation(rotate);
        TranslateAnimation translate = new TranslateAnimation(
                TranslateAnimation.RELATIVE_TO_SELF, 0
                , TranslateAnimation.RELATIVE_TO_SELF, 4f
                , TranslateAnimation.RELATIVE_TO_SELF, 0
                , TranslateAnimation.RELATIVE_TO_SELF, 0);
        set.addAnimation(translate);
        AlphaAnimation alpha = new AlphaAnimation(1, 0);
        set.addAnimation(alpha);
        set.setDuration(500);
        return set;
    }


}
