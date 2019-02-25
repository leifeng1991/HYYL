package com.xxzlkj.huayiyanglao;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.amap.api.services.core.PoiItem;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.lqr.emoji.EmotionKeyboard;
import com.lqr.emoji.EmotionLayout;
import com.lqr.emoji.IEmotionSelectedListener;
import com.xxzlkj.huayiyanglao.activity.address.LocationActivity;
import com.xxzlkj.huayiyanglao.adapter.SessionAdapter;
import com.xxzlkj.huayiyanglao.util.RongYunUtils;
import com.xxzlkj.huayiyanglao.util.ZLUserUtils;
import com.xxzlkj.zhaolinshare.base.base.MyBaseActivity;
import com.xxzlkj.zhaolinshare.base.model.User;
import com.xxzlkj.zhaolinshare.base.util.PermissionHelper;
import com.xxzlkj.zhaolinshare.base.util.PicassoUtils;
import com.xxzlkj.zhaolinshare.base.util.UserUtils;
import com.xxzlkj.zhaolinshare.chat.event.ReceiveNewMessageEvent;
import com.xxzlkj.zhaolinshare.chat.manager.AudioRecordButton;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.IOException;
import java.util.List;

import io.rong.imlib.MessageTag;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.message.TextMessage;
import me.nereo.multi_image_selector.MultiImageSelector;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;
import me.nereo.multi_image_selector.utils.FileUtils;


/**
 * @创建者 CSDN_LQR
 * @描述 会话界面（单聊、群聊）
 */
public class SessionActivity extends MyBaseActivity {

    public static final int REQUEST_IMAGE_PICKER = 1000;
    public final static int REQUEST_TAKE_PHOTO = 1001;
    public final static int REQUEST_MY_LOCATION = 1002;

    public final static int SESSION_TYPE_PRIVATE = 1;
    public final static int SESSION_TYPE_GROUP = 2;
    public final static int REQUEST_IMAGE = 5555;
    public static final String TARGET_ID = "targetId";
    private static final int REQUEST_CAMERA = 100;
    private static final int REQUEST_CODE_LOCATION = 456;

    private String mTargetId = "";
    private boolean mIsFirst = false;
    private Conversation.ConversationType mConversationType = Conversation.ConversationType.PRIVATE;


    private EmotionKeyboard mEmotionKeyboard;
    private LinearLayout mLlRoot;
    private XRecyclerView mRvMsg;
    private RelativeLayout mRlContent;
    private ImageView mIvAudio;
    private AudioRecordButton mBtnAudio;
    private EditText mEtContent;
    private ImageView mIvEmo;
    private ImageView mIvMore;
    private Button mBtnSend;
    private FrameLayout mFlEmotionView;
    private EmotionLayout mElEmotion;
    private LinearLayout mLlMore;
    private RelativeLayout mRlAlbum;
    private RelativeLayout mRlTakePhoto;
    private RelativeLayout mRlLocation;
    private RelativeLayout mRlSpeech;
    private SessionAdapter mAdapter;
    private PermissionHelper mHelper;
    private File mTmpFile;


    @Override
    protected void loadViewLayout() {
        setContentView(com.xxzlkj.zhaolinshare.chat.R.layout.activity_session);
    }

    @Override
    protected void findViewById() {
        mLlRoot = (LinearLayout) findViewById(com.xxzlkj.zhaolinshare.chat.R.id.llRoot);
        mRlContent = (RelativeLayout) findViewById(com.xxzlkj.zhaolinshare.chat.R.id.rlContent);
        mRvMsg = (XRecyclerView) findViewById(com.xxzlkj.zhaolinshare.chat.R.id.rvMsg);
        // 底部的输入布局
        mIvAudio = (ImageView) findViewById(com.xxzlkj.zhaolinshare.chat.R.id.ivAudio);
        mBtnAudio = (AudioRecordButton) findViewById(com.xxzlkj.zhaolinshare.chat.R.id.btnAudio);
        mEtContent = (EditText) findViewById(com.xxzlkj.zhaolinshare.chat.R.id.etContent);
        mIvEmo = (ImageView) findViewById(com.xxzlkj.zhaolinshare.chat.R.id.ivEmo);
        mIvMore = (ImageView) findViewById(com.xxzlkj.zhaolinshare.chat.R.id.ivMore);
        mBtnSend = (Button) findViewById(com.xxzlkj.zhaolinshare.chat.R.id.btnSend);
        // 底部的表情布局
        mFlEmotionView = (FrameLayout) findViewById(com.xxzlkj.zhaolinshare.chat.R.id.flEmotionView);
        mElEmotion = (EmotionLayout) findViewById(com.xxzlkj.zhaolinshare.chat.R.id.elEmotion);
        mLlMore = (LinearLayout) findViewById(com.xxzlkj.zhaolinshare.chat.R.id.llMore);

        // 相册
        mRlAlbum = (RelativeLayout) findViewById(com.xxzlkj.zhaolinshare.chat.R.id.rlAlbum);
        // 拍摄
        mRlTakePhoto = (RelativeLayout) findViewById(com.xxzlkj.zhaolinshare.chat.R.id.rlTakePhoto);
        // 位置
        mRlLocation = (RelativeLayout) findViewById(com.xxzlkj.zhaolinshare.chat.R.id.rlLocation);
        // 语音
        mRlSpeech = (RelativeLayout) findViewById(com.xxzlkj.zhaolinshare.chat.R.id.rlSpeech);

        // 初始化 ElEmotion
        mElEmotion.attachEditText(mEtContent);
        initEmotionKeyboard();
    }

    @Override
    protected void setListener() {
        EventBus.getDefault().register(this);
        // 表情的监听
        mElEmotion.setEmotionSelectedListener(new IEmotionSelectedListener() {

            @Override
            public void onEmojiSelected(String key) {
                // 表情选中，已经发送了
            }

            @Override
            public void onStickerSelected(String categoryName, String stickerName, String stickerBitmapPath) {
                // 纸贴选中，发送
                RongYunUtils.sendFileMsg(mConversationType, mTargetId, new File(stickerBitmapPath), mRvMsg, mAdapter);
            }
        });
        // 点击内容隐藏底部和键盘
        mRlContent.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    closeBottomAndKeyboard();
                    break;
            }
            return false;
        });
        // 点击对话列表隐藏底部和键盘
        mRvMsg.setOnTouchListener((v, event) -> {
            closeBottomAndKeyboard();
            return false;
        });
        // 点击左边的声音，切换左边显示、语音输入或文字输入
        mIvAudio.setOnClickListener(v -> {
            if (mBtnAudio.isShown()) {
                // 语音按钮显示，隐藏语音按钮，显示输入框，输入框获取到焦点，左边声音图片切换，隐藏键盘
                hideAudioButton();
                mEtContent.requestFocus();
                if (mEmotionKeyboard != null) {
                    mEmotionKeyboard.showSoftInput();
                }
            } else {
                // 语音按钮没显示，显示语音按钮，隐藏输入框，输入框失去焦点，左边声音图片切换，隐藏键盘，隐藏表情布局，隐藏更多布局
                mEtContent.clearFocus();
                showAudioButton();
                hideEmotionLayout();
                hideMoreLayout();
            }
            RongYunUtils.rvMoveToBottom(mRvMsg);
        });
        // 输入内容文本改变监听
        mEtContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mEtContent.getText().toString().trim().length() > 0) {
                    mBtnSend.setVisibility(View.VISIBLE);
                    mIvMore.setVisibility(View.GONE);
                    RongIMClient.getInstance().sendTypingStatus(mConversationType, mTargetId, TextMessage.class.getAnnotation(MessageTag.class).value());
                } else {
                    mBtnSend.setVisibility(View.GONE);
                    mIvMore.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mEtContent.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                RongYunUtils.rvMoveToBottom(mRvMsg);
            }
        });
        // 发送按钮
        mBtnSend.setOnClickListener(v -> {
            RongYunUtils.sendTextMsg(mConversationType, mTargetId, mEtContent.getText().toString(), mRvMsg, mAdapter);
            mEtContent.setText("");
        });
        // 相册
        mRlAlbum.setOnClickListener(v -> {
            // 请求权限
            mHelper = new PermissionHelper(SessionActivity.this);
            mHelper.requestPermissions("请授予[读写][拍照]权限，否则无法拍照和读取本地图片", new PermissionHelper.PermissionListener() {
                @Override
                public void doAfterGrand(String... permission) {
                    // 请求权限成功
                    MultiImageSelector.create()
                            .showCamera(true) // show camera or not. true by default
                            .single() // multi mode, default mode;
                            .start(SessionActivity.this, REQUEST_IMAGE);
                }

                @Override
                public void doAfterDenied(String... permission) {

                }
            }, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA);

        });
        // 拍照
        mRlTakePhoto.setOnClickListener(v -> {
            // 请求权限
            mHelper = new PermissionHelper(SessionActivity.this);
            mHelper.requestPermissions("请授予[读写][拍照]权限，否则无法拍照和存储图片", new PermissionHelper.PermissionListener() {
                @Override
                public void doAfterGrand(String... permission) {
                    // 请求权限成功
                    // 调用的是MultiImageSelectorFragment的方法
                    takePhoto();
                }

                @Override
                public void doAfterDenied(String... permission) {

                }
            }, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA);

        });
        // 位置
        mRlLocation.setOnClickListener(v -> startActivityForResult(LocationActivity.newIntent(mContext), REQUEST_CODE_LOCATION));
        // 语音聊天
        mRlSpeech.setOnClickListener(v -> startActivity(CallActivity.newIntentSend(mContext, mTargetId)));
        // 刷新监听
        mRvMsg.setPullRefreshEnabled(true);
        mRvMsg.setLoadingMoreEnabled(false);
        mRvMsg.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                // 获取所有
                RongYunUtils.getLocalHistoryMessage(mConversationType, mTargetId, mRvMsg, mAdapter);
            }

            @Override
            public void onLoadMore() {

            }
        });

        // 语音授权
        mBtnAudio.setCanRecord(false);
        // 授权处理
        mHelper = new PermissionHelper(this);
        mHelper.requestPermissions("请授予[录音]，[读写]权限，否则无法录音", new PermissionHelper.PermissionListener() {
            @Override
            public void doAfterGrand(String... permission) {
                // 请求权限成功
                requestPermissionsSucceed();
            }

            @Override
            public void doAfterDenied(String... permission) {
                mBtnAudio.setCanRecord(false);
            }
        }, Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    /**
     * 拍照，调用的是MultiImageSelectorFragment的方法
     */
    private void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(mContext.getPackageManager()) != null) {
            try {
                mTmpFile = FileUtils.createTmpFile(mContext);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (mTmpFile != null && mTmpFile.exists()) {
                Uri imageUri = FileProvider.getUriForFile(mContext, mContext.getPackageName() + ".mis.fileprovider", mTmpFile);//通过FileProvider创建一个content类型的Uri
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, REQUEST_CAMERA);
            } else {
                Toast.makeText(mContext, R.string.mis_error_image_not_exist, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(mContext, R.string.mis_msg_no_camera, Toast.LENGTH_SHORT).show();
        }
    }

    private void requestPermissionsSucceed() {
        mBtnAudio.setCanRecord(true);
        mBtnAudio.setAudioFinishRecorderListener(new AudioRecordButton.AudioFinishRecorderListener() {
            @Override
            public void onFinished(float seconds, String filePath) {
                // 发送语音
                RongYunUtils.sendAudioFile(mConversationType, mTargetId, Uri.fromFile(new File(filePath)), (int) seconds, mRvMsg, mAdapter);
            }
        });
    }

    //直接把参数交给mHelper就行了
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mHelper.handleRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 选择图片
        if (requestCode == REQUEST_IMAGE && resultCode == RESULT_OK) {
            // 选择的图片
            List<String> paths = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
            String imagePath = paths.get(0);//选择的图片
            // 发送图片
            RongYunUtils.sendImgMsg(Conversation.ConversationType.PRIVATE, mTargetId,
                    Uri.fromFile(new File(imagePath)), Uri.fromFile(new File(imagePath)), mRvMsg, mAdapter);
        }
        // 拍照
        if (requestCode == REQUEST_CAMERA) {
            if (resultCode == Activity.RESULT_OK) {
                if (mTmpFile != null) {
                    // 发送图片
                    RongYunUtils.sendImgMsg(Conversation.ConversationType.PRIVATE, mTargetId,
                            Uri.fromFile(mTmpFile), Uri.fromFile(mTmpFile), mRvMsg, mAdapter);
                }
            } else {
                // delete tmp file
                while (mTmpFile != null && mTmpFile.exists()) {
                    boolean success = mTmpFile.delete();
                    if (success) {
                        mTmpFile = null;
                    }
                }
            }
        }
        // 定位
        PoiItem poiItem = LocationActivity.getResult(data);
        if (requestCode == REQUEST_CODE_LOCATION && poiItem != null) {
            // 定位获取到的
            double longitude = poiItem.getLatLonPoint().getLongitude();
            double latitude = poiItem.getLatLonPoint().getLatitude();
            // 发送定位消息
            RongYunUtils.sendLocationMessage(Conversation.ConversationType.PRIVATE, mTargetId,
                    latitude, longitude, poiItem.getTitle(), mRvMsg, mAdapter);
        }
    }

    @Override
    protected void processLogic() {
        setTitleLeftBack();
        setTitleName("聊天");
        Intent intent = getIntent();
        mTargetId = intent.getStringExtra(TARGET_ID);
        if (mTargetId == null) {
            // 推送过来的
            mTargetId = intent.getData().getQueryParameter("targetId");
        }

        // 获取融云的TargtId
        mTargetId = UserUtils.getUserID(mTargetId, true);

        if (!RongYunUtils.checkConnect(mContext)) {
            // 融云没链接成功，销毁当前页面
            finish();
            return;
        }

        //设置会话已读
        RongYunUtils.setMessageIsRead(mConversationType, mTargetId);

        mAdapter = new SessionAdapter(mContext, 0, mTargetId, mRvMsg);
        mRvMsg.setLayoutManager(new LinearLayoutManager(mContext));
        mRvMsg.setAdapter(mAdapter);

        // 获取所有
        RongYunUtils.getLocalHistoryMessage(mConversationType, mTargetId, mRvMsg, mAdapter);
        // 设置用户信息
        setUserInfoByNet(false, UserUtils.getUserID(mTargetId, false));
        User loginUser = UserUtils.getLoginUser(mContext);
        if (loginUser != null) {
            setUserInfoByNet(true, loginUser.getData().getId());
        }

    }

    /**
     * 设置用户信息
     */
    private void setUserInfoByNet(boolean isMyUserInfo, String userId) {

        ZLUserUtils.getUserInfo(userId, new ZLUserUtils.OnGetUserInfoListener() {
            @Override
            public void onSuccess(User bean) {
                // 获取签名
                PicassoUtils.getImageSignatureInMainThread(mContext, new PicassoUtils.OnGetImageSignatureListener() {
                    @Override
                    public void onSuccess(String sign) {
                        User.DataBean data = bean.getData();
                        if (TextUtils.isEmpty(data.getId())) {
                            // 没有此用户
                            return;
                        }
                        io.rong.imlib.model.UserInfo userInfo = new io.rong.imlib.model.UserInfo(data.getId(), data.getUsername(),
                                Uri.parse(data.getSimg() + PicassoUtils.SIGN_SMALL + sign));
                        if (isMyUserInfo) {
                            mAdapter.setMyUserInfo(userInfo);
                        } else
                            mAdapter.setOtherUserInfo(userInfo);
                        mAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(int errorCode, String errorMsg) {

                    }
                });
            }

            @Override
            public void onError(int errorCode, String errorMessage) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!mIsFirst) {
            mEtContent.clearFocus();
        } else {
            mIsFirst = false;
        }
        // 重置草稿
        RongYunUtils.resetDraft(mConversationType, mTargetId, mEtContent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 保存草稿
        RongYunUtils.saveDraft(mConversationType, mTargetId, mEtContent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true) //在ui线程执行
    public void receiveNewMessage(ReceiveNewMessageEvent event) {
        Message message = event.getMessage();
        if (message != null && mAdapter != null) {
            RongYunUtils.receiveNewMessage(mConversationType, mTargetId, message, mRvMsg, mAdapter);
        }
    }


    private void initEmotionKeyboard() {
        mEmotionKeyboard = EmotionKeyboard.with(this);
        mEmotionKeyboard.bindToEditText(mEtContent);
        mEmotionKeyboard.bindToContent(mRlContent);
        mEmotionKeyboard.setEmotionLayout(mFlEmotionView);
        mEmotionKeyboard.bindToEmotionButton(mIvEmo, mIvMore);
        mEmotionKeyboard.setOnEmotionButtonOnClickListener(new EmotionKeyboard.OnEmotionButtonOnClickListener() {
            @Override
            public boolean onEmotionButtonOnClickListener(View view) {
                if (view.getId() == com.xxzlkj.zhaolinshare.chat.R.id.ivEmo) {
                    // 点击的表情
                    RongYunUtils.rvMoveToBottom(mRvMsg);
                    mEtContent.clearFocus();
                    if (!mElEmotion.isShown()) {
                        if (mLlMore.isShown()) {
                            showEmotionLayout();
                            hideMoreLayout();
                            hideAudioButton();
                            return true;
                        }
                    } else if (mElEmotion.isShown() && !mLlMore.isShown()) {
                        mIvEmo.setImageResource(com.xxzlkj.zhaolinshare.chat.R.mipmap.ic_chat_right_face);
                        return false;
                    }
                    showEmotionLayout();
                    hideMoreLayout();
                    hideAudioButton();
                } else if (view.getId() == com.xxzlkj.zhaolinshare.chat.R.id.ivMore) {
                    // 点击的更多
                    RongYunUtils.rvMoveToBottom(mRvMsg);
                    mEtContent.clearFocus();
                    if (!mLlMore.isShown()) {
                        if (mElEmotion.isShown()) {
                            showMoreLayout();
                            hideEmotionLayout();
                            hideAudioButton();
                            return true;
                        }
                    }
                    showMoreLayout();
                    hideEmotionLayout();
                    hideAudioButton();
                }
                return false;
            }
        });
    }

    private void showAudioButton() {
        mBtnAudio.setVisibility(View.VISIBLE);
        mEtContent.setVisibility(View.GONE);
        mIvAudio.setImageResource(com.xxzlkj.zhaolinshare.chat.R.mipmap.ic_chat_left_input);

        if (mFlEmotionView.isShown()) {
            if (mEmotionKeyboard != null) {
                mEmotionKeyboard.interceptBackPress();
            }
        } else {
            if (mEmotionKeyboard != null) {
                mEmotionKeyboard.hideSoftInput();
            }
        }
    }

    private void hideAudioButton() {
        mBtnAudio.setVisibility(View.GONE);
        mEtContent.setVisibility(View.VISIBLE);
        mIvAudio.setImageResource(com.xxzlkj.zhaolinshare.chat.R.mipmap.ic_chat_left_voice);
    }

    private void showEmotionLayout() {
        mElEmotion.setVisibility(View.VISIBLE);
        mIvEmo.setImageResource(com.xxzlkj.zhaolinshare.chat.R.mipmap.ic_chat_left_input);
    }

    private void hideEmotionLayout() {
        mElEmotion.setVisibility(View.GONE);
        mIvEmo.setImageResource(com.xxzlkj.zhaolinshare.chat.R.mipmap.ic_chat_right_face);
    }

    private void showMoreLayout() {
        mLlMore.setVisibility(View.VISIBLE);
    }

    private void hideMoreLayout() {
        mLlMore.setVisibility(View.GONE);
    }

    private void closeBottomAndKeyboard() {
        mElEmotion.setVisibility(View.GONE);// 表情布局隐藏
        mLlMore.setVisibility(View.GONE);// 更多布局隐藏
        if (mEmotionKeyboard != null) {
            mEmotionKeyboard.interceptBackPress();// 隐藏表情布局
            mIvEmo.setImageResource(com.xxzlkj.zhaolinshare.chat.R.mipmap.ic_chat_right_face);// 设置表情为笑脸
        }
    }

    /**
     * @param targetId （必传）目标id，这里面处理了，是获取融云的UserId，所有传入的时候不用处理了
     */
    public static Intent newIntent(Context context, String targetId) {
        Intent intent = new Intent(context, SessionActivity.class);
        intent.putExtra(TARGET_ID, targetId);
        return intent;
    }

    @Override
    public void onBackPressed() {
        if (mElEmotion.isShown() || mLlMore.isShown()) {
            mEmotionKeyboard.interceptBackPress();
            mIvEmo.setImageResource(com.xxzlkj.zhaolinshare.chat.R.mipmap.ic_chat_right_face);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
