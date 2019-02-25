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
 * 外卖退款详情
 *
 * @author zhangrq
 */
public class TakeOutRefundInfoActivity extends MyBaseActivity {


    public static Intent newIntent(Context context, boolean isHomeDelivery, String shopId) {
        Intent intent = new Intent(context, TakeOutRefundInfoActivity.class);
        intent.putExtra(ZLConstants.Strings.IS_HOME_DELIVERY, isHomeDelivery);
        intent.putExtra(ZLConstants.Strings.SHOPID, shopId);
        return intent;
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_take_out);
    }

    @Override
    protected void findViewById() {



    }

    @Override
    protected void setListener() {


    }

    @Override
    protected void processLogic() {
        setTitleLeftBack();

//        getFoodsCartList();

    }

    /**
     * 购物车列表
     */
   /* private void getFoodsCartList() {
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
                    }

                    @Override
                    public void handlerStart() {

                    }

                    @Override
                    public void handlerEnd() {


                    }
                });
    }*/



}
