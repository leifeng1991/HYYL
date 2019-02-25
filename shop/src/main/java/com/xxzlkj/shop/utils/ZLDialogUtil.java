package com.xxzlkj.shop.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xxzlkj.shop.R;
import com.xxzlkj.shop.adapter.TuanGouTypeAdapter;
import com.xxzlkj.shop.model.GoodsDetail;
import com.xxzlkj.zhaolinshare.base.base.BaseAdapter;
import com.xxzlkj.zhaolinshare.base.util.DialogBuilder;
import com.xxzlkj.zhaolinshare.base.util.PixelUtil;

import java.util.List;


/**
 * 乐行样式弹框
 *
 * @author zhangrq
 */
public class ZLDialogUtil {

    public static AlertDialog showRawDialogOneButton(Activity activity, String message, final OnClickConfirmListener listener) {
        return new AlertDialog.Builder(activity)
                .setTitle("提示")
                .setMessage(message)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (listener != null)
                            listener.onClickConfirm();
                    }
                })
                .show();
    }

    /**
     * 兆邻通用模板弹框
     *
     * @param activity
     * @param imageId       图片id
     * @param title         标题
     * @param content       内容
     * @param clickListener 左中右按钮点击事件
     * @return
     */
    public static Dialog zhaoLinGeneralDialog(Activity activity, int imageId, String title, String content, OnGeneralClickListener clickListener, String... btnTexts) {
        ViewGroup loadDataView = (ViewGroup) View.inflate(activity, R.layout.dialog_general, null);
        // 图片
        ImageView imageView = (ImageView) loadDataView.findViewById(R.id.id_image);
        imageView.setImageResource(imageId);
        // 标题
        TextView titleTextView = (TextView) loadDataView.findViewById(R.id.id_title);
        if (TextUtils.isEmpty(title)) {
            titleTextView.setVisibility(View.GONE);
        } else {
            titleTextView.setVisibility(View.VISIBLE);
            titleTextView.setText(title);
        }
        // 内容
        TextView contentTextView = (TextView) loadDataView.findViewById(R.id.id_content);
        if (TextUtils.isEmpty(content)) {
            contentTextView.setVisibility(View.GONE);
        } else {
            contentTextView.setVisibility(View.VISIBLE);
            contentTextView.setText(content);
        }
        // 左 中 右按钮
        LinearLayout layoutBtn = (LinearLayout) loadDataView.findViewById(R.id.id_btn_layout);
        Button leftBtn = (Button) loadDataView.findViewById(R.id.id_left_btn);
        Button rightBtn = (Button) loadDataView.findViewById(R.id.id_right_btn);
        Button centerBtn = (Button) loadDataView.findViewById(R.id.id_center_btn);
        // 控制按钮的显示
        if (btnTexts != null && btnTexts.length > 1) {
            // 左右两个按钮
            layoutBtn.setVisibility(View.VISIBLE);
            leftBtn.setVisibility(View.VISIBLE);
            rightBtn.setVisibility(View.VISIBLE);
            centerBtn.setVisibility(View.GONE);
            leftBtn.setText(btnTexts[0]);
            rightBtn.setText(btnTexts[1]);
        } else if (btnTexts != null && btnTexts.length > 0) {
            // 中间按钮
            layoutBtn.setVisibility(View.VISIBLE);
            centerBtn.setVisibility(View.VISIBLE);
            leftBtn.setVisibility(View.GONE);
            rightBtn.setVisibility(View.GONE);
            centerBtn.setText(btnTexts[0]);
        } else {
            layoutBtn.setVisibility(View.GONE);
        }

        final DialogBuilder dialogBuilder = DialogBuilder
                .create(activity)
                .setView(loadDataView)
                .setWidthScale(0.72)
                .show();
        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListener != null)
                    clickListener.onLeftButtonClick(dialogBuilder);
            }
        });
        rightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListener != null)
                    clickListener.onRightOrCenterButtonClick(dialogBuilder);
            }
        });
        centerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListener != null)
                    clickListener.onRightOrCenterButtonClick(dialogBuilder);
            }
        });
        dialogBuilder.setCancelable(true);
        return dialogBuilder.getDialog();
    }


    /**
     * 确认订单dialog
     *
     * @param activity
     */
    public static Dialog showOrderDialog(Activity activity, int imagId, String message, final OnDialogButtonClickListener listener) {

        ViewGroup loadDataView = (ViewGroup) View.inflate(activity, R.layout.dialog_custom_dialog, null);
        ImageView imageView = (ImageView) loadDataView.findViewById(R.id.id_cd_image);
        imageView.setImageResource(imagId);
        TextView contentTextView = (TextView) loadDataView.findViewById(R.id.id_cd_content);
        contentTextView.setText(message);
        Button cancelBtn = (Button) loadDataView.findViewById(R.id.id_cd_cancel);
        Button sureBtn = (Button) loadDataView.findViewById(R.id.id_cd_sure);

        // loadDataDialog.setCancelable(false);
        // loadDataDialog.setCanceledOnTouchOutside(false);
        int width = (int) activity.getResources().getDimension(R.dimen.dialog_order_width);
        int height = (int) activity.getResources().getDimension(R.dimen.dialog_order_height);
        final DialogBuilder dialogBuilder = DialogBuilder
                .create(activity)
                .setView(loadDataView)
                .setWidthAndHeight(width, height)
                .show();
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBuilder.dismiss();
                if (listener != null)
                    listener.onLeftClick();
            }
        });
        sureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBuilder.dismiss();
                if (listener != null)
                    listener.onRightClick();
            }
        });
        dialogBuilder.setCancelable(false);
        return dialogBuilder.getDialog();
    }


    /**
     * 团购
     */
    public static Dialog tuanGouDialog(Context context, Activity activity, List<GoodsDetail.DataBean.GroupPonsBean> groupPonsBeans, OnTuanGouItemClickListener listener) {

        DialogBuilder dialogBuilder;
        ViewGroup loadDataView = (ViewGroup) View.inflate(context, R.layout.dialog_tuangou, null);
        ImageView mCloseImageView = loadDataView.findViewById(R.id.id_close);// 关闭对话框
        RecyclerView mRecyclerView = loadDataView.findViewById(R.id.id_tuangou_recycler);// 团购列表
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        TuanGouTypeAdapter mTuanGouTypeAdapter = new TuanGouTypeAdapter(context, R.layout.dialog_tuangou_list_item);
        mRecyclerView.setAdapter(mTuanGouTypeAdapter);
        mTuanGouTypeAdapter.addList(groupPonsBeans);

        dialogBuilder = DialogBuilder.create(activity)
                .setView(loadDataView)
                .setWidthScale(1.0f)
                .setGravity(Gravity.BOTTOM);
        if (groupPonsBeans.size() > 4)
            dialogBuilder.setWidthAndHeight(PixelUtil.getScreenWidth(context), PixelUtil.getScreenHeight(context) / 2);

        dialogBuilder.show();

        // 关闭对话框点击事件
        mCloseImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBuilder.dismiss();
            }
        });
        // item点击事件
        mTuanGouTypeAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener<GoodsDetail.DataBean.GroupPonsBean>() {
            @Override
            public void onClick(int position, GoodsDetail.DataBean.GroupPonsBean item) {
                dialogBuilder.dismiss();
                if (listener != null)
                    listener.onItemClick(item);
            }
        });
        // 开团
        mTuanGouTypeAdapter.setClickOpenMassListener(new TuanGouTypeAdapter.OnClickOpenMassListener() {
            @Override
            public void clicOpenMass(GoodsDetail.DataBean.GroupPonsBean itemBean) {
                dialogBuilder.dismiss();
                if (listener != null)
                    listener.onItemClick(itemBean);
            }
        });

        return dialogBuilder.getDialog();
    }

    public static Dialog showRawDialog(Activity activity, String message, final OnClickConfirmListener listener) {
        return new AlertDialog.Builder(activity)
                .setTitle("提示")
                .setMessage(message)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (listener != null)
                            listener.onClickConfirm();
                    }
                })
                .show();
    }


    /**
     * 完善个人信息提示框
     *
     * @param activity
     * @param listener
     * @return
     */
    public static Dialog showPerfectInfo(Activity activity, final OnDialogButtonClickListener listener) {
        ViewGroup loadDataView = (ViewGroup) View.inflate(activity, R.layout.dialog_perfect_info_dialog, null);

        final DialogBuilder dialogBuilder = DialogBuilder
                .create(activity)
                .setView(loadDataView)
                .show();
        loadDataView.findViewById(R.id.id_cd_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBuilder.dismiss();
                if (listener != null)
                    listener.onLeftClick();
            }
        });
        loadDataView.findViewById(R.id.id_cd_sure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBuilder.dismiss();
                if (listener != null)
                    listener.onRightClick();
            }
        });
        return dialogBuilder.getDialog();
    }

    public interface OnGeneralClickListener {
        void onLeftButtonClick(DialogBuilder dialog);

        void onRightOrCenterButtonClick(DialogBuilder dialog);
    }

    public interface OnClickConfirmListener {
        void onClickConfirm();
    }

    public interface OnDialogButtonClickListener {
        void onLeftClick();

        void onRightClick();
    }

    public interface OnTuanGouItemClickListener {
        void onItemClick(GoodsDetail.DataBean.GroupPonsBean item);
    }

}
