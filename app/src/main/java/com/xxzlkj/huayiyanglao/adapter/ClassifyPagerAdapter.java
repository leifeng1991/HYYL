package com.xxzlkj.huayiyanglao.adapter;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.xxzlkj.huayiyanglao.R;
import com.xxzlkj.huayiyanglao.event.ClassifyAnimationEvent;
import com.xxzlkj.huayiyanglao.event.ClassifyMyAppAddEvent;
import com.xxzlkj.huayiyanglao.event.OtherServersChangeEvent;
import com.xxzlkj.huayiyanglao.model.ClassifyItemItemBean;
import com.xxzlkj.huayiyanglao.util.ZLActivityUtils;
import com.xxzlkj.zhaolinshare.base.util.PicassoUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;


/**
 * 描述:
 *
 * @author zhangrq
 *         2017/4/12 10:59
 */
public class ClassifyPagerAdapter extends PagerAdapter {
    private final Activity mActivity;
    private List<ClassifyItemItemBean> list;
    private int mChildCount;

    public ClassifyPagerAdapter(Activity mActivity, List<ClassifyItemItemBean> listViews) {
        if (listViews == null) {
            listViews = new ArrayList<>();
        }
        this.list = listViews;
        this.mActivity = mActivity;
    }

    @Override
    public int getCount() {
        return (list.size() + 3) / 4;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View inflate = View.inflate(container.getContext(), R.layout.item_recent_app, null);
        LinearLayout ll_root = (LinearLayout) inflate.findViewById(R.id.ll_root);

        for (int i = position * 4; i < (position + 1) * 4; i++) {
            if (i >= list.size())
                continue;
            final RelativeLayout childAt = (RelativeLayout) ll_root.getChildAt(i % 4);
            childAt.setVisibility(View.VISIBLE);

            ImageView iv_classify_top_item = (ImageView) childAt.findViewById(R.id.iv_classify_top_item);
            TextView tv_classify_top_item = (TextView) childAt.findViewById(R.id.tv_classify_top_item);
            final ImageView iv_classify_top_item_right = (ImageView) childAt.findViewById(R.id.iv_classify_top_item_right);
            // 设置值
            iv_classify_top_item_right.setVisibility(View.VISIBLE);
            final ClassifyItemItemBean bean = list.get(i);
            tv_classify_top_item.setText(bean.getTitle());// 标题名
            PicassoUtils.setImageSmall(container.getContext(), bean.getSimg(), iv_classify_top_item);//图片
            if (bean.isEdit()) {
                // 编辑页面
                // 1已添加，0，未添加
                if (bean.getState() == 1) {
                    // 已添加，显示对勾
                    iv_classify_top_item_right.setClickable(false);
                    iv_classify_top_item_right.setImageResource(R.mipmap.ic_classify_ok);
                } else if (bean.getState() == 0) {
                    // 未添加，显示加好
                    iv_classify_top_item_right.setImageResource(R.mipmap.ic_classify_add);
                    iv_classify_top_item_right.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // 设置改变
                            iv_classify_top_item_right.setImageResource(R.mipmap.ic_classify_ok);
                            bean.setState(1);

                            // 发送通知
                            EventBus.getDefault().postSticky(new ClassifyAnimationEvent(childAt, false));
                            EventBus.getDefault().postSticky(new ClassifyMyAppAddEvent(bean));//通知自己
                            EventBus.getDefault().postSticky(new OtherServersChangeEvent(bean));//通知其它服务
                        }
                    });
                }
            } else {
                // 不是编辑页面。不显示
                iv_classify_top_item_right.setVisibility(View.INVISIBLE);
            }

            // 设置点击
            childAt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 根据id，跳转
                    ZLActivityUtils.jumpToActivityByType(mActivity, bean.getType(), bean.getTo(), bean.getTitle());
                }
            });
        }


        container.addView(inflate);
        return inflate;
    }

    @Override
    public void notifyDataSetChanged() {
        mChildCount = getCount();
        super.notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(Object object) {
        if (mChildCount > 0) {
            mChildCount--;
            return POSITION_NONE;
        }
        return super.getItemPosition(object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
