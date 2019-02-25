package com.xxzlkj.shop.model;

import com.xxzlkj.zhaolinshare.base.model.BaseBean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 描述:
 *
 * @author zhangrq
 *         2017/4/19 11:33
 */
public class SelectCouponsBean extends BaseBean {

    /**
     * data : {"usable":[],"disable":[{"id":"100015","uid":"111380","type":"2","sort":"1","marketid":"0","filled":"0","discount":"10","starttime":"1497490003","endtime":"1497576409","addtime":"1497494124","title":"仅限商城商品使用"}]}
     */

    private DataBean data = new DataBean();

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private List<ItemBean> usable = new ArrayList<>();
        private List<ItemBean> disable = new ArrayList<>();

        public List<ItemBean> getUsable() {
            return usable;
        }

        public void setUsable(List<ItemBean> usable) {
            this.usable = usable;
        }

        public List<ItemBean> getDisable() {
            return disable;
        }

        public void setDisable(List<ItemBean> disable) {
            this.disable = disable;
        }

        public static class ItemBean implements Serializable{
            /**
             * id : 100015
             * uid : 111380
             * type : 2
             * sort : 1
             * marketid : 0
             * filled : 0
             * discount : 10
             * starttime : 1497490003
             * endtime : 1497576409
             * addtime : 1497494124
             * title : 仅限商城商品使用
             */

            private String id;
            private String uid;
            private String type;
            private String sort;
            private String marketid;
            private String filled;
            private String discount;
            private String starttime;
            private String endtime;
            private String addtime;
            private String title;
            private boolean isChecked;

            @Override
            public boolean equals(Object obj) {
                if (obj instanceof ItemBean && ((ItemBean) obj).getId() != null) {
                    return ((ItemBean) obj).getId().equals(id);
                }
                return super.equals(obj);
            }

            public boolean isChecked() {
                return isChecked;
            }

            public void setChecked(boolean checked) {
                isChecked = checked;
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

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getSort() {
                return sort;
            }

            public void setSort(String sort) {
                this.sort = sort;
            }

            public String getMarketid() {
                return marketid;
            }

            public void setMarketid(String marketid) {
                this.marketid = marketid;
            }

            public String getFilled() {
                return filled;
            }

            public void setFilled(String filled) {
                this.filled = filled;
            }

            public String getDiscount() {
                return discount;
            }

            public void setDiscount(String discount) {
                this.discount = discount;
            }

            public String getStarttime() {
                return starttime;
            }

            public void setStarttime(String starttime) {
                this.starttime = starttime;
            }

            public String getEndtime() {
                return endtime;
            }

            public void setEndtime(String endtime) {
                this.endtime = endtime;
            }

            public String getAddtime() {
                return addtime;
            }

            public void setAddtime(String addtime) {
                this.addtime = addtime;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }
        }
    }
}
