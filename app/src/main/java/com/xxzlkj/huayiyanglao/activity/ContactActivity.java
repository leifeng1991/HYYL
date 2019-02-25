package com.xxzlkj.huayiyanglao.activity;


import com.xxzlkj.huayiyanglao.R;
import com.xxzlkj.huayiyanglao.fragment.ContactTabFragment;
import com.xxzlkj.huayiyanglao.model.ContactItemBean;
import com.xxzlkj.huayiyanglao.model.UserFriendListBean;
import com.xxzlkj.huayiyanglao.webView.SideBar;
import com.xxzlkj.zhaolinshare.base.base.MyBaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 通讯录
 *
 * @author zhangrq
 */
public class ContactActivity extends MyBaseActivity {

    private ContactTabFragment contactTabFragment;

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_contact_tab);
    }

    @Override
    protected void findViewById() {
        contactTabFragment = ContactTabFragment.newInstance(false);
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_content, contactTabFragment).commitAllowingStateLoss();
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void processLogic() {
        setTitleLeftBack();
        setTitleName("通讯录");

        // 设置假数据
      /*  List<UserFriendListBean.DataBean.GroupBean> groupBeanList = new ArrayList<>();
        String[] titleArrays = SideBar.b;
        for (String title : titleArrays) {
            // 拼接头
            UserFriendListBean.DataBean.GroupBean groupBean = new UserFriendListBean.DataBean.GroupBean(title);
            // 拼接内容
            ArrayList<ContactItemBean> list = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                ContactItemBean contactItemBean = new ContactItemBean();
                contactItemBean.setSimg("https://f10.baidu.com/it/u=2829694298,556438610&fm=72");
                contactItemBean.setUsername(title + i);
                list.add(contactItemBean);
            }
            groupBean.setList(list);
            groupBeanList.add(groupBean);
        }
        contactTabFragment.setData(groupBeanList);*/
    }

}
