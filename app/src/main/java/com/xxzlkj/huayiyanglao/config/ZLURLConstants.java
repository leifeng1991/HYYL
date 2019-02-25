package com.xxzlkj.huayiyanglao.config;


import com.xxzlkj.huayiyanglao.BuildConfig;

/**
 * @author zhangrq
 */
public class ZLURLConstants {

    private static String BASE_URL_INDEX;// 商城接口地址
    private static String BASE_URL_WEB;// web地址
    private static String BASE_URL_YL;// 养老
    private static String BASE_URL_JZ;// 家政

    @SuppressWarnings({"FieldCanBeLocal", "ConstantConditions"})
    private static int LX_NET_MODEL = BuildConfig.isTest ? 1 : 2;//当前访问的网络模式

    static {
        switch (LX_NET_MODEL) {
            case 1:
                //测试地址
                String BASE_URL = "http://huayiyanglaotest.zhaolin365.com";
                BASE_URL_INDEX = BASE_URL + "/Api/Index2/";// 共享社区接口地址
                BASE_URL_WEB = BASE_URL + "/Api/Web/";// web地址
                BASE_URL_YL = BASE_URL + "/yanglao/Index/";// 养老
                BASE_URL_JZ = BASE_URL + "/Api/jiazheng/";// 家政
                break;
            case 2:
                //正式地址
                BASE_URL = "https://hyyl.zhaolin365.com";// 商城基地址
                BASE_URL_INDEX = BASE_URL + "/Api/Index2/";// 商城接口地址
                BASE_URL_WEB = BASE_URL + "/Api/Web/";// web地址
                BASE_URL_YL = BASE_URL + "/yanglao/Index/";// 养老
                BASE_URL_JZ = BASE_URL + "/Api/jiazheng/";// 家政
                break;
        }
    }

    /**
     * app轮询
     */
    public static final String KEEP_SEND_URL = BASE_URL_INDEX + "keep_send";

    /**
     * app启动
     */
    public static final String REQUEST_LOGIN_UP = BASE_URL_INDEX + "login_up";
    /**
     * 登录/注册
     */
    public static final String REGISTER_LOGIN_URL = BASE_URL_INDEX + "register_phone";

    /**
     * 发送验证码地址
     */
    public static final String GET_VER_CODE_URL = BASE_URL_INDEX + "yzm";
    /**
     * 发送语音验证码
     */
    public static final String GET_VOICE_VER_CODE_URL = BASE_URL_INDEX + "voice_yzm";
    /**
     * 天气
     */
    public static final String WEATHER_URL = BASE_URL_INDEX + "weather";
    /**
     * 个人资料
     */
    public static final String REQUEST_USER_INFO = BASE_URL_INDEX + "user_info";
    /**
     * 首页
     */
    public static final String REQUEST_INDEX_URL = BASE_URL_INDEX + "index";
    /**
     * 定位附近餐厅
     */
    public static final String REQUEST_FOODS_SHOP_LIST_URL = BASE_URL_INDEX + "foodsShopList";
    /**
     * 获取当前时间
     */
    public static final String GET_TIME_URL = BASE_URL_INDEX + "get_time";
    /**
     * 美食菜单
     */
    public static final String FOODS_SALE_LIST_URL = BASE_URL_INDEX + "foodsSaleList";
    /**
     * 添加到购物车
     */
    public static final String FOODS_ADD_CART_URL = BASE_URL_INDEX + "foodsAddCart";
    /**
     * 删除购物车
     */
    public static final String FOODS_DEL_CART_URL = BASE_URL_INDEX + "foodsDelCart";
    /**
     * 清空购物车
     */
    public static final String FOODS_CLEAR_CART_URL = BASE_URL_INDEX + "foodsClearCart";
    /**
     * 购物车列表
     */
    public static final String FOODS_CART_LIST_URL = BASE_URL_INDEX + "foodsCartList";
    /**
     * 修改数量
     */
    public static final String FOODS_EDIT_CART_URL = BASE_URL_INDEX + "foodsEditCart";
    /**
     * 检测地址距离
     */
    public static final String FOODS_SHOP_DISTANCE_URL = BASE_URL_INDEX + "foodsShopDistance";
    /**
     * 立即下单
     */
    public static final String FOODS_ADD_ORDER_URL = BASE_URL_INDEX + "foodsAddOrder";
    /**
     * 支付前检测
     */
    public static final String FOODS_CHECK_PAY_URL = BASE_URL_INDEX + "foodsCheckPay";
    /**
     * 模拟支付
     */
    public static final String SIMULATION_PAY_URL = BASE_URL_INDEX + "simulationPay";
    /**
     * 我的订单列表
     */
    public static final String FOODS_ORDER_LIST_URL = BASE_URL_INDEX + "foodsOrderList";
    /**
     * 订单详情
     */
    public static final String FOODS_ORDER_INFO_URL = BASE_URL_INDEX + "foodsOrderInfo";
    /**
     * 餐厅取消订单原因
     */
    public static final String FOODS_CANCEL_ORDER_GROUP_URL = BASE_URL_INDEX + "foodsCancelOrderGroup";
    /**
     * 餐厅取消订单（订单状态 state等于1时可显示操作）
     */
    public static final String FOODS_CANCEL_ORDER_URL = BASE_URL_INDEX + "foodsCancelOrder";
    /**
     * 餐厅删除订单（订单状态 state不等于 1， 2或者3时 显示操作）
     */
    public static final String FOODS_DEL_ORDERR_URL = BASE_URL_INDEX + "FoodsDelOrder";
    /**
     * 确认完成订单（订单状态 state等于3时 显示操作）
     */
    public static final String FOODS_FINISH_ORDER_URL = BASE_URL_INDEX + "foodsFinishOrder";
    /**
     * 餐厅申请退款分类
     */
    public static final String FOODS_REFUND_GROUP_URL = BASE_URL_INDEX + "foodsRefundGroup";
    /**
     * 餐厅申请退款
     */
    public static final String FOODS_ADD_REFUND_URL = BASE_URL_INDEX + "foodsAddRefund";
    /**
     * 餐厅退款详情
     */
    public static final String FOODS_REFUND_INFO_URL = BASE_URL_INDEX + "foodsRefundInfo";
    /**
     * 餐厅取消申请退款
     */
    public static final String CANCEL_FOODS_REFUND_URL = BASE_URL_INDEX + "cancelFoodsRefund";
    /**
     * 修改申请退款
     */
    public static final String EDIT_FOODS_REFUND_URL = BASE_URL_INDEX + "editFoodsRefund";
    /**
     * 投诉类型提交
     */
    public static final String SUB_FOODS_COMPLAIN_URL = BASE_URL_INDEX + "subFoodsComplain";
    /**
     * 投诉类型
     */
    public static final String FOODS_COMPLAIN_URL = BASE_URL_INDEX + "foodsComplain";
    /**
     * 催单
     */
    public static final String FOODS_URGE_ORDER_URL = BASE_URL_INDEX + "foodsUrgeOrder";
    /**
     * 订单数量
     */
    public static final String ORDER_NUM_URL = BASE_URL_INDEX + "orderNum";
    /**
     * 发现活动列表
     */
    public static final String ACTIVITY_LIST_URL = BASE_URL_INDEX + "activity_list";
    /**
     * 用户参与活动列表
     */
    public static final String MY_ACTIVITY_LIST_URL = BASE_URL_INDEX + "my_activity_list";
    /**
     * 网络客服、电话客服、亲人电话
     */
    public static final String CALL_INFO_URL = BASE_URL_INDEX + "callInfo";

    //--------------------------------家政-----------------------------------
    /**
     * 医养医疗产品列表
     */
    public static final String HEALTH_GOODS_URL = BASE_URL_JZ + "healthGoods";
    /**
     * 获取二级分类列表
     */
    public static final String HEALTH_TWO_GROUP_URL = BASE_URL_JZ + "healthTwoGroup";
    /**
     * 医养医疗产品详情
     */
    public static final String HEALTH_GOODS_DETAILS_URL = BASE_URL_JZ + "healthGoodsDetails";
    public static final String HEALTH_GOODS_DETAILS1_URL = BASE_URL_JZ + "healthGoodsDetails1";
    /**
     * 下单
     */
    public static final String HEALTH_ORDER_ADD_URL = BASE_URL_JZ + "healthOrderAdd";
    /**
     * 发现活动详情
     */
    public static final String HEALTH_ACTIVITY_INFO_URL = BASE_URL_JZ + "healthActivityInfo";
    /**
     * 活动报名/取消报名
     */
    public static final String USER_HEALTH_ACTIVITY_SIGN_URL = BASE_URL_JZ + "userHealthActivitySign";
    /**
     * 我的订单列表
     */
    public static final String HEALTH_ORDER_LIST_URL = BASE_URL_JZ + "healthOrderList";
    /**
     * 订单详情
     */
    public static final String HEALTH_ORDER_INFO_URL = BASE_URL_JZ + "healthOrderInfo";
    /**
     * 下单前检测
     */
    public static final String HEALTH_ADD_ORDER_CHECK_URL = BASE_URL_JZ + "healthAddOrderCheck";
    /**
     * 取消订单（订单状态 state等于1时可显示操作）
     */
    public static final String HEALTH_CANCEL_ORDER_URL = BASE_URL_JZ + "healthCancelOrder";
    /**
     * 删除订单（订单状态 state不等于 1， 2或者3时 显示操作）
     */
    public static final String HEALTH_DEL_ORDER_URL = BASE_URL_JZ + "healthDelOrder";
    /**
     * 医养医疗申请退款所退金额
     */
    public static final String HEALTH_ORDER_REFUND_PRICE_URL = BASE_URL_JZ + "healthOrderRefundPrice";
    /**
     * 申请退款
     */
    public static final String HEALTH_ADD_ORDER_REFUND_URL = BASE_URL_JZ + "healthAddOrderRefund";
    // --------------------------养老开始-------------------------
    /**
     * 首页体检数据
     */
    public static final String YL_GET_TIJIAN_DATA_URL = BASE_URL_YL + "tijian_index";
    /**
     * 体检数据
     */
    public static final String TIJIAN_URL = BASE_URL_YL + "tijian";
    /**
     * 认证身份证号
     */
    public static final String USER_CARD_NO_URL = BASE_URL_YL + "userCardno";
    /**
     * 认证身份证号状态
     */
    public static final String USER_CARDNO_STATE_URL = BASE_URL_YL + "userCardnoState";
    /**
     * 体检数据是否查看
     */
    public static final String TIJIAN_IS_LOOK_URL = BASE_URL_YL + "tijian_is_look";
    /**
     * 报警位置信息
     */
    public static final String WATCH_ULTRA_URL = BASE_URL_YL + "watch_ultra";
    /**
     * 手表位置信息
     */
    public static final String WATCH_URL = BASE_URL_YL + "watch";
    /**
     * 绑定手表信息
     */
    public static final String USER_WATCH = BASE_URL_YL + "user_watch";
    /**
     * 绑定手表
     */
    public static final String USER_WATCH_EDIT = BASE_URL_YL + "user_watch_edit";
    /**
     * 查询是否绑定设备
     */
    public static final String CHECK_USER_WATCH_URL = BASE_URL_YL + "checkUserWatch";
    /**
     * 获取体检数据(手表)
     */
    public static final String TIJIAN_KM8020_URL = BASE_URL_YL + "tijian_km8020";
    /**
     * 获取体检数据（大型设备）
     */
    public static final String TIJIAN_KM9020_URL = BASE_URL_YL + "tijian_km9020";
    /**
     * 检测设备号是否可以绑定
     */
    public static final String CHECK_WATCH_EXIST_URL = BASE_URL_YL + "checkWatchExist";
    /**
     * 绑定设备
     */
    public static final String BIND_WATCH_URL = BASE_URL_YL + "bindWatch";
    /**
     * 解绑设备
     */
    public static final String DEL_WATCH_URL = BASE_URL_YL + "delWatch";
    /**
     * 查询是否可以绑定监护设备
     */
    public static final String CHECK_WATCH_GUARDIANSHIP_URL = BASE_URL_YL + "checkWatchGuardianship";
    /**
     * 绑定监护设备
     */
    public static final String BIND_WATCH_GUARDIANSHIP_URL = BASE_URL_YL + "bindWatchGuardianship";
    /**
     * 解绑监护人设备
     */
    public static final String DEL_MY_GUARDIANSHIP_URL = BASE_URL_YL + "delMyGuardianship";
    /**
     * 申请监护列表
     */
    public static final String WATCH_GUARDIANSHIP_URL = BASE_URL_YL + "watchGuardianship";
    /**
     * 申请监护
     */
    public static final String AUDIT_WATCH_GUARDIANSHIP_URL = BASE_URL_YL + "auditWatchGuardianship";
    /**
     * 申请监护详情
     */
    public static final String WATCH_GUARDIANSHIP_INFO_URL = BASE_URL_YL + "watchGuardianshipInfo";
    /**
     * 监护人列表
     */
    public static final String WATCH_GUARDIANSHIP_LIST_URL = BASE_URL_YL + "watchGuardianshipList";
    /**
     * 添加快速拨号 和紧急通知
     */
    public static final String ADD_WATCH_FAST_URL = BASE_URL_YL + "addWatchFast";
    /**
     * 快速拨号 和紧急通知 列表
     */
    public static final String WATCH_FAST_LIST_URL = BASE_URL_YL + "watchFastList";
    /**
     * 修快速拨号 和紧急通知
     */
    public static final String EDIT_WATCH_FAST_URL = BASE_URL_YL + "editWatchFast";
    /**
     * 删除速拨号 和紧急通知
     */
    public static final String DEL_WATCH_FAST_URL = BASE_URL_YL + "delWatchFast";
    /**
     * 添加监护人
     */
    public static final String ADD_WATCH_GUARDIANSHIP_URL = BASE_URL_YL + "addWatchGuardianship";
    /**
     * 修改监护人备注
     */
    public static final String EDIT_GUARDIANSHIP_URL = BASE_URL_YL + "editGuardianship";
    /**
     * 查询监护人
     */
    public static final String CHECK_ADD_WATCH_GUARDIANSHIP_URL = BASE_URL_YL + "checkAddWatchGuardianship";
    /**
     * 删除监护人
     */
    public static final String DEL_GUARDIANSHIP_URL = BASE_URL_YL + "delGuardianship";
    /**
     * 查询自己有没有监护人
     */
    public static final String CHECK_IS_GUARDIANSHIP_URL = BASE_URL_YL + "checkIsGuardianship";
    /**
     * 手表最后位置
     */
    public static final String WATCH_POSITION_URL = BASE_URL_YL + "watchPosition";
    /**
     * 位置定位（按天）
     */
    public static final String WATCH_CAVEAT_DAY_URL = BASE_URL_YL + "watchCaveatDay";
    /**
     * 手表警告位置列表
     */
    public static final String WATCH_CAVEAT_DAY_LIST_URL = BASE_URL_YL + "watchCaveatDayList";
    /**
     * 删除手表所有位置
     */
    public static final String DEL_WATCH_ULTRA_LIST_URL = BASE_URL_YL + "delWatchUltraList";
}
