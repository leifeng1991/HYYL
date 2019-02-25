package com.xxzlkj.huayiyanglao.model;

import com.xxzlkj.zhaolinshare.base.model.BaseBean;

/**
 * Created by Administrator on 2017/3/31.
 */

public class AlipaySign extends BaseBean {

    /**
     * data : {"sign":"alipay_sdk=alipay-sdk-php-20161101&app_id=2017031706259896&biz_content=%7B%22subject%22%3A%22order%22%2C%22out_trade_no%22%3A%2220170331171907366593%22%2C%22total_amount%22%3A%220.01%22%2C%22timeout_express%22%3A%221d%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%7D&charset=UTF-8&format=json&method=alipay.trade.app.pay&notify_url=http%3A%2F%2Fwww.zhaolin365.com%2FApi%2FWeb%2Falipay_notify_url&sign_type=RSA2&timestamp=2017-03-31+17%3A19%3A07&version=1.0&sign=HGIaBQVuTdi3%2BKdtud3nCER52bdgvQyffLBg3SFPiZmD60BZGhDTDN2cJhlcPA%2BHrC%2BITIM8JsuTHgzUN%2FymxeKGw9lQSfUE2cQbl%2Fa8SoHGbGBlLhlL3JwJ0IU04FzC19JrjNVBOU1Ly44BblDHnXQ2WJWMuA6VXVpHshjNa2vpoy7fEFjTjbI5LwJUQOKiLqqISmxS9Zb1kcz5M8D5b42J3D%2FuHrjzaqeyUtmzGhPOu%2FtqMJgjxOoZcpKg%2FMbbfxZ5Zwmh8d0oVaw63vsTglyavxuiZTz79E8lUi4%2Ba7aIsnv4Cpmp8toH59GVYe82nEvv6M8u7CdpazlKRJ4ACg%3D%3D"}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * sign : alipay_sdk=alipay-sdk-php-20161101&app_id=2017031706259896&biz_content=%7B%22subject%22%3A%22order%22%2C%22out_trade_no%22%3A%2220170331171907366593%22%2C%22total_amount%22%3A%220.01%22%2C%22timeout_express%22%3A%221d%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%7D&charset=UTF-8&format=json&method=alipay.trade.app.pay&notify_url=http%3A%2F%2Fwww.zhaolin365.com%2FApi%2FWeb%2Falipay_notify_url&sign_type=RSA2&timestamp=2017-03-31+17%3A19%3A07&version=1.0&sign=HGIaBQVuTdi3%2BKdtud3nCER52bdgvQyffLBg3SFPiZmD60BZGhDTDN2cJhlcPA%2BHrC%2BITIM8JsuTHgzUN%2FymxeKGw9lQSfUE2cQbl%2Fa8SoHGbGBlLhlL3JwJ0IU04FzC19JrjNVBOU1Ly44BblDHnXQ2WJWMuA6VXVpHshjNa2vpoy7fEFjTjbI5LwJUQOKiLqqISmxS9Zb1kcz5M8D5b42J3D%2FuHrjzaqeyUtmzGhPOu%2FtqMJgjxOoZcpKg%2FMbbfxZ5Zwmh8d0oVaw63vsTglyavxuiZTz79E8lUi4%2Ba7aIsnv4Cpmp8toH59GVYe82nEvv6M8u7CdpazlKRJ4ACg%3D%3D
         */

        private String sign;

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }
    }
}
