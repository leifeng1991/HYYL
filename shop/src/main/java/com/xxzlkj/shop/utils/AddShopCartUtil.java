package com.xxzlkj.shop.utils;

import android.app.Activity;
import android.graphics.Point;
import android.view.View;
import android.view.ViewGroup;

import com.xxzlkj.shop.config.URLConstants;
import com.xxzlkj.shop.event.DataChangEvent;
import com.xxzlkj.shop.weight.ShopCartTextView;
import com.xxzlkj.zhaolinshare.base.app.BaseApplication;
import com.xxzlkj.zhaolinshare.base.base.BaseActivity;
import com.xxzlkj.zhaolinshare.base.model.BaseBean;
import com.xxzlkj.zhaolinshare.base.model.User;
import com.xxzlkj.zhaolinshare.base.net.OnMyActivityRequestListener;
import com.xxzlkj.zhaolinshare.base.net.RequestManager;
import com.xxzlkj.zhaolinshare.base.util.ToastManager;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;


/**
 * 加入购物车
 * Created by Administrator on 2017/7/5.
 */

public class AddShopCartUtil {

    /**
     * 加入购物车
     */
    public static void addShopCart(final BaseActivity activity, View view, View shopCartView , String id) {
        if (BaseApplication.getInstance().getLoginUserDoLogin(activity) != null) {
            User loginUserDoLogin = BaseApplication.getInstance().getLoginUserDoLogin(activity);
            Map<String, String> map = new HashMap<>();
            map.put(URLConstants.REQUEST_PARAM_ID, id);
            map.put(URLConstants.REQUEST_PARAM_UID, loginUserDoLogin.getData().getId());
            map.put(URLConstants.REQUEST_PARAM_NUM, "1");
            RequestManager.createRequest(URLConstants.REQUEST_ADDCART, map, new OnMyActivityRequestListener<BaseBean>(activity) {
                @Override
                public void onSuccess(BaseBean bean) {
                    addShopCartAnimal(activity, view, shopCartView);
                    ToastManager.showShortToast(activity.getApplicationContext(), "加入购物车成功");
                    EventBus.getDefault().postSticky(new DataChangEvent(0, true));
                }

                @Override
                public void handlerStart() {
                }

                @Override
                public void handlerEnd() {

                }
            });
        }
    }


    /**
     * 加入购物车动画
     */
    private static void addShopCartAnimal(Activity activity, View addView, View shopCartView) {
        if (BaseApplication.getInstance().getLoginUserDoLogin(activity) != null) {
            ShopCartTextView mShopCartTextView = new ShopCartTextView(activity.getApplicationContext());
            int position[] = new int[2];
            addView.getLocationInWindow(position);
            mShopCartTextView.setStartPosition(new Point(position[0], position[1]));

            ViewGroup rootView = (ViewGroup) activity.getWindow().getDecorView();
            rootView.addView(mShopCartTextView);
            int endPosition[] = new int[2];
            shopCartView.getLocationInWindow(endPosition);
            mShopCartTextView.setEndPosition(new Point(endPosition[0], endPosition[1]));
            // 开始
            mShopCartTextView.startBeizerAnimation();
        }
    }
}
