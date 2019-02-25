package com.xxzlkj.huayiyanglao.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.xxzlkj.huayiyanglao.R;
import com.xxzlkj.huayiyanglao.model.GetCommunityListBean;
import com.xxzlkj.zhaolinshare.base.base.BaseAdapter;
import com.xxzlkj.zhaolinshare.base.base.BaseViewHolder;
import com.xxzlkj.zhaolinshare.base.util.PicassoUtils;


/**
 * 描述: 首页错误布局里面的--已开通小区
 *
 * @author zhangrq
 *         2016/12/12 11:06
 */

public class CommunityListAdapter extends BaseAdapter<GetCommunityListBean.DataBean> {

    public CommunityListAdapter(Context context, int itemId) {
        super(context, itemId);
    }

    @Override
    public void convert(BaseViewHolder holder, int position, final GetCommunityListBean.DataBean itemBean) {
        ImageView iv_image = holder.getView(R.id.iv_image);// 图片
        TextView tv_title = holder.getView(R.id.tv_title);// 小区名
        TextView tv_address_name = holder.getView(R.id.tv_address_name);// 地址
        TextView tv_open_server = holder.getView(R.id.tv_open_server);// 开通服务

        // 设置值
        PicassoUtils.setImageBig(mContext, itemBean.getSimg(), iv_image);
        tv_title.setText(itemBean.getTitle());
        tv_address_name.setText(String.valueOf("地址：" + itemBean.getAddress()));
        tv_open_server.setText(String.valueOf("已开通服务：" + itemBean.getService()));
    }

}


