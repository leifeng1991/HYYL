package com.xxzlkj.shop.utils;

import android.content.Context;

import com.xxzlkj.shop.config.StringConstants;
import com.xxzlkj.shop.interfac.AddressInfoInterface;
import com.xxzlkj.zhaolinshare.base.util.PreferencesSaver;


/**
 * 描述:
 *
 * @author zhangrq
 *         2017/6/30 16:05
 */
public class ZLPreferencesUtils {

    /**
     * 保存地址信息
     */
    public static void saveAddressInfo(Context mContext, AddressInfoInterface item) {
        // 保存地址id
        PreferencesSaver.setStringAttr(mContext, StringConstants.PREFERENCES_MY_ADDRESS_ID, item.getId());
        // 保存地址
        PreferencesSaver.setStringAttr(mContext, StringConstants.PREFERENCES_MY_ADDRESS, item.getAddress());
        // 保存经度
        PreferencesSaver.setStringAttr(mContext, StringConstants.PREFERENCES_MY_LONGITUDE, item.getLongitude());
        // 保存纬度
        PreferencesSaver.setStringAttr(mContext, StringConstants.PREFERENCES_MY_LATITUDE, item.getLatitude());
    }

    /**
     * 移除地址信息
     */
    public static void removeAddressInfo(Context mContext) {
        // 删除地址id
        PreferencesSaver.removeAttr(mContext, StringConstants.PREFERENCES_MY_ADDRESS_ID);
        // 删除地址
        PreferencesSaver.removeAttr(mContext, StringConstants.PREFERENCES_MY_ADDRESS);
        // 删除经度
        PreferencesSaver.removeAttr(mContext, StringConstants.PREFERENCES_MY_LONGITUDE);
        // 删除纬度
        PreferencesSaver.removeAttr(mContext, StringConstants.PREFERENCES_MY_LATITUDE);
    }
}
