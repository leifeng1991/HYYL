package com.xxzlkj.huayiyanglao.model;

import com.xxzlkj.zhaolinshare.base.model.BaseBean;

/**
 * 描述:
 *
 * @author leifeng
 *         2018/1/30 16:28
 */


public class WatchPositionBean extends BaseBean{

    /**
     * data : {"longitude":"117.481648","latitude":"39.886613","address":"天津市蓟州区S21塘承高速"}
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
         * longitude : 117.481648
         * latitude : 39.886613
         * address : 天津市蓟州区S21塘承高速
         */

        private String longitude;
        private String latitude;
        private String address;

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }
    }
}
