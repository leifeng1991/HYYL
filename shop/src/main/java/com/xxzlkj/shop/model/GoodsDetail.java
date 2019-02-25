package com.xxzlkj.shop.model;

import com.xxzlkj.zhaolinshare.base.model.BaseBean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 商品详情
 * Created by Administrator on 2017/3/18.
 */

public class GoodsDetail extends BaseBean {
    /**
     * data : {"id":"9503848","title":"雪花啤酒纯生 500ml","img":"http://zhaolin-10029121.image.myqcloud.com/sample1489134729899461","ads":"雪花啤酒 , 品味一生","desc":"雪花啤酒 , 品味一生，请不要纵酒过渡，不要酒驾","pid":"9503836","price":"5.00","prices":"7.00","packing":"瓶装","term":"2年","storage":"常温或冷藏","content":[{"simg":"http://zhaolin-10029121.image.myqcloud.com/sample1489646979784566","w":"1024","h":"1024"},{"simg":"http://zhaolin-10029121.image.myqcloud.com/sample1489646979880598","w":"900","h":"900"},{"simg":"http://zhaolin-10029121.image.myqcloud.com/sample1489646979778289","w":"520","h":"430"}],"simg":["http://zhaolin-10029121.image.myqcloud.com/sample148913472836329","http://zhaolin-10029121.image.myqcloud.com/sample1489134729976886","http://zhaolin-10029121.image.myqcloud.com/sample1489134729899461"],"is_cell":1,"attr_list":[{"id":"410","title":"啤酒品类","catid":[{"id":"411","title":"淡爽","is_select":1,"goods_id":"9503847","is_state":"2"},{"id":"412","title":"纯生","is_select":2,"goods_id":"9503848","is_state":2}]},{"id":"415","title":"容量","catid":[{"id":"416","title":"350ml","is_select":1,"goods_id":"9503846","is_state":"2"},{"id":"417","title":"500ml","is_select":2,"goods_id":"9503848","is_state":2}]}],"attr":[{"k":"啤酒品类","v":"纯生"},{"k":"容量","v":"500ml"}]}
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
         * id : 9503848
         * title : 雪花啤酒纯生 500ml
         * img : http://zhaolin-10029121.image.myqcloud.com/sample1489134729899461
         * ads : 雪花啤酒 , 品味一生
         * desc : 雪花啤酒 , 品味一生，请不要纵酒过渡，不要酒驾
         * pid : 9503836
         * price : 5.00
         * prices : 7.00
         * packing : 瓶装
         * term : 2年
         * storage : 常温或冷藏
         * content : [{"simg":"http://zhaolin-10029121.image.myqcloud.com/sample1489646979784566","w":"1024","h":"1024"},{"simg":"http://zhaolin-10029121.image.myqcloud.com/sample1489646979880598","w":"900","h":"900"},{"simg":"http://zhaolin-10029121.image.myqcloud.com/sample1489646979778289","w":"520","h":"430"}]
         * simg : ["http://zhaolin-10029121.image.myqcloud.com/sample148913472836329","http://zhaolin-10029121.image.myqcloud.com/sample1489134729976886","http://zhaolin-10029121.image.myqcloud.com/sample1489134729899461"]
         * is_cell : 1
         * attr_list : [{"id":"410","title":"啤酒品类","catid":[{"id":"411","title":"淡爽","is_select":1,"goods_id":"9503847","is_state":"2"},{"id":"412","title":"纯生","is_select":2,"goods_id":"9503848","is_state":2}]},{"id":"415","title":"容量","catid":[{"id":"416","title":"350ml","is_select":1,"goods_id":"9503846","is_state":"2"},{"id":"417","title":"500ml","is_select":2,"goods_id":"9503848","is_state":2}]}]
         * attr : [{"k":"啤酒品类","v":"纯生"},{"k":"容量","v":"500ml"}]
         */

        private String id;
        private String title;
        private String img;
        private String ads;
        private String desc;
        private String pid;
        private String price;
        private String prices;
        private String packing;
        private String term;
        private String storage;
        private int is_cell;
        private String stock;
        private String min_qty;
        //0:普通商品 1：预售 2：团购
        private String activity;
        //0:普通商品 1：预售 2：团购 3：秒杀
        private String activitys;
        //普通商品为0 其他为活动结束时间
        private String stoptime;
        private String activity_desc;
        private String team_count;
        private String groupon_id;
        private String starttime;
        private String activity_icon_img;
        private List<ContentBean> content = new ArrayList<>();
        private List<String> simg = new ArrayList<>();
        private List<AttrListBean> attr_list = new ArrayList<>();
        private List<AttrBean> attr = new ArrayList<>();
        private List<GroupPonsBean> groupons = new ArrayList<>();
        private List<TeamBean> team = new ArrayList<>();

        public String getActivity_icon_img() {
            return activity_icon_img;
        }

        public void setActivity_icon_img(String activity_icon_img) {
            this.activity_icon_img = activity_icon_img;
        }

        public String getStarttime() {
            return starttime;
        }

        public void setStarttime(String starttime) {
            this.starttime = starttime;
        }

        public String getGroupon_id() {
            return groupon_id;
        }

        public void setGroupon_id(String groupon_id) {
            this.groupon_id = groupon_id;
        }

        public String getTeam_count() {
            return team_count;
        }

        public void setTeam_count(String team_count) {
            this.team_count = team_count;
        }

        public List<GroupPonsBean> getGroupons() {
            return groupons;
        }

        public void setGroupons(List<GroupPonsBean> groupons) {
            this.groupons = groupons;
        }

        public List<TeamBean> getTeam() {
            return team;
        }

        public void setTeam(List<TeamBean> team) {
            this.team = team;
        }

        public String getActivity_desc() {
            return activity_desc;
        }

        public void setActivity_desc(String activity_desc) {
            this.activity_desc = activity_desc;
        }

        public String getActivity() {
            return activity;
        }

        public void setActivity(String activity) {
            this.activity = activity;
        }

        public String getActivitys() {
            return activitys;
        }

        public void setActivitys(String activitys) {
            this.activitys = activitys;
        }

        public String getStoptime() {
            return stoptime;
        }

        public void setStoptime(String stoptime) {
            this.stoptime = stoptime;
        }

        public String getMin_qty() {
            return min_qty;
        }

        public void setMin_qty(String min_qty) {
            this.min_qty = min_qty;
        }

        public String getStock() {
            return stock;
        }

        public void setStock(String stock) {
            this.stock = stock;
        }

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

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
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

        public String getPid() {
            return pid;
        }

        public void setPid(String pid) {
            this.pid = pid;
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

        public String getPacking() {
            return packing;
        }

        public void setPacking(String packing) {
            this.packing = packing;
        }

        public String getTerm() {
            return term;
        }

        public void setTerm(String term) {
            this.term = term;
        }

        public String getStorage() {
            return storage;
        }

        public void setStorage(String storage) {
            this.storage = storage;
        }

        public int getIs_cell() {
            return is_cell;
        }

        public void setIs_cell(int is_cell) {
            this.is_cell = is_cell;
        }

        public List<ContentBean> getContent() {
            return content;
        }

        public void setContent(List<ContentBean> content) {
            this.content = content;
        }

        public List<String> getSimg() {
            return simg;
        }

        public void setSimg(List<String> simg) {
            this.simg = simg;
        }

        public List<AttrListBean> getAttr_list() {
            return attr_list;
        }

        public void setAttr_list(List<AttrListBean> attr_list) {
            this.attr_list = attr_list;
        }

        public List<AttrBean> getAttr() {
            return attr;
        }

        public void setAttr(List<AttrBean> attr) {
            this.attr = attr;
        }

        public static class ContentBean {
            /**
             * simg : http://zhaolin-10029121.image.myqcloud.com/sample1489646979784566
             * w : 1024
             * h : 1024
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

        public static class GroupPonsBean{
            String num;
            String price;

            public String getNum() {
                return num;
            }

            public void setNum(String num) {
                this.num = num;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }
        }

        public static class AttrListBean {
            /**
             * id : 410
             * title : 啤酒品类
             * catid : [{"id":"411","title":"淡爽","is_select":1,"goods_id":"9503847","is_state":"2"},{"id":"412","title":"纯生","is_select":2,"goods_id":"9503848","is_state":2}]
             */

            private String id;
            private String title;
            private List<CatidBean> catid;

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

            public List<CatidBean> getCatid() {
                return catid;
            }

            public void setCatid(List<CatidBean> catid) {
                this.catid = catid;
            }

            public static class CatidBean {
                /**
                 * id : 411
                 * title : 淡爽
                 * is_select : 1
                 * goods_id : 9503847
                 * is_state : 2
                 */

                private String id;
                private String title;
                private int is_select;
                private String goods_id;
                private String is_state;

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

                public int getIs_select() {
                    return is_select;
                }

                public void setIs_select(int is_select) {
                    this.is_select = is_select;
                }

                public String getGoods_id() {
                    return goods_id;
                }

                public void setGoods_id(String goods_id) {
                    this.goods_id = goods_id;
                }

                public String getIs_state() {
                    return is_state;
                }

                public void setIs_state(String is_state) {
                    this.is_state = is_state;
                }
            }
        }

        public static class AttrBean {
            /**
             * k : 啤酒品类
             * v : 纯生
             */

            private String k;
            private String v;

            public String getK() {
                return k;
            }

            public void setK(String k) {
                this.k = k;
            }

            public String getV() {
                return v;
            }

            public void setV(String v) {
                this.v = v;
            }
        }
    }
}
