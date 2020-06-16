package sy.bishe.ygou.delegate.launch;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;

import java.util.ArrayList;

import sy.bishe.ygou.R;
import sy.bishe.ygou.app.account.AccountManager;
import sy.bishe.ygou.app.account.ILauncherListener;
import sy.bishe.ygou.app.account.IUserChecker;
import sy.bishe.ygou.app.account.OnLauncherTag;
import sy.bishe.ygou.delegate.base.YGouDelegate;
import sy.bishe.ygou.delegate.sign.SigninDelegate;
import sy.bishe.ygou.ui.launcher.LauncherTag;
import sy.bishe.ygou.ui.launcher.launchHolderCreator;
import sy.bishe.ygou.utils.storage.YGouPreferences;

public class LauncherScrollDelegate extends YGouDelegate implements OnItemClickListener {

    private ConvenientBanner<Integer> convenientBanner = null;//图片集合
    private  final ArrayList<Integer> INTEGERS = new ArrayList<>();

    private ILauncherListener launcherListener = null;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (launcherListener instanceof  ILauncherListener){
            launcherListener = (ILauncherListener) activity;
        }
    }

    /**
     * 初始化
     */
    private void initConvenientBanner(){
        INTEGERS.add(R.mipmap.lc_1);
        INTEGERS.add(R.mipmap.lc_2);
        INTEGERS.add(R.mipmap.lc_3);
        INTEGERS.add(R.mipmap.lc_4);
        INTEGERS.add(R.mipmap.icon_app);
        convenientBanner.setPages(new launchHolderCreator(),INTEGERS)
                .setPageIndicator(new int[]{R.drawable.dot_normal,R.drawable.dot_foucs})
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL)
                .setOnItemClickListener(this)
                .setCanLoop(false);
    }
    @Override
    public Object setLayout() {
        convenientBanner = new ConvenientBanner<>(getContext());
        return convenientBanner;
    }

    @Override
    public void OnBindView(@Nullable Bundle bundle, View rootView) {
        initConvenientBanner();
    }

    /**
     * 点击最后一张进入首页
     * @param position
     */
    @Override
    public void onItemClick(int position) {
        if (position == INTEGERS.size() - 1){
            Log.i("position", String.valueOf(position));
            //判断进入过轮滑图表示第一次进入app了
            YGouPreferences.setAppFlag(LauncherTag.FIRST_LAUNCHER_APP.name(),true);
            AccountManager.checkAccount(new IUserChecker() {
                @Override
                public void onSignIn() {
                    Log.i("onSignIn", "onSignIn: ");
                    if (launcherListener != null){
                        launcherListener.onLauncherFinish(OnLauncherTag.SIGNED);                    }
                }
                @Override
                public void offSignIn() {
                    Log.i("offSignIn", "onSignIn: ");
                    if (launcherListener != null){
                        launcherListener.onLauncherFinish(OnLauncherTag.OFF_SIGNED);
                    }
                }
            });
            startWithPop(new SigninDelegate());
        }
    }
}
