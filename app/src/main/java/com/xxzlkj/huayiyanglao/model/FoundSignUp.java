package com.xxzlkj.huayiyanglao.model;

import com.xxzlkj.zhaolinshare.base.model.BaseBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 已经报名活动用户
 * Created by Administrator on 2017/4/24.
 */

public class FoundSignUp extends BaseBean{

    private List<SignUpUser> data = new ArrayList<>();

    public List<SignUpUser> getData() {
        return data;
    }

    public void setData(List<SignUpUser> data) {
        this.data = data;
    }

}
