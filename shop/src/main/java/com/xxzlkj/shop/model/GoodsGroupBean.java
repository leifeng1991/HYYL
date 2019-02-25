package com.xxzlkj.shop.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 描述:
 *
 * @author leifeng
 *         2017/11/18 13:55
 */


public class GoodsGroupBean implements Serializable{
    /**
     * id : 436
     * title : 酒水
     * pid : 434
     * level : 2
     * simg :
     * count
     * child : [{"id":"437","title":"啤酒","pid":"436","level":"3","simg":"http://zhaolin-10029121.image.myqcloud.com/sample1488620945806364"},{"id":"438","title":"矿泉水","pid":"436","level":"3","simg":"http://zhaolin-10029121.image.myqcloud.com/sample1488620970974798"}]
     */

    private String id;
    private String title;
    private String pid;
    private String level;
    private String simg;
    private List<Goods> child = new ArrayList<>();
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

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getSimg() {
        return simg;
    }

    public void setSimg(String simg) {
        this.simg = simg;
    }

    public List<Goods> getChild() {
        return child;
    }

    public void setChild(List<Goods> child) {
        this.child = child;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }
}
