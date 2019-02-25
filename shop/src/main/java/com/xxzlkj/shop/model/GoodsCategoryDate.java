package com.xxzlkj.shop.model;

import com.xxzlkj.zhaolinshare.base.model.BaseBean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 分类右侧页面Bean（二级分类）
 * Created by Administrator on 2017/3/16.
 */

public class GoodsCategoryDate extends BaseBean{

    /**
     * data : {"banner":{"simg":"http://zhaolin-10029121.image.myqcloud.com/sample148939920267486","type":"2","to":"9503848"},"top_goods":[],"top_group":[{"id":"437","title":"啤酒","simg":"http://zhaolin-10029121.image.myqcloud.com/sample1488620945806364"}],"group":[{"id":"436","title":"酒水","pid":"434","level":"2","simg":"","child":[{"id":"437","title":"啤酒","pid":"436","level":"3","simg":"http://zhaolin-10029121.image.myqcloud.com/sample1488620945806364"},{"id":"438","title":"矿泉水","pid":"436","level":"3","simg":"http://zhaolin-10029121.image.myqcloud.com/sample1488620970974798"}]},{"id":"439","title":"饮料","pid":"434","level":"2","simg":"","child":[{"id":"442","title":"凉茶","pid":"439","level":"3","simg":"http://zhaolin-10029121.image.myqcloud.com/sample1488619014315449"}]}]}
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
         * banner : {"simg":"http://zhaolin-10029121.image.myqcloud.com/sample148939920267486","type":"2","to":"9503848"}
         * top_goods : []
         * top_group : [{"id":"437","title":"啤酒","simg":"http://zhaolin-10029121.image.myqcloud.com/sample1488620945806364"}]
         * group : [{"id":"436","title":"酒水","pid":"434","level":"2","simg":"","child":[{"id":"437","title":"啤酒","pid":"436","level":"3","simg":"http://zhaolin-10029121.image.myqcloud.com/sample1488620945806364"},{"id":"438","title":"矿泉水","pid":"436","level":"3","simg":"http://zhaolin-10029121.image.myqcloud.com/sample1488620970974798"}]},{"id":"439","title":"饮料","pid":"434","level":"2","simg":"","child":[{"id":"442","title":"凉茶","pid":"439","level":"3","simg":"http://zhaolin-10029121.image.myqcloud.com/sample1488619014315449"}]}]
         */

        private BannerBean banner = new BannerBean();
        private TopGoodsBean top_goods = new TopGoodsBean();
        private List<Goods> top_group = new ArrayList<>();
        private List<GoodsGroupBean> group = new ArrayList<>();

        public BannerBean getBanner() {
            return banner;
        }

        public void setBanner(BannerBean banner) {
            this.banner = banner;
        }

        public TopGoodsBean getTop_goods() {
            return top_goods;
        }

        public void setTop_goods(TopGoodsBean top_goods) {
            this.top_goods = top_goods;
        }

        public List<Goods> getTop_group() {
            return top_group;
        }

        public void setTop_group(List<Goods> top_group) {
            this.top_group = top_group;
        }

        public List<GoodsGroupBean> getGroup() {
            return group;
        }

        public void setGroup(List<GoodsGroupBean> group) {
            this.group = group;
        }

        /**
         * 广告图
         */
        public static class BannerBean implements Serializable{
            /**
             * simg : http://zhaolin-10029121.image.myqcloud.com/sample148939920267486
             * type : 2
             * to : 9503848
             */

            private String simg;
            private String type;
            private String to;

            public String getSimg() {
                return simg;
            }

            public void setSimg(String simg) {
                this.simg = simg;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getTo() {
                return to;
            }

            public void setTo(String to) {
                this.to = to;
            }
        }

        /**
         * 广告图
         */
        public static class TopGoodsBean implements Serializable{
            /**
             * title :
             * list :
             */

            private String title;
            private List<Goods> list = new ArrayList<>();

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public List<Goods> getList() {
                return list;
            }

            public void setList(List<Goods> list) {
                this.list = list;
            }
        }

    }

}
