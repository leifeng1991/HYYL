package com.xxzlkj.shop.config;



/**
 * String类型的常量
 *
 * @author zhangrq
 */
public interface StringConstants {

    int HOT_RECOMMEND_TYPE_1 = 1;
    int HOT_RECOMMEND_TYPE_2 = 2;
    int HOT_RECOMMEND_TYPE_3 = 3;
    int GOODS_TYPE_1 = 1;
    int GOODS_TYPE_2 = 2;
    int INTENT_TO_NO = 0;// 不跳转
    int INTENT_TO_H5 = 1;// 跳转h5
    int INTENT_TO_GOODS = 2;// 商品
    int INTENT_TO_GOODSTYPE = 3;// 跳转搜索商品列表
    int INTENT_TO_YUSHOU = 4;// 火爆预售
    int INTENT_TO_TUANGOU = 5;// 兆邻团购
    int INTENT_TO_SHENGXIAN = 6;// 兆邻生鲜
    int TYPE_YU_SHOU = 2;
    int TYPE_TUAN_GOU = 3;
    int TYPE_SHENG_XIAN = 4;
    String INTENT_PARAM_ID = "id";
    String INTENT_PARAM_UID = "uid";
    String INTENT_PARAM_IDS = "ids";
    String INTENT_PARAM_TYPE = "type";
    String INTENT_PARAM_PSTYPE = "psType";
    String INTENT_PARAM_ZFTYPE = "zfType";
    String INTENT_PARAM_KEYWORD = "keyword";
    String INTENT_PARAM_GROUPID = "groupId";
    String INTENT_PARAM_GOODSLIST = "goodsList";
    String INTENT_PARAM_TOTALPRICE = "totalPrice";
    String INTENT_PARAM_PAYTYPE = "payType";
    String INTENT_PARAM_PEISONGTYPE = "peiSongType";
    String INTENT_PARAM_LOCATION = "location";
    String INTENT_PARAM_LOC = "loc";
    String INTENT_PARAM_LATITUDE = "latitude";
    String INTENT_PARAM_LONGITUDE = "longitude";
    String INTENT_PARAM_DATABEAN = "dataBean";
    String INTENT_PARAM_PS_TIME = "ps_time";
    String INTENT_PARAM_ORDER = "Order";
    String INTENT_PARAM_ORDERID = "order_id";
    String INTENT_PARAM_ADDRESSID = "addressId";
    String INTENT_PARAM_POSITION = "position";
    String INTENT_PARAM_NAME = "name";
    String INTENT_PARAM_STOREID = "storeId";
    String INTENT_PARAM_STORE_ID = "store_id";
    String INTENT_PARAM_PID = "pid";
    String INTENT_PARAM_PRICE = "price";
    String INTENT_PARAM_NUM = "num";
    String INTENT_PARAM_ERWEIMAURL = "erWeiMaUrl";
    String INTENT_PARAM_USERICONURL = "userIconUrl";
    String INTENT_PARAM_DISCOUNT = "discount";
    String INTENT_PARAM_TITLE = "title";
    String INTENT_PARAM_LEFT_TITLE = "left_title";
    String INTENT_PARAM_RIGHT_TITLE = "right_title";
    String INTENT_PARAM_PROJECTDETAIL_DATABEAN = "projectdetail_databean";
    String INTENT_PARAM_COUPON_PRICE = "coupon_price";
    String INTENT_PARAM_HOUSE_ID = "house_id";
    String INTENT_PARAM_IDENTITY = "identity";
    String INTENT_PARAM_ADDRESS_BEAN = "addressBean";
    String INTENT_PARAM_JUMP_TYPE = "jump_type";
    String INTENT_GROUPON_TEAM_ID = "grouponTeamId";


    String IMAGE_TOKEN = "image_token";
    String IMAGE_TOKEN_TIME = "image_token_time";

    String PREFERENCES_KEY_KEYWORD_LIST = "keyword_list";
    String PREFERENCES_MY_ADDRESS_ID = "my_address_id";
    String PREFERENCES_MY_ADDRESS = "my_address";
    String PREFERENCES_MY_LATITUDE = "my_latitude";
    String PREFERENCES_MY_LONGITUDE = "my_longitude";
    String PREFERENCES_LOCATION_OR_ADDRESS = "location_or_address";// ture:地址 false:定位

    int REQUSTCODE_MSO = 100;
    int REQUSTCODE_LOCATION = 101;
    int REQUSTCODE_ORDER = 102;
    int REQUSTCODE_STORE = 103;
    int REQUSTCODE_COUPOS = 104;
    int REQUSTCODE_HOUSING_CHOICE = 105;
    int REQUSTCODE_IS_HAS_READ = 106;
    int REQUSTCODE_MY_ACTIVITY_LIST = 107;
    int REQUSTCODE_INVITA_INFO = 108;
}
