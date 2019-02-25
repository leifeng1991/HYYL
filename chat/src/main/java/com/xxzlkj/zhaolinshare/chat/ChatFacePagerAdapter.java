package com.xxzlkj.zhaolinshare.chat;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述:
 *
 * @author zhangrq
 *         2017/4/18 10:07
 */
public class ChatFacePagerAdapter extends PagerAdapter {

    private List<EmoticonEntity> list;

    public ChatFacePagerAdapter(List<EmoticonEntity> listViews) {
        if (listViews == null) {
            listViews = new ArrayList<>();
        }
        this.list = listViews;
    }

    @Override
    public int getCount() {
        return (list.size() + 20) / 21;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(final ViewGroup container, int position) {
        View inflate = View.inflate(container.getContext(), R.layout.item_chat_view_pager_item, null);
        GridView gv_chat_face_content = (GridView) inflate.findViewById(R.id.gv_chat_face_content);
        final List<EmoticonEntity> emoticonEntities = new ArrayList<>();
        for (int i = position * 21; i < (position + 1) * 21; i++) {
            if (i >= list.size())
                continue;
            emoticonEntities.add(list.get(i));
        }
        gv_chat_face_content.setAdapter(new BaseCommonAdapter<EmoticonEntity>(container.getContext(), emoticonEntities, R.layout.item_chat_face_item) {
            @Override
            public void convert(BaseCommonViewHolder holder, final EmoticonEntity item, int position) {
                RelativeLayout rl_root = holder.getView(R.id.rl_root);
                ImageView iv_chat_face = holder.getView(R.id.iv_chat_face);

                ViewGroup.LayoutParams layoutParams = rl_root.getLayoutParams();
                layoutParams.height = container.getHeight() / 3;
                rl_root.setLayoutParams(layoutParams);
                // 设置值
                iv_chat_face.setImageResource(Integer.parseInt(item.getIconUri()));
                rl_root.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onFaceClickListener != null) {
                            onFaceClickListener.onFaceClick(item);
                        }
                    }
                });

            }
        });

        container.addView(inflate);
        return inflate;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    private OnFaceClickListener onFaceClickListener;

    interface OnFaceClickListener {
        void onFaceClick(EmoticonEntity entity);
    }

    public OnFaceClickListener getOnFaceClickListener() {
        return onFaceClickListener;
    }

    public void setOnFaceClickListener(OnFaceClickListener onFaceClickListener) {
        this.onFaceClickListener = onFaceClickListener;
    }
}
