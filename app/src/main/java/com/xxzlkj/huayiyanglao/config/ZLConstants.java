package com.xxzlkj.huayiyanglao.config;


import com.xxzlkj.huayiyanglao.BuildConfig;

/**
 * 常量
 *
 * @author zhangrq
 */
public interface ZLConstants {
    interface Strings {
        String TYPE = "type";
        String UID = "uid";
        String PHONE = "phone";
        String PASSWROD = "passwrod";
        String TITLE = "title";
        String GROUPID = "groupId";
        String LOGIN_USER_NAME = "login_user_name";
        String POI_ITEM = "poi_item";
        String PID = "pid";
        String BID = "bid";
        String IS_REFUND = "isRefund";
        String SELECT_BEAN = "selectBean";
        String MAP_LOCATION = "aMapLocation";
        String ID = "id";
        String COMMUNITY_ID = "community_id";
        String COMMUNITY_NAME = "community_name";
        String SEX = "sex";
        String SHARE_SITUATION = "share_situation";
        String SYSTEM_MESSAGE_ID = "1000";// 系统消息
        String INTERACTIVE_MESSAGE_ID = "bbs_1001";// 互动消息
        String VERSION_NAME = "version_name";//版本名
        String INTENT_PARAM_HOUSE_ID = "house_id";
        String KEYBOARD = "Keyboard";
        String BBS_COMMUNITY_BEAN = "bbs_community_bean";
        String FLAG = "flag";
        String IS_ATTENTION = "is_attention";
        String ER_WEI_MA_URL = "er_Wei_Ma_Url";
        String USER_ICON_URL = "user_icon_url";
        String IS_REFRESH = "is_refresh";
        String INDEX = "index";
        String ADDRESS_ID = "address_id";
        String IS_EDIT = "is_edit";
        String LOCATION = "location";
        String LATITUDE = "latitude";
        String LONGITUDE = "longitude";
        String SHOPID = "shopId";
        String FOODSNAME = "foodsName";
        String DATE = "date";
        String RADIUS = "radius";
        String IS_OPEN = "is_open";
        String DATA_BEAN = "data_bean";
        String FAMILY_PHONE = "family_phone";
        String URGENCY_PHONE = "urgency_phone";
        String ADDRESSBEAN_DATABEAN = "AddressBean_DataBean";
        String IS_HOME_DELIVERY = "isHomeDelivery";
        String IMEI = "imei";
        // 微信配置
        String WECHAT_APPID = BuildConfig.wechat_appId;
        String WECHAT_APPSECRET = BuildConfig.wechat_appSecret;
    }

    interface Integers {
        int REQUEST_IMAGE = 123;
        int RESULT_FINISH = 888;
        int REQUEST_CODE_LGOIN_FINISH = 101;
        int REQUEST_CODE_LOCATION_FINISH = 102;
        int REQUEST_CODE_SELECT = 103;
        int REQUEST_CODE_PERSON_MAIN = 104;
        int REQUEST_CODE_SESSION_DES = 105;
        int REQUEST_CODE_ADDRESS = 106;
        int REQUEST_CODE_ADD_ADDRESS = 107;
    }

    interface Params {
        String ID = "id";
        String IDS = "ids";
        String UID = "uid";
        String PID = "pid";
        String FID = "fid";
        String BID = "bid";
        String IS_HOME = "is_home";
        String TYPE = "type";
        String PHONE = "phone";
        String YZM = "yzm";
        String PASSWORD = "password";
        String JPUSH_ID = "jpushid";
        String USER_NAME = "username";
        String UNIT = "unit";
        String SIMG = "simg";
        String CARDNO = "cardno";
        String BIRTHDAY = "birthday";
        String PASS = "pass";
        String MY_TAGS = "my_tags";
        String TAGS = "tags";
        String VIDEO = "video";
        String ADDRESS = "address";
        String LONGITUDE = "longitude";
        String LATITUDE = "latitude";
        String COMMUNITY_ID = "community_id";
        String PAGE = "page";
        String GROUPID = "groupid";
        String ORD = "ord";
        String STYLE = "style";
        String TITLE = "title";
        String DESC = "desc";
        String IMG_SIMG = "img_simg";
        String IMG_W = "img_w";
        String IMG_H = "img_h";
        String PRICES = "prices";
        String PRICE = "price";
        String BBS_ID = "bbs_id";
        String CONTENT = "content";
        String STATE = "state";
        String DYNAMIC_ID = "dynamic_id";
        String USERNAME = "username";
        String HIDE = "hide";
        String IS_BBS = "is_bbs";
        String USER_ID = "user_id";
        String BLACK_USER_ID = "black_user_id";
        String NAME = "name";
        String POSITION = "position";
        String IDENTITY = "identity";
        String SHOPID = "shopid";
        String DATE = "date";
        String NUM = "num";
        String DELIVERY = "delivery";
        String ORDERID = "orderid";
        String FENCE_STATE = "fence_state";
        String FENCE_LONGITUDE = "fence_longitude";
        String FENCE_LATITUDE = "fence_latitude";
        String AFFECTION_PHONE = "affection_phone";
        String WATCH_STARTTIME = "watch_starttime";
        String WATCH_ENDTIME = "watch_endtime";
        String WATCH_INTERVAL = "watch_interval";
        String WATCH_PHONE = "watch_phone";
        String IMEI = "imei";
        String FENCE_SOS = "fence_sos";
        String FENCE_M = "fence_m";
        String KEYWORD = "keyword";
    }
}
