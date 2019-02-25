package com.xxzlkj.huayiyanglao.fragment;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.location.AMapLocationClientOption;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechEvent;
import com.iflytek.cloud.SpeechRecognizer;
import com.xiaomi.mipush.sdk.ErrorCode;
import com.xxzlkj.huayiyanglao.CallActivity;
import com.xxzlkj.huayiyanglao.MainTabActivity;
import com.xxzlkj.huayiyanglao.R;
import com.xxzlkj.huayiyanglao.activity.SearchActivity;
import com.xxzlkj.huayiyanglao.app.ZhaoLinApplication;
import com.xxzlkj.huayiyanglao.config.ZLGlobalParams;
import com.xxzlkj.huayiyanglao.config.ZLURLConstants;
import com.xxzlkj.huayiyanglao.model.CallPhoneBean;
import com.xxzlkj.huayiyanglao.model.CurrentTimeBean;
import com.xxzlkj.huayiyanglao.model.Found;
import com.xxzlkj.huayiyanglao.model.WeatherBean;
import com.xxzlkj.huayiyanglao.util.GaodeLocationUtil;
import com.xxzlkj.huayiyanglao.util.JsonParser;
import com.xxzlkj.huayiyanglao.util.ZLDialogUtil;
import com.xxzlkj.shop.activity.shop.SearchGoodsListActivity;
import com.xxzlkj.shop.config.GlobalParams;
import com.xxzlkj.shop.config.URLConstants;
import com.xxzlkj.speech.WaveLineView;
import com.xxzlkj.zhaolinshare.base.base.BaseActivity;
import com.xxzlkj.zhaolinshare.base.base.ReuseViewFragment;
import com.xxzlkj.zhaolinshare.base.model.BaseBean;
import com.xxzlkj.zhaolinshare.base.model.User;
import com.xxzlkj.zhaolinshare.base.net.OnBaseRequestListener;
import com.xxzlkj.zhaolinshare.base.net.OnMyActivityRequestListener;
import com.xxzlkj.zhaolinshare.base.net.RequestManager;
import com.xxzlkj.zhaolinshare.base.util.CallPhoneUtils;
import com.xxzlkj.zhaolinshare.base.util.DateUtils;
import com.xxzlkj.zhaolinshare.base.util.LogUtil;
import com.xxzlkj.zhaolinshare.base.util.NumberFormatUtils;
import com.xxzlkj.zhaolinshare.base.util.PermissionHelper;
import com.xxzlkj.zhaolinshare.base.util.TextViewUtils;
import com.xxzlkj.zhaolinshare.base.util.ToastManager;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * 描述：客服
 *
 * @author zhangrq
 *         2017/9/2 10:58
 */
public class CustomerServiceFragment extends ReuseViewFragment {

    private TextView tv_city_weather, tv_date, tv_day_num, tv_button_bjdh, tv_button_jjdh, tv_button_hjqr, tv_button_dhkf, tv_button_wlkf, tv_add_contact;
    private SpeechRecognizer mIat;
    private ArrayList<String> mResults = new ArrayList<>();
    private PermissionHelper permissionHelper;
    private ViewGroup vg_button_layout;
    private WaveLineView waveLineView;
    private ImageView iv_bottom_hint;
    private boolean isRecording;
    private CallPhoneBean.DataBean callDataBean;

    @Override
    public View loadViewLayout(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_customer_service, container, false);
    }

    @Override
    protected void findViewById() {
        tv_day_num = getView(R.id.tv_day_num);// 天
        tv_date = getView(R.id.tv_date);// 日期
        tv_city_weather = getView(R.id.tv_city_weather);// 城市天气
        tv_add_contact = getView(R.id.tv_add_contact);// 添加联系人

        vg_button_layout = getView(R.id.vg_button_layout);// 按钮布局
        tv_button_wlkf = getView(R.id.tv_button_wlkf);// 网络客服
        tv_button_dhkf = getView(R.id.tv_button_dhkf);// 电话客服
        tv_button_hjqr = getView(R.id.tv_button_hjqr);// 呼叫亲人
        tv_button_jjdh = getView(R.id.tv_button_jjdh);// 急救电话
        tv_button_bjdh = getView(R.id.tv_button_bjdh);// 报警电话

        waveLineView = getView(R.id.waveLineView);// 波动
        iv_bottom_hint = getView(R.id.iv_bottom_hint);// 波动
        // 初始化语音
        initSpeech();
    }


    @Override
    public void setListener() {
        // 添加联系人
        tv_add_contact.setOnClickListener(v -> startActivity(SearchActivity.newIntent(mContext)));
        // 网络客服
        tv_button_wlkf.setOnClickListener(v -> {
            call(1);
        });
        // 电话客服
        tv_button_dhkf.setOnClickListener(v -> call(2));
        // 呼叫亲人


        tv_button_hjqr.setOnClickListener(v -> call(3));
        // 急救电话
        tv_button_jjdh.setOnClickListener(v -> CallPhoneUtils.callPhoneDialog(mActivity, "120"));
        // 报警电话
        tv_button_bjdh.setOnClickListener(v -> CallPhoneUtils.callPhoneDialog(mActivity, "110"));
    }

    @Override
    public void processLogic() {
        // 获取当前时间
        getCurrentTime();
        // 获取天气
        getWeatherNetData();
    }

    @Override
    public void onResume() {
        super.onResume();
        waveLineView.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();
        waveLineView.onPause();
    }


    /**
     * 初始化语音
     */
    private void initSpeech() {
        // 使用SpeechRecognizer对象，可根据回调消息自定义界面；
        mIat = SpeechRecognizer.createRecognizer(mContext, code -> {
            if (code != ErrorCode.SUCCESS) {
                LogUtil.i("初始化失败，错误码：" + code);
            }
        });
        if (mIat == null) {
            // 创建单例失败，与 21001 错误为同样原因，参考 http://bbs.xfyun.cn/forum.php?mod=viewthread&tid=9688
            LogUtil.i("创建对象失败，请确认 libmsc.so 放置正确，且有调用 createUtility 进行初始化");
            return;
        }
        // 设置听写引擎
        mIat.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
        // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
        mIat.setParameter(SpeechConstant.ASR_PTT, "0");
    }

    /**
     * 开始录制
     */
    public void startRecording() {
        // 设置录音布局显示隐藏
        setRecordViewShow(true);

        permissionHelper = new PermissionHelper(this);
        permissionHelper.requestPermissions("请授予[录音]，[读写]权限，否则无法录音", new PermissionHelper.PermissionListener() {
            @Override
            public void doAfterGrand(String... permission) {
                // 请求权限成功
                if (mIat != null) {
                    // 执行录音
                    int ret = mIat.startListening(mRecognizerListener);
                    if (ret != com.iflytek.cloud.ErrorCode.SUCCESS) {
                        LogUtil.i("听写失败,错误码：" + ret);
                    } else {
                        // 执行动画
                        isRecording = true;
                        waveLineView.startAnim();
                    }
                }
            }

            @Override
            public void doAfterDenied(String... permission) {

            }
        }, Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE);

    }

    /**
     * 停止录制
     */
    public void stopRecording() {
        if (mIat != null && isRecording) {
            // 停止录制
            isRecording = false;
            mIat.stopListening();
            // 停止动画
            waveLineView.stopAnim();
        }
    }

    /**
     * 设置录音状态显示隐藏
     *
     * @param isRecording 是否录音中
     */
    private void setRecordViewShow(boolean isRecording) {
        tv_add_contact.setVisibility(isRecording ? View.GONE : View.VISIBLE);
        vg_button_layout.setVisibility(isRecording ? View.GONE : View.VISIBLE);
        waveLineView.setVisibility(isRecording ? View.VISIBLE : View.GONE);
        iv_bottom_hint.setImageResource(isRecording ? R.mipmap.ic_main_songkai : R.mipmap.ic_main_anzhu);
    }

    /**
     * 听写监听器。
     */
    private RecognizerListener mRecognizerListener = new RecognizerListener() {

        @Override
        public void onBeginOfSpeech() {
            // 此回调表示：sdk内部录音机已经准备好了，用户可以开始语音输入
            LogUtil.i("开始说话");
        }

        @Override
        public void onError(SpeechError error) {
            // 如果使用本地功能（语记）需要提示用户开启语记的录音权限。
            LogUtil.i(error.getPlainDescription(true));
            ToastManager.showShortToast(mContext, error.getPlainDescription(false));
            // 初始化状态
            initState();
        }

        @Override
        public void onEndOfSpeech() {
            // 此回调表示：检测到了语音的尾端点，已经进入识别过程，不再接受语音输入
            LogUtil.i("结束说话");
        }

        @Override
        public void onResult(RecognizerResult results, boolean isLast) {
            // 不停止录制，拼接
            String text = JsonParser.parseIatResult(results.getResultString());
            mResults.add(text);
            LogUtil.i("获的结果" + text + "是否是最后一个" + isLast);
            if (isLast) {
                // 最后一个，停止录制，播放
                if (mResults.size() > 0) {
                    StringBuilder stringBuilder = new StringBuilder();
                    for (String mResult : mResults) {
                        stringBuilder.append(mResult);
                    }
                    // 设置结果
                    if (TextUtils.isEmpty(GlobalParams.storeId)) {
                        // 首页没有获取到小区id，弹框提示
                        ZLDialogUtil.showRawDialog(mActivity, "尚未开启定位权限，无法使用此功能，去设置", () ->
                                ((MainTabActivity) mActivity).getHomeFragment().startAppSettings());
                    } else
                        // 进入到搜索页面
                        startActivity(SearchGoodsListActivity.newIntent(mContext, 1, null, stringBuilder.toString(), GlobalParams.storeId));
                } else {
                    // 没有录制数据，提示
                    ToastManager.showShortToast(mContext, "您好像没有说话哦.");
                }
                // 初始化状态
                initState();

            }
        }

        @Override
        public void onVolumeChanged(int volume, byte[] data) {
            waveLineView.setVolume((int) ((volume / 8.0) * 100));
//            LogUtil.i("当前正在说话，音量大小：" + volume);
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            // 若使用本地能力，会话id为null
            if (SpeechEvent.EVENT_SESSION_ID == eventType) {
                String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
                LogUtil.i("session id =" + sid);
            }
        }

    };

    private void initState() {
        // 状态归原
        isRecording = false;
        // 设置录音布局显示隐藏
        setRecordViewShow(false);
        // 停止动画
        waveLineView.stopAnim();
        // 清空数据
        mResults.clear();
    }

    private void getCurrentTime() {
        HashMap<String, String> stringStringHashMap = new HashMap<>();
        RequestManager.createRequest(ZLURLConstants.GET_TIME_URL, stringStringHashMap, new OnBaseRequestListener<CurrentTimeBean>() {

            @Override
            public void handlerSuccess(CurrentTimeBean bean) {
                String time = bean.getData().getTime();
                if (!TextUtils.isEmpty(time) && !"0".equals(time)) {
                    long currentTime = NumberFormatUtils.toLong(time) * 1000;
                    String week = DateUtils.getDayForWeek(currentTime);
                    String day = DateUtils.getYearMonthDay(currentTime, "dd");
                    String yearMonth = DateUtils.getYearMonthDay(currentTime, "MM/yyyy");
                    // 设置值
                    tv_day_num.setText(day);
                    tv_date.setText(week + "\n" + yearMonth);
                }
            }

            @Override
            public void handlerError(int errorCode, String errorMessage) {

            }
        });
    }

    // 获取天气的
    private void getWeatherNetData() {
        final GaodeLocationUtil gaodeLocationUtil = new GaodeLocationUtil();
        AMapLocationClientOption defaultOption = gaodeLocationUtil.getDefaultOption();
        gaodeLocationUtil.initLocation(mContext, defaultOption, aMapLocation -> {
            double longitude = aMapLocation.getLongitude();
            double latitude = aMapLocation.getLatitude();
            String city = aMapLocation.getCity();
            ZLGlobalParams.aMapLocation = aMapLocation;
            if (longitude == 0 || latitude == 0 || TextUtils.isEmpty(city)) {
                return;// 防止没有权限，不请求
            }
            HashMap<String, String> stringStringHashMap = new HashMap<>();
            //        longitude	经度
            //        latitude	纬度
            //        city	城市名称
            stringStringHashMap.put("longitude", longitude + "");
            stringStringHashMap.put("latitude", latitude + "");
            stringStringHashMap.put("city", city);
            RequestManager.createRequest(ZLURLConstants.WEATHER_URL, stringStringHashMap, new OnMyActivityRequestListener<WeatherBean>((BaseActivity) mActivity) {
                @Override
                public void onSuccess(WeatherBean bean) {
                    // 设置值
                    WeatherBean.DataBean data = bean.getData();
                    // 北京：晴   34°C
                    TextViewUtils.setTextHasValue(tv_city_weather, data.getCity(), "：", data.getText(), "   ", data.getTemp(), "°C");
                }
            });
            // 只定位一次
            gaodeLocationUtil.stopLocation();
            gaodeLocationUtil.destroyLocation();
        });
        gaodeLocationUtil.startLocation();
    }

    private void call(int type) {
        if (callDataBean == null) {
            getCallInfo(type);
        } else {
            switchCall(type);
        }
    }

    /**
     * 网络客服、电话客服、亲人电话
     */
    private void getCallInfo(int type) {
        HashMap<String, String> map = new HashMap<>();
        User loginUser = ZhaoLinApplication.getInstance().getLoginUserDoLogin(mActivity);
        if (loginUser == null) return;

        map.put(URLConstants.REQUEST_PARAM_UID, loginUser.getData().getId());
        RequestManager.createRequest(ZLURLConstants.CALL_INFO_URL,
                map, new OnMyActivityRequestListener<CallPhoneBean>((BaseActivity) mActivity) {
                    @Override
                    public void onSuccess(CallPhoneBean bean) {
                        callDataBean = bean.getData();
                        switchCall(type);
                    }

                });
    }

    private void switchCall(int type) {
        switch (type) {
            case 1:// 网络客服
                // 已经登录
                String netPhone = callDataBean.getNetPhone();
                if (TextUtils.isEmpty(netPhone)) {
                    ToastManager.showMsgToast(mContext, "暂未开放");
                } else {
                    startActivity(CallActivity.newIntentSend(mContext, netPhone));
                }
                break;
            case 2:// 电话客服
                String kfPhone = callDataBean.getKfPhone();
                if (TextUtils.isEmpty(kfPhone)) {
                    ToastManager.showMsgToast(mContext, "暂未开放");
                } else {
                    CallPhoneUtils.callPhoneDialog(mActivity, kfPhone);
                }

                break;
            case 3:// 呼叫亲人
                String famPhone = callDataBean.getFamPhone();
                if (TextUtils.isEmpty(famPhone)) {
                    ToastManager.showMsgToast(mContext, "暂未设置亲情号码");
                } else {
                    CallPhoneUtils.callPhoneDialog(mActivity, famPhone);
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] paramArrayOfInt) {
        super.onRequestPermissionsResult(requestCode, permissions, paramArrayOfInt);
        permissionHelper.handleRequestPermissionsResult(requestCode, permissions, paramArrayOfInt);
    }

    public boolean isRecording() {
        return isRecording;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != mIat) {
            // 退出时释放连接
            mIat.cancel();
            mIat.destroy();
        }

        waveLineView.release();
    }

}
