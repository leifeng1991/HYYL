package com.xxzlkj.licallife.model;

import android.text.TextUtils;

import com.xxzlkj.zhaolinshare.base.model.BaseBean;
import com.xxzlkj.zhaolinshare.base.util.NumberFormatUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class ProjectDetail extends BaseBean implements Serializable {


    /**
     * data : {"id":"2","simg":"http://zhaolin-10029121.image.myqcloud.com/sample1495504557558537","groupid":"3","shopid":"1","title":"深度保洁-擦玻璃","price":"0.01","prices":"0.00","sale":"5","star":"","ads":"专业清洗，安全放心","desc":"","content":"内容2","addtime":"1495504557","state":"2","ord":"99","adminid":"0","istop":"1","unit":"2","unit_desc":"小时","img":[{"simg":"http://zhaolin-10029121.image.myqcloud.com/sample1495504557380775","w":"1200","h":"2951"}]}
     */

    private DataBean data = new DataBean();

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean implements Serializable {
        /**
         * id : 2
         * simg : http://zhaolin-10029121.image.myqcloud.com/sample1495504557558537
         * groupid : 3
         * shopid : 1
         * title : 深度保洁-擦玻璃
         * price : 0.01
         * prices : 0.00
         * sale : 5
         * star :
         * ads : 专业清洗，安全放心
         * desc :
         * content : 内容2
         * addtime : 1495504557
         * state : 2
         * ord : 99
         * adminid : 0
         * istop : 1
         * unit : 2
         * unit_desc : 小时
         * img : [{"simg":"http://zhaolin-10029121.image.myqcloud.com/sample1495504557380775","w":"1200","h":"2951"}]
         * shop_phone 店铺电话
         */

        private String id;
        private String simg;
        private String groupid;
        private String shopid;
        private String title;
        private String price;
        private String prices;
        private String sale;
        private String star;
        private String ads;
        private String desc;
        private String content;
        private String addtime;
        private String state;
        private String ord;
        private String adminid;
        private String istop;
        private String unit;
        private String unit_desc;
        private String shop_phone;
        private String base;// 起售数量
        private String time;// 耗费时长
        private String res_starttime;// 服务开始时间
        private String res_endtime;// 服务结束时间
        private String res_type;//预约类型：1：按时间段 2：按人数
        private List<ScheduleBean> schedule = new ArrayList<>();
        private List<ImgBean> img = new ArrayList<>();
        private List<ProjectBean> project = new ArrayList<>();
        private List<ShopBean> shop = new ArrayList<>();

        public String getBase() {
            return base;
        }

        public List<ScheduleBean> getSchedule() {
            return schedule;
        }

        public void setSchedule(List<ScheduleBean> schedule) {
            this.schedule = schedule;
        }

        public void setBase(String base) {
            this.base = base;
        }

        public String getShop_phone() {
            return shop_phone;
        }

        public void setShop_phone(String shop_phone) {
            this.shop_phone = shop_phone;
        }

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

        public String getGroupid() {
            return groupid;
        }

        public void setGroupid(String groupid) {
            this.groupid = groupid;
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

        public String getPrices() {
            return prices;
        }

        public void setPrices(String prices) {
            this.prices = prices;
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

        public String getAds() {
            return ads;
        }

        public void setAds(String ads) {
            this.ads = ads;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getAddtime() {
            return addtime;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getOrd() {
            return ord;
        }

        public void setOrd(String ord) {
            this.ord = ord;
        }

        public String getAdminid() {
            return adminid;
        }

        public void setAdminid(String adminid) {
            this.adminid = adminid;
        }

        public String getIstop() {
            return istop;
        }

        public void setIstop(String istop) {
            this.istop = istop;
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

        public String getRealPrice() {
            String unit = getUnit();
            if (!TextUtils.isEmpty(unit)) {
                if ("1".equals(unit)) {// 数量
                    return "￥" + getPrice();
                } else if ("2".equals(unit)) {// 时间
                    return "￥" + NumberFormatUtils.toFloat(getPrice()) * 2;
                }
            }

            return "";
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getRes_starttime() {
            return res_starttime;
        }

        public void setRes_starttime(String res_starttime) {
            this.res_starttime = res_starttime;
        }

        public String getRes_endtime() {
            return res_endtime;
        }

        public void setRes_endtime(String res_endtime) {
            this.res_endtime = res_endtime;
        }

        public String getRes_type() {
            return res_type;
        }

        public void setRes_type(String res_type) {
            this.res_type = res_type;
        }

        public List<ImgBean> getImg() {
            return img;
        }

        public void setImg(List<ImgBean> img) {
            this.img = img;
        }

        public List<ProjectBean> getProject() {
            return project;
        }

        public void setProject(List<ProjectBean> project) {
            this.project = project;
        }

        public static class ImgBean implements Serializable {
            /**
             * simg : http://zhaolin-10029121.image.myqcloud.com/sample1495504557380775
             * w : 1200
             * h : 2951
             */

            private String simg;
            private String w;
            private String h;

            public String getSimg() {
                return simg;
            }

            public void setSimg(String simg) {
                this.simg = simg;
            }

            public String getW() {
                return w;
            }

            public void setW(String w) {
                this.w = w;
            }

            public String getH() {
                return h;
            }

            public void setH(String h) {
                this.h = h;
            }
        }

        public static class ProjectBean implements Serializable {
            private String id;
            private String goods_id;
            private String content;
            private String title;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getGoods_id() {
                return goods_id;
            }

            public void setGoods_id(String goods_id) {
                this.goods_id = goods_id;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }
        }

        public static class ShopBean implements Serializable {
            private String id;
            private String title;
            private String phone;
            private String simg;
            private String desc;
            private String service_starttime;
            private String service_endtime;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public String getSimg() {
                return simg;
            }

            public void setSimg(String simg) {
                this.simg = simg;
            }

            public String getDesc() {
                return desc;
            }

            public void setDesc(String desc) {
                this.desc = desc;
            }

            public String getService_starttime() {
                return service_starttime;
            }

            public void setService_starttime(String service_starttime) {
                this.service_starttime = service_starttime;
            }

            public String getService_endtime() {
                return service_endtime;
            }

            public void setService_endtime(String service_endtime) {
                this.service_endtime = service_endtime;
            }
        }
    }
}
