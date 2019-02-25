package com.xxzlkj.huayiyanglao.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.TextView;

import com.xxzlkj.huayiyanglao.R;
import com.xxzlkj.huayiyanglao.model.UserFriendListBean;
import com.xxzlkj.zhaolinshare.base.base.BaseAdapter;
import com.xxzlkj.zhaolinshare.base.base.BaseViewHolder;

import java.util.List;

public class ContactTabAdapter extends BaseAdapter<UserFriendListBean.DataBean.GroupBean> {

    private final boolean isSelectFriend;
    private final Activity mActivity;

    public ContactTabAdapter(Activity mActivity, Context context, int itemId, boolean isSelectFriend) {
        super(context, itemId);
        this.mActivity = mActivity;
        this.isSelectFriend = isSelectFriend;
    }

    @Override
    public void convert(BaseViewHolder holder, int position, UserFriendListBean.DataBean.GroupBean itemBean) {
        TextView tv_title = holder.getView(R.id.tv_title);
        RecyclerView rv_content = holder.getView(R.id.rv_content);
        // 设置内容
        tv_title.setText(itemBean.getTitle());

        rv_content.setLayoutManager(new LinearLayoutManager(mContext));
        rv_content.setNestedScrollingEnabled(false);
        ContactItemAdapter contactItemAdapter = new ContactItemAdapter(mContext, R.layout.item_contact, isSelectFriend);
        rv_content.setAdapter(contactItemAdapter);
        contactItemAdapter.clearAndAddList(itemBean.getList());
        // 设置点击
        contactItemAdapter.setOnItemClickListener((position1, item) -> {
            if (isSelectFriend) {
                // 选择好友页面，设置选中、取消选中
                item.setSelected(!item.isSelected());
                contactItemAdapter.notifyDataSetChanged();
            } else {
                // 普通页面
//                mActivity.startActivity(PersonMainActivity.newIntent(mContext, 1, item.getId()));
            }
        });
    }

    /**
     * 根据英文字母，获取内容数据列表的位置，如果没有返回-1
     */
    public int getPositionForSection(char letter) {
        if (letter != 0 && getItemCount() > 0) {
            List<UserFriendListBean.DataBean.GroupBean> list = getList();
            for (int i = 0; i < list.size(); i++) {
                UserFriendListBean.DataBean.GroupBean groupBean = list.get(i);
                String title = groupBean.getTitle();
                if (!TextUtils.isEmpty(title) && title.charAt(0) == letter) {
                    return i;
                }
            }
        }
        return -1;
    }
}
