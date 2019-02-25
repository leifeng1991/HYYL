package com.xxzlkj.shop.config;


import com.xxzlkj.shop.BuildConfig;

/**
 * @author zhangrq
 */
public class URLConstants {

    private static String BASE_URL;// 商城基地址
    public static String BASE_URL_INDEX;// 商城接口地址
    private static String BASE_URL_WEB;// 商城web地址
    private static String BASE_URL_WUYE;// 物业

    private static String BASE_URL_JZ;// 家政
    private static String BASE_URL_YL;// 养老
    private static String BASE_H5_URL;// 商城H5
    private static String BASE_H5_URL_YL;// 养老H5
    @SuppressWarnings({"FieldCanBeLocal", "ConstantConditions"})
    private static int LX_NET_MODEL = BuildConfig.isTest ? 1 : 2;//当前访问的网络模式

    static {
        switch (LX_NET_MODEL) {
            case 1:
                //测试地址
                BASE_URL = "http://huayiyanglaotest.zhaolin365.com";// 商城基地址
                BASE_URL_INDEX = BASE_URL + "/Api/Index2/";// 商城接口地址
                BASE_URL_WEB = BASE_URL + "/api/web/";// 商城web地址

                BASE_URL_WUYE = BASE_URL + "/Api/Wuye/";// 物业

                BASE_URL_JZ = BASE_URL + "/api/jiazheng/";// 家政
                BASE_URL_YL = BASE_URL + "/yanglao/Index/";// 养老
                break;
            case 2:
                //正式地址
                BASE_URL = "http://hyyl.zhaolin365.com";// 商城基地址
                BASE_URL_INDEX = BASE_URL + "/Api/Index2/";// 商城接口地址
                BASE_URL_WEB = BASE_URL + "/api/web/";// 商城web地址

                BASE_URL_WUYE = BASE_URL + "/Api/Wuye/";// 物业

                BASE_URL_JZ = BASE_URL + "/api/jiazheng/";// 家政
                BASE_URL_YL = BASE_URL + "/yanglao/Index/";// 养老
                break;
        }
    }

    /**
     * 登录
     */
    public static final String LOGIN_URL = BASE_URL_INDEX + "login_phone";
    /**
     * 注册
     */
    public static final String REGISTER_URL = BASE_URL_INDEX + "register_phone";
    /**
     * 检查家易的是否注册过
     */
    public static final String CHECK_JY_USER_URL = BASE_URL_INDEX + "checkJyUser";
    /**
     * 屏幕广告
     */
    public static final String SCREEN_URL = BASE_URL_INDEX + "screen";
    /**
     * app启动
     */
    public static final String REQUEST_LOGIN_UP = BASE_URL_INDEX + "login_up";
    /**
     * app退出
     */
    public static final String REQUEST_LOGIN_OUT = BASE_URL_INDEX + "login_out";
    /**
     * 首页弹框
     */
    public static final String REQUEST_HOME_WINDOW = BASE_URL_INDEX + "homeWindow";
    /**
     * 发送验证码地址
     */
    public static final String GET_VER_CODE_URL = BASE_URL_INDEX + "yzm";
    /**
     * 所有应用
     */
    public static final String APP_ALL_URL = BASE_URL_INDEX + "app_all";
    /**
     * 编辑我的应用
     */
    public static final String EDIT_MY_APP_URL = BASE_URL_INDEX + "edit_my_app";
    /**
     * 发送语音验证码
     */
    public static final String GET_VOICE_VER_CODE_URL = BASE_URL_INDEX + "voice_yzm";
    /**
     * 登录/注册
     */
    public static final String EXIT_URL = BASE_URL_INDEX + "login_out";
    /**
     * 获取顶级分类
     */
    public static final String REQUEST_TOP_GROUP = BASE_URL_INDEX + "top_group";
    /**
     * 获取下级分类
     */
    public static final String REQUEST_SUB_GROUP = BASE_URL_INDEX + "sub_group";
    /**
     * 分类右侧页面
     */
    public static final String REQUEST_GROUP_RIGHT = BASE_URL_INDEX + "group_right";
    /**
     * 获取腾讯云图片管理签名
     */
    public static final String REQUEST_TX_IMG_SIGN = BASE_URL_INDEX + "tx_img_sign";
    /**
     * 商品详情
     */
    public static final String REQUEST_GOODS_INFO = BASE_URL_INDEX + "goods_info";
    /**
     * 商品列表
     */
    public static final String REQUEST_GOODS_LIST = BASE_URL_INDEX + "goods_list";
    /**
     * 获取热搜关键字
     */
    public static final String REQUEST_HOT_WORD = BASE_URL_INDEX + "hot_word";
    /**
     * 购物车列表
     */
    public static final String REQUEST_CART_LIST = BASE_URL_INDEX + "cart_list";
    /**
     * 为您推荐商品
     */
    public static final String REQUEST_RECOMMEND_GOODS = BASE_URL_INDEX + "recommend_goods";
    /**
     * 团购商品热门推荐
     */
    public static final String REQUEST_GROUPON_RECOMMEND = BASE_URL_INDEX + "GrouponRecommend";
    /**
     * 添加到购物车
     */
    public static final String REQUEST_ADDCART = BASE_URL_INDEX + "addcart";
    /**
     * 删除购物车
     */
    public static final String REQUEST_DELCART = BASE_URL_INDEX + "delcart";
    /**
     * 清空购物车
     */
    public static final String REQUEST_CLEARCART = BASE_URL_INDEX + "clearcart";
    /**
     * 修改数量
     */
    public static final String REQUEST_EDITCART = BASE_URL_INDEX + "editcart";
    /**
     * 商城首页上
     */
    public static final String REQUEST_SHOP_INDEX = BASE_URL_INDEX + "shop_index";
    /**
     * 商城首页下
     */
    public static final String REQUEST_SHOP_INDEX_LIST = BASE_URL_INDEX + "shop_index_list";
    /**
     * 时速达首页下
     */
    public static final String SHOP_INDEX_DOWN_LIST_URL = BASE_URL_INDEX + "shopIndexDownList";
    /**
     * 火爆预售
     */
    public static final String REQUEST_ADVANCE_LIST = BASE_URL_INDEX + "advance_list";
    /**
     * 兆邻团购
     */
    public static final String REQUEST_GROUPON_LIST = BASE_URL_INDEX + "groupon_list";
    /**
     * 果蔬生鲜
     */
    public static final String REQUEST_MARKET_LIST = BASE_URL_INDEX + "market_list";
    /**
     * 果蔬生鲜更多商品
     */
    public static final String REQUEST_MARKET_GOODS_LIST = BASE_URL_INDEX + "market_goods_list";
    /**
     * 获取当前时间
     */
    public static final String REQUEST_GET_TIME = BASE_URL_INDEX + "get_time";
    /**
     * 购物车数量
     */
    public static final String REQUEST_CART_NUM = BASE_URL_INDEX + "cart_num";
    /**
     * 我的地址
     */
    public static final String REQUEST_MY_ADDRESS = BASE_URL_INDEX + "my_address";
    /**
     * 添加地址
     */
    public static final String REQUEST_ADDRESS = BASE_URL_INDEX + "address";
    /**
     * 删除地址
     */
    public static final String REQUEST_DEL_ADDRESS = BASE_URL_INDEX + "del_address";
    /**
     * 编辑地址
     */
    public static final String REQUEST_EDIT_ADDRESS = BASE_URL_INDEX + "edit_address";
    /**
     * 地址详情
     */
    public static final String REQUEST_ADDRESS_INFO = BASE_URL_INDEX + "address_info";
    /**
     * 地址设为默认
     */
    public static final String REQUEST_DEF_ADDRESS = BASE_URL_INDEX + "def_address";
    /**
     * 我的默认地址
     */
    public static final String REQUEST_MY_FIRST_ADDRESS = BASE_URL_INDEX + "my_first_address";
    /**
     * 单个收藏/取消收藏
     */
    public static final String REQUEST_CELL = BASE_URL_INDEX + "cell";
    /**
     * 商品收藏列表
     */
    public static final String REQUEST_CELL_GOODS = BASE_URL_INDEX + "cell_goods";
    /**
     * 批量删除收藏
     */
    public static final String REQUEST_CELL_DEL = BASE_URL_INDEX + "cell_del";
    /**
     * 检测地址距离
     */
    public static final String REQUEST_CHECK_DISTANCE = BASE_URL_INDEX + "check_distance";
    /**
     * 支付前检测
     */
    public static final String REQUEST_CHECK_PAY = BASE_URL_INDEX + "check_pay";
    /**
     * 立即下单
     */
    public static final String REQUEST_ADD_ORDER = BASE_URL_INDEX + "add_order";
    /**
     * 支付宝回调
     */
    public static final String REQUEST_ALIPAY_NOTIFY_URL = BASE_URL_INDEX + "alipay_notify_url";
    /**
     * 获取支付宝签名
     */
    public static final String REQUEST_AOP_SIGN = BASE_URL_INDEX + "aop_sign";
    /**
     * 取消订单
     */
    public static final String CANCEL_ORDER_URL = BASE_URL_INDEX + "cancel_order";
    /**
     * 删除订单
     */
    public static final String DEL_ORDER_URL = BASE_URL_INDEX + "del_order";
    /**
     * 确认收货
     */
    public static final String FINISH_ORDER_URL = BASE_URL_INDEX + "finish_order";
    /**
     * 订单详情
     */
    public static final String ORDER_DETAIL_URL = BASE_URL_INDEX + "order_info";
    /**
     * 成功页面
     */
    public static final String PAY_ORDER_INFO_URL = BASE_URL_INDEX + "pay_order_info";
    /**
     * 订单--退款详情
     */
    public static final String ORDER_REFUND_INFO_URL = BASE_URL_INDEX + "order_refund_info";
    /**
     * 取消申请退款
     */
    public static final String CANCEL_ORDER_REFUND_URL = BASE_URL_INDEX + "cancel_order_refund";
    /**
     * 修改申请退款
     */
    public static final String EDIT_ORDER_REFUND_URL = BASE_URL_INDEX + "edit_order_refund";
    /**
     * 商城订单投诉提交
     */
    public static final String ADD_SHOP_COMPLAIN_URL = BASE_URL_INDEX + "add_shop_complain";
    /**
     * 获取商城订单投诉原因
     */
    public static final String COMPLAIN_SHOP_GROUP_URL = BASE_URL_INDEX + "complain_shop_group";

    /**
     * 取消订单原因列表
     */
    public static final String CANCEL_ORDER_GROUP_URL = BASE_URL_INDEX + "cancel_order_group";

    /**
     * 商城申请退款
     */
    public static final String ADD_ORDER_REFUND_URL = BASE_URL_INDEX + "add_order_refund";
    /**
     * 售后订单列表
     */
    public static final String REQUEST_REFUND_GOODS = BASE_URL_INDEX + "refund_goods";
    /**
     * 删除售后(state等于2和3的时候可以删除) 不用删除了
     */
    public static final String REQUEST_DEL_REFUND_GOODS = BASE_URL_INDEX + "del_refund_goods";
    /**
     * 修改申请售后（state为1时可操作）
     */
    public static final String REQUEST_EDIT_REFUND_GOODS = BASE_URL_INDEX + "edit_refund_goods";
    /**
     * 取消申请售后（state为1或者4时可操作）
     */
    public static final String REQUEST_CANCEL_REFUND_GOODS = BASE_URL_INDEX + "cancel_refund_goods";
    /**
     * 售后订单详情
     */
    public static final String REQUEST_REFUND_GOODS_INFO = BASE_URL_INDEX + "refund_goods_info";
    /**
     * 可申请售后商品订单
     */
    public static final String REQUEST_REFUND_ORDER_GOODS_LIST = BASE_URL_INDEX + "refund_order_goods_list";
    /**
     * 选择支付配送（选择支付配送时检测下内部控件）
     */
    public static final String REQUEST_CHOOSE_DISTRIBUTION = BASE_URL_INDEX + "choose_distribution";
    /**
     * 货到付款
     */
    public static final String REQUEST_COD = BASE_URL_INDEX + "cod";
    /**
     * 线下门店地址列表
     */
    public static final String REQUEST_STORE_LIST = BASE_URL_INDEX + "store_list";
    /**
     * 商城订单申请售后
     */
    public static final String REQUEST_ADD_REFUND = BASE_URL_INDEX + "add_refund";
    /**
     * 申请售后分类
     */
    public static final String REQUEST_REFUND_GROUP = BASE_URL_INDEX + "refund_group";
    /**
     * 申请退款分类
     */
    public static final String REQUEST_ORDER_REFUND_GROUP = BASE_URL_INDEX + "order_refund_group";
    /**
     * 修改购物车商品
     */
    public static final String REQUEST_EDIT_CART_GOODS = BASE_URL_INDEX + "edit_cart_goods";
    /**
     * 获取微信预订单
     */
    public static final String REQUEST_WX_SIGN = BASE_URL_INDEX + "wx_sign";
    /**
     * 关于我们
     */
    public static final String ABOUT_H5_URL = BASE_URL_WEB + "about";
    /**
     * 头条
     */
    public static final String NEWSWEB_URL = BASE_URL_WEB + "newsweb/id/";
    /**
     * 注册协议
     */
    public static final String AGREEMENT_URL = BASE_URL_WEB + "gvrp";
    /**
     * 优惠券简介
     */
    public static final String COUPON_URL = BASE_URL_WEB + "coupon";
    /**
     * 消息详情web
     */
    public static final String CONTENT_TABLE_MESSAGE_URL = BASE_URL_WEB + "content/table/message/id/";
    /**
     * 用户反馈
     */
    public static final String FEEDBACK_URL = BASE_URL_INDEX + "feedback";
    /**
     * 个人资料
     */
    public static final String REQUEST_USER_INFO = BASE_URL_INDEX + "user_info";
    /**
     * 修改资料
     */
    public static final String REQUEST_EDIT_USER = BASE_URL_INDEX + "edit_user";
    /**
     * 修改手机号
     */
    public static final String REQUEST_EDIT_PHONE = BASE_URL_INDEX + "edit_phone";
    /**
     * 确认取货方式
     */
    public static final String REQUEST_QU_MODE = BASE_URL_INDEX + "qu_mode";
    /**
     * 我的中部六模块
     */
    public static final String MY_CENTER_URL = BASE_URL_INDEX + "my_center";
    /**
     * 商品浏览记录
     */
    public static final String REQUEST_TRACE_GOODS = BASE_URL_INDEX + "trace_goods";
    /**
     * 批量删除浏览记录
     */
    public static final String REQUEST_TRACE_DEL = BASE_URL_INDEX + "trace_del";
    /**
     * 获取快递公司
     */
    public static final String EXP_COM_LIST_URL = BASE_URL_INDEX + "exp_com_list";
    /**
     * 查询物流信息
     */
    public static final String EXPRESS_INFO_URL = BASE_URL_INDEX + "express_info";
    /**
     * 修改相册图片
     */
    public static final String EDIT_IMGS_URL = BASE_URL_INDEX + "edit_imgs";

    /**
     * 删除相册图片
     */
    public static final String DEL_USER_IMG_URL = BASE_URL_INDEX + "del_user_img";
    /**
     * 添加相册图片
     */
    public static final String ADD_USER_IMG_URL = BASE_URL_INDEX + "add_user_img";
    /**
     * 保存我的相册(排序改变时 点击返回时调用)
     */
    public static final String EDIT_USER_IMG_URL = BASE_URL_INDEX + "edit_user_img";
    /**
     * 快递幻灯
     */
    public static final String REQUEST_EXPRESS_BANNER = BASE_URL_INDEX + "express_banner";
    /**
     * 发现活动列表
     */
    public static final String REQUEST_ACTIVITY_LIST = BASE_URL_INDEX + "activity_list";
    /**
     * 发现活动列表
     */
    public static final String REQUEST_ACTIVITY_LIST1 = BASE_URL_INDEX + "activity_list1";
    /**
     * 我的活动列表
     */
    public static final String REQUEST_MY_ACTIVITY_LIST = BASE_URL_INDEX + "my_activity_list";
    /**
     * 发现活动详情
     */
    public static final String REQUEST_ACTIVITY_INFO = BASE_URL_INDEX + "activity_info";
    /**
     * 活动报名会员
     */
    public static final String REQUEST_ACTIVITY_USER = BASE_URL_INDEX + "activity_user";
    /**
     * 喜欢活动/取消喜欢活动
     */
    public static final String REQUEST_CELL_ACTIVITY = BASE_URL_INDEX + "cell_activity";
    /**
     * 活动报名/取消报名
     */
    public static final String REQUEST_USER_ACTIVITY_SIGN = BASE_URL_INDEX + "user_activity_sign";
    /**
     * 设置显示/设置隐身
     */
    public static final String REQUEST_SET_USER_HIDE = BASE_URL_INDEX + "set_user_hide";
    /**
     * 首页
     */
    public static final String INDEX_URL = BASE_URL_INDEX + "index";
    /**
     * 首页失败时显示的已开通小区
     */
    public static final String GET_COMMUNITY_LIST_URL = BASE_URL_INDEX + "getCommunityList";
    /**
     * 天气
     */
    public static final String WEATHER_URL = BASE_URL_INDEX + "weather";
    /**
     * app轮询
     */
    public static final String KEEP_SEND_URL = BASE_URL_INDEX + "keep_send";

    /**
     * 订单列表数据
     */
    public static final String MY_CELL_ACTIVITY = BASE_URL_INDEX + "my_cell_activity";
    /**
     * 我的收藏的活动
     */
    public static final String ORDER_URL = BASE_URL_INDEX + "my_order_list";
    /**
     * 个人分享页面
     */
    public static final String REQUEST_USER_SHARE = BASE_URL_INDEX + "user_share";
    /**
     * 个人二维码
     */
    public static final String REQUEST_ERWEIMA = BASE_URL_INDEX + "erweima";
    /**
     * 邀请注册二维码分享成功回调（增加分享次数）
     */
    public static final String REQUEST_ADD_SHARE = BASE_URL_INDEX + "add_share";
    /**
     * 邀请列表
     */
    public static final String REQUEST_USER_SHARE_LIST = BASE_URL_INDEX + "user_share_list";
    /**
     * 消费清单
     */
    public static final String REQUEST_USERS_PAY_INFO = BASE_URL_INDEX + "users_pay_info";
    /**
     * 年消费明细
     */
    public static final String REQUEST_USERS_PAY_YEAR = BASE_URL_INDEX + "users_pay_year";
    /**
     * 检测全场折扣接口
     */
    public static final String REQUEST_IS_ALL_DISCOUNT = BASE_URL_INDEX + "is_all_discount";
    /**
     * 关注/取消
     */
    public static final String REQUEST_ADDUSERFRIEND = BASE_URL_INDEX + "addUserFriend";
    /**
     * 检测是否完善资料
     */
    public static final String CHECK_USER_INFO_URL = BASE_URL_INDEX + "check_user_info";
    /**
     * 兆邻专属服务人员
     */
    public static final String USER_SERVICE_URL = BASE_URL_INDEX + "userService";
    /**
     * 绑定前服务人员信息
     */
    public static final String SERVICE_USER_INFO_URL = BASE_URL_INDEX + "serviceUserInfo";
    /**
     * 绑定服务人员
     */
    public static final String BINDING_USER_URL = BASE_URL_INDEX + "bindingUser";
    /**
     * 获取当前时间
     */
    public static final String GET_TIME_URL = BASE_URL_INDEX + "get_time";
    /**
     * 我的优惠券
     */
    public static final String USER_COUPON_URL = BASE_URL_INDEX + "user_coupon";
    /**
     * 我的优惠券总数量
     */
    public static final String COUPON_NUM_URL = BASE_URL_INDEX + "coupon_num";
    /**
     * 下单使用优惠券
     */
    public static final String USABLE_COUPON_URL = BASE_URL_INDEX + "usableCoupon";
    /**
     * 消息中心
     */
    public static final String MESSAGE_URL = BASE_URL_INDEX + "message1";
    /**
     * 消息类型
     */
    public static final String MESSAGE_TYPE_URL = BASE_URL_INDEX + "messagetype";
    /**
     * 消息详情
     */
    public static final String MESSAGE_DETAIL_URL = BASE_URL_INDEX + "message_detail";
    /**
     * 减到0元时支付
     */
    public static final String ZEROPAY_URL = BASE_URL_INDEX + "zeroPay";
    /**
     * 商城首页标题头列表
     */
    public static final String SHOP_NAV_URL = BASE_URL_INDEX + "shopNav";
    /**
     * 商城首页列表
     */
    public static final String SHOP_INDEX_URL = BASE_URL_INDEX + "shopIndex";
    /**
     * 时速达列表
     */
    public static final String SHOP_INDEX_STORE_URL = BASE_URL_INDEX + "shopIndexStore";
    /**
     * 预售首页
     */
    public static final String ADVANCE_INDEX_URL = BASE_URL_INDEX + "advanceIndex";
    /**
     * 团购首页
     */
    public static final String GROUPON_INDEX_URL = BASE_URL_INDEX + "grouponIndex";
    /**
     * 商城列表页分类
     */
    public static final String SHOP_APP_URL = BASE_URL_INDEX + "shopApp";
    /**
     * 时速达商城列表页分类
     */
    public static final String SHOP_APP_STORE_URL = BASE_URL_INDEX + "shopAppStore";
    /**
     * 预售列表页分类
     */
    public static final String ADVANCE_GROUP_URL = BASE_URL_INDEX + "advanceGroup";
    /**
     * 确认订单
     */
    public static final String CONFIRM_ORDER_URL = BASE_URL_INDEX + "confirmOrder";
    /**
     * 到货通知
     */
    public static final String ARRIVAL_NOTICE_URL = BASE_URL_INDEX + "arrivalNotice";
    /**
     * 品牌活动详情
     */
    public static final String BRAND_DETAILS_URL = BASE_URL_INDEX + "brandDetails";
    /**
     * 时速达获取顶级分类
     */
    public static final String SHOP_TOP_GROUP_URL = BASE_URL_INDEX + "shopTopGroup";
    /**
     * 时速达分类右侧页面
     */
    public static final String SHOP_GROUP_RIGHT_URL = BASE_URL_INDEX + "shopGroupRight";
    /**
     * 时速达商品列表
     */
    public static final String SHOP_GOODS_LIST_URL = BASE_URL_INDEX + "shopGoodsList";
    /**
     * 预售商品列表
     */
    public static final String ADVANCE_GOODS_LIST_URL = BASE_URL_INDEX + "AdvanceGoodsList";
    /**
     * 团购商品列表
     */
    public static final String GROUPON_GOODS_LIST_URL = BASE_URL_INDEX + "GrouponGoodsList";
    /**
     * 团购组详情
     */
    public static final String GROUPON_TEAM_INFO_URL = BASE_URL_INDEX + "GrouponTeamInfo";
    /**
     * 一键参团检测
     */
    public static final String GROUPON_ITEM_CHECK_URL = BASE_URL_INDEX + "GrouponItemCheck";
    /**
     * 时速达果蔬生鲜更多商品（门店）
     */
    public static final String MARKET_GOODS_LIST_STORE_URL = BASE_URL_INDEX + "marketGoodsListStore";
    /**
     * 时速达品牌活动详情（门店）
     */
    public static final String BRAND_DETAILS_STORE_URL = BASE_URL_INDEX + "brandDetailsStore";
    /**
     * 主题活动详情
     */
    public static final String ADVANCE_ACT_DETAIL_URL = BASE_URL_INDEX + "advanceActDetail";
    /**
     * 支付后检测
     */
    public static final String CHECK_PAY_REAR_URL = BASE_URL_INDEX + "checkPayRear";
    /**
     * 商品下架
     */
    public static final String GOOD_DOWN_URL = BASE_URL_INDEX + "goodsDown";
    /**
     * 预售分类右侧 主分类id等于-1时调用
     */
    public static final String ADVANCE_GROUP_RIGHT_URL = BASE_URL_INDEX + "AdvanceGroupRight";
    /**
     * 团购分类右侧 主分类id等于-2时调用
     */
    public static final String GROUPON_GROUP_RIGHT_URL = BASE_URL_INDEX + "GrouponGroupRight";
    /**
     * 团购二级
     */
    public static final String GROUPON_GROUP_URL = BASE_URL_INDEX + "grouponGroup";
    /**
     * 发起团购组
     */
    public static final String ADD_GROUPON_ITEM_URL = BASE_URL_INDEX + "addGrouponItem";
    /**
     * 团购组列表
     */
    public static final String GROUPON_TEAM_LIST_URL = BASE_URL_INDEX + "GrouponTeamList";


    /**----------------------家政--------------------------*/
    /**
     * 月嫂详情
     */
    public static final String JZ_CLEANING_YUESAO_DETAILS_URL = BASE_URL_JZ + "cleaningYuesaoDetails";
    /**
     * 月嫂列表
     */
    public static final String JZ_CLEANING_YUESAO_URL = BASE_URL_JZ + "cleaningYuesao";
    /**
     * 保姆列表
     */
    public static final String JZ_CLEANING_BAOMU_URL = BASE_URL_JZ + "cleaningBaomu";
    /**
     * 保姆详情
     */
    public static final String JZ_CLEANING_BAOMU_DETAILS_URL = BASE_URL_JZ + "cleaningBaomuDetails";
    /**
     * 保姆籍贯
     */
    public static final String JZ_CLEANING_BAOMU_PLACE_URL = BASE_URL_JZ + "cleaningPlaceBaomu";
    /**
     * 月嫂籍贯
     */
    public static final String JZ_CLEANING_YUESAO_PLACE_URL = BASE_URL_JZ + "cleaningPlaceYuesao";
    /**
     * 月嫂薪水
     */
    public static final String JZ_CLEANING_YUESAO_TREATMENT_URL = BASE_URL_JZ + "cleaningTreatmentYuesao";
    /**
     * 保姆薪水
     */
    public static final String JZ_CLEANING_BAOMU_TREATMENT_URL = BASE_URL_JZ + "cleaningTreatmentBaomu";
    /**
     * 保姆筛选
     */
    public static final String JZ_CLEANING_BAOMU_FILTER_URL = BASE_URL_JZ + "cleaningBaomuFilter";
    /**
     * 月嫂筛选
     */
    public static final String JZ_CLEANING_YUESAO_FILTER_URL = BASE_URL_JZ + "cleaningYuesaoFilter";
    /**
     * 本地生活幻灯片
     */
    public static final String JZ_CLEANING_BANNER_URL = BASE_URL_JZ + "cleaningBanner";
    /**
     * 家政首页
     */
    public static final String JZ_CLEANING_INDEX_URL = BASE_URL_JZ + "cleaningIndex";
    /**
     * 保洁分类
     */
    public static final String JZ_CLEANING_INDEX = BASE_URL_JZ + "cleaning_index";
    /**
     * 保洁产品
     */
    public static final String JZ_CLEANING_GOODS = BASE_URL_JZ + "cleaning_goods";
    /**
     * 产品详情
     */
    public static final String JZ_GOODS_INFO = BASE_URL_JZ + "goods_info";
    /**
     * 保洁师列表
     */
    public static final String JZ_CLEANING_PEOPLE = BASE_URL_JZ + "cleaning_people";
    /**
     * 下单
     */
    public static final String JZ_ORDER_ADD = BASE_URL_JZ + "order_add";
    /**
     * 订单列表数据
     */
    public static final String JZ_ORDER_URL = BASE_URL_JZ + "my_order_list";
    /**
     * 订单详情
     */
    public static final String JZ_ORDER_INFO_URL = BASE_URL_JZ + "order_info";
    /**
     * 订单详情
     */
    public static final String HEALTH_ORDER_INFO_URL = BASE_URL_JZ + "healthOrderInfo";
    /**
     * 面试完成支付剩余金额
     */
    public static final String JZ_AUDITION_YES_URL = BASE_URL_JZ + "auditionYes";

    /**
     * 订单--退款详情
     */
    public static final String JZ_ORDER_REFUND_INFO_URL = BASE_URL_JZ + "order_refund_info";

    /**
     * 申请退款分类
     */
    public static final String JZ_REQUEST_ORDER_REFUND_GROUP = BASE_URL_JZ + "order_refund_group";
    /**
     * 取消订单原因列表
     */
    public static final String JZ_CANCEL_ORDER_GROUP_URL = BASE_URL_JZ + "cancel_order_group";
    /**
     * 取消订单
     */
    public static final String JZ_CANCEL_ORDER_URL = BASE_URL_JZ + "cancel_order";
    /**
     * 服务完成
     */
    public static final String JZ_FINISH_ORDER_URL = BASE_URL_JZ + "finish_order";
    /**
     * 删除订单
     */
    public static final String JZ_DEL_ORDER_URL = BASE_URL_JZ + "del_order";
    /**
     * 申请退款提示
     */
    public static final String JZ_CLEANING_TUI_DESC_URL = BASE_URL_JZ + "cleaning_tui_desc";

    /**
     * 取消申请退款
     */
    public static final String JZ_CANCEL_ORDER_REFUND_URL = BASE_URL_JZ + "cancel_order_refund";
    /**
     * 商城申请退款
     */
    public static final String JZ_ADD_ORDER_REFUND_URL = BASE_URL_JZ + "add_order_refund";
    /**
     * 修改申请退款
     */
    public static final String JZ_EDIT_ORDER_REFUND_URL = BASE_URL_JZ + "edit_order_refund";
    /**
     * 售后订单列表
     */
    public static final String JZ_REFUND_GOODS = BASE_URL_JZ + "refund_goods";
    /**
     * 可申请售后商品订单
     */
    public static final String JZ_REFUND_ORDER_GOODS_LIST = BASE_URL_JZ + "refund_order_goods_list";
    /**
     * 申请售后分类
     */
    public static final String JZ_REFUND_GROUP_URL = BASE_URL_JZ + "refund_group";
    /**
     * 本地生活订单申请售后
     */
    public static final String JZ_ADD_REFUND = BASE_URL_JZ + "add_refund";
    /**
     * 修改申请售后（state为1时可操作）
     */
    public static final String JZ_AEDIT_REFUND_GOODS = BASE_URL_JZ + "edit_refund_goods";
    /**
     * 售后详情
     */
    public static final String JZ_REFUND_GOODS_INFO = BASE_URL_JZ + "refund_goods_info";
    /**
     * 取消申请售后（state为1或者4时可操作）
     */
    public static final String JZ_CANCEL_REFUND_GOODS = BASE_URL_JZ + "cancel_refund_goods";
    /**
     * 支付前检测
     */
    public static final String JZ_CHECK_PAY = BASE_URL_JZ + "check_pay";
    /**
     * 支付前检测(医养医疗)
     */
    public static final String HEALTH_CHECK_PAY = BASE_URL_JZ + "healthCheckPay";
    /**
     * 支付宝签名
     */
    public static final String JZ_AOP_SIGN = BASE_URL_JZ + "aop_sign";
    /**
     * 获取微信预订单
     */
    public static final String JZ_WX_SIGN = BASE_URL_JZ + "wx_sign";
    /**
     * 订单投诉提交
     */
    public static final String JZ_ADD_CLEANING_COMPLAIN_URL = BASE_URL_JZ + "add_cleaning_complain";
    /**
     * 获取订单投诉原因
     */
    public static final String JZ_COMPLAIN_CLEANING_GROUP_URL = BASE_URL_JZ + "complain_cleaning_group";
    /**
     * 保洁师详情
     */
    public static final String JZ_CLEANING_PEOPLE_INFO_URL = BASE_URL_JZ + "cleaning_people_info";
    /**
     * 小时工列表
     */
    public static final String JZ_CLEANING_HOUR_URL = BASE_URL_JZ + "cleaningHour";
    /**
     * 小时工详情
     */
    public static final String JZ_cleaning_Hour_Info_URL = BASE_URL_JZ + "cleaningHourInfo";
    /**
     * 下单前检测
     */
    public static final String JZ_ADD_ORDER_CHECK_URL = BASE_URL_JZ + "addOrderCheck";
    /**
     * 申请退款所退金额
     */
    public static final String JZ_ORDER_REFUND_PRICE_URL = BASE_URL_JZ + "orderRefundPrice";
    /**
     * 单个收藏/取消收藏
     */
    public static final String REQUEST_CELL_JZ = BASE_URL_JZ + "cell";
    /**
     * 服务收藏列表
     */
    public static final String REQUEST_CELL_LIST = BASE_URL_JZ + "cellList";
    /**
     * -------------------------养老--------------------------------------
     *
     */

    /**
     * 体检数据
     */
    public static final String YL_BASE_BASE_URL = BASE_URL_YL + "base";
    /**
     * 体检数据
     */
    public static final String YL_TIJIAN_URL = BASE_URL_YL + "tijian";
    /**
     * 首页体检数据
     */
    public static final String YL_GET_TIJIAN_DATA_URL = BASE_URL_YL + "tijian_index";
    /**
     * 当天步数更新
     */
    public static final String YL_GO_ADD_STEP_URL = BASE_URL_YL + "go_add";
    /**
     * 我的步数
     */
    public static final String YL_GO_MY_URL = BASE_URL_YL + "go_my";
    /**
     * 小屋的h5
     */
    public static final String YL_XIAOWU_H5_URL = BASE_URL_YL + "xiaowu";
    /**
     * 步行排行榜H5地址
     * http://yanglao.zhaolin365.com/view/html/walk.html
     */
    public static final String YL_WALKING_LIST_H5_URL = BASE_H5_URL_YL + "walk.html";

    /**
     * -------------------------物业--------------------------------------
     *
     */

    /**
     * 物业首页
     */
    public static final String WUYE_HOME_URL = BASE_URL_WUYE + "home";
    /**
     * 物业首页信息列表
     */
    public static final String WUYE_GET_MESSAGE_INFO_URL = BASE_URL_WUYE + "getMessageInfo";
    /**
     * 获取所有的小区信息
     */
    public static final String WUYE_GET_COMMUNITYS_URL = BASE_URL_WUYE + "getCommunitys";
    /**
     * 获取房主信息
     */
    public static final String GET_OWNER_INFO_URL = BASE_URL_WUYE + "getOwnerInfo";
    /**
     * 认证信息提交
     */
    public static final String AUTHENTICATION_URL = BASE_URL_WUYE + "authentication";
    /**
     * 发送验证码
     */
    public static final String SEND_CODE_URL = BASE_URL_WUYE + "sendCode";
    /**
     * 获取房屋信息
     */
    public static final String GET_HOUSE_INFO_URL = BASE_URL_WUYE + "getHouseInfo";
    /**
     * 申诉信息提交
     */
    public static final String ALLEGEDLY_URL = BASE_URL_WUYE + "allegedly";
    /**
     * 报检报修 房屋下拉信息
     */
    public static final String APPROVE_HOUSE_URL = BASE_URL_WUYE + "approveHouse";
    /**
     * 报检报修信息提交
     */
    public static final String REPAIR_INFO_URL = BASE_URL_WUYE + "repairInfo";
    /**
     * 报检报修信息列表
     */
    public static final String REPAIR_INFO_LIST_URL = BASE_URL_WUYE + "repairInfoList";
    /**
     * 历史账单
     */
    public static final String HISTORY_BILL_URL = BASE_URL_WUYE + "historyBill";
    /**
     * 生活缴费
     */
    public static final String LIVING_EXPENSES_URL = BASE_URL_WUYE + "livingExpenses";
    /**
     * 生活缴费支付页面（手动抄表）
     */
    public static final String PAYMENT_URL = BASE_URL_WUYE + "payment";
    public static final String WUYE_GET_HOUSE_URL = BASE_URL_WUYE + "getHouse";
    /**
     * 我的房屋列表
     */
    public static final String WUYE_MY_HOUSE_URL = BASE_URL_WUYE + "myHouse";
    /**
     * 我的管家里的人员列表
     */
    public static final String WUYE_MY_USER_LIST_URL = BASE_URL_WUYE + "myUserList";
    /**
     * 我的资产列表
     */
    public static final String WUYE_MY_ASSET_URL = BASE_URL_WUYE + "myAsset";
    /**
     * 判断该用户是否绑定住房信息
     */
    public static final String WUYE_CHECK_BINDING_URL = BASE_URL_WUYE + "checkBinding";
    /**
     * 解除/绑定关系
     */
    public static final String WUYE_DEAL_RELATION_URL = BASE_URL_WUYE + "dealRelation";
    /**
     * 缴费记录
     */
    public static final String WUYE_PAY_LOG_URL = BASE_URL_WUYE + "payLog";
    /**
     * 设置默认房屋
     */
    public static final String WUYE_SET_DEFAULT_HOUSE_URL = BASE_URL_WUYE + "setDefaultHouse";
    /**
     * 获取通用信息接口
     */
    public static final String WUYE_GET_INFOS_URL = BASE_URL_WUYE + "getInfos";
    /**
     * 房屋信息列表接口
     */
    public static final String WUYE_HOUSE_MESSAGE_URL = BASE_URL_WUYE + "houseMessage";
    /**
     * 标记消息为已读
     */
    public static final String WUYE_READMESSAGE_URL = BASE_URL_WUYE + "readMessage";
    /**
     * 应缴清单
     */
    public static final String WUYE_NEEDPAYLIST_URL = BASE_URL_WUYE + "needPayList";
    /**
     * 投诉信息
     */
    public static final String WUYE_COMPLAININFO_URL = BASE_URL_WUYE + "complainInfo";
    /**
     * 投诉信息提交
     */
    public static final String WUYE_SUBCOMPLAININFO_URL = BASE_URL_WUYE + "subComplainInfo";
    /**
     * 建议信息
     */
    public static final String WUYE_SUGGESTINFO_URL = BASE_URL_WUYE + "suggestInfo";
    /**
     * 建议信息提交
     */
    public static final String WUYE_SUBSUGGESTINFO_URL = BASE_URL_WUYE + "subSuggestInfo";
    /**
     * 联系物业
     */
    public static final String WUYE_GETWUYEMOBILE_URL = BASE_URL_WUYE + "getWuyeMobile";
    /**
     * 获取投诉记录
     */
    public static final String WUYE_GETCOMPLAINLOG_URL = BASE_URL_WUYE + "getComplainLog";
    /**
     * 获取建议记录
     */
    public static final String WUYE_GETSUGGESTLOG_URL = BASE_URL_WUYE + "getSuggestLog";
    /**
     * 获取未读消息数量
     */
    public static final String WUYE_GET_MESSAGE_COUNT_URL = BASE_URL_WUYE + "getMessageCount";
    /**
     * 邀请信息页面
     */
    public static final String WUYE_INVITE_INFO_URL = BASE_URL_WUYE + "inviteInfo";
    /**
     * 加入提交
     */
    public static final String WUYE_CONFIRMJOIN_URL = BASE_URL_WUYE + "confirmJoin";
    /**
     * 问题反馈信息提交
     */
    public static final String WUYE_FEEDBACK_URL = BASE_URL_WUYE + "feedback";


    /**
     * -------------------请求参数-------------------------
     */
    public static final String REQUEST_PARAM_ID = "id";
    public static final String REQUEST_PARAM_IDS = "ids";
    public static final String REQUEST_PARAM_UID = "uid";
    public static final String REQUEST_PARAM_FID = "fid";
    public static final String REQUEST_PARAM_KEYWORD = "keyword";// 搜索关键词
    public static final String REQUEST_PARAM_GROUPID = "groupid";
    public static final String REQUEST_PARAM_ORDER = "order";
    public static final String REQUEST_PARAM_ORD = "ord";
    public static final String REQUEST_PARAM_PAGE = "page";
    public static final String REQUEST_PARAM_NUM = "num";
    public static final String REQUEST_PARAM_NAME = "name";
    public static final String REQUEST_PARAM_PHONE = "phone";
    public static final String REQUEST_PARAM_LONGITUDE = "longitude";
    public static final String REQUEST_PARAM_LATITUDE = "latitude";
    public static final String REQUEST_PARAM_POSITION = "position";
    public static final String REQUEST_PARAM_ADDRESS = "address";
    public static final String REQUEST_PARAM_ADDRESSID = "addressId";
    public static final String REQUEST_PARAM_IDENTITY = "identity";
    public static final String REQUEST_PARAM_STATE = "state";
    public static final String REQUEST_PARAM_TYPE = "type";
    public static final String REQUEST_PARAM_PID = "pid";
    public static final String REQUEST_PARAM_DELIVERY = "delivery";
    public static final String REQUEST_PARAM_STORE_ID = "store_id";
    public static final String REQUEST_PARAM_STOREID = "storeId";
    public static final String REQUEST_PARAM_PRICE = "price";
    public static final String REQUEST_PARAM_CONTENT = "content";
    public static final String REQUEST_PARAM_ORDERID = "orderid";
    public static final String REQUEST_PARAM_TUI_MODE = "tui_mode";
    public static final String REQUEST_PARAM_TITLE = "title";
    public static final String REQUEST_PARAM_SIMG = "simg";
    public static final String REQUEST_PARAM_GOODS_ID = "goods_id";
    public static final String REQUEST_PARAM_QU_MODE = "qu_mode";
    public static final String REQUEST_PARAM_DOOR_TIME = "door_time";
    public static final String REQUEST_PARAM_DOOT_CONTENT = "doot_content";
    public static final String REQUEST_PARAM_USERNAME = "username";
    public static final String REQUEST_PARAM_SEX = "sex";
    public static final String REQUEST_PARAM_BIRTHDAY = "birthday";
    public static final String REQUEST_PARAM_DESC = "desc";
    public static final String REQUEST_PARAM_YZM = "yzm";
    public static final String REQUEST_PARAM_ENDTIME = "endtime";
    public static final String REQUEST_PARAM_HIDE = "hide";
    public static final String REQUEST_PARAM_COUPON_ID = "coupon_id";
    public static final String REQUEST_PARAM_LAT = "lat";
    public static final String REQUEST_PARAM_LNG = "lng";
    public static final String REQUEST_PARAM_SEARCH = "search";
    public static final String REQUEST_PARAM_COMMUNITYID = "communityId";
    public static final String REQUEST_PARAM_HOUSEID = "houseId";
    public static final String REQUEST_PARAM_MESSAGE_TYPE = "messagetype";
    public static final String REQUEST_PARAM_COMMUNITY_ID = "community_id";
    public static final String REQUEST_PARAM_CRYPTONYM = "cryptonym";
    public static final String REQUEST_PARAM_MESSAGE = "message";
    public static final String REQUEST_PARAM_GROUPON_TEAM_ID = "grouponTeamId";
    public static final String REQUEST_PARAM_GROUPON_ID = "groupon_id";
    public static final String REQUEST_PARAM_SERVICE_ENDTIME = "service_endtime";
    public static final String REQUEST_PARAM_NUMBER_ID = "number_id";
    public static final String REQUEST_PARAM_ADDRESS_PHONE = "address_phone";

    /**
     * 获取腾讯token
     */
    public static final String TX_IMG_SIGN_URL = BASE_URL_INDEX + "tx_img_sign";

}
