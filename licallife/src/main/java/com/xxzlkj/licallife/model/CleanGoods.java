package com.xxzlkj.licallife.model;

import android.text.TextUtils;

import com.xxzlkj.zhaolinshare.base.model.BaseBean;
import com.xxzlkj.zhaolinshare.base.util.NumberFormatUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 保洁商品
 * Created by Administrator on 2017/4/20.
 */

public class CleanGoods extends BaseBean {


    private List<DataBean> data = new ArrayList<>();

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 3
         * simg : http://zhaolin-10029121.image.myqcloud.com/sample1495518993770559
         * shopid : 2
         * title : 日常保洁-常规
         * price : 0.01
         * sale : 0
         * star :
         * groupid : 2
         * unit : 2
         * unit_desc : 小时
         * shop_title : 本地生活店铺2
         */

        private String id;
        private String simg;
        private String shopid;
        private String title;
        private String price;
        private String sale;
        private String star;
        private String groupid;
        private String unit;
        private String unit_desc;
        private String shop_title;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getSimg() {
            return simg;
        }

        public void setSimg(String simg) {
            this.simg = simg;
        }

        public String getShopid() {
            return shopid;
        }

        public void setShopid(String shopid) {
            this.shopid = shopid;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getSale() {
            return sale;
        }

        public void setSale(String sale) {
            this.sale = sale;
        }

        public String getStar() {
            return star;
        }

        public void setStar(String star) {
            this.star = star;
        }

        public String getGroupid() {
            return groupid;
        }

        public void setGroupid(String groupid) {
            this.groupid = groupid;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public String getUnit_desc() {
            return unit_desc;
        }

        public void setUnit_desc(String unit_desc) {
            this.unit_desc = unit_desc;
        }

        public String getShop_title() {
            return shop_title;
        }

        public void setShop_title(String shop_title) {
            this.shop_title = shop_title;
        }

        public String getUnitContent() {
            String unit = getUnit();
            if (!TextUtils.isEmpty(unit)) {
                if ("1".equals(unit)) {// 数量
                    return "￥" + getPrice() + "/" + getUnit_desc();
                } else if ("2".equals(unit)) {// 时间
                    return "￥" + NumberFormatUtils.toFloat(getPrice()) * 2 + "/" + getUnit_desc();
                }
            }

            return "";
        }
    }
}
