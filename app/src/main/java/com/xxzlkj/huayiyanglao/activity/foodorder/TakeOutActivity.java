package com.xxzlkj.huayiyanglao.activity.foodorder;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xxzlkj.huayiyanglao.R;
import com.xxzlkj.huayiyanglao.adapter.DialogGoodsListAdapter;
import com.xxzlkj.huayiyanglao.adapter.TakeOutCommonNavigatorAdapter;
import com.xxzlkj.huayiyanglao.app.ZhaoLinApplication;
import com.xxzlkj.huayiyanglao.config.ZLConstants;
import com.xxzlkj.huayiyanglao.config.ZLURLConstants;
import com.xxzlkj.huayiyanglao.event.FoodsSaleListEvent;
import com.xxzlkj.huayiyanglao.event.FoodsShopCartEvent;
import com.xxzlkj.huayiyanglao.fragment.TodayByTomorrowMenuFragment;
import com.xxzlkj.huayiyanglao.model.FoodsCartListBean;
import com.xxzlkj.huayiyanglao.util.HYNetRequestUtil;
import com.xxzlkj.huayiyanglao.weight.ShapeButton;
import com.xxzlkj.zhaolinshare.base.base.BaseAdapter;
import com.xxzlkj.zhaolinshare.base.base.BaseFragmentPagerAdapter;
import com.xxzlkj.zhaolinshare.base.base.MyBaseActivity;
import com.xxzlkj.zhaolinshare.base.model.BaseBean;
import com.xxzlkj.zhaolinshare.base.model.User;
import com.xxzlkj.zhaolinshare.base.net.OnMyActivityRequestListener;
import com.xxzlkj.zhaolinshare.base.net.RequestManager;
import com.xxzlkj.zhaolinshare.base.util.NumberFormatUtils;
import com.xxzlkj.zhaolinshare.base.util.ToastManager;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 外卖
 *
 * @author zhangrq
 */
public class TakeOutActivity extends MyBaseActivity {
    private MagicIndicator mMagicIndicator;
    private ViewPager mViewPager;
    private RelativeLayout mShopCartRelativeLayout;
    private ImageView mShopCartImageView;
    private ShapeButton mShopCartNumberShapeButton;
    private TextView mTotalPriceTextView, mClearTextView;
    private Button mImmediatePaymentButton;
    private LinearLayout mDialogParentLayout, mShadeLinearLayout, mBottomLinearLayout;
    private RecyclerView mRecyclerView;
    public DialogGoodsListAdapter mDialogGoodsListAdapter;
    private List<Fragment> fragments = new ArrayList<>();
    private String mShopId;
    private boolean isHomeDelivery;

    /**
     * @param context
     * @param isHomeDelivery 是否是外卖到家true:外卖到家 false：到店食用（必传）
     * @param shopId         店铺id（必传）
     * @param foodsName      店铺名（必传）
     * @return
     */
    public static Intent newIntent(Context context, boolean isHomeDelivery, String shopId, String foodsName) {
        Intent intent = new Intent(context, TakeOutActivity.class);
        intent.putExtra(ZLConstants.Strings.IS_HOME_DELIVERY, isHomeDelivery);
        intent.putExtra(ZLConstants.Strings.SHOPID, shopId);
        intent.putExtra(ZLConstants.Strings.FOODSNAME, foodsName);
        return intent;
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_take_out);
    }

    @Override
    protected void findViewById() {
        EventBus.getDefault().register(this);
        mMagicIndicator = getView(R.id.id_magic_indicator);// table栏
        mViewPager = getView(R.id.id_view_pager);
        mShopCartRelativeLayout = getView(R.id.id_shop_cart_layout);// 购物车布局
        mShopCartImageView = getView(R.id.id_shop_cart);// 购物车
        mShopCartNumberShapeButton = getView(R.id.id_shop_cart_number);// 购物车数量
        mTotalPriceTextView = getView(R.id.id_total_price);// 实际支付金额
        mImmediatePaymentButton = getView(R.id.id_immediate_payment);// 立即支付
        mShadeLinearLayout = getView(R.id.id_dialog_layout);// 购物车商品列表布局
        mDialogParentLayout = getView(R.id.id_parent);
        mClearTextView = getView(R.id.id_clear);// 清空
        mBottomLinearLayout = getView(R.id.id_bottom_layout);
        mRecyclerView = getView(R.id.id_recycler_view);// 购物车中商品列表
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mDialogGoodsListAdapter = new DialogGoodsListAdapter(mContext, this, R.layout.adapter_dialog_list_item);
        mRecyclerView.setAdapter(mDialogGoodsListAdapter);


    }

    @Override
    protected void setListener() {
        // 立即支付点击事件
        mImmediatePaymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDialogGoodsListAdapter != null && mDialogGoodsListAdapter.getList().size() > 0) {
                    // 跳转到确认订单界面
                    startActivity(TakeOutConfirmOrderActivity.newIntent(mContext, mShopId, isHomeDelivery));
                } else {
                    ToastManager.showMsgToast(mContext, "你还没有加入商品");
                }

            }
        });
        // 购物车
        mBottomLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mShadeLinearLayout.getVisibility() == View.GONE) {
                    startShowAnimal();
                } else {
                    startHideAnimal();
                }

            }
        });
        // 清空购物车
        mClearTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDialogGoodsListAdapter != null && mDialogGoodsListAdapter.getItemCount() > 0)
                    HYNetRequestUtil.foodsClearCart(TakeOutActivity.this, mShopId, new HYNetRequestUtil.OnSuccessListener() {
                        @Override
                        public void RequestSuccess(BaseBean bean) {
                            // 遍历获取商品id和数量并且商品数量和id必须是一一对应
                            String ids = "";
                            String nums = "";
                            for (int i = 0; i < mDialogGoodsListAdapter.getItemCount(); i++) {
                                FoodsCartListBean.DataBean dataBean = mDialogGoodsListAdapter.getList().get(i);
                                List<FoodsCartListBean.DataBean.ListBean> list = dataBean.getList();
                                for (int j = 0; j < list.size(); j++) {
                                    FoodsCartListBean.DataBean.ListBean listBean = list.get(j);
                                    // 商品id
                                    String pid = listBean.getPid();
                                    if (TextUtils.isEmpty(ids)) {
                                        ids = pid;
                                        nums = "0";
                                    } else {
                                        ids = ids + "," + pid;
                                        nums = nums + "," + "0";
                                    }

                                }
                            }
                            startHideAnimal();
                            // 通知更新
                            if (!TextUtils.isEmpty(ids)){
                                EventBus.getDefault().postSticky(new FoodsSaleListEvent(ids, nums));
                                EventBus.getDefault().postSticky(new FoodsShopCartEvent());
                            }
                        }
                    });

            }
        });
        // 购物车商品列表改变监听事件
        mDialogGoodsListAdapter.setOnGoodsListChangeListener(new DialogGoodsListAdapter.OnGoodsListChangeListener() {
            @Override
            public void onChange(boolean isNotify) {
                if (isNotify)
                    startHideAnimal();
            }
        });
        // 遮罩点击事件
        mShadeLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startHideAnimal();
            }
        });
        // 商品列表item点击事件
        mDialogGoodsListAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener<FoodsCartListBean.DataBean>() {
            @Override
            public void onClick(int position, FoodsCartListBean.DataBean item) {

            }
        });

    }

    @Override
    protected void processLogic() {
        setTitleLeftBack();
        isHomeDelivery = getIntent().getBooleanExtra(ZLConstants.Strings.IS_HOME_DELIVERY, true);
        mShopId = getIntent().getStringExtra(ZLConstants.Strings.SHOPID);
        String foodsName = getIntent().getStringExtra(ZLConstants.Strings.FOODSNAME);
        setTitleName(isHomeDelivery ? foodsName + "-外卖" : foodsName + "-到店");

        fragments.add(TodayByTomorrowMenuFragment.newInstance(TakeOutActivity.this, mShopCartNumberShapeButton, mShopId, "1"));
        fragments.add(TodayByTomorrowMenuFragment.newInstance(TakeOutActivity.this, mShopCartNumberShapeButton, mShopId, "0"));
        BaseFragmentPagerAdapter pagerAdapter = new BaseFragmentPagerAdapter(getSupportFragmentManager(), fragments);
        mViewPager.setOffscreenPageLimit(fragments.size());
        mViewPager.setAdapter(pagerAdapter);

        CommonNavigator commonNavigator = new CommonNavigator(mContext);
        commonNavigator.setAdapter(new TakeOutCommonNavigatorAdapter(mViewPager,
                Arrays.asList("今日菜单", "明日菜单")));
        mMagicIndicator.setNavigator(commonNavigator);

        ViewPagerHelper.bind(mMagicIndicator, mViewPager);

        getFoodsCartList();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 购物车商品列表展示
     */
    private void startShowAnimal() {
        if (mDialogGoodsListAdapter != null && mDialogGoodsListAdapter.getItemCount() > 0) {
            mShadeLinearLayout.setVisibility(View.VISIBLE);
            mDialogParentLayout.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.dialog_show));
        } else {
            ToastManager.showMsgToast(mContext, "你还没有加入商品");
        }
    }

    /**
     * 购物车商品列表隐藏
     */
    private void startHideAnimal() {
        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.dialog_hide);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mShadeLinearLayout.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mDialogParentLayout.startAnimation(animation);
    }


    /**
     * 购物车列表
     */
    private void getFoodsCartList() {
        Map<String, String> map = new HashMap<>();
        map.put(ZLConstants.Params.SHOPID, mShopId);
        User loginUser = ZhaoLinApplication.getInstance().getLoginUser();
        if (loginUser == null) {
            finish();
            return;
        }

        // 会员id(会员id)
        map.put(ZLConstants.Params.UID, loginUser.getData().getId());

        RequestManager.createRequest(ZLURLConstants.FOODS_CART_LIST_URL, map,
                new OnMyActivityRequestListener<FoodsCartListBean>(this) {
                    @Override
                    public void onSuccess(FoodsCartListBean bean) {
                        mShopCartNumberShapeButton.setVisibility(View.VISIBLE);
                        double mTotalPrice = 0;
                        int mTotalNumber = 0;
                        mDialogGoodsListAdapter.clearAndAddList(bean.getData());

                        // 遍历获取购物车中商品总数和所有商品总价格
                        for (int i = 0; i < mDialogGoodsListAdapter.getList().size(); i++) {
                            List<FoodsCartListBean.DataBean.ListBean> list = mDialogGoodsListAdapter.getList().get(i).getList();
                            for (int j = 0; j < list.size(); j++) {
                                FoodsCartListBean.DataBean.ListBean listBean = list.get(j);
                                int number = NumberFormatUtils.toInt(listBean.getNum());
                                mTotalPrice += NumberFormatUtils.toDouble(listBean.getPrice()) * number;
                                mTotalNumber += number;
                            }
                        }
                        // 设置数量和总价格
                        mShopCartNumberShapeButton.setText(mTotalNumber + "");
                        mTotalPriceTextView.setText(String.format("￥%s", mTotalPrice));

                    }

                    @Override
                    public void onFailed(boolean isError, String code, String message) {
                        mShopCartNumberShapeButton.setVisibility(View.GONE);
                        // 设置数量和总价格
                        mShopCartNumberShapeButton.setText(0 + "");
                        mTotalPriceTextView.setText(String.format("￥%s", 0.00));
                        mDialogGoodsListAdapter.clear();
                    }

                    @Override
                    public void handlerStart() {

                    }

                    @Override
                    public void handlerEnd() {


                    }
                });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updataFoodsCartList(FoodsShopCartEvent event) {
        getFoodsCartList();
    }

}
