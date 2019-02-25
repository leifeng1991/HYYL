package com.xxzlkj.shop.weight;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.xxzlkj.shop.R;
import com.xxzlkj.shop.adapter.GoodsAttrListAdapter;
import com.xxzlkj.shop.model.GoodsDetail;
import com.xxzlkj.zhaolinshare.base.util.BasePopupWindow;
import com.xxzlkj.zhaolinshare.base.util.PicassoUtils;

import java.util.List;

/**
 * 自定义popupwindow
 * Created by Administrator on 2017/4/14.
 */

public class CustomPopupWindow extends BasePopupWindow {
    private GoodsAttrListAdapter mAttrListAdapter;
    private TextView mPopupPrice;
    private TextView mPopupSerialNumber;
    private TextView mPopupNameTextView;
    private TextView mPopupNumberTextView;
    private ImageView mPopupImage;


    public CustomPopupWindow(Activity activity, AddShopCartListener listener, boolean intentFlag) {
        super(activity);
        createPopupWindow(activity, listener, intentFlag);
    }

    public CustomPopupWindow(Activity activity, String title, OnMyClickListener listener, List<String> mTypeList) {
        super(activity);
        createPopupWindow(activity, title, listener, mTypeList);
    }

    public void createPopupWindow(final Activity activity, String title, final OnMyClickListener listener, List<String> mTypeList) {
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewType = inflater.inflate(R.layout.popup_wiondow_wheel, null);
        final WheelView mTypeWheelView = (WheelView) viewType.findViewById(R.id.id_ww_wheel);

        viewType.findViewById(R.id.id_ww_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBgAlpha(activity, 1f);
                CustomPopupWindow.this.dismiss();
                if (listener == null) return;
                listener.cancelClickListener();
            }
        });
        viewType.findViewById(R.id.id_ww_sure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBgAlpha(activity, 1f);
                CustomPopupWindow.this.dismiss();
                if (listener == null) return;
                listener.sureClickListener(mTypeWheelView.getSeletedItem());
            }
        });

        mTypeWheelView.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                super.onSelected(selectedIndex, item);
            }
        });

        mTypeWheelView.setItems(mTypeList);

        TextView mViewTypeTitle = (TextView) viewType.findViewById(R.id.id_ww_title);
        mViewTypeTitle.setText(title);
        init(viewType, activity);
    }


    /**
     * 商品属性选择
     *
     * @param activity
     * @param listener
     * @param intentFlag 标记从那个界面跳进的 true:商品详情 false:购物车
     */
    private void createPopupWindow(final Activity activity, final AddShopCartListener listener, boolean intentFlag) {
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.popup_wiondow_add_layout, null);
        // 商品价格
        mPopupPrice = (TextView) view.findViewById(R.id.id_wdl_price);
        // 商品编号
        mPopupSerialNumber = (TextView) view.findViewById(R.id.id_wdl_serial_number);
        // 商品图片
        mPopupImage = (ImageView) view.findViewById(R.id.id_wdl_image);
        // 商品数量
        mPopupNumberTextView = (TextView) view.findViewById(R.id.id_wdl_number);
        // 加一按钮
        view.findViewById(R.id.id_wdl_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener == null) return;
                listener.addClick(mPopupNumberTextView);
            }
        });
        // 减一按钮
        view.findViewById(R.id.id_wdl_subtract).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener == null) return;
                listener.reduceClick(mPopupNumberTextView);
            }
        });
        // 商品名字
        mPopupNameTextView = (TextView) view.findViewById(R.id.id_wdl_name);
        // 确定按钮
        Button mSureBtn = (Button) view.findViewById(R.id.id_wdl_sure);
        mSureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomPopupWindow.this.dismiss();
                if (listener == null) return;
                listener.sureClick(mPopupNumberTextView);
            }
        });
        // 取消按钮
        ImageView mCancelImage = (ImageView) view.findViewById(R.id.id_wdl_cancle);
        mCancelImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomPopupWindow.this.dismiss();
            }
        });
        // 商品规格
        RecyclerView mAttrListRecyclerView = (RecyclerView) view.findViewById(R.id.id_wdl_recycler);
        mAttrListRecyclerView.setLayoutManager(new LinearLayoutManager(activity));
        mAttrListAdapter = new GoodsAttrListAdapter(activity, R.layout.adapter_attr_list, intentFlag);
        mAttrListRecyclerView.setAdapter(mAttrListAdapter);

        init(view, activity);
    }


    /**
     * 设置数据
     *
     * @param activity
     * @param goodsDetail
     */
    public void setPopupWindow(Activity activity, GoodsDetail goodsDetail) {
        if (goodsDetail != null && goodsDetail.getData().getAttr_list() != null) {
            if (mAttrListAdapter.getItemCount() > 0) {
                mAttrListAdapter.clear();
            }
            mAttrListAdapter.addList(goodsDetail.getData().getAttr_list());
            mPopupPrice.setText("￥" + goodsDetail.getData().getPrice());
            mPopupSerialNumber.setText("商品编号:" + goodsDetail.getData().getId());
            mPopupNameTextView.setText(goodsDetail.getData().getTitle());
            PicassoUtils.setImageBig(activity, goodsDetail.getData().getImg(), mPopupImage);
        }
    }

    public void setNumberText(String number) {
        mPopupNumberTextView.setText(number);
    }


    /**
     * 初始化popupWindow的一些数据
     *
     * @param view
     */
    private void init(View view, final Activity activity) {
        //设置PopupWindow的View
        this.setContentView(view);
        //设置PopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.FILL_PARENT);
        //设置PopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置PopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置PopupWindow弹出窗体动画效果
//        this.setAnimationStyle(R.style.popwindow_anim_style);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x00000000);
        //设置PopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        this.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setBgAlpha(activity, 1f);
            }
        });

    }

    public void showAtLocationBottom(Activity activity, int layoutId) {
        View view = activity.findViewById(layoutId);
        this.showAtLocation(view, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

        // 设置背景颜色变暗
        setBgAlpha(activity, 0.4f);
        getContentView().startAnimation(AnimationUtils.loadAnimation(activity.getApplicationContext(), R.anim.popupwindow_show));
    }

    @Override
    public void dismiss() {
        Animation animation = AnimationUtils.loadAnimation(getContentView().getContext(), R.anim.popupwindow_hide);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                CustomPopupWindow.super.dismiss();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        getContentView().startAnimation(animation);
    }

    /**
     * 设置背景透明度
     *
     * @param activity
     */
    private void setBgAlpha(Activity activity, float alpha) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = alpha;
        // 让该window后所有的东西都成暗淡（dim）
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        activity.getWindow().setAttributes(lp);
    }

    public interface OnMyClickListener {
        void sureClickListener(String item);

        void cancelClickListener();
    }

    public interface AddShopCartListener {
        void sureClick(TextView numberText);

        void addClick(TextView numberText);

        void reduceClick(TextView numberText);
    }

}
