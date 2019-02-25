package com.xxzlkj.shop.config;

/**
 * 全局参数
 *
 * @author zhangrq
 */
public class GlobalParams {
    /**
     * 是否开启调试模式（调试的模式，吐司提示错误信息、log开）
     */
//    public static boolean isDebug = BuildConfig.isTest;
    public static boolean isDebug = false;
    public static boolean isFirstNetRun = true;
    public static boolean isFromJiaYi = false;
    public static String jiaYiUserName;

    public static double longitude;// 经度
    public static double latitude;// 纬度
    public static String storeId;// 店铺id
    public static String communityId;// 小区id
    public static String houseId;// 房屋id
    public static boolean isOpenLocalLife;// 是否开通本地生活
    public final static int RESULT_FINISH_SUBMIT_AUTH = 555;// 提交认证模块的销毁码
}
