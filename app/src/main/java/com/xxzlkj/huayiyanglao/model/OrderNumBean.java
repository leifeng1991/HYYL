package com.xxzlkj.huayiyanglao.model;

import com.xxzlkj.zhaolinshare.base.model.BaseBean;

/**
 * 描述:
 *
 * @author leifeng
 *         2017/12/2 15:00
 */


public class OrderNumBean extends BaseBean {

    /**
     * data : {"order":{"all":"147","pay":"41","send":"25","finish":"0","tui":"6"},"express":{},"cleaning":{"all":"13","send":"1","comment":"2"},"foods":{"all":"4","outer":"2","to":"4"},"activity":{"id":"6","title":"兆邻6.18手机卖场","simg":"http://zhaolin-10029121.image.myqcloud.com/sample1496828865524714","starttime":"1498816066","address":"0","pv":"67","cell":"2","count":"4"}}
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
         * order : {"all":"147","pay":"41","send":"25","finish":"0","tui":"6"}
         * express : {}
         * cleaning : {"all":"13","send":"1","comment":"2"}
         * foods : {"all":"4","outer":"2","to":"4"}
         * activity : {"id":"6","title":"兆邻6.18手机卖场","simg":"http://zhaolin-10029121.image.myqcloud.com/sample1496828865524714","starttime":"1498816066","address":"0","pv":"67","cell":"2","count":"4"}
         */

        private OrderBean order;
        private ExpressBean express;
        private CleaningBean cleaning;
        private FoodsBean foods;
        private ActivityBean activity;
        private HealthBean health;

        public OrderBean getOrder() {
            return order;
        }

        public void setOrder(OrderBean order) {
            this.order = order;
        }

        public ExpressBean getExpress() {
            return express;
        }

        public void setExpress(ExpressBean express) {
            this.express = express;
        }

        public CleaningBean getCleaning() {
            return cleaning;
        }

        public void setCleaning(CleaningBean cleaning) {
            this.cleaning = cleaning;
        }

        public FoodsBean getFoods() {
            return foods;
        }

        public void setFoods(FoodsBean foods) {
            this.foods = foods;
        }

        public ActivityBean getActivity() {
            return activity;
        }

        public void setActivity(ActivityBean activity) {
            this.activity = activity;
        }

        public HealthBean getHealth() {
            return health;
        }

        public void setHealth(HealthBean health) {
            this.health = health;
        }

        public static class OrderBean {
            /**
             * all : 147
             * pay : 41
             * send : 25
             * finish : 0
             * tui : 6
             */

            private String all;
            private String pay;
            private String send;
            private String finish;
            private String tui;

            public String getAll() {
                return all;
            }

            public void setAll(String all) {
                this.all = all;
            }

            public String getPay() {
                return pay;
            }

            public void setPay(String pay) {
                this.pay = pay;
            }

            public String getSend() {
                return send;
            }

            public void setSend(String send) {
                this.send = send;
            }

            public String getFinish() {
                return finish;
            }

            public void setFinish(String finish) {
                this.finish = finish;
            }

            public String getTui() {
                return tui;
            }

            public void setTui(String tui) {
                this.tui = tui;
            }
        }

        public static class ExpressBean {
        }

        public static class CleaningBean {
            /**
             * all : 13
             * send : 1
             * comment : 2
             */

            private String all;
            private String send;
            private String comment;

            public String getAll() {
                return all;
            }

            public void setAll(String all) {
                this.all = all;
            }

            public String getSend() {
                return send;
            }

            public void setSend(String send) {
                this.send = send;
            }

            public String getComment() {
                return comment;
            }

            public void setComment(String comment) {
                this.comment = comment;
            }
        }

        public static class FoodsBean {
            /**
             * all : 4
             * outer : 2
             * to : 4
             */

            private String all;
            private String outer;
            private String to;

            public String getAll() {
                return all;
            }

            public void setAll(String all) {
                this.all = all;
            }

            public String getOuter() {
                return outer;
            }

            public void setOuter(String outer) {
                this.outer = outer;
            }

            public String getTo() {
                return to;
            }

            public void setTo(String to) {
                this.to = to;
            }
        }

        public static class ActivityBean {
            /**
             * id : 6
             * title : 兆邻6.18手机卖场
             * simg : http://zhaolin-10029121.image.myqcloud.com/sample1496828865524714
             * starttime : 1498816066
             * address : 0
             * pv : 67
             * cell : 2
             * count : 4
             */

            private String id;
            private String title;
            private String simg;
            private String starttime;
            private String address;
            private String pv;
            private String cell;
            private String count;

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

            public String getSimg() {
                return simg;
            }

            public void setSimg(String simg) {
                this.simg = simg;
            }

            public String getStarttime() {
                return starttime;
            }

            public void setStarttime(String starttime) {
                this.starttime = starttime;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public String getPv() {
                return pv;
            }

            public void setPv(String pv) {
                this.pv = pv;
            }

            public String getCell() {
                return cell;
            }

            public void setCell(String cell) {
                this.cell = cell;
            }

            public String getCount() {
                return count;
            }

            public void setCount(String count) {
                this.count = count;
            }
        }

        public static class HealthBean {
            private String all;
            private String pay;
            private String send;
            private String finish;
            private String failure;

            public String getAll() {
                return all;
            }

            public void setAll(String all) {
                this.all = all;
            }

            public String getPay() {
                return pay;
            }

            public void setPay(String pay) {
                this.pay = pay;
            }

            public String getSend() {
                return send;
            }

            public void setSend(String send) {
                this.send = send;
            }

            public String getFinish() {
                return finish;
            }

            public void setFinish(String finish) {
                this.finish = finish;
            }

            public String getFailure() {
                return failure;
            }

            public void setFailure(String failure) {
                this.failure = failure;
            }
        }
    }
}
