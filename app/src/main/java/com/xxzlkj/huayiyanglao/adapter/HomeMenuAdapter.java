package com.xxzlkj.huayiyanglao.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xxzlkj.huayiyanglao.R;
import com.xxzlkj.huayiyanglao.model.HomeIndexBean;
import com.xxzlkj.zhaolinshare.base.base.BaseAdapter;
import com.xxzlkj.zhaolinshare.base.base.BaseViewHolder;
import com.xxzlkj.zhaolinshare.base.util.PicassoUtils;
import com.xxzlkj.zhaolinshare.base.util.PixelUtil;


/**
 * 首页菜单
 */
public class HomeMenuAdapter extends BaseAdapter<HomeIndexBean.DataBean.MenuBean> {
    private int mWidth;

    public HomeMenuAdapter(Context context, int itemId) {
        super(context, itemId);
        mWidth = PixelUtil.getScreenWidth(context);
    }

    @Override
    public void convert(BaseViewHolder holder, int position, HomeIndexBean.DataBean.MenuBean itemBean) {
        RelativeLayout mRelativeLayout = holder.getView(R.id.id_property_parent);
        mRelativeLayout.getLayoutParams().width = (int) (mWidth / 4.0);

        ImageView mImageView = holder.getView(R.id.id_property_item_image);
        TextView mTextView = holder.getView(R.id.id_property_item_title);
        // 设置值
        PicassoUtils.setImageBig(mContext, itemBean.getSimg(), mImageView);
        mTextView.setText(itemBean.getTitle());
    }
}
