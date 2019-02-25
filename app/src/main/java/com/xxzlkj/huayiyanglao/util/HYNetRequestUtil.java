package com.xxzlkj.huayiyanglao.util;

import android.app.Activity;

import com.xxzlkj.huayiyanglao.app.ZhaoLinApplication;
import com.xxzlkj.huayiyanglao.config.ZLConstants;
import com.xxzlkj.huayiyanglao.config.ZLURLConstants;
import com.xxzlkj.huayiyanglao.model.FoodsAddCartBean;
import com.xxzlkj.huayiyanglao.model.FoodsBean;
import com.xxzlkj.huayiyanglao.model.UserWatchBean;
import com.xxzlkj.shop.config.URLConstants;
import com.xxzlkj.zhaolinshare.base.base.BaseActivity;
import com.xxzlkj.zhaolinshare.base.model.BaseBean;
import com.xxzlkj.zhaolinshare.base.model.User;
import com.xxzlkj.zhaolinshare.base.net.OnMyActivityRequestListener;
import com.xxzlkj.zhaolinshare.base.net.RequestManager;
import com.xxzlkj.zhaolinshare.base.util.ToastManager;

import java.util.HashMap;
import java.util.Map;

import static com.xxzlkj.huayiyanglao.config.ZLURLConstants.USER_WATCH;
import static com.xxzlkj.huayiyanglao.config.ZLURLConstants.USER_WATCH_EDIT;

/**
 * 描述:
 *
 * @author leifeng
 *         2017/11/28 11:05
 */


public class HYNetRequestUtil {

    /**
     * 外卖-加入购物车
     *
     * @param id 商品id(必传)
     *           //     * @param number 商品数量(必传 最小为1)
     */
    public static void foodsAddCart(Activity activity, String id, OnRequestSuccessListener listener) {
        Map<String, String> map = new HashMap<>();
        // 商品id(必传)
        map.put(ZLConstants.Params.ID, id);
        User loginUserDoLogin = ZhaoLinApplication.getInstance().getLoginUserDoLogin(activity);
        if (loginUserDoLogin == null) return;
        // 会员id(必传)
        map.put(ZLConstants.Params.UID, loginUserDoLogin.getData().getId());
        // 数量 默认为1
//        map.put(ZLConstants.Params.NUM, String.valueOf(number));
        RequestManager.createRequest(ZLURLConstants.FOODS_ADD_CART_URL, map,
                new OnMyActivityRequestListener<FoodsAddCartBean>((BaseActivity) activity) {
                    @Override
                    public void onSuccess(FoodsAddCartBean bean) {
                        if (listener != null)
                            listener.RequestSuccess(bean);
                    }

                    @Override
                    public void handlerStart() {

                    }

                    @Override
                    public void handlerEnd() {

                    }
                });
    }

    /**
     * 外卖-删除购物车
     *
     * @param id 购物车id(必传) 多个已英文逗号隔开
     */
    public static void foodsDelCart(Activity activity, String id, OnRequestSuccessListener listener) {
        Map<String, String> map = new HashMap<>();
        // 商品id(必传)
        map.put(ZLConstants.Params.ID, id);
        RequestManager.createRequest(ZLURLConstants.FOODS_DEL_CART_URL, map,
                new OnMyActivityRequestListener<FoodsAddCartBean>((BaseActivity) activity) {
                    @Override
                    public void onSuccess(FoodsAddCartBean bean) {
                        if (listener != null)
                            listener.RequestSuccess(bean);
                    }

                    @Override
                    public void handlerStart() {

                    }

                    @Override
                    public void handlerEnd() {

                    }
                });
    }

    /**
     * 外卖-清空购物车
     *
     * @param shopid
     */
    public static void foodsClearCart(Activity activity, String shopid, OnSuccessListener listener) {
        Map<String, String> map = new HashMap<>();
        User loginUserDoLogin = ZhaoLinApplication.getInstance().getLoginUserDoLogin(activity);
        if (loginUserDoLogin == null) return;
        // 会员id(必传)
        map.put(ZLConstants.Params.UID, loginUserDoLogin.getData().getId());
        // 店铺id（必传）
        map.put(ZLConstants.Params.SHOPID, shopid);
        RequestManager.createRequest(ZLURLConstants.FOODS_CLEAR_CART_URL, map,
                new OnMyActivityRequestListener<BaseBean>((BaseActivity) activity) {
                    @Override
                    public void onSuccess(BaseBean bean) {
                        if (listener != null)
                            listener.RequestSuccess(bean);
                    }

                    @Override
                    public void handlerStart() {

                    }

                    @Override
                    public void handlerEnd() {

                    }
                });
    }

    /**
     * 外卖-修改数量
     *
     * @param id  购物车id(必传)
     * @param num 修改后的数量（必传）
     */
    public static void foodsEditCart(Activity activity, String id, int num, OnRequestSuccessListener listener) {
        Map<String, String> map = new HashMap<>();
        // 购物车id(必传)
        map.put(ZLConstants.Params.ID, id);
        // 修改后的数量（必传）
        map.put(ZLConstants.Params.NUM, num + "");
        RequestManager.createRequest(ZLURLConstants.FOODS_EDIT_CART_URL, map,
                new OnMyActivityRequestListener<FoodsAddCartBean>((BaseActivity) activity) {
                    @Override
                    public void onSuccess(FoodsAddCartBean bean) {
                        if (listener != null)
                            listener.RequestSuccess(bean);
                    }

                    @Override
                    public void handlerStart() {

                    }

                    @Override
                    public void handlerEnd() {

                    }
                });
    }

    /**
     * 取消订单接口
     *
     * @param orderId 订单id(必传)
     * @param content 取消原因(必传)
     */
    public static void submitCancelOrder(final BaseActivity activity, String orderId, String content, final OnSuccessListener onSuccessListener) {
        HashMap<String, String> stringStringHashMap = new HashMap<>();
        // 订单id(必传)
        stringStringHashMap.put(ZLConstants.Params.ID, orderId);
        // 取消原因(必传)
        stringStringHashMap.put(ZLConstants.Params.CONTENT, content);
        RequestManager.createRequest(ZLURLConstants.FOODS_CANCEL_ORDER_URL, stringStringHashMap, new OnMyActivityRequestListener<BaseBean>(activity) {

            @Override
            public void onSuccess(BaseBean bean) {
                if (onSuccessListener != null) {
                    onSuccessListener.RequestSuccess(bean);
                }
            }
        });
    }

    /**
     * 餐厅删除订单（订单状态 state不等于 1， 2或者3时 显示操作）
     *
     * @param orderId 订单id(必传)
     */
    public static void foodsDelOrder(final BaseActivity activity, String orderId, final OnSuccessListener onSuccessListener) {
        HashMap<String, String> stringStringHashMap = new HashMap<>();
        // 订单id(必传)
        stringStringHashMap.put(ZLConstants.Params.ID, orderId);
        RequestManager.createRequest(ZLURLConstants.FOODS_DEL_ORDERR_URL, stringStringHashMap, new OnMyActivityRequestListener<BaseBean>(activity) {

            @Override
            public void onSuccess(BaseBean bean) {
                if (onSuccessListener != null) {
                    onSuccessListener.RequestSuccess(bean);
                }
            }
        });
    }

    /**
     * 确认完成订单（订单状态 state等于3时 显示操作）
     *
     * @param orderId 订单id(必传)
     */
    public static void foodsFinishOrder(final BaseActivity activity, String orderId, final OnSuccessListener onSuccessListener) {
        HashMap<String, String> stringStringHashMap = new HashMap<>();
        // 订单id(必传)
        stringStringHashMap.put(ZLConstants.Params.ID, orderId);
        RequestManager.createRequest(ZLURLConstants.FOODS_FINISH_ORDER_URL, stringStringHashMap, new OnMyActivityRequestListener<BaseBean>(activity) {

            @Override
            public void onSuccess(BaseBean bean) {
                if (onSuccessListener != null) {
                    onSuccessListener.RequestSuccess(bean);
                }
            }
        });
    }

    /**
     * 催单
     *
     * @param orderId 订单id(必传)
     */
    public static void foodsUrgeOrder(final BaseActivity activity, String orderId, final OnSuccessListener onSuccessListener) {
        HashMap<String, String> stringStringHashMap = new HashMap<>();
        // 订单id(必传)
        stringStringHashMap.put(ZLConstants.Params.ORDERID, orderId);
        User loginUserDoLogin = ZhaoLinApplication.getInstance().getLoginUserDoLogin(activity);
        if (loginUserDoLogin == null) return;
        // 会员id(必传)
        stringStringHashMap.put(ZLConstants.Params.UID, loginUserDoLogin.getData().getId());
        RequestManager.createRequest(ZLURLConstants.FOODS_URGE_ORDER_URL, stringStringHashMap, new OnMyActivityRequestListener<BaseBean>(activity) {

            @Override
            public void onSuccess(BaseBean bean) {
                if (onSuccessListener != null) {
                    onSuccessListener.RequestSuccess(bean);
                }
            }
        });
    }

    /**
     * 获取绑定手表信息
     */
    public static void getUserWatch(Activity activity, OnLoadSuccessListener<UserWatchBean> loadSuccessListener) {
        Map<String, String> map = new HashMap<>();
        User loginUserDoLogin = ZhaoLinApplication.getInstance().getLoginUserDoLogin(activity);
        if (loginUserDoLogin == null) {
            activity.finish();
            return;
        }
        // 用户id
        map.put(ZLConstants.Params.UID, loginUserDoLogin.getData().getId());
        RequestManager.createRequest(USER_WATCH, map, new OnMyActivityRequestListener<UserWatchBean>((BaseActivity) activity) {
            @Override
            public void onSuccess(UserWatchBean bean) {
                if (loadSuccessListener != null)
                    loadSuccessListener.loadSuccess(bean);
            }
        });
    }

    /**
     * 绑定手表
     *
     * @param userWatchData 手表信息参数
     */
    public static void userWatchEdit(Activity activity, UserWatchBean.DataBean userWatchData, final OnMyActivityRequestListener<BaseBean> onSuccessListener) {
        Map<String, String> map = new HashMap<>();
        User loginUserDoLogin = ZhaoLinApplication.getInstance().getLoginUserDoLogin(activity);
        if (loginUserDoLogin == null) {
            activity.finish();
            return;
        }
        // 用户id
        map.put(ZLConstants.Params.UID, loginUserDoLogin.getData().getId());
        // 手表超出范围警告 1 关闭 2开启
        map.put(ZLConstants.Params.FENCE_STATE, userWatchData.getFence_state());
        // 范围中心经度
        map.put(ZLConstants.Params.FENCE_LONGITUDE, userWatchData.getFence_longitude());
        // 范围中心纬度
        map.put(ZLConstants.Params.FENCE_LATITUDE, userWatchData.getFence_latitude());
        // 限制范围（米 半径）
        map.put(ZLConstants.Params.FENCE_M, userWatchData.getFence_m());
        // 紧急联系人
        map.put(ZLConstants.Params.FENCE_SOS, userWatchData.getFence_sos());
        // 手表imei号
        map.put(ZLConstants.Params.IMEI, userWatchData.getImei());
        // 手表手机号
        map.put(ZLConstants.Params.WATCH_PHONE, userWatchData.getWatch_phone());
        // 亲情号码用逗号隔开
        map.put(ZLConstants.Params.AFFECTION_PHONE, userWatchData.getAffection_phone());
        // 手表数据上传开始时间（时:分:秒）
        map.put(ZLConstants.Params.WATCH_STARTTIME, userWatchData.getWatch_starttime());
        // 手表数据上传结束时间（时:分:秒）
        map.put(ZLConstants.Params.WATCH_ENDTIME, userWatchData.getWatch_endtime());
        // 手表位置上传间隔（大于等于5）（分钟）
        map.put(ZLConstants.Params.WATCH_INTERVAL, userWatchData.getWatch_interval());
        RequestManager.createRequest(USER_WATCH_EDIT, map, onSuccessListener);
    }

    public interface OnRequestSuccessListener {
        void RequestSuccess(FoodsAddCartBean bean);
    }

    public interface OnSuccessListener {
        void RequestSuccess(BaseBean bean);
    }

    public interface OnLoadSuccessListener<T extends BaseBean> {
        void loadSuccess(T t);
    }
}
