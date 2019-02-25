package com.xxzlkj.shop.utils;

import android.view.View;
import android.widget.TextView;

import com.xxzlkj.shop.config.URLConstants;
import com.xxzlkj.shop.model.ShopCartNumber;
import com.xxzlkj.zhaolinshare.base.app.BaseApplication;
import com.xxzlkj.zhaolinshare.base.base.BaseActivity;
import com.xxzlkj.zhaolinshare.base.model.User;
import com.xxzlkj.zhaolinshare.base.net.OnMyActivityRequestListener;
import com.xxzlkj.zhaolinshare.base.net.RequestManager;

import java.util.HashMap;

/**
 * 描述:
 *
 * @author zhangrq
 *         2017/4/28 9:31
 */
public class ShopCartNumberUtils {
    /**
     * 获取购物车数量
     */
    public static void getShopCartNumber(BaseActivity activity, final TextView mShopCartNumber) {
        if (mShopCartNumber == null) {
            return;
        }
        User loginUser = BaseApplication.getInstance().getLoginUser();
        if (loginUser != null) {
            HashMap<String, String> map = new HashMap<>();
            map.put(URLConstants.REQUEST_PARAM_UID, loginUser.getData().getId());
            RequestManager.createRequest(URLConstants.REQUEST_CART_NUM, map, new OnMyActivityRequestListener<ShopCartNumber>(activity) {
                @Override
                public void onSuccess(ShopCartNumber bean) {

                    if ("0".equals(bean.getData().getNum())) {
                        mShopCartNumber.setVisibility(View.GONE);
                    } else {
                        mShopCartNumber.setVisibility(View.VISIBLE);
                    }
                    mShopCartNumber.setText(bean.getData().getNum());
                }

                @Override
                public void onFailed(boolean isError, String code, String message) {
                    super.onFailed(isError, code, message);
                    mShopCartNumber.setVisibility(View.GONE);
                }

                @Override
                public void handlerStart() {
                }

                @Override
                public void handlerEnd() {

                }
            });
        } else {
            mShopCartNumber.setVisibility(View.GONE);
        }
    }
}
