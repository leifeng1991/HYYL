package com.xxzlkj.huayiyanglao.model;

/**
 * 描述:
 *
 * @author zhangrq
 *         2017/4/12 14:27
 */
public class ClassifyItemItemBean {
    private boolean isEdit;
    private int state;// 1已在我的应用，0，未在我的应用里面
    /**
     * id : 7
     * pid : 425
     * simg : http://zhaolin-10029121.image.myqcloud.com/sample14916383087985
     * state : 0
     * title : 日常保洁
     */

    private String id;
    private String pid;
    private String simg;
    private String title;
    private String type;
    private String to;

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

    public boolean isEdit() {
        return isEdit;
    }

    public void setEdit(boolean edit) {
        isEdit = edit;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getSimg() {
        return simg;
    }

    public void setSimg(String simg) {
        this.simg = simg;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public boolean equals(Object obj) {
        if (title != null && obj instanceof ClassifyItemItemBean) {
            return title.equals(((ClassifyItemItemBean) obj).getTitle());
        } else
            return super.equals(obj);
    }
}
