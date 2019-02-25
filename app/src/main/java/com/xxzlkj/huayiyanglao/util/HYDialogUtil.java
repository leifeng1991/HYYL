package com.xxzlkj.huayiyanglao.util;

import android.view.View;

import com.bigkoo.pickerview.OptionsPickerView;
import com.xxzlkj.huayiyanglao.config.ZLURLConstants;
import com.xxzlkj.shop.model.CancelOrderGroupBean;
import com.xxzlkj.zhaolinshare.base.base.BaseActivity;
import com.xxzlkj.zhaolinshare.base.model.BaseBean;
import com.xxzlkj.zhaolinshare.base.net.OnMyActivityRequestListener;
import com.xxzlkj.zhaolinshare.base.net.RequestManager;

import java.util.HashMap;
import java.util.List;


/**
 * 描述:
 *
 * @author leifeng
 *         2017/11/25 14:31
 */


public class HYDialogUtil {

    /**
     * 取消订单接口--- 带弹框
     */
    public static void submitCancelOrderHasDialog(final BaseActivity activity, final String orderId, final OnSuccessListener onSuccessListener) {
        HashMap<String, String> stringStringHashMap = new HashMap<>();
        RequestManager.createRequest(ZLURLConstants.FOODS_CANCEL_ORDER_GROUP_URL, stringStringHashMap, new OnMyActivityRequestListener<CancelOrderGroupBean>(activity) {

            @Override
            public void onSuccess(CancelOrderGroupBean bean) {
                List<String> group = bean.getData().getGroup();
                if (group != null){
                    OptionsPickerView picker = new OptionsPickerView.Builder(activity, new OptionsPickerView.OnOptionsSelectListener() {
                        @Override
                        public void onOptionsSelect(int options1, int option2, int options3, View v) {
                            HYNetRequestUtil.submitCancelOrder(activity, orderId, group.get(options1), new HYNetRequestUtil.OnSuccessListener() {
                                @Override
                                public void RequestSuccess(BaseBean bean) {
                                    if (onSuccessListener != null)
                                        onSuccessListener.onSuccess();
                                }
                            });
                        }
                    })
                            .setLineSpacingMultiplier(2.0f)
                            .setDividerColor(0xffc6c9cf)
                            .setTitleSize(14)
                            .setBgColor(0xffe1e1e1)
                            .setTitleBgColor(0xfff5f5f5)
                            .setTitleText("请选择原因")
                            .setContentTextSize(16)
                            .build();
                    picker.setPicker(group);
                    picker.show();

                }

            }
        });
    }

    public interface OnSuccessListener {
        void onSuccess();
    }
}
