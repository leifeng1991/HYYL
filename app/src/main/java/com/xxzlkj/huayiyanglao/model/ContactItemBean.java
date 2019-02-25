package com.xxzlkj.huayiyanglao.model;

import java.io.Serializable;

/**
 * 描述:
 *
 * @author zhangrq
 *         2017/10/24 9:59
 */
public class ContactItemBean implements Serializable {
    /**
     * age : 18
     * birthday : 936806400
     * id : 111346
     * prefix : L
     * sex : 2
     * simg : http://zhaolin-10029121.image.myqcloud.com/sample1493868934023
     * type : 0
     * username : 李奇不不不跨步不我额
     */

    private String age;
    private String birthday;
    private String id;
    private String prefix;
    private String sex;
    private String simg;
    private String type;
    private String username;
    private boolean isSelected;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

