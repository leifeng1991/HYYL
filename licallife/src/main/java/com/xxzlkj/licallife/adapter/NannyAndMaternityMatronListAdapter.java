package com.xxzlkj.licallife.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xxzlkj.licallife.R;
import com.xxzlkj.licallife.model.NannyAndMaternityMatronListBean;
import com.xxzlkj.zhaolinshare.base.base.BaseAdapter;
import com.xxzlkj.zhaolinshare.base.base.BaseViewHolder;
import com.xxzlkj.zhaolinshare.base.util.NumberFormatUtils;
import com.xxzlkj.zhaolinshare.base.util.PicassoUtils;
import com.xxzlkj.zhaolinshare.base.util.StringUtil;
import com.xxzlkj.zhaolinshare.base.util.TextViewUtils;


/**
 * 描述: 保姆或月嫂列表adapter
 *
 * @author zhangrq
 *         2017/7/25 15:06
 */
public class NannyAndMaternityMatronListAdapter extends BaseAdapter<NannyAndMaternityMatronListBean.DataBean> {

    public NannyAndMaternityMatronListAdapter(Context context, int itemId) {
        super(context, itemId);
    }

    @Override
    public void convert(BaseViewHolder holder, int position, NannyAndMaternityMatronListBean.DataBean itemBean) {
        ImageView iv_icon = holder.getView(R.id.iv_icon);// 头像
        TextView tv_title = holder.getView(R.id.tv_title);// 标题
        TextView tv_praise = holder.getView(R.id.tv_praise);// 好评
        TextView tv_time = holder.getView(R.id.tv_time);// 在岗时间
        LinearLayout ll_tabs = holder.getView(R.id.ll_tabs);// 所有标签
        TextView tv_tab1 = holder.getView(R.id.tv_tab1);// 标签1
        TextView tv_tab2 = holder.getView(R.id.tv_tab2);// 标签2
        TextView tv_tab3 = holder.getView(R.id.tv_tab3);// 标签3
        TextView tv_money = holder.getView(R.id.tv_money);// 每月价格
//        id	月嫂id
//        name	姓名
//        simg	头像
//        price	每月价格
//        onduty	在岗天数
//        label	标签
        // 设置值
        PicassoUtils.setImageBig(mContext, itemBean.getSimg(), iv_icon);// 头像
        tv_title.setText(itemBean.getName());// 标题
        TextViewUtils.setTextHasValue(tv_praise, itemBean.getPraise(), "%（好评）");// 好评
        TextViewUtils.setTextHasValue(tv_time, itemBean.getOnduty(), "天（在岗时间）");// 在岗时间
        // 标签
        String label = itemBean.getLabel();
        if (TextUtils.isEmpty(label)) {
            // 无标签
            ll_tabs.setVisibility(View.GONE);
        } else {
            // 有标签
            ll_tabs.setVisibility(View.VISIBLE);
            String[] split = label.split(",");
            // 标签1
            tv_tab1.setVisibility(split.length > 0 ? View.VISIBLE : View.GONE);
            tv_tab1.setText(split.length > 0 ? split[0] : null);
            // 标签2
            tv_tab2.setVisibility(split.length > 1 ? View.VISIBLE : View.GONE);
            tv_tab2.setText(split.length > 1 ? split[1] : null);
            // 标签3
            tv_tab3.setVisibility(split.length > 2 ? View.VISIBLE : View.GONE);
            tv_tab3.setText(split.length > 2 ? split[2] : null);
        }
        // 价格
        double money = NumberFormatUtils.toDouble(itemBean.getPrice()) * 2;
        TextViewUtils.setTextHasValue(tv_money, StringUtil.saveTwoDecimal(String.valueOf(money)), "/月");// 月薪

    }
}
