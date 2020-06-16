package sy.bishe.ygou.ui.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.alipay.sdk.app.EnvUtils;

import qiu.niorgai.StatusBarCompat;
import sy.bishe.ygou.app.account.ILauncherListener;
import sy.bishe.ygou.app.account.ISignLisenter;
import sy.bishe.ygou.app.account.OnLauncherTag;
import sy.bishe.ygou.app.config.YGou;
import sy.bishe.ygou.base.BaseSupportActivity;
import sy.bishe.ygou.delegate.base.YGouDelegate;
import sy.bishe.ygou.delegate.buttons.ButtonDelegate;
import sy.bishe.ygou.delegate.launch.LaunchDlegate;
import sy.bishe.ygou.delegate.sign.SigninDelegate;
import sy.bishe.ygou.jpush.ExampleUtil;

//实现启动 登陆监听
public class MainActivity extends BaseSupportActivity  implements ISignLisenter, ILauncherListener {
    //jpush 配置
    public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";
    public static boolean isForeground = false;
    //微信
    private MessageReceiver mMessageReceiver;

    private EditText msgText;

    //极光推送接收
    public class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                    String messge = intent.getStringExtra(KEY_MESSAGE);
                    String extras = intent.getStringExtra(KEY_EXTRAS);
                    StringBuilder showMsg = new StringBuilder();
                    showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
                    if (!ExampleUtil.isEmpty(extras)) {
                        showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
                    }
                    setCostomMsg(showMsg.toString());
                }
            } catch (Exception e){
            }
        }
    }

    private void setCostomMsg(String msg){
        if (null != msgText) {
            msgText.setText(msg);
            msgText.setVisibility(android.view.View.VISIBLE);
        }
    }
    //注册
    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, filter);
    }
    @Override
    public YGouDelegate setRootDelegate() {
        return new LaunchDlegate();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        YGou.getConfigurator().withActivity(this);
        EnvUtils.setEnv(EnvUtils.EnvEnum.SANDBOX);
        StatusBarCompat.translucentStatusBar(this,true);


    }
//    private void initContainer(@Nullable Bundle bundle){
//        @SuppressLint("RestrictedApi") final ContentFrameLayout contentFrameLayout = new ContentFrameLayout(this);
//        contentFrameLayout.setId(R.id.delegate_container);
//        setContentView(contentFrameLayout);
//        if (bundle==null){
//          loadRootFragment(R.id.delegate_container,new ButtonDelegate());
//        }
//    }
    /**
     * 登陆过启动页完成
     * @param tag
     */
    @Override
    public void onLauncherFinish(OnLauncherTag tag) {
        switch (tag){
            //登录过 直接进入主页
            case SIGNED:
                getSupportDelegate().start(new ButtonDelegate());
                break;
                //未登录过
            case OFF_SIGNED:
                getSupportDelegate().start(new SigninDelegate());
                break;
                default:
                    break;
        }
    }


    /**
     * 登陆成功
     */
    @Override
    public void onSignInSuccess() {
        getSupportDelegate().start(new ButtonDelegate());
    }

    /**
     * 注册成功
     */
    @Override
    public void onSignUpSucess() {
        Toast.makeText(getApplicationContext(),"注册成功，请登录！",Toast.LENGTH_SHORT).show();
        getSupportDelegate().start(new SigninDelegate());
    }
}
