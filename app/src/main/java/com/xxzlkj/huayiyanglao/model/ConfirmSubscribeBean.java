package com.xxzlkj.huayiyanglao.model;

import java.io.Serializable;

/**
 * 描述:
 *
 * @author leifeng
 *         2018/3/9 14:49
 */


public class ConfirmSubscribeBean implements Serializable{
    private String id;// 产品id
    private String service_project;// 服务项目
    private String starttime;// 预约时间
    private String endtime;// 结束时间
    private String service_point;// 服务点
    private String service_number;// 服务好
    private String service_cost;// 服务费用
    private String number_id;// 预约按人数时间段id
    private String res_type;//1：按时间段 2按人数

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getService_project() {
        return service_project;
    }

    public void setService_project(String service_project) {
        this.service_project = service_project;
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

    public String getService_point() {
        return service_point;
    }

    public void setService_point(String service_point) {
        this.service_point = service_point;
    }

    public String getService_number() {
        return service_number;
    }

    public void setService_number(String service_number) {
        this.service_number = service_number;
    }

    public String getService_cost() {
        return service_cost;
    }

    public void setService_cost(String service_cost) {
        this.service_cost = service_cost;
    }

    public String getNumber_id() {
        return number_id;
    }

    public void setNumber_id(String number_id) {
        this.number_id = number_id;
    }

    public String getRes_type() {
        return res_type;
    }

    public void setRes_type(String res_type) {
        this.res_type = res_type;
    }
}
