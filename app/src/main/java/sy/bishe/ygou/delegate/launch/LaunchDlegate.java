package sy.bishe.ygou.delegate.launch;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import java.text.MessageFormat;
import java.util.Timer;

import butterknife.BindView;
import butterknife.OnClick;
import sy.bishe.ygou.R;
import sy.bishe.ygou.R2;
import sy.bishe.ygou.app.account.AccountManager;
import sy.bishe.ygou.app.account.ILauncherListener;
import sy.bishe.ygou.app.account.IUserChecker;
import sy.bishe.ygou.app.account.OnLauncherTag;
import sy.bishe.ygou.delegate.base.YGouDelegate;
import sy.bishe.ygou.ui.launcher.LauncherTag;
import sy.bishe.ygou.utils.storage.YGouPreferences;
import sy.bishe.ygou.utils.timer.BaseTimerTask;
import sy.bishe.ygou.utils.timer.ITimerLisenter;

public class LaunchDlegate extends YGouDelegate implements ITimerLisenter {

    private int count = 5; //五秒
    private Timer timer = null;//定时器
    private ILauncherListener launcherListener = null;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof ILauncherListener){
            launcherListener = (ILauncherListener) activity;
        }
    }

    @BindView(R2.id.launcher_timer)
    AppCompatTextView aTvTimer = null;

    /**
     * 点击图片
     */
    @OnClick(R2.id.launcher_timer)
    void onClickTimer(){
        if(timer != null){
            timer.cancel();
            timer = null;
            checkIsShowScroll();
        }
    }
    private void initTimer(){
        timer = new Timer();
        final BaseTimerTask task = new BaseTimerTask(this);
        timer.schedule(task,0,1000);

    }
    @Override
    public Object setLayout() {
        return R.layout.fragment_launch;
    }
    @Override
    public void OnBindView(@Nullable Bundle bundle, View rootView) {
        initTimer();
    }
    /**
     * 滑动启动页
     *
     */
    private void checkIsShowScroll(){
        //第一次进入App
            if (!YGouPreferences.getAppFlag(LauncherTag.FIRST_LAUNCHER_APP.name())){
                start(new LauncherScrollDelegate(),SINGLETASK);
            }else {
                //检查是否登录或者注册过没有 回activity监听
                AccountManager.checkAccount(new IUserChecker() {
                    @Override
                    public void onSignIn() {
                        //登陆过
                        if (launcherListener != null){
                            launcherListener.onLauncherFinish(OnLauncherTag.SIGNED);
                        }
                    }

                    @Override
                    public void offSignIn() {
                        //注册过
                            if (launcherListener != null){
                                launcherListener.onLauncherFinish(OnLauncherTag.OFF_SIGNED);
                            }
                    }
                });
            }
    }

    /**
     * 时间递减
     */
    @Override
    public void onTimer() {
        getSupportActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(aTvTimer != null){
                    aTvTimer.setText(MessageFormat.format("跳过\n{0}s",count));
                    count--;
                    if (count<0){
                        if (timer != null){
                            timer.cancel();
                            timer = null;
                            checkIsShowScroll();
                        }
                    }
            }
            }
        });
    }
}
