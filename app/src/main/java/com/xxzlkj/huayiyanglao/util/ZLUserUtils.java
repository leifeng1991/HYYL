package com.xxzlkj.huayiyanglao.util;

import com.xxzlkj.huayiyanglao.config.ZLConstants;
import com.xxzlkj.huayiyanglao.config.ZLURLConstants;
import com.xxzlkj.zhaolinshare.base.model.User;
import com.xxzlkj.zhaolinshare.base.net.OnBaseRequestListener;
import com.xxzlkj.zhaolinshare.base.net.RequestManager;

import java.util.HashMap;

/**
 * 描述:
 *
 * @author zhangrq
 *         2017/9/21 10:58
 */
public class ZLUserUtils {

    public static void getUserInfo(String id, OnGetUserInfoListener listener) {
        HashMap<String, String> stringStringHashMap = new HashMap<>();
//        id	会员id(必传)
        stringStringHashMap.put(ZLConstants.Params.ID, id);
        RequestManager.createRequest(ZLURLConstants.REQUEST_USER_INFO, stringStringHashMap, new OnBaseRequestListener<User>() {

            @Override
            public void handlerSuccess(User bean) {
                if (listener != null)
                    listener.onSuccess(bean);
            }

            @Override
            public void handlerError(int errorCode, String errorMessage) {
                if (listener != null)
                    listener.onError(errorCode, errorMessage);
            }
        });
    }

    public interface OnGetUserInfoListener {
        void onSuccess(User bean);

        void onError(int errorCode, String errorMessage);
    }
}
