package com.xxzlkj.huayiyanglao.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xxzlkj.huayiyanglao.R;
import com.xxzlkj.huayiyanglao.model.ContactItemBean;
import com.xxzlkj.zhaolinshare.base.base.BaseAdapter;
import com.xxzlkj.zhaolinshare.base.base.BaseViewHolder;
import com.xxzlkj.zhaolinshare.base.util.PicassoUtils;


public class ContactItemAdapter extends BaseAdapter<ContactItemBean> {

    private final boolean isSelectFriend;

    public ContactItemAdapter(Context context, int itemId, boolean isSelectFriend) {
        super(context, itemId);
        this.isSelectFriend = isSelectFriend;
    }

    @Override
    public void convert(BaseViewHolder holder, int position, ContactItemBean itemBean) {
        ImageView iv_selected = holder.getView(R.id.iv_selected);// 是否选中
        ImageView iv_avatar = holder.getView(R.id.iv_avatar);// 头像
        TextView tv_name = holder.getView(R.id.tv_name);// 昵称
        // 设置是否选中
        if (isSelectFriend) {
            // 选择好友页面，设置是否选中
            iv_selected.setVisibility(View.VISIBLE);
            iv_selected.setImageResource(itemBean.isSelected() ? R.mipmap.ic_selected_yellow : R.mipmap.ic_normal_white);
        } else
            // 普通页面，没有是否选中
            iv_selected.setVisibility(View.GONE);

        // 设置内容
        PicassoUtils.setImageSmall(mContext, itemBean.getSimg(), iv_avatar);// 头像
        tv_name.setText(itemBean.getUsername());// 昵称
    }

}
