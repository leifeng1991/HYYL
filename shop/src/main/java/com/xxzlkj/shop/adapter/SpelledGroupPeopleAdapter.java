package com.xxzlkj.shop.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.xxzlkj.shop.R;
import com.xxzlkj.shop.model.SpelledGroupDesBean;
import com.xxzlkj.zhaolinshare.base.base.BaseAdapter;
import com.xxzlkj.zhaolinshare.base.base.BaseViewHolder;
import com.xxzlkj.zhaolinshare.base.util.PicassoUtils;


/**
 * 描述:
 *
 * @author zhangrq
 *         2017/11/14 11:02
 */
public class SpelledGroupPeopleAdapter extends BaseAdapter<SpelledGroupDesBean.DataBean.TeamUserBean> {
    public SpelledGroupPeopleAdapter(Context context, int itemId) {
        super(context, itemId);
    }

    @Override
    public void convert(BaseViewHolder holder, int position, SpelledGroupDesBean.DataBean.TeamUserBean itemBean) {
        ImageView iv_people_image = holder.getView(R.id.iv_people_image);// 图片
        View vg_colonel = holder.getView(R.id.vg_colonel);// 团长
        // 设置头像
        String id = itemBean.getId();
        if (TextUtils.isEmpty(id)) {
            // 未添加的
            iv_people_image.setImageResource(R.mipmap.ic_no_add_people);
        } else {
            // 已添加
            String simg = itemBean.getSimg();
            if (!TextUtils.isEmpty(simg))
                // 有头像的
                PicassoUtils.setImageSmall(mContext, simg, iv_people_image);
            else
                // 没有头像的
                iv_people_image.setImageResource(R.mipmap.default_icon2);

        }
        // 设置是否是团长
        vg_colonel.setVisibility("1".equals(itemBean.getIs_top()) ? View.VISIBLE : View.GONE);

    }
}
