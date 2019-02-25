package com.xxzlkj.licallife.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xxzlkj.licallife.R;
import com.xxzlkj.licallife.model.NannyAndMaternityMatronDesTabBean;
import com.xxzlkj.zhaolinshare.base.base.BaseAdapter;
import com.xxzlkj.zhaolinshare.base.base.BaseViewHolder;
import com.xxzlkj.zhaolinshare.base.util.PixelUtil;


/**
 * 描述: 订单adapter
 *
 * @author zhangrq
 *         2016/12/12 11:06
 */

public class NannyAndMaternityMatronDesTabAdapter extends BaseAdapter<NannyAndMaternityMatronDesTabBean> {

    private final int paddingTop;

    public NannyAndMaternityMatronDesTabAdapter(Context context, int itemId) {
        super(context, itemId);
        paddingTop = PixelUtil.dip2px(mContext, 20);
    }

    @Override
    public void convert(BaseViewHolder holder, int position, final NannyAndMaternityMatronDesTabBean itemBean) {
        RelativeLayout rl_content = holder.getView(R.id.rl_content);// 布局
        TextView tv_title_left = holder.getView(R.id.tv_title_left);// 左边Title
        TextView tv_value_left = holder.getView(R.id.tv_value_left);// 左边内容
        View iv_right_point = holder.getView(R.id.iv_right_point);// 右边的点
        TextView tv_title_right = holder.getView(R.id.tv_title_right);// 右边Title
        TextView tv_value_right = holder.getView(R.id.tv_value_right);// 右边内容
        // 初始化
        rl_content.setPadding(rl_content.getPaddingLeft(), position == 0 ? 0 : paddingTop, rl_content.getPaddingRight(), rl_content.getPaddingBottom());

        // 设置值
        tv_title_left.setText(itemBean.getLeftTitle());
        tv_value_left.setText(itemBean.getLeftValue());

        // 右边的内容和标题都为空，不显示点，内容可以设置null，和不显示一个效果
        iv_right_point.setVisibility(TextUtils.isEmpty(itemBean.getRightTitle()) && TextUtils.isEmpty(itemBean.getRightValue()) ? View.INVISIBLE : View.VISIBLE);
        tv_title_right.setText(itemBean.getRightTitle());
        tv_value_right.setText(itemBean.getRightValue());
    }

}


