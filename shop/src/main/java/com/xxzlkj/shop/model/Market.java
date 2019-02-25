package com.xxzlkj.shop.model;

import com.xxzlkj.zhaolinshare.base.model.BaseBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 果蔬生鲜
 * Created by Administrator on 2017/3/25.
 */

public class Market extends BaseBean{
    /**
     * id : 48
     * simg : http://zhaolin-10029121.image.myqcloud.com/sample149026406964407
     * type : 3
     * pid : 2
     * to : 437
     * goods : [{"id":"9503857","title":"百岁山","simg":"http://zhaolin-10029121.image.myqcloud.com/sample1489558730343895","price":"3.00","prices":"4.00"},{"id":"9503847","title":"雪花啤酒淡爽 500ml","simg":"http://zhaolin-10029121.image.myqcloud.com/sample148913472866691","price":"5.00","prices":"7.00"},{"id":"9503846","title":"雪花啤酒纯生 350ml","simg":"http://zhaolin-10029121.image.myqcloud.com/sample1489134727973815","price":"5.00","prices":"7.00"}]
     */

    private String id;
    private String simg;
    private String type;
    private int pid;
    private String to;
    private String icon;// v-icon : "http://zhaolin-10029121.image.myqcloud.com/sample1496629637650814"
    private String title;//v-title : "水果"
    private List<Goods> goods=new ArrayList<>();

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public List<Goods> getGoods() {
        return goods;
    }

    public void setGoods(List<Goods> goods) {
        this.goods = goods;
    }
}
