package com.xxzlkj.shop.utils;

import android.view.View;
import android.widget.TextView;

import com.xxzlkj.zhaolinshare.base.util.NumberFormatUtils;
import com.zrq.spanbuilder.Spans;

/**
 * 价格相同时隐藏 下滑线价格
 * Created by Administrator on 2017/5/3.
 */

public class TextPriceUtil {
    /**
     * 设置原价
     *
     * @param price          现价
     * @param prices         原价
     * @param pricesTextView 原价TextView
     */
    public static void setTextPrices(String price, String prices, TextView pricesTextView) {
        if (prices == null || prices.equals(price) || NumberFormatUtils.toDouble(price) >= NumberFormatUtils.toDouble(prices)) {
            // 原价为空，或者现价大于等于原价
            pricesTextView.setVisibility(View.GONE);
        } else {
            pricesTextView.setVisibility(View.VISIBLE);
            pricesTextView.setText(Spans.builder().text("￥" + prices).deleteLine().build());
        }
    }

    /**
     * 获取原价显示
     *
     * @param price  现价
     * @param prices 原价
     */
    public static String getTextPrices(String price, String prices) {
        if (prices == null || prices.equals(price) || NumberFormatUtils.toDouble(price) >= NumberFormatUtils.toDouble(prices)) {
            // 原价为空，或者现价大于等于原价
            return "";
        } else {
            return prices;
        }
    }
}
