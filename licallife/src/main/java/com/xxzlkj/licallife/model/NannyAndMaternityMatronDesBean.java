package com.xxzlkj.licallife.model;

import com.xxzlkj.zhaolinshare.base.model.BaseBean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class NannyAndMaternityMatronDesBean extends BaseBean {


    /**
     * data : {"id":"9","uid":"111337","simg":"","shopid":"1","name":"刘某某","price":"100.00","prices":"5000.00","addtime":"1500965850","type":"3","state":"2","content":"这里是详情内容","sale":"0","label":"","star":"5","ord":"99","base":"1","bases":"1","ads":"有证有经验","cardno":"13000000000000000000","phone":"15911056751","types":"","place":"97","place2":"9","nation":"汉","marriage":"1","age":"48","constellation":"天蝎座","genus":"龙","address":"北京","rest":"4","specialty":"做饭","jobs":"12","introduction":"这里是自我介绍","onduty":"1000","experience":"","education":"0","cardno_img":"","insurance":"1","pv":"0","satisfaction":"0","saturation":"0","service_hours":"","duties":["做家务","辅助孩子"],"ability":[],"training":[],"paperwork":["123"],"shop":{"id":"1","uid":"111337","title":"本地生活店铺1","addtime":"0","address":"北京朝阳区","phone":"1555555555","simg":"","desc":"","longitude":"116.435545","latitude":"39.926594","state":"2","yuesao":"100"}}
     */

    private DataBean data = new DataBean();

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean implements Serializable{
        /**
         * id : 9
         * uid : 111337
         * simg :
         * shopid : 1
         * name : 刘某某
         * price : 100.00
         * prices : 5000.00
         * addtime : 1500965850
         * type : 3
         * state : 2
         * content : 这里是详情内容
         * sale : 0
         * label :
         * star : 5
         * ord : 99
         * base : 1
         * bases : 1
         * ads : 有证有经验
         * cardno : 13000000000000000000
         * phone : 15911056751
         * types :
         * place : 97
         * place2 : 9
         * nation : 汉
         * marriage : 1
         * age : 48
         * constellation : 天蝎座
         * genus : 龙
         * address : 北京
         * rest : 4
         * specialty : 做饭
         * jobs : 12
         * introduction : 这里是自我介绍
         * onduty : 1000
         * experience :
         * education : 0
         * cardno_img :
         * insurance : 1
         * pv : 0
         * satisfaction : 0
         * saturation : 0
         * service_hours :
         * duties : ["做家务","辅助孩子"]
         * ability : []
         * training : []
         * paperwork : ["123"]
         * shop : {"id":"1","uid":"111337","title":"本地生活店铺1","addtime":"0","address":"北京朝阳区","phone":"1555555555","simg":"","desc":"","longitude":"116.435545","latitude":"39.926594","state":"2","yuesao":"100"}
         */

        private String id;
        private int is_cell;
        private String uid;
        private String simg;
        private String shopid;
        private String name = "";
        private String price = "";
        private String praise = "";
        private String prices;
        private String addtime;
        private String type;
        private String state;
        private String status;// 1 待岗 2在岗
        private String edittime;
        private String content;
        private String sale = "";
        private String label;
        private String star;
        private String ord;
        private String base;
        private String bases;
        private String ads;
        private String cardno;
        private String phone;
        private String types;
        private String place;
        private String place2;
        private String nation;
        private String marriage;
        private String age;
        private String constellation;
        private String genus;
        private String address;
        private String work;
        private String place_title;
        private String rest;
        private String specialty;
        private String jobs;
        private String introduction;
        private String onduty = "";
        private String experience;
        private String education;
        private String cardno_img;
        private String insurance;
        private String pv = "";
        private String satisfaction;
        private String saturation;
        private String service_hours;
        private String juli;// 距小区距离（km）
        private ShopBean shop = new ShopBean();
        private List<AbilityBean> ability = new ArrayList<>();
        private List<TrainingBean> training = new ArrayList<>();
        private List<NannyAndMaternityMatronListBean.DataBean> recommend = new ArrayList<>();
        private List<String> duties = new ArrayList<>();
        private List<String> paperwork = new ArrayList<>();
        private List<ScheduleBean> schedule = new ArrayList<>();

        public String getJuli() {
            return juli;
        }

        public void setJuli(String juli) {
            this.juli = juli;
        }

        public static class TrainingBean  implements Serializable{
            private String title;
            private String theme;
            private String types;
            private String themetime;

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getTheme() {
                return theme;
            }

            public void setTheme(String theme) {
                this.theme = theme;
            }

            public String getTypes() {
                return types;
            }

            public void setTypes(String types) {
                this.types = types;
            }

            public String getThemetime() {
                return themetime;
            }

            public void setThemetime(String themetime) {
                this.themetime = themetime;
            }
        }

        public static class AbilityBean  implements Serializable{
            private String title;    //标题
            private String simg;    //图片

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getSimg() {
                return simg;
            }

            public void setSimg(String simg) {
                this.simg = simg;
            }
        }

        public List<ScheduleBean> getSchedule() {
            return schedule;
        }

        public void setSchedule(List<ScheduleBean> schedule) {
            this.schedule = schedule;
        }

        public List<NannyAndMaternityMatronListBean.DataBean> getRecommend() {
            return recommend;
        }

        public void setRecommend(List<NannyAndMaternityMatronListBean.DataBean> recommend) {
            this.recommend = recommend;
        }

        public String getWork() {
            return work;
        }

        public void setWork(String work) {
            this.work = work;
        }

        public String getPlace_title() {
            return place_title;
        }

        public void setPlace_title(String place_title) {
            this.place_title = place_title;
        }

        public int getIs_cell() {
            return is_cell;
        }

        public void setIs_cell(int is_cell) {
            this.is_cell = is_cell;
        }

        public List<TrainingBean> getTraining() {
            return training;
        }

        public void setTraining(List<TrainingBean> training) {
            this.training = training;
        }

        public List<AbilityBean> getAbility() {
            return ability;
        }

        public void setAbility(List<AbilityBean> ability) {
            this.ability = ability;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getEdittime() {
            return edittime;
        }

        public void setEdittime(String edittime) {
            this.edittime = edittime;
        }

        public String getPraise() {
            return praise;
        }

        public void setPraise(String praise) {
            this.praise = praise;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
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

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
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

        public String getAddtime() {
            return addtime;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getSale() {
            return sale;
        }

        public void setSale(String sale) {
            this.sale = sale;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public String getStar() {
            return star;
        }

        public void setStar(String star) {
            this.star = star;
        }

        public String getOrd() {
            return ord;
        }

        public void setOrd(String ord) {
            this.ord = ord;
        }

        public String getBase() {
            return base;
        }

        public void setBase(String base) {
            this.base = base;
        }

        public String getBases() {
            return bases;
        }

        public void setBases(String bases) {
            this.bases = bases;
        }

        public String getAds() {
            return ads;
        }

        public void setAds(String ads) {
            this.ads = ads;
        }

        public String getCardno() {
            return cardno;
        }

        public void setCardno(String cardno) {
            this.cardno = cardno;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getTypes() {
            return types;
        }

        public void setTypes(String types) {
            this.types = types;
        }

        public String getPlace() {
            return place;
        }

        public void setPlace(String place) {
            this.place = place;
        }

        public String getPlace2() {
            return place2;
        }

        public void setPlace2(String place2) {
            this.place2 = place2;
        }

        public String getNation() {
            return nation;
        }

        public void setNation(String nation) {
            this.nation = nation;
        }

        public String getMarriage() {
            return marriage;
        }

        public void setMarriage(String marriage) {
            this.marriage = marriage;
        }

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }

        public String getConstellation() {
            return constellation;
        }

        public void setConstellation(String constellation) {
            this.constellation = constellation;
        }

        public String getGenus() {
            return genus;
        }

        public void setGenus(String genus) {
            this.genus = genus;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getRest() {
            return rest;
        }

        public void setRest(String rest) {
            this.rest = rest;
        }

        public String getSpecialty() {
            return specialty;
        }

        public void setSpecialty(String specialty) {
            this.specialty = specialty;
        }

        public String getJobs() {
            return jobs;
        }

        public void setJobs(String jobs) {
            this.jobs = jobs;
        }

        public String getIntroduction() {
            return introduction;
        }

        public void setIntroduction(String introduction) {
            this.introduction = introduction;
        }

        public String getOnduty() {
            return onduty;
        }

        public void setOnduty(String onduty) {
            this.onduty = onduty;
        }

        public String getExperience() {
            return experience;
        }

        public void setExperience(String experience) {
            this.experience = experience;
        }

        public String getEducation() {
            return education;
        }

        public void setEducation(String education) {
            this.education = education;
        }

        public String getCardno_img() {
            return cardno_img;
        }

        public void setCardno_img(String cardno_img) {
            this.cardno_img = cardno_img;
        }

        public String getInsurance() {
            return insurance;
        }

        public void setInsurance(String insurance) {
            this.insurance = insurance;
        }

        public String getPv() {
            return pv;
        }

        public void setPv(String pv) {
            this.pv = pv;
        }

        public String getSatisfaction() {
            return satisfaction;
        }

        public void setSatisfaction(String satisfaction) {
            this.satisfaction = satisfaction;
        }

        public String getSaturation() {
            return saturation;
        }

        public void setSaturation(String saturation) {
            this.saturation = saturation;
        }

        public String getService_hours() {
            return service_hours;
        }

        public void setService_hours(String service_hours) {
            this.service_hours = service_hours;
        }

        public ShopBean getShop() {
            return shop;
        }

        public void setShop(ShopBean shop) {
            this.shop = shop;
        }

        public List<String> getDuties() {
            return duties;
        }

        public void setDuties(List<String> duties) {
            this.duties = duties;
        }

        public List<String> getPaperwork() {
            return paperwork;
        }

        public void setPaperwork(List<String> paperwork) {
            this.paperwork = paperwork;
        }

        public static class ShopBean  implements Serializable{
            /**
             * id : 1
             * uid : 111337
             * title : 本地生活店铺1
             * addtime : 0
             * address : 北京朝阳区
             * phone : 1555555555
             * simg :
             * desc :
             * longitude : 116.435545
             * latitude : 39.926594
             * state : 2
             * yuesao : 100
             */

            private String id;
            private String uid;
            private String title;
            private String addtime;
            private String address;
            private String phone;
            private String simg;
            private String desc;
            private String longitude;
            private String latitude;
            private String state;
            private String yuesao;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getUid() {
                return uid;
            }

            public void setUid(String uid) {
                this.uid = uid;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getAddtime() {
                return addtime;
            }

            public void setAddtime(String addtime) {
                this.addtime = addtime;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
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

            public String getState() {
                return state;
            }

            public void setState(String state) {
                this.state = state;
            }

            public String getYuesao() {
                return yuesao;
            }

            public void setYuesao(String yuesao) {
                this.yuesao = yuesao;
            }
        }
    }
}
