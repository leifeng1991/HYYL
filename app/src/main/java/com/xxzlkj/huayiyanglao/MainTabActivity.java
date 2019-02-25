package com.xxzlkj.huayiyanglao;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.xxzlkj.huayiyanglao.app.ZhaoLinApplication;
import com.xxzlkj.huayiyanglao.event.JumpToCustomerServiceEvent;
import com.xxzlkj.huayiyanglao.fragment.CustomerServiceFragment;
import com.xxzlkj.huayiyanglao.fragment.HomeFragment;
import com.xxzlkj.huayiyanglao.fragment.MessageFragment;
import com.xxzlkj.huayiyanglao.fragment.MineFragment;
import com.xxzlkj.huayiyanglao.fragment.OrderFragment;
import com.xxzlkj.huayiyanglao.util.RongYunUtils;
import com.xxzlkj.huayiyanglao.util.ZLLooper;
import com.xxzlkj.huayiyanglao.util.ZLUpdateUtils;
import com.xxzlkj.huayiyanglao.weight.ShapeButton;
import com.xxzlkj.zhaolinshare.base.base.BaseActivity;
import com.xxzlkj.zhaolinshare.base.util.PermissionHelper;
import com.xxzlkj.zhaolinshare.base.util.PicassoUtils;
import com.xxzlkj.zhaolinshare.base.util.SystemBarUtils;
import com.xxzlkj.zhaolinshare.base.util.ToastManager;
import com.xxzlkj.zhaolinshare.chat.event.ReceiveNewMessageEvent;
import com.xxzlkj.zhaolinshare.chat.event.RongConnectSuccessEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import io.rong.imlib.RongIMClient;


/**
 * 主页
 *
 * @author zhangrq
 */
public class MainTabActivity extends BaseActivity {

    // 定义数组来存放Fragment界面 首页，订单，客服，消息，我的
    @SuppressWarnings("rawtypes")
    private Class fragmentArray[] = {HomeFragment.class, OrderFragment.class,
            CustomerServiceFragment.class, MessageFragment.class, MineFragment.class};

    // 定义数组来存放按钮图片
    private int mImageViewArray[] = {R.drawable.selector_main_tab_iv1,
            R.drawable.selector_main_tab_iv2,
            R.drawable.selector_main_tab_iv3,
            R.drawable.selector_main_tab_iv4,
            R.drawable.selector_main_tab_iv5};

    // // Tab选项卡的文字
    private String mTextViewArray[] = {"首页", "订单", "客服", "消息", "我的"};

    private FragmentTabHost mTabHost;
    private PermissionHelper permissionHelper;
    private ShapeButton unReadMessageView;
    private ImageView iv_main_center;

    // 中间按钮点击事件处理标记
    public static Intent newIntent(Context context, int currentTab) {
        Intent intent = new Intent(context, MainTabActivity.class);
        intent.putExtra("currentTab", currentTab);
        return intent;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        int index = intent.getIntExtra("currentTab", 0);
        setCurrentTab(index);
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_main_tab);
        SystemBarUtils.setStatusBarTranslucent(this);
        SystemBarUtils.setStatusBarLightMode(this, true);
    }

    @Override
    protected void findViewById() {
        // 实例化TabHost对象，得到TabHost
        mTabHost = (FragmentTabHost) findViewById(R.id.tabHost);
        iv_main_center = (ImageView) findViewById(R.id.iv_main_center);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realTabContent);
        mTabHost.getTabWidget().setDividerDrawable(null);

        // 得到fragment的个数
        int count = fragmentArray.length;
        for (int i = 0; i < count; i++) {
            // 为每一个Tab按钮设置图标、文字和内容
            View tabItemView = getTabItemView(i);
            TabSpec tabSpec = mTabHost.newTabSpec(mTextViewArray[i]).setIndicator(tabItemView);
            // 将Tab按钮添加进Tab选项卡中
            mTabHost.addTab(tabSpec, fragmentArray[i], null);
            // 设置Tab点击事件
            tabItemView.setId(i);
            tabItemView.setOnClickListener(this);// 此事件会消费，原生的点击事件，所以得自己处理
        }

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void setListener() {
        EventBus.getDefault().register(this);
        // 首页中间按钮点击监听
        iv_main_center.setOnTouchListener((v, event) -> {
            Fragment currentFragment = getSupportFragmentManager().findFragmentByTag(mTextViewArray[mTabHost.getCurrentTab()]);
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    // 按下，开始录制
                    if (currentFragment != null && currentFragment instanceof CustomerServiceFragment && !((CustomerServiceFragment) currentFragment).isRecording()) {
                        // 已经在客服页面了，并且没有录制，直接录制
                        ((CustomerServiceFragment) currentFragment).startRecording();
                        iv_main_center.setImageResource(R.mipmap.ic_main_center_talk);
                    } else {
                        // 其它，跳转到客服页面
                        setCurrentTab(2);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    // 抬起，停止录制
                    if (currentFragment != null && currentFragment instanceof CustomerServiceFragment && ((CustomerServiceFragment) currentFragment).isRecording()) {
                        // 已经在客服页面了，并且已经录制，停止录制
                        ((CustomerServiceFragment) currentFragment).stopRecording();
                        iv_main_center.setImageResource(R.mipmap.ic_main_center_normal);
                    } else {
                        // 其它，跳转到客服页面
                        setCurrentTab(2);
                    }

                    break;
            }
            return true;
        });
    }

    @Override
    protected void processLogic() {
        // 初始化应用
        ZhaoLinApplication.getInstance().connectRongIM();// 连接融云
        PicassoUtils.startLooperToken(mContext);// 开始轮训获取image token
        ZLLooper.startLooper(mContext);// 开始轮训发定位
        permissionHelper = new PermissionHelper(this);
        ZLUpdateUtils.checkUpdateInMainThread(this, permissionHelper);// 检查更新
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 设置消息数量
        setTotalUnreadCount();
    }

    private void setTotalUnreadCount() {
        System.out.println("融云获取消息");
        // 设置默认值
        unReadMessageView.setVisibility(View.GONE);
        if (RongYunUtils.checkConnect(mContext))
            RongYunUtils.getTotalUnreadCount(new RongIMClient.ResultCallback<Integer>() {
                @Override
                public void onSuccess(Integer integer) {
                    System.out.println("融云获取消息成功=" + integer);
                    if (integer > 0) {
                        unReadMessageView.setVisibility(View.VISIBLE);
                        unReadMessageView.setText(integer > 99 ? "99+" : integer + "");
                    }
                }

                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {
                    System.out.println("融云获取消息错误=" + errorCode);
                }
            });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] paramArrayOfInt) {
        super.onRequestPermissionsResult(requestCode, permissions, paramArrayOfInt);
        permissionHelper.handleRequestPermissionsResult(requestCode, permissions, paramArrayOfInt);
    }

    @Override
    public void onClick(View v) {
        setCurrentTab(v.getId());// 设置当前位置
    }

    public void setCurrentTab(int index) {
        mTabHost.setCurrentTab(index);// 设置当前位置
    }

    /**
     * 给Tab按钮设置图标和文字
     */
    private View getTabItemView(int index) {
        View view = View.inflate(mContext, R.layout.layout_tab_item_view, null);

        ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
        imageView.setImageResource(mImageViewArray[index]);

        TextView textView = (TextView) view.findViewById(R.id.textview);
        textView.setText(mTextViewArray[index]);
        if (index == 3) {
            // 消息的
            unReadMessageView = view.findViewById(R.id.id_unread_message);
        }
        return view;
    }

    @Override
    public void onBackPressed() {
        exitApp();
    }

    // 数组长度 N 就代表几击事件
    long[] mHits = new long[2];

    // 判断是否退出app
    private void exitApp() {
        // 数组向左移位操作
        System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
        mHits[mHits.length - 1] = SystemClock.uptimeMillis();
        if (mHits[0] >= (SystemClock.uptimeMillis() - 2000)) {// 1000代表多击事件的限定时间
            // 处理多击事件的代码
            this.finish();
        } else {
            ToastManager.showShortToast(mContext, "再按一次退出");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void receiveNewMessage(ReceiveNewMessageEvent event) {
        // 刷新消息数量
        setTotalUnreadCount();
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void rongYunConnectSuccess(RongConnectSuccessEvent event) {
        // 刷新消息数量
        setTotalUnreadCount();
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void jumpTo2(JumpToCustomerServiceEvent event) {
        // 刷新消息数量
        setCurrentTab(2);
    }

    public HomeFragment getHomeFragment() {
        return (HomeFragment) getSupportFragmentManager().findFragmentByTag(mTextViewArray[0]);
    }
}
